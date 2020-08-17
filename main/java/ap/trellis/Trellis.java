package ap.trellis;
import ij.ImagePlus;
import ij.gui.Roi;
import ij.measure.Measurements;
import ij.measure.ResultsTable;
import ij.plugin.filter.Analyzer;
import ij.process.ImageProcessor;
import ij.process.ImageStatistics;

import java.awt.Point;

import ap.AutoPlotHelper;
import ap.AutoPlotImageProcessor;

/* Trellis, based on Bamford (1998) */
public class Trellis {
	private static final int INNER_NODE_RADIUS = 1;
		
	private boolean initial = true;
	// TODO stages en stage_nodes afhankelijk maken van radius?
	private boolean draw = false;
	private int stages = 12;
	private int stage_nodes = 9;
	private double radius = 20.0;
	
	private TrellisNode[][] trellis;
	private Point point;
	private AutoPlotImageProcessor image_processor;
	private ImageProcessor convolved_dx;
	private ImageProcessor convolved_dy;

	public Trellis() {
		this.trellis = new TrellisNode[this.stages][this.stage_nodes];
		initializeTrellis();		
	}
	
	public Trellis(int stages, int nodes) {
		this.stages = stages;
		this.stage_nodes = nodes;
		this.trellis = new TrellisNode[this.stages][this.stage_nodes];
		initializeTrellis();
	}
	
	public void create(Point point, AutoPlotImageProcessor imageProcessor) {
		this.point = point;
		this.image_processor = imageProcessor;
		processPointAsTrellis();
		setGradientAndEnergy();
		
		// Go through the trellis again, but start halfway the previous trellis
		this.initial = false;
		int sliceStage = getStages() / 2;
		for (int stage = 0; stage < (getStages() / 2); stage++) {
			TrellisNode tmpStage[] = this.trellis[stage];
			this.trellis[stage] = this.trellis[stage + sliceStage];
			this.trellis[stage + sliceStage] = tmpStage;
		}
		setGradientAndEnergy();
	}
	
	public void findContour(String debugCsvFilePath) {
		TrellisContourFinder trellisContourFinder = new TrellisContourFinder(this.image_processor, this);
		if (isDraw()) {
			trellisContourFinder.setDraw();
		}
		if ("".equals(debugCsvFilePath)) {
			trellisContourFinder.findContour();
		} else {
			trellisContourFinder.findAndDebugContour(debugCsvFilePath);
		}
		//System.out.println(getObjectPixelValue() + " " + getObjectDonutPixelValue());
	}
	
	/**
	 * @deprecated
	 */
	public int getTrellisAveragePixelValue() {
		int pixelValueInsideContour = 0;
		int trellisPixelsWithinContour = 0;
		TrellisContourFinder trellisContourFinder = new TrellisContourFinder(this.image_processor, this);
		int[] contourNodeNumbers = trellisContourFinder.getContourNodeNumbers();
		for (int stage = 0; stage < this.trellis.length; stage++) {
			for (int node = 0; node < this.trellis[0].length; node++) {
				if (node < contourNodeNumbers[stage]) {
					pixelValueInsideContour = pixelValueInsideContour + this.trellis[stage][node].getValue();
					trellisPixelsWithinContour++;
				}
			}
		}
		
		int averagePixelValueInsideContour = pixelValueInsideContour / trellisPixelsWithinContour;
		return averagePixelValueInsideContour;
	}
	
	public double getMeanPixelValue() {
		TrellisContourFinder trellisContourFinder = new TrellisContourFinder(this.image_processor, this);
		Roi roi = trellisContourFinder.getContourAsRoi();
		ImageProcessor imageProcessor = this.image_processor.getImageProcessor();
		ImagePlus imagePlus = new ImagePlus("", imageProcessor);
		imagePlus.setRoi(roi);
		ResultsTable resultsTable = new ResultsTable();
		int measurements = Measurements.MEAN;
		//int measurements = Measurements.INTEGRATED_DENSITY;
		ImageStatistics statistics = imagePlus.getStatistics(measurements);
		Analyzer analyzer = new Analyzer(imagePlus, measurements, resultsTable);
		analyzer.saveResults(statistics, roi);
		return resultsTable.getValueAsDouble(ResultsTable.MEAN, 0);
		//return resultsTable.getValueAsDouble(ResultsTable.INTEGRATED_DENSITY, 0);
	}
	
	public void setConvolvedImages(ImageProcessor convolvedDx, ImageProcessor convolvedDy) {
		this.convolved_dx = convolvedDx;
		this.convolved_dy = convolvedDy;
	}
	
	public int getPixelValue(int stage, int node) {
		return this.trellis[stage][node].getValue();
	}
	
	public void setNode(int stage, int node, TrellisNode trellisNode) {
		this.trellis[stage][node] = trellisNode;
	}
	
	public TrellisNode[][] getNodes() {
		return this.trellis;
	}
	
	public int getStages() {
		return this.stages;
	}
	
	public void setStages(int stages) {
		this.stages = stages;
	}
	
	public int getStageNodes() {
		return this.stage_nodes;
	}
	
	public void setStageNodes(int stageNodes) {
		this.stage_nodes = stageNodes;
	}
	
	public double getRadius() {
		return this.radius;
	}
	
