package ap;

import java.util.Comparator;

import ap.data.DataCell;

public class ComparatorByArea implements Comparator<DataCell> {
	public int compare(DataCell a, DataCell b) {
  		int difference = ((DataCell)a).getArea() - ((DataCell)b).getArea();
  		return difference;
 	}
}
