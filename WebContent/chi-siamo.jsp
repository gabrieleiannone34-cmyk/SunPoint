<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chi Siamo</title>
   <link rel="stylesheet" href="${pageContext.request.contextPath}/style/style.css">
</head>
<body>

    <jsp:include page="/header.jsp" />

    <div class="container chi-siamo-page">
        <h2 style="text-align: center; margin-bottom: 40px;">La Nostra Storia</h2>

        <div class="chi-siamo-content">
            <div class="chi-siamo-text">
                <h3>Visionari dal primo giorno</h3>
                <p>Fondato con la passione per il design e l'innovazione, <strong>Sunpoint</strong> non è solo un negozio di occhiali, ma un vero e proprio laboratorio di stile. Crediamo che un occhiale non sia un semplice accessorio, ma il riflesso della tua personalità.</p>
                <p>Selezioniamo accuratamente i migliori materiali e collaboriamo con designer emergenti per offrirti montature uniche, resistenti e alla moda, garantendo sempre la massima protezione UV per i tuoi occhi.</p>
            </div>
            
        </div>

        <div class="valori-section">
            <div class="valore-card">
                <h4>Qualità</h4>
                <p>Lenti certificate e montature testate per durare nel tempo.</p>
            </div>
            <div class="valore-card">
                <h4>Stile</h4>
                <p>Design all'avanguardia che anticipa le tendenze stagionali.</p>
            </div>
            <div class="valore-card">
                <h4>Assistenza</h4>
                <p>Un team sempre pronto ad aiutarti a scegliere il modello perfetto.</p>
            </div>
        </div>
    </div>

    <jsp:include page="/footer.jsp" />

</body>
</html>