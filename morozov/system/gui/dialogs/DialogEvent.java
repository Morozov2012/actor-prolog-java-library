// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.dialogs;

import morozov.run.*;

public class DialogEvent {
	public DialogEntry entry;
	public boolean sendFlowMessage;
	public DialogEventType isControlModificationEvent;
	public DialogEvent(DialogEntry e, boolean isSlot, DialogEventType mode) {
		entry= e;
		sendFlowMessage= isSlot;
		isControlModificationEvent= mode;
	}
	public void transmitEntryValue(AbstractDialog targetDialog, ChoisePoint rootCP) {
		if (sendFlowMessage) {
			targetDialog.transmitEntryValue(entry,rootCP);
		}
	}
	public void sendCreatedControlMessage(AbstractDialog targetDialog, ChoisePoint rootCP) {
		switch (isControlModificationEvent) {
			case CREATED_CONTROL:
				targetDialog.sendCreatedControlMessage(entry,rootCP);
				break;
			case MODIFIED_CONTROL:
				targetDialog.sendModifiedControlMessage(entry,rootCP);
				break;
			case COMPLETE_EDITING:
				targetDialog.sendCompleteEditingMessage(entry,rootCP);
				break;
		}
	}
}
