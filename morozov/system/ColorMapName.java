// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system;

import morozov.system.colormaps.*;
import morozov.system.modes.*;

public enum ColorMapName {
	//
	AQUA {
		@Override
		public int[][] toColors(int size, IntegerAttribute paletteIterations) {
			return colorMapAqua.getColorMap(size,paletteIterations,colorMapAlpha);
		}
		@Override
		public int[][] toColors(int size) {
			return colorMapAqua.getColorMap(size,colorMapAlpha);
		}
		@Override
		public int computeNumberOfBands(IntegerAttribute paletteIterations) {
			return colorMapAqua.computeNumberOfBands(paletteIterations);
		}
		@Override
		public DataColorMap toDataColorMap() {
			return DataColorMap.AQUA;
		}
	},
	AUTUMN {
		@Override
		public int[][] toColors(int size, IntegerAttribute paletteIterations) {
			return colorMapAutumn.getColorMap(size,paletteIterations,colorMapAlpha);
		}
		@Override
		public int[][] toColors(int size) {
			return colorMapAutumn.getColorMap(size,colorMapAlpha);
		}
		@Override
		public int computeNumberOfBands(IntegerAttribute paletteIterations) {
			return colorMapAutumn.computeNumberOfBands(paletteIterations);
		}
		@Override
		public DataColorMap toDataColorMap() {
			return DataColorMap.AUTUMN;
		}
	},
	BLACKHOT {
		@Override
		public int[][] toColors(int size, IntegerAttribute paletteIterations) {
			return colorMapBlackhot.getColorMap(size,paletteIterations,colorMapAlpha);
		}
		@Override
		public int[][] toColors(int size) {
			return colorMapBlackhot.getColorMap(size,colorMapAlpha);
		}
		@Override
		public int computeNumberOfBands(IntegerAttribute paletteIterations) {
			return colorMapBlackhot.computeNumberOfBands(paletteIterations);
		}
		@Override
		public DataColorMap toDataColorMap() {
			return DataColorMap.BLACKHOT;
		}
	},
	BLAZE {
		@Override
		public int[][] toColors(int size, IntegerAttribute paletteIterations) {
			return colorMapBlaze.getColorMap(size,paletteIterations,colorMapAlpha);
		}
		@Override
		public int[][] toColors(int size) {
			return colorMapBlaze.getColorMap(size,colorMapAlpha);
		}
		@Override
		public int computeNumberOfBands(IntegerAttribute paletteIterations) {
			return colorMapBlaze.computeNumberOfBands(paletteIterations);
		}
		@Override
		public DataColorMap toDataColorMap() {
			return DataColorMap.BLAZE;
		}
	},
	BLUERED {
		@Override
		public int[][] toColors(int size, IntegerAttribute paletteIterations) {
			return colorMapBlueRed.getColorMap(size,paletteIterations,colorMapAlpha);
		}
		@Override
		public int[][] toColors(int size) {
			return colorMapBlueRed.getColorMap(size,colorMapAlpha);
		}
		@Override
		public int computeNumberOfBands(IntegerAttribute paletteIterations) {
			return colorMapBlueRed.computeNumberOfBands(paletteIterations);
		}
		@Override
		public DataColorMap toDataColorMap() {
			return DataColorMap.BLUERED;
		}
	},
	BONE {
		@Override
		public int[][] toColors(int size, IntegerAttribute paletteIterations) {
			return colorMapBone.getColorMap(size,paletteIterations,colorMapAlpha);
		}
		@Override
		public int[][] toColors(int size) {
			return colorMapBone.getColorMap(size,colorMapAlpha);
		}
		@Override
		public int computeNumberOfBands(IntegerAttribute paletteIterations) {
			return colorMapBone.computeNumberOfBands(paletteIterations);
		}
		@Override
		public DataColorMap toDataColorMap() {
			return DataColorMap.BONE;
		}
	},
	BRIGHT_RAINBOW {
		@Override
		public int[][] toColors(int size, IntegerAttribute paletteIterations) {
			return colorMapBrightRainbow.getColorMap(size,paletteIterations,colorMapAlpha);
		}
		@Override
		public int[][] toColors(int size) {
			return colorMapBrightRainbow.getColorMap(size,colorMapAlpha);
		}
		@Override
		public int computeNumberOfBands(IntegerAttribute paletteIterations) {
			return colorMapBrightRainbow.computeNumberOfBands(paletteIterations);
		}
		@Override
		public DataColorMap toDataColorMap() {
			return DataColorMap.BRIGHT_RAINBOW;
		}
	},
	COOL {
		@Override
		public int[][] toColors(int size, IntegerAttribute paletteIterations) {
			return colorMapCool.getColorMap(size,paletteIterations,colorMapAlpha);
		}
		@Override
		public int[][] toColors(int size) {
			return colorMapCool.getColorMap(size,colorMapAlpha);
		}
		@Override
		public int computeNumberOfBands(IntegerAttribute paletteIterations) {
			return colorMapCool.computeNumberOfBands(paletteIterations);
		}
		@Override
		public DataColorMap toDataColorMap() {
			return DataColorMap.COOL;
		}
	},
	COPPER {
		@Override
		public int[][] toColors(int size, IntegerAttribute paletteIterations) {
			return colorMapCopper.getColorMap(size,paletteIterations,colorMapAlpha);
		}
		@Override
		public int[][] toColors(int size) {
			return colorMapCopper.getColorMap(size,colorMapAlpha);
		}
		@Override
		public int computeNumberOfBands(IntegerAttribute paletteIterations) {
			return colorMapCopper.computeNumberOfBands(paletteIterations);
		}
		@Override
		public DataColorMap toDataColorMap() {
			return DataColorMap.COPPER;
		}
	},
	GRAY {
		@Override
		public int[][] toColors(int size, IntegerAttribute paletteIterations) {
			return colorMapGray.getColorMap(size,paletteIterations,colorMapAlpha);
		}
		@Override
		public int[][] toColors(int size) {
			return colorMapGray.getColorMap(size,colorMapAlpha);
		}
		@Override
		public int computeNumberOfBands(IntegerAttribute paletteIterations) {
			return colorMapGray.computeNumberOfBands(paletteIterations);
		}
		@Override
		public DataColorMap toDataColorMap() {
			return DataColorMap.GRAY;
		}
	},
	GREEN {
		@Override
		public int[][] toColors(int size, IntegerAttribute paletteIterations) {
			return colorMapGreen.getColorMap(size,paletteIterations,colorMapAlpha);
		}
		@Override
		public int[][] toColors(int size) {
			return colorMapGreen.getColorMap(size,colorMapAlpha);
		}
		@Override
		public int computeNumberOfBands(IntegerAttribute paletteIterations) {
			return colorMapGreen.computeNumberOfBands(paletteIterations);
		}
		@Override
		public DataColorMap toDataColorMap() {
			return DataColorMap.GREEN;
		}
	},
	HOT {
		@Override
		public int[][] toColors(int size, IntegerAttribute paletteIterations) {
			return colorMapHot.getColorMap(size,paletteIterations,colorMapAlpha);
		}
		@Override
		public int[][] toColors(int size) {
			return colorMapHot.getColorMap(size,colorMapAlpha);
		}
		@Override
		public int computeNumberOfBands(IntegerAttribute paletteIterations) {
			return colorMapHot.computeNumberOfBands(paletteIterations);
		}
		@Override
		public DataColorMap toDataColorMap() {
			return DataColorMap.HOT;
		}
	},
	HSV {
		@Override
		public int[][] toColors(int size, IntegerAttribute paletteIterations) {
			return colorMapHSV.getColorMap(size,paletteIterations,colorMapAlpha);
		}
		@Override
		public int[][] toColors(int size) {
			return colorMapHSV.getColorMap(size,colorMapAlpha);
		}
		@Override
		public int computeNumberOfBands(IntegerAttribute paletteIterations) {
			return colorMapHSV.computeNumberOfBands(paletteIterations);
		}
		@Override
		public DataColorMap toDataColorMap() {
			return DataColorMap.HSV;
		}
	},
	IRON {
		@Override
		public int[][] toColors(int size, IntegerAttribute paletteIterations) {
			return colorMapIron.getColorMap(size,paletteIterations,colorMapAlpha);
		}
		@Override
		public int[][] toColors(int size) {
			return colorMapIron.getColorMap(size,colorMapAlpha);
		}
		@Override
		public int computeNumberOfBands(IntegerAttribute paletteIterations) {
			return colorMapIron.computeNumberOfBands(paletteIterations);
		}
		@Override
		public DataColorMap toDataColorMap() {
			return DataColorMap.IRON;
		}
	},
	JET {
		@Override
		public int[][] toColors(int size, IntegerAttribute paletteIterations) {
			return colorMapJet.getColorMap(size,paletteIterations,colorMapAlpha);
		}
		@Override
		public int[][] toColors(int size) {
			return colorMapJet.getColorMap(size,colorMapAlpha);
		}
		@Override
		public int computeNumberOfBands(IntegerAttribute paletteIterations) {
			return colorMapJet.computeNumberOfBands(paletteIterations);
		}
		@Override
		public DataColorMap toDataColorMap() {
			return DataColorMap.JET;
		}
	},
	LIGHTJET {
		@Override
		public int[][] toColors(int size, IntegerAttribute paletteIterations) {
			return colorMapLightJet.getColorMap(size,paletteIterations,colorMapAlpha);
		}
		@Override
		public int[][] toColors(int size) {
			return colorMapLightJet.getColorMap(size,colorMapAlpha);
		}
		@Override
		public int computeNumberOfBands(IntegerAttribute paletteIterations) {
			return colorMapLightJet.computeNumberOfBands(paletteIterations);
		}
		@Override
		public DataColorMap toDataColorMap() {
			return DataColorMap.LIGHTJET;
		}
	},
	MEDICAL {
		@Override
		public int[][] toColors(int size, IntegerAttribute paletteIterations) {
			return colorMapMedical.getColorMap(size,paletteIterations,colorMapAlpha);
		}
		@Override
		public int[][] toColors(int size) {
			return colorMapMedical.getColorMap(size,colorMapAlpha);
		}
		@Override
		public int computeNumberOfBands(IntegerAttribute paletteIterations) {
			return colorMapMedical.computeNumberOfBands(paletteIterations);
		}
		@Override
		public DataColorMap toDataColorMap() {
			return DataColorMap.MEDICAL;
		}
	},
	OCEAN {
		@Override
		public int[][] toColors(int size, IntegerAttribute paletteIterations) {
			return colorMapOcean.getColorMap(size,paletteIterations,colorMapAlpha);
		}
		@Override
		public int[][] toColors(int size) {
			return colorMapOcean.getColorMap(size,colorMapAlpha);
		}
		@Override
		public int computeNumberOfBands(IntegerAttribute paletteIterations) {
			return colorMapOcean.computeNumberOfBands(paletteIterations);
		}
		@Override
		public DataColorMap toDataColorMap() {
			return DataColorMap.OCEAN;
		}
	},
	PARULA {
		@Override
		public int[][] toColors(int size, IntegerAttribute paletteIterations) {
			return colorMapParula.getColorMap(size,paletteIterations,colorMapAlpha);
		}
		@Override
		public int[][] toColors(int size) {
			return colorMapParula.getColorMap(size,colorMapAlpha);
		}
		@Override
		public int computeNumberOfBands(IntegerAttribute paletteIterations) {
			return colorMapParula.computeNumberOfBands(paletteIterations);
		}
		@Override
		public DataColorMap toDataColorMap() {
			return DataColorMap.PARULA;
		}
	},
	PINK {
		@Override
		public int[][] toColors(int size, IntegerAttribute paletteIterations) {
			return colorMapPink.getColorMap(size,paletteIterations,colorMapAlpha);
		}
		@Override
		public int[][] toColors(int size) {
			return colorMapPink.getColorMap(size,colorMapAlpha);
		}
		@Override
		public int computeNumberOfBands(IntegerAttribute paletteIterations) {
			return colorMapPink.computeNumberOfBands(paletteIterations);
		}
		@Override
		public DataColorMap toDataColorMap() {
			return DataColorMap.PINK;
		}
	},
	PRISM {
		@Override
		public int[][] toColors(int size, IntegerAttribute paletteIterations) {
			return colorMapPrism.getColorMap(size,paletteIterations,colorMapAlpha);
		}
		@Override
		public int[][] toColors(int size) {
			return colorMapPrism.getColorMap(size,colorMapAlpha);
		}
		@Override
		public int computeNumberOfBands(IntegerAttribute paletteIterations) {
			return colorMapPrism.computeNumberOfBands(paletteIterations);
		}
		@Override
		public DataColorMap toDataColorMap() {
			return DataColorMap.PRISM;
		}
	},
	PURPLE {
		@Override
		public int[][] toColors(int size, IntegerAttribute paletteIterations) {
			return colorMapPurple.getColorMap(size,paletteIterations,colorMapAlpha);
		}
		@Override
		public int[][] toColors(int size) {
			return colorMapPurple.getColorMap(size,colorMapAlpha);
		}
		@Override
		public int computeNumberOfBands(IntegerAttribute paletteIterations) {
			return colorMapPurple.computeNumberOfBands(paletteIterations);
		}
		@Override
		public DataColorMap toDataColorMap() {
			return DataColorMap.PURPLE;
		}
	},
	RED {
		@Override
		public int[][] toColors(int size, IntegerAttribute paletteIterations) {
			return colorMapRed.getColorMap(size,paletteIterations,colorMapAlpha);
		}
		@Override
		public int[][] toColors(int size) {
			return colorMapRed.getColorMap(size,colorMapAlpha);
		}
		@Override
		public int computeNumberOfBands(IntegerAttribute paletteIterations) {
			return colorMapRed.computeNumberOfBands(paletteIterations);
		}
		@Override
		public DataColorMap toDataColorMap() {
			return DataColorMap.RED;
		}
	},
	REPTILOID {
		@Override
		public int[][] toColors(int size, IntegerAttribute paletteIterations) {
			return colorMapReptiloid.getColorMap(size,paletteIterations,colorMapAlpha);
		}
		@Override
		public int[][] toColors(int size) {
			return colorMapReptiloid.getColorMap(size,colorMapAlpha);
		}
		@Override
		public int computeNumberOfBands(IntegerAttribute paletteIterations) {
			return colorMapReptiloid.computeNumberOfBands(paletteIterations);
		}
		@Override
		public DataColorMap toDataColorMap() {
			return DataColorMap.REPTILOID;
		}
	},
	SOFT_RAINBOW {
		@Override
		public int[][] toColors(int size, IntegerAttribute paletteIterations) {
			return colorMapSoftRainbow.getColorMap(size,paletteIterations,colorMapAlpha);
		}
		@Override
		public int[][] toColors(int size) {
			return colorMapSoftRainbow.getColorMap(size,colorMapAlpha);
		}
		@Override
		public int computeNumberOfBands(IntegerAttribute paletteIterations) {
			return colorMapSoftRainbow.computeNumberOfBands(paletteIterations);
		}
		@Override
		public DataColorMap toDataColorMap() {
			return DataColorMap.SOFT_RAINBOW;
		}
	},
	SPRING {
		@Override
		public int[][] toColors(int size, IntegerAttribute paletteIterations) {
			return colorMapSpring.getColorMap(size,paletteIterations,colorMapAlpha);
		}
		@Override
		public int[][] toColors(int size) {
			return colorMapSpring.getColorMap(size,colorMapAlpha);
		}
		@Override
		public int computeNumberOfBands(IntegerAttribute paletteIterations) {
			return colorMapSpring.computeNumberOfBands(paletteIterations);
		}
		@Override
		public DataColorMap toDataColorMap() {
			return DataColorMap.SPRING;
		}
	},
	SUMMER {
		@Override
		public int[][] toColors(int size, IntegerAttribute paletteIterations) {
			return colorMapSummer.getColorMap(size,paletteIterations,colorMapAlpha);
		}
		@Override
		public int[][] toColors(int size) {
			return colorMapSummer.getColorMap(size,colorMapAlpha);
		}
		@Override
		public int computeNumberOfBands(IntegerAttribute paletteIterations) {
			return colorMapSummer.computeNumberOfBands(paletteIterations);
		}
		@Override
		public DataColorMap toDataColorMap() {
			return DataColorMap.SUMMER;
		}
	},
	WINTER {
		@Override
		public int[][] toColors(int size, IntegerAttribute paletteIterations) {
			return colorMapWinter.getColorMap(size,paletteIterations,colorMapAlpha);
		}
		@Override
		public int[][] toColors(int size) {
			return colorMapWinter.getColorMap(size,colorMapAlpha);
		}
		@Override
		public int computeNumberOfBands(IntegerAttribute paletteIterations) {
			return colorMapWinter.computeNumberOfBands(paletteIterations);
		}
		@Override
		public DataColorMap toDataColorMap() {
			return DataColorMap.WINTER;
		}
	},
	OPTICAL {
		@Override
		public int[][] toColors(int size, IntegerAttribute paletteIterations) {
			return colorMapNone.getColorMap(size,paletteIterations,colorMapAlpha);
		}
		@Override
		public int[][] toColors(int size) {
			return colorMapNone.getColorMap(size,colorMapAlpha);
		}
		@Override
		public int computeNumberOfBands(IntegerAttribute paletteIterations) {
			return colorMapNone.computeNumberOfBands(paletteIterations);
		}
		@Override
		public DataColorMap toDataColorMap() {
			return DataColorMap.OPTICAL;
		}
	},
	NONE {
		@Override
		public int[][] toColors(int size, IntegerAttribute paletteIterations) {
			return colorMapNone.getColorMap(size,paletteIterations,colorMapAlpha);
		}
		@Override
		public int[][] toColors(int size) {
			return colorMapNone.getColorMap(size,colorMapAlpha);
		}
		@Override
		public int computeNumberOfBands(IntegerAttribute paletteIterations) {
			return colorMapNone.computeNumberOfBands(paletteIterations);
		}
		@Override
		public DataColorMap toDataColorMap() {
			return DataColorMap.NONE;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public int[][] toColors(int size, IntegerAttribute paletteIterations);
	abstract public int[][] toColors(int size);
	abstract public int computeNumberOfBands(IntegerAttribute paletteIterations);
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
