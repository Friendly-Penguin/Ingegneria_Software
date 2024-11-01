<%@page session="false"%>
<%@page import="com.cinema.cin.model.mo.Admin"%>
<%@ page import="java.util.*" %>
<%@ page import="com.cinema.cin.model.mo.Film" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    Admin loggedAdmin = (Admin) request.getAttribute("loggedAdmin");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Nuova Proiezione";
    ArrayList<Film> listaFilm = (ArrayList<Film>) request.getAttribute("listaFilm");
    String data = (String) request.getAttribute("data");
    Film film = (Film) request.getAttribute("film");
    String sala = (String) request.getAttribute("sala");
    LinkedHashMap<String,Boolean> orari = (LinkedHashMap<String,Boolean>) request.getAttribute("mappaOrari");
%>
<html>

<head>
    <link rel="icon" type="image/x-icon" href="images/popcorn.ico">
    <link rel="stylesheet" href="css/General_settings.css" type="text/css" media="screen">
    <link rel="stylesheet" href="css/Header_and_Nav.css" type="text/css" media="screen">
    <link rel="stylesheet" href="css/Admin/aggiungiProiezione/SelezionaOrario.css" type="text/css" media="screen">

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

</head>
<body>

<%@include file="/include/header_and_nav_admin.inc"%>

<main>

    <div class="account">
        <div class="titoloAccount">
            <h1 id="titoloSeleziona">Seleziona Orari/o</h1>
        </div>
    </div>


<div class="selezioneOrario">

    <div class="superiore">
        <div class="freccia">
            <a href="Dispatcher?controllerAction=AdminManagement.nuovaProiezione">
                <i class="fa-solid fa-arrow-left" ></i>
            </a>
        </div>
        <div class="titolo">
            <h1>Proiezione</h1>
        </div>
    </div>

    <div class="parte-centrale">
        <div class="data">
            <h1>Data selezionata: </h1>
            <h2><%=data%></h2>
        </div>
        <div class="film">
            <h1>Film selezionato: </h1>
            <h2><%=film.getTitolo()%></h2>
        </div>
        <div class="sala">
            <h1>Sala selezionata: </h1>
            <h2><%=sala%></h2>
        </div>

    </div>

    <div class="parte-inferiore">
        <h1>Orari disponibili:</h1>
        <div class="bottoni">
        <% int k = 0;
            for(String chiave : orari.keySet()) {
                Boolean valore = orari.get(chiave);
                if(valore.equals(false)){%>
                    <button id="bottone<%=k%>" name="<%=chiave%>" type="button" class="bottoneVerde bottone" style="background-color: rgb(3, 122, 3);" onclick="setColore(<%=k%>)"><%=chiave%></button>
                <%}else{%>
                    <button id="bottone<%=k%>" type="button" class="bottoneRosso"><%=chiave%></button>
                <%}
                k++;
            }%>
        </div>

    </div>
        <button id="submit1" type="button" onclick="inviaOrari()">Conferma</button>
</div>

<form id="formID" name="proiezioneForm" action="Dispatcher" method="post">
    <input id="inputPro" type="hidden" name="controllerAction" value="AdminManagement.creaProiezione"/>
    <input id="stringa" type="hidden" name="orari" value="">
    <input type="hidden" name="sala" value="<%=sala%>">
    <input type="hidden" name="data" value="<%=data%>">
    <input type="hidden" name="filmID" value="<%=film.getFilmID()%>">
</form>

<script>
    function setColore(bottoneID){

        var bottone =  document.getElementById("bottone"+ bottoneID);

        if(bottone.style.backgroundColor === 'rgb(3, 122, 3)'){
            bottone.style.backgroundColor = "orange";
        }else{
            bottone.style.backgroundColor = 'rgb(3, 122, 3)';
        }

    }

    function inviaOrari(){

        var stringa = document.getElementById("stringa");
        var bottoni = document.querySelectorAll('.bottone');
        var form = document.getElementById("formID");

        bottoni.forEach(function(elemento) {

            var backgroundColor = window.getComputedStyle(elemento).backgroundColor;

            if (backgroundColor === "orange" || backgroundColor === 'rgb(255, 165, 0)') {
               stringa.value = stringa.value + elemento.name + "-";
            }
        });
        form.submit();
    }
</script>

</main>

<%@include file="/include/footer.inc"%>

</body>
</html>
