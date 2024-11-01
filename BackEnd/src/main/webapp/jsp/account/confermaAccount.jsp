<%@page session="false"%>
<%@page import="com.cinema.cin.model.mo.User"%>
<%@ page import="java.util.*" %>
<%String contextPath=request.getContextPath();%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    User loggedUser = (User) request.getAttribute("loggedUser");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Conferma Account";
%>


<!DOCTYPE html>
<head>

    <link rel="icon" type="image/x-icon" href="images/popcorn.ico">
    <link rel="stylesheet" href="css/General_settings.css" type="text/css" media="screen">
    <link rel="stylesheet" href="css/Header_and_Nav.css" type="text/css" media="screen">
    <link rel="stylesheet" href="css/Account/Account_confermaAccount.css" type="text/css" media="screen">

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

</head>

<body>

<!-- Defining the header and nav section of the page -->
<%@include file="/include/header_and_nav_account.inc"%>

<main>

    <div class="account">
        <div class="titoloAccount">
            <h1>Account</h1>
            <h1>Conferma account</h1>
        </div>
    </div>

    <div class="conferma">

        <div class="testoConferma">
    <h2>INSERISCI IL TOKEN PER<br>CONFERMARE L'ACCOUNT</h2>
        </div>

        <div class="box">
            <form id="conferma" action="Dispatcher" method="post">
                <input type="hidden" name="controllerAction" value="AccountManagement.confermaAccount"/>
                <input type="hidden" name="token" id="campoToken">
            <h1 class="testoToken">Inserisci il TOKEN:</h1>
                <div class="token-code">
                <input type="text"  class="campotoken" id="1" maxlength="1" required>
                <input type="text"  class="campotoken" id="2" maxlength="1" required>
                <input type="text"  class="campotoken" id="3" maxlength="1" required>
                <input type="text"  class="campotoken" id="4" maxlength="1" required>
                <input type="text"  class="campotoken" id="5" maxlength="1" required>
                </div>
            </form>
        </div>

        <div class="bottoneDiv">
            <button id="bottone" type="button" onclick="inviaForm()">Conferma</button>
        </div>

    </div>

    <!-- funzione per creare la stringa del token e submittare -->
    <script>

        function inviaForm(){

            var token = document.getElementById("campoToken");
            var stringa = "";

            for(i=1; i<6; i++){
                stringa = stringa + document.getElementById(i).value;
            }

            token.value = stringa;
            form = document.getElementById("conferma");
            form.submit();
        }

    </script>

    <!-- funzione per gestire i vari input -->
    <script>
        document.getElementById('bottone').enabled = false;
        const inputTag = document.querySelectorAll('.campotoken');
        const btn = document.getElementById("bottone");
        inputTag.forEach((item) => {
            item.value = '';
            let prev = null
            if (item.id != '1') {
                document.querySelectorAll('.campotoken').forEach((item_1) => {if (parseInt(item.id)-1 === parseInt(item_1.id)) prev = item_1});
            }
            let next = null
            if (item.id != '5') {
                document.querySelectorAll('.campotoken').forEach((item_1) => {if (parseInt(item.id)+1 === parseInt(item_1.id)) next = item_1});
            }
            item.addEventListener('focus', (evt) => {
                if (prev && prev.value == '') {
                    prev.focus();
                }
            });
            item.addEventListener('keyup', (evt) => {
                let lista = [
                    '0','1','2','3','4','5','6','7','8','9',
                    'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
                    'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z']

                if (!lista.includes(item.value)) {
                    item.value = '';
                } else {
                    if (next) {
                        next.focus();
                    }
                }
                if (evt.keyCode === 8 && prev){
                    prev.focus();
                }

                let disabled = true;

                inputTag.forEach((item_1) => {
                    if (item_1.value === '') {
                        disabled = false
                    }
                })
                document.getElementById('bottone').enabled = disabled;
            })
        })

    </script>


</main>

<!-- footer section -->
<%@include file="/include/footer.inc"%>

<script>
    document.getElementById("cartItems").innerHTML = getValore(getCookie('cart'), 1);
    document.getElementById("carrelloID").value = getValore(getCookie('cart'), 0);
</script>

</body>
</html>
