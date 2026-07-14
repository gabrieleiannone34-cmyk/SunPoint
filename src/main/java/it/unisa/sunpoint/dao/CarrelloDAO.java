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

    // 1. Svuota il carrello salvato nel DB (usato prima di un nuovo salvataggio)
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

    // 2. Salva il carrello della sessione dentro MySQL
    public synchronized void salvaCarrello(int userId, List<Prodotto> carrello) throws SQLException {
        // Prima cancelliamo i vecchi salvataggi per evitare doppioni
        svuotaCarrelloDB(userId);

        // Se il carrello è vuoto, abbiamo finito
        if (carrello == null || carrello.isEmpty()) return;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        // Inseriamo ogni prodotto con quantità 1 (se l'utente ne ha 2 uguali, farà 2 righe separate, perfetto per la tua List)
        String insertSQL = "INSERT INTO ElementiCarrello (user_id, prodotto_id, quantita) VALUES (?, ?, 1)";

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(insertSQL);

            for (Prodotto p : carrello) {
                preparedStatement.setInt(1, userId);
                preparedStatement.setInt(2, p.getId());
                preparedStatement.executeUpdate();
            }
        } finally {
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) connection.close();
        }
    }

    // 3. Recupera il carrello da MySQL e lo ritrasforma in List<Prodotto> al Login
    public synchronized List<Prodotto> caricaCarrello(int userId) throws SQLException {
        List<Prodotto> carrelloSalvato = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        String selectSQL = "SELECT prodotto_id FROM ElementiCarrello WHERE user_id = ?";

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setInt(1, userId);
            rs = preparedStatement.executeQuery();

            ProdottoDAO prodottoDAO = new ProdottoDAO();

            // Per ogni ID prodotto trovato nel database...
            while (rs.next()) {
            	int productId = rs.getInt("prodotto_id");
                
                // ...usiamo il ProdottoDAO per recuperare tutte le info (nome, prezzo, ecc.)
                Prodotto p = prodottoDAO.doRetrieveById(productId); 
                if (p != null) {
                    carrelloSalvato.add(p);
                }
            }
        } finally {
            if (rs != null) rs.close();
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) connection.close();
        }
        return carrelloSalvato;
    }
}
