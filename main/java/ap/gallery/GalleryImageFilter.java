package ap.gallery;
import java.awt.Point;

import ij.gui.Roi;

public class GalleryImageFilter extends GalleryImage {
	
	public GalleryImageFilter(String path) {
		super(path);
	}
	
	public GalleryImageCentroid getCentroidImage(String name, Point centroid) {
		setRoi(
				(new Double(centroid.getX()).intValue()), 
				(new Double(centroid.getY()).intValue())
		);
		return new GalleryImageCentroid(name, this.image_plus.getProcessor().crop());
	}
	
	private void setRoi(int x, int y) {
		Roi roi = new Roi(
				x - (GalleryImageCentroid.WIDTH/2), 
				y - (GalleryImageCentroid.HEIGHT/2), 
				GalleryImageCentroid.WIDTH, 
				GalleryImageCentroid.HEIGHT
			);
		this.image_plus.setRoi(roi);
	}
	
	public void flatten() {
		if (this.image_plus.getStackSize() > 1) {
			this.image_plus = this.image_plus.flatten();
		}
	}
}
