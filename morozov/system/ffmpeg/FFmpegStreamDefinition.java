// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg;

import static org.bytedeco.javacpp.avutil.*;
import static org.bytedeco.javacpp.avcodec.*;
import org.bytedeco.javacpp.*;

import target.*;

import morozov.run.*;
import morozov.system.converters.*;
import morozov.system.ffmpeg.converters.*;
import morozov.system.ffmpeg.errors.*;
import morozov.terms.*;
import morozov.terms.errors.*;
import morozov.terms.signals.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

public class FFmpegStreamDefinition {
	//
	protected FFmpegMediaType codecType;
	protected Integer codecId;
	protected Integer codecTag;
	protected Integer bitRate;
	// protected FFmpegCodecOption[] codecOptions;
	protected Integer bitRateTolerance;
	protected Integer globalQuality;
	protected Integer compressionLevel;
	protected AVRational timeBase;
	protected Integer sourceImageWidth;
	protected Integer sourceImageHeight;
	protected Integer destinationImageWidth;
	protected Integer destinationImageHeight;
	protected Integer gopSize;
	protected FFmpegPixelFormat pixFmt;
	protected Integer maxBFrames;
	protected Double bQuantFactor;
	protected Double bQuantOffset;
	protected Double iQuantFactor;
	protected Double iQuantOffset;
	protected Double lumiMasking;
	protected Double temporalCplxMasking;
	protected Double spatialCplxMasking;
	protected Double pMasking;
	protected Double darkMasking;
	protected AVRational sampleAspectRatio;
	protected FFmpegComparisonFunction meCmp;
	protected FFmpegComparisonFunction meSubCmp;
	protected FFmpegComparisonFunction mbCmp;
	protected FFmpegComparisonFunction ildctCmp;
	protected Integer diaSize;
	protected Integer lastPredictorCount;
	protected FFmpegComparisonFunction mePreCmp;
	protected Integer preDiaSize;
	protected Integer meSubpelQuality;
	protected Integer meRange;
	protected FFmpegMacroblockDecisionMode mbDecision;
	protected Integer intraDcPrecision;
	protected Integer mbLMin;
	protected Integer mbLMax;
	protected Integer bidirRefine;
	protected Integer keyintMin;
	protected Integer refs;
	protected Integer mv0Threshold;
	protected Integer colorPrimaries;
	protected Integer colorTrc;
	protected Integer colorspace;
	protected Integer colorRange;
	protected Integer chromaSampleLocation;
	protected Integer slices;
	protected Integer sampleRate;
	protected Integer channels;
	protected FFmpegAudioSampleFormat sampleFmt;
	protected Integer cutoff;
	protected FFmpegChannelLayout channelLayout;
	protected Integer audioServiceType;
	protected Double qCompress;
	protected Double qBlur;
	protected Integer qMin;
	protected Integer qMax;
	protected Integer maxQDiff;
	protected Integer rcBufferSize;
	// protected Integer rcOverrideCount;
	// protected Integer rcOverride;
	protected Integer rcMaxRate;
	protected Integer rcMinRate;
	protected Double rcMaxAvailableVbvUse;
	protected Double rcMinVbvOverflowUse;
	protected Integer rcInitialBufferOccupancy;
	protected Integer trellis;
	protected FFmpegWorkAroundBug[] workaroundBugs;
	protected FFmpegStandardCompliance strictStdCompliance;
	protected FFmpegDebugOption[] debug;
	protected FFmpeg_DCT_Algorithm dctAlgo;
	protected FFmpeg_IDCT_Algorithm idctAlgo;
	protected Integer bitsPerRawSample;
	protected Integer threadCount;
	protected FFmpegThreadType threadType;
	protected Integer threadSafeCallbacks;
	protected Integer nsseWeight;
	protected FFmpegProfile profile;
	protected FFmpegLevel level;
	protected FFmpegSubtitleTextFormat subTextFormat;
	protected Integer nbSamples;
	//
	protected static FFmpegMediaType defaultCodecType= new FFmpegMediaType(FFmpegMediaTypeName.AVMEDIA_TYPE_VIDEO);
	protected static FFmpegStreamDefinition[] defaultStreamDefinitionList= new FFmpegStreamDefinition[]{new FFmpegStreamDefinition()};
	//
	///////////////////////////////////////////////////////////////
	//
	public FFmpegStreamDefinition() {
		codecType= defaultCodecType;
	}
	public FFmpegStreamDefinition(
			FFmpegMediaType givenCodecType,
			Integer givenCodecId,
			Integer givenCodecTag,
			Integer givenBitRate,
			// FFmpegCodecOption[] givenCodecOptions,
			Integer givenBitRateTolerance,
			Integer givenGlobalQuality,
			Integer givenCompressionLevel,
			AVRational givenTimeBase,
			Integer givenSourceImageWidth,
			Integer givenSourceImageHeight,
			Integer givenDestinationImageWidth,
			Integer givenDestinationImageHeight,
			Integer givenGopSize,
			FFmpegPixelFormat givenPixFmt,
			Integer givenMaxBFrames,
			Double givenBQuantFactor,
			Double givenBQuantOffset,
			Double givenIQuantFactor,
			Double givenIQuantOffset,
			Double givenLumiMasking,
			Double givenTemporalCplxMasking,
			Double givenSpatialCplxMasking,
			Double givenPMasking,
			Double givenDarkMasking,
			AVRational givenSampleAspectRatio,
			FFmpegComparisonFunction givenMeCmp,
			FFmpegComparisonFunction givenMeSubCmp,
			FFmpegComparisonFunction givenMbCmp,
			FFmpegComparisonFunction givenIldctCmp,
			Integer givenDiaSize,
			Integer givenLastPredictorCount,
			FFmpegComparisonFunction givenMePreCmp,
			Integer givenPreDiaSize,
			Integer givenMeSubpelQuality,
			Integer givenMeRange,
			FFmpegMacroblockDecisionMode givenMbDecision,
			Integer givenIntraDcPrecision,
			Integer givenMbLMin,
			Integer givenMbLMax,
			Integer givenBidirRefine,
			Integer givenKeyintMin,
			Integer givenRefs,
			Integer givenMv0Threshold,
			Integer givenColorPrimaries,
			Integer givenColorTrc,
			Integer givenColorspace,
			Integer givenColorRange,
			Integer givenChromaSampleLocation,
			Integer givenSlices,
			Integer givenSampleRate,
			Integer givenChannels,
			FFmpegAudioSampleFormat givenSampleFmt,
			Integer givenCutoff,
			FFmpegChannelLayout givenChannelLayout,
			Integer givenAudioServiceType,
			Double givenQCompress,
			Double givenQBlur,
			Integer givenQMin,
			Integer givenQMax,
			Integer givenMaxQDiff,
			Integer givenRcBufferSize,
			// Integer givenRcOverrideCount,
			// Integer givenRcOverride,
			Integer givenRcMaxRate,
			Integer givenRcMinRate,
			Double givenRcMaxAvailableVbvUse,
			Double givenRcMinVbvOverflowUse,
			Integer givenRcInitialBufferOccupancy,
			Integer givenTrellis,
			FFmpegWorkAroundBug[] givenWorkaroundBugs,
			FFmpegStandardCompliance givenStrictStdCompliance,
			FFmpegDebugOption[] givenDebug,
			FFmpeg_DCT_Algorithm givenDctAlgo,
			FFmpeg_IDCT_Algorithm givenIdctAlgo,
			Integer givenBitsPerRawSample,
			Integer givenThreadCount,
			FFmpegThreadType givenThreadType,
			Integer givenThreadSafeCallbacks,
			Integer givenNsseWeight,
			FFmpegProfile givenProfile,
			FFmpegLevel givenLevel,
			FFmpegSubtitleTextFormat givenSubTextFormat,
			Integer givenNBSamples
			) {
		codecType= givenCodecType;
		codecId= givenCodecId;
		codecTag= givenCodecTag;
		bitRate= givenBitRate;
		// codecOptions= givenCodecOptions;
		bitRateTolerance= givenBitRateTolerance;
		globalQuality= givenGlobalQuality;
		compressionLevel= givenCompressionLevel;
		timeBase= givenTimeBase;
		sourceImageWidth= givenSourceImageWidth;
		sourceImageHeight= givenSourceImageHeight;
		destinationImageWidth= givenDestinationImageWidth;
		destinationImageHeight= givenDestinationImageHeight;
		gopSize= givenGopSize;
		pixFmt= givenPixFmt;
		maxBFrames= givenMaxBFrames;
		bQuantFactor= givenBQuantFactor;
		bQuantOffset= givenBQuantOffset;
		iQuantFactor= givenIQuantFactor;
		iQuantOffset= givenIQuantOffset;
		lumiMasking= givenLumiMasking;
		temporalCplxMasking= givenTemporalCplxMasking;
		spatialCplxMasking= givenSpatialCplxMasking;
		pMasking= givenPMasking;
		darkMasking= givenDarkMasking;
		sampleAspectRatio= givenSampleAspectRatio;
		meCmp= givenMeCmp;
		meSubCmp= givenMeSubCmp;
		mbCmp= givenMbCmp;
		ildctCmp= givenIldctCmp;
		diaSize= givenDiaSize;
		lastPredictorCount= givenLastPredictorCount;
		mePreCmp= givenMePreCmp;
		preDiaSize= givenPreDiaSize;
		meSubpelQuality= givenMeSubpelQuality;
		meRange= givenMeRange;
		mbDecision= givenMbDecision;
		intraDcPrecision= givenIntraDcPrecision;
		mbLMin= givenMbLMin;
		mbLMax= givenMbLMax;
		bidirRefine= givenBidirRefine;
		keyintMin= givenKeyintMin;
		refs= givenRefs;
		mv0Threshold= givenMv0Threshold;
		colorPrimaries= givenColorPrimaries;
		colorTrc= givenColorTrc;
		colorspace= givenColorspace;
		colorRange= givenColorRange;
		chromaSampleLocation= givenChromaSampleLocation;
		slices= givenSlices;
		sampleRate= givenSampleRate;
		channels= givenChannels;
		sampleFmt= givenSampleFmt;
		cutoff= givenCutoff;
		channelLayout= givenChannelLayout;
		audioServiceType= givenAudioServiceType;
		qCompress= givenQCompress;
		qBlur= givenQBlur;
		qMin= givenQMin;
		qMax= givenQMax;
		maxQDiff= givenMaxQDiff;
		rcBufferSize= givenRcBufferSize;
		// rcOverrideCount= givenRcOverrideCount;
		// rcOverride= givenRcOverride;
		rcMaxRate= givenRcMaxRate;
		rcMinRate= givenRcMinRate;
		rcMaxAvailableVbvUse= givenRcMaxAvailableVbvUse;
		rcMinVbvOverflowUse= givenRcMinVbvOverflowUse;
		rcInitialBufferOccupancy= givenRcInitialBufferOccupancy;
		trellis= givenTrellis;
		workaroundBugs= givenWorkaroundBugs;
		strictStdCompliance= givenStrictStdCompliance;
		debug= givenDebug;
		dctAlgo= givenDctAlgo;
		idctAlgo= givenIdctAlgo;
		bitsPerRawSample= givenBitsPerRawSample;
		threadCount= givenThreadCount;
		threadType= givenThreadType;
		threadSafeCallbacks= givenThreadSafeCallbacks;
		nsseWeight= givenNsseWeight;
		profile= givenProfile;
		level= givenLevel;
		subTextFormat= givenSubTextFormat;
		nbSamples= givenNBSamples;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public FFmpegMediaType getCodecType() {
		return codecType;
	}
	public Integer getCodecId() {
		return codecId;
	}
	public Integer getCodecTag() {
		return codecTag;
	}
	public Integer getBitRate() {
		return bitRate;
	}
	public Integer getBitRateTolerance() {
		return bitRateTolerance;
	}
	public Integer getGlobalQuality() {
		return globalQuality;
	}
	public Integer getCompressionLevel() {
		return compressionLevel;
	}
	public AVRational getTimeBase() {
		return timeBase;
	}
	public Integer getSourceImageWidth() {
		return sourceImageWidth;
	}
	public Integer getSourceImageHeight() {
		return sourceImageHeight;
	}
	public Integer getDestinationImageWidth() {
		return destinationImageWidth;
	}
	public Integer getDestinationImageHeight() {
		return destinationImageHeight;
	}
	public int getDestinationImageWidth(int defaultWidth) {
		if (destinationImageWidth != null) {
			return destinationImageWidth;
		} else {
			return defaultWidth;
		}
	}
	public int getDestinationImageHeight(int defaultHeight) {
		if (destinationImageHeight != null) {
			return destinationImageHeight;
		} else {
			return defaultHeight;
		}
	}
	public Integer getGopSize() {
		return gopSize;
	}
	public FFmpegPixelFormat getPixFmt() {
		return pixFmt;
	}
	public Integer getMaxBFrames() {
		return maxBFrames;
	}
	public Double getBQuantFactor() {
		return bQuantFactor;
	}
	public Double getBQuantOffset() {
		return bQuantOffset;
	}
	public Double getIQuantFactor() {
		return iQuantFactor;
	}
	public Double getIQuantOffset() {
		return iQuantOffset;
	}
	public Double getLumiMasking() {
		return lumiMasking;
	}
	public Double getTemporalCplxMasking() {
		return temporalCplxMasking;
	}
	public Double getSpatialCplxMasking() {
		return spatialCplxMasking;
	}
	public Double getPMasking() {
		return pMasking;
	}
	public Double getDarkMasking() {
		return darkMasking;
	}
	public AVRational getSampleAspectRatio() {
		return sampleAspectRatio;
	}
	public FFmpegComparisonFunction getMeCmp() {
		return meCmp;
	}
	public FFmpegComparisonFunction getMeSubCmp() {
		return meSubCmp;
	}
	public FFmpegComparisonFunction getMbCmp() {
		return mbCmp;
	}
	public FFmpegComparisonFunction getIldctCmp() {
		return ildctCmp;
	}
	public Integer getDiaSize() {
		return diaSize;
	}
	public Integer getLastPredictorCount() {
		return lastPredictorCount;
	}
	public FFmpegComparisonFunction getMePreCmp() {
		return mePreCmp;
	}
	public Integer getPreDiaSize() {
		return preDiaSize;
	}
	public Integer getMeSubpelQuality() {
		return meSubpelQuality;
	}
	public Integer hetMeRange() {
		return meRange;
	}
	public FFmpegMacroblockDecisionMode getMbDecision() {
		return mbDecision;
	}
	public Integer getIntraDcPrecision() {
		return intraDcPrecision;
	}
	public Integer getMbLMin() {
		return mbLMin;
	}
	public Integer getMbLMax() {
		return mbLMax;
	}
	public Integer getBidirRefine() {
		return bidirRefine;
	}
	public Integer getKeyintMin() {
		return keyintMin;
	}
	public Integer getRefs() {
		return refs;
	}
	public Integer getMv0Threshold() {
		return mv0Threshold;
	}
	public Integer getColorPrimaries() {
		return colorPrimaries;
	}
	public Integer getColorTrc() {
		return colorTrc;
	}
	public Integer getColorspace() {
		return colorspace;
	}
	public Integer getColorRange() {
		return colorRange;
	}
	public Integer getChromaSampleLocation() {
		return chromaSampleLocation;
	}
	public Integer getSlices() {
		return slices;
	}
	public Integer getSampleRate() {
		return sampleRate;
	}
	public Integer getChannels() {
		return channels;
	}
	public FFmpegAudioSampleFormat getSampleFmt() {
		return sampleFmt;
	}
	public Integer getCutoff() {
		return cutoff;
	}
	public FFmpegChannelLayout getChannelLayout() {
		return channelLayout;
	}
	public Integer getAudioServiceType() {
		return audioServiceType;
	}
	public Double getQCompress() {
		return qCompress;
	}
	public Double getQBlur() {
		return qBlur;
	}
	public Integer getQMin() {
		return qMin;
	}
	public Integer getQMax() {
		return qMax;
	}
	public Integer getMaxQDiff() {
		return maxQDiff;
	}
	public Integer getRcBufferSize() {
		return rcBufferSize;
	}
	public Integer getRcMaxRate() {
		return rcMaxRate;
	}
	public Integer getRcMinRate() {
		return rcMinRate;
	}
	public Double getRcMaxAvailableVbvUse() {
		return rcMaxAvailableVbvUse;
	}
	public Double getRcMinVbvOverflowUse() {
		return rcMinVbvOverflowUse;
	}
	public Integer getRcInitialBufferOccupancy() {
		return rcInitialBufferOccupancy;
	}
	public Integer getTrellis() {
		return trellis;
	}
	public FFmpegWorkAroundBug[] getWorkaroundBugs() {
		return workaroundBugs;
	}
	public FFmpegStandardCompliance getStrictStdCompliance() {
		return strictStdCompliance;
	}
	public FFmpegDebugOption[] getDebug() {
		return debug;
	}
	public FFmpeg_DCT_Algorithm getDctAlgo() {
		return dctAlgo;
	}
	public FFmpeg_IDCT_Algorithm getIdctAlgo() {
		return idctAlgo;
	}
	public Integer getBitsPerRawSample() {
		return bitsPerRawSample;
	}
	public Integer getThreadCount() {
		return threadCount;
	}
	public FFmpegThreadType getThreadType() {
		return threadType;
	}
	public Integer getThreadSafeCallbacks() {
		return threadSafeCallbacks;
	}
	public Integer getNsseWeight() {
		return nsseWeight;
	}
	public FFmpegProfile getProfile() {
		return profile;
	}
	public FFmpegLevel getLevel() {
		return level;
	}
	public FFmpegSubtitleTextFormat getSubTextFormat() {
		return subTextFormat;
	}
	public Integer getNBSamples() {
		return nbSamples;
	}
	//
	public boolean isVideoStream() {
		if (codecType.isNamed()) {
			FFmpegMediaTypeName name= codecType.getName();
			if (name==FFmpegMediaTypeName.AVMEDIA_TYPE_VIDEO) {
				return true;
			} else {
				return false;
			}
		} else {
			int value= codecType.getValue();
			if (value==AVMEDIA_TYPE_VIDEO) {
				return true;
			} else {
				return false;
			}
		}
	}
	//
	public boolean isAudioStream() {
		if (codecType.isNamed()) {
			FFmpegMediaTypeName name= codecType.getName();
			if (name==FFmpegMediaTypeName.AVMEDIA_TYPE_AUDIO) {
				return true;
			} else {
				return false;
			}
		} else {
			int value= codecType.getValue();
			if (value==AVMEDIA_TYPE_AUDIO) {
				return true;
			} else {
				return false;
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setReadingAttributes(AVCodecContext c, AVCodec codec, FFmpegOutputStreamState outputStreamState) {
		setUniversalAttributes(c,codec,outputStreamState);
		Integer width= sourceImageWidth;
		Integer height= sourceImageHeight;
		if (width != null) {
			c.width(width);
		};
		if (height != null) {
			c.height(height);
		}
	}
	public void setRecordingAttributes(AVCodecContext c, AVCodec codec, FFmpegOutputStreamState outputStreamState) {
		setUniversalAttributes(c,codec,outputStreamState);
		Integer width= destinationImageWidth;
		Integer height= destinationImageHeight;
		if (width != null) {
			c.width(width);
		};
		if (height != null) {
			c.height(height);
		}
	}
	//
	protected void setUniversalAttributes(AVCodecContext c, AVCodec codec, FFmpegOutputStreamState outputStreamState) {
		if (outputStreamState != null) {
			outputStreamState.sourceImageWidth= sourceImageWidth;
			outputStreamState.sourceImageHeight= sourceImageHeight;
		};
		// codecType;
		if (codecId != null) {
			c.codec_id(codecId);
		};
		if (codecTag != null) {
			c.codec_tag(codecTag);
		};
		if (bitRate != null) {
			c.bit_rate(bitRate);
		};
		if (bitRateTolerance != null) {
			c.bit_rate_tolerance(bitRateTolerance);
		};
		if (globalQuality != null) {
			c.global_quality(globalQuality);
		};
		if (compressionLevel != null) {
			c.compression_level(compressionLevel);
		};
		if (timeBase != null) {
			if (outputStreamState != null) {
				outputStreamState.st.time_base(timeBase);
			};
			c.time_base(timeBase);
		};
		// if (width != null) {
		//	c.width(width);
		// };
		// if (height != null) {
		//	c.height(height);
		// };
		if (gopSize != null) {
			c.gop_size(gopSize);
		};
		if (pixFmt != null) {
			int userDefinedPixelFormat= pixFmt.toInteger();
			c.pix_fmt(userDefinedPixelFormat);
			// pix_fmts:
			// Array of supported pixel formats, or NULL
			// if unknown, array is terminated by -1.
			IntPointer supportedFormats= codec.pix_fmts();
			if (supportedFormats != null) {
				int supportedFormat= supportedFormats.get(0);
				if (supportedFormat >= 0) {
					c.pix_fmt(supportedFormat);
					int position= 0;
					while (true) {
						supportedFormat= supportedFormats.get(position);
						if (supportedFormat < 0) {
							break;
						} else if (supportedFormat==userDefinedPixelFormat) {
							c.pix_fmt(userDefinedPixelFormat);
							break;
						} else {
							position++;
						}
					}
				}
			}
		} else {
			// pix_fmts:
			// Array of supported pixel formats, or NULL
			// if unknown, array is terminated by -1.
			IntPointer supportedFormats= codec.pix_fmts();
			if (supportedFormats != null) {
				int supportedFormat= supportedFormats.get(0);
				if (supportedFormat >= 0) {
					c.pix_fmt(supportedFormat);
				}
			}
		};
		if (maxBFrames != null) {
			c.max_b_frames(maxBFrames);
		};
		if (bQuantFactor != null) {
			c.b_quant_factor(bQuantFactor.floatValue());
		};
		if (bQuantOffset != null) {
			c.b_quant_offset(bQuantOffset.floatValue());
		};
		if (iQuantFactor != null) {
			c.i_quant_factor(iQuantFactor.floatValue());
		};
		if (iQuantOffset != null) {
			c.i_quant_offset(iQuantOffset.floatValue());
		};
		if (lumiMasking != null) {
			c.lumi_masking(lumiMasking.floatValue());
		};
		if (temporalCplxMasking != null) {
			c.temporal_cplx_masking(temporalCplxMasking.floatValue());
		};
		if (spatialCplxMasking != null) {
			c.spatial_cplx_masking(spatialCplxMasking.floatValue());
		};
		if (pMasking != null) {
			c.p_masking(pMasking.floatValue());
		};
		if (darkMasking != null) {
			c.dark_masking(darkMasking.floatValue());
		};
		if (sampleAspectRatio != null) {
			c.sample_aspect_ratio(sampleAspectRatio);
		};
		if (meCmp != null) {
			c.me_cmp(meCmp.toInteger());
		};
		if (meSubCmp != null) {
			c.me_sub_cmp(meSubCmp.toInteger());
		};
		if (mbCmp != null) {
			c.mb_cmp(mbCmp.toInteger());
		};
		if (ildctCmp != null) {
			c.ildct_cmp(ildctCmp.toInteger());
		};
		if (diaSize != null) {
			c.dia_size(diaSize);
		};
		if (lastPredictorCount != null) {
			c.last_predictor_count(lastPredictorCount);
		};
		if (mePreCmp != null) {
			c.me_pre_cmp(mePreCmp.toInteger());
		};
		if (preDiaSize != null) {
			c.pre_dia_size(preDiaSize);
		};
		if (meSubpelQuality != null) {
			c.me_subpel_quality(meSubpelQuality);
		};
		if (meRange != null) {
			c.me_range(meRange);
		};
		if (mbDecision != null) {
			c.mb_decision(mbDecision.toInteger());
		};
		if (intraDcPrecision != null) {
			c.intra_dc_precision(intraDcPrecision);
		};
		if (mbLMin != null) {
			c.mb_lmin(mbLMin);
		};
		if (mbLMax != null) {
			c.mb_lmax(mbLMax);
		};
		if (bidirRefine != null) {
			c.bidir_refine(bidirRefine);
		};
		if (keyintMin != null) {
			c.keyint_min(keyintMin);
		};
		if (refs != null) {
			c.refs(refs);
		};
		if (mv0Threshold != null) {
			c.mv0_threshold(mv0Threshold);
		};
		if (colorPrimaries != null) {
			c.color_primaries(colorPrimaries);
		};
		if (colorTrc != null) {
			c.color_trc(colorTrc);
		};
		if (colorspace != null) {
			c.colorspace(colorspace);
		};
		if (colorRange != null) {
			c.color_range(colorRange);
		};
		if (chromaSampleLocation != null) {
			c.chroma_sample_location(chromaSampleLocation);
		};
		if (slices != null) {
			c.slices(slices);
		};
		if (sampleRate != null) {
			c.sample_rate(sampleRate);
			// supported_samplerates:
			// Array of supported audio samplerates, or
			// NULL if unknown, array is terminated by 0.
			IntPointer supportedSamplerates= codec.supported_samplerates();
			if (supportedSamplerates != null) {
				int supportedSamplerate= supportedSamplerates.get(0);
				if (supportedSamplerate != 0) {
					c.sample_rate(supportedSamplerate);
					int position= 0;
					while (true) {
						supportedSamplerate= supportedSamplerates.get(position);
						if (supportedSamplerate==0) {
							break;
						} else if (supportedSamplerate==sampleRate) {
							c.sample_rate(sampleRate);
							break;
						};
						position++;
					}
				}
			}
		} else {
			// supported_samplerates:
			// Array of supported audio samplerates, or
			// NULL if unknown, array is terminated by 0.
			IntPointer supportedSamplerates= codec.supported_samplerates();
			if (supportedSamplerates != null) {
				int supportedSamplerate= supportedSamplerates.get(0);
				if (supportedSamplerate != 0) {
					c.sample_rate(supportedSamplerate);
				}
			}
		};
		if (channels != null) {
			c.channels(channels);
		};
		if (sampleFmt != null) {
			int userDefinedSampleFormat= sampleFmt.toInteger();
			c.sample_fmt(userDefinedSampleFormat);
			// sample_fmts:
			// Array of supported sample formats, or
			// NULL if unknown, array is terminated by -1.
			IntPointer supportedFormats= codec.sample_fmts();
			if (supportedFormats != null) {
				int supportedFormat= supportedFormats.get(0);
				if (supportedFormat >= 0) {
					c.sample_fmt(supportedFormat);
					int position= 0;
					while (true) {
						supportedFormat= supportedFormats.get(position);
						if (supportedFormat < 0) {
							break;
						} else if (supportedFormat==userDefinedSampleFormat) {
							c.sample_fmt(userDefinedSampleFormat);
							break;
						} else {
							position++;
						}
					}
				}
			}
		} else {
			// sample_fmts:
			// Array of supported sample formats, or
			// NULL if unknown, array is terminated by -1.
			IntPointer supportedFormats= codec.sample_fmts();
			if (supportedFormats != null) {
				int supportedFormat= supportedFormats.get(0);
				if (supportedFormat >= 0) {
					c.sample_fmt(supportedFormat);
				}
			}
		};
		if (cutoff != null) {
			c.cutoff(cutoff);
		};
		if (channelLayout != null) {
			long userDefinedChannelLayout= channelLayout.toLong();
			// channel_layout:
			// Audio channel layout.
			// - encoding: set by user.
			// - decoding: set by user, may be overwritten
			// by libavcodec.
			//
			// av_get_channel_layout_nb_channels:
			// Return the number of channels in the
			// channel layout.
			//
			// channels:
			// number of audio channels
			c.channel_layout(userDefinedChannelLayout);
			c.channels(av_get_channel_layout_nb_channels(c.channel_layout()));
			// channel_layouts:
			// Array of support channel layouts, or NULL
			// if unknown. Array is terminated by 0.
			LongPointer channelLayouts= codec.channel_layouts();
			if (channelLayouts != null) {
				long channelLayout= channelLayouts.get(0);
				if (channelLayout != 0) {
					c.channel_layout(channelLayout);
					int position= 0;
					while (true) {
						channelLayout= channelLayouts.get(position);
						if (channelLayout==0) {
							break;
						} else if (channelLayout==userDefinedChannelLayout) {
							c.channel_layout(userDefinedChannelLayout);
							break;
						};
						position++;
					};
					c.channels(av_get_channel_layout_nb_channels(c.channel_layout()));
				}
			}
		} else {
			// channel_layouts:
			// Array of support channel layouts, or NULL
			// if unknown. Array is terminated by 0.
			LongPointer channelLayouts= codec.channel_layouts();
			if (channelLayouts != null) {
				long channelLayout= channelLayouts.get(0);
				if (channelLayout != 0) {
					c.channel_layout(channelLayout);
					c.channels(av_get_channel_layout_nb_channels(c.channel_layout()));
				}
			}
		};
		if (audioServiceType != null) {
			c.audio_service_type(audioServiceType);
		};
		if (qCompress != null) {
			c.qcompress(qCompress.floatValue());
		};
		if (qBlur != null) {
			c.qblur(qBlur.floatValue());
		};
		if (qMin != null) {
			c.qmin(qMin);
		};
		if (qMax != null) {
			c.qmax(qMax);
		};
		if (maxQDiff != null) {
			c.max_qdiff(maxQDiff);
		};
		if (rcBufferSize != null) {
			c.rc_buffer_size(rcBufferSize);
		};
		if (rcMaxRate != null) {
			c.rc_max_rate(rcMaxRate);
		};
		if (rcMinRate != null) {
			c.rc_min_rate(rcMinRate);
		};
		if (rcMaxAvailableVbvUse != null) {
			c.rc_max_available_vbv_use(rcMaxAvailableVbvUse.floatValue());
		};
		if (rcMinVbvOverflowUse != null) {
			c.rc_min_vbv_overflow_use(rcMinVbvOverflowUse.floatValue());
		};
		if (rcInitialBufferOccupancy != null) {
			c.rc_initial_buffer_occupancy(rcInitialBufferOccupancy);
		};
		if (trellis != null) {
			c.trellis(trellis);
		};
		if (workaroundBugs != null) {
			c.workaround_bugs(FFmpegTools.convertFFmpegWorkAroundBugsToInteger(workaroundBugs));
		};
		if (strictStdCompliance != null) {
			c.strict_std_compliance(strictStdCompliance.toInteger());
		};
		if (debug != null) {
			c.debug(FFmpegTools.convertFFmpegDebugOptionsToInteger(debug));
		};
		if (dctAlgo != null) {
			c.dct_algo(dctAlgo.toInteger());
		};
		if (idctAlgo != null) {
			c.idct_algo(idctAlgo.toInteger());
		};
		if (bitsPerRawSample != null) {
			c.bits_per_raw_sample(bitsPerRawSample);
		};
		if (threadCount != null) {
			c.thread_count(threadCount);
		};
		if (threadType != null) {
			c.thread_type(threadType.toInteger());
		};
		if (threadSafeCallbacks != null) {
			c.thread_safe_callbacks(threadSafeCallbacks);
		};
		if (nsseWeight != null) {
			c.nsse_weight(nsseWeight);
		};
		if (profile != null) {
			c.profile(profile.toInteger());
		};
		if (level != null) {
			c.level(level.toInteger());
		};
		if (subTextFormat != null) {
			c.sub_text_format(subTextFormat.toInteger());
		};
		if (outputStreamState != null) {
			outputStreamState.nbSamples= nbSamples;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static FFmpegStreamDefinition[] argumentToStreamDefinitions(Term value, ChoisePoint iX) {
		if (value != null) {
			value= value.dereferenceValue(iX);
			if (value.thisIsEmptyList()) {
				return new FFmpegStreamDefinition[0];
			} else {
				try {
					value.getNextListHead(iX);
					return listToStreamDefinitions(value,iX);
				} catch (EndOfList e) {
					return new FFmpegStreamDefinition[0];
				} catch (TermIsNotAList e) {
					FFmpegStreamDefinition stream= argumentToStreamDefinition(value,iX);
					return new FFmpegStreamDefinition[]{stream};
				}
			}
		} else {
			return defaultStreamDefinitionList;
		}
	}
	//
	public static FFmpegStreamDefinition[] listToStreamDefinitions(Term value, ChoisePoint iX) {
		value= value.dereferenceValue(iX);
		ArrayList<FFmpegStreamDefinition> streamArray= new ArrayList<>();
		Term nextHead= null;
		Term currentTail= value;
		try {
			while (true) {
				nextHead= currentTail.getNextListHead(iX);
				FFmpegStreamDefinition stream= argumentToStreamDefinition(nextHead,iX);
				streamArray.add(stream);
				currentTail= currentTail.getNextListTail(iX);
			}
		} catch (EndOfList e1) {
		} catch (TermIsNotAList e2) {
			// throw new WrongArgumentIsNotFFmpegStream(value);
			FFmpegStreamDefinition stream= argumentToStreamDefinition(currentTail,iX);
			streamArray.add(stream);
		};
		return streamArray.toArray(new FFmpegStreamDefinition[0]);
	}
	//
	public static FFmpegStreamDefinition argumentToStreamDefinition(Term value, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
		Term setEnd= value.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			FFmpegMediaType givenCodecType= null;
			Integer givenCodecId= null;
			Integer givenCodecTag= null;
			Integer givenBitRate= null;
			// FFmpegCodecOption[] givenCodecOptions= null;
			Integer givenBitRateTolerance= null;
			Integer givenGlobalQuality= null;
			Integer givenCompressionLevel= null;
			AVRational givenTimeBase= null;
			Integer givenSourceImageWidth= null;
			Integer givenSourceImageHeight= null;
			Integer givenDestinationImageWidth= null;
			Integer givenDestinationImageHeight= null;
			Integer givenGopSize= null;
			FFmpegPixelFormat givenPixFmt= null;
			Integer givenMaxBFrames= null;
			Double givenBQuantFactor= null;
			Double givenBQuantOffset= null;
			Double givenIQuantFactor= null;
			Double givenIQuantOffset= null;
			Double givenLumiMasking= null;
			Double givenTemporalCplxMasking= null;
			Double givenSpatialCplxMasking= null;
			Double givenPMasking= null;
			Double givenDarkMasking= null;
			AVRational givenSampleAspectRatio= null;
			FFmpegComparisonFunction givenMeCmp= null;
			FFmpegComparisonFunction givenMeSubCmp= null;
			FFmpegComparisonFunction givenMbCmp= null;
			FFmpegComparisonFunction givenIldctCmp= null;
			Integer givenDiaSize= null;
			Integer givenLastPredictorCount= null;
			FFmpegComparisonFunction givenMePreCmp= null;
			Integer givenPreDiaSize= null;
			Integer givenMeSubpelQuality= null;
			Integer givenMeRange= null;
			FFmpegMacroblockDecisionMode givenMbDecision= null;
			Integer givenIntraDcPrecision= null;
			Integer givenMbLMin= null;
			Integer givenMbLMax= null;
			Integer givenBidirRefine= null;
			Integer givenKeyintMin= null;
			Integer givenRefs= null;
			Integer givenMv0Threshold= null;
			Integer givenColorPrimaries= null;
			Integer givenColorTrc= null;
			Integer givenColorspace= null;
			Integer givenColorRange= null;
			Integer givenChromaSampleLocation= null;
			Integer givenSlices= null;
			Integer givenSampleRate= null;
			Integer givenChannels= null;
			FFmpegAudioSampleFormat givenSampleFmt= null;
			Integer givenCutoff= null;
			FFmpegChannelLayout givenChannelLayout= null;
			Integer givenAudioServiceType= null;
			Double givenQCompress= null;
			Double givenQBlur= null;
			Integer givenQMin= null;
			Integer givenQMax= null;
			Integer givenMaxQDiff= null;
			Integer givenRcBufferSize= null;
			// Integer givenRcOverrideCount= null;
			// Integer givenRcOverride= null;
			Integer givenRcMaxRate= null;
			Integer givenRcMinRate= null;
			Double givenRcMaxAvailableVbvUse= null;
			Double givenRcMinVbvOverflowUse= null;
			Integer givenRcInitialBufferOccupancy= null;
			Integer givenTrellis= null;
			FFmpegWorkAroundBug[] givenWorkaroundBugs= null;
			FFmpegStandardCompliance givenStrictStdCompliance= null;
			FFmpegDebugOption[] givenDebug= null;
			FFmpeg_DCT_Algorithm givenDctAlgo= null;
			FFmpeg_IDCT_Algorithm givenIdctAlgo= null;
			Integer givenBitsPerRawSample= null;
			Integer givenThreadCount= null;
			FFmpegThreadType givenThreadType= null;
			Integer givenThreadSafeCallbacks= null;
			Integer givenNsseWeight= null;
			FFmpegProfile givenProfile= null;
			FFmpegLevel givenLevel= null;
			FFmpegSubtitleTextFormat givenSubTextFormat= null;
			Integer givenNBSamples= null;
			Set<Long> nameList= setPositiveMap.keySet();
			Iterator<Long> iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_codec_type) {
					givenCodecType= FFmpegMediaType.argumentToFFmpegMediaType(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_codec_id) {
					givenCodecId= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_codec_tag) {
					givenCodecTag= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_bit_rate) {
					givenBitRate= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				// } else if (pairName==SymbolCodes.symbolCode_E_codec_options) {
				//	givenCodecOptions= FFmpegCodecOption.argumentToCodecOptions(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_bit_rate_tolerance) {
					givenBitRateTolerance= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_global_quality) {
					givenGlobalQuality= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_compression_level) {
					givenCompressionLevel= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_time_base) {
					givenTimeBase= FFmpegTools.argumentToAVRational(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_source_image_width) {
					givenSourceImageWidth= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_source_image_height) {
					givenSourceImageHeight= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_destination_image_width) {
					givenDestinationImageWidth= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_destination_image_height) {
					givenDestinationImageHeight= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_gop_size) {
					givenGopSize= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_pix_fmt) {
					givenPixFmt= FFmpegPixelFormat.argumentToFFmpegPixelFormat(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_max_b_frames) {
					givenMaxBFrames= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_b_quant_factor) {
					givenBQuantFactor= GeneralConverters.argumentToReal(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_b_quant_offset) {
					givenBQuantOffset= GeneralConverters.argumentToReal(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_i_quant_factor) {
					givenIQuantFactor= GeneralConverters.argumentToReal(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_i_quant_offset) {
					givenIQuantOffset= GeneralConverters.argumentToReal(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_lumi_masking) {
					givenLumiMasking= GeneralConverters.argumentToReal(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_temporal_cplx_masking) {
					givenTemporalCplxMasking= GeneralConverters.argumentToReal(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_spatial_cplx_masking) {
					givenSpatialCplxMasking= GeneralConverters.argumentToReal(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_p_masking) {
					givenPMasking= GeneralConverters.argumentToReal(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_dark_masking) {
					givenDarkMasking= GeneralConverters.argumentToReal(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_sample_aspect_ratio) {
					givenSampleAspectRatio= FFmpegTools.argumentToAVRational(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_me_cmp) {
					givenMeCmp= FFmpegComparisonFunction.argumentToFFmpegComparisonFunction(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_me_sub_cmp) {
					givenMeSubCmp= FFmpegComparisonFunction.argumentToFFmpegComparisonFunction(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_mb_cmp) {
					givenMbCmp= FFmpegComparisonFunction.argumentToFFmpegComparisonFunction(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_ildct_cmp) {
					givenIldctCmp= FFmpegComparisonFunction.argumentToFFmpegComparisonFunction(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_dia_size) {
					givenDiaSize= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_last_predictor_count) {
					givenLastPredictorCount= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_me_pre_cmp) {
					givenMePreCmp= FFmpegComparisonFunction.argumentToFFmpegComparisonFunction(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_pre_dia_size) {
					givenPreDiaSize= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_me_subpel_quality) {
					givenMeSubpelQuality= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_me_range) {
					givenMeRange= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_mb_decision) {
					givenMbDecision= FFmpegMacroblockDecisionMode.argumentToFFmpegMacroblockDecisionMode(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_intra_dc_precision) {
					givenIntraDcPrecision= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_mb_lmin) {
					givenMbLMin= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_mb_lmax) {
					givenMbLMax= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_bidir_refine) {
					givenBidirRefine= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_keyint_min) {
					givenKeyintMin= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_refs) {
					givenRefs= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_mv0_threshold) {
					givenMv0Threshold= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_color_primaries) {
					givenColorPrimaries= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_color_trc) {
					givenColorTrc= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_colorspace) {
					givenColorspace= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_color_range) {
					givenColorRange= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_chroma_sample_location) {
					givenChromaSampleLocation= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_slices) {
					givenSlices= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_sample_rate) {
					givenSampleRate= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_channels) {
					givenChannels= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_sample_fmt) {
					givenSampleFmt= FFmpegAudioSampleFormat.argumentToFFmpegAudioSampleFormat(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_cutoff) {
					givenCutoff= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_channel_layout) {
					givenChannelLayout= FFmpegChannelLayout.argumentToFFmpegChannelLayout(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_audio_service_type) {
					givenAudioServiceType= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_qcompress) {
					givenQCompress= GeneralConverters.argumentToReal(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_qblur) {
					givenQBlur= GeneralConverters.argumentToReal(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_qmin) {
					givenQMin= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_qmax) {
					givenQMax= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_max_qdiff) {
					givenMaxQDiff= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_rc_buffer_size) {
					givenRcBufferSize= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				// } else if (pairName==SymbolCodes.symbolCode_E_rc_override_count) {
				//	givenRcOverrideCount= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				// } else if (pairName==SymbolCodes.symbolCode_E_rc_override) {
				//	givenRcOverride= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_rc_max_rate) {
					givenRcMaxRate= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_rc_min_rate) {
					givenRcMinRate= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_rc_max_available_vbv_use) {
					givenRcMaxAvailableVbvUse= GeneralConverters.argumentToReal(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_rc_min_vbv_overflow_use) {
					givenRcMinVbvOverflowUse= GeneralConverters.argumentToReal(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_rc_initial_buffer_occupancy) {
					givenRcInitialBufferOccupancy= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_trellis) {
					givenTrellis= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_workaround_bugs) {
					givenWorkaroundBugs= FFmpegTools.argumentToFFmpegWorkAroundBugs(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_strict_std_compliance) {
					givenStrictStdCompliance= FFmpegStandardCompliance.argumentToFFmpegStandardCompliance(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_debug) {
					givenDebug= FFmpegTools.argumentToFFmpegDebugOptions(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_dct_algo) {
					givenDctAlgo= FFmpeg_DCT_Algorithm.argumentToFFmpeg_DCT_Algorithm(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_idct_algo) {
					givenIdctAlgo= FFmpeg_IDCT_Algorithm.argumentToFFmpeg_IDCT_Algorithm(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_bits_per_raw_sample) {
					givenBitsPerRawSample= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_thread_count) {
					givenThreadCount= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_thread_type) {
					givenThreadType= FFmpegThreadType.argumentToFFmpegThreadType(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_thread_safe_callbacks) {
					givenThreadSafeCallbacks= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_nsse_weight) {
					givenNsseWeight= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_profile) {
					givenProfile= FFmpegProfile.argumentToFFmpegProfile(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_level) {
					givenLevel= FFmpegLevel.argumentToFFmpegLevel(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_sub_text_format) {
					givenSubTextFormat= FFmpegSubtitleTextFormat.argumentToFFmpegSubtitleTextFormat(pairValue,iX);
				} else if (pairName==SymbolCodes.symbolCode_E_nb_samples) {
					givenNBSamples= GeneralConverters.argumentToSmallInteger(pairValue,iX);
				} else {
					throw new WrongArgumentIsNotFFmpegStreamDefinitionAttribute(key);
				}
			};
			if (givenCodecType==null) {
				throw new CodecTypeIsNotDefined(value);
			};
			return new FFmpegStreamDefinition(
				givenCodecType,
				givenCodecId,
				givenCodecTag,
				givenBitRate,
				// givenCodecOptions,
				givenBitRateTolerance,
				givenGlobalQuality,
				givenCompressionLevel,
				givenTimeBase,
				givenSourceImageWidth,
				givenSourceImageHeight,
				givenDestinationImageWidth,
				givenDestinationImageHeight,
				givenGopSize,
				givenPixFmt,
				givenMaxBFrames,
				givenBQuantFactor,
				givenBQuantOffset,
				givenIQuantFactor,
				givenIQuantOffset,
				givenLumiMasking,
				givenTemporalCplxMasking,
				givenSpatialCplxMasking,
				givenPMasking,
				givenDarkMasking,
				givenSampleAspectRatio,
				givenMeCmp,
				givenMeSubCmp,
				givenMbCmp,
				givenIldctCmp,
				givenDiaSize,
				givenLastPredictorCount,
				givenMePreCmp,
				givenPreDiaSize,
				givenMeSubpelQuality,
				givenMeRange,
				givenMbDecision,
				givenIntraDcPrecision,
				givenMbLMin,
				givenMbLMax,
				givenBidirRefine,
				givenKeyintMin,
				givenRefs,
				givenMv0Threshold,
				givenColorPrimaries,
				givenColorTrc,
				givenColorspace,
				givenColorRange,
				givenChromaSampleLocation,
				givenSlices,
				givenSampleRate,
				givenChannels,
				givenSampleFmt,
				givenCutoff,
				givenChannelLayout,
				givenAudioServiceType,
				givenQCompress,
				givenQBlur,
				givenQMin,
				givenQMax,
				givenMaxQDiff,
				givenRcBufferSize,
				// givenRcOverrideCount,
				// givenRcOverride,
				givenRcMaxRate,
				givenRcMinRate,
				givenRcMaxAvailableVbvUse,
				givenRcMinVbvOverflowUse,
				givenRcInitialBufferOccupancy,
				givenTrellis,
				givenWorkaroundBugs,
				givenStrictStdCompliance,
				givenDebug,
				givenDctAlgo,
				givenIdctAlgo,
				givenBitsPerRawSample,
				givenThreadCount,
				givenThreadType,
				givenThreadSafeCallbacks,
				givenNsseWeight,
				givenProfile,
				givenLevel,
				givenSubTextFormat,
				givenNBSamples);
		} else {
			throw new WrongArgumentIsNotEndedSetOfAttributes(setEnd);
		}
	}
}
