<%@page session="false"%>
<%@page import="com.cinema.cin.model.mo.User"%>
<%@ page import="java.util.*" %>
<%@ page import="com.cinema.cin.model.mo.Ticket" %>
<%String contextPath=request.getContextPath();%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    User loggedUser = (User) request.getAttribute("loggedUser");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Account";
    List<Ticket> ticketList = (List<Ticket>) request.getAttribute("ticket");
%>


<!DOCTYPE html>
<head>

    <link rel="icon" type="image/x-icon" href="images/popcorn.ico">
    <link rel="stylesheet" href="css/General_settings.css" type="text/css" media="screen">
    <link rel="stylesheet" href="css/Header_and_Nav.css" type="text/css" media="screen">
    <link rel="stylesheet" href="css/Account/Account_Biglietti.css" type="text/css" media="screen">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" integrity="sha512-SnH5WK+bZxgPHs44uWIX+LLJAJ9/2PkPKZ5QiAj6Ta86w+fsb2TkcmfRyVX3pBnMFcV7oQPJkl9QevSCWr3W6A==" crossorigin="anonymous" referrerpolicy="no-referrer" />

    <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js" integrity="sha512-qZvrmS2ekKPF2mSznTQsxqPgnpkI4DNTlrdUmTzrDgektczlKNRRhy5X5AAOnx5S09ydFYWWNSfcEqDTTHgtNA==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>

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

<main>

    <div class="account">
        <div class="titoloAccount">
            <h1>Account</h1>
            <h1>Biglietti</h1>
        </div>
    </div>

    <div class="container">
    <%if(ticketList.isEmpty()){%>
        <div class="noTicket">
            <h1 style="color: whitesmoke;">Nessun biglietto presente!</h1>
        </div>
    <%}else{%>
        <div class="Contenitore">
            <div class="cercaFilm">
                <h1>Cerca: </h1>
                <input type="text" id="cercaFilm" placeholder="Titolo..." onkeyup="trovaFilm()">
            </div>
            <%
                HashMap<String, ArrayList<Ticket>> film_tickets = new HashMap<>();
                for (int i = 0; i < ticketList.size(); i++) {
                    String titoloFilm = ticketList.get(i).getProiezione().getFilm().getTitolo();
                    if (!film_tickets.containsKey(titoloFilm)) {
                        ArrayList<Ticket> tickets = new ArrayList<>();
                        film_tickets.put(titoloFilm, tickets);
                    }
                    film_tickets.get(titoloFilm).add(ticketList.get(i));
                }
                for (String key: film_tickets.keySet()){%>
                    <div class="tuttituttiBiglietti">
                    <%for (int i = 0; i < film_tickets.get(key).size(); i++) {%>
                        <div class="tuttiBiglietti" id="Biglietto_<%=key%>_<%=i%>">
                            <img src="images/locandine/<%=film_tickets.get(key).get(i).getProiezione().getFilm().getLocandina()%>">
                            <div class="scritte">
                                <div class="titolo">
                                    <h1>Titolo: </h1>
                                    <h2> <%=key%></h2>
                                </div>
                                <h2>Data: <%=film_tickets.get(key).get(i).getProiezione().getData()%></h2>
                            </div>
                            <div class="bottoni">
                                <input type="hidden" id="costo-<%=key%>-<%=i%>" value="<%=String.format("%.2f",film_tickets.get(key).get(i).getProiezione().getCosto())%>">
                                <input type="hidden" id="titolo-<%=key%>-<%=i%>" value="<%=film_tickets.get(key).get(i).getProiezione().getFilm().getTitolo()%>">
                                <input type="hidden" id="biglietto-<%=key%>-<%=i%>" value="<%=film_tickets.get(key).get(i).getTicket_id()%>">
                                <input type="hidden" id="data-<%=key%>-<%=i%>" value="<%=film_tickets.get(key).get(i).getProiezione().getData()%>">
                                <input type="hidden" id="numeroSala-<%=key%>-<%=i%>" value="<%=film_tickets.get(key).get(i).getProiezione().getSala().getNumeroSala()%>">
                                <input type="hidden" id="posto-<%=key%>-<%=i%>" value="<%=(char) (Integer.parseInt(ticketList.get(i).getN_riga()) + 64)%><%=ticketList.get(i).getN_colonna()%>">
                                <i id="scarica" class="fa-solid fa-file-arrow-down" onclick="creaPDF('<%=key%>','<%=i%>')"> Scarica</i>
                                <i id="modifica"  class="fa-solid fa-file-pen" onclick="modificaTicket('<%=key%>','<%=i%>')"> Modifica</i>
                            </div>
                        </div>
                    <%}%>
                     <div class="divisore" style="color:whitesmoke;font-size: 10px">
                         <input type="hidden" value="<%=key%>">
                         <h1>── ───── ─ ───── ──</h1>
                     </div>
                <%}%>
                    </div>
        </div>
        <form id="formModificaTicket" action="Dispatcher" method="post">
            <input id="inputType" type="hidden" name="controllerAction" value="ModificaTicketManagement.modificaTicket"/>
            <input id="formTicketID" type="hidden" name="ticketID" value="">
        </form>
    <%}%>
    </div>
