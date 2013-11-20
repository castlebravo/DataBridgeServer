package org.vt.ece4564.finalserver.cli;

import org.vt.ece4564.finalserver.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CLI extends HttpServlet{
	
	String cmd;
	String output = " ";
	JSONArray jArray = new JSONArray();

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("application/json");
		
		try {
			jArray.write(resp.getWriter());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//resp.getWriter().write(jArray.toString());
	}
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		output = " ";

		cmd = req.getParameter("cmd");
		String update = sendCmd(cmd);
		
		if(output == " "){
			resp.getWriter().write(update);
		}else{
			resp.getWriter().write(output);
		}
		
		JSONObject jObj = new JSONObject();
		try {
			jObj.put("response", output);
			jObj.put("cmd", cmd);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resp.getWriter().write(e.getMessage());
		}
		
		jArray.put(jObj);
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
			
			if(OSname.contains("Windows")){
				proc = run.exec("cmd " + inputCmd);
			}else if (OSname.contains("Mac")){
				proc = run.exec("/usr/bin/open -a Terminal " + inputCmd);
			}else proc = run.exec("/usr/bin/xterm " + inputCmd);
			
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
