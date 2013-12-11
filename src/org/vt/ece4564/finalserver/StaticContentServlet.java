package org.vt.ece4564.finalserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


// basic servlet used to open and serve static content from within the
//     jar, eliminating the need for a war folder and directory structure
public class StaticContentServlet extends HttpServlet
{
    private static final String failed_to_load_resource = 
            "Error: Failed to load the html resource!";
    private String path;
    private String html;
    
    
    public StaticContentServlet(String s){
        path = s;
        loadHtmlResource();
    }
    
    @Override
    protected void doGet(HttpServletRequest req, 
        HttpServletResponse resp) throws ServletException, IOException{
        
        resp.setContentType("text/html");
	resp.getWriter().write(html);
    }

    @Override
    protected void doPost(HttpServletRequest req,
    HttpServletResponse resp) throws ServletException, IOException{
        
        resp.setContentType("text/html");
	resp.getWriter().write(html);
    }
    
    
    
    //========================================================================//
    //============================Helper Functions============================//
    private void loadHtmlResource(){
        String line;
        html = "";
        
        InputStream stream = StaticContentServlet.class.getResourceAsStream(path);
        
        if(stream != null){
            BufferedReader rd = new BufferedReader(new InputStreamReader(stream));
            try{
                while((line = rd.readLine()) != null){
                    html += line;
                }
                rd.close();    
            }catch(Exception e){
                System.err.println(e.toString());
            }
        }else{
            html = failed_to_load_resource;
        }
    }
}
