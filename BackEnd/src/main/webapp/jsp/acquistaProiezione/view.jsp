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
    String menuActiveLink = "Prenota";
    ArrayList<Proiezione> listaProiezioni = (ArrayList<Proiezione>) request.getAttribute("proiezioni");
    ArrayList<Film> listaFilm = (ArrayList<Film>) request.getAttribute("film");

%>
<html>

<head>
        <link rel="icon" type="image/x-icon" href="images/popcorn.ico">
        <link rel="stylesheet" href="css/General_settings.css" type="text/css" media="screen">
        <link rel="stylesheet" href="css/Header_and_Nav.css" type="text/css" media="screen">
        <link rel="stylesheet" href="css/AcquistaProiezione/AcquistaProiezione.css" type="text/css" media="screen">

        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" integrity="sha512-SnH5WK+bZxgPHs44uWIX+LLJAJ9/2PkPKZ5QiAj6Ta86w+fsb2TkcmfRyVX3pBnMFcV7oQPJkl9QevSCWr3W6A==" crossorigin="anonymous" referrerpolicy="no-referrer" />


        <title>Cineplex: <%=menuActiveLink%></title>

    <style>



    </style>

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


</head>

<body>

    <%@include file="/include/header_and_nav.inc"%>

    <div class="titoloAcquista">
        <h1>Spettacoli in Programmazione</h1>
    </div>
    <div class="main">
        <div class="select">
            <div class="acquistare">

            </div>
            <div class="listaFilm">
                <select id="film">
                    <%for (int i = 0; i < listaFilm.size(); i++ ){%>
                    <option class="film_option" value="<%=listaFilm.get(i).getFilmID()%>"><%=listaFilm.get(i).getTitolo()%></option>
                    <%}%>
                </select>
            </div>
            <div class="listaProiezioni">
                <select class="select" id="proiezioni" datafld="film1" style="display: none">

                </select>
            </div>
            <div class="listaOrari">
                <select class="select" id="orari" style="display: none">

                </select>
            </div>
        </div>
    </div>
    <form id="proiezioneForm" action="Dispatcher" method="post">
        <input type="hidden" name="controllerAction" value="SalaManagement.view"/>
        <input id="filmTitolo" type="hidden" name="filmTitolo" value="">
        <input id="proiezioneID" type="hidden" name="proiezioneID" value="">
        <button type="submit" id="submit" disabled>Seleziona</button>
    </form>
    <%@include file="/include/footer.inc"%>

    <script>
        film_list = {};
        proiezione_list = {};
        all_list = {};
        FGO_proiezioniID = {};
        <%for (int i = 0; i < listaFilm.size() ; i++) {%>
        film_list[<%=listaFilm.get(i).getFilmID()%>] = '<%=listaFilm.get(i).getTitolo()%>';
        all_list['<%=listaFilm.get(i).getTitolo()%>'] = {};
        <%for (int j = 0; j < listaProiezioni.size(); j++) {
            if (listaFilm.get(i).getFilmID() == listaProiezioni.get(j).getFilm().getFilmID()) {%>
        proiezione_list['<%=listaFilm.get(i).getTitolo()%>:<%=listaProiezioni.get(j).getData()%>'] = '<%=listaProiezioni.get(j).getProie_id()%>';
        if (all_list['<%=listaFilm.get(i).getTitolo()%>'].hasOwnProperty('<%=listaProiezioni.get(j).getData().split(" ")[0]%>')) {
            all_list['<%=listaFilm.get(i).getTitolo()%>']['<%=listaProiezioni.get(j).getData().split(" ")[0]%>'].push('<%=listaProiezioni.get(j).getData().split(" ")[1]%>');
        } else {
            all_list['<%=listaFilm.get(i).getTitolo()%>']['<%=listaProiezioni.get(j).getData().split(" ")[0]%>'] = ['<%=listaProiezioni.get(j).getData().split(" ")[1]%>'];
        }
        if (FGO_proiezioniID.hasOwnProperty('<%=listaFilm.get(i).getTitolo()%>:<%=listaProiezioni.get(j).getData()%>')) {
            FGO_proiezioniID['<%=listaFilm.get(i).getTitolo()%>:<%=listaProiezioni.get(j).getData()%>'].push('<%=listaProiezioni.get(j).getProie_id()%>');
        } else {
            FGO_proiezioniID['<%=listaFilm.get(i).getTitolo()%>:<%=listaProiezioni.get(j).getData()%>'] = ['<%=listaProiezioni.get(j).getProie_id()%>'];
        }
        <%}
    }
}%>
        for (var i = 0; i < Object.keys(all_list).length; i++) {
            var chiave_1 = Object.keys(all_list)[i];
            for (var l = 0; l < Object.keys(all_list[Object.keys(all_list)[i]]).length; l++) {
                var chiave_2 = Object.keys(all_list[chiave_1])[l];
                all_list[chiave_1][chiave_2] = new Set(all_list[chiave_1][chiave_2]);
                all_list[chiave_1][chiave_2] = Array.from(all_list[chiave_1][chiave_2]);
            }
        }
    </script>

    <script>
        document.getElementById("cartItems").innerHTML = getValore(getCookie('cart'), 1);
        document.getElementById("carrelloID").value = getValore(getCookie('cart'), 0);
    </script>

    <script>
        document.getElementById('film').onclick = function() {
            document.getElementById('proiezioni').style.display = "flex";
            document.getElementById('orari').style.display = "none";
            Titolo = film_list[document.querySelector('#film').value];
            document.getElementById('proiezioni').innerHTML = '';
            for (var i = 0; i < Object.keys(all_list[Titolo]).length; i++) {
                document.getElementById('proiezioni').innerHTML = document.getElementById('proiezioni').innerHTML + '<option value="'+Object.keys(all_list[Titolo])[i]+'">'+Object.keys(all_list[Titolo])[i]+'</option>';
            }
            document.getElementById('submit').disabled  = true;
        }
        document.getElementById('proiezioni').onclick = function () {
            document.getElementById('orari').style.display = "flex";
            Titolo = film_list[document.querySelector('#film').value];
            Proiezione = document.getElementById('proiezioni').value;
            document.getElementById('orari').innerHTML = '';
            for (var i = 0; i < all_list[Titolo][Proiezione].length; i++) {
                document.getElementById('orari').innerHTML = document.getElementById('orari').innerHTML + '<option value="'+all_list[Titolo][Proiezione][i]+'">'+all_list[Titolo][Proiezione][i]+'</option>';
            }
            document.getElementById('filmTitolo').value = Titolo;
            document.getElementById('proiezioneID').value = FGO_proiezioniID[Titolo+':'+all_list[Titolo][Proiezione][0]]
            document.getElementById('submit').disabled  = false;
        }
        document.getElementById('orari').onclick = function () {
            Titolo = film_list[document.querySelector('#film').value];
            Proiezione = document.getElementById('proiezioni').value;
            Orario = document.getElementById('orari').value;
            document.getElementById('proiezioneID').value = FGO_proiezioniID[Titolo+':'+Proiezione+' '+Orario].join("-");
        }
    </script>

</body>
</html>
