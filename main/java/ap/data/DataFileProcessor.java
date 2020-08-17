package ap.data;
import ij.IJ;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;

import ap.Files;

public class DataFileProcessor {
	private boolean debug = false;
	
	private File dataFilesPath;
	
	public DataFileProcessor(File dataFilesPath) {
		this.dataFilesPath = dataFilesPath;
	}
	
	public DataWell getWellData() {
		return processDataFiles();
	}
	
	private File[] getDataFiles() {
		FilenameFilter fileNameFilter = Files.getFileNameFilterXls();
		File[] dataFiles = this.dataFilesPath.listFiles(fileNameFilter);
		return dataFiles;
	}
	
	private DataWell processDataFiles() {
		/*
			The Excel files generated by the Macro aren't really Excel
			files, but just text files with an .xls extension.

			TODO: generate CSV files;
		*/
		File[] dataFiles = getDataFiles();
		String wellName = this.dataFilesPath.getName();
		DataWell plotData = new DataWell(wellName);
		File dataFileDyeOne = new File("");
		File dataFileDyeTwo = new File("");
		File dataFileDyeThree = new File("");
		
		for (int i=0; i<dataFiles.length; i++) {
			String fileName = dataFiles[i].getName();
			if (fileName.startsWith(Files.RESULTS_PREFIX) && fileName.matches(Files.REGEX_DYE_ONE_XLS)) {
				dataFileDyeOne = dataFiles[i];
			}
			else if (fileName.startsWith(Files.RESULTS_PREFIX) && fileName.matches(Files.REGEX_DYE_TWO_XLS)) {
				dataFileDyeTwo = dataFiles[i];
			}
			else if (fileName.startsWith(Files.RESULTS_PREFIX) && fileName.matches(Files.REGEX_DYE_THREE_XLS)) {
				dataFileDyeThree = dataFiles[i];
			}
		}

		/* 
		 * TODO This order is important! It shouldn't be. 
		 * Perhaps let auto_pathway Macro write data to one Excel file to solve this.
		 */
		if (!"".equals(dataFileDyeOne.getName()) && !"".equals(dataFileDyeTwo.getName())) {
			processDataFile(dataFileDyeOne, plotData, Files.DYE_ONE);
			processDataFile(dataFileDyeTwo, plotData, Files.DYE_TWO);
			if (!"".equals(dataFileDyeThree.getName())) {
				processDataFile(dataFileDyeThree, plotData, Files.DYE_THREE);
			}
			plotData.sort();
		} else {
			IJ.log(wellName + ": No datafiles found.");
		}
		debug("plotData size: " + plotData.size());
		return plotData;
	}
	
	// TODO deze methode (en eigenlijk de klasse) is lelijk geworden; herschrijven
	private void processDataFile(File file, DataWell plotData, String type) {
		try {
			String line = "";
        	FileReader fr = new FileReader(file);
        	debug(file.getName());
			BufferedReader br = new BufferedReader(fr);
			while ( (line = br.readLine()) != null ) {
				String[] values = line.split("\\s");
				if (valueIsNumeric(values[0])) {
					if (values.length == 6 && !"".equals(values[0])) {
						
						debug(
							"(1) " + values[0] + ", (2) " + values[1] + ", (3) " +  values[2] + 
							", (4) " +  values[3] + ", (5) " + values[4] + ", (6) " + values[5]
						); 
						
						if (Files.DYE_ONE.equals(type)) {
							addGfpValuesToNewPlotDataRow(plotData, values);
						} else {
							addPiValuesToLastPlotDataRow(plotData, values);
						}
					} else if (values.length == 7 && !"".equals(values[0]) && Files.DYE_THREE.equals(type)){
						addDyeThreeValuesToLastPlotDataRow(plotData, values);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean valueIsNumeric(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private void addGfpValuesToNewPlotDataRow(DataWell plotData, String[] values) {
		DataCell pdr = new DataCell();
		pdr.setRow(values[0]);
		pdr.setArea(values[1]);
		pdr.setCentroid(values[2], values[3]);
		pdr.setIntDenGfp(values[4]);
		pdr.setRawIntDenGfp(values[5]);
		plotData.add(pdr);
	}

	private void addPiValuesToLastPlotDataRow(DataWell plotData, String[] values) {
		int row = Integer.valueOf(values[0]).intValue();
		DataCell pdr = plotData.get(row - 1);
		pdr.setIntDenPi(values[4]);
		pdr.setRawIntDenPi(values[5]);
	}
	
	private void addDyeThreeValuesToLastPlotDataRow(DataWell plotData, String[] values) {
		int row = Integer.valueOf(values[0]).intValue();
		DataCell pdr = plotData.get(row - 1);
		pdr.setValueDyeThree(values[6]);
	}
	
	private void debug(String message) {
		if (this.debug) {
			IJ.log(message);
			System.out.println(message);
		}
	}
}