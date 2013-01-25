// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.gui.space3d;

import target.*;

import morozov.built_in.*;
import morozov.system.*;
import morozov.terms.*;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.View;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class Utils3D extends PrincipalNode3D {
	//
	public static BranchGroup termToBranchGroupOrNodeList(Term value, Canvas3D targetWorld, SimpleUniverse u, javax.media.j3d.Canvas3D canvas3D, ChoisePoint iX) {
		try { // BranchGroup
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_BranchGroup,1,iX);
			return attributesToBranchGroup(arguments[0],targetWorld,u,canvas3D,iX);
		} catch (Backtracking b) {
			BranchGroup branchGroup= new BranchGroup();
			termToListOfNodes(branchGroup,branchGroup,value,targetWorld,u,canvas3D,null,iX);
			return branchGroup;
			// throw new WrongArgumentIsNotBranchGroup(value);
		}
	}
	//
	public static int termToProjectionPolicy(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_PARALLEL_PROJECTION) {
				return View.PARALLEL_PROJECTION;
			} else if (code==SymbolCodes.symbolCode_E_PERSPECTIVE_PROJECTION) {
				return View.PERSPECTIVE_PROJECTION;
			} else {
				throw new WrongTermIsNotProjectionPolicy(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongTermIsNotProjectionPolicy(value);
		}
	}
	//
	public static int termToWindowResizePolicy(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_VIRTUAL_WORLD) {
				return View.VIRTUAL_WORLD;
			} else if (code==SymbolCodes.symbolCode_E_PHYSICAL_WORLD) {
				return View.PHYSICAL_WORLD;
			} else {
				throw new WrongTermIsNotWindowResizePolicy(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongTermIsNotWindowResizePolicy(value);
		}
	}
	//
	public static int termToWindowMovementPolicy(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_VIRTUAL_WORLD) {
				return View.VIRTUAL_WORLD;
			} else if (code==SymbolCodes.symbolCode_E_PHYSICAL_WORLD) {
				return View.PHYSICAL_WORLD;
			} else {
				throw new WrongTermIsNotWindowMovementPolicy(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongTermIsNotWindowMovementPolicy(value);
		}
	}
	//
	public static int termToVisibilityPolicy(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_VISIBILITY_DRAW_VISIBLE) {
				return View.VISIBILITY_DRAW_VISIBLE;
			} else if (code==SymbolCodes.symbolCode_E_VISIBILITY_DRAW_INVISIBLE) {
				return View.VISIBILITY_DRAW_INVISIBLE;
			} else if (code==SymbolCodes.symbolCode_E_VISIBILITY_DRAW_ALL) {
				return View.VISIBILITY_DRAW_ALL;
			} else {
				throw new WrongTermIsNotVisibilityPolicy(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongTermIsNotVisibilityPolicy(value);
		}
	}
	//
	public static int termToTransparencySortingPolicy(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_TRANSPARENCY_SORT_NONE) {
				return View.TRANSPARENCY_SORT_NONE;
			} else if (code==SymbolCodes.symbolCode_E_TRANSPARENCY_SORT_GEOMETRY) {
				return View.TRANSPARENCY_SORT_GEOMETRY;
			} else {
				throw new WrongTermIsNotTransparencySortingPolicy(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongTermIsNotTransparencySortingPolicy(value);
		}
	}
	//
	public static double termToFieldOfView(Term value, ChoisePoint iX) throws TermIsSymbolDefault {
		try {
			return Converters.termToReal(value,iX);
		} catch (TermIsNotAReal e1) {
			try {
				long code= value.getSymbolValue(iX);
				if (code==SymbolCodes.symbolCode_E_default) {
					throw new TermIsSymbolDefault();
				} else {
					throw new WrongTermIsNotFieldOfView(value);
				}
			} catch (TermIsNotASymbol e2) {
				throw new WrongTermIsNotFieldOfView(value);
			}
		}
	}
	//
	public static double termToFrontClipDistance(Term value, ChoisePoint iX) throws TermIsSymbolDefault {
		try {
			return Converters.termToReal(value,iX);
		} catch (TermIsNotAReal e1) {
			try {
				long code= value.getSymbolValue(iX);
				if (code==SymbolCodes.symbolCode_E_default) {
					throw new TermIsSymbolDefault();
				} else {
					throw new WrongTermIsNotFrontClipDistance(value);
				}
			} catch (TermIsNotASymbol e2) {
				throw new WrongTermIsNotFrontClipDistance(value);
			}
		}
	}
	//
	public static double termToBackClipDistance(Term value, ChoisePoint iX) throws TermIsSymbolDefault {
		try {
			return Converters.termToReal(value,iX);
		} catch (TermIsNotAReal e1) {
			try {
				long code= value.getSymbolValue(iX);
				if (code==SymbolCodes.symbolCode_E_default) {
					throw new TermIsSymbolDefault();
				} else {
					throw new WrongTermIsNotBackClipDistance(value);
				}
			} catch (TermIsNotASymbol e2) {
				throw new WrongTermIsNotBackClipDistance(value);
			}
		}
	}
}
