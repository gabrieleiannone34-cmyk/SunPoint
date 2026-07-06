package it.unisa.sunpoint.control;

import java.io.Serializable;

public class Utente implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int id;
    private String nome;
    private String cognome;
    private String email;
    private String passwordHash; 
    private String indirizzo;
    private String citta;
    private String role; // user o admin (default admin)
	
}
