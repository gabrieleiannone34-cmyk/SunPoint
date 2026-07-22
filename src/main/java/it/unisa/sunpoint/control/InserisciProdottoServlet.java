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
       
	//Metodo GET: Mostra la pagina col form all'amministratore
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Utente utente = (Utente) session.getAttribute("utenteLoggato");
		
		// CONTROLLO DI SICUREZZA: Sei loggato? E sei un admin?
		if (utente == null || !"admin".equals(utente.getRole())) {
			// Se non sei admin, ti caccio via
			response.sendRedirect(request.getContextPath() + "/index.jsp");
			return;
		}
		
		// Se sei admin, ti apro la porta del form nascosto
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/inserisciProdotto.jsp");
		dispatcher.forward(request, response);
	}

	// Metodo POST: Riceve i dati dal form e salva l'occhiale nel DB
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Utente utente = (Utente) session.getAttribute("utenteLoggato");

		// Ripetiamo il controllo di sicurezza anche per il salvataggio
		if (utente == null || !"admin".equals(utente.getRole())) {
			response.sendRedirect(request.getContextPath() + "/index.jsp");
			return;
		}

		try {
			// 1. Catturiamo i dati scritti dall'admin nel form HTML
			String nome = request.getParameter("nome");
			String descrizione = request.getParameter("descrizione");
			double prezzo = Double.parseDouble(request.getParameter("prezzo"));
			int quantita = Integer.parseInt(request.getParameter("quantita"));
			String imagePath = request.getParameter("imagePath");

			// 2. Creiamo l'occhiale
			Prodotto nuovoOcchiale = new Prodotto();
			nuovoOcchiale.setNome(nome);
			nuovoOcchiale.setDescrizione(descrizione);
			nuovoOcchiale.setPrezzo(prezzo);
			nuovoOcchiale.setQuantita(quantita);
			nuovoOcchiale.setImagePath(imagePath);

			// 3. Lo salviamo nel database
			ProdottoDAO prodottoDAO = new ProdottoDAO();
			prodottoDAO.doSave(nuovoOcchiale);

			// 4. Riportiamo l'admin al catalogo, così vede subito l'occhiale inserito!
			response.sendRedirect(request.getContextPath() + "/CatalogoServlet");

		} catch (SQLException | NumberFormatException e) {
			e.printStackTrace();
			response.getWriter().println("Errore nell'inserimento del prodotto. Controlla i dati.");
		}
	}

}
