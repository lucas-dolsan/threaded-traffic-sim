package model;

import java.util.concurrent.TimeUnit;

public class SemaphoreRoad extends Road {
    public SemaphoreRoad(int x, int y, int direction) {
        super(x, y, direction);
    }

    @Override
    public boolean tryAcquire() {
        boolean acquired = false;
        try {
            acquired = super.semaphore.tryAcquire(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return acquired;
    }

    @Override
    public void release() {
        super.semaphore.release();
    }

    public void addCar(Car car) {
        super.car = car;
        setCarImage();
    }

    public void removeCar() {
        super.car = null;
        this.setCellImage();
    }
}
