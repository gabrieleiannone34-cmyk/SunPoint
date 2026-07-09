<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="it.unisa.sunpoint.model.Utente" %>
<%
	//Aprimalo la sessione e cerchiamo l'attributo utenteLoggato
	Utente utenteLoggato = (Utente) session.getAttribute("utenteLoggato");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Home</title>
</head>
<body>
	<h1>SunPoint</h1>
	
	<% if(utenteLoggato != null) { %>
		<p> <%= utenteLoggato.getNome()%> <%= utenteLoggato.getCognome()%>Ben tornato/a</p>
		
		<% if ("admin".equals(utenteLoggato.getRole())) { %>
            <p><em>(Hai i privilegi di Amministratore)</em></p>
        <% } %>
        <br>
        <a href="LogoutServlet"><button>Esci (Logout)</button></a>
        
	<% } else { %>
		<p>Non sei autenticato. Accedi per acquistare prodotti.</p>
        <a href="LoginServlet">Accedi</a> | <a href="RegistrazioneServlet">Registrati</a>
    <% } %>
</body>
</html>