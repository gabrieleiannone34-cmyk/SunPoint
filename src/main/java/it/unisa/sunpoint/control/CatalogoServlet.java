package it.unisa.sunpoint.control;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import it.unisa.sunpoint.dao.ProdottoDAO;
import it.unisa.sunpoint.model.Prodotto;

@WebServlet("/CatalogoServlet")
public class CatalogoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String minPriceStr = request.getParameter("minPrice");
	    String maxPriceStr = request.getParameter("maxPrice");
	    
	    ProdottoDAO prodottoDAO = new ProdottoDAO();
	    List<Prodotto> catalogo;
	    
	    try {
	        if ((minPriceStr != null && !minPriceStr.isEmpty()) || (maxPriceStr != null && !maxPriceStr.isEmpty())) {
	            
	            Double minPrice = (minPriceStr != null && !minPriceStr.isEmpty()) ? Double.parseDouble(minPriceStr) : null;
	            Double maxPrice = (maxPriceStr != null && !maxPriceStr.isEmpty()) ? Double.parseDouble(maxPriceStr) : null;
	            
	            catalogo = prodottoDAO.doRetrieveByFilters(minPrice, maxPrice);
	        } else {
	            catalogo = prodottoDAO.doRetrieveAll();
	        }
            
            request.setAttribute("catalogo", catalogo);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/catalogo.jsp");
            dispatcher.forward(request, response);
		} catch(SQLException e) {
			e.printStackTrace();
            response.getWriter().println("Errore nel caricamento del catalogo.");
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
