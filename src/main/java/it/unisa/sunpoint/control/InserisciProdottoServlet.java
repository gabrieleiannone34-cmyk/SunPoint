package it.unisa.sunpoint.control;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import it.unisa.sunpoint.model.Utente;


@WebServlet("/InserisciProdottoServlet")
public class InserisciProdottoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	//Metodo GET: Mostra la pagina col form all'amministratore
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Utente utente = (Utente) session.getAttribute("utenteLoggato");
		
		// CONTROLLO DI SICUREZZA: Sei loggato? E sei un admin?
		if (utente == null || !"admin".equals(utente.getRole())) {
			// Se non sei admin, ti caccio via
			response.sendRedirect(request.getContextPath() + "/index.jsp");
			return;
		}
		
		// Se sei admin, ti apro la porta del form nascosto
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/inserisciProdotto.jsp");
		dispatcher.forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
