package org.vt.ece4564.finalserver.chat;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;



public class Chat extends HttpServlet{
	ArrayList<String> messages = new ArrayList<String>();
	ArrayList<String> serverMessages = new ArrayList<String>();
	int index = 0;

	//doGet will handle anyone connecting to our chat server with a HTTP GET
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            resp.setContentType("text/html");
            PrintWriter out = resp.getWriter();
		
            //if no query string, it's from the server
            //That means we want ALL messages
            if(req.getQueryString()==null){
                //auto reload every 1 second
		resp.setIntHeader("Refresh", 1);
			
		//only execute if there are messages
		if(messages.size() > 0){
                    for(int i = 0; i < messages.size();i++){
                        out.write(messages.get(i) + "<br>");
                    }
		}
			
            }
            //we only want the messages we haven't received yet
            //but only messages from the server
            else{
            	//only execute if there are server messages
            	if(serverMessages.size() > 0){
                    while(index<serverMessages.size()){		
                        out.write(serverMessages.get(index)+"\n");
			index++;	
                    }
		}	
            }
	}

	//doPost will handle all request coming to the chat server using HTTP POST
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
	    resp.setContentType("text/html");
	    PrintWriter out = resp.getWriter();
		
            String msg = req.getParameter("newMessage");
            String sender = req.getParameter("sender");
            //if sender is null, server is sending us this message
            if(sender == null){
            	serverMessages.add(msg);
            }
            //always want to add it to the messages, regardless of sender
            messages.add(msg);
		
            //Server form post
	    out.println("<form method=\"post\" action=\"/chat\">");
	    out.println("<input type=\"text\" name=\"newMessage\"/>");
	    out.println("<input type=\"submit\" value=\"send\"/>");
	    out.println("</form>");
	}
}