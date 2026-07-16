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
	// Metodo per estrarre un singolo prodotto usando il suo ID
	public synchronized Prodotto doRetrieveById(int id) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Prodotto bean = null; // Partiamo con un occhiale vuoto

        String selectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setInt(1, id); // Sostituiamo il "?" con l'ID richiesto
            resultSet = preparedStatement.executeQuery();

            // Usiamo 'if' e non 'while' perché l'ID è unico, troveremo massimo una riga
            if (resultSet.next()) {
                bean = new Prodotto();
                bean.setId(resultSet.getInt("id"));
                bean.setNome(resultSet.getString("nome"));
                bean.setDescrizione(resultSet.getString("descrizione"));
                bean.setPrezzo(resultSet.getDouble("prezzo"));
                bean.setQuantita(resultSet.getInt("quantita"));
                bean.setImagePath(resultSet.getString("image_path"));
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
        return bean; // Restituisce l'occhiale trovato (o null se non esiste)
    }
	
	// Metodo per scalare di 1 la quantità di un prodotto venduto
    public synchronized void aggiornaQuantita(int idProdotto) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        // La query UPDATE: prende la quantità attuale e le sottrae 1
        String updateSQL = "UPDATE Prodotti SET quantita = quantita - 1 WHERE id = ?";

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(updateSQL);
            
            // Sostituiamo il "?" con l'ID dell'occhiale venduto
            preparedStatement.setInt(1, idProdotto);

            // Eseguiamo la modifica nel database
            preparedStatement.executeUpdate();

        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
            } finally {
                if (connection != null) connection.close();
            }
        }
    }
 // Metodo ESCLUSIVO per l'Admin: inserisce un nuovo prodotto nel catalogo
 	public synchronized void doSave(Prodotto prodotto) throws SQLException {
 		Connection connection = null;
 		PreparedStatement preparedStatement = null;

 		String insertSQL = "INSERT INTO Prodotti (nome, descrizione, prezzo, quantita, image_path) VALUES (?, ?, ?, ?, ?)";

 		try {
 			connection = ds.getConnection();
 			preparedStatement = connection.prepareStatement(insertSQL);

 			preparedStatement.setString(1, prodotto.getNome());
 			preparedStatement.setString(2, prodotto.getDescrizione());
 			preparedStatement.setDouble(3, prodotto.getPrezzo());
 			preparedStatement.setInt(4, prodotto.getQuantita());
 			preparedStatement.setString(5, prodotto.getImagePath());

 			preparedStatement.executeUpdate();

 		} finally {
 			if (preparedStatement != null) preparedStatement.close();
 			if (connection != null) connection.close();
 		}
 	}
 	//Metodo ESCLUSIVO per admin: Metodo per ELIMINARE un prodotto dal catalogo
 	public synchronized boolean doDelete(int id) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		int result = 0;

		String deleteSQL = "DELETE FROM Prodotti WHERE id = ?";

		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(deleteSQL);
			preparedStatement.setInt(1, id);

			result = preparedStatement.executeUpdate();
		} finally {
			if (preparedStatement != null) preparedStatement.close();
			if (connection != null) connection.close();
		}
		return (result != 0);
	}
 	//Metodo ESCLUSIVO per admin: Metodo per AGGIORNARE i dati di un prodotto esistente
 	public synchronized void doUpdate(Prodotto prodotto) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		String updateSQL = "UPDATE Prodotti SET nome = ?, descrizione = ?, prezzo = ?, quantita = ?, image_path = ? WHERE id = ?";

		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(updateSQL);

			preparedStatement.setString(1, prodotto.getNome());
			preparedStatement.setString(2, prodotto.getDescrizione());
			preparedStatement.setDouble(3, prodotto.getPrezzo());
			preparedStatement.setInt(4, prodotto.getQuantita());
			preparedStatement.setString(5, prodotto.getImagePath());
			preparedStatement.setInt(6, prodotto.getId()); // La clausola WHERE!

			preparedStatement.executeUpdate();
		} finally {
			if (preparedStatement != null) preparedStatement.close();
			if (connection != null) connection.close();
		}
	}
}
