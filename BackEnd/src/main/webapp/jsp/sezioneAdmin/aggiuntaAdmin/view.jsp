<%@page session="false"%>
<%@page import="com.cinema.cin.model.mo.Admin"%>
<%@ page import="java.util.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    Admin loggedAdmin = (Admin) request.getAttribute("loggedAdmin");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Home - Admin";

%>
<html>

<head>
    <link rel="icon" type="image/x-icon" href="images/popcorn.ico">
    <link rel="stylesheet" href="css/General_settings.css" type="text/css" media="screen">
    <link rel="stylesheet" href="css/Header_and_Nav.css" type="text/css" media="screen">
    <link rel="stylesheet" href="css/Admin/aggiuntaAdmin/View.css" type="text/css" media="screen">

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
            <h1 id="titoloSeleziona">Nuovo ADMIN</h1>
        </div>
    </div>

    <div class="form-box">

        <h1 id="Titles">Compila i seguenti campi</h1>

    <form name="addAdminForm" action="Dispatcher" method="post">
        <input id="inpuType" type="hidden" name="controllerAction" value="AdminManagement.nuovoAdmin"/>

        <div class="input-group">

            <div class="input-filed" id="inputEmail">
                <i class="fa-solid fa-envelope"></i>
                <input type="email" name="campoEmail" id="campoEmail" placeholder="Email" maxlength="40" required>
            </div>

            <div class="input-filed" id="nameFiled">
                <i class="fa-solid fa-user"></i>
                <input name="campoNome" type="text" id="campoNome" placeholder="Nome" maxlength="40">
            </div>


            <div class="input-filed" id="inputPassword">
                <i class="fa-solid fa-key"></i>
                <input type="password" name="campoPassword" id="campoPassword" placeholder="Password" maxlength="40" onblur="minCaratteri()" required>
            </div>

            <div class="input-filed" id="nameFiled3">
                <i class="fa-solid fa-key"></i>
                <input type="password" id="campoPassword2"  placeholder="Conferma Password" maxlength="40" onblur="validate()" >
            </div>

            <div class="rootBox">
                <h2>ACCESSO ROOT?</h2>
                <input type="checkbox" id="root" name="rootOption" value="">
            </div>

            <div class="btn-filed1">
                <button id="bottone" type="submit">Conferma</button>
                <h2 id="scritta" style="display: none;">Le password non coincidono!</h2>
            </div>

        </div>
    </form>

    <script>

        var campoPassword = document.getElementById("campoPassword");
        var campoPassword2 = document.getElementById("campoPassword2");

        function minCaratteri(){

                if(campoPassword.value.length < 5){
                    alert("Minimo 5 caratteri per il campo PASSWORD!");
                    bottone.style.pointerEvents = "none";
                    verificato = false;
                    campoPassword2.disabled =  true;

                }else{
                    verificato = true;
                    bottone.style.pointerEvents = "auto";
                    campoPassword2.disabled =  false;
                }
        }

        function validate() {

            var bottone = document.getElementById("bottone");
            var scritta = document.getElementById("scritta");

            if (campoPassword.value !== "" && campoPassword2.value !== "") {
                if (campoPassword.value === campoPassword2.value) {
                    bottone.style.pointerEvents = "auto";
                    scritta.style.display = "none";
                } else {
                    bottone.style.pointerEvents = "none";
                    scritta.style.display = "block";
                    scritta.style.color = "rgb(255, 102, 102)";
                    alert("Le password non coincidono");

                }
            }
        }

    </script>

</div>

</main>

<%@include file="/include/footer.inc"%>

</body>
</html>
