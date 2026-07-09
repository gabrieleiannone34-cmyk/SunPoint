package it.unisa.sunpoint.model;

import java.io.Serializable;

public class Prodotto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String nome;
	private String descrizione;
	private double prezzo;
	private int quantita;
	private String imagePath; //Percorso dell'immagine
	
	public Prodotto() {	
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

    public String getDescrizione() { 
    	return descrizione; 
    }
    public void setDescrizione(String descrizione) { 
    	this.descrizione = descrizione; 
    }

    public double getPrezzo() { 
    	return prezzo;
    }
    public void setPrezzo(double prezzo) { 
    	this.prezzo = prezzo; 
    }

    public int getQuantita() { 
    	return quantita; 
    }
    public void setQuantita(int quantita) { 
    	this.quantita = quantita; 
    }

    public String getImagePath() { 
    	return imagePath; 
    }
    public void setImagePath(String imagePath) { 
    	this.imagePath = imagePath; 
    }
}
