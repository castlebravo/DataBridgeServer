package org.vt.ece4564.finalserver.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class CLI extends HttpServlet{
    String cmd;
    String output = " ";
    ArrayList<String> logs = new ArrayList<String>();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
		
        //auto reload every 5 seconds
        resp.setIntHeader("Refresh", 5);
		
        //outputs logs
        if(logs.size() > 0){
            for(int i = 0; i < logs.size();i++){
                resp.getWriter().write(logs.get(i) + "\n");
            }
        }
    }
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");
        output = " ";

        //run cmd on terminal
        cmd = req.getParameter("cmd");
        String update = sendCmd(cmd);
        logs.add(cmd);
		
        if(output == " "){
            resp.getWriter().write(update);
            logs.add(update);
        }else{
            resp.getWriter().write(output);
            logs.add(output);
        }
    }

    // this method sends the posted cmd to the computers CLI
    // checks for system information before "launching" the correct terminal
    protected String sendCmd(String command){
        try{
            String inputCmd;
            inputCmd = cmd;
            // store information about OS running so know which command to run
	    String OSname = System.getProperty("os.name");        
	    String OSver = System.getProperty("os.version");        
	    String OSarch = System.getProperty("os.arch");
			
            // connect runtime object to a cmd.exe system process
            Runtime run = Runtime.getRuntime();
            Process proc;
			
            //run different cli, depending on its system
            if(OSname.contains("Windows")){
                proc = run.exec("cmd /c " + inputCmd);
            }else if (OSname.contains("Mac")){
                proc = run.exec("/usr/bin/open -a Terminal " + inputCmd);
            }else{
                proc = run.exec("/usr/bin/xterm " + inputCmd);
            }
			
            InputStream in = proc.getInputStream();
            String line = "";
			
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
			
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
			
            output = sb.toString();
            in.close();
			
            // waitFor outside program to finish
            int end = proc.waitFor();
            return ("Exit Error: " + end);
        } catch(Exception e){
            e.printStackTrace();
            return (e.toString());
        }
    }
}