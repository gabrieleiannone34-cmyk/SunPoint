/* ========================================================
   PARTE 1: MOTORE AJAX (Controllo Email Asincrono)
   ======================================================== */

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

function loadAjaxDoc(url, method, params, cFunction) {
    var request = createXMLHttpRequest();
    if (request) {
        request.onreadystatechange = function() {
            if (this.readyState == 4) {
                if (this.status == 200) {
                    cFunction(this);
                } else {
                    if (this.status == 0) {
                        alert("Problemi nell'esecuzione della richiesta: timeout");
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

function checkEmail() {
    var emailInput = document.getElementById("email").value;
    var messageSpan = document.getElementById("emailMessage");

    if (emailInput.length === 0) {
        messageSpan.innerHTML = "";
        return;
    }

    var url = contextPath + "/VerificaEmailServlet";
    var params = "email=" + encodeURIComponent(emailInput);
    
    loadAjaxDoc(url, "GET", params, handleEmailResponse);
}


/* ========================================================
   PARTE 2: VALIDAZIONE FORM (Stile del Professore)
   ======================================================== */

// 1. Costanti con le Regex 
const passwordPattern = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/;

// 2. Costanti con i messaggi di errore
const passwordErrorMessage = "La password deve essere di almeno 8 caratteri con lettere e numeri.";

// 3. La funzione generica del Professore per colorare i campi
function validateFormElem(formElem, pattern, span, message) {
    if (formElem.value.match(pattern)) {
        formElem.classList.remove("error");
        span.style.color = "black";
        span.innerHTML = "";
        return true;
    } else {
        formElem.classList.add("error");
        span.innerHTML = message;
        span.style.color = "red";
        return false;
    }
}

// 4. Controllo finale quando si preme "Registrati"
function validateRegistrazione() {
    let valid = true;
    let form = document.getElementById("formRegistrazione");

    // Validazione Password
    let spanPassword = document.getElementById("passwordError");
    if (!validateFormElem(form.password, passwordPattern, spanPassword, passwordErrorMessage)) {
        valid = false;
    }

    return valid;
}