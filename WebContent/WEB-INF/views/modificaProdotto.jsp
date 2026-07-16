<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="it.unisa.sunpoint.model.Prodotto" %>
<%
    // Recuperiamo il prodotto passato dalla Servlet
    Prodotto p = (Prodotto) request.getAttribute("prodottoDaModificare");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Modifica Prodotto</title>
</head>
<body>
	<h2>Pannello di Controllo - Modifica Occhiale</h2>
	
	<form action="${pageContext.request.contextPath}/ModificaProdottoServlet" method="POST">
	    <%-- Nascondiamo l'ID perché ci serve per l'UPDATE ma non va modificato a mano --%>
		<input type="hidden" name="id" value="<%= p.getId() %>">
		
		<label>Nome Modello:</label><br>
		<input type="text" name="nome" value="<%= p.getNome() %>" required><br><br>
		
		<label>Descrizione:</label><br>
		<textarea name="descrizione" rows="4" cols="50" required><%= p.getDescrizione() %></textarea><br><br>
		
		<label>Prezzo (€):</label><br>
		<input type="number" name="prezzo" step="0.01" min="0" value="<%= p.getPrezzo() %>" required><br><br>
		
		<label>Quantità in Magazzino:</label><br>
		<input type="number" name="quantita" min="0" value="<%= p.getQuantita() %>" required><br><br>
		
		<label>Nome File Immagine:</label><br>
		<input type="text" name="imagePath" value="<%= p.getImagePath() %>" required><br><br>
		
		<input type="submit" value="Salva Modifiche">
	</form>

	<br>
	<a href="${pageContext.request.contextPath}/CatalogoServlet">Annulla e torna al Catalogo</a>
</body>
</html>