package org.vt.ece4564.finalserver;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;


public class PrimaryServlet extends HttpServlet
{
    JSONArray jArray = new JSONArray();
    
    public PrimaryServlet(){}
    
    
    @Override
    protected void doGet(HttpServletRequest req, 
        HttpServletResponse resp) throws ServletException, IOException{
        
        GUI.log_.addServerMessage(generateConnectionString(req));

        resp.setContentType("text/plain");
	resp.getWriter().write("please look for cli, desktop or filebrowser");
    }

    
    @Override
    protected void doPost(HttpServletRequest req,
    HttpServletResponse resp) throws ServletException, IOException{
        
        GUI.log_.addServerMessage(generateConnectionString(req));

        resp.setContentType("text/plain");
	resp.getWriter().write("please look for cli, desktop or filebrowser");
    }
    
    
    
    //========================================================================//
    //============================Helper Functions============================//
    private String generateConnectionString(HttpServletRequest req){
        return  "Primary Servlet Connection:\nSession Creation Time: " +
                req.getSession().getCreationTime() + "\nAuth Type: " + 
                req.getAuthType() + "\nContent Type: " + req.getContentType() +
                "\nContext Path: " + req.getContextPath() + "\nLocal Addr: " +
                req.getLocalAddr() + "\nLocal Name: " + req.getLocalName() + 
                "\nMethod: " + req.getMethod() + "\nPath Info: " + req.getPathInfo() +
                "\nPath Translated: " + req.getPathTranslated() + "\nProtocol: " +
                req.getProtocol() + "\nQuery String: " + req.getQueryString() +
                "\nRemote Address: " + req.getRemoteAddr() + "\nRemote Host: " +
                req.getRemoteHost() + "\nRemote User: " + req.getRemoteUser() +
                "\nRequest URI: " + req.getRequestURI() + "\nRequest Session ID: " +
                req.getRequestedSessionId() + "\n";
    }
}
