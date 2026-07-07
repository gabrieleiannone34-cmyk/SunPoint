package it.unisa.sunpoint.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import it.unisa.sunpoint.model.Utente;

public class UtenteDAO {

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
	
	private static final String TABLE_NAME = "Utenti";
	// Registrazione
	public synchronized void doSave(Utente utente) throws SQLException {
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        
        String insertSQL = "INSERT INTO " + TABLE_NAME + " (nome, cognome, email, password_hash, indirizzo, citta, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try {
        	connection = ds.getConnection();
        	preparedStatement = connection.prepareStatement(insertSQL);
        	preparedStatement.setString(1, utente.getNome());
        	preparedStatement.setString(2, utente.getCognome());
        	preparedStatement.setString(3, utente.getEmail());
        	preparedStatement.setString(4, utente.getPasswordHash());
        	preparedStatement.setString(5, utente.getIndirizzo());
        	preparedStatement.setString(6, utente.getCitta());
        	preparedStatement.setString(7, utente.getRole() != null ? utente.getRole() : "user");
        	preparedStatement.executeUpdate();
        } finally {
        	try {
        		if (preparedStatement != null) preparedStatement.close();
        	} finally {
        		if (connection != null) connection.close();
        	}
        }
	}

	// Metodo di ricerca per Login (Email + Password)
	public synchronized Utente doRetrieveByEmailAndPassword(String email, String passwordHash) throws SQLException {
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Utente utente = null;
        
        String selectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE email = ? AND password_hash = ?";
        
        try {
        	connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, passwordHash);
            
            resultSet = preparedStatement.executeQuery();
            
            if(resultSet.next()) {
            	utente = new Utente();
                utente.setId(resultSet.getInt("id"));
                utente.setNome(resultSet.getString("nome"));
                utente.setCognome(resultSet.getString("cognome"));
                utente.setEmail(resultSet.getString("email"));
                utente.setPasswordHash(resultSet.getString("password_hash"));
                utente.setIndirizzo(resultSet.getString("indirizzo"));
                utente.setCitta(resultSet.getString("citta"));
                utente.setRole(resultSet.getString("role"));
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
        return utente;
	}
	
}

