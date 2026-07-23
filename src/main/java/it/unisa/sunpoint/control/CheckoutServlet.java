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

import it.unisa.sunpoint.model.ItemCarrello;
import it.unisa.sunpoint.model.Ordine;
import it.unisa.sunpoint.model.Prodotto;
import it.unisa.sunpoint.model.Utente;
import it.unisa.sunpoint.dao.CarrelloDAO;
import it.unisa.sunpoint.dao.OrdineDAO;
import it.unisa.sunpoint.dao.ProdottoDAO;

@WebServlet("/CheckoutServlet")
public class CheckoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		HttpSession session = request.getSession();
		
		// Recuperiamo l'utente loggato e il carrello della sessione
		Utente utente = (Utente) session.getAttribute("utenteLoggato");
		List<ItemCarrello> carrello = (List<ItemCarrello>) session.getAttribute("carrello");
		
		// 2. Controlli di sicurezza: l'utente deve essere loggato e il carrello non deve essere vuoto
		if (utente == null) {
			response.sendRedirect(request.getContextPath() + "/LoginServlet");
            return;
        }
        
        if (carrello == null || carrello.isEmpty()) {
        	response.sendRedirect(request.getContextPath() + "/VisualizzaCarrelloServlet");
            return;
        }
        
        // 3. Calcoliamo il totale dei prodotti nel carrello
        double totaleDaPagare = 0.0;
        for (ItemCarrello item : carrello) {
            totaleDaPagare += item.getProdotto().getPrezzo();
        }
        
        // 4. Prepariamo lo "scontrino" (il JavaBean Ordine)
        Ordine nuovoOrdine = new Ordine();
        nuovoOrdine.setUserId(utente.getId()); // Usiamo l'ID dell'utente loggato
        nuovoOrdine.setTotale(totaleDaPagare);
        
        try {
        	// 5. Passiamo lo scontrino al DAO per salvarlo in MySQL
        	OrdineDAO ordineDAO = new OrdineDAO();
        	//Salviamo l'ordine e CATTURIAMO l'ID generato
        	int orderId = ordineDAO.doSave(nuovoOrdine);
        	
        	//Se l'ordine è stato salvato correttamente (ID maggiore di zero)
            if (orderId > 0) {
                // Salviamo tutti gli articoli in Articoli_ordinati!
                ordineDAO.salvaArticoliOrdine(orderId, carrello);
            }
            
            //Scaliamo i pezzi dal magazzino 
            ProdottoDAO prodottoDAO = new ProdottoDAO();
            for (ItemCarrello item : carrello) {
                prodottoDAO.aggiornaQuantita(item.getProdotto().getId());
            }
            
            // Visto che ha pagato, dobbiamo svuotare anche il carrello salvato nel Database
            CarrelloDAO carrelloDAO = new CarrelloDAO();
            carrelloDAO.svuotaCarrelloDB(utente.getId());
            
         // Mettiamo il numero dell'ordine nello zaino per la pagina JSP
            request.setAttribute("numeroOrdine", orderId);
            
         // Svuotiamo la sessione e rimandiamo alla conferma
            session.removeAttribute("carrello");
            request.getRequestDispatcher("/WEB-INF/view/conferma.jsp").forward(request, response);
            
        } catch (SQLException e) {
        	e.printStackTrace();
            response.getWriter().println("Errore durante il salvataggio dell'ordine nel database.");
        }
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
