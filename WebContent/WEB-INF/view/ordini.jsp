<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.sunpoint.model.Ordine" %>
<%
	List<Ordine> ordini = (List<Ordine>) request.getAttribute("storicoOrdini");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/style/style.css">
<title>I Miei Ordini</title>
</head>
<body>
	<div class="container">
	<h2>Storico Ordini</h2>
	<a href="${pageContext.request.contextPath}/index.jsp">Torna alla Home</a>
	<br>
	
	<% if (ordini != null && !ordini.isEmpty()) { %>
		<table>
			<tr>
				<th>Numero Ordine</th>
				<th>Totale Pagato</th>
			</tr>
			
			<% for(Ordine o: ordini) { %>
				<tr>
					<td># <%= o.getId() %></td>
					<td>€ <%= o.getTotale() %></td>
				</tr>
			<% } %>
		</table>
		
	<% } else { %>
        <p>Non hai ancora effettuato nessun ordine.</p>
        <p>Vai al <a href="${pageContext.request.contextPath}/CatalogoServlet">Catalogo</a> per iniziare i tuoi acquisti!</p>
    <% } %>
    </div>
    <jsp:include page="/footer.jsp" />
</body>
</html>