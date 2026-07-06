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
	
    public Utente() {
    }
    
    public int getId() { 
    	return id; 
    	}
    public void setId(int id) { 
    	this.id = id; 
    	}
    
    
    public String getNome() { 
    	return nome; 
    	}
    public void setNome(String nome) { 
    	this.nome = nome; 
    	}

    public String getCognome() { 
    	return cognome; 
    	}
    public void setCognome(String cognome) { 
    	this.cognome = cognome; 
    	}

    public String getEmail() { 
    	return email; 
    	}
    public void setEmail(String email) { 
    	this.email = email; 
    	}

    public String getPasswordHash() { 
    	return passwordHash; 
    	}
    public void setPasswordHash(String passwordHash) { 
    	this.passwordHash = passwordHash; 
    	}

    public String getIndirizzo() { 
    	return indirizzo; 
    	}
    public void setIndirizzo(String indirizzo) { 
    	this.indirizzo = indirizzo; 
    	}

    public String getCitta() { 
    	return citta; 
    	}
    public void setCitta(String citta) { 
    	this.citta = citta; 
    	}

    public String getRole() { 
    	return role; 
    	}
    public void setRole(String role) { 
    	this.role = role; 
    	}
}

