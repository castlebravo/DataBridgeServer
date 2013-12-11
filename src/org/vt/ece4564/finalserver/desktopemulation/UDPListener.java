package org.vt.ece4564.finalserver.desktopemulation;


// class used to manage threaded runnables listening for incoming UDP datagrams
public class UDPListener
{
    private Thread thread;
    public static int port;
    private DataProcessor data_processor;
    
    
    public UDPListener(int port,  DataProcessor data_processor){
        this.data_processor = data_processor;
        UDPListener.port = port;
    }
    
    // starts the UDP listener
    public void openUDPPort(){
        thread = new Thread(new UDPRunnable(port, data_processor));
        thread.start();
    }
}
