<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.sunpoint.model.Prodotto" %>
<%@ page import="it.unisa.sunpoint.model.Utente" %>
<%
    // Recupero della lista inviata dalla CatalogoServlet
    List<Prodotto> prodotti = (List<Prodotto>) request.getAttribute("prodotti");
    
    // Recupero dell'utente per i controlli Admin
    Utente utenteLoggato = (Utente) session.getAttribute("utenteLoggato");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Catalogo - SunPoint</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <jsp:include page="/header.jsp" />
        
        <h2>La nostra Collezione di Occhiali da Sole</h2>
        
        <% if (prodotti != null && !prodotti.isEmpty()) { %>
            
            <%-- INIZIO GRIGLIA PRODOTTI --%>
            <div class="catalogo-grid">
                <% for(Prodotto p : prodotti) { %>
                    
                    <%-- SINGOLA SCHEDA (CARD) --%>
                    <div class="prodotto-card">
                        
                        <img src="images/<%= p.getImagePath() %>" alt="<%= p.getNome() %>" class="prodotto-img">
                        
                        <div class="prodotto-titolo"><%= p.getNome() %></div>
                        <div class="prodotto-descrizione"><%= p.getDescrizione() %></div>
                        <div class="prodotto-prezzo">€ <%= p.getPrezzo() %></div>
                        
                        <%-- FORM PER AGGIUNGERE AL CARRELLO --%>
                        <form action="${pageContext.request.contextPath}/AggiungiCarrelloServlet" method="POST">
                            <input type="hidden" name="id" value="<%= p.getId() %>">
                            <button type="submit" class="btn-carrello">Aggiungi al Carrello</button>
                        </form>

                        <%-- OPZIONI PER L'ADMIN --%>
                        <% if (utenteLoggato != null && "admin".equals(utenteLoggato.getRole())) { %>
                            <div class="admin-card-actions">
                                <strong>Opzioni Admin:</strong><br>
                                <a href="${pageContext.request.contextPath}/ModificaProdottoServlet?id=<%= p.getId() %>">Modifica</a> |
                                <a href="${pageContext.request.contextPath}/CancellaProdottoServlet?id=<%= p.getId() %>">Elimina</a>
                            </div>
                        <% } %>
                        
                    </div>
                <% } %>
            </div>
       
            
        <% } else { %>
            <p>Il catalogo è attualmente vuoto. Torna a trovarci presto!</p>
        <% } %>
        
    </div>
</body>
</html>