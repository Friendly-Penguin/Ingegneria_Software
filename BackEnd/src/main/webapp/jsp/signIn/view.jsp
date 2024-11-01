<%@page import="com.cinema.cin.model.mo.User"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<%
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Registrati";
    String tipologia = "Accedi";

%>

<html>

<head>


    <!-- Linking styles -->
    <link rel="icon" type="image/x-icon" href="images/popcorn.ico">
    <link rel="stylesheet" href="css/General_settings.css" type="text/css" media="screen">
    <link rel="stylesheet" href="css/Header_and_Nav.css" type="text/css" media="screen">
    <link rel="stylesheet" href="css/Accedi/SignIN_LogON.css" type="text/css" media="screen">

    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" integrity="sha512-SnH5WK+bZxgPHs44uWIX+LLJAJ9/2PkPKZ5QiAj6Ta86w+fsb2TkcmfRyVX3pBnMFcV7oQPJkl9QevSCWr3W6A==" crossorigin="anonymous" referrerpolicy="no-referrer" />

    <title>Cineplex: <%=menuActiveLink%></title>

    <script>
        var applicationMessage;
        <%if (applicationMessage != null) {%>
        applicationMessage="<%=applicationMessage%>";
        <%}%>
        function onLoadHandler() {
            headerOnLoadHandler();
            try { mainOnLoadHandler(); } catch (e) {}
            if (applicationMessage!=undefined) alert(applicationMessage);
        }
        window.addEventListener("load", onLoadHandler);
    </script>

    <script>
        function headerOnLoadHandler() {
            var emailTextField = document.querySelector("#campoEmail");
            var emailTextFieldMsg = "Email \xE8 obbligatoria.";
            var passwordTextField = document.querySelector("#campoPassword");
            var passwordTextFieldMsg = "Password \xE8 obbligatoria.";


            if (emailTextField != undefined && passwordTextField != undefined) {
                emailTextField.setCustomValidity(emailTextFieldMsg);
                emailTextField.addEventListener("change", function () {
                    this.setCustomValidity(this.validity.valueMissing ? emailTextFieldMsg : "");
                });
                passwordTextField.setCustomValidity(passwordTextFieldMsg);
                passwordTextField.addEventListener("change", function () {
                    this.setCustomValidity(this.validity.valueMissing ? passwordTextFieldMsg : "");
                });

            }
        }
    </script>


</head>


<body>

<header>

    <div class="griglia_head">

        <div class="logo_head">
            <h1 class="logo"><a class="noUnderline" href="Dispatcher?controllerAction=HomeManagement.view">Cineplex</a></h1>
        </div>

    </div>

</header>

