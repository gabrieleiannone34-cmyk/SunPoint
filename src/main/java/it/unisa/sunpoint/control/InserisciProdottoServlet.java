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


@WebServlet("/InserisciProdottoServlet")
public class InserisciProdottoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Utente utente = (Utente) session.getAttribute("utenteLoggato");
		

		if (utente == null || !"admin".equals(utente.getRole())) {
			response.sendRedirect(request.getContextPath() + "/index.jsp");
			return;
		}

		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/inserisciProdotto.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Utente utente = (Utente) session.getAttribute("utenteLoggato");

		if (utente == null || !"admin".equals(utente.getRole())) {
			response.sendRedirect(request.getContextPath() + "/index.jsp");
			return;
		}

		try {

			String nome = request.getParameter("nome");
			String descrizione = request.getParameter("descrizione");
			double prezzo = Double.parseDouble(request.getParameter("prezzo"));
			int quantita = Integer.parseInt(request.getParameter("quantita"));
			String imagePath = request.getParameter("imagePath");


			Prodotto nuovoOcchiale = new Prodotto();
			nuovoOcchiale.setNome(nome);
			nuovoOcchiale.setDescrizione(descrizione);
			nuovoOcchiale.setPrezzo(prezzo);
			nuovoOcchiale.setQuantita(quantita);
			nuovoOcchiale.setImagePath(imagePath);

			ProdottoDAO prodottoDAO = new ProdottoDAO();
			prodottoDAO.doSave(nuovoOcchiale);

			response.sendRedirect(request.getContextPath() + "/CatalogoServlet");

		} catch (SQLException | NumberFormatException e) {
			e.printStackTrace();
			response.getWriter().println("Errore nell'inserimento del prodotto. Controlla i dati.");
		}
	}

}
