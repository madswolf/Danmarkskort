package bfst19;

import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;
import java.util.ArrayList;

public class MultiPolyline implements Drawable, Serializable, BoundingBoxable {
	public ArrayList<Polyline> lines;
	private float centerX;
	private float centerY;
	private BoundingBox bb;

	public MultiPolyline(OSMRelation rel) {
		lines = new ArrayList<>();
		for (OSMWay way : rel){
			Polyline addingLine = new Polyline(way);
			add(addingLine);
		}

		//Find the limits of BoundingBox
		//Code copy pasted to KDTree
		//Arbitrary values that should exceed the coords on Denmark
		double minX = 100, maxX = 0, minY = 100, maxY = 0;
		for(Polyline polyline : lines) {
			BoundingBox lineBB = polyline.getBB();
			if (lineBB.getMinX() < minX) {
				minX = lineBB.getMinX();
			}
			if (lineBB.getMaxX() > maxX) {
				maxX = lineBB.getMaxX();
			}
			if (lineBB.getMinY() < minY) {
				minY = lineBB.getMinY();
			}
			if (lineBB.getMaxY() > maxY) {
				maxY = lineBB.getMaxY();
			}
		}

		this.centerX = (float) (minX + maxX) / 2;
		this.centerY = (float) (minY + maxY) / 2;
		bb = new BoundingBox(minX, minY, maxX-minX, maxY-minY);
	}

	private void add(Polyline addingLine) {
		lines.add(addingLine);
	}

	@Override
	public float getCenterX() {
		return centerX;
	}

	@Override
	public float getCenterY() {
		return centerY;
	}

	@Override
	public BoundingBox getBB() {

		return bb;
	}

	@Override
	public void stroke(GraphicsContext gc) {
		gc.beginPath();
		trace(gc);
		gc.stroke();
	}

	public void trace(GraphicsContext gc) {
		for (Polyline p : lines) p.trace(gc);
	}

	@Override
	public void fill(GraphicsContext gc) {
		gc.beginPath();
		trace(gc);
		gc.fill();
	}

}