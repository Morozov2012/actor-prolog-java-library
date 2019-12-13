// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.checker;

import target.*;

import morozov.system.files.*;
import morozov.terms.*;

import java.net.URI;
import java.net.URL;

import java.util.Date;
import java.util.Calendar;

public class URL_Attributes {
	//
	protected URI uri;
	protected URL url;
	protected Throwable exception;
	protected Term exceptionName;
	protected CharacterSet characterSet;
	protected int maxWaitingInterval;
	protected boolean isLocalResource;
	protected LocalFileAttributes localFileAttributes;
	protected boolean isDirectory;
	protected long lastModificationTime;
	protected long size;
	//
	public URL_Attributes(URI i, URL l, Throwable e, Term eN, CharacterSet cS, int mWI, boolean iLR, boolean iD) {
		uri= i;
		url= l;
		exception= e;
		exceptionName= eN;
		characterSet= cS;
		maxWaitingInterval= mWI;
		isLocalResource= iLR;
		if (isLocalResource) {
			localFileAttributes= new LocalFileAttributes(uri);
		};
		isDirectory= iD;
		lastModificationTime= -1;
		size= -1;
	}
	public URL_Attributes(URI i, URL l, CharacterSet cS, int mWI, boolean iLR, boolean iD, long lMT, long s) {
		uri= i;
		url= l;
		exception= null;
		exceptionName= null;
		characterSet= cS;
		maxWaitingInterval= mWI;
		isLocalResource= iLR;
		if (isLocalResource) {
			localFileAttributes= new LocalFileAttributes(uri);
		};
		isDirectory= iD;
		lastModificationTime= lMT;
		size= s;
	}
	public URL_Attributes(URL_Attributes oldAttributes, boolean iD, long lMT, long s) {
		uri= oldAttributes.uri;
		url= oldAttributes.url;
		exception= null;
		exceptionName= null;
		characterSet= oldAttributes.characterSet;
		maxWaitingInterval= oldAttributes.maxWaitingInterval;
		isLocalResource= oldAttributes.isLocalResource;
		if (isLocalResource) {
			localFileAttributes= new LocalFileAttributes(uri);
		};
		isDirectory= iD;
		lastModificationTime= lMT;
		size= s;
	}
	public URL_Attributes(URL_Attributes oldAttributes, boolean iD, Throwable e, Term eN) {
		uri= oldAttributes.uri;
		url= oldAttributes.url;
		exception= e;
		exceptionName= eN;
		characterSet= oldAttributes.characterSet;
		maxWaitingInterval= oldAttributes.maxWaitingInterval;
		isLocalResource= oldAttributes.isLocalResource;
		if (isLocalResource) {
			localFileAttributes= new LocalFileAttributes(uri);
		};
		isDirectory= iD;
		lastModificationTime= -1;
		size= -1;
	}
	//
	public URI getURI() {
		return uri;
	}
	public URL getURL() {
		return url;
	}
	public CharacterSet getCharacterSet() {
		return characterSet;
	}
	public int getMaxWaitingInterval() {
		return maxWaitingInterval;
	}
	public boolean isLocalResource() {
		return isLocalResource;
	}
	public LocalFileAttributes getLocalFileAttributes() {
		return localFileAttributes;
	}
	public boolean isDirectory() {
		return isDirectory;
	}
	public long getLastModificationTime() {
		return lastModificationTime;
	}
	public long getSize() {
		return size;
	}
	//
	public boolean wereChanged(URL_Attributes recentAttributes) {
		if (recentAttributes.connectionThrowsException(exception)) {
			// Do not report repeated error:
			return false;
		} else {
			if (	lastModificationTime <= 0 ||
				lastModificationTime != recentAttributes.lastModificationTime ||
				size != recentAttributes.size ||
				isDirectory != recentAttributes.isDirectory
					) {
				return true;
			} else if (isLocalResource) {
				if (recentAttributes.isLocalResource) {
					LocalFileAttributes recentLocalFileAttributes= recentAttributes.localFileAttributes;
					if (localFileAttributes == null) {
						if (recentLocalFileAttributes == null) {
							return false;
						} else {
							return true;
						}
					} else {
						if (recentLocalFileAttributes == null) {
							return true;
						} else {
							return localFileAttributes.wereChanged(recentLocalFileAttributes);
						}
					}
				} else {
					return true;
				}
			} else {
				return false;
			}
		}
	}
	public boolean connectionWasSuccessful() {
		return (exception==null);
	}
	public boolean connectionThrowsException(Throwable e) {
		if (exception==null || e==null) {
			return false;
		} else {
			return (exception.toString().equals(e.toString()));
		}
	}
	public void setException(Throwable e) {
		exception= e;
	}
	public Term getExceptionName() {
		if (exception != null && exceptionName != null) {
			return exceptionName;
		} else {
			return new PrologSymbol(SymbolCodes.symbolCode_E_unknown);
		}
	}
	public Term toTerm() {
		if (connectionWasSuccessful()) {
			Term address= new PrologString(url.toExternalForm());
			Calendar calendar= Calendar.getInstance();
			calendar.setTime(new Date(lastModificationTime));
			Term year= new PrologInteger(calendar.get(Calendar.YEAR));
			Term month= new PrologInteger(calendar.get(Calendar.MONTH) + 1);
			Term day= new PrologInteger(calendar.get(Calendar.DAY_OF_MONTH));
			Term hour= new PrologInteger(calendar.get(Calendar.HOUR_OF_DAY));
			Term minute= new PrologInteger(calendar.get(Calendar.MINUTE));
			Term second= new PrologInteger(calendar.get(Calendar.SECOND));
			Term millisecond= new PrologInteger(calendar.get(Calendar.MILLISECOND));
			Term date= new PrologStructure(SymbolCodes.symbolCode_E_date,new Term[]{year,month,day});
			Term time= new PrologStructure(SymbolCodes.symbolCode_E_time,new Term[]{hour,minute,second,millisecond});
			Term resourceSize= new PrologInteger(size);
			long functorCode;
			if (isDirectory) {
				functorCode= SymbolCodes.symbolCode_E_catalog;
			} else {
				functorCode= SymbolCodes.symbolCode_E_entry;
			};
			return new PrologStructure(functorCode,new Term[]{address,date,time,resourceSize});
		} else {
			return exceptionName;
		}
	}
	//
	@Override
	public String toString() {
		return	uri.toString() + ";" +
			url.toExternalForm() + ";" +
			String.format("%d",maxWaitingInterval) + ";" +
			String.format("%s",isDirectory) + ";" +
			String.format("%d",lastModificationTime) + ";" +
			String.format("%d",size) + ";" +
			String.format("%s",exception) + ";";
	}
}
