package ap.data;

import java.util.ArrayList;

public class DataWellPlate {
	private ArrayList<DataWell> plateData;
	private String plateName;

	public DataWellPlate(String plateName) {
		setName(plateName);
		plateData = new ArrayList<DataWell>();
	}

	public void add(DataWell plotData) {
		plateData.add(plotData);
	}

	public String getName() {
		return plateName;
	}

	public DataWell get(int index) {
		return plateData.get(index);
	}

	private void setName(String name) {
		plateName = name;
	}

	public int size() {
		return plateData.size();
	}
}
