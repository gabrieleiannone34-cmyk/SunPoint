package it.unisa.sunpoint.control;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import it.unisa.sunpoint.model.Utente;
import it.unisa.sunpoint.dao.UtenteDAO;

@WebServlet("/RegistrazioneServlet")
public class RegistrazioneServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
 

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/registrazione.jsp");
	    dispatcher.forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nome = request.getParameter("nome");
		String cognome = request.getParameter("cognome");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String indirizzo = request.getParameter("indirizzo");
		String citta = request.getParameter("citta");
		
		Utente nuovoUtente = new Utente();
		nuovoUtente.setNome(nome);
		nuovoUtente.setCognome(cognome);
		nuovoUtente.setEmail(email);
		nuovoUtente.setPasswordHash(password);
		nuovoUtente.setIndirizzo(indirizzo);
		nuovoUtente.setCitta(citta);
		nuovoUtente.setRole("user"); 
		
		UtenteDAO utenteDAO = new UtenteDAO();
		try {
			utenteDAO.doSave(nuovoUtente);

			request.setAttribute("messaggio", "Registrazione avvenuta con successo");
			RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
			dispatcher.forward(request, response);
		} catch(SQLException e) {
            e.printStackTrace();
            request.setAttribute("errore", "Errore durante la registrazione. L'email potrebbe essere già in uso.");
            
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/registrazione.jsp");
            dispatcher.forward(request, response);
		}
		
	}

}
