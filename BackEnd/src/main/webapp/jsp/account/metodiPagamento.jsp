<%@page session="false"%>
<%@page import="com.cinema.cin.model.mo.User"%>
<%@ page import="java.util.*" %>
<%@ page import="com.cinema.cin.model.mo.MetodoPagamento" %>
<%String contextPath=request.getContextPath();%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    User loggedUser = (User) request.getAttribute("loggedUser");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Account";
    ArrayList<MetodoPagamento> metodoList = (ArrayList<MetodoPagamento>) request.getAttribute("listaMetodi");
%>


<!DOCTYPE html>
<head>

    <link rel="icon" type="image/x-icon" href="images/popcorn.ico">
    <link rel="stylesheet" href="css/General_settings.css" type="text/css" media="screen">
    <link rel="stylesheet" href="css/Header_and_Nav.css" type="text/css" media="screen">
    <link rel="stylesheet" href="css/Account/Account_Metodi.css" type="text/css" media="screen">

    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" integrity="sha512-SnH5WK+bZxgPHs44uWIX+LLJAJ9/2PkPKZ5QiAj6Ta86w+fsb2TkcmfRyVX3pBnMFcV7oQPJkl9QevSCWr3W6A==" crossorigin="anonymous" referrerpolicy="no-referrer" />


    <title>Cineplex: <%=menuActiveLink%></title>

    <script>

        var applicationMessage;
        <%if (applicationMessage != null) {%>
        applicationMessage="<%=applicationMessage%>";
        <%}%>
        function onLoadHandler() {
            //headerOnLoadHandler();
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


</head>

<body>

<!-- Defining the header and nav section of the page -->
<%@include file="/include/header_and_nav_account.inc"%>



<!-- Sezione riepilogo ordine + carte -->
<div class="contenitore">

    <div class="titoloAccount">
        <h1>Account</h1>
        <h1>Metodi di pagamento</h1>
    </div>

    <div class="container">
        <h1 id="testoMetodi">Metodi di pagamento - CARTE</h1>
        <%if(!metodoList.isEmpty()){%>
        <h4 >Seleziona un metodo per poterlo rimuovere</h4>
        <%}%>

        <div class="tutteCard">

            <%if(metodoList.isEmpty()){%>

            <h1>Nessun metodo presente! <br> Clicca e aggiungi un nuovo metodo di pagamento!</h1><%

        }else
            for(int i = 0; i < metodoList.size(); i++){%>


            <div class="card1" id="<%=i%>">
                <div class="intern">
                    <img class="approximation" src="images/aprox.png" alt="aproximation">
                    <i id='checked-<%=i%>' class="fa-solid fa-xmark croce"></i>
                    <input type="hidden" id="cartaID<%=i%>" value="<%=metodoList.get(i).getMetodo_id()%>">
                    <div class="card-number">
                        <div class="number-v1"><%=metodoList.get(i).getN_Carta()%></div>
                    </div>

                    <div class="card-holder">
                        <label>Titolare</label>
                        <div class="name-v1"><%=metodoList.get(i).getTitolare()%></div>
                    </div>

                    <div class="card-infos">
                        <div class="exp">
                            <label>Scadenza</label>
                            <div class="expiration-v1"><%=metodoList.get(i).getDataScad()%></div>
                        </div>

                        <div class="cvv">
                            <label>CVV</label>
                            <div class="cvv-v1">***</div>
                            <input id="cvv-<%=i%>" type="hidden" value="<%=metodoList.get(i).getCVV()%>">
                        </div>
                    </div>
                    <img class="chip" src="images/chip.png" alt="chip">
                </div>
            </div>

            <%}%>

            <script>

                function aggiungi(){
                    var formPro = document.getElementById('aggiungiMetodoForm');
                    formPro.submit();
                }

            </script>

            <form class="card1" id="aggiungiMetodoForm" action="Dispatcher" method="post">
                <input id="inpuType" type="hidden" name="controllerAction" value="CarrelloManagement.viewAggiungiMetodo"/>
                <div class="card-aggiungi intern"  onclick="aggiungi()">
                    <i class="fa-solid fa-circle-plus cerchioPiu" onclick="aggiungi()"></i>
                </div>
                <input type="hidden" name="destinazione" value="AccountManagement.viewMetodiPagamento">
            </form>
        </div>
        <div class="form-elimina">
            <form id="rimuoviMetodo" action="Dispatcher" method="post">
                <input type="hidden" name="controllerAction" value="AccountManagement.rimuoviMetodo"/>
                <input id="cartaDaRimuovere" type="hidden" name="cartaID" value="">
                <button id="bottoneRimuovi" type="button" onclick="rimuoviCarta()">Rimuovi</button>
            </form>
        </div>
    </div>


</div>



<!-- funzione per il tilt delle carte -->
<script src="script/Vanilla_tilts.js"></script>
<script>
    VanillaTilt.init(document.querySelector(".intern"), {
        max: 25,
        speed: 400
    });

    //It also supports NodeList
    VanillaTilt.init(document.querySelectorAll(".intern"));
</script>

<!-- funzione che restituisce la posizione della carta selezionata e degli elementi del carrello -->
<script>
    document.querySelectorAll('.card1').forEach((element) => {
        if (element.id != 'aggiungiMetodoForm') {
            document.getElementById('checked-'+element.id).style.display = 'none';
            element.onclick = function (event, id = element.id) {
                document.getElementById('CartaSelezionata').value = id;
                document.querySelectorAll('.card1').forEach((element_1) => {
                    if (element_1.id != 'aggiungiMetodoForm') {
                        if (element_1.id == id) {
                            if (document.getElementById('checked-'+element_1.id).style.display == 'unset') {
                                document.getElementById('checked-'+element_1.id).style.display = 'none';
                            } else {
                                document.getElementById('checked-'+element_1.id).style.display = 'unset';
                            }
                        } else {
                            document.getElementById('checked-'+element_1.id).style.display = 'none';
                        }
                    }
                });
            }
        }
    })
</script>

<input type='hidden' id="CartaSelezionata">

<!-- submit form elimina carta -->
<script>

    function rimuoviCarta(){

        var cartaID = document.getElementById("CartaSelezionata");
        var inputCarta = document.getElementById("cartaDaRimuovere");
        var form = document.getElementById("rimuoviMetodo");

        if(cartaID.value === ""){

            alert("Seleziona una carta per poterla rimuovere!");

        }else{

            inputCarta.value = document.getElementById("cartaID"+cartaID.value).value;
            form.submit();

        }


    }



</script>

<!-- footer section -->
<%@include file="/include/footer.inc"%>

<script>
    document.getElementById("cartItems").innerHTML = getValore(getCookie('cart'), 1);
    document.getElementById("carrelloID").value = getValore(getCookie('cart'), 0);
</script>

<%if((loggedUser != null) && (loggedUser.getConfermaEmail().equals("false"))){%>
<!-- funzione che nasconde l'alert -->
<script>
    function funzioneNascondi(){
        document.getElementById("formConf").style.visibility = 'hidden';   // hide
    }
</script>
<%}%>

</body>
</html>
