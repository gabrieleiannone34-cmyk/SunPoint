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


@WebServlet("/StoricoOrdiniServlet")
public class StoricoOrdiniServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Utente utente = (Utente) session.getAttribute("utenteLoggato");
		
		
		if(utente == null) {
			response.sendRedirect(request.getContextPath() + "/LoginServlet");
			return;
		}
		OrdineDAO ordineDAO = new OrdineDAO();
		try {
			
			List<Ordine> storicoOrdini = ordineDAO.doRetrieveByUserId(utente.getId());
			
			
			request.setAttribute("storicoOrdini", storicoOrdini);
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/ordini.jsp");
			dispatcher.forward(request, response);
			
		} catch (SQLException e) {
			e.printStackTrace();
			response.getWriter().println("Errore durante il recupero dello storico ordini.");
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		doGet(request, response);
	}

}
