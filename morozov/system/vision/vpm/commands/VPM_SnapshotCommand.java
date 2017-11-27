// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands;

import morozov.system.vision.vpm.*;

public abstract class VPM_SnapshotCommand extends VPM_Command {
	abstract public void execute(VPM_Snapshot snapshot);
}
