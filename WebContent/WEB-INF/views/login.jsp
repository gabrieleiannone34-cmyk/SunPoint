<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
<title>Login</title>
</head>
<body>
	<div class="container">
	<h2>Accesso</h2>
	
	<%-- Stampa Errori di login (es. email o password errata) --%>
	<%
		String errore = (String) request.getAttribute("errore");
		if(errore != null){
	%>

		<p>Errore <%= errore %></p>
	<% } %>
	
	<%-- Stampa messaggi di successo per il login --%>
	<%
		String messaggio = (String) request.getAttribute("messaggio");
		if(messaggio != null){
	%>

		<p>Ok:  <%= messaggio %></p>
	<% } %>
	
	<form action="${pageContext.request.contextPath}/LoginServlet" method="POST">
        <label for="email">Email:</label><br>
        <input type="email" id="email" name="email" required><br><br>

        <label for="password">Password:</label><br>
        <input type="password" id="password" name="password" required><br><br>

        <input type="submit" value="Accedi">
    </form>
    <p>Non hai un account? <a href="${pageContext.request.contextPath}/RegistrazioneServlet">Registrati qui</a></p>
    </div>
</body>
</html>