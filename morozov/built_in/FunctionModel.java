// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.run.*;
import morozov.system.errors.*;
import morozov.system.gui.*;
import morozov.system.gui.sadt.*;
import morozov.system.gui.sadt.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;
import morozov.worlds.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

public abstract class FunctionModel extends Alpha {
	//
	protected Map<String,AbstractProcess> diagramComponents= Collections.synchronizedMap(new HashMap<String,AbstractProcess>());
	protected ActiveDiagram diagram;
	//
	public FunctionModel() {
	}
	public FunctionModel(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	public void closeFiles() {
		synchronized(this) {
			DiagramUtils.safelyDisposeAllDiagrams(staticContext);
		};
		super.closeFiles();
	}
	//
	public void registerComponents1s(ChoisePoint iX, Term value) {
		try {
			try {
				while (true) {
					Term head= value.getNextListHead(iX);
					try {
						long functor= head.getStructureFunctor(iX);
						if (functor!=SymbolCodes.symbolCode_E_component) {
							throw new WrongArgumentIsNotIdentifierAndComponent(head);
						};
						Term[] pair= head.getStructureArguments(iX);
						if (pair.length!=2) {
							throw new WrongArgumentIsNotIdentifierAndComponent(head);
						};
						try {
							String identifier= pair[0].getStringValue(iX);
							AbstractProcess process= (AbstractProcess)(pair[1].dereferenceValue(iX));
							diagramComponents.put(identifier,process);
							value= value.getNextListTail(iX);
						} catch (TermIsNotAString e2) {
							throw new WrongArgumentIsNotComponentIdentifier(pair[0]);
						}
					} catch (TermIsNotAStructure e2) {
						throw new WrongArgumentIsNotIdentifierAndComponent(head);
					}
				}
			} catch (EndOfList e2) {
			}
		} catch (TermIsNotAList e3) {
			throw new WrongArgumentIsNotComponentIdentifierList(value);
		}
	}
	//
	public void showModel1s(ChoisePoint iX, Term value) {
		try {
			String identifier= value.getStringValue(iX);
			DesktopUtils.createPaneIfNecessary(staticContext);
			createActiveDiagramIfNecessary();
			diagram.showModel(identifier);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(value);
		}
	}
	//
	public void showDescription1s(ChoisePoint iX, Term value) {
		try {
			String identifier= value.getStringValue(iX);
			DesktopUtils.createPaneIfNecessary(staticContext);
			createActiveDiagramIfNecessary();
			diagram.showDescription(identifier);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(value);
		}
	}
	//
	protected void createActiveDiagramIfNecessary() {
		synchronized(this) {
			if (diagram==null) {
				diagram= new ActiveDiagram(diagramComponents,staticContext);
			}
		}
	}
}
