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
    <link rel="stylesheet" href="css/Admin_newFilm.css" type="text/css" media="screen">
    <link rel="stylesheet" href="css/Admin/aggiuntaFilm/View.css" type="text/css" media="screen">

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
            <h2>Aggiunta FILM</h2>
        </div>
    </div>


    <div class="contenitore">

        <div class="anteprima">

            <div class="sinistra">
                <h1>Compila i seguenti campi:</h1>
                <form action="Dispatcher" method="post" id="campiForm">
                    <input type="hidden" name="controllerAction" value="AdminManagement.aggiungiFilm"/>
                    <input type="text" name="titolo" id="titolo" placeholder="Titolo" required onkeydown="scrivere(event, 'titolo')">
                    <input type="text" name="regista" id="regista" placeholder="Regista" required onkeydown="scrivere(event, 'regista')">
                    <input type="text" name="durata" id="durata" placeholder="Durata HH:MM:SS" required onkeydown="scrivere(event, 'durata')">
                    <input type="text" name="genere" id="genere" placeholder="Genere" required onkeydown="scrivere(event,'genere')">
                    <input type="text" name="link_trailer" id="link_trailer" placeholder="Youtube Link" required>
                    <textarea type="text" name="trama" id="trama" placeholder="Trama" maxlength="500" required onkeydown="scrivere(event, 'trama')"></textarea>
                    <input type="text" id="contatoreParole" value="">
                    <input type="hidden" id="nomeFile" name="nomeFile" value="">
                </form>

                <form id="imgForm" action="upload" method="post" enctype="multipart/form-data">
                    <h1>Locandina:</h1>
                    <input type="file" id="fileInput" name="photo" accept="image/*" required>
                </form>
            </div>

            <div class="destra">
                <h1>Anteprima</h1>
                <div class="campi">
                    <div class="immagineBut">
                        <img id="imgPre" src="images/placeholder.jpg">
                        <button type="button" class="apri_Trailer" id="bottoneTrailer" onclick="apriTrailer()">Trailer</button>
                        <div class="trailer-container conta active">
                            <iframe id="iframe" class="schermo"  width="80%" src="" allowfullscreen></iframe>
                            <i class="fa-solid fa-xmark close-icon" id="icon" ></i>
                        </div>
                    </div>
                    <div class="informazioni">
                        <div>
                            <h1>Titolo: </h1>
                            <h2 id="titoloPre">Titolo</h2>
                        </div>
                            <h1 id="infoTit">Informazioni</h1>
                        <div>
                            <h1>Regista: </h1>
                            <h2 id="registaPre">Regista</h2>
                        </div>
                        <div>
                            <h1>Durata: </h1>
                            <h2 id="durataPre">Durata</h2>
                        </div>

                        <div>
                            <h1>Genere: </h1>
                            <h2 id="generePre">Genere</h2>
                        </div>

                        <div style="display: flex;
                             flex-direction: column;">
                            <h1>Trama: </h1>
                            <textarea id="tramaPre" readonly></textarea>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div >
            <button id="bottoneInv" type="submit" onclick="invia()">Carica</button>
        </div>
    </div>

    <script>

        var input_trailer = document.getElementById("link_trailer");

        function apriTrailer(){
            if(input_trailer.value === ''){
                alert("Inserisci un trailer per poterlo visualizzare!");

            }else{
                document.querySelectorAll('.apri_Trailer').forEach((elem) => {
                    elem.onclick = function () {
                        document.querySelector('.conta').classList.remove("active");
                    }
                })
                document.querySelectorAll('.close-icon').forEach((elem) => {
                    elem.onclick = function () {
                        document.querySelector('.conta').classList.add("active");
                    }
                })
            }
        }

    </script>

    <!-- funzione per CTRL + V -->
    <script>

        document.getElementById("link_trailer").addEventListener("paste", handlePaste);

        function handlePaste(event) {
            
            var linkBottone = document.getElementById("iframe");
            var trailerInput = document.getElementById("link_trailer");

            // Ottieni i dati incollati dal clipboard
            var pastedData = (event.clipboardData || window.clipboardData).getData('text');
            var newValue = trailerInput.value + pastedData;

            if (newValue.length <= 100) {
                trailerInput.value = newValue;
                linkBottone.src = newValue;
            }

            if (trailerInput.value.length === 0) {
                linkBottone.src = "";
            }
            // Previeni l'azione di incollaggio predefinita
            event.preventDefault();

        }


    </script>

    <script>
        document.getElementById("link_trailer").addEventListener("keydown", handleKeyEvents);

        function handleKeyEvents(event) {

            if (event !== "ClipboardEvent")
            event.preventDefault();

            var linkBottone = document.getElementById("iframe");
            var trailerInput = document.getElementById("link_trailer");

            switch (event.key) {
                case "Backspace":
                    const start = trailerInput.selectionStart;
                    const end = trailerInput.selectionEnd;
                    if (start !== end) {
                        let stringA  = trailerInput.value.substring(0, start);
                        trailerInput.value = stringA;
                        linkBottone.src = trailerInput.value;
                        break;
                    } else {
                        let stringa = trailerInput.value.substring(0, trailerInput.value.length - 1);
                        trailerInput.value = stringa;
                        linkBottone.src = stringa;
                        break;
                    }
                case "Tab":
                    document.getElementById('trama').focus();
                    break;
                default:
                    if(event.ctrlKey && event.key === 'v'){
                        navigator.clipboard.readText()
                            .then((pasteData) => {
                            trailerInput.value = pasteData;
                            linkBottone.src = pasteData;
                        })
                        break;
                    }else if ("abcdefghijklmonpqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890:._/?=".includes(event.key) && trailerInput.value.length < 100) {
                        trailerInput.value = trailerInput.value + event.key;
                        linkBottone.src = trailerInput.value;
                    }
                    break;
            }

            if (trailerInput.value.length === 0) {
                linkBottone.src = "";
            }
        }
    </script>

