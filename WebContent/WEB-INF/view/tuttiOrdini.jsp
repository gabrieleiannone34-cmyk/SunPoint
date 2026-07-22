<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.sunpoint.model.Ordine" %>
<%
    List<Ordine> ordini = (List<Ordine>) request.getAttribute("tuttiOrdini");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/style/style.css">
<title>Gestione Ordini - Area Admin</title>
</head>
<body>
	<div class="container">
    <h2>Pannello di Controllo - Tutti gli Ordini</h2>
    <a href="${pageContext.request.contextPath}/index.jsp">Torna alla Home</a>
    <br>
    <div>
        <h3>Filtra Ordini</h3>
        <form action="${pageContext.request.contextPath}/TuttiOrdiniServlet" method="GET">
            <label for="dataInizio">Da (Data):</label>
            <input type="date" id="dataInizio" name="dataInizio">
            
            <label for="dataFine">A (Data):</label>
            <input type="date" id="dataFine" name="dataFine">
            
            <label for="idCliente">ID Cliente:</label>
            <input type="number" id="idCliente" name="idCliente" placeholder="Es. 3" min="1">
            
            <input type="submit" value="Cerca">
            <a href="${pageContext.request.contextPath}/TuttiOrdiniServlet">Resetta Filtri</a>
        </form>
    </div>
	</div>
    <% if (ordini != null && !ordini.isEmpty()) { %>
        <table>
            <tr>
                <th>Numero Scontrino</th>
                <th>ID Cliente</th>
                <th>Totale Pagato</th>
            </tr>

            <% for(Ordine o : ordini) { %>
                <tr>
                    <td># <%= o.getId() %></td>
                    <td>Cliente n° <%= o.getUserId() %></td>
                    <td>€ <%= o.getTotale() %></td>
                </tr>
            <% } %>
        </table>
    <% } else { %>
        <p>Nessun ordine effettuato nel negozio per ora.</p>
    <% } %>

</body>
</html>