// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.sadt;

import java.util.Map;

public enum BooleanOperation {
	ATOM {
		@Override
		boolean eval(DiagramBox[] boxes, Map<String,ComponentState> componentSuccess) {
			return true;
		}
	},
	AND {
		@Override
		boolean eval(DiagramBox[] boxes, Map<String,ComponentState> componentSuccess) {
			for (int n=0; n < boxes.length; n++) {
				if (!boxes[n].isOK(componentSuccess)) {
					return false;
				}
			};
			return true;
		}
	},
	OR {
		@Override
		boolean eval(DiagramBox[] boxes, Map<String,ComponentState> componentSuccess) {
			for (int n=0; n < boxes.length; n++) {
				if (boxes[n].isOK(componentSuccess)) {
					return true;
				}
			};
			return false;
		}
	},
	XOR {
		@Override
		boolean eval(DiagramBox[] boxes, Map<String,ComponentState> componentSuccess) {
			int numberOfGreenBlocks= 0;
			for (int n=0; n < boxes.length; n++) {
				if (boxes[n].isOK(componentSuccess)) {
					numberOfGreenBlocks= numberOfGreenBlocks + 1;
				}
			};
			if (numberOfGreenBlocks==1) {
				return true;
			} else {
				return false;
			}
		}
	},
	ALL_NOT {
		@Override
		boolean eval(DiagramBox[] boxes, Map<String,ComponentState> componentSuccess) {
			for (int n=0; n < boxes.length; n++) {
				if (boxes[n].isOK(componentSuccess)) {
					return false;
				}
			};
			return true;
		}
	},
	NOT_ALL {
		@Override
		boolean eval(DiagramBox[] boxes, Map<String,ComponentState> componentSuccess) {
			for (int n=0; n < boxes.length; n++) {
				if (!boxes[n].isOK(componentSuccess)) {
					return true;
				}
			};
			return false;
		}
	};
	abstract boolean eval(DiagramBox[] boxes, Map<String,ComponentState> componentSuccess);
}
