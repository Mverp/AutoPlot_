package ap.trellis;
import java.awt.Point;

public class TrellisNode {
	private int value;
	private int previous_node;
	private Point image_coordinates;
	private Point trellis_coordinates;
	
	private boolean is_contour = false;
	private double internal_energy = 0.0;
	private double external_energy = 0.0;
	private double gradient = 0.0;
	private double minimal_energy = 0.0;
	
	public TrellisNode(Point imageCoordinates, int value) {
		this.image_coordinates = imageCoordinates;
		this.value = value;
	}
	
	public Point getImageCoordinates() {
		return this.image_coordinates;
	}
	
	public void setImageCoordinates(Point imageCoordinates) {
		this.image_coordinates = imageCoordinates;
	}
	
	public Point getTrellisCoordinates() {
		return this.trellis_coordinates;
	}
	
	public void setTrellisCoordinates(Point trellisCoordinates) {
		this.trellis_coordinates = trellisCoordinates;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public double getGradient() {
		return this.gradient;
	}
	
	public void setGradient(double gradient) {
		this.gradient = gradient;
	}
	
	public double getInternalEnergy() {
		return this.internal_energy;
	}
	
	public void setInternalEnergy(double internalEnergy) {
		this.internal_energy = internalEnergy;
	}
	
	public double getExternalEnergy() {
		return this.external_energy;
	}
	
	public void setExternalEnergy(double externalEnergy) {
		this.external_energy = externalEnergy;
	}
	
	public double getMinimalEnergy() {
		return this.minimal_energy;
	}
	
	public void setMinimalEnergy(double minimalEnergy) {
		this.minimal_energy = minimalEnergy;
	}
	
	public void setPreviousNode(int node) {
		this.previous_node = node;
	}
	
	public int getPreviousNode() {
		return this.previous_node;
	}
	
	public void setContour() {
		this.is_contour = true;
	}
	
	public void unsetContour() {
		this.is_contour = false;
	}
	
	public boolean isContour() {
		return this.is_contour;
	}
}
