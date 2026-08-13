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
import it.unisa.sunpoint.model.ItemCarrello;
import it.unisa.sunpoint.model.Prodotto;

@WebServlet("/CarrelloServlet")
public class CarrelloServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int idProdotto = Integer.parseInt(request.getParameter("idProdotto"));
		
		ProdottoDAO prodottoDAO = new ProdottoDAO();
		
		try {
			
			Prodotto occhialeScelto = prodottoDAO.doRetrieveById(idProdotto);
			
			if (occhialeScelto != null) {
				
				HttpSession session = request.getSession();
				
				
                List<ItemCarrello> carrello = (List<ItemCarrello>) session.getAttribute("carrello");
                if (carrello == null) {
                    carrello = new ArrayList<>();
                }
                
             
                ItemCarrello itemTrovato = null;
                int pezziGiaNelCarrello = 0;
                for (ItemCarrello item : carrello) {
                    if (item.getProdotto().getId() == idProdotto) {
                        itemTrovato = item;
                    	pezziGiaNelCarrello = item.getQuantita();
                    	break;
                    }
                }

                
                if (pezziGiaNelCarrello < occhialeScelto.getQuantita()) {
                	if (itemTrovato != null) {
                		itemTrovato.incrementaQuantita(); 
                	} else {
                		carrello.add(new ItemCarrello(occhialeScelto, 1)); 
                	}
                        session.setAttribute("carrello", carrello);
                        response.sendRedirect(request.getContextPath() + "/VisualizzaCarrelloServlet");
                        return;
                } else {
                	response.sendRedirect(request.getContextPath() + "/CatalogoServlet?errore=esaurito");
					return;
                }
			}
                response.sendRedirect(request.getContextPath() + "/CatalogoServlet");
			} catch (SQLException e) {
				e.printStackTrace();
				response.getWriter().println("Errore nell'aggiunta al carrello.");
			}
		}
	}

	

