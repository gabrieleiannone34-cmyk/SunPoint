package it.unisa.sunpoint.control;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class SvuotaCarrelloServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Recuperiamo la sessione dell'utente
		HttpSession session = request.getSession();
		
		// Rimuoviamo SOLO l'attributo "carrello".
		// L'utente rimane loggato 
		session.removeAttribute("carrello");
		
		//Rimandiamo l'utente alla pagina del carello (che ora risulterà vuoto)
		response.sendRedirect(request.getContextPath() + "/carrello.jsp");
}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
