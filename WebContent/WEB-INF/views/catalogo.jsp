<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.sunpoint.model.Prodotto" %>
<%@ page import="it.unisa.sunpoint.model.Utente" %>
<%
    // Recuperiamo la lista di prodotti che la Servlet ci ha inviato
    List<Prodotto> prodotti = (List<Prodotto>) request.getAttribute("catalogo");

	//Recupero l'utente dalla sessione per controllare i permessi
	Utente utenteLoggato = (Utente) session.getAttribute("utenteLoggato");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Catalogo Occhiali - SunPoint</title>
</head>
<body>
	<%
    // Se nell'URL c'è scritto "errore=esaurito", mostriamo un avviso
    String errore = request.getParameter("errore");
    if ("esaurito".equals(errore)) {
	%>
        <h3 style="color: red;">Spiacenti, scorte esaurite per questo occhiale!</h3>
	<%
    }
	%>
    <h2>La nostra Collezione di Occhiali da Sole</h2>
    <a href="${pageContext.request.contextPath}/index.jsp">Torna alla Home</a>
    <a href="${pageContext.request.contextPath}/VisualizzaCarrelloServlet">Vai al Carrello </a>

    <%-- Controllo: la lista esiste e ha almeno un elemento? --%>
    <% if (prodotti != null && !prodotti.isEmpty()) { %>
        
        <%-- Usiamo una tabella HTML base per impaginare i prodotti --%>
        <table>
            <tr>
            	<th>Immagine</th>
                <th>Nome Modello</th>
                <th>Descrizione</th>
                <th>Prezzo</th>
                <th>Disponibilità</th>
                <th>Azione</th>
            </tr>
            
            <%-- Inizio del ciclo FOR --%>
            <% for(Prodotto p : prodotti) { %>
                
                <tr>
                	<td><img src="<%= request.getContextPath() %>/images/<%= p.getImagePath() %>" width="100" alt="<%= p.getNome() %>"></td>
                    <td><%= p.getNome() %></td>
                    <td><%= p.getDescrizione() %></td>
                    <td>€ <%= p.getPrezzo() %></td>
                    <td><%= p.getQuantita() %> pz</td>
                    <td>
                    <% if (utenteLoggato != null && "admin".equals(utenteLoggato.getRole())) { %>
            		<div style="margin-top: 15px; padding-top: 10px; border-top: 1px solid #ccc;">
                		<p>Opzioni Admin:</p>
                
                		<%-- Tasto Modifica (passa l'ID tramite URL) --%>
                		<a href="${pageContext.request.contextPath}/ModificaProdottoServlet?id=<%= p.getId() %>">Modifica</a>
                 
                		<%-- Tasto Elimina (passa l'ID tramite URL e chiede conferma) --%>
                		<a href="${pageContext.request.contextPath}/CancellaProdottoServlet?id=<%= p.getId() %>">Elimina</a>
            		</div>
        		<% } %>
        			<form action="${pageContext.request.contextPath}/CarrelloServlet" method="POST">
                            <input type="hidden" name="idProdotto" value="<%= p.getId() %>">
                            <input type="submit" value="Aggiungi al Carrello">
                        </form>
        		</td>
               </tr>
                 
                
        	<% } %>
        </table>
        

    <% } else { %>
        <p>Al momento non ci sono occhiali nel catalogo.</p>
    <% } %>
    
   

</body>
</html>