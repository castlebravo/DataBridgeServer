package org.vt.ece4564.finalserver;

import java.util.ArrayList;
import javax.swing.JTextArea;


public class ServerLog
{
    private ArrayList<String> messages_;
    private JTextArea gui_log_;
    
    public ServerLog(JTextArea gui_log){
        messages_ = new ArrayList<String>();
        gui_log_ = gui_log;
    }
    
    public void addServerMessage(String msg){
        messages_.add(msg);
        gui_log_.append(msg + "\n\n");
    }
}
