// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system.checker;

import morozov.run.*;

import java.net.CookieHandler;
import java.util.concurrent.locks.ReentrantLock;

public class StaticWebAttributes extends StaticAttributes {
	//
	private CookieHandler manager;
	private ReentrantLock webGuard= new ReentrantLock();
	private static final String staticIdentifier= "_Web";
	//
	private static StaticWebAttributes retrieveStaticWebAttributes(StaticContext context) {
		StaticAttributes attributes= context.retrieveAttributes(staticIdentifier);
		if (attributes==null) {
			synchronized (context) {
				attributes= context.retrieveAttributes(staticIdentifier);
				if (attributes==null) {
					attributes= new StaticWebAttributes();
					context.saveAttributes(staticIdentifier,attributes);
				}
			}
		};
		return (StaticWebAttributes)attributes;
	}
	public static void setCookieManager(CookieHandler manager, StaticContext context) {
		StaticWebAttributes attributes= retrieveStaticWebAttributes(context);
		synchronized (attributes) {
			attributes.manager= manager;
		}
	}
	public static CookieHandler retrieveCookieManager(StaticContext context) {
		StaticWebAttributes attributes= retrieveStaticWebAttributes(context);
		CookieHandler manager;
		synchronized (attributes) {
			manager= attributes.manager;
		};
		return manager;
	}
	public static ReentrantLock retrieveWebGuard(StaticContext context) {
		StaticWebAttributes attributes= retrieveStaticWebAttributes(context);
		ReentrantLock guard;
		synchronized (attributes) {
			guard= attributes.webGuard;
		};
		return guard;
	}
}
