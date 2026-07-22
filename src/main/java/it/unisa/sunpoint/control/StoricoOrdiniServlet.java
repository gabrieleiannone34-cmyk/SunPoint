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
		
		//Verifica se l'utente è loggato(se non è loggatto va alla pagina di login)
		if(utente == null) {
			response.sendRedirect(request.getContextPath() + "/LoginServlet");
			return;
		}
		OrdineDAO ordineDAO = new OrdineDAO();
		try {
			// 2. Chiediamo al database tutti gli ordini di questo utente
			List<Ordine> storicoOrdini = ordineDAO.doRetrieveByUserId(utente.getId());
			
			// 3. Inseriamo la lista nella request
			// (Usiamo la request e non la sessione perché è un dato "usa e getta" per visualizzare la pagina)
			request.setAttribute("storicoOrdini", storicoOrdini);
			
			// 4. Apriamo la porta della JSP nascosta
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
