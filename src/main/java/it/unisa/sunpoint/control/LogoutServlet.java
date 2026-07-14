package it.unisa.sunpoint.control;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

import it.unisa.sunpoint.dao.CarrelloDAO;
import it.unisa.sunpoint.model.Prodotto;
import it.unisa.sunpoint.model.Utente;

public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// request.getSession(false) significa: dammi la sessione solo se esiste già, non crearne una nuova
		HttpSession session = request.getSession(false);
		
		if(session != null) {
			Utente utente = (Utente) session.getAttribute("utenteLoggato");
			List<Prodotto> carrello = (List<Prodotto>) session.getAttribute("carrello");
			
			//Se l'utente era loggato e aveva roba nel carrello, SALVIAMO NEL DB!
			if (utente != null && carrello != null && !carrello.isEmpty()) {
                CarrelloDAO carrelloDAO = new CarrelloDAO();
                try {
                    carrelloDAO.salvaCarrello(utente.getId(), carrello);
                } catch (java.sql.SQLException e) {
                    System.out.println("Errore salvataggio carrello al logout: " + e.getMessage());
                }
            }
            
            // 3. Ora possiamo distruggere la sessione in totale sicurezza
            session.invalidate();
		}
		
		//Rimanda l'utente alla home page (che ora lo vedrà come ospite non loggato)
		response.sendRedirect(request.getContextPath() + "/index.jsp");
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
