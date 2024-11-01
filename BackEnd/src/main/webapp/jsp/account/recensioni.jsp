<%@page session="false"%>
<%@page import="com.cinema.cin.model.mo.User"%>
<%@ page import="java.util.*" %>
<%@ page import="com.cinema.cin.model.mo.Review" %>
<%String contextPath=request.getContextPath();%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    User loggedUser = (User) request.getAttribute("loggedUser");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Account: Recensioni";
    User usr = (User) request.getAttribute("utente");
    List<Review> reviews = (List<Review>) request.getAttribute("recensioni");
%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<head>

    <link rel="icon" type="image/x-icon" href="images/popcorn.ico">
    <link rel="stylesheet" href="css/General_settings.css" type="text/css" media="screen">
    <link rel="stylesheet" href="css/Header_and_Nav.css" type="text/css" media="screen">
    <link rel="stylesheet" href="css/Account/Account_recensioni.css" type="text/css" media="screen">
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

    <style>
        .star {
            color: yellow;
            height: 20px;
            width: 20px;
        }
    </style>

</head>

<body>

<!-- Defining the header and nav section of the page -->
<%@include file="/include/header_and_nav_account.inc"%>

<main>

<div class="account">
    <div class="titoloAccount">
        <h1>Account</h1>
        <h1>Recensioni</h1>
    </div>
</div>

    <div class="container">

        <%if(reviews.isEmpty()){%>
        <div class="noTicket">
            <h1 style="
                text-align: center;
                color: whitesmoke;"
            >Nessuna recensione presente!</h1>
        </div>

        <%}else{%>

        <div class="cercaFilm">
            <h1>Cerca: </h1>
            <input type="text" id="cercaFilm" placeholder="Cerca..." onkeyup="trovaRec()">
        </div>

        <div class="Contenitore">

            <%for (int i = 0; i < reviews.size(); i++) {%>

            <div class="tuttiRecensioni" id="Recensione_<%=i%>">
                <div class="informazioni">
                    <img src="images/locandine/<%=reviews.get(i).getFilm().getLocandina()%>">
                    <div class="scritte">
                        <div class="titolo">
                            <h2><%=reviews.get(i).getFilm().getTitolo()%></h2>
                            <i class="fa-solid fa-xmark" onclick="eliminaRew('<%=reviews.get(i).getRew_id()%>')"></i>
                        </div>
                        <h2>Titolo rec: <%=reviews.get(i).getTitolo()%></h2>
                        <h2>Voto: <%=reviews.get(i).getVoto()%></h2>
                        <h2>Data: <%=reviews.get(i).getData()%></h2>
                        <h2>Testo: <%=reviews.get(i).getTesto()%></h2>
                    </div>
                </div>
            </div>

            <%}%>

        </div>
        <%}%>

</div>

    <form id="formEliminaRec" action="Dispatcher" method="post">
        <input id="inputType" type="hidden" name="controllerAction" value="AccountManagement.eliminaRew"/>
        <input id="formRewID" type="hidden" name="reviewID" value="">
    </form>

    <!-- Script per filtrare le recensioni -->
    <script>
        function trovaRec() {
            var input = document.getElementById('cercaFilm');
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

    <!-- Script che elimina la recesione -->
    <script>

        function eliminaRew(id){

            var input = document.getElementById("formRewID");
            var form = document.getElementById('formEliminaRec');

            input.value = id;
            form.submit();
        }


    </script>



</main>

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
