package it.unisa.sunpoint.control;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// request.getSession(false) significa: dammi la sessione solo se esiste già, non crearne una nuova
		HttpSession session = request.getSession(false);
		
		if(session != null) {
			//Distrugge fisicamente la sessione in memoria 
			session.invalidate();
		}
		
		//Rimanda l'utente alla home page (che ora lo vedrà come ospite non loggato)
		response.sendRedirect(request.getContextPath() + "/index.jsp");
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
