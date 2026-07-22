<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
<title>Registrazione</title>
</head>
<body>
	<div class="container">
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
		<span id="emailMessage" class="emailstyle"></span>
		
		<label for="password">Password:</label><br>
		<input type="password" id="password" name="password" required><br>
		
		<label for="indirizzo">Indirizzo:</label><br>
		<input type="text" id="indirizzo" name="indirizzo" required><br>
		
		<label for="citta">Città:</label><br>
		<input type="text" id="citta" name="citta" required><br>
		
		<input type="submit" value="Registrati">
	</form>
	</div>
	<script>
    // 1. Inizializzazione compatibile (Esattamente come nelle slide)
    function createXMLHttpRequest() {
        var request;
        try {
            request = new XMLHttpRequest();
        } catch (e) {
            try {
                request = new ActiveXObject("Msxml2.XMLHTTP");
            } catch (e) {
                try {
                    request = new ActiveXObject("Microsoft.XMLHTTP");
                } catch (e) {
                    alert("Il browser non supporta AJAX");
                    return null;
                }
            }
        }
        return request;
    }

    // 2. La funzione "core" riutilizzabile del prof
    function loadAjaxDoc(url, method, params, cFunction) {
        var request = createXMLHttpRequest();
        if (request) {
            request.onreadystatechange = function() {
                if (this.readyState == 4) {
                    if (this.status == 200) {
                        cFunction(this);
                    } else {
                        if (this.status == 0) { // Timeout o abort()
                            alert("Problemi nell'esecuzione della richiesta: nessuna risposta ricevuta nel tempo limite");
                        } else {
                            alert("Problemi nell'esecuzione della richiesta:\n" + this.statusText);
                        }
                        return null;
                    }
                }
            };

            // Timeout di 15 secondi come da direttive
            setTimeout(function () {
                if (request.readyState < 4) {
                    request.abort();
                }
            }, 15000);

            // Costruzione e invio richiesta
            if (method.toLowerCase() == "get") {
                if (params) {
                    request.open("GET", url + "?" + params, true);
                } else {
                    request.open("GET", url, true);
                }
                request.setRequestHeader("Connection", "close");
                request.send(null);
            }
        }
    }

    // 3. La Funzione di Callback (Elaborazione JSON)
    function handleEmailResponse(request) {
        // Parsing della stringa JSON restituita dalla Servlet
        var response = JSON.parse(request.responseText);
        var messageSpan = document.getElementById("emailMessage");

        if (response.result === "occupata") {
            messageSpan.style.color = "#d9534f";
            messageSpan.innerHTML = "Attenzione: Email già in uso!";
        } else if (response.result === "disponibile") {
            messageSpan.style.color = "#5cb85c";
            messageSpan.innerHTML = "Email disponibile";
        }
    }

    // 4. La funzione legata all'evento onkeyup sull'input
    function checkEmail() {
        var emailInput = document.getElementById("email").value;
        var messageSpan = document.getElementById("emailMessage");

        if (emailInput.length === 0) {
            messageSpan.innerHTML = "";
            return;
        }

        var url = "${pageContext.request.contextPath}/VerificaEmailServlet";
        var params = "email=" + encodeURIComponent(emailInput);
        
        // Chiamata alla funzione generica
        loadAjaxDoc(url, "GET", params, handleEmailResponse);
    }
</script>
</body>
</html>