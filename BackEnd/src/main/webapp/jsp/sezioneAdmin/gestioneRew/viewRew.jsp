<%@page session="false"%>
<%@page import="com.cinema.cin.model.mo.Admin"%>
<%@ page import="java.util.*" %>
<%@ page import="com.cinema.cin.model.mo.Film" %>
<%@ page import="com.cinema.cin.model.mo.Review" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    Admin loggedAdmin = (Admin) request.getAttribute("loggedAdmin");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Gestione Recensioni";
    ArrayList<Review> reviews = (ArrayList<Review>) request.getAttribute("listaRew");
    String titoloFilm = (String) request.getAttribute("titoloFilm");
%>
<html>
<head>
    <link rel="icon" type="image/x-icon" href="images/popcorn.ico">
    <link rel="stylesheet" href="css/General_settings.css" type="text/css" media="screen">
    <link rel="stylesheet" href="css/Header_and_Nav.css" type="text/css" media="screen">
    <link rel="stylesheet" href="css/Admin/Recensioni/viewRew.css" type="text/css" media="screen">
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

    <style>
        .hidden{
            display: none;
        }


    </style>


</head>
<body>

<%@include file="/include/header_and_nav_admin.inc"%>

<main>

    <div class="account">
        <div class="titoloAccount">
            <h1>ADMIN</h1>
            <h1 id="titoloSeleziona">Visualizza Recensioni: </h1>
            <h1><%=titoloFilm%></h1>
        </div>
    </div>


    <div class="container">

        <div class="titolo">
            <a href="Dispatcher?controllerAction=AdminManagement.viewRew">
                <div class="superiore">
            <div class="freccia">
                <i class="fa-solid fa-arrow-left"></i>
            </div>
            <h1>Pagina Precedente</h1>
        </div>
            </a>
            <%if(reviews.isEmpty()){%>
            <div class="noTicket">
                <h1>Nessuna recensione presente!</h1>
            </div>
            <%}%>
        </div>

        <%if(!reviews.isEmpty()){%>

        <div class="parte-superiore">

            <div class="cercaFilm">
                <h1>Ricerca parola:</h1>
                <input type="text" id="searchInput" placeholder="Cerca ..." onkeyup="filterReviews()">
            </div>

            <div class="selezionaRew">
                <h1>Seleziona recensioni da cancellare:</h1>
                <div class="bottoniTop">
                    <button id="bottoneCanc" type="button" onclick="smistatore()">Seleziona</button>
                    <button id="submit1" type="button" onclick="inviaRew()">Cancella</button>
                </div>
            </div>

        </div>

        <div class="Contenitore">

            <%for (int i = 0; i < reviews.size(); i++) {%>

            <div class="tuttiRecensioni recensione" id="Recensione_<%=i%>">
                <div class="informazioni ">
                    <img src="images/locandine/<%=reviews.get(i).getFilm().getLocandina()%>">
                    <div class="scritte">
                        <div class="titolo">
                            <h2><%=reviews.get(i).getTitolo()%></h2>
                            <i class="fa-solid fa-trash bidone hidden" onclick="deleteRew('<%=i%>')"></i>
                        </div>
                        <h2>Voto: <%=reviews.get(i).getVoto()%></h2>
                        <h2>Data: <%=reviews.get(i).getData()%></h2>
                        <h2>Testo: <%=reviews.get(i).getTesto()%></h2>
                        <input class="valore" type="hidden" id="<%=i%>" value="">
                        <input id="rew<%=i%>" type="hidden" value="<%=reviews.get(i).getRew_id()%>">
                    </div>
                </div>
            </div>

            <%}%>

        </div>
        <%}%>

    </div>

    <%if(!reviews.isEmpty()){%>
    <form id="formRew" action="Dispatcher" method="post">
        <input type="hidden" name="controllerAction" value="AdminManagement.cancellaRew"/>
        <input type="hidden" name="stringaID" id="stringaID" value="">
    </form>
    <%}%>
</main>

<!-- Script per gestione bottoni -->
<script>

    var bottoneCanc = document.getElementById("bottoneCanc");
    var contatore = 0;


    function smistatore(){
        if(contatore === 0){
            contatore = 1;
            viewDelete();

        } else if(contatore === 1){
            contatore = 0;
            hiddenDelete();
        }
    }
    function viewDelete(){

        document.querySelectorAll('.bidone').forEach((item) =>{
            item.classList.toggle('hidden');
        });

        bottoneCanc.style.backgroundColor = "green";
        bottoneCanc.innerHTML = "Conferma";






    }
    function hiddenDelete(){

        document.querySelectorAll('.bidone').forEach((item) =>{
            item.classList.toggle('hidden');
        });

        bottoneCanc.style.backgroundColor = "#bd1daf";
        bottoneCanc.innerHTML = "Seleziona";


    }

</script>

<!-- Script che gestisce il recupero degli id delle recensioni -->
<script>
    function deleteRew(induce){


        var inputRew = document.getElementById(induce);
        var divRew = document.getElementById("Recensione_"+induce);

        if(inputRew.value === ""){
            inputRew.value = "1";
            divRew.style.backgroundColor = 'rgba(173, 35, 35, 0.66)'

        }else{
            inputRew.value = "";
            divRew.style.backgroundColor = "transparent"
        }
    }
</script>

<!-- Script che invia gli id delle recensioni -->
<script>
    function inviaRew(){

        var items = document.querySelectorAll('.valore');
        var stringa="";
        var formRew = document.getElementById('formRew');
        var inputStringa = document.getElementById('stringaID');

        items.forEach((item) =>{
            if(item.value === "1"){
                var id = item.id;
                stringa = stringa + document.getElementById("rew"+id).value + "-";
            }
        });
        inputStringa.value = stringa;

        if(stringa === ""){

            alert("Seleziona almeno una recensione da cancellare!");

        }else{

            formRew.submit();

        }

    }
</script>

<!-- Script per filtrare le recensioni -->
<script>
        function filterReviews() {
        var input = document.getElementById('searchInput');
        var filter = input.value.toLowerCase();
        var reviews = document.querySelectorAll('.tuttiRecensioni');

        reviews.forEach(function(review) {
        var tutto_test = review.querySelector('.titolo h2').innerHTML + " ";
        review.querySelectorAll('.informazioni h2').forEach((item) => {
        if (item.innerHTML.split('/').length !== 3) {
        tutto_test = tutto_test + item.innerHTML.split(":")[1];
    }
    })
        tutto_test = tutto_test.toLowerCase();
        if (tutto_test.includes(filter)) {
        review.style.display = "";

    } else if(filter === '') {
        review.style.display = "";


    }else{
        review.style.display = "none";

    }
    });
    }

</script>

<%@include file="/include/footer.inc"%>

</body>
</html>
