package ap.data;

import java.awt.Point;

public class DataCell {
	private int row;
	private int area;
	private float int_den_gfp;
	private float raw_int_den_gfp;
	private float int_den_pi;
	private float raw_int_den_pi;
	private float value_dye_three;
	
	private Point centroid = new Point(0, 0);

	public void setRow(String row) {
		//System.out.println("String: " + row);
		this.row = Integer.valueOf(row.trim()).intValue();
	}

	public void setArea(String area) {
		this.area = Integer.valueOf(area.trim()).intValue();
	}

	public void setCentroid(String x, String y) {
		double xD = (new Double(x.trim())).doubleValue();
		double yD = (new Double(y.trim())).doubleValue();
		this.centroid.setLocation(xD, yD);
	}

	public void setIntDenGfp(String intDen) {
		this.int_den_gfp = Float.valueOf(intDen.trim()).floatValue();
	}

	public void setRawIntDenGfp(String rawIntDen) {
		this.raw_int_den_gfp = Float.valueOf(rawIntDen.trim()).floatValue();
	}

	public void setIntDenPi(String intDen) {
		this.int_den_pi = Float.valueOf(intDen.trim()).floatValue();
	}

	public void setRawIntDenPi(String rawIntDen) {
		this.raw_int_den_pi = Float.valueOf(rawIntDen.trim()).floatValue();
	}
	
	public void setValueDyeThree(int value) {
		this.value_dye_three = value;
	}
	
	public void setValueDyeThree(double value) {
		this.value_dye_three = (new Double(value)).floatValue();
	}
	
	public void setValueDyeThree(String value) {
		int intValue = Float.valueOf(value).intValue();
		setValueDyeThree(intValue);
	}

	public int getRow() {
		return this.row;
	}

	public int getArea() {
		return this.area;
	}

	public Point getCentroid() {
		return this.centroid;
	}
	
	public float getIntDenGfp() {
		return this.int_den_gfp;
	}

	public float getRawIntDenGfp() {
		return this.raw_int_den_gfp;
	}

	public float getIntDenPi() {
		return this.int_den_pi;
	}

	public float getRawIntDenPi() {
		return this.raw_int_den_pi;
	}
	
	public float getValueDyeThree() {
		return this.value_dye_three;
	}
}
