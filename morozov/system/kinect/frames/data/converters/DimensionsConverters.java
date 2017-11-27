// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.data.converters;

import target.*;

import morozov.system.*;
import morozov.system.kinect.frames.data.converters.errors.*;
import morozov.system.kinect.frames.data.converters.signals.*;
import morozov.system.kinect.frames.data.interfaces.*;
import morozov.system.kinect.frames.tools.*;
import morozov.system.kinect.modes.*;
import morozov.system.kinect.modes.interfaces.*;
import morozov.terms.*;

import java.awt.Graphics2D;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Locale;
import java.util.ArrayList;
import java.util.HashMap;

public class DimensionsConverters {
	//
	public static Term skeletonsAndDimensionsToTerm(GeneralSkeletonInterface[] skeletons, DimensionsInterface dimensions, KinectDisplayingModeInterface displayingMode) {
		Term[] arrayOfSkeletons= skeletonsAndDimensionsToArray(skeletons,dimensions,displayingMode);
		return Converters.sparseArrayToList(arrayOfSkeletons);
	}
	//
	public static Term[] skeletonsAndDimensionsToArray(GeneralSkeletonInterface[] skeletons, DimensionsInterface dimensions, KinectDisplayingModeInterface displayingMode) {
		int numberOfItems= 0;
		if (skeletons != null) {
			numberOfItems= skeletons.length;
		} else if (dimensions != null) {
			numberOfItems= dimensions.getSize();
		};
		Term[] arrayOfSkeletons= new Term[numberOfItems];
		if (skeletons != null || dimensions != null) {
			for (int n=0; n < numberOfItems; n++) {
				GeneralSkeletonInterface skeleton;
				if (skeletons != null) {
					skeleton= skeletons[n];
				} else {
					skeleton= null;
				};
				SkeletonDimensionsInterface skeletonDimensions;
				if (dimensions != null) {
					skeletonDimensions= dimensions.getSkeletonDimensions(n);
				} else {
					skeletonDimensions= null;
				};
				Term localResult= skeletonAndDimensionsToTerm(skeleton,skeletonDimensions,null,displayingMode);
				arrayOfSkeletons[n]= localResult;
			}
		};
		return arrayOfSkeletons;
	}
	//
	public static Term skeletonAndDimensionsToTerm(
			GeneralSkeletonInterface skeleton,
			SkeletonDimensionsInterface skeletonDimensions,
			SkeletonTrack track,
			KinectDisplayingModeInterface displayingMode) {
		PlayerDimensionsInterface totalDepthDimensions= null;
		PlayerDimensionsInterface skeletonDepthDimensions= null;
		PlayerDimensionsInterface totalColorDimensions= null;
		PlayerDimensionsInterface skeletonColorDimensions= null;
		if (skeletonDimensions != null) {
			totalDepthDimensions= skeletonDimensions.getTotalDepthDimensions();
			skeletonDepthDimensions= skeletonDimensions.getSkeletonDepthDimensions();
			totalColorDimensions= skeletonDimensions.getTotalColorDimensions();
			skeletonColorDimensions= skeletonDimensions.getSkeletonColorDimensions();
		};
		Term localResult= PrologEmptySet.instance;
		boolean addElement= false;
		//
		// skeleton_rectangle
		//
		if (displayingMode.requiresCircumscriptionMode(KinectCircumscriptionMode.SKELETON_RECTANGLES)) {
			boolean flag1= skeletonDepthDimensions != null && skeletonDepthDimensions.areInitialized_2D_Bounds();
			boolean flag2= skeletonColorDimensions != null && skeletonColorDimensions.areInitialized_2D_Bounds();
			if (flag1 || flag2) {
				Term termSkeletonRectangle= rectangleToTerm(skeletonDepthDimensions,skeletonColorDimensions);
				localResult= new PrologSet(-SymbolCodes.symbolCode_E_skeleton_rectangle,termSkeletonRectangle,localResult);
				addElement= true;
			}
		};
		//
		// total_rectangle
		//
		if (displayingMode.requiresCircumscriptionMode(KinectCircumscriptionMode.TOTAL_RECTANGLES)) {
			boolean flag1= totalDepthDimensions != null && totalDepthDimensions.areInitialized_2D_Bounds();
			boolean flag2= totalColorDimensions != null && totalColorDimensions.areInitialized_2D_Bounds();
			if (flag1 || flag2) {
				Term termSkeletonRectangle= rectangleToTerm(totalDepthDimensions,totalColorDimensions);
				localResult= new PrologSet(-SymbolCodes.symbolCode_E_total_rectangle,termSkeletonRectangle,localResult);
				addElement= true;
			}
		};
		//
		// skeleton_parallelepiped
		//
		if (displayingMode.requiresCircumscriptionMode(KinectCircumscriptionMode.SKELETON_PARALLELEPIPEDS)) {
			boolean flag1= skeletonDepthDimensions != null && skeletonDepthDimensions.areInitialized_Depth3D_Bounds();
			boolean flag2= skeletonColorDimensions != null && skeletonColorDimensions.areInitialized_Depth3D_Bounds();
			if (flag1 || flag2) {
				PlayerDimensionsChangeInterface skeletonDepthDimensionsVelocities;
				PlayerDimensionsChangeInterface skeletonColorDimensionsVelocities;
				PlayerDimensionsChangeInterface skeletonDepthDimensionsAccelerations;
				PlayerDimensionsChangeInterface skeletonColorDimensionsAccelerations;
				if (track != null) {
					skeletonDepthDimensionsVelocities= track.getCurrentSkeletonDepthDimensionsVelocities();
					skeletonColorDimensionsVelocities= track.getCurrentSkeletonDepthDimensionsVelocities();
					skeletonDepthDimensionsAccelerations= track.getCurrentSkeletonDepthDimensionsAccelerations();
					skeletonColorDimensionsAccelerations= track.getCurrentSkeletonDepthDimensionsAccelerations();
				} else {
					skeletonDepthDimensionsVelocities= null;
					skeletonColorDimensionsVelocities= null;
					skeletonDepthDimensionsAccelerations= null;
					skeletonColorDimensionsAccelerations= null;
				};
				Term termSkeletonParallelepiped= parallelepipedToTerm(
					skeletonDepthDimensions,
					skeletonColorDimensions,
					skeletonDepthDimensionsVelocities,
					skeletonColorDimensionsVelocities,
					skeletonDepthDimensionsAccelerations,
					skeletonColorDimensionsAccelerations);
				localResult= new PrologSet(-SymbolCodes.symbolCode_E_skeleton_parallelepiped,termSkeletonParallelepiped,localResult);
				addElement= true;
			}
		};
		//
		// total_parallelepiped
		//
		if (displayingMode.requiresCircumscriptionMode(KinectCircumscriptionMode.TOTAL_PARALLELEPIPEDS)) {
			boolean flag1= totalDepthDimensions != null && totalDepthDimensions.areInitialized_Depth3D_Bounds();
			boolean flag2= totalColorDimensions != null && totalColorDimensions.areInitialized_Depth3D_Bounds();
			if (flag1 || flag2) {
				PlayerDimensionsChangeInterface totalDepthDimensionsVelocities;
				PlayerDimensionsChangeInterface totalColorDimensionsVelocities;
				PlayerDimensionsChangeInterface totalDepthDimensionsAccelerations;
				PlayerDimensionsChangeInterface totalColorDimensionsAccelerations;
				if (track != null) {
					totalDepthDimensionsVelocities= track.getCurrentTotalDepthDimensionsVelocities();
					totalColorDimensionsVelocities= track.getCurrentTotalDepthDimensionsVelocities();
					totalDepthDimensionsAccelerations= track.getCurrentTotalDepthDimensionsAccelerations();
					totalColorDimensionsAccelerations= track.getCurrentTotalDepthDimensionsAccelerations();
				} else {
					totalDepthDimensionsVelocities= null;
					totalColorDimensionsVelocities= null;
					totalDepthDimensionsAccelerations= null;
					totalColorDimensionsAccelerations= null;
				};
				Term termTotalParallelepiped= parallelepipedToTerm(
					totalDepthDimensions,
					totalColorDimensions,
					totalDepthDimensionsVelocities,
					totalColorDimensionsVelocities,
					totalDepthDimensionsAccelerations,
					totalColorDimensionsAccelerations);
				localResult= new PrologSet(-SymbolCodes.symbolCode_E_total_parallelepiped,termTotalParallelepiped,localResult);
				addElement= true;
			}
		};
		//
		if (	displayingMode.requiresSkeletonsMode(KinectSkeletonsMode.DETECT_SKELETONS) ||
			displayingMode.requiresSkeletonsMode(KinectSkeletonsMode.DETECT_AND_TRACK_SKELETONS)) {
			if (skeleton != null && skeleton.isTracked()) {
				localResult= GeneralSkeletonConverters.toTerm(skeleton,track,localResult);
				addElement= true;
			}
		};
		//
		if (addElement) {
			return localResult;
		} else {
			return null;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term rectangleToTerm(PlayerDimensionsInterface depthDimensions, PlayerDimensionsInterface colorDimensions) {
		Term termVertexPosition1= vertexPosition2DtoTerm(depthDimensions,colorDimensions,RectangleVertex.ONE);
		Term termVertexPosition2= vertexPosition2DtoTerm(depthDimensions,colorDimensions,RectangleVertex.TWO);
		Term termVertexPosition3= vertexPosition2DtoTerm(depthDimensions,colorDimensions,RectangleVertex.THREE);
		Term termVertexPosition4= vertexPosition2DtoTerm(depthDimensions,colorDimensions,RectangleVertex.FOUR);
		Term result= PrologEmptySet.instance;
		result= new PrologSet(-SymbolCodes.symbolCode_E_vertex4,termVertexPosition4,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_vertex3,termVertexPosition3,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_vertex2,termVertexPosition2,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_vertex1,termVertexPosition1,result);
		return result;
	}
	//
	protected static Term vertexPosition2DtoTerm(PlayerDimensionsInterface depthDimensions, PlayerDimensionsInterface colorDimensions, RectangleVertex vertex) {
		Term result= PrologEmptySet.instance;
		if (colorDimensions != null && colorDimensions.areInitialized_2D_Bounds()) {
			int colorX= selectRectangle_X(colorDimensions,vertex);
			int colorY= selectRectangle_Y(colorDimensions,vertex);
			Term termPositionX= new PrologInteger(colorX);
			Term termPositionY= new PrologInteger(colorY);
			Term termPosition= new PrologStructure(SymbolCodes.symbolCode_E_p,new Term[]{termPositionX,termPositionY});
			result= new PrologSet(-SymbolCodes.symbolCode_E_mapping2,termPosition,result);
		};
		if (depthDimensions != null && depthDimensions.areInitialized_2D_Bounds()) {
			int infraredX= selectRectangle_X(depthDimensions,vertex);
			int infraredY= selectRectangle_Y(depthDimensions,vertex);
			Term termPositionX= new PrologInteger(infraredX);
			Term termPositionY= new PrologInteger(infraredY);
			Term termPosition= new PrologStructure(SymbolCodes.symbolCode_E_p,new Term[]{termPositionX,termPositionY});
			result= new PrologSet(-SymbolCodes.symbolCode_E_mapping1,termPosition,result);
		};
		return result;
	}
	//
	protected static int selectRectangle_X(PlayerDimensionsInterface dimensions, RectangleVertex vertex) {
		switch (vertex) {
		case ONE:
			return dimensions.getMinimalX_2D();
		case TWO:
			return dimensions.getMaximalX_2D();
		case THREE:
			return dimensions.getMaximalX_2D();
		case FOUR:
			return dimensions.getMinimalX_2D();
		};
		throw new UnknownRectangleVertex(vertex);
	}
	protected static int selectRectangle_Y(PlayerDimensionsInterface dimensions, RectangleVertex vertex) {
		switch (vertex) {
		case ONE:
			return dimensions.getMinimalY_2D();
		case TWO:
			return dimensions.getMinimalY_2D();
		case THREE:
			return dimensions.getMaximalY_2D();
		case FOUR:
			return dimensions.getMaximalY_2D();
		};
		throw new UnknownRectangleVertex(vertex);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term parallelepipedToTerm(
			PlayerDimensionsInterface depthDimensions,
			PlayerDimensionsInterface colorDimensions,
			PlayerDimensionsChangeInterface depthDimensionsVelocities,
			PlayerDimensionsChangeInterface colorDimensionsVelocities,
			PlayerDimensionsChangeInterface depthDimensionsAccelerations,
			PlayerDimensionsChangeInterface colorDimensionsAccelerations) {
		Term termVertexPosition11= vertexPosition3DtoTerm(
			depthDimensions,
			colorDimensions,
			depthDimensionsVelocities,
			colorDimensionsVelocities,
			depthDimensionsAccelerations,
			colorDimensionsAccelerations,
			ParallelepipedVertex.NEAR_ONE);
		Term termVertexPosition12= vertexPosition3DtoTerm(
			depthDimensions,
			colorDimensions,
			depthDimensionsVelocities,
			colorDimensionsVelocities,
			depthDimensionsAccelerations,
			colorDimensionsAccelerations,
			ParallelepipedVertex.NEAR_TWO);
		Term termVertexPosition13= vertexPosition3DtoTerm(
			depthDimensions,
			colorDimensions,
			depthDimensionsVelocities,
			colorDimensionsVelocities,
			depthDimensionsAccelerations,
			colorDimensionsAccelerations,
			ParallelepipedVertex.NEAR_THREE);
		Term termVertexPosition14= vertexPosition3DtoTerm(
			depthDimensions,
			colorDimensions,
			depthDimensionsVelocities,
			colorDimensionsVelocities,
			depthDimensionsAccelerations,
			colorDimensionsAccelerations,
			ParallelepipedVertex.NEAR_FOUR);
		Term termVertexPosition21= vertexPosition3DtoTerm(
			depthDimensions,
			colorDimensions,
			depthDimensionsVelocities,
			colorDimensionsVelocities,
			depthDimensionsAccelerations,
			colorDimensionsAccelerations,
			ParallelepipedVertex.FAR_ONE);
		Term termVertexPosition22= vertexPosition3DtoTerm(
			depthDimensions,
			colorDimensions,
			depthDimensionsVelocities,
			colorDimensionsVelocities,
			depthDimensionsAccelerations,
			colorDimensionsAccelerations,
			ParallelepipedVertex.FAR_TWO);
		Term termVertexPosition23= vertexPosition3DtoTerm(
			depthDimensions,
			colorDimensions,
			depthDimensionsVelocities,
			colorDimensionsVelocities,
			depthDimensionsAccelerations,
			colorDimensionsAccelerations,
			ParallelepipedVertex.FAR_THREE);
		Term termVertexPosition24= vertexPosition3DtoTerm(
			depthDimensions,
			colorDimensions,
			depthDimensionsVelocities,
			colorDimensionsVelocities,
			depthDimensionsAccelerations,
			colorDimensionsAccelerations,
			ParallelepipedVertex.FAR_FOUR);
		Term result= PrologEmptySet.instance;
		result= new PrologSet(-SymbolCodes.symbolCode_E_vertex24,termVertexPosition24,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_vertex23,termVertexPosition23,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_vertex22,termVertexPosition22,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_vertex21,termVertexPosition21,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_vertex14,termVertexPosition14,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_vertex13,termVertexPosition13,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_vertex12,termVertexPosition12,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_vertex11,termVertexPosition11,result);
		return result;
	}
	//
	protected static Term vertexPosition3DtoTerm(
			PlayerDimensionsInterface depthDimensions,
			PlayerDimensionsInterface colorDimensions,
			PlayerDimensionsChangeInterface depthDimensionsVelocities,
			PlayerDimensionsChangeInterface colorDimensionsVelocities,
			PlayerDimensionsChangeInterface depthDimensionsAccelerations,
			PlayerDimensionsChangeInterface colorDimensionsAccelerations,
			ParallelepipedVertex vertex) {
		Term result= PrologEmptySet.instance;
		if (colorDimensions != null && colorDimensions.isInitialized_ColorParallelepiped()) {
			try {
				int colorX= selectParallelepiped2D_X(colorDimensions,vertex);
				int colorY= selectParallelepiped2D_Y(colorDimensions,vertex);
				Term termPositionX= new PrologInteger(colorX);
				Term termPositionY= new PrologInteger(colorY);
				Term termPosition= new PrologStructure(SymbolCodes.symbolCode_E_p,new Term[]{termPositionX,termPositionY});
				result= new PrologSet(-SymbolCodes.symbolCode_E_mapping2,termPosition,result);
			} catch (XYisUndefined e) {
			}
		};
		if (depthDimensions != null && depthDimensions.isInitialized_DepthParallelepiped()) {
			try {
				int infraredX= selectParallelepiped2D_X(depthDimensions,vertex);
				int infraredY= selectParallelepiped2D_Y(depthDimensions,vertex);
				Term termPositionX= new PrologInteger(infraredX);
				Term termPositionY= new PrologInteger(infraredY);
				Term termPosition= new PrologStructure(SymbolCodes.symbolCode_E_p,new Term[]{termPositionX,termPositionY});
				result= new PrologSet(-SymbolCodes.symbolCode_E_mapping1,termPosition,result);
			} catch (XYisUndefined e) {
			}
		};
		if (depthDimensions != null && depthDimensions.areInitialized_Depth3D_Bounds()) {
			if (depthDimensionsAccelerations != null && depthDimensionsAccelerations.areInitialized()) {
				float infraredDDx= selectParallelepiped3D_X(depthDimensionsAccelerations,vertex);
				float infraredDDy= selectParallelepiped3D_Y(depthDimensionsAccelerations,vertex);
				float infraredDDz= selectParallelepiped3D_Z(depthDimensionsAccelerations,vertex);
				Term termPositionDDx= new PrologReal(infraredDDx);
				Term termPositionDDy= new PrologReal(infraredDDy);
				Term termPositionDDz= new PrologReal(infraredDDz);
				Term termAcceleration= new PrologStructure(SymbolCodes.symbolCode_E_p,new Term[]{termPositionDDx,termPositionDDy,termPositionDDz});
				result= new PrologSet(-SymbolCodes.symbolCode_E_acceleration,termAcceleration,result);
			};
			if (depthDimensionsVelocities != null && depthDimensionsVelocities.areInitialized()) {
				float infraredDx= selectParallelepiped3D_X(depthDimensionsVelocities,vertex);
				float infraredDy= selectParallelepiped3D_Y(depthDimensionsVelocities,vertex);
				float infraredDz= selectParallelepiped3D_Z(depthDimensionsVelocities,vertex);
				Term termPositionDx= new PrologReal(infraredDx);
				Term termPositionDy= new PrologReal(infraredDy);
				Term termPositionDz= new PrologReal(infraredDz);
				Term termVelocity= new PrologStructure(SymbolCodes.symbolCode_E_p,new Term[]{termPositionDx,termPositionDy,termPositionDz});
				result= new PrologSet(-SymbolCodes.symbolCode_E_velocity,termVelocity,result);
			};
			float infraredX= selectParallelepiped3D_X(depthDimensions,vertex);
			float infraredY= selectParallelepiped3D_Y(depthDimensions,vertex);
			float infraredZ= selectParallelepiped3D_Z(depthDimensions,vertex);
			Term termPositionX= new PrologReal(infraredX);
			Term termPositionY= new PrologReal(infraredY);
			Term termPositionZ= new PrologReal(infraredZ);
			Term termPosition= new PrologStructure(SymbolCodes.symbolCode_E_p,new Term[]{termPositionX,termPositionY,termPositionZ});
			result= new PrologSet(-SymbolCodes.symbolCode_E_point,termPosition,result);
		} else if (colorDimensions != null && colorDimensions.areInitialized_Depth3D_Bounds()) {
			if (colorDimensionsAccelerations != null && colorDimensionsAccelerations.areInitialized()) {
				float infraredDDx= selectParallelepiped3D_X(colorDimensionsAccelerations,vertex);
				float infraredDDy= selectParallelepiped3D_Y(colorDimensionsAccelerations,vertex);
				float infraredDDz= selectParallelepiped3D_Z(colorDimensionsAccelerations,vertex);
				Term termPositionDDx= new PrologReal(infraredDDx);
				Term termPositionDDy= new PrologReal(infraredDDy);
				Term termPositionDDz= new PrologReal(infraredDDz);
				Term termAcceleration= new PrologStructure(SymbolCodes.symbolCode_E_p,new Term[]{termPositionDDx,termPositionDDy,termPositionDDz});
				result= new PrologSet(-SymbolCodes.symbolCode_E_acceleration,termAcceleration,result);
			};
			if (colorDimensionsVelocities != null && colorDimensionsVelocities.areInitialized()) {
				float infraredDx= selectParallelepiped3D_X(colorDimensionsVelocities,vertex);
				float infraredDy= selectParallelepiped3D_Y(colorDimensionsVelocities,vertex);
				float infraredDz= selectParallelepiped3D_Z(colorDimensionsVelocities,vertex);
				Term termPositionDx= new PrologReal(infraredDx);
				Term termPositionDy= new PrologReal(infraredDy);
				Term termPositionDz= new PrologReal(infraredDz);
				Term termVelocity= new PrologStructure(SymbolCodes.symbolCode_E_p,new Term[]{termPositionDx,termPositionDy,termPositionDz});
				result= new PrologSet(-SymbolCodes.symbolCode_E_velocity,termVelocity,result);
			};
			float infraredX= selectParallelepiped3D_X(colorDimensions,vertex);
			float infraredY= selectParallelepiped3D_Y(colorDimensions,vertex);
			float infraredZ= selectParallelepiped3D_Z(colorDimensions,vertex);
			Term termPositionX= new PrologReal(infraredX);
			Term termPositionY= new PrologReal(infraredY);
			Term termPositionZ= new PrologReal(infraredZ);
			Term termPosition= new PrologStructure(SymbolCodes.symbolCode_E_p,new Term[]{termPositionX,termPositionY,termPositionZ});
			result= new PrologSet(-SymbolCodes.symbolCode_E_point,termPosition,result);
		};
		return result;
	}
	//
	protected static int selectParallelepiped2D_X(PlayerDimensionsInterface dimensions, ParallelepipedVertex vertex) throws XYisUndefined {
		XY_Interface xy;
		switch (vertex) {
		case NEAR_ONE:
			xy= dimensions.getXY11();
			break;
		case NEAR_TWO:
			xy= dimensions.getXY12();
			break;
		case NEAR_THREE:
			xy= dimensions.getXY13();
			break;
		case NEAR_FOUR:
			xy= dimensions.getXY14();
			break;
		case FAR_ONE:
			xy= dimensions.getXY21();
			break;
		case FAR_TWO:
			xy= dimensions.getXY22();
			break;
		case FAR_THREE:
			xy= dimensions.getXY23();
			break;
		case FAR_FOUR:
			xy= dimensions.getXY24();
			break;
		default:
			throw new UnknownParallelepipedVertex(vertex);
		};
		if (xy != null) {
			return xy.getX();
		} else {
			throw XYisUndefined.instance;
		}
	}
	protected static int selectParallelepiped2D_Y(PlayerDimensionsInterface dimensions, ParallelepipedVertex vertex) throws XYisUndefined {
		XY_Interface xy;
		switch (vertex) {
		case NEAR_ONE:
			xy= dimensions.getXY11();
			break;
		case NEAR_TWO:
			xy= dimensions.getXY12();
			break;
		case NEAR_THREE:
			xy= dimensions.getXY13();
			break;
		case NEAR_FOUR:
			xy= dimensions.getXY14();
			break;
		case FAR_ONE:
			xy= dimensions.getXY21();
			break;
		case FAR_TWO:
			xy= dimensions.getXY22();
			break;
		case FAR_THREE:
			xy= dimensions.getXY23();
			break;
		case FAR_FOUR:
			xy= dimensions.getXY24();
			break;
		default:
			throw new UnknownParallelepipedVertex(vertex);
		};
		if (xy != null) {
			return xy.getY();
		} else {
			throw XYisUndefined.instance;
		}
	}
	//
	protected static float selectParallelepiped3D_X(PlayerDimensionsInterface dimensions, ParallelepipedVertex vertex) {
		switch (vertex) {
		case NEAR_ONE:
			return dimensions.getMinimalX_3D();
		case NEAR_TWO:
			return dimensions.getMaximalX_3D();
		case NEAR_THREE:
			return dimensions.getMaximalX_3D();
		case NEAR_FOUR:
			return dimensions.getMinimalX_3D();
		case FAR_ONE:
			return dimensions.getMinimalX_3D();
		case FAR_TWO:
			return dimensions.getMaximalX_3D();
		case FAR_THREE:
			return dimensions.getMaximalX_3D();
		case FAR_FOUR:
			return dimensions.getMinimalX_3D();
		};
		throw new UnknownParallelepipedVertex(vertex);
	}
	protected static float selectParallelepiped3D_Y(PlayerDimensionsInterface dimensions, ParallelepipedVertex vertex) {
		switch (vertex) {
		case NEAR_ONE:
			return dimensions.getMinimalY_3D();
		case NEAR_TWO:
			return dimensions.getMinimalY_3D();
		case NEAR_THREE:
			return dimensions.getMaximalY_3D();
		case NEAR_FOUR:
			return dimensions.getMaximalY_3D();
		case FAR_ONE:
			return dimensions.getMinimalY_3D();
		case FAR_TWO:
			return dimensions.getMinimalY_3D();
		case FAR_THREE:
			return dimensions.getMaximalY_3D();
		case FAR_FOUR:
			return dimensions.getMaximalY_3D();
		};
		throw new UnknownParallelepipedVertex(vertex);
	}
	protected static float selectParallelepiped3D_Z(PlayerDimensionsInterface dimensions, ParallelepipedVertex vertex) {
		switch (vertex) {
		case NEAR_ONE:
			return dimensions.getMinimalZ_3D();
		case NEAR_TWO:
			return dimensions.getMinimalZ_3D();
		case NEAR_THREE:
			return dimensions.getMinimalZ_3D();
		case NEAR_FOUR:
			return dimensions.getMinimalZ_3D();
		case FAR_ONE:
			return dimensions.getMaximalZ_3D();
		case FAR_TWO:
			return dimensions.getMaximalZ_3D();
		case FAR_THREE:
			return dimensions.getMaximalZ_3D();
		case FAR_FOUR:
			return dimensions.getMaximalZ_3D();
		};
		throw new UnknownParallelepipedVertex(vertex);
	}
	//
	protected static float selectParallelepiped3D_X(PlayerDimensionsChangeInterface dimensions, ParallelepipedVertex vertex) {
		switch (vertex) {
		case NEAR_ONE:
			return dimensions.getChangeOfMinimalX();
		case NEAR_TWO:
			return dimensions.getChangeOfMaximalX();
		case NEAR_THREE:
			return dimensions.getChangeOfMaximalX();
		case NEAR_FOUR:
			return dimensions.getChangeOfMinimalX();
		case FAR_ONE:
			return dimensions.getChangeOfMinimalX();
		case FAR_TWO:
			return dimensions.getChangeOfMaximalX();
		case FAR_THREE:
			return dimensions.getChangeOfMaximalX();
		case FAR_FOUR:
			return dimensions.getChangeOfMinimalX();
		};
		throw new UnknownParallelepipedVertex(vertex);
	}
	protected static float selectParallelepiped3D_Y(PlayerDimensionsChangeInterface dimensions, ParallelepipedVertex vertex) {
		switch (vertex) {
		case NEAR_ONE:
			return dimensions.getChangeOfMinimalY();
		case NEAR_TWO:
			return dimensions.getChangeOfMinimalY();
		case NEAR_THREE:
			return dimensions.getChangeOfMaximalY();
		case NEAR_FOUR:
			return dimensions.getChangeOfMaximalY();
		case FAR_ONE:
			return dimensions.getChangeOfMinimalY();
		case FAR_TWO:
			return dimensions.getChangeOfMinimalY();
		case FAR_THREE:
			return dimensions.getChangeOfMaximalY();
		case FAR_FOUR:
			return dimensions.getChangeOfMaximalY();
		};
		throw new UnknownParallelepipedVertex(vertex);
	}
	protected static float selectParallelepiped3D_Z(PlayerDimensionsChangeInterface dimensions, ParallelepipedVertex vertex) {
		switch (vertex) {
		case NEAR_ONE:
			return dimensions.getChangeOfMinimalZ();
		case NEAR_TWO:
			return dimensions.getChangeOfMinimalZ();
		case NEAR_THREE:
			return dimensions.getChangeOfMinimalZ();
		case NEAR_FOUR:
			return dimensions.getChangeOfMinimalZ();
		case FAR_ONE:
			return dimensions.getChangeOfMaximalZ();
		case FAR_TWO:
			return dimensions.getChangeOfMaximalZ();
		case FAR_THREE:
			return dimensions.getChangeOfMaximalZ();
		case FAR_FOUR:
			return dimensions.getChangeOfMaximalZ();
		};
		throw new UnknownParallelepipedVertex(vertex);
	}
}
