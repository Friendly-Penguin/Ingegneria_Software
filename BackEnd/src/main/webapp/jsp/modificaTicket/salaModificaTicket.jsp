<%@page session="false"%>
<%@page import="com.cinema.cin.model.mo.User"%>
<%@ page import="com.cinema.cin.model.mo.Film" %>
<%@ page import="java.util.*" %>
<%@ page import="com.cinema.cin.model.mo.Proiezione" %>
<%@ page import="com.cinema.cin.model.mo.Ticket" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%String contextPath=request.getContextPath();%>


<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    User loggedUser = (User) request.getAttribute("loggedUser");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Prenota";
    LinkedHashMap<Long, ArrayList<Ticket>> proiezione_biglietti = (LinkedHashMap<Long, ArrayList<Ticket>>) request.getAttribute("proiezione_biglietti");
    LinkedHashMap<Long, String> proiezione_sala = (LinkedHashMap<Long, String>)  request.getAttribute("proiezione_sala");
    String titolo = (String) request.getAttribute("titoloFilm");
    String data = (String) request.getAttribute("data");
    String ticketID = (String) request.getAttribute("ticketID");


%>


<!DOCTYPE html>

<html>
<head>
    <link rel="icon" type="image/x-icon" href="images/popcorn.ico">
    <link rel="stylesheet" href="css/General_settings.css" type="text/css" media="screen">
    <link rel="stylesheet" href="css/Header_and_Nav.css" type="text/css" media="screen">
    <link rel="stylesheet" href="css/Sala/Sala.css" type="text/css" media="screen">


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


</head>


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

<body>

<!-- Defining the header and nav section of the page -->
<%@include file="/include/header_and_nav_account.inc"%>




<div class="titolo">
    <h1><%=titolo%> - <%=data%></h1>
</div>
<div class="sale">
    <%for (Long item: proiezione_biglietti.keySet()) {%>
        <button onclick="setSala('<%=proiezione_sala.get(item)%>')" id="bottone_sala<%=proiezione_sala.get(item)%>" class="bottoni" proiezione="<%=item%>">Sala <%=proiezione_sala.get(item)%></button>
    <%}%>

</div>

<div id="schermo">SCHERMO</div>
<%for (Long item: proiezione_biglietti.keySet()) {%>

    <div id="posti" class="<%=proiezione_sala.get(item)%>" style="display: none;">
        <div id="colonna_1">
            <%for (int row = 1; row <= 10; row++) {
                for (int col = 1; col <= 4; col++) {%>
            <button id="posto" class="_<%=row%>_<%=col%>"></button>
            <%}
            }%>
        </div>
        <div id="colonna_2">
            <%for (int row = 1; row <= 10; row++) {
                for (int col = 5; col <= 16; col++) {%>
            <button id="posto" class="_<%=row%>_<%=col%>"></button>
            <%}
            }%>
        </div>
        <div id="colonna_3">
            <%for (int row = 1; row <= 10; row++) {
                for (int col = 17; col <= 20; col++) {%>
            <button id="posto" class="_<%=row%>_<%=col%>"></button>
            <%}
            }%>
        </div>
    </div>
<script>
    var posto_corretto = document.querySelectorAll('#posti')[document.querySelectorAll('#posti').length-1];
    <%for (int i = 0; i < proiezione_biglietti.get(item).size(); i++) {
        if(proiezione_biglietti.get(item).get(i).isComprato() || (!(proiezione_biglietti.get(item).get(i).isComprato()) && (!(proiezione_biglietti.get(item).get(i).getUsr().getEmail().equals(loggedUser.getEmail()))))){%>
    posto_corretto.querySelector('._<%=proiezione_biglietti.get(item).get(i).getN_riga()%>_<%=proiezione_biglietti.get(item).get(i).getN_colonna()%>').style.backgroundColor = 'red';
    posto_corretto.querySelector('._<%=proiezione_biglietti.get(item).get(i).getN_riga()%>_<%=proiezione_biglietti.get(item).get(i).getN_colonna()%>').innerHTML = '<i class="fa-solid fa-user"></i>';
    posto_corretto.querySelector('._<%=proiezione_biglietti.get(item).get(i).getN_riga()%>_<%=proiezione_biglietti.get(item).get(i).getN_colonna()%>').firstChild.style.color = 'black';
    posto_corretto.querySelector('._<%=proiezione_biglietti.get(item).get(i).getN_riga()%>_<%=proiezione_biglietti.get(item).get(i).getN_colonna()%>').disabled = true;
    <%}else if(proiezione_biglietti.get(item).get(i).getUsr().getEmail().equals(loggedUser.getEmail())){%>
    posto_corretto.querySelector('._<%=proiezione_biglietti.get(item).get(i).getN_riga()%>_<%=proiezione_biglietti.get(item).get(i).getN_colonna()%>').style.backgroundColor = 'orange';
    posto_corretto.querySelector('._<%=proiezione_biglietti.get(item).get(i).getN_riga()%>_<%=proiezione_biglietti.get(item).get(i).getN_colonna()%>').innerHTML = '<i class="fa-solid fa-basket-shopping"></i>';
    posto_corretto.querySelector('._<%=proiezione_biglietti.get(item).get(i).getN_riga()%>_<%=proiezione_biglietti.get(item).get(i).getN_colonna()%>').firstChild.style.color = 'black';
    posto_corretto.querySelector('._<%=proiezione_biglietti.get(item).get(i).getN_riga()%>_<%=proiezione_biglietti.get(item).get(i).getN_colonna()%>').disabled = true;
    <%}
}%>
</script>

<%}%>

