<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="it.unisa.sunpoint.model.Utente" %>

<header>
    <h1>SunPoint</h1>
    <nav>
        <ul>
            <li><a href="${pageContext.request.contextPath}/index.jsp">Home</a></li>
            <li><a href="${pageContext.request.contextPath}/CatalogoServlet">Catalogo</a></li>
            <li><a href="${pageContext.request.contextPath}/VisualizzaCarrelloServlet">Carrello</a></li>
            
            <%-- Controllo Sessione: Mostriamo Login o Logout in base allo stato --%>
            <% 
               Utente utenteNav = (Utente) session.getAttribute("utenteLoggato");
               if(utenteNav != null) { 
            %>
                <li><a href="${pageContext.request.contextPath}/LogoutServlet">Logout</a></li>
            <% } else { %>
                <li><a href="${pageContext.request.contextPath}/LoginServlet">Login</a></li>
            <% } %>
        </ul>
    </nav>
</header>