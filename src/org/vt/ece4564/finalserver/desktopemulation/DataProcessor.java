package org.vt.ece4564.finalserver.desktopemulation;

import java.util.ArrayList;
import javax.swing.JTable;
import edu.vt.ece4564.finalproject.databridge.SensorData;


// class used to process incoming sensor data and control the desktop with it
public class DataProcessor
{
    private DesktopControl ctrl;
    private ArrayList<SensorData> sensor_data;
    private int MAX_DATA_POINTS = 200;
    private JTable CtrlDebugTable;
    private int count = 0;
    
    public DataProcessor(JTable CtrlDebugTable, DesktopControl dc){
        this.CtrlDebugTable = CtrlDebugTable;
        this.ctrl = dc;
        
        sensor_data = new ArrayList<SensorData>();
        setTableString(0, 0, "Acceleration");
        setTableString(0, 1, "Rotation");
        setTableString(0, 2, "Control Action");
        setTableString(0, 3, "Mouse Position");
    }
    
    public void addSensorData(SensorData sd){
        if(sensor_data.size() >= MAX_DATA_POINTS){
            sensor_data.remove(0);
        }
        sensor_data.add(sd);
        updateDebugTable(sd);
        processData(sd);
    }
    
    
    //========================================================================//
    //============================Helper Functions============================//   
    private void setTableString(int col, int row, String val){
        CtrlDebugTable.setValueAt(val, row, col);
    }
    
    private void processData(SensorData sd){
        
        if(sd.getAcl_x() >= 2)
            CtrlDebugTable.setValueAt(1, 2, 1);
        else if(sd.getAcl_x() <= -2)
            CtrlDebugTable.setValueAt(-1, 2, 1);
        else
            CtrlDebugTable.setValueAt(0, 2, 1);
        
        if(sd.getAcl_y() >= 2)
            CtrlDebugTable.setValueAt(1, 2, 2);
        else if(sd.getAcl_y() <= -2)
            CtrlDebugTable.setValueAt(-1, 2, 2);
        else
            CtrlDebugTable.setValueAt(0, 2, 2);
        
        
        double[] mouse = ctrl.getMouseXY();
        CtrlDebugTable.setValueAt(mouse[0], 3, 1);
        CtrlDebugTable.setValueAt(mouse[1], 3, 2);
        
        ctrl.doMouseMovement(sd);
        
        CtrlDebugTable.setValueAt(count, 3, 3);
        count++;
    }
   
    private void updateDebugTable(SensorData sd){
        // row col
        CtrlDebugTable.setValueAt(sd.getAcl_x(), 0, 1);
        CtrlDebugTable.setValueAt(sd.getAcl_y(), 0, 2);
        CtrlDebugTable.setValueAt(sd.getAcl_z(), 0, 3);
        
        CtrlDebugTable.setValueAt(sd.getGyro_x(), 1, 1);
        CtrlDebugTable.setValueAt(sd.getGyro_y(), 1, 2);
        CtrlDebugTable.setValueAt(sd.getGyro_z(), 1, 3);
    }
}
