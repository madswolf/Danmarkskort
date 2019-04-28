package bfst19.osmdrawing;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MapCanvas extends Canvas {
    GraphicsContext gc = getGraphicsContext2D();
    //linear transformation object used to transform our data while preserving proportions between nodes
    Affine transform = new Affine();
    Model model;
    HashMap<WayType,Color> wayColors = new HashMap<>();
    boolean paintNonRoads = true;
    int detailLevel =1;
    private boolean colorBlindEnabled = false;


    public void init(Model model) {
        this.model = model;
        //conventions in screen coords and map coords are not the same,
        // so we convert to screen convention by flipping x y
        pan(-model.minlon, -model.maxlat);
        //TODO #soup type colors method
        setTypeColors();
        //sets an initial zoom level, 800 for now because it works
        zoom(800/(model.maxlon-model.minlon), 0,0);
        transform.prependScale(1,-1, 0, 0);
        //model.addObserver(this::repaint);
        model.addObserver(this::setTypeColors);
        repaint();
    }

    public void repaint() {
        //to clearly communicate that the fillRect should fill the entire screen
        gc.setTransform(new Affine());
        //checks if the file contains coastlines or not, if not set background color to white
        // otherwise set background color to blue
        if (model.getWaysOfType(WayType.COASTLINE).iterator().hasNext()) {
            gc.setFill(getColor(WayType.WATER));
        } else {
            gc.setFill(Color.WHITE);
        }
        //clears screen by painting a color on the entire screen not background
        gc.fillRect(0, 0, getWidth(), getHeight());
        gc.setTransform(transform);
        //line width equals 1 px wide relative to the screen no matter zoom level
        gc.setLineWidth(1/Math.sqrt(Math.abs(transform.determinant())));

        gc.setFillRule(FillRule.EVEN_ODD);

        //color for landmasses with nothing drawn on top
        gc.setFill(Color.WHITE);
        for (Drawable way : model.getWaysOfType(WayType.COASTLINE)) way.fill(gc);
        gc.setFill(getColor(WayType.WATER));
        for (Drawable way : model.getWaysOfType(WayType.WATER)) way.fill(gc);


        gc.setFillRule(null);
        //checks for toggle for only roads
        if(paintNonRoads) {
            for (WayType type : WayType.values()) {

                if (!(type.isRoadOrSimilar()) && type.levelOfDetail()<detailLevel) {
                    if(type == WayType.COASTLINE) {
                    }else{
                        gc.setFill(getColor(type));
                        for (Drawable way : model.getWaysOfType(type)) way.fill(gc);

                    }
                } else if (type.isRoadOrSimilar() && type.levelOfDetail()<detailLevel) {
                    if (type == WayType.COASTLINE) {
                    //TODO Keep this or delete it
                    }else if(type==WayType.UNKNOWN) {

                    } else {
                        gc.setStroke(getColor(type));
                        for (Drawable way : model.getWaysOfType(type)) way.stroke(gc);

                    }
                }
            }

        }else{
            for(WayType type : WayType.values()){
                if(type.isRoadOrSimilar() && type.levelOfDetail()<detailLevel){
                    if(type == WayType.UNKNOWN){
                    // The unknown WayType is ways that have not been parsed to an implemented WayType,
                    // so it's better to exclude it.
                    }else{
                        gc.setStroke(getColor(type));
                        for (Drawable way : model.getWaysOfType(type)) way.stroke(gc);

                    }

                }
            }
        }
    }

    private Color getColor(WayType type) { return wayColors.get(type); }

    private void setTypeColors(){
        Iterator<String> iterator = model.colorIterator();
        while(iterator.hasNext()){
            wayColors.put(WayType.valueOf(iterator.next()),Color.valueOf(iterator.next()));
        }
        repaint();
    }

    public void pan(double dx, double dy) {
        transform.prependTranslation(dx, dy);
        repaint();
    }

    public void zoom(double factor, double x, double y) {
        transform.prependScale(factor, factor, x, y);
        //TODO Set level of detail dependent on determinant
        //this translates to "if zooming out decrement"
        if(factor < 1){
            detailLevel -= 1;
        //this translates to "if zooming in increment"
        }else if(factor > 1){
            detailLevel += 1;
        }
        repaint();
    }

    public void toggleNonRoads() {
        paintNonRoads = !paintNonRoads;
    }
}