// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.run;

public class ProcessStateRequest {
	//
	protected ProcessStateListener listener;
	protected String identifier;
	//
	public ProcessStateRequest(ProcessStateListener target, String label) {
		listener= target;
		identifier= label;
	}
	//
	public ProcessStateListener getListener() {
		return listener;
	}
	public String getIdentifier() {
		return identifier;
	}
}
