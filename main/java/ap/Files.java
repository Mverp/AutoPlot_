package ap;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

public class Files {
	public static final String TAB = "\t";
	
	public static final String DYE_ONE = "DYE 1";
	public static final String DYE_TWO = "DYE 2";
	public static final String DYE_THREE = "DYE 3";
	
	public static final String RESULTS_PREFIX = "Results";
	public static final String REGEX_DYE_ONE_XLS = ".*1.xls";
	public static final String REGEX_DYE_TWO_XLS = ".*2.xls";
	public static final String REGEX_DYE_THREE_XLS = ".*3.xls";
	public static final String GENERIC_FILENAME_PART_CONVOLVED = "_convolved_d";
	public static final String GENERIC_FILENAME_PART_CONTOUR = " -- Contours";
	
	public static final String EXT_CSV = ".csv";
	public static final String EXT_TIF = ".tif";
	public static final String EXT_XLS = ".xls";
	
	public static final String RESULTS_FILENAME_DYE_THREE = RESULTS_PREFIX + " " + DYE_THREE + EXT_XLS;
	
	private static final String GENERIC_FILENAME_PART_PLOT = " -- Plot";
	// TODO fix the unnecessary spaces in the macro
	private static final String GENERIC_FILENAME_PART_DYE_THREE = " -- Background  and Flatfield  Corrected DYE 3";
	
	public static String getImageFilePathDyeThree(File wellDirectory) {
		return wellDirectory.getPath() + File.separator + wellDirectory.getName() + GENERIC_FILENAME_PART_DYE_THREE + EXT_TIF;
	}
	
	public static String getImageFilePathDyeThree(String plateDirectory, String wellName) {
		return plateDirectory + wellName + File.separator + wellName + GENERIC_FILENAME_PART_DYE_THREE + EXT_TIF;
	}
	
	public static String getImageFilePathPlot(String plateDirectory, String wellName) {
		String path = plateDirectory + wellName + File.separator + wellName + GENERIC_FILENAME_PART_PLOT + EXT_TIF;
		int supplement = 0;
		while ((new File(path)).exists()) {
			supplement++;
			path = plateDirectory + wellName + File.separator + wellName + GENERIC_FILENAME_PART_PLOT + "_" + supplement + EXT_TIF;
		}
		return path;
	}
	
	public static String getImageFilePathConvolveX(String plateDirectory, String wellName) {
		return getImageFilePathConvolve(plateDirectory, wellName, "x");
	}
	
	public static String getImageFilePathConvolveY(String plateDirectory, String wellName) {
		return getImageFilePathConvolve(plateDirectory, wellName, "y");
	}
	
	private static String getImageFilePathConvolve(String plateDirectory, String wellName, String direction) {
		return plateDirectory + wellName + File.separator + wellName + GENERIC_FILENAME_PART_CONVOLVED + direction + EXT_TIF;
	}
	
	public static FilenameFilter getFileNameFilterXls() {
		return new FilenameFilter() { 
			public boolean accept(File dir, String name) { 
				return name.endsWith(EXT_XLS);
			}
		};
	}

	public static FilenameFilter getFileNameFilterTif() {
		return new FilenameFilter() { 
			public boolean accept(File dir, String name) { 
				return name.endsWith(EXT_TIF);
			}
		};
	}
	
	public static FilenameFilter getFileNameFilterImageDyeThree() {
		return new FilenameFilter() { 
			public boolean accept(File dir, String name) { 
				return name.endsWith(DYE_THREE + EXT_TIF); 
			}
		};
	}
	
	public static FilenameFilter getFileNameFilterDataDyeThree() {
		return new FilenameFilter() { 
			public boolean accept(File dir, String name) { 
				return name.endsWith(DYE_THREE + EXT_XLS); 
			}
		};
	}
	
	public static FileFilter getDirectoryFilter() {
		return new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		};
	}
}
