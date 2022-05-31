package view;

import controller.MeshController;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.io.File;

public class RoadMeshPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    class RoadTableModel extends AbstractTableModel {
        private static final long serialVersionUID = 1L;

        @Override
        public int getRowCount() {
            return meshController.getLines();
        }

        @Override
        public int getColumnCount() {
            return meshController.getColumns();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return new ImageIcon(meshController.getMatrixPosition(rowIndex, columnIndex));
        }
    }

    private MeshController meshController;
    private JTable roadMeshTable;

    public RoadMeshPanel(File meshFile) {
        this.meshController = MeshController.getInstance();
        this.meshController.createMatrixFromMeshFile(meshFile);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setOpaque(false);
        this.initComponents();
    }

    private void initComponents() {
        roadMeshTable = new JTable();
        roadMeshTable.setModel(new RoadTableModel());

        roadMeshTable.setPreferredScrollableViewportSize(roadMeshTable.getPreferredSize());
        roadMeshTable.setFillsViewportHeight(true);

        roadMeshTable.setRowHeight(25);
        roadMeshTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        roadMeshTable.setIntercellSpacing(new Dimension(0, 0));
        roadMeshTable.setDefaultRenderer(Object.class, new RoadMeshItemRender());
        this.add(roadMeshTable);
    }


    public int getWidth() {
        return (int) roadMeshTable.getMaximumSize().getWidth();
    }

}
