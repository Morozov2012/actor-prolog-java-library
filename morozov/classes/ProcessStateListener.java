// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.classes;

public interface ProcessStateListener {
	public void rememberStateOfProcess(String identifier, boolean isProven, boolean isSuspended);
}
