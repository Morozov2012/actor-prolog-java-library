// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system;

import morozov.system.colormaps.*;
import morozov.system.modes.*;

public enum ColorMapName {
	AQUA {
		public int[][] toColors(int size) {
			return colorMapAqua.getColorMap(size,colorMapAlpha);
		}
		public DataColorMap toDataColorMap() {
			return DataColorMap.AQUA;
		}
	},
	AUTUMN {
		public int[][] toColors(int size) {
			return colorMapAutumn.getColorMap(size,colorMapAlpha);
		}
		public DataColorMap toDataColorMap() {
			return DataColorMap.AUTUMN;
		}
	},
	BLACKHOT {
		public int[][] toColors(int size) {
			return colorMapBlackhot.getColorMap(size,colorMapAlpha);
		}
		public DataColorMap toDataColorMap() {
			return DataColorMap.BLACKHOT;
		}
	},
	BLAZE {
		public int[][] toColors(int size) {
			return colorMapBlaze.getColorMap(size,colorMapAlpha);
		}
		public DataColorMap toDataColorMap() {
			return DataColorMap.BLAZE;
		}
	},
	BLUERED {
		public int[][] toColors(int size) {
			return colorMapBlueRed.getColorMap(size,colorMapAlpha);
		}
		public DataColorMap toDataColorMap() {
			return DataColorMap.BLUERED;
		}
	},
	BONE {
		public int[][] toColors(int size) {
			return colorMapBone.getColorMap(size,colorMapAlpha);
		}
		public DataColorMap toDataColorMap() {
			return DataColorMap.BONE;
		}
	},
	BRIGHT_RAINBOW {
		public int getRepetitionFactor() {
			return 5;
		}
		public int[][] toColors(int size) {
			return colorMapBrightRainbow.getColorMap(size,colorMapAlpha);
		}
		public DataColorMap toDataColorMap() {
			return DataColorMap.BRIGHT_RAINBOW;
		}
	},
	COOL {
		public int[][] toColors(int size) {
			return colorMapCool.getColorMap(size,colorMapAlpha);
		}
		public DataColorMap toDataColorMap() {
			return DataColorMap.COOL;
		}
	},
	COPPER {
		public int[][] toColors(int size) {
			return colorMapCopper.getColorMap(size,colorMapAlpha);
		}
		public DataColorMap toDataColorMap() {
			return DataColorMap.COPPER;
		}
	},
	GRAY {
		public int[][] toColors(int size) {
			return colorMapGray.getColorMap(size,colorMapAlpha);
		}
		public DataColorMap toDataColorMap() {
			return DataColorMap.GRAY;
		}
	},
	GREEN {
		public int[][] toColors(int size) {
			return colorMapGreen.getColorMap(size,colorMapAlpha);
		}
		public DataColorMap toDataColorMap() {
			return DataColorMap.GREEN;
		}
	},
	HOT {
		public int getRepetitionFactor() {
			return 8;
		}
		public int[][] toColors(int size) {
			return colorMapHot.getColorMap(size,colorMapAlpha);
		}
		public DataColorMap toDataColorMap() {
			return DataColorMap.HOT;
		}
	},
	HSV {
		public int getRepetitionFactor() {
			return 6;
		}
		public int[][] toColors(int size) {
			return colorMapHSV.getColorMap(size,colorMapAlpha);
		}
		public DataColorMap toDataColorMap() {
			return DataColorMap.HSV;
		}
	},
	IRON {
		public int[][] toColors(int size) {
			return colorMapIron.getColorMap(size,colorMapAlpha);
		}
		public DataColorMap toDataColorMap() {
			return DataColorMap.IRON;
		}
	},
	JET {
		public int getRepetitionFactor() {
			return 8;
		}
		public int[][] toColors(int size) {
			return colorMapJet.getColorMap(size,colorMapAlpha);
		}
		public DataColorMap toDataColorMap() {
			return DataColorMap.JET;
		}
	},
	LIGHTJET {
		public int getRepetitionFactor() {
			return 8;
		}
		public int[][] toColors(int size) {
			return colorMapLightJet.getColorMap(size,colorMapAlpha);
		}
		public DataColorMap toDataColorMap() {
			return DataColorMap.LIGHTJET;
		}
	},
	MEDICAL {
		public int[][] toColors(int size) {
			return colorMapMedical.getColorMap(size,colorMapAlpha);
		}
		public DataColorMap toDataColorMap() {
			return DataColorMap.MEDICAL;
		}
	},
	OCEAN {
		public int getRepetitionFactor() {
			return 3;
		}
		public int[][] toColors(int size) {
			return colorMapOcean.getColorMap(size,colorMapAlpha);
		}
		public DataColorMap toDataColorMap() {
			return DataColorMap.OCEAN;
		}
	},
	PARULA {
		public int[][] toColors(int size) {
			return colorMapParula.getColorMap(size,colorMapAlpha);
		}
		public DataColorMap toDataColorMap() {
			return DataColorMap.PARULA;
		}
	},
	PINK {
		public int[][] toColors(int size) {
			return colorMapPink.getColorMap(size,colorMapAlpha);
		}
		public DataColorMap toDataColorMap() {
			return DataColorMap.PINK;
		}
	},
	PRISM {
		public int getRepetitionFactor() {
			return 6;
		}
		public int[][] toColors(int size) {
			return colorMapPrism.getColorMap(size,colorMapAlpha);
		}
		public DataColorMap toDataColorMap() {
			return DataColorMap.PRISM;
		}
	},
	PURPLE {
		public int[][] toColors(int size) {
			return colorMapPurple.getColorMap(size,colorMapAlpha);
		}
		public DataColorMap toDataColorMap() {
			return DataColorMap.PURPLE;
		}
	},
	RED {
		public int[][] toColors(int size) {
			return colorMapRed.getColorMap(size,colorMapAlpha);
		}
		public DataColorMap toDataColorMap() {
			return DataColorMap.RED;
		}
	},
	REPTILOID {
		public int[][] toColors(int size) {
			return colorMapReptiloid.getColorMap(size,colorMapAlpha);
		}
		public DataColorMap toDataColorMap() {
			return DataColorMap.REPTILOID;
		}
	},
	SOFT_RAINBOW {
		public int[][] toColors(int size) {
			return colorMapSoftRainbow.getColorMap(size,colorMapAlpha);
		}
		public DataColorMap toDataColorMap() {
			return DataColorMap.SOFT_RAINBOW;
		}
	},
	SPRING {
		public int[][] toColors(int size) {
			return colorMapSpring.getColorMap(size,colorMapAlpha);
		}
		public DataColorMap toDataColorMap() {
			return DataColorMap.SPRING;
		}
	},
	SUMMER {
		public int[][] toColors(int size) {
			return colorMapSummer.getColorMap(size,colorMapAlpha);
		}
		public DataColorMap toDataColorMap() {
			return DataColorMap.SUMMER;
		}
	},
	WINTER {
		public int[][] toColors(int size) {
			return colorMapWinter.getColorMap(size,colorMapAlpha);
		}
		public DataColorMap toDataColorMap() {
			return DataColorMap.WINTER;
		}
	},
	OPTICAL {
		public int[][] toColors(int size) {
			return colorMapNone.getColorMap(size,colorMapAlpha);
		}
		public DataColorMap toDataColorMap() {
			return DataColorMap.OPTICAL;
		}
	},
	NONE {
		public int[][] toColors(int size) {
			return colorMapNone.getColorMap(size,colorMapAlpha);
		}
		public DataColorMap toDataColorMap() {
			return DataColorMap.NONE;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	public int getRepetitionFactor() {
		return 1;
	}
	//
	abstract public int[][] toColors(int size);
	abstract public DataColorMap toDataColorMap();
	//
	///////////////////////////////////////////////////////////////
	//
	protected static int maximalColorValue= 255;
	//
	public static ColorMapName defaultColorMapName= ColorMapName.LIGHTJET;
	//
	public static ColorMapName getDefaultColorMapName() {
		return defaultColorMapName;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static int colorMapAlpha= 255;
	//
	protected static ColorMapRGBA colorMapAqua= new AquaColorMap();
	protected static ColorMapRGBA colorMapAutumn= new AutumnColorMap();
	protected static ColorMapRGBA colorMapBlackhot= new BlackhotColorMap();
	protected static ColorMapRGBA colorMapBlaze= new BlazeColorMap();
	protected static ColorMapRGBA colorMapBlueRed= new BlueRedColorMap();
	protected static ColorMapRGBA colorMapBone= new BoneColorMap();
	protected static ColorMapRGBA colorMapBrightRainbow= new BrightRainbowColorMap();
	protected static ColorMapRGBA colorMapCool= new CoolColorMap();
	protected static ColorMapRGBA colorMapCopper= new CopperColorMap();
	protected static ColorMapRGBA colorMapGray= new GrayColorMap();
	protected static ColorMapRGBA colorMapGreen= new GreenColorMap();
	protected static ColorMapRGBA colorMapHot= new HotColorMap();
	protected static ColorMapRGBA colorMapHSV= new HSVColorMap();
	protected static ColorMapRGBA colorMapIron= new IronColorMap();
	protected static ColorMapRGBA colorMapJet= new JetColorMap();
	protected static ColorMapRGBA colorMapLightJet= new LightJetColorMap();
	protected static ColorMapRGBA colorMapMedical= new MedicalColorMap();
	protected static ColorMapRGBA colorMapOcean= new OceanColorMap();
	protected static ColorMapRGBA colorMapParula= new ParulaColorMap();
	protected static ColorMapRGBA colorMapPink= new PinkColorMap();
	protected static ColorMapRGBA colorMapPrism= new PrismColorMap();
	protected static ColorMapRGBA colorMapPurple= new PurpleColorMap();
	protected static ColorMapRGBA colorMapRed= new RedColorMap();
	protected static ColorMapRGBA colorMapReptiloid= new ReptiloidColorMap();
	protected static ColorMapRGBA colorMapSoftRainbow= new SoftRainbowColorMap();
	protected static ColorMapRGBA colorMapSpring= new SpringColorMap();
	protected static ColorMapRGBA colorMapSummer= new SummerColorMap();
	protected static ColorMapRGBA colorMapWinter= new WinterColorMap();
	protected static ColorMapRGBA colorMapNone= new NoneColorMap();
}
