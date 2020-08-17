package ap.plot;

import ap.Configuration;

public class PlotLabel {
	public static final String DYE_ONE = "Dye 1";
	public static final String DYE_TWO = "Dye 2";
	public static final String DYE_THREE = "Dye 3";
	public static final String PREFIX_INTEGRATED_DENSITY = "Integrated Density";
	public static final String PREFIX_TRELLIS_VALUE = "Trellis Pixel Value";
	public static final String INT_DEN_DYE_ONE = PREFIX_INTEGRATED_DENSITY + " " + DYE_ONE;
	public static final String INT_DEN_DYE_TWO = PREFIX_INTEGRATED_DENSITY + " " + DYE_TWO;
	public static final String TRELLIS_VALUE_DYE_THREE =  PREFIX_TRELLIS_VALUE + " " + DYE_THREE;
	private Configuration configuration;
	
	public PlotLabel(Configuration configuration) {
		this.configuration = configuration;
	}
	
	public String createTitle(String plateName, String wellName) {
		return plateName + " (" + wellName + ")";
	}
	
	public String getAxisLabel(String axis) {
		String label = "";
		String prefix = "";
		if (INT_DEN_DYE_ONE.equals(axis)) {
			prefix = PREFIX_INTEGRATED_DENSITY;
			label = getChannelFromProperty(DYE_ONE);
		} else if (INT_DEN_DYE_TWO.equals(axis)) {
			prefix = PREFIX_INTEGRATED_DENSITY;
			label = getChannelFromProperty(DYE_TWO);
		} else if (TRELLIS_VALUE_DYE_THREE.equals(axis)) {
			prefix = PREFIX_TRELLIS_VALUE;
			label = getChannelFromProperty(DYE_THREE);
		}
		
		prefix += " ";
		if ("".equals(label)) {
			prefix = " ";
			label = axis;
		}
		
		label = "ln(" + prefix + label + ")";
		return label;
	}
	
	private String getChannelFromProperty(String property) {
		String channel = "";
		if (DYE_ONE.equals(property) || DYE_TWO.equals(property) || DYE_THREE.equals(property)) {
			// Expecting something like: "CHANNEL confocal - n000000.tif"
			String value = this.configuration.getProperty(property);
			if (!"".equals(value.trim())) {
				String array[] = value.split(" ");
				channel = array[0].trim();
			}
		}
		return channel;
	}
}