<!-- funzione per la visualizzazione di preview -->
<script>

    //campi di input sinistra
    var inputImg = document.getElementById("fileInput");


    //campi preview destra
    var preImg = document.getElementById("imgPre");

    inputImg.onchange = function () {
        preImg.src = URL.createObjectURL(inputImg.files[0]);
    }

    function scrivere(event, tipo) {

        event.preventDefault();

        switch (tipo) {
            case "titolo": {
                var preTitolo = document.getElementById("titoloPre");
                if ("abcdefghijklmonpqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ 1234567890:".includes(event.key)) {
                    document.getElementById("titolo").value += event.key;
                    preTitolo.innerHTML = document.getElementById("titolo").value;
                } else if (event.key == "Backspace") {
                    document.getElementById("titolo").value = document.getElementById("titolo").value.substring(0, document.getElementById("titolo").value.length - 1);
                    preTitolo.innerHTML = document.getElementById("titolo").value;
                } else if (event.key == "Tab") {
                    document.getElementById('regista').focus();
                }

                if (document.getElementById("titolo").value.length == 0) {
                    document.getElementById("titolo").value = "";
                    preTitolo.innerHTML = "Titolo";

                }
                break;
            }
            case "genere": {
                var preGenere = document.getElementById("generePre");
                if ("abcdefghijklmonpqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ ,".includes(event.key)) {
                    document.getElementById("genere").value += event.key;
                    preGenere.innerHTML = document.getElementById("genere").value;
                } else if (event.key == "Backspace") {
                    document.getElementById("genere").value = document.getElementById("genere").value.substring(0, document.getElementById("genere").value.length - 1);
                    preGenere.innerHTML = document.getElementById("genere").value;
                } else if (event.key == "Tab") {
                    document.getElementById('link_trailer').focus();
                }

                if (document.getElementById("titolo").value.length == 0) {
                    document.getElementById("titolo").value = "";
                    preTitolo.innerHTML = "Titolo";

                }
                break;
            }
            case "durata": {
                var preDurata = document.getElementById("durataPre");
                value = document.getElementById("durata").value.replaceAll(":","");

                if ("1234567890".includes(event.key) && value.length < 6) {
                    value += event.key;


                } else if (event.key == "Backspace") {
                    value = value.substring(0, value.length - 1);


                } else if (event.key == "Tab") {
                    document.getElementById('genere').focus();
                }

                if (value.length > 2) {
                    value = value.substring(0, 2) + ":" + value.substring(2, value.length);
                }
                if (value.length > 5) {
                    value = value.substring(0, 5) + ":" + value.substring(5, value.length);
                }


                if (value.length == 0) {
                    document.getElementById("durata").value = "";
                    preDurata.innerHTML = "Durata HH:MM:SS";
                }else{
                    document.getElementById("durata").value = value;
                    preDurata.innerHTML = value;
                }
                break;
            }
            case "regista": {
                var preRegista = document.getElementById("registaPre");
                if ("abcdefghijklmonpqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ ".includes(event.key) && document.getElementById("regista").value.length < 50) {
                    document.getElementById("regista").value += event.key;
                    preRegista.innerHTML = document.getElementById("regista").value;
                } else if (event.key == "Backspace") {
                    document.getElementById("regista").value = document.getElementById("regista").value.substring(0, document.getElementById("regista").value.length - 1);
                    preRegista.innerHTML = document.getElementById("regista").value;
                } else if (event.key == "Tab") {
                    document.getElementById('durata').focus();
                }

                if (document.getElementById("regista").value.length == 0) {
                    document.getElementById("regista").value = "";
                    preRegista.innerHTML = "Regista";

                }
                break;
            }
            case "trama": {
                var preTrama = document.getElementById("tramaPre");
                var contatore = document.getElementById("contatoreParole");
                if ("abcdefghijklmonpqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ 1234567890:.\"".includes(event.key) && document.getElementById("trama").value.length < 500) {
                    document.getElementById("trama").value += event.key;
                    preTrama.innerHTML = document.getElementById("trama").value;
                } else if (event.key == "Backspace") {
                    document.getElementById("trama").value = document.getElementById("trama").value.substring(0, document.getElementById("trama").value.length - 1);
                    preTrama.innerHTML = document.getElementById("trama").value;
                } else if (event.key == "Tab") {
                    document.getElementById('titolo').focus();
                }

                if (document.getElementById("trama").value.length == 0) {
                    document.getElementById("trama").value = "";
                    preTrama.innerHTML = "Trama";

                }
                contatore.value = document.getElementById("trama").value.length + "/" + "500";
                break;
            }
        }
    }
</script>

<!-- submit delle due form -->
<script>
    async function invia() {
        // Recupera i form
        var form1 = document.getElementById("imgForm");
        var form2 = document.getElementById("campiForm");

        try {

        // Crea FormData per form1
        var formData1 = new FormData(form1);

            let response1 = await fetch(form1.action, {
                method: 'POST',
                body: formData1

            });


            let data1 = await response1.text(); // Lettura della risposta come testo
            document.getElementById("nomeFile").value = data1.toString();

            form2.submit();

        }catch (error) {
            console.error('Error submitting forms:', error);
        }
    }
</script>

</main>

<%@include file="/include/footer.inc"%>

</body>
</html>
