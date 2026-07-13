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

import it.unisa.sunpoint.model.Ordine;
import it.unisa.sunpoint.model.Prodotto;
import it.unisa.sunpoint.model.Utente;
import it.unisa.sunpoint.dao.OrdineDAO;


public class CheckoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		HttpSession session = request.getSession();
		
		// Recuperiamo l'utente loggato e il carrello della sessione
		Utente utente = (Utente) session.getAttribute("utenteLoggato");
		List<Prodotto> carrello = (List<Prodotto>) session.getAttribute("carrello");
		
		// 2. Controlli di sicurezza: l'utente deve essere loggato e il carrello non deve essere vuoto
		if (utente == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        if (carrello == null || carrello.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/carrello.jsp");
            return;
        }
        
        // 3. Calcoliamo il totale dei prodotti nel carrello
        double totaleDaPagare = 0.0;
        for (Prodotto p : carrello) {
            totaleDaPagare += p.getPrezzo();
        }
        
        // 4. Prepariamo lo "scontrino" (il JavaBean Ordine)
        Ordine nuovoOrdine = new Ordine();
        nuovoOrdine.setUserId(utente.getId()); // Usiamo l'ID dell'utente loggato
        nuovoOrdine.setTotale(totaleDaPagare);
        
        try {
        	// 5. Passiamo lo scontrino al DAO per salvarlo in MySQL
        	OrdineDAO ordineDAO = new OrdineDAO();
            ordineDAO.doSave(nuovoOrdine);
            
            // 6. Svuotiamo il carrello (l'acquisto è concluso!)
            session.removeAttribute("carrello");
            
            // 7. Rimandiamo l'utente a una pagina di ringraziamento
            response.sendRedirect(request.getContextPath() + "/conferma.jsp");
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