</main>

<!-- funzione che crea il PDF del biglietto -->
<script>
    window.jsPDF = window.jspdf.jsPDF;
    function creaPDF(titolo,valore){
        var costo = document.getElementById("costo-"+titolo+"-"+valore).value;
        var titoloFilm = document.getElementById("titolo-"+titolo+"-"+valore).value;
        var bigliettoID = document.getElementById("biglietto-"+titolo+"-"+valore).value;
        var data = document.getElementById("data-"+titolo+"-"+valore).value;
        var numeroSala = document.getElementById("numeroSala-"+titolo+"-"+valore).value;
        var posto = document.getElementById("posto-"+titolo+"-"+valore).value;

        var doc = new jsPDF({
            unit: 'mm',
            format: [80, 216],
            orientation: 'landscape'
        });

        doc.setFontSize(14);

        // Aggiungi titolo e bordo del biglietto
        doc.setFontSize(22);
        doc.setTextColor(40);
        doc.setDrawColor(0);
        doc.setLineWidth(1.5);
        doc.rect(10, 10, 196, 60); // Dimensioni adeguate per A6 orizzontale con margine
        doc.line(10,30,30,10);
        doc.line(10,50,50,10);
        doc.text("Cineplex", 17, 38, {angle: 45});
        // Aggiungi dati del biglietto
        doc.setFontSize(16);
        doc.setTextColor(100);
        doc.text(titoloFilm, 60, 30);
        doc.text(data, 60, 40);
        doc.text("Sala: " + numeroSala +"\nPosto: " + posto, 60, 50);

        doc.text(bigliettoID, 198, 65);
        doc.text("€ "+costo, 20, 60);
        doc.saveGraphicsState();

        doc.setTextColor("red");
        doc.text("Pagato", 145, 42, {angle: 45});
        doc.restoreGraphicsState();

        doc.circle(150,35,15,"S");
        doc.save("biglietto" + valore + ".pdf");
    }
</script>

<!-- funzione che submitta la form per modificare il Ticket -->
<script>
    function modificaTicket(titolo,valore){
        var form = document.getElementById("formModificaTicket");
        var ticket = document.getElementById("formTicketID");

        ticket.value = document.getElementById("biglietto-"+titolo+"-"+valore).value;
        form.submit();
    }
</script>

<!-- Script per filtrare i biglietti -->
<script>
    function trovaFilm() {
        var input = document.getElementById('cercaFilm');
        var filter = input.value.toLowerCase();
        var tickets = document.querySelectorAll('.tuttiBiglietti');
        var divisori = document.querySelectorAll('.divisore');

        tickets.forEach(function(ticket) {

            var title = ticket.querySelector('.scritte .titolo h2').textContent.toLowerCase();

            if (title.includes(filter)) {
                    ticket.style.display = "";

                } else if(filter === '') {
                    ticket.style.display = "";


                }else{
                    ticket.style.display = "none";

                }
        });

        divisori.forEach(function (div){

            var divKey = div.querySelector('input').value.toLowerCase();

            if (divKey.includes(filter)) {
                div.style.display = "";

            } else if(filter === '') {
                div.style.display = "";

            }else{
                div.style.display = "none";
            }
        });
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
