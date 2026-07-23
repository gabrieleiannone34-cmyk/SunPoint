package it.unisa.sunpoint.model;

public class ItemCarrello {
	private Prodotto prodotto;
    private int quantita;
	
    public ItemCarrello(Prodotto prodotto, int quantita) {
		super();
		this.prodotto = prodotto;
		this.quantita = quantita;
	}

	public Prodotto getProdotto() {
		return prodotto;
	}

	public void setProdotto(Prodotto prodotto) {
		this.prodotto = prodotto;
	}

	public int getQuantita() {
		return quantita;
	}

	public void setQuantita(int quantita) {
		this.quantita = quantita;
	}
    
	public void incrementaQuantita() {
        this.quantita++;
    }

    public void decrementaQuantita() {
        if (this.quantita > 1) {
            this.quantita--;
        }
    }
}
