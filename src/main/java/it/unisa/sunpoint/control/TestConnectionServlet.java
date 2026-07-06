package it.unisa.sunpoint.control;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class TestConnectionServlet
 */
@WebServlet("/TestConnection")
public class TestConnectionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
        
        try {
            // 1. Ottiene il contesto JNDI
            Context initContext = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:comp/env");
            
            // 2. Ottiene il DataSource (il nome deve corrispondere a quello nel web.xml e context.xml)
            DataSource ds = (DataSource)envContext.lookup("jdbc/sunpoint");
            
            // 3. Prova a ottenere la connessione
            Connection con = ds.getConnection();
            
            if (con != null) {
                out.println("<h1>Connessione al database riuscita!</h1>");
                con.close(); // Chiudi subito la connessione
            }
        } catch (NamingException | SQLException e) {
            out.println("<h1>Errore di connessione:</h1>");
            out.println("<p>" + e.getMessage() + "</p>");
            e.printStackTrace();
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
