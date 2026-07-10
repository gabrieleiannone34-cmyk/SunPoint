package it.unisa.sunpoint.control;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.unisa.sunpoint.dao.ProdottoDAO;
import it.unisa.sunpoint.model.Prodotto;

/**
 * Servlet implementation class CarrelloServlet
 */
@WebServlet("/CarrelloServlet")
public class CarrelloServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	// Usiamo doPost perché il form nella pagina del catalogo usa method="POST"
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. Catturiamo l'ID degli occhiali che l'utente ha cliccato
		int idProdotto = Integer.parseInt(request.getParameter("idProdotto"));
		
		ProdottoDAO prodottoDAO = new ProdottoDAO();
		
		try {
			// 2. Andiamo a prendere gli occhiali veri e propri dal database
			Prodotto occhialeScelto = prodottoDAO.doRetrieveById(idProdotto);
			
			if (occhialeScelto != null) {
				// 3. Apriamo il cassetto della sessione
				HttpSession session = request.getSession();
				
				// 4. Cerchiamo se l'utente ha già un carrello. Se non ce l'ha, ne creiamo uno vuoto.
                List<Prodotto> carrello = (List<Prodotto>) session.getAttribute("carrello");
                if (carrello == null) {
                    carrello = new ArrayList<>();
                }
                
            	// 5. Infiliamo gli occhiali nel carrello
                carrello.add(occhialeScelto);
                
                // 6. Salviamo il carrello aggiornato di nuovo nel cassetto
                session.setAttribute("carrello", carrello);
			}
			// 7. Finito! Rimandiamo l'utente alla pagina del catalogo per continuare gli acquisti
            response.sendRedirect(request.getContextPath() + "/CatalogoServlet");
		} catch (SQLException e) {
			e.printStackTrace();
            response.getWriter().println("Errore nell'aggiunta al carrello.");
		}
	}
}
