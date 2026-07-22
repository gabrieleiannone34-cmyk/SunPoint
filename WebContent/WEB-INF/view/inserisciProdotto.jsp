<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
<title>Area Admin</title>
</head>
<body>
	<div class="container">
	<h2>Pannello di Controllo - Aggiungi Occhiale</h2>
	<p>Inserisci i dettagli del nuovo occhiale da aggiungere al catalogo.</p>
	
	<form action="${pageContext.request.contextPath}/InserisciProdottoServlet" method="POST">
		<label for="nome">Nome Modello:</label><br>
		<input type="text" id="nome" name="nome" required><br><br>
		
		<label for="descrizione">Descrizione:</label><br>
		<textarea id="descrizione" name="descrizione" rows="4" cols="50" required></textarea><br><br>
		
		<label for="prezzo">Prezzo (€):</label><br>
		<input type="number" id="prezzo" name="prezzo" step="0.01" min="0" required><br><br>
		
		<label for="quantita">Quantità in Magazzino:</label><br>
		<input type="number" id="quantita" name="quantita" min="1" required><br><br>
		
		<label for="imagePath">Nome File Immagine (es. occhiale1.jpg):</label><br>
		<input type="text" id="imagePath" name="imagePath" required><br><br>
		
		<input type="submit" value="Salva nel Catalogo">
	</form>

	<br>
	<a href="${pageContext.request.contextPath}/index.jsp">Torna alla Home</a>
	</div>
</body>
</html>