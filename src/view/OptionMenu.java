package view;

import javax.swing.*;

import model.SimulationParameters;

import java.awt.*;
import java.awt.event.ActionEvent;

public class OptionMenu extends JFrame {
    private JLabel carCountLabel;
    private JTextField carCountField;
    private JTextField carSpawnIntervalField;
    private JButton confirmButton;
    private JPanel mainPanel;
    private JRadioButton mesh1RadioButton;
    private JRadioButton mesh2RadioButton;
    private JRadioButton mesh3RadioButton;

    public OptionMenu() {
        super("OptionMenu");
        super.setPreferredSize(new Dimension(800, 400));
        super.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(this.mainPanel);
        this.setLocationRelativeTo(null);
        this.pack();
        this.addListeners();
    }

    public String chooseMeshFile() {
        if(this.mesh1RadioButton.isSelected()) {
            return "mesh1.txt";
        }
        if(this.mesh2RadioButton.isSelected()) {
            return "mesh2.txt";
        }

        if(this.mesh3RadioButton.isSelected()) {
            return "mesh3.txt";
        }
        System.out.println("choosing mesh1 as default");
        return "mesh1.txt";
    }

    private void addListeners() {
        confirmButton.addActionListener((ActionEvent e) -> {
            String carCount = carCountField.getText();
            String carSpawnInterval = carSpawnIntervalField.getText();
            String meshFile = this.chooseMeshFile();

            SimulationParameters params = new SimulationParameters(
                Integer.parseInt(carCount),
                Integer.parseInt(carSpawnInterval),
                meshFile
            );

            new SimulationFrame(params);
        });
    }
}
