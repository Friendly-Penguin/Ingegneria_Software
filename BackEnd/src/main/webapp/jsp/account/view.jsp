<%@page session="false"%>
<%@page import="com.cinema.cin.model.mo.User"%>
<%@ page import="java.util.*" %>
<%@ page import="com.cinema.cin.model.mo.Abbonamento" %>
<%String contextPath=request.getContextPath();%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    User loggedUser = (User) request.getAttribute("loggedUser");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Account";
    User usr = (User) request.getAttribute("utente");
    Abbonamento abbonamento = (Abbonamento) request.getAttribute("abbonamento");
    String id="numAccessiFunzione";
%>


<!DOCTYPE html>
    <head>

        <link rel="icon" type="image/x-icon" href="images/popcorn.ico">
        <link rel="stylesheet" href="css/General_settings.css" type="text/css" media="screen">
        <link rel="stylesheet" href="css/Header_and_Nav.css" type="text/css" media="screen">
        <link rel="stylesheet" href="css/Account/Account_Info.css" type="text/css" media="screen">
        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" integrity="sha512-SnH5WK+bZxgPHs44uWIX+LLJAJ9/2PkPKZ5QiAj6Ta86w+fsb2TkcmfRyVX3pBnMFcV7oQPJkl9QevSCWr3W6A==" crossorigin="anonymous" referrerpolicy="no-referrer" />

        <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js" integrity="sha512-qZvrmS2ekKPF2mSznTQsxqPgnpkI4DNTlrdUmTzrDgektczlKNRRhy5X5AAOnx5S09ydFYWWNSfcEqDTTHgtNA==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>


        <title>Cineplex: <%=menuActiveLink%></title>

        <script>

            var applicationMessage;
            <%if (applicationMessage != null) {%>
            applicationMessage="<%=applicationMessage%>";
            <%}%>
            function onLoadHandler() {
                //headerOnLoadHandler();
                //try { mainOnLoadHandler(); } catch (e) {}
                if (applicationMessage != undefined) alert(applicationMessage);
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

    <div class="account">
        <div class="titoloAccount">
            <h1>Account</h1>
            <h1>Informazioni</h1>
        </div>
        <canvas id="canvas" width="400" height="200" style="display:none;"></canvas>
        <!-- Dove mostrare l'immagine generata -->
        <img id="cartaAbbonamento" alt="Carta Abbonamento" onclick="scaricaPDF()">
        <h3>Clicca sull'immagine per scaricare la tua tessera!</h3>
        <div class='triangle' id="normal">

        </div>
        <div class="infoAccount" id="notvisible">
            <div class="info">
                <h1>Info Account</h1>
                <div>
                    <h2>
                        Nome: <%=usr.getNome()%>
                    </h2>
                </div>
                <div>
                    <h2>
                        Cognome: <%=usr.getCognome()%>
                    </h2>
                </div>
                <div>
                    <h2>
                        Email: <%=usr.getEmail()%>
                    </h2>
                </div>
            </div>
        </div>


    </div>

<!-- funzione che disegna la carta -->
        <script>
            function creaCartaAbbonamento() {
                var nome = '<%=usr.getNome()%>';
                var cognome = '<%=usr.getCognome()%>';
                var accessi = <%if (abbonamento != null) {%><%=abbonamento.getNumAccessi()%><%} else {%>0<%}%>;
                var N_tessera = <%=usr.getNumero_tessera()%>
                var canvas = document.getElementById('canvas');
                var ctx = canvas.getContext('2d');

                // Rendi lo sfondo del canvas trasparente
                ctx.clearRect(0, 0, canvas.width, canvas.height);

                // Rettangolo con bordi arrotondati e sfondo giallino
                var x = 10;
                var y = 10;
                var width = canvas.width - 20;
                var height = canvas.height - 20;
                var radius = 20;

                ctx.fillStyle = '#fffacd'; // Giallino chiaro
                ctx.beginPath();
                ctx.moveTo(x + radius, y);
                ctx.lineTo(x + width - radius, y);
                ctx.quadraticCurveTo(x + width, y, x + width, y + radius);
                ctx.lineTo(x + width, y + height - radius);
                ctx.quadraticCurveTo(x + width, y + height, x + width - radius, y + height);
                ctx.lineTo(x + radius, y + height);
                ctx.quadraticCurveTo(x, y + height, x, y + height - radius);
                ctx.lineTo(x, y + radius);
                ctx.quadraticCurveTo(x, y, x + radius, y);
                ctx.closePath();
                ctx.fill();

                // Bordo del rettangolo
                ctx.lineWidth = 5; // Aumenta la larghezza del bordo
                ctx.strokeStyle = '#000000';
                ctx.stroke();

                // Testo
                ctx.font = '20px Arial';
                ctx.fillStyle = 'red';
                ctx.fillText('Carta Abbonamento', 110, 40);

                ctx.fillStyle = 'black';
                ctx.font = '40px Arial';
                ctx.fillText(nome + ' ' + cognome, 50, 90);
                ctx.font = '14px Arial';
                ctx.fillText('Accessi Rimanenti: ' + accessi, 20, 130);
                ctx.fillText('N° tessera: ' + N_tessera, 20, 150);

                // Converti il canvas in un'immagine
                var image = document.getElementById('cartaAbbonamento');
                image.src = canvas.toDataURL();
                image.style.display = 'block';
            }
        </script>

    <!-- funzione per creare il pdf on click -->
    <script>
        // Funzione per scaricare il PDF
        function scaricaPDF() {
            window.jsPDF = window.jspdf.jsPDF;
            var canvas = document.getElementById('canvas');
            var imgData = canvas.toDataURL('image/png', 1.0);

            var pdf = new jsPDF({
                orientation: 'landscape',
                unit: 'px',
                format: [canvas.width / 2, canvas.height / 2] // Ridimensiona mantenendo alta risoluzione
            });

            pdf.addImage(imgData, 'PNG', 0, 0, canvas.width / 2, canvas.height / 2); // Aggiungi immagine ridimensionata
            pdf.save('Carta_Abbonamento.pdf');
        }
    </script>

    </main>
<script>
    creaCartaAbbonamento();
</script>

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
    <!-- triangolo che si muove -->
    <script>
        document.querySelector('.triangle').addEventListener('click',function() {
            var elem = document.querySelector('.triangle');
            if (elem.id == 'normal') {
                elem.focus()
                elem.id = 'notnormal';
                document.querySelector('.infoAccount').id = 'visible';
            } else {
                elem.id = 'normal';
                document.querySelector('.infoAccount').id = 'notvisible';
            }
        })
    </script>
</body>
</html>
