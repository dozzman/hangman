package hangman;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Hangman extends HttpServlet {
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    out.println("<title>Testinggggg</title>" +
                "<body bgcolor=0000FF>");
    out.println("<h1>Hello, World!</h1>");
                
    out.close();
    }
}
