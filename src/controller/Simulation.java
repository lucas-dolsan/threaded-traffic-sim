package controller;

import model.Car;
import model.Road;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import static java.lang.Thread.sleep;


public class Simulation extends Thread {
    Queue<Car> carQueue;
    private MeshController meshController;
    private Road[][] matrix;

    private ArrayList<Car> runningCars;

    public Simulation(MeshController meshController) {
        this.meshController = meshController;
        this.carQueue = new LinkedList<>();
        this.runningCars = new ArrayList<>();
    }

    public void run() {
        this.carQueue = this.loadCars();
        this.matrix = this.meshController.getMatrix();

        this.meshController.updateRoadMesh();

        while(!carQueue.isEmpty()) {
            for (int y = 0; y < this.meshController.getLines(); y++) {
                for (int x = 0; x < this.meshController.getColumns(); x++) {

                    Road startingRoad = matrix[x][y];

                    if (startingRoad.isEntryCell() && startingRoad.isAvailable() && !carQueue.isEmpty()) {

                        try {
                            sleep(this.meshController.getSimulationParameters().getCarSpawnInterval() * 1000);

                            Car car = carQueue.remove();
                            car.defineRoute(startingRoad);
                            car.start();

                            this.runningCars.add(car);

                            System.out.println("running cars: " + runningCars.size());

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
