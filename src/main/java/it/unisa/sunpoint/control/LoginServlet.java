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

import it.unisa.sunpoint.model.Prodotto;
import it.unisa.sunpoint.model.Utente;
import it.unisa.sunpoint.dao.CarrelloDAO;
import it.unisa.sunpoint.dao.UtenteDAO;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	// Mostra la pagina di login (nascosta nella cartella WEB-INF)
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/login.jsp");
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
			
			CarrelloDAO carrelloDAO = new CarrelloDAO();
            try {
                // Chiediamo al database di ridarci la lista degli occhiali di questo utente
                List<Prodotto> carrelloSalvato = carrelloDAO.caricaCarrello(utente.getId());
                
                // Mettiamo la lista recuperata nella sessione!
                session.setAttribute("carrello", carrelloSalvato);
                
            } catch (SQLException e) {
                System.out.println("Errore caricamento carrello al login: " + e.getMessage());
                // Se c'è un errore, creiamo almeno un carrello vuoto per non far crashare il sito
                session.setAttribute("carrello", new ArrayList<Prodotto>());
            }
			
			// Reindirizziamo l'utente alla home page
			response.sendRedirect(request.getContextPath() + "/index.jsp");
		} else {
			// Login fallito
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
