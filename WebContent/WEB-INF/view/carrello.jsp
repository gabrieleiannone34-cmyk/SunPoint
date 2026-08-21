<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.sunpoint.model.ItemCarrello" %>
<%
	List<ItemCarrello> carrello = (List<ItemCarrello>) session.getAttribute("carrello");

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
	
	<% 
            String errore = request.getParameter("errore");
            if ("esaurito".equals(errore)) { 
        %>
            <div style="background-color: #ffe6e6; color: #cc0000; padding: 15px; border: 1px solid #cc0000; text-align: center; margin-bottom: 20px; border-radius: 5px; font-weight: bold;">
                 Impossibile aggiungere: le quantità richieste superano le scorte in magazzino!
            </div>
        <% 
            } 
        %>
        
	<a href="${pageContext.request.contextPath}/CatalogoServlet">Continua lo shopping</a> | <a href="${pageContext.request.contextPath}/index.jsp">Torna alla Home</a>
	
	<% if (carrello != null && !carrello.isEmpty()) { %>
	
		<table>
			<tr>
				<th>Modello Occhiali</th>
				<th>Prezzo</th>
				<th>Quantità</th>
                <th>Azione</th>
			</tr>
				
            <% for(ItemCarrello item : carrello) { 
            	double subtotale = item.getProdotto().getPrezzo() * item.getQuantita();
                totale += subtotale;
            %>
                <tr>
                    <td><%= item.getProdotto().getNome() %></td>
                    <td>€ <%= item.getProdotto().getPrezzo() * item.getQuantita()%></td>
                    <td><form action="<%= request.getContextPath() %>/GestioneCarrelloServlet" method="POST" style="display: inline-block;">
                            <input type="hidden" name="idProdotto" value="<%= item.getProdotto().getId() %>">
                            <button type="submit" name="azione" value="diminuisci" class="btn-quantita">-</button>  
                            <input type="text" value="<%= item.getQuantita() %>" readonly class="input-quantita">
                            <button type="submit" name="azione" value="aumenta" class="btn-quantita">+</button>
                        </form>
                    </td>
                    
                    
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
        
    <% } else { %>
        <p>Il tuo carrello è vuoto. Torna al catalogo per aggiungere un paio di occhiali!</p>
    <% } %>
    </div>
</body>
</html>