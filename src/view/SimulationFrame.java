package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import controller.MeshController;
import model.SimulationParameters;


public class SimulationFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private MeshController meshController;
    private RoadMeshPanel roadMeshPanel;

    public SimulationFrame(SimulationParameters simulationParameters) {
        super.setTitle("SimulationFrame");

        this.meshController = MeshController.getInstance();

        this.meshController.setSimulationFrame(this);
        this.meshController.setSimulationParameters(simulationParameters);

        super.setPreferredSize(new Dimension(1000, 800));
        super.setFocusable(true);
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
        super.getContentPane().setBackground(Color.WHITE);
        super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        initComponents();
        super.setVisible(true);

        start();
    }

    private void initComponents() {
        this.roadMeshPanel = new RoadMeshPanel(this.meshController.getSimulationParameters().getMeshFile());
        JButton buttonStopSimulation = new JButton();
        buttonStopSimulation.setText("Stop simulation");
        buttonStopSimulation.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });

        this.roadMeshPanel.add(buttonStopSimulation);
        this.add(this.roadMeshPanel);
    }

    public RoadMeshPanel getRoadMeshPanel() {
        return roadMeshPanel;
    }

    private void start() {
        super.pack();
        super.setLocationRelativeTo(null);
        this.meshController.runSimulation();
    }
}
