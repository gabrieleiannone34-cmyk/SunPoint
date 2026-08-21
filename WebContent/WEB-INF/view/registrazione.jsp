<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/style/style.css">
<title>Registrazione</title>
</head>
<body>
	<div class="container">
	<h2>Registrazione</h2>
	
	<%
		String errore = (String) request.getAttribute("errore");
		if(errore != null) {
	%>
		<p> ERRORE: <%= errore %></p>
	<% } %>
	<form id="formRegistrazione"action="${pageContext.request.contextPath}/RegistrazioneServlet" method="POST" onsubmit="return validaFormRegistrazione()">
		<label for="nome">Nome:</label><br>
		<input type="text" id="nome" name="nome" required><br>
		
		<label for="cognome">Cognome:</label><br>
		<input type="text" id="cognome" name="cognome" required><br>
		
		<label for="email">Email:</label><br>
		<input type="email" id="email" name="email" onkeyup="checkEmail()" required><br>
		<span id="emailMessage" class="regstyle"></span>
		<br>
		<label for="password">Password:</label><br>
		<input type="password" id="password" name="password" onchange="validateFormElem(this, passwordPattern, document.getElementById('passwordError'), passwordErrorMessage)" required><br>
		<span id="passwordError" class="regstyle"></span><br>
		
		<label for="indirizzo">Indirizzo:</label><br>
		<input type="text" id="indirizzo" name="indirizzo" required><br>
		
		<label for="citta">Città:</label><br>
		<input type="text" id="citta" name="citta" required><br>
		
		<input type="submit" value="Registrati">
	</form>
	</div>
    <script>
        var contextPath = "${pageContext.request.contextPath}";
    </script>
    
    <script src="${pageContext.request.contextPath}/script/registrazione.js"></script>
</body>
</html>