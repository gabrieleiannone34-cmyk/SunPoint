<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.sunpoint.model.ItemCarrello" %>
<%
	//1. Apriamo il cassetto della sessione per prendere la lista degli occhiali
	List<ItemCarrello> carrello = (List<ItemCarrello>) session.getAttribute("carrello");

	// 2. Prepariamo una variabile per sommare i prezzi
	double totale = 0.0;
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/style/style.css">
<title>Il tuo Carrello</title>
</head>
<body>
	<div class="container">
	<h2>Il tuo Carrello</h2>
	<a href="${pageContext.request.contextPath}/CatalogoServlet">Continua lo shopping</a> | <a href="${pageContext.request.contextPath}/index.jsp">Torna alla Home</a>
	
	<%-- Mostrinamo gli elementi del carrello (Se non è vuoto) tramite una tabella --%>
	<% if (carrello != null && !carrello.isEmpty()) { %>
	
		<table>
			<tr>
				<th>Modello Occhiali</th>
				<th>Prezzo</th>
				<th>Quantità</th>
                <th>Totale Riga</th>
                <th>Azioni</th>
			</tr>
				
			<%-- Scorriamo gli occhiali nel carrello uno per uno --%>
            <% for(ItemCarrello item : carrello) { 
            	double subtotale = item.getProdotto().getPrezzo() * item.getQuantita();
                totale += subtotale;
            %>
                <%-- Riga per il singolo prodotto --%>
                <tr>
                    <td><%= item.getProdotto().getNome() %></td>
                    <td>€ <%= item.getProdotto().getPrezzo() %></td>
                    <td><form action="<%= request.getContextPath() %>/GestioneCarrelloServlet" method="POST" style="display: inline-block;">
                            <input type="hidden" name="idProdotto" value="<%= p.getId() %>">
                            <button type="submit" name="azione" value="diminuisci" class="btn-quantita">-</button>  
                            <input type="text" value="<%= "item.getQuantita()" %>" readonly class="input-quantita">
                            <button type="submit" name="azione" value="aumenta" class="btn-quantita">+</button>
                        </form>
                    </td>
                    
                    <td>€ <%= String.format("%.2f", subtotale) %></td>
                    
                    <td><form action="<%= request.getContextPath() %>/RimuoviCarrelloServlet" method="POST">
                    	<input type="hidden" name="idProdotto" value="<%= item.getProdotto().getId() %>">
                    		<button type="submit">Rimuovi</button>
                    	</form> 
                   	</td>
                </tr>
            <% } %>
            
        </table>
        
        <h3>Totale da pagare: € <%= totale %></h3>
        <form action="${pageContext.request.contextPath}/CheckoutServlet" method="GET">
            <input type="submit" value="Procedi al Checkout">
        </form>
        <br>
        
        <a href="${pageContext.request.contextPath}/SvuotaCarrelloServlet">Svuota il Carrello</a>
        
	<%-- Se il carrello non esiste o è vuoto --%>
    <% } else { %>
        <p>Il tuo carrello è vuoto. Torna al catalogo per aggiungere un paio di occhiali!</p>
    <% } %>
    </div>
</body>
</html>