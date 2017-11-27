// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.gui.space3d;

import target.*;

import morozov.built_in.*;
import morozov.run.*;
import morozov.system.gui.space3d.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.View;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class Utils3D extends PrincipalNode3D {
	protected static Term termParallelProjection= new PrologSymbol(SymbolCodes.symbolCode_E_PARALLEL_PROJECTION);
	protected static Term termPerspectiveProjection= new PrologSymbol(SymbolCodes.symbolCode_E_PERSPECTIVE_PROJECTION);
	protected static Term termVirtualWorld= new PrologSymbol(SymbolCodes.symbolCode_E_VIRTUAL_WORLD);
	protected static Term termPhysicalWorld= new PrologSymbol(SymbolCodes.symbolCode_E_PHYSICAL_WORLD);
	protected static Term termVisibilityDrawVisible= new PrologSymbol(SymbolCodes.symbolCode_E_VISIBILITY_DRAW_VISIBLE);
	protected static Term termVisibilityDrawInvisible= new PrologSymbol(SymbolCodes.symbolCode_E_VISIBILITY_DRAW_INVISIBLE);
	protected static Term termVisibilityDrawAll= new PrologSymbol(SymbolCodes.symbolCode_E_VISIBILITY_DRAW_ALL);
	protected static Term termTransparencySortNone= new PrologSymbol(SymbolCodes.symbolCode_E_TRANSPARENCY_SORT_NONE);
	protected static Term termTransparencySortGeometry= new PrologSymbol(SymbolCodes.symbolCode_E_TRANSPARENCY_SORT_GEOMETRY);
	//protected static Term term= new PrologSymbol(SymbolCodes.symbolCode_E_);
	//protected static Term term= new PrologSymbol(SymbolCodes.symbolCode_E_);
	//protected static Term term= new PrologSymbol(SymbolCodes.symbolCode_E_);
	//
	public static BranchGroup argumentToBranchGroupOrNodeList(Term value, Canvas3D targetWorld, SimpleUniverse u, javax.media.j3d.Canvas3D space3D, ChoisePoint iX) {
		try { // BranchGroup
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_BranchGroup,1,iX);
			return attributesToBranchGroup(arguments[0],targetWorld,u,space3D,iX);
		} catch (Backtracking b) {
			BranchGroup branchGroup= new BranchGroup();
			argumentToListOfNodes(branchGroup,branchGroup,value,targetWorld,u,space3D,null,iX);
			return branchGroup;
			// throw new WrongArgumentIsNotBranchGroup(value);
		}
	}
	//
	public static int argumentToProjectionPolicy(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_PARALLEL_PROJECTION) {
				return View.PARALLEL_PROJECTION;
			} else if (code==SymbolCodes.symbolCode_E_PERSPECTIVE_PROJECTION) {
				return View.PERSPECTIVE_PROJECTION;
			} else {
				throw new WrongArgumentIsNotProjectionPolicy(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotProjectionPolicy(value);
		}
	}
	public static Term projectionPolicyToTerm(int value) {
		switch (value) {
			case View.PARALLEL_PROJECTION: return termParallelProjection;
			case View.PERSPECTIVE_PROJECTION: return termPerspectiveProjection;
			default: return termPerspectiveProjection;
		}
	}
	//
	public static int argumentToWindowResizePolicy(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_VIRTUAL_WORLD) {
				return View.VIRTUAL_WORLD;
			} else if (code==SymbolCodes.symbolCode_E_PHYSICAL_WORLD) {
				return View.PHYSICAL_WORLD;
			} else {
				throw new WrongArgumentIsNotWindowResizePolicy(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotWindowResizePolicy(value);
		}
	}
	public static Term windowResizePolicyToTerm(int value) {
		switch (value) {
			case View.VIRTUAL_WORLD: return termVirtualWorld;
			case View.PHYSICAL_WORLD: return termPhysicalWorld;
			default: return termPhysicalWorld;
		}
	}
	//
	public static int argumentToWindowMovementPolicy(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_VIRTUAL_WORLD) {
				return View.VIRTUAL_WORLD;
			} else if (code==SymbolCodes.symbolCode_E_PHYSICAL_WORLD) {
				return View.PHYSICAL_WORLD;
			} else {
				throw new WrongArgumentIsNotWindowMovementPolicy(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotWindowMovementPolicy(value);
		}
	}
	public static Term windowMovementPolicyToTerm(int value) {
		switch (value) {
			case View.VIRTUAL_WORLD: return termVirtualWorld;
			case View.PHYSICAL_WORLD: return termPhysicalWorld;
			default: return termPhysicalWorld;
		}
	}
	//
	public static int argumentToVisibilityPolicy(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_VISIBILITY_DRAW_VISIBLE) {
				return View.VISIBILITY_DRAW_VISIBLE;
			} else if (code==SymbolCodes.symbolCode_E_VISIBILITY_DRAW_INVISIBLE) {
				return View.VISIBILITY_DRAW_INVISIBLE;
			} else if (code==SymbolCodes.symbolCode_E_VISIBILITY_DRAW_ALL) {
				return View.VISIBILITY_DRAW_ALL;
			} else {
				throw new WrongArgumentIsNotVisibilityPolicy(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotVisibilityPolicy(value);
		}
	}
	public static Term visibilityPolicyToTerm(int value) {
		switch (value) {
			case View.VISIBILITY_DRAW_VISIBLE: return termVisibilityDrawVisible;
			case View.VISIBILITY_DRAW_INVISIBLE: return termVisibilityDrawInvisible;
			case View.VISIBILITY_DRAW_ALL: return termVisibilityDrawAll;
			default: return termVisibilityDrawVisible;
		}
	}
	//
	public static int argumentToTransparencySortingPolicy(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_TRANSPARENCY_SORT_NONE) {
				return View.TRANSPARENCY_SORT_NONE;
			} else if (code==SymbolCodes.symbolCode_E_TRANSPARENCY_SORT_GEOMETRY) {
				return View.TRANSPARENCY_SORT_GEOMETRY;
			} else {
				throw new WrongArgumentIsNotTransparencySortingPolicy(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotTransparencySortingPolicy(value);
		}
	}
	public static Term transparencySortingPolicyToTerm(int value) {
		switch (value) {
			case View.TRANSPARENCY_SORT_NONE: return termTransparencySortNone;
			case View.TRANSPARENCY_SORT_GEOMETRY: return termTransparencySortGeometry;
			default: return termTransparencySortNone;
		}
	}
}
