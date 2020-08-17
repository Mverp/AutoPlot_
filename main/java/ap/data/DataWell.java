package ap.data;


import java.util.ArrayList;
import java.util.Collections;

import ap.ComparatorByArea;

public class DataWell {
	private ArrayList<DataCell> wellData;
	private String datasetName;

	public DataWell(String wellName) {
		setName(wellName);
		wellData = new ArrayList<DataCell>();
	}

	public void add(DataCell cellData) {
		wellData.add(cellData);
	}

	public ArrayList<DataCell> get() {
		return this.wellData;
	}
	
	public DataCell get(int index) {
		return wellData.get(index);
	}

	public String getName() {
		return datasetName;
	}

	public void sort() {
		Collections.sort(wellData, new ComparatorByArea());
	}

	public int size() {
		return wellData.size();
	}

	private void setName(String name) {
		datasetName = name;
	}
}
