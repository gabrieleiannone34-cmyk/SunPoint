package it.unisa.sunpoint.control;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

import it.unisa.sunpoint.model.ItemCarrello;
import it.unisa.sunpoint.model.Prodotto;

@WebServlet("/RimuoviCarrelloServlet")
public class RimuoviCarrelloServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. Recuperiamo l'ID dell'occhiale che l'utente vuole cancellare
        String idProdottoRim = request.getParameter("idProdotto");
        
        if (idProdottoRim != null) {
            int idProdotto = Integer.parseInt(idProdottoRim);
            
         // 2. Apriamo la sessione e prendiamo il carrello
         HttpSession session = request.getSession();
         List<ItemCarrello> carrello = (List<ItemCarrello>) session.getAttribute("carrello");
        
         if (carrello != null) {
             for (int i = 0; i < carrello.size(); i++) {
                 // 2. Dobbiamo usare getProdotto().getId() per trovare l'occhiale
                 if (carrello.get(i).getProdotto().getId() == idProdotto) {
                     carrello.remove(i);
                     break;
                 }
             }
             session.setAttribute("carrello", carrello);
         }
            
      }
     // 5. Ricarichiamo la pagina del carrello, che ora mostrerà un occhiale in meno!
        response.sendRedirect(request.getContextPath() + "/VisualizzaCarrelloServlet");
	}

}
