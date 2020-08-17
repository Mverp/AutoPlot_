package ap.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import ap.Files;

public class DataFile {
	public static final String HEADER_LABEL_AREA = "Area";
	public static final String HEADER_LABEL_X = "X";
	public static final String HEADER_LABEL_Y = "y";
	public static final String HEADER_LABEL_INT_DEN = "IntDen";
	public static final String HEADER_LABEL_RAW_INT_DEN = "RawIntDen";
	public static final String HEADER_LABEL_TRELLIS = "Trellis";
	
	private String plate_path = "";
	private DataWell well_data;
	
	public DataFile(DataWell dataWell, String plateMainDirectory) {
		this.plate_path = plateMainDirectory;
		this.well_data = dataWell;
	}
	
	public void create() {
		if (this.well_data != null) {
			String fileData = getWellDataAsString();
			write(fileData);
		}
	}
	
	private void write(String string) {
		try {
			String path = getDataFilePath();
			FileWriter fileWriter = new FileWriter(path);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(string);
			bufferedWriter.close();
		} catch (Exception e) {
			// Handle exception
		}
	}
	
	private String getDataFilePath() {
		return this.plate_path + this.well_data.getName() + File.separator + Files.RESULTS_FILENAME_DYE_THREE;
	}
	
	private String getWellDataAsString() {
		String newLine = String.format("%n");
		String fileData = Files.TAB + HEADER_LABEL_AREA + Files.TAB + 
			HEADER_LABEL_X + Files.TAB + 
			HEADER_LABEL_Y + Files.TAB + 
			HEADER_LABEL_INT_DEN + Files.TAB + 
			HEADER_LABEL_RAW_INT_DEN + Files.TAB + 
			HEADER_LABEL_TRELLIS + newLine;
		
		for (int i = 0; i < this.well_data.size(); i++) {
			DataCell cellData = this.well_data.get(i);
			float value = cellData.getValueDyeThree();
			fileData += String.valueOf(i + 1) + getEmptyTabbedLine() + String.valueOf(value) + newLine;
		}
		
		return fileData;
	}
	
	public DataWell getDataWell() {
		return this.well_data;
	}
	
	public void setDataWell(DataWell dataWell) {
		this.well_data = dataWell;
	}
	
	private String getEmptyTabbedLine() {
		String tabbedLine = "";
		for (int i = 0; i < 6; i++) {
			tabbedLine += Files.TAB;
		}
		return tabbedLine;
	}
}