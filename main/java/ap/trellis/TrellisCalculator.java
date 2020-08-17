package ap.trellis;
import java.awt.Point;


public class TrellisCalculator {
	private static double labda = 0.7;
	
	public static double calculateInternalEnergy(Point previousStageNode, Point currentStageNode, Point nextStageNode) {
		double internalEnergy = 2 - 2 * (
				(
						((nextStageNode.getX() - currentStageNode.getX()) * (currentStageNode.getX() - previousStageNode.getX())) +
						((nextStageNode.getY() - currentStageNode.getY()) * (currentStageNode.getY() - previousStageNode.getY()))
				) / (
						Math.sqrt(
								Math.pow((nextStageNode.getX() - currentStageNode.getX()),2) + 
								Math.pow((nextStageNode.getY() - currentStageNode.getY()), 2)
						) * 
						Math.sqrt(
								Math.pow((currentStageNode.getX() - previousStageNode.getX()), 2) +
								Math.pow((currentStageNode.getY() - previousStageNode.getY()), 2)
						)
				)
		);
		return internalEnergy;
	}
	
	public static double calculateGradient(float dX, float dY, int currentStage, int totalStages) {
		double stageFracture = (new Double(currentStage).doubleValue() / totalStages);
		double dYCos = dY * Math.cos(2 * Math.PI * stageFracture);
		double dXSin = dX * Math.sin(2 * Math.PI * stageFracture);
		return dYCos - dXSin;
	}
	
	public static double calculateExternalEnergy(double gradient) {
		//return (1 / (1 + Math.exp(-1 * gradient)));
		return -1 * gradient;
	}
	
	public static double calculateTotalEnergy(double minimalEnergy, double internalEnergy, double externalEnergy) {
		double labda = TrellisCalculator.labda;
		return minimalEnergy + (labda * internalEnergy) + ((1 - labda) * externalEnergy);
	}
}
