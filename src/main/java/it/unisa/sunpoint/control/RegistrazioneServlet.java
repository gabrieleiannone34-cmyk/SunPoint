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

//@WebServlet("/RegistrazioneServlet")
public class RegistrazioneServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
 

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Se qualcuno prova ad accedere alla servlet via URL diretto, lo rimandiamo al form
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/registrazione.jsp");
	    dispatcher.forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Recupo dei parametri dalla form
		String nome = request.getParameter("nome");
		String cognome = request.getParameter("cognome");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String indirizzo = request.getParameter("indirizzo");
		String citta = request.getParameter("citta");
		
		//Creazione e aggiungiamo i parametri al JavaBean
		Utente nuovoUtente = new Utente();
		nuovoUtente.setNome(nome);
		nuovoUtente.setCognome(cognome);
		nuovoUtente.setEmail(email);
		nuovoUtente.setPasswordHash(password);
		nuovoUtente.setIndirizzo(indirizzo);
		nuovoUtente.setCitta(citta);
		nuovoUtente.setRole("user"); //ruolo default
		
		//Invochiamo il DAO
		UtenteDAO utenteDAO = new UtenteDAO();
		try {
			utenteDAO.doSave(nuovoUtente);
			
			// Se va tutto bene, reindirizziamo a una pagina di successo o alla login
            // Per ora lo rimandiamo alla index (o creiamo una successo.jsp)
			request.setAttribute("messaggio", "Registrazione avvenuta con successo");
			RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
			dispatcher.forward(request, response);
		} catch(SQLException e) {
			// Se c'è un errore (es. email già esistente nel DB)
            e.printStackTrace();
            request.setAttribute("errore", "Errore durante la registrazione. L'email potrebbe essere già in uso.");
            
         // Rimandiamo indietro l'utente alla pagina di registrazione mostrando l'errore
            RequestDispatcher dispatcher = request.getRequestDispatcher("registrazione.jsp");
            dispatcher.forward(request, response);
		}
		
	}

}
