<%@page session="false"%>
<%@ page import="java.util.*" %>
<%@ page import="com.cinema.cin.model.mo.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%String contextPath=request.getContextPath();%>


<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    User loggedUser = (User) request.getAttribute("loggedUser");
    String menuActiveLink = "Aggiungi metodo";
    String destinazione = (String) request.getAttribute("destinazione");

%>

<html>


    <head>
        <link rel="icon" type="image/x-icon" href="images/popcorn.ico">
        <link rel="stylesheet" href="css/General_settings.css" type="text/css" media="screen">
        <link rel="stylesheet" href="css/Header_and_Nav.css" type="text/css" media="screen">
        <link rel="stylesheet" href="css/Carrello/Card.css" type="text/css" media="screen">


        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" integrity="sha512-SnH5WK+bZxgPHs44uWIX+LLJAJ9/2PkPKZ5QiAj6Ta86w+fsb2TkcmfRyVX3pBnMFcV7oQPJkl9QevSCWr3W6A==" crossorigin="anonymous" referrerpolicy="no-referrer" />


        <title>Cineplex: <%=menuActiveLink%></title>


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


</head>


    <body>

    <!-- Defining the header and nav section of the page -->
    <%@include file="/include/header_and_nav.inc"%>


    <script>

        function scrivere(event, tipo) {

            event.preventDefault();

            switch (tipo) {
                case "numerocarta": {
                    var cardnumber = document.getElementById('card-number');
                    value = cardnumber.value.replaceAll(" ","");
                    if ("01234567890".includes(event.key) && value.length < 16) {
                        value += event.key;
                    }
                    else if (event.key == "Backspace") {
                        value = value.substring(0, value.length-1);
                    } else if (event.key == "Tab") {
                        document.getElementById('name-text').focus();
                    }

                    if (value.length > 4) {
                        value = value.substring(0, 4) + " " + value.substring(4, value.length);
                    }
                    if (value.length > 9) {
                        value = value.substring(0, 9) + " " + value.substring(9, value.length);
                    }
                    if (value.length > 14) {
                        value = value.substring(0, 14) + " " + value.substring(14, value.length);
                    }

                    if (value.length == 0){
                        cardnumber.value = "";
                        document.querySelector(".number-v1").innerHTML = "1234 5678 9101 1121";
                    } else {
                        cardnumber.value = value
                        document.querySelector('.number-v1').innerHTML = value;
                    }
                    break;
                }
                case "nome": {
                    if (event.key == 'Backspace') {
                        document.getElementById('name-text').value = document.getElementById('name-text').value.substring(0, document.getElementById('name-text').value.length - 1);
                        document.querySelector(".name-v1").innerHTML = document.getElementById('name-text').value.toUpperCase();
                    } else if (event.key == 'Tab') {
                        document.getElementById('Valid-thru-text').focus();
                    } else {
                        if("abcdefghijklmonpqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ ".includes(event.key) && document.getElementById('name-text').value.length < 26) {
                            document.getElementById('name-text').value += event.key;
                            document.querySelector(".name-v1").innerHTML = document.getElementById('name-text').value.toUpperCase();
                        }
                    }
                    if(document.getElementById('name-text').value.length == 0){

                        document.getElementById('name-text').value = "";
                        document.querySelector(".name-v1").innerHTML = "Nome Cognome";
                    }
                    break;
                }
                case "data": {

                    if (event.key == 'Backspace') {
                        document.getElementById('Valid-thru-text').value = document.getElementById('Valid-thru-text').value.substring(0, document.getElementById('Valid-thru-text').value.length - 1);
                        document.querySelector(".expiration-v1").innerHTML = document.getElementById('Valid-thru-text').value;
                    } else if (event.key == 'Tab') {
                        document.getElementById('cvv-text').focus();
                    } else {
                        if("0123456789".includes(event.key) && document.getElementById('Valid-thru-text').value.length < 5) {
                            if(document.getElementById('Valid-thru-text').value.length >= 2 && document.getElementById('Valid-thru-text').value[2] != "/"){
                                document.getElementById('Valid-thru-text').value = document.getElementById('Valid-thru-text').value.substring(0, 2) + "/" + document.getElementById('Valid-thru-text').value.substring(2, document.getElementById('Valid-thru-text').value.length);
                            }
                            document.getElementById('Valid-thru-text').value += event.key;
                            document.querySelector(".expiration-v1").innerHTML = document.getElementById('Valid-thru-text').value.toUpperCase();
                        }
                    }
                    if(document.getElementById('Valid-thru-text').value.length == 0){

                        document.getElementById('Valid-thru-text').value = "";
                        document.querySelector(".expiration-v1").innerHTML = "Scadenza";
                    }
                    break;

                }
                case "codice":{
                    if (event.key == 'Backspace') {
                        document.getElementById('cvv-text').value = document.getElementById('cvv-text').value.substring(0, document.getElementById('cvv-text').value.length - 1);
                        document.querySelector(".cvv-v1").innerHTML = document.getElementById('cvv-text').value;
                    } else if (event.key == 'Tab') {
                        document.getElementById('card-number').focus();
                    } else {
                        if("0123456789".includes(event.key) && document.getElementById('cvv-text').value.length < 3) {
                            document.getElementById('cvv-text').value += event.key;
                            document.querySelector(".cvv-v1").innerHTML = document.getElementById('cvv-text').value;
                        }
                    }
                    if(document.getElementById('cvv-text').value.length == 0){

                        document.getElementById('cvv-text').value = "";
                        document.querySelector(".cvv-v1").innerHTML = "CVV";
                    }
                    break;
                }

            }
        }
        function verificaForm() {
            card = document.getElementById('card-number').value;
            nome = document.getElementById('name-text').value;
            scad = document.getElementById('Valid-thru-text').value;
            cvv = document.getElementById('cvv-text').value;
            if (card.length == 19 && nome.split(" ").length == 2 && scad.split("/").length == 2 && scad.length == 5 && cvv.length == 3) {
                document.getElementById('credit-card').submit();
            } else {
                if (card.length != 19) {
                    alert("Errore: Lunghezza carta non valida");
                } else if (nome.split(" ").length != 2) {
                    alert("Errore: Nome non valido");
                } else if (scad.split("/").length != 2 || scad.length != 5) {
                    alert("Errore: Data scadenza non valida");
                } else {
                    alert("Errore: CVV non valido");
                }
            }

        }
    </script>

    <div class="container">

        <section class="ui">

            <%if(destinazione != null){%>
            <div class="freccia">
                <a href="Dispatcher?controllerAction=AccountManagement.viewMetodiPagamento">
                    <i class="fa-solid fa-arrow-left"></i>
                </a>
            </div>
            <%}else{%>
            <div class="freccia">
                <a href="Dispatcher?controllerAction=CarrelloManagement.view">
                    <i class="fa-solid fa-arrow-left"></i>
                </a>
            </div>
            <%}%>
            <div class="contieni-lati">
            <div class="container-left">


            <form id="credit-card" name="metodoForm" action="Dispatcher" method="post">

                <div class="number-container">
                    <label>Numero della carta</label>
                    <input type="text" maxlength="19" name="campoNumeroCarta" placeholder="1234 5678 9101 1121" id="card-number" onkeydown="scrivere(event, 'numerocarta')">
                </div>

                <div class="name-container">
                    <label>Titolare</label>
                    <input type="text" name="campoTitolare" placeholder="Nome Cognome" id="name-text" maxlength="26" required onkeydown="scrivere(event, 'nome')">

                </div>

                <div class="infos-container">

                    <div class="expiration-container">
                        <label>Scadenza</label>
                        <input type="text" name="campoScadenza" placeholder="Scadenza" id="Valid-thru-text" maxlength="5" required onkeydown="scrivere(event, 'data')">
                    </div>

                    <div class="cvv-container">
                        <label>CVV</label>
                        <br>
                        <input type="text" name="campoCVV" placeholder="CVV" id="cvv-text" maxlength="3" required onkeydown="scrivere(event,'codice')">
                    </div>
                </div>

                <input id="carrelloID" type="hidden" name="carrelloID" value="">
                <input id="inpuType" type="hidden" name="controllerAction" value="CarrelloManagement.nuovoMetodo"/>
                <input name="destinazione" value="<%=destinazione%>" type="hidden">
                <input type="button" value="Aggiungi" id="add" onclick="verificaForm()">

            </form>
        </div>

        <div class="container-right">
            <div class="card">
                <div class="intern">
                    <img class="approximation" src="images/aprox.png" alt="aproximation">
                    <div class="card-number">
                        <div class="number-v1">1234 5678 9101 1121</div>
                    </div>

                    <div class="card-holder">
                        <label>Titolare</label>
                        <div class="name-v1">Nome Cognome</div>
                    </div>

                    <div class="card-infos">
                        <div class="exp">
                            <label>Scadenza</label>
                            <div class="expiration-v1">Scadenza</div>
                        </div>

                        <div class="cvv">
                            <label>CVV</label>
                            <div class="cvv-v1">CVV</div>
                        </div>
                    </div>
                    <img class="chip" src="images/chip.png" alt="chip">
                </div>
            </div>
        </div>
        </div>
    </section>
    </div>

    <!-- Funzione che controlla se la data di scadenza è valida -->
    <script>
        document.getElementById('Valid-thru-text').addEventListener('blur', function() {
            var input = this.value;

            if (input.length == 5) {

                var month = parseInt(input.substring(0, 3), 10);
                var year = parseInt(input.substring(3, input.length), 10);


                if(month < 1 || month > 12) {
                    this.setCustomValidity('Mese non valido. Usa un mese tra 01 e 12.');
                    this.reportValidity();
                    return;
                }

                var currentDate = new Date();
                var currentMonth = currentDate.getMonth() + 1; // Mesi da 0 a 11
                var currentYear = currentDate.getFullYear() % 100;

                if (year > currentYear || (year === currentYear && month >= currentMonth)) {
                    this.setCustomValidity(''); // Input valido
                } else {
                    this.setCustomValidity('La data di scadenza deve essere maggiore della data attuale.');
                    this.reportValidity();
                }
            }
        });
    </script>


    <!-- definizione sezione footer -->
    <%@include file="/include/footer.inc"%>


    <script>
        document.getElementById("cartItems").innerHTML = getValore(getCookie('cart'), 1);
        document.getElementById("carrelloID").value = getValore(getCookie('cart'), 0);
    </script>

    </body>


</html>