	public void setRadius(double radius) {
		this.radius = radius;
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
	
	public Point getCenterPoint() {
		return this.point;
	}
	
	public ImageProcessor getConvolvedDx() {
		return this.convolved_dx;
	}
	
	public ImageProcessor getConvolvedDy() {
		return this.convolved_dy;
	}
	
	private void processPointAsTrellis() {
		for (int stage = 0; stage < getStages(); stage++) {
			int nodes = getStageNodes() + INNER_NODE_RADIUS;
			for (int node = INNER_NODE_RADIUS; node < nodes; node++) {
				setTrellisNode(stage, node);
			}
		}
	}
	
	private void setTrellisNode(int stage, int node) {
		int x = getImageCoordinateX(stage, node);
		int y = getImageCoordinateY(stage, node);
		int value = this.image_processor.getImageProcessor().getPixel(x, y);
		TrellisNode trellisNode = new TrellisNode(new Point(x, y), value);
		
		int nodeInTrellis = node - INNER_NODE_RADIUS;
		trellisNode.setTrellisCoordinates(new Point(stage, nodeInTrellis));
		
		setNode(stage, nodeInTrellis, trellisNode);
	}
	
	private void setGradientAndEnergy() {
		for (int stage = 0; stage < getStages(); stage++) {
			if (this.initial) {
				setStageGradient(stage);
			}
			setStageEnergy(stage);
		}
	}
	
	private void setStageGradient(int stage) {
		for (int node = 0; node < getStageNodes(); node++) {
			double gradient = getNodeGradient(stage, node);
			this.trellis[stage][node].setGradient(gradient);
		}
	}
	
	private double getNodeGradient(int stage, int node) {
		int x = getImageCoordinateX(stage, node);
		int y = getImageCoordinateY(stage, node);
		float dX = this.convolved_dx.getPixelValue(x, y); 
		float dY = this.convolved_dy.getPixelValue(x, y);
		return TrellisCalculator.calculateGradient(dX, dY, stage, getStages());
	}
	
	private void setStageEnergy(int currentStage) {
		if (currentStage<getStages() - 1) {
			int nextStage = currentStage + 1;
			int previousStage = getPreviousStage(currentStage);
			setEnergy(previousStage, currentStage, nextStage);
		}
	}
	
	private int getPreviousStage(int currentStage) {
		int previousStage = currentStage - 1;
		if (currentStage == 0) {
			previousStage = getStages() - 1;
		}
		return previousStage;
	}
	
	private void setEnergy(int previousStage, int currentStage, int nextStage) {
		for (int yCurrentStageNode = 0; yCurrentStageNode < getStageNodes(); yCurrentStageNode++) {
			TrellisNode currentStageNode = this.trellis[currentStage][yCurrentStageNode];
			if (currentStage == 0 && this.initial) {
				currentStageNode.setPreviousNode(yCurrentStageNode);
				currentStageNode.setMinimalEnergy(0);
			}
			TrellisNode previousStageNode = this.trellis[previousStage][currentStageNode.getPreviousNode()]; 
			calculateAndSetEnergy(previousStageNode, currentStageNode, nextStage);
		}
	}
	
	private void calculateAndSetEnergy(TrellisNode previousStageNode, TrellisNode currentStageNode, int nextStage) {
		for (int yNextStageNode = 0; yNextStageNode < getStageNodes(); yNextStageNode++) {
			TrellisNode nextStageNode = this.trellis[nextStage][yNextStageNode];
			
			double internalEnergy = calculateAndSetInternalEnergy(previousStageNode, currentStageNode, nextStageNode);
			double externalEnergy = calculateAndSetExternalEnergy(currentStageNode);
			double energy = TrellisCalculator.calculateTotalEnergy(currentStageNode.getMinimalEnergy(), internalEnergy, externalEnergy); 
			
			if (yNextStageNode == 0 || energy < nextStageNode.getMinimalEnergy() || nextStageNode.getMinimalEnergy() == 0) {
				nextStageNode.setMinimalEnergy(energy);
				nextStageNode.setPreviousNode(AutoPlotHelper.doubleToInt(currentStageNode.getTrellisCoordinates().getY()));
			}
		}
	}
	
	private double calculateAndSetInternalEnergy(TrellisNode previousStageNode, TrellisNode currentStageNode, TrellisNode nextStageNode) {
		double internalEnergy = TrellisCalculator.calculateInternalEnergy(
				previousStageNode.getImageCoordinates(), 
				currentStageNode.getImageCoordinates(), 
				nextStageNode.getImageCoordinates()
			);
		// TODO Check dit. Dit moet eigenlijk niet kunnen
		if (Double.isNaN(internalEnergy)) {
			internalEnergy = 0.0;
		}
		nextStageNode.setInternalEnergy(internalEnergy);
		return internalEnergy;
	}
	
	private double calculateAndSetExternalEnergy(TrellisNode currentStageNode) {
		double externalEnergy = TrellisCalculator.calculateExternalEnergy(currentStageNode.getGradient());
		currentStageNode.setExternalEnergy(externalEnergy);
		return externalEnergy;
	}
	
	private void initializeTrellis() {
		for(int stage=0; stage<stages; stage++) {
			for(int node=0; node<stage_nodes; node++) {
				trellis[stage][node] = new TrellisNode(new Point(), 0);
			}
		}
	}
	
	private int getImageCoordinateX(int stage, int node) {
		int x = AutoPlotHelper.doubleToInt(this.point.getX() + ((node * this.radius / this.stage_nodes) * Math.sin((stage * 2 * Math.PI) / this.stages)));
		return x;
	}
	
	private int getImageCoordinateY(int stage, int node) {
		int y = AutoPlotHelper.doubleToInt(this.point.getY() - ((node * this.radius / this.stage_nodes) * Math.cos((stage * 2 * Math.PI) / this.stages)));
		return y;
	}
}
