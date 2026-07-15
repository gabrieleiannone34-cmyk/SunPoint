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
<title>Gestione Ordini - Area Admin</title>
</head>
<body>
    <h2>Pannello di Controllo - Tutti gli Ordini</h2>
    <a href="${pageContext.request.contextPath}/index.jsp">Torna alla Home</a>
    <br><br>

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