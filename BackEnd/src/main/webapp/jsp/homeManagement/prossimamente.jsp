<%@page session="false"%>
<%@page import="com.cinema.cin.model.mo.User"%>
<%@ page import="com.cinema.cin.model.mo.Film" %>
<%@ page import="java.util.*" %>
<%@ page import="com.cinema.cin.model.mo.Proiezione" %>
<%@ page import="javax.xml.crypto.Data" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%String contextPath=request.getContextPath();%>


<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    User loggedUser = (User) request.getAttribute("loggedUser");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Prossimamente";
    List<Film> films = (List<Film>) request.getAttribute("listaFilm");

%>


<!DOCTYPE html>

<html>
<head>
    <link rel="icon" type="image/x-icon" href="images/popcorn.ico">
    <link rel="stylesheet" href="css/General_settings.css" type="text/css" media="screen">
    <link rel="stylesheet" href="css/Header_and_Nav.css" type="text/css" media="screen">
    <link rel="stylesheet" href="css/Home/Home.css" type="text/css" media="screen">

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

    <!-- funzione per visualizzare la pagina del film -->
    <script language="javascript">

        function viewFilm(filmID) {
            var f = document.getElementById("filmForm");
            f.elements["filmID"].value = filmID;
            f.submit();
        }


    </script>

</head>
<body>

<%@include file="/include/header_and_nav.inc"%>

<main>

    <div class="inSala">
        <h1>PROSSIMAMENTE</h1>
    </div>

    <%for (int i = 0; i < films.size(); i++) {

        if (i % 2 == 0 && i == 0) {%>

    <div class="griglia">

        <%} else if (i % 2 == 0) {%>

    </div>
    <div class="griglia">
        <%}%>

        <div class="casella_film item_<%=i%> grigliaFilm">
            <div class="sez_sup">
                <div class="Img">
                    <img title="img<%=i%>" class="contenutoImg" src="images/locandine/<%=films.get(i).getLocandina()%>">
                </div>
                <div class="Title_Description">
                    <div class="Title">
                        <form id="filmForm" action="Dispatcher" method="post">
                            <input id="inpuType" type="hidden" name="controllerAction" value="ViewFilmManagement.view"/>
                            <input type="hidden" name="filmID" value="">
                            <h1><a id="link_film" href="javascript:viewFilm('<%=(films.get(i).getFilmID())%>')"><%=films.get(i).getTitolo()%></a></h1>
                        </form>
                    </div>
                    <div class="Info">
                        <b>Informazioni</b></br>
                        <b>Regista</b>: <%=films.get(i).getRegista()%></br>
                        <b>Durata</b>: <%=films.get(i).getDurata()%> </br>
                        <b>Genere</b>: <%=films.get(i).getGenere()%>
                    </div>
                </div>
            </div>

            <div class="sez_inf">

                <button type="button" class="apri_Trailer but<%=i%>" id="<%=i%>">Trailer</button>
                <div class="proiezioni">
                    <h1>─── Proiezioni ───</h1>
                    <h2>In aggiornamento</h2>
                </div>

        <div class="trailer-container active conta<%=i%>">
            <iframe class="schermo"  width="80%" src="<%=films.get(i).getLink_trailer()%>" allowfullscreen></iframe>
            <i class="fa-solid fa-xmark close-icon" id="icon<%=i%>" ></i>
        </div>
            </div>
    </div>
    <%} if (films.size() % 2 == 1) {%>
    </div>
    <%}%>

</main>

<%@include file="/include/footer.inc"%>

<%if(loggedOn){%>
<script>
    document.getElementById("cartItems").innerHTML = getValore(getCookie('cart'), 1);
    document.getElementById("carrelloID").value = getValore(getCookie('cart'), 0);
</script>
<%}%>

<!-- script per visualizzare/nascondere il trailer -->
<script>
    document.querySelectorAll('.apri_Trailer').forEach((elem) => {
        elem.onclick = function () {
            id = elem.id
            document.querySelector('.conta'+id).classList.remove("active");
            document.getElementById("footer").style.display = "none";
            document.getElementById("formConf").style.zIndex = "-2";
        }
    })
    document.querySelectorAll('.close-icon').forEach((elem) => {
        elem.onclick = function () {
            id = elem.id.replace('icon','');
            document.querySelector('.conta'+id).classList.add("active");
            document.getElementById("footer").style.display = "block";
            document.getElementById("formConf").style.zIndex = "1";
        }
    })

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
