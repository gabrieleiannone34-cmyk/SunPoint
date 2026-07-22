package it.unisa.sunpoint.control;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

import it.unisa.sunpoint.dao.ProdottoDAO;
import it.unisa.sunpoint.model.Prodotto;


@WebServlet("/GestioneCarrelloServlet")
public class GestioneCarrelloServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String azione = request.getParameter("azione");
		int idProdotto = Integer.parseInt(request.getParameter("idProdotto"));

		// 2. Recuperiamo il carrello dalla Sessione dell'utente
		HttpSession session = request.getSession();
		List<Prodotto> carrello = (List<Prodotto>) session.getAttribute("carrello");

		if (carrello != null) {
			ProdottoDAO prodottoDAO = new ProdottoDAO();
			
			// 3. Logica di variazione quantità
			if ("aumenta".equals(azione)) {
				try {
					// Verifichiamo quanti pezzi di questo prodotto ci sono già nel carrello
					int pezziGiaNelCarrello = 0;
					Prodotto prodottoTrovato = null;
					
					for (Prodotto item : carrello) {
						if (item.getId() == idProdotto) {
							pezziGiaNelCarrello++;
							prodottoTrovato = item;
						}
					}
					
					// Controlliamo le scorte nel database prima di aggiungere
					Prodotto occhialeDb = prodottoDAO.doRetrieveById(idProdotto);
					if (occhialeDb != null && pezziGiaNelCarrello < occhialeDb.getQuantita() && prodottoTrovato != null) {
						// Aggiungiamo un clone/istanza dello stesso prodotto alla lista
						carrello.add(prodottoTrovato);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			} else if ("diminuisci".equals(azione)) {
				// Rimuoviamo il primo prodotto trovato con quell'ID dalla lista
				for (int i = 0; i < carrello.size(); i++) {
					if (carrello.get(i).getId() == idProdotto) {
						carrello.remove(i);
						break; // Ne rimuoviamo solo uno per volta
					}
				}
			}
			
			// Aggiorniamo il carrello nella sessione
			session.setAttribute("carrello", carrello);
		}

		// 4. Ricarichiamo la pagina del carrello
		response.sendRedirect(request.getContextPath() + "/view/carrello.jsp");
	}
}
