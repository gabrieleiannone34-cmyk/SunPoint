package it.unisa.sunpoint.control;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import org.json.JSONObject;

import it.unisa.sunpoint.dao.UtenteDAO;


@WebServlet("/VerificaEmailServlet")
public class VerificaEmailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        String email = request.getParameter("email");
        String risultato = "disponibile"; // default

        if (email != null && !email.trim().isEmpty()) {
            UtenteDAO utenteDAO = new UtenteDAO();
            try {
                boolean esiste = utenteDAO.checkEmailExists(email);
                if (esiste) {
                    risultato = "occupata";
                }
            } catch (SQLException e) {
                risultato = "errore";
            }
        }

        // 2. Creiamo l'oggetto JSON e lo stampiamo
        JSONObject json = new JSONObject();
        json.put("result", risultato);
        out.print(json.toString());
    }



	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
