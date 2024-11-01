<%@page session="false"%>
<%@page import="com.cinema.cin.model.mo.User"%>
<%@ page import="com.cinema.cin.model.mo.Film" %>
<%@ page import="java.util.*" %>
<%@ page import="com.cinema.cin.model.mo.Proiezione" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%String contextPath=request.getContextPath();%>


<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    User loggedUser = (User) request.getAttribute("loggedUser");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Abbonamento & Ticket";

%>

<html>
<head>
    <link rel="icon" type="image/x-icon" href="images/popcorn.ico">
    <link rel="stylesheet" href="css/General_settings.css" type="text/css" media="screen">
    <link rel="stylesheet" href="css/Header_and_Nav.css" type="text/css" media="screen">
    <link rel="stylesheet" href="css/Home/Home_Abbonamento.css" type="text/css" media="screen">

    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" integrity="sha512-SnH5WK+bZxgPHs44uWIX+LLJAJ9/2PkPKZ5QiAj6Ta86w+fsb2TkcmfRyVX3pBnMFcV7oQPJkl9QevSCWr3W6A==" crossorigin="anonymous" referrerpolicy="no-referrer" />


    <title>Cineplex: <%=menuActiveLink%></title>

    <!-- Funzione per settare la stringa applicationMessage onLoad -->
    <script>
        var applicationMessage;
        <%if (applicationMessage != null) {%>
        applicationMessage="<%=applicationMessage%>";
        <%}%>
        function onLoadHandler() {
            //try { mainOnLoadHandler(); } catch (e) {}
            if (applicationMessage!=undefined) alert(applicationMessage);
        }
        window.addEventListener("load", onLoadHandler);
    </script>

    <!-- Funzione per leggere il valore di un cookie dato il suo nome -->
    <script>

        function getCookie(nomeCookie) {
            let arrayCookie = document.cookie.split(';');
            for (let i = 0; i < arrayCookie.length; i++) {
                arrayCookie[i] = arrayCookie[i].split(" ");
                if (arrayCookie[i].length == 2) {
                    if (arrayCookie[i][0] == "") {
                        arrayCookie[i] = arrayCookie[i][1];
                    } else {
                        arrayCookie[i] = arrayCookie[i][0];
                    }
                } else {
                    arrayCookie[i] = arrayCookie[i][0];
                }
            }
            for(let i = 0; i < arrayCookie.length; i++) {
                arrayCookie[i] = arrayCookie[i].split('=');
                if (arrayCookie[i][0] == nomeCookie){
                    return arrayCookie[i][1];
                }
            }
            return null;

        }

        function getValore(array,posizione){

            let arrayValori = array.split('#');
            return arrayValori[posizione];
        }
    </script>

    <style>


    </style>

    <!-- Funzione che setta il costo nella form -->
    <script>

        function setValue(val) {

            if(<%=loggedOn%>){

                var form = document.getElementById("formAbbonamento");
                var campoCosto = document.getElementById("costo");
                if (val == 1) {
                    campoCosto.value = "28";
                    form.submit();
                }else if(document.getElementById("studente").value == "true"){
                    campoCosto.value = "35";
                    form.submit();
                }else{
                    alert("Offerta riservata agli studenti!");
                }
            }else{
                alert("Accedi prima di poter acquistare!");
            }
        }

    </script>

    <!-- funzione che chiama la prenotazione -->
    <script>

        function prenota() {

            if(<%=loggedOn%>){

                var form = document.getElementById("formPrenota");
                form.submit();

            }else{
                alert("Accedi prima di poter acquistare!");
            }
        }

    </script>

</head>
<body>

<%@include file="/include/header_and_nav.inc"%>

<div class="contenitore">

    <h1 id="testo">Abbonamento</h1>

    <form id='formAbbonamento' name="abbonamentoForm" action="Dispatcher" method="post">
        <input type="hidden" name="controllerAction" value="CreaElementiManagement.creaAbbonamento">
        <input id="costo" type="hidden" name="costo" value="">
        <input type="hidden" name="carrelloID" id="CarrelloID" value="">



        <div class="abbonamento">
            <h1>Tessera CinePlus</h1>
            <img src="images/abbonamenti/abb1.png">
            <p>10 ingressi totali: € 35,00</p>
            <button type="button" onclick="setValue(1)">Acquista</button>
        </div>

        <div class="abbonamento">
            <h1>Tessera CinePlus +</h1>
            <img src="images/abbonamenti/abb2.png">
            <p>10 ingressi totali: € 29,00
                <br>RISERVATA AGLI STUDENTI
            </p>
            <button type="button" onclick="setValue(2)">Acquista</button>
        </div>

    </form>


<div class="biglietti">
    <h1>Biglietti</h1>

    <div class="bigliettiInfo">
        <h2>Ingresso Cinema</h2>
        <img src="images/abbonamenti/ticket.png">
        <p>INTERO: € 8,50<br>RIDOTTO STUDENTI: € 4,50</p>
    </div>

    <form id='formPrenota' name="abbonamentoForm" action="Dispatcher" method="post">
        <input type="hidden" name="controllerAction" value="AcquistaProiezioniManagement.view">
        <input id="CarrelloID1" type="hidden" name="CarrelloID1" value="">
    <button type="button" onclick="prenota()">Prenota</button>
    </form>
</div>
</div>

<%@include file="/include/footer.inc"%>

<input type="hidden" id="studente" value="">


<%if(loggedOn){%>
<script>
    document.getElementById("cartItems").innerHTML = getValore(getCookie('cart'), 1);
    document.getElementById("carrelloID").value = getValore(getCookie('cart'), 0);
    document.getElementById("CarrelloID").value = getValore(getCookie('cart'), 0);
    document.getElementById("CarrelloID1").value = getValore(getCookie('cart'), 0);
    document.getElementById("studente").value = getValore(getCookie('loggedUser'), 4);
</script>
<%}%>

</body>
</html>
