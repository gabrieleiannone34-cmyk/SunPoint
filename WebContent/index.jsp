<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="it.unisa.sunpoint.model.Utente" %>
<%
    // Recuperiamo l'utente dalla sessione per personalizzare la dashboard
    Utente utente = (Utente) session.getAttribute("utenteLoggato");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>SunPoint - Home</title>
    <!-- Collegamento al nuovo CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <jsp:include page="/header.jsp" />
        
        <h2>Benvenuto su SunPoint!</h2>
        <p>Scopri la nostra nuova collezione di occhiali da sole.</p>
        
        <%-- Mostriamo pannelli diversi in base al ruolo dell'utente --%>
        <% if (utente != null && "admin".equals(utente.getRole())) { %>
            <div style="background-color: #f9f9f9; padding: 20px; border-left: 4px solid #000; margin-top: 30px;">
                <h3>Pannello di Controllo Admin</h3>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/inserisciProdotto.jsp">Inserisci un Nuovo Occhiale nel Catalogo</a></li>
                    <li><a href="${pageContext.request.contextPath}/TuttiOrdiniServlet">Visualizza e Filtra Tutti gli Ordini</a></li>
                </ul>
            </div>
            
        <% } else if (utente != null && "cliente".equals(utente.getRole())) { %>
            <div style="background-color: #f9f9f9; padding: 20px; border-left: 4px solid #555; margin-top: 30px;">
                <h3>Area Personale</h3>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/StoricoOrdiniServlet">Visualizza lo Storico dei Miei Ordini</a></li>
                </ul>
            </div>
        <% } %>
        
    </div>
</body>
</html>