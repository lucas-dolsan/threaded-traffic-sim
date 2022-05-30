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

        JLabel runningCarsCountLabel = new JLabel();
        runningCarsCountLabel.setText("Running cars: 0");

        JLabel carsOnQueueLabel = new JLabel();
        carsOnQueueLabel.setText("cars on queue: 0");


        this.meshController.setRunningCarsCountLabel(runningCarsCountLabel);
        this.meshController.setCarsOnQueueLabel(carsOnQueueLabel);

        JLabel isPausedLabel = new JLabel();
        isPausedLabel.setText("PLAYING...");

        JButton unpauseSimulationButton = new JButton();

        unpauseSimulationButton.setText("Unpause simulation");
        unpauseSimulationButton.addActionListener((ActionEvent e) -> {
            if(!this.meshController.getSimulation().hasEnded()) {
                this.meshController.unpauseSimulation();
                isPausedLabel.setText("PLAYING...");
            }
        });


        JButton pauseSimulationButton = new JButton();
        pauseSimulationButton.setText("Pause simulation");
        pauseSimulationButton.addActionListener((ActionEvent e) -> {
            if(!this.meshController.getSimulation().hasEnded()) {
                this.meshController.pauseSimulation();
                isPausedLabel.setText("PAUSED");
            }
        });

        JButton endSimulationButton = new JButton();
        endSimulationButton.setText("End simulation");
        endSimulationButton.addActionListener((ActionEvent e) -> {
            isPausedLabel.setText("ENDED");
            this.meshController.endSimulation();
        });

        JButton interruptSimulationButton = new JButton();
        interruptSimulationButton.setText("Interrupt simulation");
        interruptSimulationButton.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });

        this.roadMeshPanel.add(carsOnQueueLabel);
        this.roadMeshPanel.add(runningCarsCountLabel);
        this.roadMeshPanel.add(isPausedLabel);

        JFrame buttonFrame = new JFrame();
        JPanel buttonPanel = new JPanel();

        buttonPanel.add(endSimulationButton);
        buttonPanel.add(pauseSimulationButton);
        buttonPanel.add(unpauseSimulationButton);
        buttonPanel.add(interruptSimulationButton);

        buttonFrame.setPreferredSize(new Dimension(500, 100));
        buttonPanel.setPreferredSize(new Dimension(100, 100));

        buttonFrame.add(buttonPanel);
        buttonFrame.pack();
        buttonFrame.setVisible(true);

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
