package controller;

import model.Road;
import model.SemaphoreRoad;
import model.SimulationParameters;
import view.SimulationFrame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class MeshController {

    private static MeshController instance;
    private SimulationFrame simulationFrame;
    private Road matrix[][];
    private File file = null;
    private Simulation simulation;
    private int lines;
    private int columns;
    private SimulationParameters simulationParameters;

    public static synchronized MeshController getInstance() {
        if (instance == null) {
            instance = new MeshController();
        }

        return instance;
    }

    public void setSimulationFrame(SimulationFrame simulationFrame) {
        this.simulationFrame = simulationFrame;
    }

    private MeshController() {}

    public void createMatrixFromMeshFile(File meshFile) {
        Scanner meshScanner = null;
        try {

            meshScanner = new Scanner(meshFile);

            while (meshScanner.hasNextInt()) {
                this.lines = meshScanner.nextInt();
                this.columns = meshScanner.nextInt();

                this.matrix = new Road[this.columns][this.lines];

                for (int y = 0; y < this.lines; y++) {
                    for (int x = 0; x < this.columns; x++) {
                        int direction = meshScanner.nextInt();
                        SemaphoreRoad cell = new SemaphoreRoad(x, y, direction);
                        if (this.checkIfIsRoad(cell)) {
                            this.defineEntriesAndExits(cell);
                        }
                        this.matrix[x][y] = cell;
                    }
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            meshScanner.close();
        }
    }

    public boolean checkIfIsRoad(SemaphoreRoad cell) {
        return cell.getDirection() > 0;
    }

    public void runSimulation() {
        this.simulation = new Simulation(this);
        this.simulation.start();
    }


    public void defineEntriesAndExits(Road cell) {
        boolean isEntryCell = (
                this.isTopEntryCell(cell) ||
                        this.isBottomEntryCell(cell) ||
                        this.isLeftEntryCell(cell) ||
                        this.isRightEntryCell(cell)
        );

        cell.setIsEntryCell(isEntryCell);

        boolean isExitCell = (
                this.isTopExitCell(cell) ||
                        this.isLeftExitCell(cell) ||
                        this.isRightExitCell(cell) ||
                        this.isBottomExitCell(cell)
        );

        cell.setIsExitCell(isExitCell);
    }

    public boolean isTopEntryCell(Road cell) {
        return cell.getY() - 1 < 0 && cell.getDirection() == 3;

    }

    public void updateRoadMesh() {
        this.simulationFrame.getRoadMeshPanel().updateUI();
    }

    public boolean isTopExitCell(Road cell) {
        return cell.getY() - 1 < 0 && cell.getDirection() == 1;
    }

    public boolean isBottomEntryCell(Road cell) {
        return cell.getY() + 1 >= this.lines && cell.getDirection() == 1;
    }

    public boolean isBottomExitCell(Road cell) {
        return cell.getY() + 1 >= this.lines && cell.getDirection() == 3;
    }

    public boolean isLeftEntryCell(Road cell) {
        return cell.getX() - 1 < 0 && cell.getDirection() == 2;
    }

    public boolean isLeftExitCell(Road cell) {
        return cell.getX() - 1 < 0 && cell.getDirection() == 4;
    }

    public boolean isRightExitCell(Road cell) {
        return cell.getX() + 1 >= this.columns && cell.getDirection() == 2;
    }

    public boolean isRightEntryCell(Road cell) {
        return cell.getX() + 1 >= this.columns && cell.getDirection() == 4;
    }

    public String getMatrixPosition(int rowIndex, int columnIndex) {
        return matrix[columnIndex][rowIndex].getImagePath();
    }

    public File getFile() {
        return file;
    }

    public int getLines() {
        return lines;
    }

    public int getColumns() {
        return columns;
    }


    public void setPathName(File file) {
        this.file = file;
    }


    public SimulationParameters getSimulationParameters() {
        return this.simulationParameters;
    }

    public void setSimulationParameters(SimulationParameters simulationParameters) {
        this.simulationParameters = simulationParameters;
    }

    public Road[][] getMatrix() {
        return this.matrix;
    }

}
