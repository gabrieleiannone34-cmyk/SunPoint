package it.unisa.sunpoint.control;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

import it.unisa.sunpoint.model.Prodotto;

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
         List<Prodotto> carrello = (List<Prodotto>) session.getAttribute("carrello");
        
         // 3. Cerchiamo il prodotto nel carrello e lo eliminiamo
         if (carrello != null) {
             // Usiamo un classico ciclo for per scorrere la lista
             for (int i = 0; i < carrello.size(); i++) {
                 // Se l'ID dell'occhiale nella lista coincide con quello da rimuovere...
                 if (carrello.get(i).getId() == idProdotto) {
                     carrello.remove(i); // Lo rimuoviamo!
                     break; // INTERROMPIAMO IL CICLO: ne togliamo solo UNO alla volta (nel caso ce ne siano due uguali)
                 }
             }
             // 4. Rimettiamo il carrello aggiornato nella sessione
             session.setAttribute("carrello", carrello);
         }
      }
     // 5. Ricarichiamo la pagina del carrello, che ora mostrerà un occhiale in meno!
     response.sendRedirect(request.getContextPath() + "/carrello.jsp");
	}

}
