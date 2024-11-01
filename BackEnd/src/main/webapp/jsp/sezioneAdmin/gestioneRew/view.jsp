<%@page session="false"%>
<%@page import="com.cinema.cin.model.mo.Admin"%>
<%@ page import="java.util.*" %>
<%@ page import="com.cinema.cin.model.mo.Film" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    Admin loggedAdmin = (Admin) request.getAttribute("loggedAdmin");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Gestione Recensioni";
    ArrayList<Film> listaFilm = (ArrayList<Film>) request.getAttribute("listaFilm");
%>
<html>
<head>
    <link rel="icon" type="image/x-icon" href="images/popcorn.ico">
    <link rel="stylesheet" href="css/General_settings.css" type="text/css" media="screen">
    <link rel="stylesheet" href="css/Header_and_Nav.css" type="text/css" media="screen">
    <link rel="stylesheet" href="css/Admin/Recensioni/View.css" type="text/css" media="screen">

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
        <h1>ADMIN</h1>
        <h1 id="titoloSeleziona">Visualizza Recensioni</h1>
    </div>
</div>


<div class="contenitore">

        <div class="freccia">
            <a href="Dispatcher?controllerAction=AdminManagement.view">
                <i class="fa-solid fa-arrow-left" ></i>
            </a>
        </div>

    <div class="cerca">

        <div class="titolo">
            <h1>Scegli un FILM:</h1>
        </div>

        <form id="formID" name="proiezioneForm" action="Dispatcher" method="post">
            <input id="inputPro" type="hidden" name="controllerAction" value="AdminManagement.gestisciRew"/>
            <select name="filmSelezionato" id="film" form="formID">
                <%for (int i = 0; i < listaFilm.size(); i++ ){%>
                <option class="film_option" value="<%=listaFilm.get(i).getFilmID()%>"><%=listaFilm.get(i).getTitolo()%></option>
                 <%}%>
            </select>
        </form>
    </div>
    <button id="submit1" type="button" onclick="formSubmit()">Ricerca</button>
</div>

    <script>
        function formSubmit(){
            var form = document.getElementById("formID");
            form.submit();
        }
    </script>

</main>

<%@include file="/include/footer.inc"%>

</body>
</html>
