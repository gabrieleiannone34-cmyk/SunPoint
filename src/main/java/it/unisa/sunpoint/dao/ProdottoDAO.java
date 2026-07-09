package it.unisa.sunpoint.dao;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


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
	
}
