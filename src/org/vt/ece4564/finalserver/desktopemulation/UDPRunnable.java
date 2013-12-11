package org.vt.ece4564.finalserver.desktopemulation;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import edu.vt.ece4564.finalproject.databridge.SensorData;


// runnable used to receive incoming UDP datagram packets
public class UDPRunnable implements Runnable
{
    private int port_;
    private DataProcessor data_processor;
    
    
    public UDPRunnable(int p, DataProcessor data_processor){
        port_ = p;
        this.data_processor = data_processor;
    }
    
    @Override
    public void run(){
        try{
            DatagramSocket serverSocket = new DatagramSocket(port_);
            byte[] receiveData = new byte[1028];
            
            // get sensor data fromthe app
            DatagramPacket receivePacket = 
                        new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            
            // handle incoming data
            ObjectInputStream ois_ = new ObjectInputStream(new ByteArrayInputStream(receiveData));
            SensorData received = (SensorData)ois_.readObject();
            ois_.close();
            
            // processes the newly received sensor data
            data_processor.addSensorData(received);
            
            // continually listens for incoming packets
            while(true){
                receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);    // is blocking
                
                // converts the bytes to the SensorData object
                ois_ = new ObjectInputStream(new ByteArrayInputStream(receiveData));
                received = (SensorData)ois_.readObject();
                ois_.close();
            
                data_processor.addSensorData(received);
            }  
        }catch(Exception e){
            System.err.println("ERROR in UDPRunnable: " + e.toString());
            System.exit(1);
        }
    }    
}
