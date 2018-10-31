// (c) 2015-2017 IRE RAS Alexei A. Morozov

package morozov.worlds.remote;

import morozov.run.*;
import morozov.system.gui.space2d.*;
import morozov.worlds.remote.signals.*;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ExternalWorldInterface extends Remote {
	//
	public long selectSignature(MethodSignature signature, byte[] localDomainTable) throws RemoteException;
	public void receiveAsyncCall(long domainSignatureNumber, boolean isControlCall, boolean useBuffer, byte[] arguments) throws RemoteException;
	public void sendResidentRequest(ExternalResidentInterface resident, long domainSignatureNumber, byte[] arguments, boolean sortAndReduceResultList) throws RemoteException;
	public void withdrawRequest(ExternalResidentInterface resident) throws RemoteException;
	//
	public byte[] getImage() throws RemoteException, OwnWorldIsNotBufferedImage;
	public void setImage(byte[] array, GenericImageEncodingAttributes attributes) throws RemoteException, OwnWorldIsNotBufferedImage;
	//
	public void process(java.awt.image.BufferedImage nativeImage, long frameNumber, long timeInMilliseconds, boolean takeFrameIntoAccount, GenericImageEncodingAttributes attributes) throws RemoteException, OwnWorldIsNotVideoProcessingMachine;
	public void process(byte[] bytes, long frameNumber, long timeInMilliseconds, boolean takeFrameIntoAccount, GenericImageEncodingAttributes attributes) throws RemoteException, OwnWorldIsNotVideoProcessingMachine;
	public long getFrameNumber() throws RemoteException, OwnWorldIsNotVideoProcessingMachine;
	public long getFrameTime() throws RemoteException, OwnWorldIsNotVideoProcessingMachine;
	public void commit() throws RemoteException, OwnWorldIsNotVideoProcessingMachine;
	public void resetSettings() throws RemoteException, OwnWorldIsNotVideoProcessingMachine;
	public void resetStatistics() throws RemoteException, OwnWorldIsNotVideoProcessingMachine;
	public void resetResults() throws RemoteException, OwnWorldIsNotVideoProcessingMachine;
	public void resetAll() throws RemoteException, OwnWorldIsNotVideoProcessingMachine;
	public byte[] getBlobs() throws RemoteException, OwnWorldIsNotVideoProcessingMachine;
	public byte[] getTracks() throws RemoteException, OwnWorldIsNotVideoProcessingMachine;
	public byte[] getConnectedGraphs() throws RemoteException, OwnWorldIsNotVideoProcessingMachine;
	public byte[] getChronicle() throws RemoteException, OwnWorldIsNotVideoProcessingMachine;
	public byte[] getRecentImage() throws RemoteException, OwnWorldIsNotVideoProcessingMachine;
	public byte[] getPreprocessedImage() throws RemoteException, OwnWorldIsNotVideoProcessingMachine;
	public byte[] getForegroundImage() throws RemoteException, OwnWorldIsNotVideoProcessingMachine;
	public byte[] getSynthesizedImage() throws RemoteException, OwnWorldIsNotVideoProcessingMachine;
	public byte[] getBackgroundImage(int layerNumber) throws RemoteException, OwnWorldIsNotVideoProcessingMachine;
	public byte[] getSigmaImage(int layerNumber) throws RemoteException, OwnWorldIsNotVideoProcessingMachine;
	public double[] physicalCoordinates(int pixelX, int pixelY) throws RemoteException, OwnWorldIsNotVideoProcessingMachine;
	public double characteristicLength(int x, int y) throws RemoteException, OwnWorldIsNotVideoProcessingMachine;
	public GenericImageEncodingAttributes getCurrentImageEncodingAttributes() throws RemoteException, OwnWorldIsNotVideoProcessingMachine;
}
