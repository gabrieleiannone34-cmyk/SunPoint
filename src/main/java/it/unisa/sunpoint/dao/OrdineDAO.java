package it.unisa.sunpoint.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import it.unisa.sunpoint.model.ItemCarrello;
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
	public synchronized void salvaArticoliOrdine(int orderId, List<ItemCarrello> carrello) throws SQLException {
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        
        // Inseriamo l'ID dell'ordine, l'ID dell'occhiale, quantità 1 e il prezzo attuale
        String insertSQL = "INSERT INTO Articoli_ordinati (order_id, product_id, quantita, prezzo_al_momento) VALUES (?, ?, ?, ?)";
        
        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(insertSQL);
            
            for (ItemCarrello item : carrello) {
                preparedStatement.setInt(1, orderId);
                preparedStatement.setInt(2, item.getProdotto().getId());
                
                // Inseriamo la quantità esatta scelta dall'utente 
                preparedStatement.setInt(3, item.getQuantita()); 
                
                // Congeliamo il prezzo al momento dell'acquisto (Requisito storico!)
                preparedStatement.setDouble(4, item.getProdotto().getPrezzo());
                
                preparedStatement.executeUpdate(); // Spara il comando su MySQL
            }
        } finally {
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) connection.close();
        }
	}
	// Nuovo metodo per estrarre lo storico ordini di un cliente
	public synchronized List<Ordine> doRetrieveByUserId(int userId) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
			
		// Creiamo una lista vuota per contenere gli ordini trovati
		List<Ordine> ordini = new ArrayList<>();
			
		// Li ordiniamo in modo decrescente (dal più recente al più vecchio)
		String selectSQL = "SELECT * FROM Ordini WHERE user_id = ? ORDER BY id DESC";
			
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setInt(1, userId);
			rs = preparedStatement.executeQuery();
				
			while (rs.next()) {
				Ordine ordine = new Ordine();
				ordine.setId(rs.getInt("id"));
				ordine.setUserId(rs.getInt("user_id"));
				ordine.setTotale(rs.getDouble("totale"));
		
				ordini.add(ordine);
			}
		} finally {
			if (rs != null) rs.close();
			if (preparedStatement != null) preparedStatement.close();
			if (connection != null) connection.close();
		}
			
		return ordini;
	}
	// Metodo ESCLUSIVO per l'Admin: recupera TUTTI gli ordini del negozio
	public synchronized List<Ordine> doRetrieveAll() throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
			
		List<Ordine> tuttiOrdini = new ArrayList<>();
			
		// Li ordiniamo dal più recente al più vecchio
		String selectSQL = "SELECT * FROM Ordini ORDER BY id DESC";
			
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			rs = preparedStatement.executeQuery();
				
			while (rs.next()) {
				Ordine ordine = new Ordine();
				ordine.setId(rs.getInt("id"));
				ordine.setUserId(rs.getInt("user_id")); 
				ordine.setTotale(rs.getDouble("totale"));
					
				tuttiOrdini.add(ordine);
		}
			
	} finally {
		if (rs != null) rs.close();
		if (preparedStatement != null) preparedStatement.close();
		if (connection != null) connection.close();
	}
			
		return tuttiOrdini;
	}
	
	// Metodo ESCLUSIVO per l'Admin: recupera ordini filtrati
	public synchronized List<Ordine> doRetrieveByFilters(String dataInizio, String dataFine, String idCliente) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<Ordine> ordiniFiltrati = new java.util.ArrayList<>();
			
		// Usiamo "WHERE 1=1" come trucco per poter accodare facilmente gli "AND" successivi
		StringBuilder query = new StringBuilder("SELECT * FROM Ordini WHERE 1=1");
			
		if (dataInizio != null && !dataInizio.isEmpty()) {
			query.append(" AND data_ordine >= ?");
		}
		if (dataFine != null && !dataFine.isEmpty()) {
			query.append(" AND data_ordine <= ?");
		}
		if (idCliente != null && !idCliente.trim().isEmpty()) {
			query.append(" AND user_id = ?");
		}
			
		query.append(" ORDER BY id DESC");

		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(query.toString());
				
			int paramIndex = 1;
			if (dataInizio != null && !dataInizio.isEmpty()) {
				preparedStatement.setString(paramIndex++, dataInizio);
			}
			if (dataFine != null && !dataFine.isEmpty()) {
				preparedStatement.setString(paramIndex++, dataFine);
			}
			if (idCliente != null && !idCliente.trim().isEmpty()) {
				preparedStatement.setInt(paramIndex++, Integer.parseInt(idCliente));
			}
				
			rs = preparedStatement.executeQuery();
			
			while (rs.next()) {
				Ordine ordine = new Ordine();
				ordine.setId(rs.getInt("id"));
				ordine.setUserId(rs.getInt("user_id"));
				ordine.setTotale(rs.getDouble("totale"));
				ordiniFiltrati.add(ordine);
			}
		} finally {
			if (rs != null) rs.close();
			if (preparedStatement != null) preparedStatement.close();
			if (connection != null) connection.close();
		}
			
		return ordiniFiltrati;
	}
}

