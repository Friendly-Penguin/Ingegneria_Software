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
%>
<html>

<head>
    <link rel="icon" type="image/x-icon" href="images/popcorn.ico">
    <link rel="stylesheet" href="css/General_settings.css" type="text/css" media="screen">
    <link rel="stylesheet" href="css/Header_and_Nav.css" type="text/css" media="screen">
    <link rel="stylesheet" href="css/Admin/aggiungiProiezione/View.css" type="text/css" media="screen">

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
            <h1>Admin</h1>
            <h1 id="titoloSeleziona">Inserisci una nuova proiezione</h1>
        </div>
    </div>

    <div class="destra">
        <form id="formID" name="proiezioneForm" action="Dispatcher" method="post">
            <div class="select">
                <input id="inputPro" type="hidden" name="controllerAction" value="AdminManagement.selezionaOrario"/>
                <div class="Testi">
                    <h1>Data: </h1>
                    <h1>Film: </h1>
                    <h1>Sala: </h1>
                </div>
                <div class="input">
                    <input id="data" type="date" name="data" required>
                    <select title="film" name="filmSelezionato" id="film" form="formID">
                        <%for (int i = 0; i < listaFilm.size(); i++ ){%>
                        <option class="film_option" value="<%=listaFilm.get(i).getFilmID()%>"><%=listaFilm.get(i).getTitolo()%></option>
                        <%}%>
                    </select>
                    <select title="sala" name="salaSelezionata" id="sala" form="formID">
                        <%for (int i = 1; i < 5; i++ ){%>
                        <option  class="sala_option" value="<%=i%>"><%=i%></option>
                        <%}%>
                    </select>
                </div>
            </div>
            <button id="submit1" type="submit">Invia</button>
        </form>
    </div>

</main>

<script>
    // Ottieni l'elemento input date
    var inputDate = document.getElementById('data');

    // Ottieni la data odierna
    var oggi = new Date();

    //lo imposto a domani
    oggi.setDate(oggi.getDate() + 1);

    // Formatta la data odierna nel formato richiesto da input date (YYYY-MM-DD)
    var yyyy = oggi.getFullYear();
    var mm = String(oggi.getMonth() + 1).padStart(2, '0'); // Gennaio è 0!
    var dd = String(oggi.getDate()).padStart(2, '0');
    var dataOggi = yyyy + '-' + mm + '-' + dd;

    // Imposta l'attributo min dell'input date alla data odierna
    inputDate.setAttribute('min', dataOggi);
</script>


<%@include file="/include/footer.inc"%>

</body>
</html>
