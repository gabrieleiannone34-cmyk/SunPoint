<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/style/style.css">
<title>Dati Spedizione</title>
</head>
<body>
	<div class="container">
		<form action=${pageContext.request.contextPath}/CheckoutServlet method="POST">
    		<h3>Dati di Spedizione</h3>
    
    		<label>Via e Numero Civico *</label>
    		<input type="text" name="indirizzo" required>
    
    		<label>Città *</label>
    		<input type="text" name="citta" required>
    
    		<label>CAP *</label>
    		<input type="text" name="cap" required maxlength="5">
    
    		<h3>Metodo di Pagamento</h3>
    		<select name="pagamento">
        		<option value="Carta di Credito">Carta di Credito</option>
        		<option value="PayPal">PayPal</option>
        		<option value="Carta Bancaria">Carta Bancaria</option>
    		</select>
    
    		<button type="submit">Conferma Ordine e Paga</button>
		</form>
	</div>
	<jsp:include page="/footer.jsp" />
</body>
</html>