package org.vt.ece4564.finalserver.desktopemulation;


import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Desktop extends HttpServlet{
    
	public Desktop(){
            
        }
        
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		//add stuff
		resp.setContentType("text/plain");
		resp.getWriter().write("Desktop");
	}
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		//add stuff
	}
}
