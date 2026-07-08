package it.unisa.sunpoint.control;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import it.unisa.sunpoint.model.Utente;
import it.unisa.sunpoint.dao.UtenteDAO;

//@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	// Mostra la pagina di login (nascosta nella cartella WEB-INF)
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/login.jsp");
        dispatcher.forward(request, response);
	}

	// Riceve i dati dal form ed effettua il controllo
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		UtenteDAO utenteDAO = new UtenteDAO();
		
	try {
		Utente utente = utenteDAO.doRetrieveByEmailAndPassword(email, password);
		
		if(utente != null) {
			//Login riuscito, creiamo la sessione HTTP e salviamo l'intero oggetto Utente
			HttpSession session = request.getSession();
			session.setAttribute("utenteLoggato", utente);
			
			// Reindirizziamo l'utente alla home page
			response.sendRedirect(request.getContextPath() + "/index.jsp");
		} else {
			// Login fallito
			request.setAttribute("errore", "Email o password non validi. Riprova.");
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/login.jsp");
			dispatcher.forward(request, response);
		}
	} catch(SQLException e) {
		e.printStackTrace();
		request.setAttribute("errore", "Errore di connessione al database.");
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/login.jsp");
        dispatcher.forward(request, response);
			
		}
	}

}
