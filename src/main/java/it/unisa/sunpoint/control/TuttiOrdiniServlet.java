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
		
		//Solo gli admin possono vedere questa pagina
		if (utente == null || !"admin".equals(utente.getRole())) {
			response.sendRedirect(request.getContextPath() + "/index.jsp");
			return;
		}
		
		OrdineDAO ordineDAO = new OrdineDAO();
		try {
			// L'Admin chiede tutti gli ordini del negozio
			List<Ordine> tuttiOrdini = ordineDAO.doRetrieveAll();
			
			// Li mettiamo nello "zaino"
			request.setAttribute("tuttiOrdini", tuttiOrdini);
		
			// Apriamo la porta della pagina riservata
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/tuttiOrdini.jsp");
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
