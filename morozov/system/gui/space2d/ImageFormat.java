// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import javax.imageio.ImageTypeSpecifier;

public enum ImageFormat {
	JPEG {
		public Space2DWriter createWriter(String defaultFormatName, ImageTypeSpecifier its) {
			return new Space2D_JPEG_Writer(its);
		}
	},
	PNG {
		public Space2DWriter createWriter(String defaultFormatName, ImageTypeSpecifier its) {
			return new Space2D_PNG_Writer(its);
		}
	},
	GIF {
		public Space2DWriter createWriter(String defaultFormatName, ImageTypeSpecifier its) {
			return new Space2D_GIF_Writer(its);
		}
	},
	BMP {
		public Space2DWriter createWriter(String defaultFormatName, ImageTypeSpecifier its) {
			return new Space2D_BMP_Writer(its);
		}
	},
	WBMP {
		public Space2DWriter createWriter(String defaultFormatName, ImageTypeSpecifier its) {
			return new Space2D_WBMP_Writer(its);
		}
	},
	UNIVERSAL {
		public Space2DWriter createWriter(String defaultFormatName, ImageTypeSpecifier its) {
			ImageFormat format= recognizeFormat(defaultFormatName);
			if (format==UNIVERSAL) {
				return new Space2DWriter(defaultFormatName,its);
			} else {
				return format.createWriter(defaultFormatName,its);
			}
		}
	};
	public abstract Space2DWriter createWriter(String defaultFormatName, ImageTypeSpecifier its);
	public static ImageFormat recognizeFormat(String name) {
		if (name.equalsIgnoreCase("jpg") || name.equalsIgnoreCase("jpeg")) {
			return JPEG;
		} else if (name.equalsIgnoreCase("png")) {
			return PNG;
		} else if (name.equalsIgnoreCase("gif")) {
			return GIF;
		} else if (name.equalsIgnoreCase("bmp")) {
			return BMP;
		} else if (name.equalsIgnoreCase("wbmp")) {
			return WBMP;
		} else {
			return UNIVERSAL;
		}
	}
}
