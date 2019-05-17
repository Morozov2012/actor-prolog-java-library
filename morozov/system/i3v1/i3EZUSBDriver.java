// The Actor Prolog project, (c) 2018 IRE RAS, Alexei A. Morozov
//
// Thanks to the usb4java team (http://usb4java.org/):
// Klaus Reimer, Luca Longinotti.
// Thanks to the libusb team (http://libusb.info/):
// Johannes Erdfelt, Daniel Drake, Peter Stuge, Nathan Hjelm,
// Pete Batard, Ludovic Rousseau, Michael Plante, Hans de Goede,
// Martin Pieuchot, Toby Gray.
//

package morozov.system.i3v1;

import morozov.system.*;
import morozov.system.i3v1.errors.*;

import org.usb4java.ConfigDescriptor;
import org.usb4java.Context;
import org.usb4java.Device;
import org.usb4java.DeviceHandle;
import org.usb4java.DeviceDescriptor;
import org.usb4java.DeviceList;
import org.usb4java.EndpointDescriptor;
import org.usb4java.Interface;
import org.usb4java.InterfaceDescriptor;
import org.usb4java.LibUsb;

import javax.swing.SwingUtilities;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.ArrayList;

public class i3EZUSBDriver {
	//
	protected static final int MAX_BYTES_PER_PACKET= 16384;
	protected static final int MESSAGE_DEVICE_ATTACHED= 6;
	protected static final int MESSAGE_DEVICE_DEATTACHED= 3;
	protected static final int MESSAGE_ERROR= 4;
	protected static final int MESSAGE_PERMISSION_DENIED= 2;
	protected static final int MESSAGE_PERMISSION_GRANTED= 1;
	protected static final int MESSAGE_READ_TIME= 5;
	protected static final int PACKET_TIMES= 512;
	//
	protected static final int VENDOR_ID= 1351;
	protected static final int PRODUCT_ID= 0x00000080;
	protected Context context;
	protected int kernelDriverIsAttached;
	//
	protected DeviceHandle handle; // = new DeviceHandle();
	protected Device m_USBDevice;
	protected EndpointDescriptor m_USBRecvEndPoint;
	protected EndpointDescriptor m_USBSendEndPoint;
	protected byte m_USBRecvEndPointAddress;
	protected byte m_USBSendEndPointAddress;
	protected int m_VenBufLength= 8;
	protected byte[] m_VenBuf;
	protected ByteBuffer m_VenByteBuf;
	//
	protected AtomicBoolean m_bOpen= new AtomicBoolean(false);
	protected DeviceIdentifier currentDeviceIdentifier;
	protected int m_iMaxPacketSize;
	protected AtomicInteger m_iReadTimeOut= new AtomicInteger(70); // 50; // 15;
	protected AtomicInteger m_iWriteTimeOut= new AtomicInteger(70); // 50; // 500; // 50;
	protected AtomicInteger m_iOutputDebugInformation= new AtomicInteger(0);
	// protected AtomicBoolean reportUSBTransferDelays= new AtomicBoolean(false);
	//
	protected int maximalNumberOfTailReadingFailures= 50; // 10; // 50; // 500; // 100; 1000;
	//
	protected static int reportCriticalErrorsLevel= 1;
	protected static int reportAdmissibleErrorsLevel= 2;
	protected static int reportWarningsLevel= 3;
	protected static int reportFlashAttributesLevel= 4;
	protected static int reportUSBTransferDelaysLevel= 5;
	//
	///////////////////////////////////////////////////////////////
	//
	public i3EZUSBDriver() {
		m_VenBuf= new byte[m_VenBufLength];
		m_VenBuf[0]= (byte) -80;
		m_VenBuf[MESSAGE_PERMISSION_GRANTED]= (byte) 71;
		m_VenBuf[MESSAGE_PERMISSION_DENIED]= (byte) 5;
		m_VenBuf[MESSAGE_DEVICE_DEATTACHED]= Byte.MIN_VALUE;
		m_VenBuf[MESSAGE_ERROR]= (byte) 0;
		m_VenBuf[MESSAGE_READ_TIME]= (byte) 1;
		m_VenBuf[MESSAGE_DEVICE_ATTACHED]= (byte) 0;
		m_VenByteBuf= ByteBuffer.allocateDirect(m_VenBufLength);
		m_VenByteBuf.put(m_VenBuf);
		m_VenByteBuf.flip();
		initializeLibUSB();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean isConnected() {
		return m_bOpen.get();
	}
	public DeviceIdentifier getIdentifier() {
		return currentDeviceIdentifier;
	}
	//
	public void setReadTimeOut(int value) {
		m_iReadTimeOut.set(value);
	}
	public int getReadTimeOut() {
		return m_iReadTimeOut.get();
	}
	//
	public void setWriteTimeOut(int value) {
		m_iWriteTimeOut.set(value);
	}
	public int getWriteTimeOut() {
		return m_iWriteTimeOut.get();
	}
	//
	public void setOutputDebugInformation(int value) {
		m_iOutputDebugInformation.set(value);
	}
	public int getOutputDebugInformation() {
		return m_iOutputDebugInformation.get();
	}
	//
	public boolean reportCriticalErrors() {
		return m_iOutputDebugInformation.get() >= reportCriticalErrorsLevel;
	}
	public boolean reportAdmissibleErrors() {
		return m_iOutputDebugInformation.get() >= reportAdmissibleErrorsLevel;
	}
	public boolean reportWarnings() {
		return m_iOutputDebugInformation.get() >= reportWarningsLevel;
	}
	public boolean reportFlashAttributes() {
		return m_iOutputDebugInformation.get() >= reportFlashAttributesLevel;
	}
	public boolean reportUSBTransferDelays() {
		return m_iOutputDebugInformation.get() >= reportUSBTransferDelaysLevel;
	}
	//
	public int getReportCriticalErrorsLevel() {
		return reportCriticalErrorsLevel;
	}
	public int getReportAdmissibleErrorsLevel() {
		return reportAdmissibleErrorsLevel;
	}
	public int getReportWarningsLevel() {
		return reportWarningsLevel;
	}
	public int getReportFlashAttributesLevel() {
		return reportFlashAttributesLevel;
	}
	public int getReportUSBTransferDelaysLevel() {
		return reportUSBTransferDelaysLevel;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void initializeLibUSB() {
		// Create the libusb context:
		// Context
		context= new Context();
		// Initialize the libusb context:
		// init:
		// Initialize libusb.
		// This function must be called before
		// calling any other libusb function.
		// If you do not provide an output location
		// for a Context, a default context will be
		// created. If there was already a default
		// context, it will be reused (and nothing will
		// be initialized/reinitialized).
		// @param context - Optional output location
		// for context pointer. Null to use default
		// context. Only valid on return code 0.
		// @return 0 on success or a error code
		// on failure.
		int result1= LibUsb.init(context);
		// SUCCESS:
		// Success (no error).
		// int SUCCESS= 0;
		// Error codes. Most libusb functions return 0
		// on success or one of these codes on failure.
		// You can call errorName() to retrieve a string
		// representation of an error code.
		if (result1 != LibUsb.SUCCESS) {
			if (reportCriticalErrors()) {
				writeLater(String.format("Unable to initialize libusb: %s\n",result1));
			};
			throw new UnableToInitializeLibUSB(result1);
		};
		// setDebug:
		// Set log message verbosity.
		// The default level is LOG_LEVEL_NONE, which means
		// no messages are ever printed. If you choose to
		// increase the message verbosity level, ensure that
		// your application does not close the stdout/stderr
		// file descriptors.
		// You are advised to use level LOG_LEVEL_WARNING.
		// libusb is conservative with its message logging
		// and most of the time, will only log messages that
		// explain error conditions and other oddities. This
		// will help you debug your software.
		// If the LOG_LEVEL_DEBUG environment variable was
		// set when libusb was initialized, this function
		// does nothing: the message verbosity is fixed to
		// the value in the environment variable.
		// If libusb was compiled without any message logging,
		// this function does nothing: you'll never get any
		// messages.
		// If libusb was compiled with verbose debug message
		// logging, this function does nothing: you'll always
		// get messages from all levels.
		// context - The Context to operate on, or NULL for
		// the default context.
		// level - The log level to set.
		// LibUsb.setDebug(context,LibUsb.LOG_LEVEL_DEBUG);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public String[] findDevices() {
		DeviceList list= new DeviceList();
		int result= LibUsb.getDeviceList(context,list);
		if (result < 0) {
			throw new UnableToGetDeviceList(result);
		};
		// Iterate over all devices and scan
		// for the Thermal Expert devices.
		ArrayList<String> deviceIdentifiers= new ArrayList<>();
		for (Device device: list) {
			DeviceDescriptor descriptor= new DeviceDescriptor();
			result= LibUsb.getDeviceDescriptor(device,descriptor);
			if (result < 0) {
				throw new UnableToReadDeviceDescriptor(result);
			};
			if (descriptor.idVendor() == VENDOR_ID && descriptor.idProduct() == PRODUCT_ID) {
				String identifier= createIdentifier(device,descriptor);
				deviceIdentifiers.add(identifier);
			}
		};
		return deviceIdentifiers.toArray(new String[deviceIdentifiers.size()]);
	}
	//
	protected static String createIdentifier(Device device, DeviceDescriptor descriptor) {
		int busNumber= LibUsb.getBusNumber(device);
		int port= LibUsb.getPortNumber(device);
		int address= LibUsb.getDeviceAddress(device);
		int speed= LibUsb.getDeviceSpeed(device);
		String identifier= String.format(
			"%04X-%04X-%02X-%02X-%02X-%04X-%04X-%04X-%04X",
			descriptor.idVendor() & 0xFFFF,
			descriptor.idProduct() & 0xFFFF,
			descriptor.bDeviceClass() & 0xFF,
			descriptor.bDeviceSubClass() & 0xFF,
			descriptor.bDeviceProtocol() & 0xFF,
			busNumber & 0xFFFF,
			port & 0xFFFF,
			address & 0xFFFF,
			speed & 0xFFFF);
		return identifier;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean findDevice(DeviceIdentifier identifier) {
		if (m_bOpen.get()) {
			return true;
		} else {
			m_USBDevice= findTheThermalExpertDevice(identifier);
			if (m_USBDevice == null) {
				return false;
			};
			return true;
		}
	}
	public boolean findDevice() {
		if (m_bOpen.get()) {
			return true;
		} else {
			m_USBDevice= findTheThermalExpertDevice(DeviceIdentifier.DEFAULT);
			if (m_USBDevice == null) {
				return false;
			};
			return true;
		}
	}
	//
	// Searches for the Thermal Expert device and returns it.
	// If there are multiple Thermal Expert devices attached then
	// this procedure returns the first one.
	// @return The Thermal Expert USB device or null if not found.
	//
	protected Device findTheThermalExpertDevice(DeviceIdentifier targetIdentifier) {
		// Read the USB device list
		DeviceList list= new DeviceList();
		// getDeviceList:
		// Returns a list of USB devices currently
		// attached to the system.
		// This is your entry point into finding
		// a USB device to operate.
		// You are expected to unreference all the
		// devices when you are done with them, and
		// then free the list with
		// freeDeviceList(DeviceList,boolean). Note
		// that freeDeviceList(DeviceList,boolean)
		// can unref all the devices for you. Be
		// careful not to unreference a device you
		// are about to open until after you have
		// opened it.
		// @param context - The context to operate on,
		// or NULL for the default context.
		// @param list - Output location for a list
		// of devices. Must be later freed with
		// freeDeviceList(DeviceList,boolean).
		// @return - The number of devices in the
		// outputted list, or any ERROR code
		// according to errors encountered by the
		// backend.
		int result= LibUsb.getDeviceList(context,list);
		if (result < 0) {
			throw new UnableToGetDeviceList(result);
		};
		try {
			// Iterate over all devices and scan
			// for the Thermal Expert device.
			for (Device device: list) {
				DeviceDescriptor descriptor= new DeviceDescriptor();
				result= LibUsb.getDeviceDescriptor(device,descriptor);
				if (result < 0) {
					throw new UnableToReadDeviceDescriptor(result);
				};
				if (descriptor.idVendor() == VENDOR_ID && descriptor.idProduct() == PRODUCT_ID) {
					if (targetIdentifier==null || targetIdentifier.isDefault()) {
						return device;
					} else {
						String currentIdentifier= createIdentifier(device,descriptor);
						if (targetIdentifier.hasName(currentIdentifier)) {
							return device;
						}
					}
				}
			}
		} finally {
			// Ensure the allocated device list is freed:
			// freeDeviceList:
			// Frees a list of devices previously
			// discovered using
			// getDeviceList(Context,DeviceList).
			// If the unref_devices parameter is set,
			// the reference count of each device
			// in the list is decremented by 1.
			// @param list - The list to free.
			// @param unrefDevices - Whether to unref
			// the devices in the list.
			// A.M.: LibUsb.freeDeviceList(list,true);
		};
		// No Thermal Expert USB device found:
		return null;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean connectDevice(DeviceIdentifier identifier) {
		m_bOpen.set(false);
		currentDeviceIdentifier= null;
		connectTheThermalExpertDevice(identifier);
		return m_bOpen.get();
	}
	public boolean connectDevice() {
		m_bOpen.set(false);
		currentDeviceIdentifier= null;
		connectTheThermalExpertDevice(DeviceIdentifier.DEFAULT);
		return m_bOpen.get();
	}
	//
	protected void connectTheThermalExpertDevice(DeviceIdentifier identifier) {
		// Search for the Thermal Expert USB device and stop
		// when not found:
		Device device= findTheThermalExpertDevice(identifier);
		if (device == null) {
			if (reportCriticalErrors()) {
				writeLater("The Thermal Expert USB device is not found.\n");
			};
			throw new TheThermalExpertUSBDeviceIsNotFound();
		};
		// Read the device descriptor
		// DeviceDescriptor:
		// A structure representing the standard USB device
		// descriptor. This descriptor is documented in
		// section 9.6.1 of the USB 3.0 specification.
		// All multiple-byte fields are represented in
		// host-endian format.
		final DeviceDescriptor deviceDescriptor= new DeviceDescriptor();
		// getDeviceDescriptor:
		// Get the USB device descriptor for a given device.
		// This is a non-blocking function; the device
		// descriptor is cached in memory.
		// device - the device
		// descriptor - output location for the descriptor
		// data
		// It returns 0 on success or a ERROR code on failure.
		int result2= LibUsb.getDeviceDescriptor(device,deviceDescriptor);
		if (result2 < 0) {
			if (reportCriticalErrors()) {
				writeLater(String.format("Unable to read device descriptor: %s\n",result2));
			};
			throw new UnableToReadDeviceDescriptor(result2);
		};
		// bNumConfigurations:
		// Returns the number of possible configurations.
		// return - The number of possible configurations.
		int numberOfConfigurations= deviceDescriptor.bNumConfigurations();
		for (byte i=0; i < numberOfConfigurations; i+= 1) {
			// ConfigDescriptor:
			// A structure representing the standard USB
			// configuration descriptor.
			// This descriptor is documented in section
			// 9.6.3 of the USB 3.0 specification.
			// All multiple-byte fields are represented
			// in host-endian format.
			final ConfigDescriptor configDescriptor= new ConfigDescriptor();
			// getConfigDescriptor:
			// Get the USB configuration descriptor for
			// the currently active configuration.
			// This is a non-blocking function which does
			// not involve any requests being sent to the
			// device.
			// device - A device.
			// descriptor - Output location for the USB
			// configuration descriptor. Only valid if 0
			// was returned. Must be freed with
			// freeConfigDescriptor(ConfigDescriptor)
			// after use.
			// Returns 0 on success, ERROR_NOT_FOUND if
			// the device is in unconfigured state
			// another ERROR code on error.
			final int result3= LibUsb.getConfigDescriptor(device,i,configDescriptor);
			if (result3 < 0) {
				if (reportCriticalErrors()) {
					writeLater(String.format("Unable to read config descriptor: %s\n",result3));
				};
				throw new UnableToReadConfigDescriptor(result3);
			};
			try {
				analyseConfigDescriptor(configDescriptor);
			} finally {
				// Ensure that the config descriptor is freed
				// freeConfigDescriptor:
				// Free a configuration descriptor
				// obtained from getConfigDescriptor
				// (Device,byte,ConfigDescriptor) or
				// getActiveConfigDescriptor(Device,
				// ConfigDescriptor).
				// It is safe to call this function
				// with a NULL config parameter, in
				// which case the function simply
				// returns.
				// descriptor - The configuration
				// descriptor to free
				LibUsb.freeConfigDescriptor(configDescriptor);
			}
		};
		if (m_USBRecvEndPoint == null || m_USBSendEndPoint == null) {
			if (reportCriticalErrors()) {
				writeLater(String.format("m_USBRecvEndPoint == null || m_USBSendEndPoint == null; m_bOpen: %s\n",m_bOpen.get()));
			};
			return;
		};
		handle= new DeviceHandle();
		// open:
		// Open a device and obtain a device handle.
		// A handle allows you to perform I/O on the
		// device in question.
		// Internally, this function adds a reference
		// to the device and makes it available to you
		// through getDevice(DeviceHandle)}. This
		// reference is removed during
		// close(DeviceHandle).
		// This is a non-blocking function; no requests
		// are sent over the bus.
		// @param device - The device to open.
		// @param handle - Output location for the
		// returned device handle pointer. Only
		// populated when the return code is 0.
		// @return 0 on success, ERROR_NO_MEM on memory
		// allocation failure, ERROR_ACCESS if the user
		// has insufficient permissions, ERROR_NO_DEVICE
		// if the device has been disconnected, another
		// error on other failure.
		int result4= LibUsb.open(device,handle);
		// No such device (it may have been disconnected):
		// ERROR_NO_DEVICE= -4;
		// Operation not supported or unimplemented on
		// this platform:
		// ERROR_NOT_SUPPORTED= -12;
		if (result4 != LibUsb.SUCCESS) {
			if (reportCriticalErrors()) {
				writeLater(String.format("Unable to open USB device: %s\n",result4));
			}
		};
		// Check if kernel driver is attached to
		// the interface.
		// kernelDriverActive:
		// Determine if a kernel driver is active
		// on an interface.
		// If a kernel driver is active, you
		// cannot claim the interface, and libusb
		// will be unable to perform I/O.
		// This functionality is not available on
		// Windows.
		// @param handle - A device handle.
		// @param interfaceNumber - The interface
		// to check.
		// @return 0 if no kernel driver is active,
		// 1 if a kernel driver is active,
		// ERROR_NO_DEVICE if the device has been
		// disconnected, ERROR_NOT_SUPPORTED on
		// platforms where the functionality is not
		// available, another ERROR code on other
		// failure.
		kernelDriverIsAttached= LibUsb.kernelDriverActive(handle,1);
		if (kernelDriverIsAttached < 0) {
			if (reportAdmissibleErrors()) {
				writeLater(String.format("Unable to check kernel driver active: %s\n",kernelDriverIsAttached));
			}
		};
		// Detach kernel driver from interface
		// 0 and 1. This can fail if kernel is
		// not attached to the device or operating
		// system doesn't support this operation.
		// These cases are ignored here.
		// detachKernelDriver:
		// Detach a kernel driver from an
		// interface. If successful, you will
		// then be able to claim the interface
		// and perform I/O. This functionality
		// is not available on Darwin or Windows.
		// Note that libusb itself also talks to
		// the device through a special kernel
		// driver, if this driver is already
		// attached to the device, this call will
		// not detach it and return
		// ERROR_NOT_FOUND.
		// @param handle - a device handle.
		// @param interfaceNumber - the interface
		// to detach the driver from.
		// @return 0 on success, ERROR_NOT_FOUND
		// if no kernel driver was active,
		// ERROR_INVALID_PARAM if the interface
		// does not exist, ERROR_NO_DEVICE if
		// the device has been disconnected,
		// ERROR_NOT_SUPPORTED on platforms where
		// the functionality is not available,
		// another ERROR code on other failure.
		int result5= LibUsb.detachKernelDriver(handle,1);
		if (	result5 != LibUsb.SUCCESS &&
			result5 != LibUsb.ERROR_NOT_SUPPORTED &&
			result5 != LibUsb.ERROR_NOT_FOUND) {
			if (reportCriticalErrors()) {
				writeLater(String.format("Unable to detach kernel driver: %s\n",result5));
			};
			throw new UnableToDetachKernelDriver(result5);
		};
		// claimInterface:
		// Claim an interface on a given
		// device handle. You must claim the
		// interface you wish to use before
		// you can perform I/O on any of its
		// endpoints.
		// It is legal to attempt to claim
		// an already-claimed interface, in
		// which case libusb just returns 0
		// without doing anything.
		// Claiming of interfaces is a purely
		// logical operation; it does not cause
		// any requests to be sent over the bus.
		// Interface claiming is used to
		// instruct the underlying operating
		// system that your application wishes
		// to take ownership of the interface.
		// This is a non-blocking function.
		// @param handle - A device handle.
		// @param iface - The bInterfaceNumber
		// of the interface you wish to claim.
		// @return 0 on success, ERROR_NOT_FOUND
		// if the requested interface does not
		// exist, ERROR_BUSY if another program
		// or driver has claimed the interface,
		// ERROR_NO_DEVICE if the device has
		// been disconnected, another error code
		// on other failure.
		int result6= LibUsb.claimInterface(handle,0);
		// Entity not found:
		// ERROR_NOT_FOUND= -5;
		if (result6 != LibUsb.SUCCESS) {
			if (reportCriticalErrors()) {
				writeLater(String.format("Unable to claim interface: %s\n",result6));
			};
			throw new UnableToClaimInterface(result6);
		};
		currentDeviceIdentifier= identifier;
		m_bOpen.set(true);
	}
	//
	protected void analyseConfigDescriptor(final ConfigDescriptor configDescriptor) {
		// iface:
		// Returns the array with interfaces
		// supported by this configuration.
		// return - The array with interfaces.
		Interface[] interfaces= configDescriptor.iface();
		for (int k=0; k < interfaces.length; k++) {
			Interface deviceInterface= interfaces[k];
			// altsetting:
			// Returns the array with
			// interface descriptors. The
			// length of this array is
			// determined by the
			// numAltsetting() field.
			// return - The array with
			// interface descriptors.
			InterfaceDescriptor[] iDescriptors= deviceInterface.altsetting();
			for (int m=0; m < iDescriptors.length; m++) {
				InterfaceDescriptor iDescriptor= iDescriptors[m];
				analyseInterfaceDescriptor(iDescriptor);
			}
		}
	}
	protected void analyseInterfaceDescriptor(final InterfaceDescriptor iDescriptor) {
		// endpoint:
		// Returns the array with
		// endpoints.
		// return - The array
		// with endpoints.
		EndpointDescriptor[] endpoints= iDescriptor.endpoint();
		for (int n=0; n < endpoints.length; n++) {
			EndpointDescriptor endpoint= endpoints[n];
			// bEndpointAddress:
			// The address of the endpoint described
			// by this descriptor. Bits 0:3 are
			// the endpoint number. Bits 4:6 are
			// reserved. Bit 7 indicates direction
			// (Either LibUsb#ENDPOINT_IN or
			// LibUsb#ENDPOINT_OUT).
			// return - The endpoint address.
			byte address= endpoint.bEndpointAddress();
			if (address==(byte)134) {
				m_USBRecvEndPoint= endpoint;
				m_USBRecvEndPointAddress= m_USBRecvEndPoint.bEndpointAddress();
			} else if (address==(byte)2) {
				m_USBSendEndPoint= endpoint;
				m_USBSendEndPointAddress= m_USBSendEndPoint.bEndpointAddress();
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void disconnectDevice() {
		if (m_bOpen.get()) {
			// boolean reportDelays= reportUSBTransferDelays.get();
			boolean reportDelays= reportUSBTransferDelays();
			try {
				long time1= 0;
				if (reportDelays) {
					writeLater("CONTROL TRANSFER... ");
					time1= System.currentTimeMillis();
				};
				int transfered= LibUsb.controlTransfer(
					handle,
					LibUsb.REQUEST_TYPE_STANDARD,
					(byte)51,
					(byte)1,
					(short)48879,
					m_VenByteBuf,
					m_iWriteTimeOut.get());
				if (reportDelays) {
					long time2= System.currentTimeMillis();
					long delay= time2 - time1;
					writeLater(String.format("COMPLETED; delay: %s\n",delay));
				};
				m_VenByteBuf.rewind();
				releaseInterface();
				closeHandle();
				if (transfered < 0) {
					if (reportAdmissibleErrors()) {
						writeLater(String.format("Control transfer failed: %s\n",transfered));
					};
					return;
				}
			} catch (Throwable e) {
				if (reportAdmissibleErrors()) {
					writeLater(String.format("Throwable: %s\n",e));
				}
			}
		};
		m_bOpen.set(false);
		currentDeviceIdentifier= null;
	}
	//
	protected void releaseInterface() {
		// Release the interface:
		// releaseInterface:
		// Release an interface previously claimed
		// with claimInterface(DeviceHandle,int)}.
		// You should release all claimed interfaces
		// before closing a device handle.
		// This is a blocking function. A
		// SET_INTERFACE control request will be
		// sent to the device, resetting interface
		// state to the first alternate setting.
		// @param handle - a device handle.
		// @param iface - The bInterfaceNumber of
		// the previously-claimed interface.
		// @return 0 on success, ERROR_NOT_FOUND
		// if the interface was not claimed,
		// ERROR_NO_DEVICE if the device has been
		// disconnected, another ERROR code on
		// other failure.
		// int result1= LibUsb.releaseInterface(handle,1);
		try {
			int result1= LibUsb.releaseInterface(handle,0);
			if (result1 != LibUsb.SUCCESS) {
				if (reportCriticalErrors()) {
					writeLater(String.format("Unable to release interface: %s\n",result1));
				};
				throw new UnableToReleaseInterface(result1);
			};
		} catch (Throwable e) {
			if (reportAdmissibleErrors()) {
				writeLater(String.format("Unable to release interface, e: %s\n",e));
			}
		}
	}
	//
	protected void closeHandle() {
		// Re-attach kernel driver if needed:
		if (kernelDriverIsAttached == 1) {
			// attachKernelDriver:
			// Re-attach an interface's kernel
			// driver, which was previously
			// detached using
			// detachKernelDriver(DeviceHandle,int).
			// This call is only effective on
			// Linux and returns ERROR_NOT_SUPPORTED
			// on all other platforms.
			// This functionality is not available on
			// Darwin or Windows.
			// @param handle - A device handle.
			// @param interfaceNumber - the interface
			// to attach the driver from.
			// @return 0 on success, ERROR_NOT_FOUND
			// if no kernel driver was active,
			// ERROR_INVALID_PARAM if the interface
			// does not exist, ERROR_NO_DEVICE if
			// the device has been disconnected,
			// ERROR_NOT_SUPPORTED on platforms
			// where the functionality is not
			// available, ERROR_BUSY if the driver
			// cannot be attached because the
			// interface is claimed by a program
			// or driver, another ERROR code on other
			// failure.
			int result2= LibUsb.attachKernelDriver(handle,1);
			if (result2 != LibUsb.SUCCESS) {
				if (reportCriticalErrors()) {
					writeLater(String.format("Unable to re-attach kernel driver: %s\n",result2));
				};
				throw new UnableToReattachKernelDriver(result2);
			}
		};
		// close:
		// Close a device handle.
		// Should be called on all open handles
		// before your application exits.
		// Internally, this function destroys the
		// reference that was added by
		// open(Device,DeviceHandle) on the given
		// device. This is a non-blocking function;
		// no requests are sent over the bus.
		// @param handle - The handle to close.
		try {
			LibUsb.close(handle);
		} catch (Throwable e) {
			if (reportAdmissibleErrors()) {
				writeLater(String.format("Unable to close device, e: %s\n",e));
			}
		}
	}
	//
	protected void closeUSB() {
		// Deinitialize the libusb context:
		// exit:
		// Deinitialize libusb.
		// Should be called after closing all open
		// devices and before your application
		// terminates.
		// @param context - The Context to
		// deinitialize, or NULL for the default
		// context.
		LibUsb.exit(context);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected byte[] createBuffer(int iSize) {
		if (iSize % PACKET_TIMES != 0) {
			iSize+= 512 - (iSize % PACKET_TIMES);
		};
		return new byte[iSize];
	}
	//
	protected int read(byte[] buf, int iSize) {
		int iOffset= 0;
		if (iSize % PACKET_TIMES != 0) {
			iSize+= PACKET_TIMES - (iSize % PACKET_TIMES);
		};
		int iRepeat= iSize / MAX_BYTES_PER_PACKET;
		if (iSize % MAX_BYTES_PER_PACKET != 0) {
			iRepeat++;
		};
		// boolean reportDelays= reportUSBTransferDelays.get();
		boolean reportDelays= reportUSBTransferDelays();
		try {
			// controlTransfer:
			// Perform a USB control transfer.
			// The direction of the transfer is inferred
			// from the bmRequestType field of the setup
			// packet.
			// The wValue, wIndex and wLength fields
			// values should be given in host-endian
			// byte order.
			// handle - A handle for the device to
			// communicate with.
			// bmRequestType - The request type
			// field for the setup packet.
			// bRequest - The request field for the
			// setup packet.
			// wValue - The value field for the
			// setup packet.
			// wIndex - The index field for the
			// setup packet.
			// data - A suitably-sized data buffer for
			// either input or output (depending on
			// direction bits within bmRequestType).
			// timeout - Timeout (in millseconds) that
			// this function should wait before giving
			// up due to no response being received.
			// For an unlimited timeout, use value 0.
			// @return on success the number of bytes
			// actually transferred, ERROR_TIMEOUT if
			// the transfer timed out, ERROR_PIPE} if
			// the control request was not supported
			// by the device, ERROR_NO_DEVICE if the
			// device has been disconnected, another
			// ERROR code on other failures
			//
			long time11= 0;
			if (reportDelays) {
				writeLater("CONTROL TRANSFER... ");
				time11= System.currentTimeMillis();
			};
			int transfered= LibUsb.controlTransfer(
				handle,
				LibUsb.REQUEST_TYPE_STANDARD,
				(byte)134,
				(short)(iSize >> 9),
				(short)48879,
				m_VenByteBuf,
				// m_iReadTimeOut);
				m_iWriteTimeOut.get());
			if (reportDelays) {
				long time12= System.currentTimeMillis();
				long delay1= time12 - time11;
				writeLater(String.format("COMPLETED; delay: %s\n",delay1));
			};
			m_VenByteBuf.rewind();
			if (transfered < 0) {
				if (reportAdmissibleErrors()) {
					writeLater(String.format("Control transfer failed: %s\n",transfered));
				};
				return -1;
			};
			//
			ByteBuffer byteBufTemp= ByteBuffer.allocateDirect(MAX_BYTES_PER_PACKET);
			ByteBuffer auxiliaryByteBufTemp= ByteBuffer.allocateDirect(MAX_BYTES_PER_PACKET);
			//
			int INT_SIZE= Integer.SIZE / Byte.SIZE;
			IntBuffer transferedBytes= ByteBuffer.allocateDirect(INT_SIZE).asIntBuffer();
			//
			int readTimeOut= m_iReadTimeOut.get();
			int i= 0;
			// int numberOfFailures= 0;
			while (iSize > 0) {
				try {
					int iRecvSize= Math.min(MAX_BYTES_PER_PACKET,iSize);
					transferedBytes.clear();
					transferedBytes.put(new int[]{iRecvSize});
					transferedBytes.flip();
					// bulkTransfer:
					// Perform a USB bulk transfer.
					// The direction of the transfer
					// is inferred from the direction
					// bits of the endpoint address.
					// For bulk reads, the length
					// field indicates the maximum
					// length of data you are
					// expecting to receive. If less
					// data arrives than expected,
					// this function will return
					// that data, so be sure to
					// check the transferred
					// output parameter.
					// You should also check the
					// transferred parameter for
					// bulk writes. Not all of the
					// data may have been written.
					// Also check transferred when
					// dealing with a timeout error
					// code. libusb may have to
					// split your transfer into a
					// number of chunks to satisfy
					// underlying O/S requirements,
					// meaning that the timeout may
					// expire after the first few
					// chunks have completed. libusb
					// is careful not to lose any
					// data that may have been
					// transferred; do not assume
					// that timeout conditions
					// indicate a complete lack
					// of I/O.
					// handle - A handle for the
					// device to communicate with.
					// endpoint - The address of a
					// valid endpoint to communicate
					// with.
					// data - A suitably-sized data
					// buffer for either input or
					// output (depending on
					// endpoint).
					// transferred - Output location
					// for the number of bytes
					// actually transferred.
					// timeout - timeout (in
					// millseconds) that this
					// function should wait before
					// giving up due to no response
					// being received. For an
					// unlimited timeout, use value
					// 0.
					// Return: 0 on success (and
					// populates transferred),
					// ERROR_TIMEOUT if the transfer
					// timed out (and populates
					// transferred), ERROR_PIPE} if
					// the endpoint halted,
					// ERROR_OVERFLOW} if the device
					// offered more data, see
					// Packets and overflows,
					// ERROR_NO_DEVICE if the
					// device has been disconnected,
					// another ERROR code on other
					// failures.
					// int ERROR_TIMEOUT = -7;
					long time21= 0;
					if (reportDelays) {
						writeLater("bulk transfer... ");
						time21= System.currentTimeMillis();
					};
					int result= LibUsb.bulkTransfer(
						handle,
						m_USBRecvEndPointAddress,
						byteBufTemp,
						transferedBytes,
						readTimeOut); // Это источник задержки?
					if (reportDelays) {
						long time22= System.currentTimeMillis();
						long delay2= time22 - time21;
						writeLater(String.format("completed; delay: %s\n",delay2));
					};
					byteBufTemp.rewind();
					int iRecv= transferedBytes.get();
					if (iRecv != iRecvSize) {
						if (reportDelays) {
							writeLater(String.format("USB Transfer Error: iRecv(%s) != iRecvSize(%s); byteBufTemp.capacity(): %s; MAX_BYTES_PER_PACKET: %s; iSize: %s\n",iRecv,iRecvSize,byteBufTemp.capacity(),MAX_BYTES_PER_PACKET,iSize));
						};
						if (iRecv > iRecvSize) {
							iRecv= 0;
							readTail(auxiliaryByteBufTemp,transferedBytes,iRecvSize);
						};
						return iOffset;
					};
					byteBufTemp.get(buf,iOffset,iRecv);
					byteBufTemp.clear();
					iOffset+= iRecv;
					// iSize-= iRecv;
					iSize-= iRecvSize;
					i++;
				} catch (Exception e) {
					if (reportAdmissibleErrors()) {
						writeLater(String.format("Exception: %s\n",e));
					};
					return iOffset;
				}
			};
			return iOffset;
		} catch (Exception e2) {
			if (reportAdmissibleErrors()) {
				writeLater(String.format("Exception: %s\n",e2));
			};
			return -1;
		}
	}
	//
	public int write(byte[] buf, int iSize) {
		int iOffset= 0;
		if (iSize % PACKET_TIMES != 0) {
			iSize+= PACKET_TIMES - (iSize % PACKET_TIMES);
		};
		int iRepeat= iSize / MAX_BYTES_PER_PACKET;
		if (iSize % MAX_BYTES_PER_PACKET != 0) {
			iRepeat++;
		};
		// boolean reportDelays= reportUSBTransferDelays.get();
		boolean reportDelays= reportUSBTransferDelays();
		try {
			long time11= 0;
			if (reportDelays) {
				writeLater("CONTROL TRANSFER... ");
				time11= System.currentTimeMillis();
			};
			int transfered= LibUsb.controlTransfer(
				handle,
				LibUsb.REQUEST_TYPE_STANDARD,
				(byte)2,
				(short)(iSize >> 9),
				(short)48879,
				m_VenByteBuf,
				m_iWriteTimeOut.get());
			if (reportDelays) {
				long time12= System.currentTimeMillis();
				long delay1= time12 - time11;
				writeLater(String.format("COMPLETED; delay: %s\n",delay1));
			};
			m_VenByteBuf.rewind();
			if (transfered < 0) {
				if (reportAdmissibleErrors()) {
					writeLater(String.format("Control transfer failed: %s\n",transfered));
				};
				return -1;
			};
			ByteBuffer byteBufTemp= ByteBuffer.allocateDirect(buf.length);
			byteBufTemp.put(buf);
			byteBufTemp.flip();
			int writeTimeOut= m_iWriteTimeOut.get();
			int i= 0;
			while (i < iRepeat) {
				try {
					int iWriteSize= Math.min(MAX_BYTES_PER_PACKET,iSize);
					int INT_SIZE= Integer.SIZE / Byte.SIZE;
					IntBuffer transferedBytes= ByteBuffer.allocateDirect(INT_SIZE).asIntBuffer();
					transferedBytes.put(new int[]{iWriteSize});
					transferedBytes.flip();
					long time21= 0;
					if (reportDelays) {
						writeLater("bulk transfer... ");
						time21= System.currentTimeMillis();
					};
					int result= LibUsb.bulkTransfer(
						handle,
						(byte)m_USBSendEndPointAddress,
						byteBufTemp,
						transferedBytes,
						writeTimeOut);
					if (reportDelays) {
						long time22= System.currentTimeMillis();
						long delay2= time22 - time21;
						writeLater(String.format("completed; delay: %s\n",delay2));
					};
					byteBufTemp.flip();
					int iSend= transferedBytes.get();
					iOffset+= iSend;
					// iSize-= 16384;
					// iSize-= iSend;
					iSize-= iWriteSize;
					i++;
				} catch (Exception e) {
					if (reportCriticalErrors()) {
						e.printStackTrace();
					};
					return iOffset;
				}
			};
			return iOffset;
		} catch (Exception e2) {
			if (reportCriticalErrors()) {
				e2.printStackTrace();
			};
			return -1;
		}
	}
	//
	protected void readTail(ByteBuffer auxiliaryByteBufTemp, IntBuffer transferedBytes, int iRecvSize) {
		long time1= System.currentTimeMillis();
		// boolean reportDelays= reportUSBTransferDelays.get();
		boolean reportDelays= reportUSBTransferDelays();
		if (reportAdmissibleErrors()) {
			writeLater("CALL: READ TAIL\n");
		};
		try {
			int readTimeOut= m_iReadTimeOut.get();
			int counter= 0;
			boolean repeatReading= true;
			while (repeatReading) {
				if (counter > maximalNumberOfTailReadingFailures) {
					break;
				};
				transferedBytes.clear();
				transferedBytes.put(new int[]{iRecvSize});
				transferedBytes.flip();
				if (reportDelays) {
					writeLater("bulk transfer... ");
					time1= System.currentTimeMillis();
				};
				int result= LibUsb.bulkTransfer(
					handle,
					m_USBRecvEndPointAddress,
					auxiliaryByteBufTemp,
					transferedBytes,
					readTimeOut);
				if (reportDelays) {
					long time2= System.currentTimeMillis();
					long delay= time2 - time1;
					writeLater(String.format("completed; delay: %s\n",delay));
				};
				auxiliaryByteBufTemp.rewind();
				int iRecv= transferedBytes.get();
				if (iRecv <= 0) {
					repeatReading= false;
				};
				counter++;
			}
		} catch (Exception e) {
			if (reportAdmissibleErrors()) {
				writeLater(String.format("Exception: %s\n",e));
			}
			// return;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public int resetDevice() {
		// resetDevice:
		// Perform a USB port reset to reinitialize a device.
		// The system will attempt to restore the previous
		// configuration and alternate settings after the
		// reset has completed.
		// If the reset fails, the descriptors change, or the
		// previous state cannot be restored, the device will
		// appear to be disconnected and reconnected. This
		// means that the device handle is no longer valid
		// (you should close it) and rediscover the device.
		// A return code of {@link #ERROR_NOT_FOUND}
		// indicates when this is the case. This is a
		// blocking function which usually incurs a
		// noticeable delay.
		// @param handle - a handle of the device to reset
		// @return 0 on success, {@link #ERROR_NOT_FOUND}
		// if re-enumeration is required, or if the device
		// has been disconnected another ERROR code on other
		// failure.
		return LibUsb.resetDevice(handle);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static void writeLater(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				System.err.print(text);
			}
		});
	}
}