<main>

        <div class="form-box">

            <h1 id="Titles">Accedi</h1>

            <form name="logonForm" action="Dispatcher" method="post">

                <div class="input-group">

                    <div class="input-filed" id="inputEmail">
                        <i class="fa-solid fa-envelope"></i>
                        <input type="email" name="campoEmail" id="campoEmail" placeholder="Email" maxlength="40" required>
                    </div>

                    <div class="input-filed" id="nameFiled">
                        <i class="fa-solid fa-user"></i>
                        <input tabindex="-1" name="campoNome" type="text" id="campoNome" placeholder="Nome" maxlength="40">
                    </div>

                    <div class="input-filed" id="nameFiled1">
                        <i class="fa-solid fa-user"></i>
                        <input tabindex="-1" name="campoCognome" type="text" id="campoCognome" placeholder="Cognome" maxlength="40">

                    </div>

                    <div class="input-filed" id="inputPassword">
                        <i class="fa-solid fa-key"></i>
                        <input type="password" name="campoPassword" id="campoPassword" placeholder="Password" maxlength="40" onblur="minCaratteri()" required>
                    </div>

                    <div class="input-filed" id="nameFiled3">
                        <i class="fa-solid fa-key" type="hidden"></i>
                        <input tabindex="-1" type="password" id="campoPassword2"  placeholder="Conferma Password" maxlength="40" >
                    </div>

                    <div class="input-filed" id="nameFiled4">
                        <i class="fa-solid fa-id-card"></i>
                        <input tabindex="-1" name="campoMatricola" type="text" id="campoMatricola" maxlength="40" placeholder="Matricola">
                    </div>


                <input id="inpuType" type="hidden" name="controllerAction" value="SignInManagement.logon"/>

                <div class="btn-filed1">
                    <h1 id="scritta" style="display: none;font-size: 15px;">Le password non coincidono!</h1>
                    <button id="bottone" type="submit" id="invia" >Conferma</button>
                </div>
                <div class="btn-filed">
                    <button type="button" id="signupBtn" >Accedi</button>
                    <button type="button" id="signinBtn" class="disable">Registrati</button>
                </div>
                </div>
            </form>

            <div id="testoAdmin">
                <a href="Dispatcher?controllerAction=SignInManagement.viewAdmin">Sei un amministratore? Clicca qui!</a>
            </div>

        </div>


        <script>



            var campoEmail = document.getElementById("campoEmail");
            var signupBtn = document.getElementById("signupBtn");
            var signinBtn = document.getElementById("signinBtn");
            var campoNome = document.getElementById("campoNome");
            var campoCognome = document.getElementById("campoCognome");
            var campoPassword = document.getElementById("campoPassword");
            var campoPassword2 = document.getElementById("campoPassword2");
            var campoMatricola = document.getElementById("campoMatricola");
            var type = document.getElementById("inpuType");
            var bottone = document.getElementById("bottone");
            var scritta = document.getElementById("scritta");
            var verificato = false;

            function minCaratteri(){

                if(document.getElementById('Titles').innerHTML === "Registrati"){
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
            }

            function validate() {

                if (verificato) {
                    if (campoPassword.value === campoPassword2.value) {

                        bottone.style.pointerEvents = "auto";
                        scritta.style.display = "none";


                    } else {

                        bottone.style.pointerEvents = "none";
                        scritta.style.display = "block";
                        scritta.style.color = "rgb(255, 102, 102)";

                        alert("Le password non corrispondono!");
                    }
                }
            }


    signupBtn.onclick = function (){

        campoNome.value = "";
        campoCognome.value = "";
        campoPassword2.value = "";
        campoPassword.value = "";
        campoEmail.value="";
        campoMatricola.value = "";
        bottone.style.pointerEvents = "auto";
        scritta.style.display = "none";

        campoNome.removeAttribute('required');
        campoCognome.removeAttribute('required');
        campoPassword2.removeAttribute('required');
        campoPassword2.removeAttribute('onblur');
        campoNome.setAttribute('tabindex', '-1');
        campoCognome.setAttribute('tabindex', '-1');
        campoPassword2.setAttribute('tabindex', '-1');
        campoMatricola.setAttribute('tabindex', '-1');
        document.getElementById('nameFiled').style.maxHeight = "0";
        document.getElementById('nameFiled1').style.maxHeight = "0";
        document.getElementById('nameFiled3').style.maxHeight = "0";
        document.getElementById('nameFiled4').style.maxHeight = "0";
        document.getElementById('Titles').innerHTML = "Accedi";
        signupBtn.classList.remove("disable");
        signinBtn.classList.add("disable");
        type.setAttribute('value',"SignInManagement.logon")
        document.getElementById("testoAdmin").style.display = "unset";

    }

    signinBtn.onclick = function (){

        campoNome.value = "";
        campoCognome.value = "";
        campoPassword2.value = "";
        campoPassword.value = "";
        campoEmail.value="";
        campoMatricola.value = "";
        bottone.style.pointerEvents = "auto";
        scritta.style.display = "none";


        campoNome.setAttribute('required', 'required');
        campoCognome.setAttribute('required', 'required');
        campoPassword2.setAttribute('required', 'required');
        campoPassword2.setAttribute('onblur','validate()');
        campoNome.removeAttribute('tabindex');
        campoCognome.removeAttribute('tabindex');
        campoPassword2.removeAttribute('tabindex');
        campoMatricola.removeAttribute('tabindex');
        document.getElementById('nameFiled').style.maxHeight = "60px";
        document.getElementById('nameFiled1').style.maxHeight = "60px";
        document.getElementById('nameFiled3').style.maxHeight = "60px";
        document.getElementById('nameFiled4').style.maxHeight = "60px";
        document.getElementById('Titles').innerHTML = "Registrati";
        signupBtn.classList.add("disable");
        signinBtn.classList.remove("disable");
        type.setAttribute('value',"SignInManagement.create")
        document.getElementById("testoAdmin").style.display = "none";

    }


</script>

    </main>


</body>

</html>
