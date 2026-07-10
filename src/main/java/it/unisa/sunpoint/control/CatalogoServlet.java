package it.unisa.sunpoint.control;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import it.unisa.sunpoint.dao.ProdottoDAO;
import it.unisa.sunpoint.model.Prodotto;


public class CatalogoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	// Usiamo il doGet perché l'utente sta solo richiedendo di VEDERE una pagina, non sta inviando un form
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ProdottoDAO prodottoDAO = new ProdottoDAO();
		
		try {
			// 1. Chiediamo al DAO(con il metodo doRetrieveAll()) tutta la lista dei prodotti
            List<Prodotto> listaProdotti = prodottoDAO.doRetrieveAll();
			
            // 2. Attacchiamo la lista alla richiesta dandole l'etichetta "catalogo"
            request.setAttribute("catalogo", listaProdotti);
            
         // 3. Passiamo alla JSP nascosta
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/catalogo.jsp");
            dispatcher.forward(request, response);
		} catch(SQLException e) {
			e.printStackTrace();
            response.getWriter().println("Errore nel caricamento del catalogo.");
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
