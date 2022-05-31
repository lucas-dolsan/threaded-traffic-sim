package model;

import controller.MeshController;
import controller.Simulation;

import java.util.*;


public class Car extends Thread {
    private ArrayList<Road> route;
    private Road matrix[][];
    private Random random;
    private Road currentRoad;
    private MeshController meshController;
    private int speed;

    private Simulation simulation;

    public Car() {
        this.route = new ArrayList<>();
        this.meshController = MeshController.getInstance();
        this.matrix = meshController.getMatrix();

        this.currentRoad = null;
        this.random = new Random();
        this.speed = random.nextInt(100) + 500;
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }

    private void handleCrossroads() {
        try {
            sleep(this.speed);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        ArrayList<Road> crossroadsToAcquire = new ArrayList<>();
        ArrayList<Road> acquiredCrossroads = new ArrayList<>();


        for (int i = 0; i < this.route.size(); i++) {
            Road road = this.route.get(i);

            // take into consideration the next road cell right outside the crossroads
            crossroadsToAcquire.add(road);

            if (!road.isCrossRoadCell()) {
                break;
            }
        }

        for (Road crossroadToAcquire : crossroadsToAcquire) {
            if (crossroadToAcquire.tryAcquire()) {
                acquiredCrossroads.add(crossroadToAcquire);
            } else {
                // failed to acquire all the crossroads + the road right outside the crossroad
                for (Road acquiredRoad : acquiredCrossroads) {
                    acquiredRoad.release();
                }
                break;
            }
        }

        boolean acquiredAllNecessaryCrossroads = acquiredCrossroads.size() == crossroadsToAcquire.size();

        if (acquiredAllNecessaryCrossroads) {
            for (Road acquiredCrossroad : acquiredCrossroads) {
                this.route.remove(acquiredCrossroad);
                this.move(acquiredCrossroad, false);
            }
        }
    }

    private void move(Road nextRoad, Boolean needsAcquire) {
        if (nextRoad.isAvailable()) {

            Boolean acquired = false;

            if (needsAcquire) {
                do {
                    if (nextRoad.tryAcquire()) {
                        acquired = true;
                    }
                } while (!acquired);
            }

            nextRoad.addCar(this);

            Road previousRoad = this.getCurrentRoad();

            if (previousRoad != null) {
                previousRoad.removeCar();
                previousRoad.release();
            }

            this.setCurrentRoad(nextRoad);

            this.meshController.updateRoadMesh();

            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void run() {
        while (!route.isEmpty()) {
            int nextRoadIndex = 0;

            if (route.get(nextRoadIndex).isCrossRoadCell()) {
                handleCrossroads();
            } else {
                Road road = this.route.remove(nextRoadIndex);

                this.move(road, true);
            }
        }
        this.getCurrentRoad().removeCar();
        this.getCurrentRoad().release();
        this.simulation.removeRunningCar(this);
        this.meshController.updateRoadMesh();
    }

    public void defineRoute(Road startingRoad) throws Exception {
        // defineRoute will append road cells to the route list until the last one is an exit
        boolean foundExit = false;

        Road nextRoadOnRoute = startingRoad;

        route.add(nextRoadOnRoute);

        // this variable counts the amount of crossroad cells immediately on route, to prevent driving in circles
        // inside the crossroad
        int amountOfCrossroadsCellsOnCurrentCrossroad = 0;

        while (!foundExit) {
            int direction = nextRoadOnRoute.getDirection();
            int currentRouteX = nextRoadOnRoute.getX();
            int currentRouteY = nextRoadOnRoute.getY();

            boolean isRoad = direction <= 4;

            if (isRoad) {
                nextRoadOnRoute = this.chooseRoad(direction, currentRouteX, currentRouteY);
            } else {
                nextRoadOnRoute = this.chooseCrossroad(
                        direction,
                        currentRouteX,
                        currentRouteY,
                        amountOfCrossroadsCellsOnCurrentCrossroad
                );
                if (nextRoadOnRoute.isCrossRoadCell()) {
                    amountOfCrossroadsCellsOnCurrentCrossroad++;
                } else {
                    amountOfCrossroadsCellsOnCurrentCrossroad = 0;
                }
            }

            route.add(nextRoadOnRoute);

            foundExit = nextRoadOnRoute.isExitCell();
        }
    }

    public Road chooseRoad(int direction, int currentRouteX, int currentRouteY) throws Exception {
        switch (direction) {
            case 1:
                return this.matrix[currentRouteX][currentRouteY - 1];
            case 2:
                return this.matrix[currentRouteX + 1][currentRouteY];
            case 3:
                return this.matrix[currentRouteX][currentRouteY + 1];
            case 4:
                return this.matrix[currentRouteX - 1][currentRouteY];
            default:
                throw new Exception("Invalid road direction");
        }
    }

    private Road chooseCrossroad(
            int direction,
            int currentRouteX,
            int currentRouteY,
            int amountOfTimesDrivenInCrossroad
    )throws Exception {
        switch (direction) {
            case 5: {
                return this.matrix[currentRouteX][currentRouteY - 1];
            }
            case 6: {
                return this.matrix[currentRouteX + 1][currentRouteY];
            }
            case 7: {
                return this.matrix[currentRouteX][currentRouteY + 1];
            }
            case 8: {
                return this.matrix[currentRouteX - 1][currentRouteY];
            }
            case 9: {
                if (amountOfTimesDrivenInCrossroad == 3) {
                    return this.matrix[currentRouteX + 1][currentRouteY];
                } else {
                    int choice = random.nextInt(2);
                    if (choice == 0) {
                        return this.matrix[currentRouteX][currentRouteY - 1];
                    } else {
                        return this.matrix[currentRouteX + 1][currentRouteY];
                    }
                }
            }
            case 10: {
                if (amountOfTimesDrivenInCrossroad == 3) {
                    return this.matrix[currentRouteX][currentRouteY - 1];
                } else {
                    int choice = random.nextInt(2);
                    if (choice == 0) {
                        return this.matrix[currentRouteX][currentRouteY - 1];
                    } else {
                        return this.matrix[currentRouteX - 1][currentRouteY];
                    }
                }
            }
            case 11: {
                if (amountOfTimesDrivenInCrossroad == 3) {
                    return this.matrix[currentRouteX][currentRouteY + 1];
                } else {
                    int choice = random.nextInt(2);
                    if (choice == 0) {
                        return this.matrix[currentRouteX + 1][currentRouteY];
                    } else {
                        return this.matrix[currentRouteX][currentRouteY + 1];
                    }
                }
            }
            case 12: {
                if (amountOfTimesDrivenInCrossroad == 3) {
                    return this.matrix[currentRouteX - 1][currentRouteY];
                } else {
                    int choice = random.nextInt(2);
                    if (choice == 0) {
                        return this.matrix[currentRouteX][currentRouteY + 1];
                    } else {
                        return this.matrix[currentRouteX - 1][currentRouteY];
                    }
                }
            }
            default: {
                throw new Exception("Invalid road direction");
            }
        }
    }

    public Road getCurrentRoad() {
        return currentRoad;
    }

    public void setCurrentRoad(Road currentRoad) {
        this.currentRoad = currentRoad;
    }

}