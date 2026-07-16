package it.unisa.sunpoint.control;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

import it.unisa.sunpoint.dao.ProdottoDAO;
import it.unisa.sunpoint.model.Utente;


@WebServlet("/CancellaProdottoServlet")
public class CancellaProdottoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Utente utente = (Utente) session.getAttribute("utenteLoggato");

		// Sicurezza: Solo gli Admin possono eliminare!
		if (utente == null || !"admin".equals(utente.getRole())) {
			response.sendRedirect(request.getContextPath() + "/index.jsp");
			return;
		}

		String idStr = request.getParameter("id");
		if (idStr != null) {
			try {
				int id = Integer.parseInt(idStr);
				ProdottoDAO prodottoDAO = new ProdottoDAO();
				prodottoDAO.doDelete(id);
			} catch (SQLException | NumberFormatException e) {
				e.printStackTrace();
			}
		}
		// Dopo aver eliminato, rimandiamo l'admin al catalogo per vedere il risultato
		response.sendRedirect(request.getContextPath() + "/CatalogoServlet");
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
