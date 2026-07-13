package it.unisa.sunpoint.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import it.unisa.sunpoint.model.Ordine;

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
	public synchronized void doSave(Ordine ordine) throws SQLException {
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        
     // Non inseriamo 'id' (è AUTO_INCREMENT) e 'data_ordine' (è CURRENT_TIMESTAMP)
     String insertSQL = "INSERT INTO " + TABLE_NAME + " (user_id, totale) VALUES (?, ?)";
     
     try {
    	 connection = ds.getConnection();
    	 preparedStatement = connection.prepareStatement(insertSQL);
    	 preparedStatement.setInt(1, ordine.getUserId());
    	 preparedStatement.setDouble(2, ordine.getTotale());
    	 preparedStatement.executeUpdate();
     } finally {
    	 try {
    		 if (preparedStatement != null) preparedStatement.close();
    	 } finally {
    		 if (connection != null) connection.close();
    	 }
     }
	}
}