<script>

    function inviaPosti() {

        var cont_selected = null
        document.querySelectorAll('#posti').forEach((item) => {
            if (item.style.display == 'flex') {
                cont_selected = item;
            }
        })
        for (var col = 1; col < 21; col++) {
            for (var row = 1; row < 11; row ++) {
                if (cont_selected.querySelector('._'+row+'_'+col).style.backgroundColor == 'green') {
                    document.getElementById('val').value = document.getElementById('val').value + row + '_' + col + '|';
                }
            }
        }
        if (document.getElementById('val').value == "") {
            alert('Nessun posto è stato selezionato');
        } else {

            document.getElementById('form').submit();
        }

    }
    post_slct = 0;
    document.addEventListener('click', (event) => {

        if (event.target.tagName == 'I') {
            var target = event.target.parentElement;
        } else {
            var target = event.target;
        }
        if (target.id == 'posto') {

            if (target.style.backgroundColor == 'rgb(68, 68, 81)' && post_slct < 1) {
                target.style.backgroundColor = 'green';
                target.innerHTML = '<i class="fa-solid fa-ticket"></i>';
                post_slct++;

            } else if (target.style.backgroundColor == 'green') {
                target.style.backgroundColor = 'rgb(68, 68, 81)';
                target.innerHTML = '';
                post_slct--;

            } else if(post_slct == 1){
                alert("Hai già selezionato il numero massimo di biglietti per questa transazione!");

            }
        }
    })
    for (var col = 1; col < 21; col++) {
        for (var row = 1; row < 11; row++) {
            document.querySelectorAll('._'+row+"_"+col).forEach((item) => {
                if (item.style.backgroundColor != 'orange' && item.style.backgroundColor != 'red') {
                    item.style.backgroundColor = 'rgb(68, 68, 81)';
                }
            })
        }
    }
</script>

<div>
    <form id='form' action="Dispatcher" method="post">
        <input type="hidden" name="controllerAction" value="CreaElementiManagement.modificaBiglietto">

        <input type="hidden" name="posti" id='val' value="">
        <input type="hidden" name="proiezioneID" id="proiezioneID" value="">
        <input type="hidden" name="ticketID" value="<%=ticketID%>">

    </form>
    <div class="bottoneSala">
    <button type="button" id="button" onclick="inviaPosti()">Modifica</button>
    </div>
</div>


<input type="hidden" id="numeroTicket" value="">
<input type="hidden" id="num_sala" value="">


<%@include file="/include/footer.inc"%>

<script>
    document.getElementById("cartItems").innerHTML = getValore(getCookie('cart'), 1);
    document.getElementById("carrelloID").value = getValore(getCookie('cart'), 0);
    document.getElementById("numeroTicket").value = getValore(getCookie('cart'), 2);
</script>

<%if((loggedUser != null) && (loggedUser.getConfermaEmail().equals("false"))){%>
<!-- funzione che nasconde l'alert -->
<script>
    function funzioneNascondi(){
        document.getElementById("formConf").style.visibility = 'hidden';   // hide
    }
</script>
<%}%>
<script>
    function setSala(num_sala) {
        if (post_slct == 0) {
            document.querySelectorAll('#posti').forEach((item) => {
                if (item.className == num_sala) {
                    item.style.display = 'flex';
                    document.getElementById('proiezioneID').value = document.getElementById('bottone_sala'+num_sala).getAttribute('proiezione');
                } else {
                    item.style.display = 'none';
                }
            })
            document.querySelectorAll('.bottoni').forEach((item) => {
                item.style.backgroundColor = '#591fc1';
            })
            document.getElementById('bottone_sala'+num_sala).style.backgroundColor = 'goldenrod';
        } else {
            alert('Deseleziona i ticket che hai selezionato prima di cambiare sala');
        }
    }
</script>
<script>
    document.addEventListener('DOMContentLoaded', (event) => {
        var lista = [null, null, null, null];
        var sale = document.querySelector('.sale');
        for (i = 0; i < sale.children.length; i++) {
            lista[parseInt(sale.children[i].innerHTML[sale.children[i].innerHTML.length - 1]) - 1] = sale.children[i];
        }
        sale.innerHTML = '';
        passato = 0;
        for (i = 0; i < lista.length; i++) {
            if (lista[i] != null) {
                passato++;
                sale.innerHTML = sale.innerHTML + lista[i].outerHTML;
                if (passato == 1) {
                    lista[i].click();
                }
            }
        }
    })
</script>
</body>
</html>
