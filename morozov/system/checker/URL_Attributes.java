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
	public URI uri;
	public URL url;
	private Throwable exception;
	private Term exceptionName;
	public CharacterSet characterSet;
	public int maxWaitingInterval;
	public boolean isLocalResource;
	public LocalFileAttributes localFileAttributes;
	// public boolean has_UTF_Coding;
	public boolean isDirectory;
	public long lastModificationTime;
	public long size;
	public URL_Attributes(URI i, URL l, Throwable e, Term eN, CharacterSet cS, int mWI, boolean iLR, boolean iD) {
		uri= i;
		url= l;
		// connection= null;
		exception= e;
		exceptionName= eN;
		characterSet= cS;
		maxWaitingInterval= mWI;
		isLocalResource= iLR;
		if (isLocalResource) {
			localFileAttributes= new LocalFileAttributes(uri);
		};
		// has_UTF_Coding= h_UFT_C;
		isDirectory= iD;
		lastModificationTime= -1;
		size= -1;
	}
	public URL_Attributes(URI i, URL l, CharacterSet cS, int mWI, boolean iLR, boolean iD, long lMT, long s) {
		uri= i;
		url= l;
		// connection= c;
		exception= null;
		exceptionName= null;
		characterSet= cS;
		maxWaitingInterval= mWI;
		isLocalResource= iLR;
		if (isLocalResource) {
			localFileAttributes= new LocalFileAttributes(uri);
		};
		// has_UTF_Coding= h_UFT_C;
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
		// has_UTF_Coding= oldAttributes.has_UTF_Coding;
		isDirectory= iD; // oldAttributes.isDirectory;
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
		// has_UTF_Coding= oldAttributes.has_UTF_Coding;
		isDirectory= iD; // oldAttributes.isDirectory;
		lastModificationTime= -1;
		size= -1;
	}
	public boolean wereChanged(URL_Attributes recentAttributes) {
		if (recentAttributes.connectionThrowsException(exception)) {
			// Do not report repeated error
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
			Term address= null;
			Term date= null;
			Term time= null;
			// Term attribute= null;
			Term resourceSize= null;
			address= new PrologString(url.toExternalForm());
			// address= new PrologString(url.toString());
			// Calendar calendar= new GregorianCalendar();
			Calendar calendar= Calendar.getInstance();
			// calendar.setTime(new Date(connection.getDate()));
			calendar.setTime(new Date(lastModificationTime));
			Term year= new PrologInteger(calendar.get(Calendar.YEAR));
			Term month= new PrologInteger(calendar.get(Calendar.MONTH) + 1);
			Term day= new PrologInteger(calendar.get(Calendar.DAY_OF_MONTH));
			Term hour= new PrologInteger(calendar.get(Calendar.HOUR_OF_DAY));
			Term minute= new PrologInteger(calendar.get(Calendar.MINUTE));
			Term second= new PrologInteger(calendar.get(Calendar.SECOND));
			Term millisecond= new PrologInteger(calendar.get(Calendar.MILLISECOND));
			date= new PrologStructure(SymbolCodes.symbolCode_E_date,new Term[]{year,month,day});
			time= new PrologStructure(SymbolCodes.symbolCode_E_time,new Term[]{hour,minute,second,millisecond});
			// attribute= new PrologInteger(0);
			resourceSize= new PrologInteger(size);
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
	public String toString() {
		return	uri.toString() + ";" +
			url.toExternalForm() + ";" +
			// connection.toString() + ";" +
			String.format("%d",maxWaitingInterval) + ";" +
			String.format("%s",isDirectory) + ";" +
			String.format("%d",lastModificationTime) + ";" +
			String.format("%d",size) + ";" +
			String.format("%s",exception) + ";";
	}
}
