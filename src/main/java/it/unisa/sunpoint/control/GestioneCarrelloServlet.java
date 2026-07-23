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

import it.unisa.sunpoint.dao.ProdottoDAO;
import it.unisa.sunpoint.model.ItemCarrello;
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

		HttpSession session = request.getSession();
		List<ItemCarrello> carrello = (List<ItemCarrello>) session.getAttribute("carrello");

		if (carrello != null) {
			ProdottoDAO prodottoDAO = new ProdottoDAO();
			
			for (int i = 0; i < carrello.size(); i++) {
				ItemCarrello item = carrello.get(i);
				if (item.getProdotto().getId() == idProdotto) {
					
					if ("aumenta".equals(azione)) {
						try {
							Prodotto occhialeDb = prodottoDAO.doRetrieveById(idProdotto);
							if (occhialeDb != null && item.getQuantita() < occhialeDb.getQuantita()) {
								item.incrementaQuantita();
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
					} 
					else if ("diminuisci".equals(azione)) {
						if (item.getQuantita() > 1) {
							item.decrementaQuantita();
						} else {
							carrello.remove(i); // Se la quantità arriva a 0, rimuoviamo l'articolo dalla lista
						}
					}
					break;
				}
			}
			session.setAttribute("carrello", carrello);
		}

		response.sendRedirect(request.getContextPath() + "/view/carrello.jsp");
	}
}
