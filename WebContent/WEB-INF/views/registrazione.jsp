<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Registrazione</title>
</head>
<body>
	<h2>Registrazione</h2>
	
	<%-- Gli errori vengono stampati qui --%>
	<%
		String errore = (String) request.getAttribute("errore");
		if(errore != null) {
	%>
		<p> ERRORE: <%= errore %></p>
	<% } %>
	<form action="${pageContext.request.contextPath}/RegistrazioneServlet" method="POST">
		<label for="nome">Nome:</label><br>
		<input type="text" id="nome" name="nome" required><br>
		
		<label for="cognome">Cognome:</label><br>
		<input type="text" id="cognome" name="cognome" required><br>
		
		<label for="email">Email:</label><br>
		<input type="email" id="email" name="email" required><br>
		
		<label for="password">Password:</label><br>
		<input type="password" id="password" name="password" required><br>
		
		<label for="indirizzo">Indirizzo:</label><br>
		<input type="text" id="indirizzo" name="indirizzo" required><br>
		
		<label for="citta">Città:</label><br>
		<input type="text" id="citta" name="citta" required><br>
		
		<input type="submit" value="Registrati">
	</form>
</body>
</html>