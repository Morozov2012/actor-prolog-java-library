// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.run;

public class ProcessStateRequest {
	public ProcessStateListener listener;
	public String identifier;
	public ProcessStateRequest(ProcessStateListener target, String label) {
		listener= target;
		identifier= label;
	}
}
