// (c) 2018 Alexei A. Morozov

package morozov.system.frames.converters.interfaces;

public interface DataFrameProviderInterface {
	//
	public void reportBufferOverflow();
	public void annulBufferOverflow();
	//
	public void completeDataWriting(long numberOfFrames, Throwable e);
}
