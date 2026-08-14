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
	

	public synchronized List<Prodotto> doRetrieveAll() throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		List<Prodotto> prodotti = new ArrayList<>();
		
		String selectSQL = "SELECT * FROM " + TABLE_NAME;
		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
                Prodotto bean = new Prodotto();
                bean.setId(resultSet.getInt("id"));
                bean.setNome(resultSet.getString("nome"));
                bean.setDescrizione(resultSet.getString("descrizione"));
                bean.setPrezzo(resultSet.getDouble("prezzo"));
                bean.setQuantita(resultSet.getInt("quantita"));
                bean.setImagePath(resultSet.getString("image_path"));
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

	public synchronized Prodotto doRetrieveById(int id) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Prodotto bean = null; 

        String selectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setInt(1, id); 
            resultSet = preparedStatement.executeQuery();

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
        return bean; 
    }

    public synchronized void aggiornaQuantita(int idProdotto) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        String updateSQL = "UPDATE Prodotti SET quantita = quantita - 1 WHERE id = ?";

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(updateSQL);
            
            preparedStatement.setInt(1, idProdotto);

            preparedStatement.executeUpdate();

        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
            } finally {
                if (connection != null) connection.close();
            }
        }
    }

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

 		public synchronized boolean doDelete(int id) throws SQLException {
 			Connection connection = null;
 			PreparedStatement preparedStatement = null;
 			int result = 0;

 			try {
 				connection = ds.getConnection();
 				
 				String deleteCartSQL = "DELETE FROM ElementiCarrello WHERE prodotto_id = ?";
 				preparedStatement = connection.prepareStatement(deleteCartSQL);
 				preparedStatement.setInt(1, id);
 				preparedStatement.executeUpdate();
 				preparedStatement.close(); 
 				
 				String deleteOrdersSQL = "DELETE FROM Articoli_ordinati WHERE product_id = ?";
 				preparedStatement = connection.prepareStatement(deleteOrdersSQL);
 				preparedStatement.setInt(1, id);
 				preparedStatement.executeUpdate();
 				preparedStatement.close(); 
 				

 				String deleteSQL = "DELETE FROM Prodotti WHERE id = ?";
 				preparedStatement = connection.prepareStatement(deleteSQL);
 				preparedStatement.setInt(1, id);
 				
 				result = preparedStatement.executeUpdate();

 			} finally {

 				if (preparedStatement != null) preparedStatement.close();
 				if (connection != null) connection.close();
 			}
 			
 			return (result != 0);
 		}

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
			preparedStatement.setInt(6, prodotto.getId()); 

			preparedStatement.executeUpdate();
		} finally {
			if (preparedStatement != null) preparedStatement.close();
			if (connection != null) connection.close();
		}
	}
}
