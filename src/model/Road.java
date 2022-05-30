package model;

import java.util.concurrent.Semaphore;


public abstract class Road {

    protected Semaphore semaphore;
    protected String imagePath = "assets/road0.png";
    protected boolean isEntryCell;
    protected boolean isExitCell;
    protected int direction;
    protected Car car;
    protected int x;
    protected int y;


    public Road(int x, int y, int direction) {
        this.car = null;
        this.direction = direction;
        this.x = x;
        this.y = y;
        this.semaphore = new Semaphore(1);

        this.setCellImage();
    }

    public boolean isAvailable() {
        return this.car == null;
    }

    public abstract boolean tryAcquire();

    public abstract void release();

    public abstract void addCar(Car car);

    public abstract void removeCar();

    public boolean isCrossRoadCell() {
        return this.direction > 4;
    }

    public void setCellImage() {
        if(this.isCrossRoadCell()) {
            this.setCrossRoadImage();
        } else {
            this.setRoadImage();
        }
    }    

    public void setRoadImage() {
        this.imagePath = "assets/road" + this.direction + ".png";
    }

    public void setCrossRoadImage() {
        this.imagePath = "assets/crossroad.png";
    }

    public void setCarImage() {
        setImagePath("assets/car.png");
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public boolean isEntryCell() {
        return isEntryCell;
    }

    public void setIsEntryCell(boolean isEntryCell) {
        this.isEntryCell = isEntryCell;
    }

    public boolean isExitCell() {
        return isExitCell;
    }

    public void setIsExitCell(boolean isExitCell) {
        this.isExitCell = isExitCell;
    }

}
