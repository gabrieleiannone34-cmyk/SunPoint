package it.unisa.sunpoint.control;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import it.unisa.sunpoint.dao.CarrelloDAO;
import it.unisa.sunpoint.model.ItemCarrello;
import it.unisa.sunpoint.model.Prodotto;
import it.unisa.sunpoint.model.Utente;

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Utente utente = (Utente) session.getAttribute("utenteLoggato");

		System.out.println("--- LOGOUT AVVIATO ---");

		if (utente != null) {
			System.out.println("Utente in fase di logout: " + utente.getNome());
			
			// Recuperiamo il carrello
			List<ItemCarrello> carrello = (List<ItemCarrello>) session.getAttribute("carrello");
			
			if (carrello != null) {
				System.out.println("Carrello in sessione trovato. Contiene " + carrello.size() + " articoli diversi.");
				CarrelloDAO carrelloDAO = new CarrelloDAO();
				
				try {
					carrelloDAO.salvaCarrello(utente.getId(), carrello);
					System.out.println("Operazione sul DB completata con successo!");
				} catch (SQLException e) {
					System.out.println("ERRORE DATABASE DURANTE IL SALVATAGGIO DEL CARRELLO:");
					e.printStackTrace(); // Questo stamperà l'errore esatto di MySQL!
				}
			} else {
				System.out.println("Attenzione: Il carrello nella sessione era NULL.");
			}
		} else {
			System.out.println("Nessun utente trovato in sessione (forse sessione già scaduta?).");
		}

		// Distruggiamo la sessione e torniamo alla home
		session.invalidate();
		response.sendRedirect(request.getContextPath() + "/index.jsp");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
