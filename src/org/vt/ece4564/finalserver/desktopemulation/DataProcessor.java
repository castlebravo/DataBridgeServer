package org.vt.ece4564.finalserver.desktopemulation;


import java.util.ArrayList;
import javax.swing.JTable;
import edu.vt.ece4564.finalproject.databridge.SensorData;


public class DataProcessor
{
    private DesktopControl ctrl;
    
    private ArrayList<SensorData> sensor_data;
    private int MAX_DATA_POINTS = 200;
    
    private JTable CtrlDebugTable;
    
    //private double avg_acl_x = 0, avg_acl_y = 0, avg_acl_z = 0;
    
    private int count = 0;
    
    public DataProcessor(JTable CtrlDebugTable){
        this.CtrlDebugTable = CtrlDebugTable;
        
        sensor_data = new ArrayList<SensorData>();
        ctrl = new DesktopControl();
        
        setTableString(0, 0, "Acceleration");
        setTableString(0, 1, "Rotation");
        setTableString(0, 2, "Control Action");
        setTableString(0, 3, "Mouse Position");
    }
    
    public void addSensorData(SensorData sd){
        if(sensor_data.size() >= MAX_DATA_POINTS)
            sensor_data.remove(0);
        sensor_data.add(sd);
        updateDebugTable(sd);
        processData(sd);
    }
    
    public String getFormattedData(){
        String toReturn = 
        "google.visualization.Query.setResponse(\n" +
        "  {'version':'0.6','reqId':'0','status':'ok',\n" +
        "    'table':{\n" +
        "     'cols':[{'id':'time','label':'Time','type':'number'}, \n" +
               buildChartColumns("a", "component") + ",\n" +
               buildChartColumns("g", "component") + ",\n" +
               buildChartColumns("m", "component") + "],\n" +
        "     'rows':[";
        
        // gets a static array representing the sensor data array list and copy
        //     the data from each object into the correct string form
        Object[] data = sensor_data.toArray();
        for(int i = 0; i < data.length; ++i){
            SensorData sd = (SensorData)data[i];
            toReturn += "{'c':[{'v':" + sd.getMs_time() + "},{'v':" + 
                  sd.getAcl_x() + "},{'v':" + sd.getAcl_y() + "},{'v':" + 
                  sd.getAcl_z() + "},{'v':" + sd.getGyro_x() + "},{'v':" + 
                  sd.getGyro_y() + "},{'v':" + sd.getGyro_z() + "},{'v':" + 
                  sd.getMag_x() + "},{'v':" + sd.getMag_y() + "},{'v':" + 
                  sd.getMag_z() + "}]}";
            if(i < data.length - 1)
                toReturn += ",";
        }
        toReturn += 
        "    ]\n" +
        "  }\n" +
        "});";
        return toReturn;        
    }
    
    public void clearData(){
        sensor_data.clear();
    }
    
    
    
    //========================================================================//
    //============================Helper Functions============================//
    
    private void setTableValue(int col, int row, double val){
        CtrlDebugTable.setValueAt(val, row, col);
    }
    
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
        //avg_acl_x = (avg_acl_x + sd.getAcl_x())/2.0;
        //avg_acl_y = (avg_acl_y + sd.getAcl_y())/2.0;
        //avg_acl_z = (avg_acl_z + sd.getAcl_z())/2.0;
        
        // row col
        CtrlDebugTable.setValueAt(sd.getAcl_x(), 0, 1);
        CtrlDebugTable.setValueAt(sd.getAcl_y(), 0, 2);
        CtrlDebugTable.setValueAt(sd.getAcl_z(), 0, 3);
        
        CtrlDebugTable.setValueAt(sd.getGyro_x(), 1, 1);
        CtrlDebugTable.setValueAt(sd.getGyro_y(), 1, 2);
        CtrlDebugTable.setValueAt(sd.getGyro_z(), 1, 3);
    }
    
    
    private String buildChartColumns(String postfix, String label){
        return
    "             {'id':'x" + postfix + "','label':'x " + label + "','type':'number'},\n" +
    "             {'id':'y" + postfix + "','label':'y " + label + "','type':'number'},\n" +
    "             {'id':'z" + postfix + "','label':'z " + label + "','type':'number'}";
    }
}
