package ap.trellis;
import ij.gui.PolygonRoi;
import ij.gui.Roi;
import ij.process.ImageProcessor;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;

import ap.AutoPlotHelper;
import ap.AutoPlotImageProcessor;


public class TrellisContourFinder {
	private boolean draw = false;
	private boolean debug = false;
	private String csv_debug_path = "";
	
	private Point contour_centroid;
	private AutoPlotImageProcessor autoplot_image_processor;
	private Trellis trellis;
	
	public TrellisContourFinder(AutoPlotImageProcessor imageProcessor, Trellis trellis) {
		this.autoplot_image_processor = imageProcessor;
		this.trellis = trellis;
	}
	
	public void findContour() {
		calculatePath();
	}
	
	public void findAndDebugContour(String csvDebugPath) {
		this.debug = true;
		this.csv_debug_path = csvDebugPath;
		calculatePath();
	}
	
	public int[] getContourNodeNumbers() {
		int[] contourNodeNumbers = new int[getStages()];
		for (int stage = 0; stage < getStages(); stage++) {
			for (int node = 0; node < getStageNodes(); node++) {
				if (this.trellis.getNodes()[stage][node].isContour()) {
					contourNodeNumbers[stage] = node;
				}
			}
		}
		return contourNodeNumbers;
	}
	
	public Roi getContourAsRoi() {
		int[] contourNodeNumbers = getContourNodeNumbers();
		TrellisNode[][] nodes = this.trellis.getNodes();
		Polygon polygon = new Polygon();
		for (int stage=0; stage<contourNodeNumbers.length; stage++) {
			int node = contourNodeNumbers[stage];
			TrellisNode trellisNode = nodes[stage][node];
			Point contourPoint = trellisNode.getImageCoordinates();
			polygon.addPoint(contourPoint.x, contourPoint.y);
		}
		Roi roi = new PolygonRoi(polygon, Roi.POLYGON);
		return roi;
	}
	
	public void setDraw() {
		this.draw = true;
	}
	
	public void unsetDraw() {
		this.draw = false;
	}
	
	public boolean isDraw() {
		return this.draw;
	}
	
	public void setDebug() {
		this.debug = true;
	}
	
	public void unsetDebug() {
		this.debug = false;
	}
	
	public boolean isDebug() {
		return this.debug;
	}
	
	public Point getContourCentroid() {
		return this.contour_centroid;
	}
	
	private void calculatePath() {
		TrellisNode[] contourNodes = markAndGetContourNodes();
		Point center =  getContourCenter(contourNodes);
		ArrayList<String> oldCenterCoordinates = new ArrayList<String>();
		oldCenterCoordinates.add(getCoordinate(this.trellis.getCenterPoint()));
		oldCenterCoordinates.add(getCoordinate(center));
		if (isValidTrellis()) {
			int i = 0;
			do {
				Trellis newTrellis = new Trellis(getStages(), getStageNodes());
				newTrellis.setConvolvedImages(this.trellis.getConvolvedDx(), this.trellis.getConvolvedDy());
				newTrellis.create(center, this.autoplot_image_processor);
				contourNodes = markAndGetContourNodes(newTrellis.getNodes());
				oldCenterCoordinates.add(getCoordinate(center));
				center = getContourCenter(contourNodes);
				this.trellis = newTrellis;
				i++;
			} while (!oldCenterCoordinates.contains(getCoordinate(center)) && i<10);
		}
		
		if (isDebug()) {
			TrellisDebugger trellisDebugger = new TrellisDebugger(this.trellis.getNodes());
			trellisDebugger.debugWithTrellisImages(this.csv_debug_path, contourNodes);
		}
		
		if (isDraw()) {
			int x = AutoPlotHelper.doubleToInt(center.getX());
			int y = AutoPlotHelper.doubleToInt(center.getY());
			this.autoplot_image_processor.getImageProcessor().putPixel(x, y, 1000);
			drawContour(contourNodes);
		}
	}
	
