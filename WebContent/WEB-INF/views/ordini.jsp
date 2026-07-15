<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.sunpoint.model.Ordine" %>
<%
	//Recuperiamo la lista degli ordini preparata dalla Servlet
	List<Ordine> ordini = (List<Ordine>) request.getAttribute("storicoOrdini");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>I Miei Ordini</title>
</head>
<body>
	<h2>Storico Ordini</h2>
	<a href="${pageContext.request.contextPath}/index.jsp">Torna alla Home</a>
	<br>
	
	<%-- Se la lista esiste e non è vuota, mostriamo la tabella --%>
	<% if (ordini != null && !ordini.isEmpty()) { %>
		<table>
			<tr>
				<th>Numero Ordine</th>
				<th>Data Ordine</th>
				<th>Totale Pagato</th>ù
			</tr>
			
			<%-- Cicliamo su tutti gli ordini --%>
			<% for(Ordine o: ordini) { %>
				<tr>
					<td># <%= o.getId() %></td>
					<td>Data: <%= o.getDataOrdine() %></td>
					<td>€ <%= o.getTotale() %></td>
				</tr>
			<% } %>
		</table>
		
	<%-- Se invece l'utente non ha mai comprato nulla --%>
	<% } else { %>
        <p>Non hai ancora effettuato nessun ordine.</p>
        <p>Vai al <a href="${pageContext.request.contextPath}/CatalogoServlet">Catalogo</a> per iniziare i tuoi acquisti!</p>
    <% } %>
</body>
</html>