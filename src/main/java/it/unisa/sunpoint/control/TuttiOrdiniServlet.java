package it.unisa.sunpoint.control;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import it.unisa.sunpoint.dao.OrdineDAO;
import it.unisa.sunpoint.model.Ordine;
import it.unisa.sunpoint.model.Utente;


@WebServlet("/TuttiOrdiniServlet")
public class TuttiOrdiniServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Utente utente = (Utente) session.getAttribute("utenteLoggato");

		if (utente == null || !"admin".equals(utente.getRole())) {
			response.sendRedirect(request.getContextPath() + "/index.jsp");
			return;
		}

		// Catturiamo i filtri dalla richiesta (se ci sono)
		String dataInizio = request.getParameter("dataInizio");
		String dataFine = request.getParameter("dataFine");
		String idCliente = request.getParameter("idCliente");

		OrdineDAO ordineDAO = new OrdineDAO();
		try {
			List<Ordine> ordini;
			
			// Se c'è almeno un filtro attivo, usiamo la ricerca filtrata
			if ((dataInizio != null && !dataInizio.isEmpty()) || 
			    (dataFine != null && !dataFine.isEmpty()) || 
			    (idCliente != null && !idCliente.trim().isEmpty())) {
				
				ordini = ordineDAO.doRetrieveByFilters(dataInizio, dataFine, idCliente);
			} else {
				// Altrimenti, mostriamo tutto
				ordini = ordineDAO.doRetrieveAll();
			}
			
			request.setAttribute("tuttiOrdini", ordini);
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/tuttiOrdini.jsp");
			dispatcher.forward(request, response);

		} catch (SQLException e) {
			e.printStackTrace();
			response.getWriter().println("Errore durante il recupero degli ordini.");
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