	private boolean isValidTrellis() {
		return this.trellis.getMeanPixelValue() > this.autoplot_image_processor.getMean();
	}
	
	private Point getContourCenter(TrellisNode[] contourNodes) {
		TrellisNode lowestX = null;
		TrellisNode highestX = null;
		TrellisNode lowestY = null;
		TrellisNode highestY = null;
		for(int i=0; i<contourNodes.length; i++) {
			Point contourPoint = contourNodes[i].getImageCoordinates();
			double x = contourPoint.getX();
			double y = contourPoint.getY();
			
			if (lowestX == null || x < lowestX.getImageCoordinates().getX()) {
				lowestX = contourNodes[i];
			}
			if (highestX == null || x > highestX.getImageCoordinates().getX()) {
				highestX = contourNodes[i];
			}
			if (lowestY == null || y < lowestY.getImageCoordinates().getY()) {
				lowestY = contourNodes[i];
			}
			if (highestY == null || y > highestY.getImageCoordinates().getY()) {
				highestY = contourNodes[i];
			}
		}

		double centerX = ((highestX.getImageCoordinates().getX() - lowestX.getImageCoordinates().getX()) / 2) + lowestX.getImageCoordinates().getX();
		double centerY = ((highestY.getImageCoordinates().getY() - lowestY.getImageCoordinates().getY()) / 2) + lowestY.getImageCoordinates().getY();
		return new Point(AutoPlotHelper.doubleToInt(centerX), AutoPlotHelper.doubleToInt(centerY));
	}
	
	private void drawContour(TrellisNode[] contourNodes) {
		ImageProcessor imageProcessor = this.autoplot_image_processor.getImageProcessor();
		drawContour(contourNodes, imageProcessor);
	}
	
	private void drawContour(TrellisNode[] contourNodes, ImageProcessor imageProcessor) {
		Point oldNode = new Point();
		Point newNode = new Point();
		for (int i = 0; i < contourNodes.length; i++) {
			Point node = contourNodes[i].getImageCoordinates();
			oldNode = newNode;
			if (i == 0) {
				oldNode = node;
			}
			newNode = node;
			imageProcessor.setColor(Color.GRAY);
			imageProcessor.drawLine(oldNode.x, oldNode.y, newNode.x, newNode.y);
		}
	}
	
	private TrellisNode[] markAndGetContourNodes() {
		TrellisNode[][] trellis = this.trellis.getNodes();
		return markAndGetContourNodes(trellis);
	}
	
	private TrellisNode[] markAndGetContourNodes(TrellisNode[][] trellis) {
		int lastStage = getStages() - 1;
		int contourNode = calculateContourNode(trellis[lastStage]);
		TrellisNode[] contourNodes = new TrellisNode[getStages()];
		TrellisNode node = trellis[lastStage][contourNode];
		contourNodes[lastStage] = node;
		int lowestEnergyNode = node.getPreviousNode(); 
		for (int stage = lastStage; stage >= 0; stage--) {
			node = trellis[stage][lowestEnergyNode];
			node.setContour();
			contourNodes[stage] = node;
			lowestEnergyNode = node.getPreviousNode();
		}
		return contourNodes;
	}
	
	private int calculateContourNode(TrellisNode[] nodes) {
		int contourNode = 0;
		double lowestEnergy = 0;
		for (int i = 0; i < nodes.length; i++) {
			if (i == 0) {
				lowestEnergy = nodes[i].getMinimalEnergy();
			} else {
				double nodeEnergy = nodes[i].getMinimalEnergy(); 
				if (nodeEnergy < lowestEnergy) {
					lowestEnergy = nodeEnergy;
					contourNode = i;
				}
			}
		}
		return contourNode;
	}
	
	private int getStages() {
		return this.trellis.getNodes().length;
	}
	
	private int getStageNodes() {
		return this.trellis.getNodes()[0].length;
	}
	
	private String getCoordinate(Point point) {
		return "[" + point.getX() + "-" + point.getY() + "]";
				
	}
}
