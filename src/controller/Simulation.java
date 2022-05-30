package controller;

import model.Car;
import model.Road;
import model.SimulationParameters;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import static java.lang.Thread.sleep;

public class Simulation extends Thread {
    Queue<Car> carQueue;
    private MeshController meshController;
    private Road[][] matrix;
    private Boolean pauseSimulation = false;
    private Boolean endSimulation = false;

    private ArrayList<Car> runningCars;

    public Simulation(MeshController meshController) {
        this.meshController = meshController;
        this.carQueue = new LinkedList<>();
        this.runningCars = new ArrayList<>();
    }

    public Boolean hasEnded() {
        return this.endSimulation == true;
    }

    public void setEndSimulation(Boolean endSimulation) {
        this.endSimulation = endSimulation;
    }

    public void setPauseSimulation(Boolean pauseSimulation) {
        this.pauseSimulation = pauseSimulation;
    }
    public void addRunningCar(Car car) {
        this.runningCars.add(car);
    }

    public void removeRunningCar(Car car) {
        this.runningCars.remove(car);
        this.meshController.getRunningCarsCountLabel().setText("Running cars: " + this.runningCars.size());
    }

    public void run() {
        this.carQueue = this.loadCars();

        this.meshController.getCarsOnQueueLabel().setText("Cars on queue: " + this.carQueue.size());

        this.matrix = this.meshController.getMatrix();

        this.meshController.updateRoadMesh();

        SimulationParameters params = this.meshController.getSimulationParameters();

        while(!carQueue.isEmpty()) {
            for (int y = 0; y < this.meshController.getLines(); y++) {
                for (int x = 0; x < this.meshController.getColumns(); x++) {

                    Road startingRoad = matrix[x][y];

                    Boolean canAddCarsToSimulation = (
                        startingRoad.isEntryCell() &&
                        startingRoad.isAvailable() &&
                        !carQueue.isEmpty() &&
                        !this.pauseSimulation &&
                        !this.endSimulation &&
                        this.runningCars.size() < params.getMaxCarsOnMesh()
                    );

                    if (canAddCarsToSimulation) {
                        try {
                            sleep(params.getCarSpawnInterval() * 1000);

                            Car car = carQueue.remove();

                            this.meshController.getCarsOnQueueLabel().setText("Cars on queue: " + this.carQueue.size());

                            car.defineRoute(startingRoad);
                            car.start();

                            car.setSimulation(this);

                            this.addRunningCar(car);
                            this.meshController.getRunningCarsCountLabel().setText("Running cars: " + this.runningCars.size());

                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }

    public Queue<Car> loadCars() {
        Queue<Car> cars = new LinkedList<>();
        for (int i = 0; i < this.meshController.getSimulationParameters().getCarCount(); i++) {
            cars.add(new Car());
        }
        return cars;
    }
}
