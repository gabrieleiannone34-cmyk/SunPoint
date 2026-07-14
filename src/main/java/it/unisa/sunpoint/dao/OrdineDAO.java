package it.unisa.sunpoint.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import it.unisa.sunpoint.model.Ordine;
import it.unisa.sunpoint.model.Prodotto;

public class OrdineDAO {
	private static DataSource ds;
	
	static {
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			ds = (DataSource) envCtx.lookup("jdbc/sunpoint");
		} catch (NamingException e) {
			System.out.println("Errore DataSource OrdineDAO: " + e.getMessage());
		}
	}
	
	private static final String TABLE_NAME = "Ordini";
	
	//Metodo per salvare un nuovo ordine nel database
	public synchronized int doSave(Ordine ordine) throws SQLException {
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        int orderId = -1; 
        
        String insertSQL = "INSERT INTO Ordini (user_id, totale) VALUES (?, ?)";
        
     try {
    	 connection = ds.getConnection();
    	 //Statement.RETURN_GENERATED_KEYS dice a MySQL di ridarci l'ID appena creato!
    	 preparedStatement = connection.prepareStatement(insertSQL, PreparedStatement.RETURN_GENERATED_KEYS);
    	 preparedStatement.setInt(1, ordine.getUserId());
    	 preparedStatement.setDouble(2, ordine.getTotale());
    	 preparedStatement.executeUpdate();
    	 
    	 //Prendiamo l'id appena generato
    	 rs = preparedStatement.getGeneratedKeys();
         if (rs.next()) {
             orderId = rs.getInt(1);
         }
     } finally {
    	 if (rs != null) rs.close();
         if (preparedStatement != null) preparedStatement.close();
         if (connection != null) connection.close();
     }
     return orderId; // Restituiamo il numero dello scontrino alla Servlet
    }
	// Nuovo metodo per salvare le singole voci dello scontrino
	public synchronized void salvaArticoliOrdine(int orderId, List<Prodotto> carrello) throws SQLException {
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        
        // Inseriamo l'ID dell'ordine, l'ID dell'occhiale, quantità 1 e il prezzo attuale
        String insertSQL = "INSERT INTO Articoli_ordinati (order_id, product_id, quantita, prezzo_al_momento) VALUES (?, ?, 1, ?)";
        
        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(insertSQL);
            
            for (Prodotto p : carrello) {
                preparedStatement.setInt(1, orderId);
                preparedStatement.setInt(2, p.getId());
                preparedStatement.setDouble(3, p.getPrezzo());
                preparedStatement.executeUpdate(); // Spara il comando su MySQL
            }
        } finally {
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) connection.close();
        }
	}
}

