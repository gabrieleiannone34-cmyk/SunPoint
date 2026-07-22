package it.unisa.sunpoint.control;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

import it.unisa.sunpoint.dao.ProdottoDAO;
import it.unisa.sunpoint.model.Prodotto;
import it.unisa.sunpoint.model.Utente;


@WebServlet("/ModificaProdottoServlet")
public class ModificaProdottoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		Utente utente = (Utente) session.getAttribute("utenteLoggato");

		if (utente == null || !"admin".equals(utente.getRole())) {
			response.sendRedirect(request.getContextPath() + "/index.jsp");
			return;
		}

		// Leggiamo l'ID del prodotto da modificare
		int id = Integer.parseInt(request.getParameter("id"));
		ProdottoDAO prodottoDAO = new ProdottoDAO();
		
		try {
			Prodotto prodotto = prodottoDAO.doRetrieveById(id);
			// Passiamo il prodotto alla pagina JSP in modo che possa pre-compilare i campi
			request.setAttribute("prodottoDaModificare", prodotto);
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/modificaProdotto.jsp");
			dispatcher.forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Utente utente = (Utente) session.getAttribute("utenteLoggato");

		if (utente == null || !"admin".equals(utente.getRole())) {
			response.sendRedirect(request.getContextPath() + "/index.jsp");
			return;
		}

		try {
			// Recuperiamo i nuovi dati inseriti nel form
			int id = Integer.parseInt(request.getParameter("id"));
			String nome = request.getParameter("nome");
			String descrizione = request.getParameter("descrizione");
			double prezzo = Double.parseDouble(request.getParameter("prezzo"));
			int quantita = Integer.parseInt(request.getParameter("quantita"));
			String imagePath = request.getParameter("imagePath");

			Prodotto prodottoAggiornato = new Prodotto();
			prodottoAggiornato.setId(id);
			prodottoAggiornato.setNome(nome);
			prodottoAggiornato.setDescrizione(descrizione);
			prodottoAggiornato.setPrezzo(prezzo);
			prodottoAggiornato.setQuantita(quantita);
			prodottoAggiornato.setImagePath(imagePath);

			// Salviamo le modifiche sul Database
			ProdottoDAO prodottoDAO = new ProdottoDAO();
			prodottoDAO.doUpdate(prodottoAggiornato);

			// Torniamo al catalogo
			response.sendRedirect(request.getContextPath() + "/CatalogoServlet");

		} catch (SQLException | NumberFormatException e) {
			e.printStackTrace();
		}
	}
}
