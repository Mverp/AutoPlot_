package ap.trellis;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;


public class TrellisDebugger {
	TrellisNode[][] trellis;
	
	public TrellisDebugger(TrellisNode[][] trellisNodes) {
		this.trellis = trellisNodes;
	}
	
	public void debug(String debugCsvFilePath) {
		System.out.println("Creating CSV file...");
		// String separator = ",";
		String separator = ";";
		try {
			File file = new File(debugCsvFilePath);
			String[] valueTypes = {"value", "coordinates", "internalenergy", "gradient", "externalenergy", "minimalenergy"};
			
			Writer out = new OutputStreamWriter(new FileOutputStream(file));
			
			for(int i = 0; i < valueTypes.length; i++) {
				
				String valueHeader = valueTypes[i] + separator;
				for (int stage = 0; stage < getStages(); stage++) {
					valueHeader += String.valueOf(stage) + separator;
				}
				out.write(valueHeader);
				out.write(System.getProperty("line.separator"));
				for (int node = 0; node < getStageNodes(); node++) {
					String nodeValues = String.valueOf(node) + separator;
					for (int stage = 0; stage < getStages(); stage++) {
						if (this.trellis[stage][node].isContour()) {
							nodeValues += "#";
						}
						if ("value".equals(valueTypes[i])) {
							nodeValues += this.trellis[stage][node].getValue() + separator;
						}
						if ("coordinates".equals(valueTypes[i])) {
							nodeValues += this.trellis[stage][node].getImageCoordinates().getX() + "," + this.trellis[stage][node].getImageCoordinates().getY() + "\"" + separator;
						}
						if ("internalenergy".equals(valueTypes[i])) {
							nodeValues += this.trellis[stage][node].getInternalEnergy() + separator;
						}
						if ("gradient".equals(valueTypes[i])) {
							nodeValues += this.trellis[stage][node].getGradient() + separator;
						}
						if ("externalenergy".equals(valueTypes[i])) {
							nodeValues += this.trellis[stage][node].getExternalEnergy() + separator;
						}
						if ("minimalenergy".equals(valueTypes[i])) {
							nodeValues += this.trellis[stage][node].getMinimalEnergy() + separator;
						}
					}
					out.write(nodeValues);
					out.write(System.getProperty("line.separator"));
				}
				out.write(System.getProperty("line.separator"));
			}
			
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("CSV file done.");
	}
	
	public void debugWithTrellisImages(String debugCsvFilePath, TrellisNode[] contourNodes) {
		debug(debugCsvFilePath);
		System.out.println("Creating Trellis images...");
		
		int[][] values = new int[getStages()][getStageNodes()];
		int[][] gradients = new int[getStages()][getStageNodes()];
		for (int i=0; i<getStages(); i++) {
			for(int j=0; j<getStageNodes(); j++) {
				values[i][j] = this.trellis[i][j].getValue();
				gradients[i][j] = new Double(this.trellis[i][j].getGradient()).intValue();
			}
		}
		createImage(values, contourNodes);
		createImage(gradients, contourNodes);
		
		System.out.println("Trellis images done.");
	}
	
	private void createImage(int[][] values, TrellisNode[] contourNodes) {
		TrellisImage image = new TrellisImage(values, contourNodes);
		image.show();
	}
	
	private int getStages() {
		return this.trellis.length;
	}
	
	private int getStageNodes() {
		return this.trellis[0].length;
	}
}
