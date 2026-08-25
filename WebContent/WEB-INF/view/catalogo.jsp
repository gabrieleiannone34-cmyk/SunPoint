<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.sunpoint.model.Prodotto" %>
<%@ page import="it.unisa.sunpoint.model.Utente" %>
<%
    List<Prodotto> prodotti = (List<Prodotto>) request.getAttribute("catalogo");

    Utente utenteLoggato = (Utente) session.getAttribute("utenteLoggato");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Catalogo - SunPoint</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style/style.css">
</head>
<body>
    <div class="container">
        <jsp:include page="/header.jsp" />
        
        <h2>La nostra Collezione di Occhiali da Sole</h2>
        
        <div class="filtri-catalogo">
    		<form action="${pageContext.request.contextPath}/CatalogoServlet" method="GET">
        		<label for="minPrice">Prezzo Min (€):</label>
        		<input type="number" name="minPrice" id="minPrice" step="0.01" min="0" placeholder="Es. 10">
        
        		<label for="maxPrice">Prezzo Max (€):</label>
        		<input type="number" name="maxPrice" id="maxPrice" step="0.01" min="0" placeholder="Es. 100">
        
        		<input type="submit" value="Filtra">
        		<a href="${pageContext.request.contextPath}/CatalogoServlet">Resetta Filtri</a>
    		</form>
		</div>
		
        <% 
            String errore = request.getParameter("errore");
            if ("esaurito".equals(errore)) { 
        %>
            <div class="error">
                 Impossibile aggiungere: le quantità richieste superano le scorte in magazzino!
            </div>
        <%  } %>
        
        <% if (prodotti != null && !prodotti.isEmpty()) { %>
            
            <div class="catalogo-grid">
                <% for(Prodotto p : prodotti) { %>
                    
                    <div class="prodotto-card">
                        
                        <img src="images/<%= p.getImagePath() %>" alt="<%= p.getNome() %>" class="prodotto-img">
                        
                        <div class="prodotto-titolo"><%= p.getNome() %></div>
                        <div class="descrizione-occhiale"><%= p.getDescrizione() %></div>
                        <div class="prodotto-prezzo">€ <%= p.getPrezzo() %></div>
                        
                        <form action="${pageContext.request.contextPath}/CarrelloServlet" method="POST">
                            <input type="hidden" name="idProdotto" value="<%= p.getId() %>">
                            <button type="submit" class="btn-carrello">Aggiungi al Carrello</button>
                        </form>

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
    <jsp:include page="/footer.jsp" />
</body>
</html>