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

	// Usiamo doPost perché il form nella pagina del catalogo usa method="POST"
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Catturiamo l'ID degli occhiali che l'utente ha cliccato
		int idProdotto = Integer.parseInt(request.getParameter("idProdotto"));
		
		ProdottoDAO prodottoDAO = new ProdottoDAO();
		
		try {
			//Andiamo a prendere gli occhiali veri e propri dal database
			Prodotto occhialeScelto = prodottoDAO.doRetrieveById(idProdotto);
			
			if (occhialeScelto != null) {
				//Apriamo il cassetto della sessione
				HttpSession session = request.getSession();
				
				//Cerchiamo se l'utente ha già un carrello. Se non ce l'ha, ne creiamo uno vuoto.
                List<ItemCarrello> carrello = (List<ItemCarrello>) session.getAttribute("carrello");
                if (carrello == null) {
                    carrello = new ArrayList<>();
                }
                
             // Verifichiamo se l'occhiale è già presente nel carrello
                ItemCarrello itemTrovato = null;
                int pezziGiaNelCarrello = 0;
                for (ItemCarrello item : carrello) {
                    if (item.getProdotto().getId() == idProdotto) {
                        itemTrovato = item;
                    	pezziGiaNelCarrello = item.getQuantita();
                    	break;
                    }
                }

                //LA REGOLA D'ORO: Aggiungiamo al carrello SOLO SE non superiamo le scorte
                if (pezziGiaNelCarrello < occhialeScelto.getQuantita()) {
                	if (itemTrovato != null) {
                		itemTrovato.incrementaQuantita(); // Se c'è già, aumentiamo solo la quantità
                	} else {
                		carrello.add(new ItemCarrello(occhialeScelto, 1)); // Altrimenti creiamo un nuovo elemento con quantità 1
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

	

