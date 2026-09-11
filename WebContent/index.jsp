<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="it.unisa.sunpoint.model.Utente" %>
<%
    
    Utente utente = (Utente) session.getAttribute("utenteLoggato");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SunPoint - Home</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style/style.css">
</head>
<body>
    <div class="container">
        <jsp:include page="/header.jsp" />
        
        <h2>Benvenuto su SunPoint!</h2>
        <p>Scopri la nostra nuova collezione di occhiali da sole.</p>
        
        
        <% if (utente != null && "admin".equals(utente.getRole())) { %>
            <div class="pannelli">
                <h3>Pannello di Controllo Admin</h3>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/InserisciProdottoServlet">Inserisci un Nuovo Occhiale nel Catalogo</a></li>
                    <li><a href="${pageContext.request.contextPath}/TuttiOrdiniServlet">Visualizza e Filtra Tutti gli Ordini</a></li>
                </ul>
            </div>
            
        <% } else if (utente != null && "user".equals(utente.getRole())) { %>
            <div class="pannelli">
                <h3>Area Personale</h3>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/StoricoOrdiniServlet">Visualizza lo Storico dei Miei Ordini</a></li>
                </ul>
            </div>
        <% } %>
       <div class="features-section">
            <div class="feature-box">
                <h3>🚚 Spedizione Gratuita</h3>
                <p>Su tutti gli ordini superiori a 50€. Consegna rapida e tracciata in 24/48h in tutta Italia.</p>
            </div>
            <div class="feature-box">
                <h3>🔄 Reso Facile</h3>
                <p>Hai 30 giorni di tempo per cambiare idea. La procedura di reso è gratuita e veloce.</p>
            </div>
            <div class="feature-box">
                <h3>🛡️ Qualità Premium</h3>
                <p>Solo lenti certificate con protezione totale dai raggi solari dannosi UV400.</p>
            </div>
        </div> 
    </div>

    <div class="sezioni">
		<h2 class="sezioni-title">Adatto a tutti i generi e a tutte le età</h2>
            
		<div class="sezioni-gallery">
			<div class="sezioni-item">
				<img src="${pageContext.request.contextPath}/images/donna.jpg" alt="Occhiali da sole per Donna">
                <h4>Donna</h4>
			</div> 
            <div class="sezioni-item">
               <img src="${pageContext.request.contextPath}/images/uomo.jpg" alt="Occhiali da sole per Uomo">
 	           <h4>Uomo</h4>
       	    </div>
            <div class="sezioni-item">
               <img src="${pageContext.request.contextPath}/images/bambino.jpg" alt="Occhiali da sole per Bambino">
               <h4>Bambino</h4>
            </div>
        </div>
	</div>
		
    <div class="bestseller-section">
    	<h2 class="sezioni-title">Il Nostro Best-Seller</h2>
    		<div class="bestseller-gallery">
        		<div class="bestseller-item">
            		<img src="${pageContext.request.contextPath}/images/rayban_aviator.jpg" alt="Occhiale Best Seller">
            		<h4>Modello Aviator Classic</h4>
            		<p class="bestseller-prezzo">€ 145.5</p>
            		<a href="${pageContext.request.contextPath}/CatalogoServlet" class="btn-compra">Scopri nel Catalogo</a>
        		</div>
    		</div>
	</div>
        
    <jsp:include page="/footer.jsp" />
</body>
</html>