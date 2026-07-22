// 1. Inizializzazione compatibile
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
                    if (this.status == 0) {
                        alert("Problemi nell'esecuzione della richiesta: nessuna risposta ricevuta nel tempo limite");
                    } else {
                        alert("Problemi nell'esecuzione della richiesta:\n" + this.statusText);
                    }
                    return null;
                }
            }
        };

        setTimeout(function () {
            if (request.readyState < 4) {
                request.abort();
            }
        }, 15000);

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

// 3. La Funzione di Callback
function handleEmailResponse(request) {
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

// 4. La funzione legata all'evento
function checkEmail() {
    var emailInput = document.getElementById("email").value;
    var messageSpan = document.getElementById("emailMessage");

    if (emailInput.length === 0) {
        messageSpan.innerHTML = "";
        return;
    }

    // MODIFICA CRUCIALE: Usiamo la variabile globale contextPath invece di JSP
    var url = contextPath + "/VerificaEmailServlet";
    var params = "email=" + encodeURIComponent(emailInput);
    
    loadAjaxDoc(url, "GET", params, handleEmailResponse);
}