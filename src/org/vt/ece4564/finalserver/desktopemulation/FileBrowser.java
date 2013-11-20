package org.vt.ece4564.finalserver.desktopemulation;

import org.vt.ece4564.finalserver.*;
import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class FileBrowser extends HttpServlet{
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		//add stuff
		resp.setContentType("text/plain");
		resp.getWriter().write("File Browser");
	}
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		//add stuff
	}
}
