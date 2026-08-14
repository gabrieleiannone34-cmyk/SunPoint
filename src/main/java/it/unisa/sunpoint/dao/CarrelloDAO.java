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
import it.unisa.sunpoint.model.Prodotto;

public class CarrelloDAO {

    private static DataSource ds;

    static {
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            ds = (DataSource) envCtx.lookup("jdbc/sunpoint");
        } catch (NamingException e) {
            System.out.println("Errore DataSource CarrelloDAO: " + e.getMessage());
        }
    }

    
    public synchronized void svuotaCarrelloDB(int userId) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String deleteSQL = "DELETE FROM ElementiCarrello WHERE user_id = ?";

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
        } finally {
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) connection.close();
        }
    }

    
    public synchronized void salvaCarrello(int userId, List<ItemCarrello> carrello) throws SQLException {
        
        svuotaCarrelloDB(userId);

        if (carrello == null || carrello.isEmpty()) return;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        
        
        String insertSQL = "INSERT INTO ElementiCarrello (user_id, prodotto_id, quantita) VALUES (?, ?, ?)";

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(insertSQL);

            
            for (ItemCarrello item : carrello) {
                preparedStatement.setInt(1, userId);
                preparedStatement.setInt(2, item.getProdotto().getId()); 
                preparedStatement.setInt(3, item.getQuantita());         
                preparedStatement.executeUpdate();
            }
        } finally {
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) connection.close();
        }
    }

    
    public List<ItemCarrello> caricaCarrello(int idUtente) throws SQLException {
        List<ItemCarrello> carrelloSalvato = new ArrayList<>();
        
        String query = "SELECT p.*, ec.quantita AS quantita_carrello FROM ElementiCarrello ec JOIN Prodotti p ON ec.prodotto_id = p.id WHERE ec.user_id = ?";
                       
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
             
            ps.setInt(1, idUtente);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    
                    Prodotto p = new Prodotto();
                    p.setId(rs.getInt("id"));
                    p.setNome(rs.getString("nome"));
                    p.setPrezzo(rs.getDouble("prezzo"));

                    int quantita = rs.getInt("quantita");
                    
                    int quantitaNelCarrello = rs.getInt("quantita_carrello");
					
					ItemCarrello item = new ItemCarrello(p, quantitaNelCarrello);
                    
                   
                    carrelloSalvato.add(item);
                }
            }
        }
        return carrelloSalvato;
    }
}
