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
		
		
		Utente utente = (Utente) session.getAttribute("utenteLoggato");
		List<ItemCarrello> carrello = (List<ItemCarrello>) session.getAttribute("carrello");
		
		
		if (utente == null) {
			response.sendRedirect(request.getContextPath() + "/LoginServlet");
            return;
        }
        
        if (carrello == null || carrello.isEmpty()) {
        	response.sendRedirect(request.getContextPath() + "/VisualizzaCarrelloServlet");
            return;
        }
        
        
        double totaleDaPagare = 0.0;
        for (ItemCarrello item : carrello) {
            totaleDaPagare += item.getProdotto().getPrezzo();
        }
        
        
        Ordine nuovoOrdine = new Ordine();
        nuovoOrdine.setUserId(utente.getId()); 
        nuovoOrdine.setTotale(totaleDaPagare);
        
        try {
        	
        	OrdineDAO ordineDAO = new OrdineDAO();
        	
        	int orderId = ordineDAO.doSave(nuovoOrdine);
        	
        	
            if (orderId > 0) {
                
                ordineDAO.salvaArticoliOrdine(orderId, carrello);
            }
            
           
            ProdottoDAO prodottoDAO = new ProdottoDAO();
            for (ItemCarrello item : carrello) {
                prodottoDAO.aggiornaQuantita(item.getProdotto().getId());
            }
            
            
            CarrelloDAO carrelloDAO = new CarrelloDAO();
            carrelloDAO.svuotaCarrelloDB(utente.getId());
            
        
            request.setAttribute("numeroOrdine", orderId);
            
         
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
