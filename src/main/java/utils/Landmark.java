package utils;

import java.io.Serializable;

public class Landmark implements Serializable {
    public double x, y;
    public String landmarkName;
    public boolean hist;

    public Landmark(double x, double y, String landmarkName, boolean hist) {
        this.x = x;
        this.y = y;
        this.landmarkName = landmarkName;
        this.hist = hist;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getLandmarkName() {
        return landmarkName;
    }

    public void setLandmarkName(String landmarkName) {
        this.landmarkName = landmarkName;
    }

    public boolean isHist() {
        return hist;
    }

    public void setHist(boolean hist) {
        this.hist = hist;
    }

    @Override
    public String toString() {
        return x + ", " + y + ", " + landmarkName;
    }
}