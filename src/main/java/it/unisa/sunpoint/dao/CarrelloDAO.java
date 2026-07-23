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
    public List<ItemCarrello> caricaCarrello(int idUtente) throws SQLException {
        List<ItemCarrello> carrelloSalvato = new ArrayList<>();
        
        // Facciamo una JOIN tra il carrello salvato e i prodotti per prendere tutti i dati
        String query = "SELECT p.*, ec.quantita " +
                       "FROM ElementiCarrello ec " +
                       "JOIN Prodotti p ON ec.prodotto_id = p.id " +
                       "WHERE ec.utente_id = ?";
                       
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
             
            ps.setInt(1, idUtente);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // 1. Ricostruiamo l'oggetto Prodotto
                    Prodotto p = new Prodotto();
                    p.setId(rs.getInt("id"));
                    p.setNome(rs.getString("nome"));
                    p.setPrezzo(rs.getDouble("prezzo"));
                    // (Setta anche gli altri campi come descrizione e image_path se ti servono)
                    
                    // 2. Recuperiamo la quantità che l'utente aveva lasciato nel carrello
                    int quantita = rs.getInt("quantita");
                    
                    // 3. Creiamo l'ItemCarrello unendo il prodotto e la sua quantità
                    ItemCarrello item = new ItemCarrello(p, quantita);
                    
                    // 4. Lo aggiungiamo alla lista
                    carrelloSalvato.add(item);
                }
            }
        }
        return carrelloSalvato;
    }
}
