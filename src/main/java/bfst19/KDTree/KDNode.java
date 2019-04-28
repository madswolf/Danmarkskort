package bfst19.KDTree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class KDNode implements Serializable {
    BoundingBoxable[] values;
    float split;
    boolean vertical; //if true, splits on x

    KDNode nodeL; //child
    KDNode nodeR; //child
    BoundingBox bb;

    public KDNode(float split, boolean vertical) {
        this.split = split;
        this.vertical = vertical;
        nodeL = nodeR = null;
    }

    //Sets up some arbitrary values that should be beyond the coords of Denmark
    // Runs through the values in the node to find the encompassing bounding box
    // Creates and sets the bounding box based on the values
    private void makeNodeBB() {
        double minX = 100, maxX = 0, minY = 100, maxY = 0;
        for(BoundingBoxable valueBB : values) {
            BoundingBox lineBB = valueBB.getBB();
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

        double width = maxX-minX;
        double height = maxY-minY;

        bb = new BoundingBox(minX, minY, maxX-minX, maxY-minY);
    }

    public void growToEncompassChildren() {
        BoundingBox leftBB;
        BoundingBox rightBB;

        if(nodeL == null && nodeR == null){
            return;
        }

        if(nodeL == null){
            rightBB = nodeR.getBB();
            setBB(rightBB.getMinX(), rightBB.getMinY(), rightBB.getMaxX(), rightBB.getMaxY());
            return;
        }

        if(nodeR == null){
            leftBB = nodeL.getBB();
            setBB(leftBB.getMinX(), leftBB.getMinY(), leftBB.getMaxX(), leftBB.getMaxY());
            return;
        }

        leftBB = nodeL.getBB();
        rightBB = nodeR.getBB();

        double minX = Double.min(leftBB.getMinX(),rightBB.getMinX());
        double minY = Double.min(leftBB.getMinY(),rightBB.getMinY());
        double maxX = Double.max(leftBB.getMaxX(),rightBB.getMaxX());
        double maxY = Double.max(leftBB.getMaxY(),rightBB.getMaxY());

        setBB(minX, minY, maxX, maxY);
    }

    void setBB(double minX, double minY, double maxX, double maxY) {
        double width = maxX-minX;
        double height = maxY-minY;
        bb = new BoundingBox(minX,minY,width,height);
    }

    //Returns the value where the node split the data
    // Needs vertical to figure out what exactly was split on
    public float getSplit() {
        return split;
    }

    //Returns a BoundingBox object representing the bounding box of all the elements in the node
    public BoundingBox getBB() {
        return bb;
    }

    //For testing
    public KDNode getNodeL() {
        return nodeL;
    }

    //For testing
    public KDNode getNodeR() {
        return nodeR;
    }

    public void setValues(List<BoundingBoxable> valueList) {
        values = new BoundingBoxable[valueList.size()];
        for(int i = 0 ; i < valueList.size() ; i++){
            values[i] = valueList.get(i);
        }

        //Create BoundingBox for the KDNode
        makeNodeBB();
    }

    public boolean isEmpty(){
        return values == null;
    }

}