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

import it.unisa.sunpoint.model.Prodotto;


public class ProdottoDAO {

	private static DataSource ds;
	
	static {
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			ds = (DataSource) envCtx.lookup("jdbc/sunpoint");
		} catch (NamingException e) {
			System.out.println("Errore JNDI: " + e.getMessage());
		}
	}
	
	private static final String TABLE_NAME = "Prodotti";
	
	// Metodo utilizzato per estrarre tutti i prodotti dal database
	public synchronized List<Prodotto> doRetrieveAll() throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		// Creiamo una lista vuota che riempiremo con i prodotti
		List<Prodotto> prodotti = new ArrayList<>();
		
		String selectSQL = "SELECT * FROM " + TABLE_NAME;
		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			resultSet = preparedStatement.executeQuery();
			
			//Scorriamo tutte le righe trovate nel database
			while (resultSet.next()) {
                Prodotto bean = new Prodotto();
                bean.setId(resultSet.getInt("id"));
                bean.setNome(resultSet.getString("nome"));
                bean.setDescrizione(resultSet.getString("descrizione"));
                bean.setPrezzo(resultSet.getDouble("prezzo"));
                bean.setQuantita(resultSet.getInt("quantita"));
                bean.setImagePath(resultSet.getString("image_path"));
                
                // Aggiungiamo il singolo prodotto alla lista
                prodotti.add(bean);
			}
		} finally {
			try {
				if (resultSet != null) resultSet.close();
			} finally {
				try {
					if (preparedStatement != null) preparedStatement.close();
				} finally {
					if (connection != null) connection.close();
				}
			}
		}
		return prodotti;
	}
}
