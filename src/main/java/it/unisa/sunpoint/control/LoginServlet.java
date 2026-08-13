package it.unisa.sunpoint.control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import it.unisa.sunpoint.model.ItemCarrello;
import it.unisa.sunpoint.model.Prodotto;
import it.unisa.sunpoint.model.Utente;
import it.unisa.sunpoint.dao.CarrelloDAO;
import it.unisa.sunpoint.dao.UtenteDAO;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/login.jsp");
        dispatcher.forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		UtenteDAO utenteDAO = new UtenteDAO();
		
	try {
		Utente utente = utenteDAO.doRetrieveByEmailAndPassword(email, password);
		
		if(utente != null) {
			
			HttpSession session = request.getSession();
			session.setAttribute("utenteLoggato", utente);
			
			CarrelloDAO carrelloDAO = new CarrelloDAO();
			try {
			    
			    List<ItemCarrello> carrelloSalvato = carrelloDAO.caricaCarrello(utente.getId());
			    
			    
			    session.setAttribute("carrello", carrelloSalvato);
			    
			} catch (SQLException e) {
			    System.out.println("Errore caricamento carrello al login: " + e.getMessage());
			    
			    session.setAttribute("carrello", new ArrayList<ItemCarrello>());
			}
			
			
			response.sendRedirect(request.getContextPath() + "/index.jsp");
		} else {
			
			request.setAttribute("errore", "Email o password non validi. Riprova.");
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/login.jsp");
			dispatcher.forward(request, response);
		}
	} catch(SQLException e) {
		e.printStackTrace();
		request.setAttribute("errore", "Errore di connessione al database.");
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/login.jsp");
        dispatcher.forward(request, response);
			
		}
	}

}
