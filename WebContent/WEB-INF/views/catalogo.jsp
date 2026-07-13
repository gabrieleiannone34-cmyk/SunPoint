<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.sunpoint.model.Prodotto" %>
<%
    // Recuperiamo la lista di prodotti che la Servlet ci ha inviato
    List<Prodotto> prodotti = (List<Prodotto>) request.getAttribute("catalogo");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Catalogo Occhiali - SunPoint</title>
</head>
<body>
    <h2>La nostra Collezione di Occhiali da Sole</h2>
    <a href="${pageContext.request.contextPath}/index.jsp">Torna alla Home</a>
    <a href="${pageContext.request.contextPath}/carrello.jsp">Vai al Carrello </a>

    <%-- Controllo: la lista esiste e ha almeno un elemento? --%>
    <% if (prodotti != null && !prodotti.isEmpty()) { %>
        
        <%-- Usiamo una tabella HTML base per impaginare i prodotti --%>
        <table>
            <tr>
                <th>Nome Modello</th>
                <th>Descrizione</th>
                <th>Prezzo</th>
                <th>Disponibilità</th>
                <th>Azione</th>
            </tr>
            
            <%-- Inizio del ciclo FOR --%>
            <% for(Prodotto p : prodotti) { %>
                
                <tr>
                    <td><%= p.getNome() %></td>
                    <td><%= p.getDescrizione() %></td>
                    <td>€ <%= p.getPrezzo() %></td>
                    <td><%= p.getQuantita() %> pz</td>
                    <td>
                        <form action="CarrelloServlet" method="POST">
                            <input type="hidden" name="idProdotto" value="<%= p.getId() %>">
                            <input type="submit" value="Aggiungi al Carrello">
                        </form>
                    </td>
                </tr>
                
            <% } // Fine del ciclo FOR %>
            
        </table>

    <% } else { %>
        <p>Al momento non ci sono occhiali nel catalogo.</p>
    <% } %>

</body>
</html>