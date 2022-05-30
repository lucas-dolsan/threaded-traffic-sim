package model;

import java.io.File;

public class SimulationParameters {
  private static final String MESH_FILES_PATH = "./mesh/";

  private int carCount;
  private int carSpawnInterval;
  private int maxCarsOnMesh;
  private File meshFile;

  private File readMeshFile(String meshFileName) {
    return new File(MESH_FILES_PATH + meshFileName);
  }

  public SimulationParameters(int carCount, int carSpawnInterval, int maxCarsOnMesh, String meshFile) {
    this.carCount = carCount;
    this.carSpawnInterval = carSpawnInterval;
    this.maxCarsOnMesh = maxCarsOnMesh;
    this.meshFile = this.readMeshFile(meshFile);
  }

  public int getCarCount() {
    return carCount;
  }

  public int getCarSpawnInterval() {
    return carSpawnInterval;
  }

  public File getMeshFile() {
    return meshFile;
  }

  public int getMaxCarsOnMesh() {
    return maxCarsOnMesh;
  }
}
