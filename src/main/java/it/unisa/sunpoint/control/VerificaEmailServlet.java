package it.unisa.sunpoint.control;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import it.unisa.sunpoint.dao.UtenteDAO;


@WebServlet("/VerificaEmailServlet")
public class VerificaEmailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Catturiamo l'email digitata dall'utente
		String email = request.getParameter("email");
		
		// Diciamo al browser che risponderemo con del semplice testo (non una pagina HTML intera!)
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		
		if(email != null && !email.trim().isEmpty()) {
			UtenteDAO utenteDAO = new UtenteDAO();
			try {
				boolean esiste = utenteDAO.checkEmailExists(email);
			
				// Rispondiamo in modo asincrono con una semplice parola chiave
				if (esiste) {
					response.getWriter().write("occupata");
				} else {
					response.getWriter().write("disponibile");
				}
				
			} catch (SQLException e) {
				response.getWriter().write("errore");
			}
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
