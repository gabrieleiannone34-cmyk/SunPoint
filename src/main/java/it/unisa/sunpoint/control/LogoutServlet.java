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

		if (utente != null) {
			List<ItemCarrello> carrello = (List<ItemCarrello>) session.getAttribute("carrello");
			
			if (carrello != null) {
				CarrelloDAO carrelloDAO = new CarrelloDAO();
				
				try {
					carrelloDAO.salvaCarrello(utente.getId(), carrello);
				} catch (SQLException e) {
					e.printStackTrace(); 
				}
		

		}
		session.invalidate();
		response.sendRedirect(request.getContextPath() + "/index.jsp");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
