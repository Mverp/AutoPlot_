package ap;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Scanner;

public class Configuration {
	public static final String CONFIGURATION_FILE = "configuration.ini";
	public static final String DYE_1 = "Dye 1";
	public static final String DYE_2 = "Dye 2";
	public static final String DYE_3 = "Dye 3";
	public static final String REFERENCE_1 = "Reference 1";
	public static final String REFERENCE_2 = "Reference 2";
	public static final String REFERENCE_3 = "Reference 3";
	public static final String LAYOUT = "Layout (w x h)";
	public static final String SINGLE_IMAGE_FOR_MASK = "Single image for mask";
	public static final String THRESHOLD = "Threshold";
	
	private String path = "";
	private HashMap<String, String> properties;
	
	public Configuration(String path) {
		this.path = path;
		this.properties = new HashMap<String, String>();
		
		try {
			// this.properties.load(new FileInputStream(path + File.separator + CONFIGURATION_FILE));
			Scanner scanner = new Scanner(new FileInputStream(path + File.separator + CONFIGURATION_FILE));
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String pair[] = line.split("=");
				properties.put(pair[0].trim(), pair[1].trim());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public String getPath() {
		return this.path;
	}
	
	public String getProperty(String property) {
		if (this.properties.containsKey(property)) {
			return this.properties.get(property);
		}
		return "";
	}
}
