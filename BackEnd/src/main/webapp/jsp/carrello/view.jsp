<%@page session="false"%>
<%@ page import="java.util.*" %>
<%@ page import="com.cinema.cin.model.mo.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%String contextPath=request.getContextPath();%>


<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");

    User loggedUser = (User) request.getAttribute("loggedUser");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Carrello";

    List<MetodoPagamento> metodoList = (List<MetodoPagamento>) request.getAttribute("metodoPagamento");
    List<Ticket> ticketList = (List<Ticket>) request.getAttribute("biglietti");
    Abbonamento abbonamentoCarrello = (Abbonamento) request.getAttribute("abbonamentoCarrello");
    Abbonamento abbUser = (Abbonamento) request.getAttribute("abbonamentoUser");
    Integer numeroOggetti = (Integer) request.getAttribute("numeroOggetti");
    Integer numeroBiglietti = (Integer) request.getAttribute("numeroBiglietti");

%>
<!DOCTYPE html>
<html>


<head>
    <link rel="icon" type="image/x-icon" href="images/popcorn.ico">
    <link rel="stylesheet" href="css/General_settings.css" type="text/css" media="screen">
    <link rel="stylesheet" href="css/Header_and_Nav.css" type="text/css" media="screen">
    <link rel="stylesheet" href="css/Carrello/Card.css" type="text/css" media="screen">
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
        var abbonamento_rimozione = true;
        var sconto_studente = true;
        var num_sconto_studente = 0;
        <%if ((abbUser != null)) {%>
            var num_abbonamenti = <%=abbUser.getNumAccessi()%>;
        <%} else {%>
            var num_abbonamenti = 0;
        <%}%>
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

        var Studente = getValore(getCookie("loggedUser"),4) === "true";
    </script>


</head>

<body>

    <!-- Defining the header section of the page -->
   <%@include file="/include/header_and_nav.inc"%>

    <!-- Sezione riepilogo ordine + carte -->
    <div class="contenitore">

        <div class="container">
            <h1 id="testoMetodi">Metodi di pagamento</h1>
            <h1>Carte</h1>

            <div class="tutteCard">

            <%if(metodoList.size() != 0) {
                for (int i = 0; i < metodoList.size(); i++){%>
            <div class="card1" id="<%=i%>">
                <div class="intern">
                    <img class="approximation" src="images/aprox.png" alt="aproximation">
                    <i id='checked-<%=i%>'class="fa-solid fa-check"></i>
                    <div class="card-number">
                        <div class="number-v1"><%=metodoList.get(i).getN_Carta()%></div>
                    </div>

                    <div class="card-holder">
                        <label>Titolare</label>
                        <div class="name-v1"><%=metodoList.get(i).getTitolare()%></div>
                    </div>

                    <div class="card-infos">
                        <div class="exp">
                            <label>Scadenza</label>
                            <div class="expiration-v1"><%=metodoList.get(i).getDataScad()%></div>
                        </div>

                        <div class="cvv">
                            <label>CVV</label>
                            <div class="cvv-v1">***</div>
                            <input id="cvv-<%=i%>" type="hidden" value="<%=metodoList.get(i).getCVV()%>">
                        </div>
                    </div>
                    <img class="chip" src="images/chip.png" alt="chip">
                </div>
            </div>

                <%}
            }%>


            <script>

                function aggiungi(){
                    var formPro = document.getElementById('aggiungiMetodoForm');
                    formPro.submit();
                }

            </script>

            <form class="card1" id="aggiungiMetodoForm" action="Dispatcher" method="post">
                <input id="inpuType" type="hidden" name="controllerAction" value="CarrelloManagement.viewAggiungiMetodo"/>
            <div class="card-aggiungi intern"  onclick="aggiungi()" >

                <i class="fa-solid fa-circle-plus cerchioPiu" onclick="aggiungi()"></i>

            </div>
            </form>
            </div>
            <%if(abbUser != null){%>
            <h1 id="abbonamentoTesto">Abbonamento</h1>
            <div class="sezioneAbbbonamenti">
                <div>
                    <div class="abbonamento">
                        <h1>Puoi ancora utilizzare: <%=abbUser.getNumAccessi()%> ingressi</h1>
                    </div>
                </div>
                <input type="button"  value="Usa" onclick="riceviValoreAbbonmaneto();" id="abbonamento">
            </div>
            <%}%>
        </div>

        <aside>
            <div class="scontrino">

                <div class="ordine">
                    <h1 class="titolo-ordine">Riepilogo Ordine</h1><br>
                    <h2>Articoli</h2>

                    <form id="formCancella" name="cancellaItemsForm" action="Dispatcher" method="post">

                        <div class="testoCancella">
                            <h8>Seleziona gli articoli per poterli rimuovere</h8>
                            <i class="fa-solid fa-trash-can" onclick="cancellaForm()"></i>
                        </div>

                        <input type="hidden" name="controllerAction" value="CarrelloManagement.cancella"/>
                        <input id="formBiglietto1" type="hidden" name="biglietto1" value="">
                        <input id="formBiglietto2" type="hidden" name="biglietto2" value="">
                        <input id="formBiglietto3" type="hidden" name="biglietto3" value="">
                        <input id="formBiglietto4" type="hidden" name="biglietto4" value="">
                        <input id="formBiglietto5" type="hidden" name="biglietto5" value="">
                        <input id="formAbbonamento" type="hidden" name="abbonamentoID" value="">
                        <input id="formCancellaCarrelloID" type="hidden" name="carrelloID" value="">
                    </form>

                </div>

                <div class="riepilogo">
                    <div class="articoli">
                        <%
                            if (ticketList.size() != 0 || abbonamentoCarrello != null ) {

                                Float sommaTotale = 0.00f;

                                if(ticketList.size() != 0){

                                    HashMap<String, ArrayList<ArrayList>> biglietti_film = new HashMap<String, ArrayList<ArrayList>>();

                                for (int i = 0; i < ticketList.size(); i++) {

                                    String Titolo = ticketList.get(i).getProiezione().getFilm().getTitolo() + " - " + ticketList.get(i).getProiezione().getData();
                                    String Posizione = ((char) (Integer.parseInt(ticketList.get(i).getN_riga()) + 64))+ ticketList.get(i).getN_colonna();
                                    String Costo = String.format("%.2f",ticketList.get(i).getProiezione().getCosto());
                                    String tiket_ID = ticketList.get(i).getTicket_id().toString();
                                    sommaTotale += ticketList.get(i).getProiezione().getCosto();
                                    ArrayList<String> lista_valori = new ArrayList<>();
                                    lista_valori.add(Posizione);
                                    lista_valori.add(Costo);
                                    lista_valori.add(tiket_ID);

                                    if (!biglietti_film.containsKey(Titolo)) {
                                        ArrayList<ArrayList> lista_prima = new ArrayList<ArrayList>();
                                        biglietti_film.put(Titolo, lista_prima);
                                    }

                                    biglietti_film.get(Titolo).add(lista_valori);
                                }

                                for (String key : biglietti_film.keySet()) {%>

                                    <div class="articolo" id="articolo">
                                        <h3><%=key%></h3>
                                    </div>
                                    <%for (int i = 0; i < biglietti_film.get(key).size(); i++) {

                                        if(i == biglietti_film.get(key).size()-1){%>
                                            <div class="articolo subarticolo" ID_biglietto = '<%=biglietti_film.get(key).get(i).get(2)%>'>
                                                <h5>└ Posto: <%=biglietti_film.get(key).get(i).get(0)%></h5>
                                                <h5>€<%=biglietti_film.get(key).get(i).get(1)%></h5>
                                            </div>
                                        <%}else{%>
                                            <div class="articolo subarticolo" ID_biglietto = '<%=biglietti_film.get(key).get(i).get(2)%>'>
                                                <h5>├ Posto: <%=biglietti_film.get(key).get(i).get(0)%></h5>
                                                <h5>€<%=biglietti_film.get(key).get(i).get(1)%></h5>
                                            </div>
                                        <%}
                                    }%>
                                    <br>
                                <%}
                                }
                                if(abbonamentoCarrello != null){
                                    sommaTotale += abbonamentoCarrello.getPrezzo();%>
                                    <div class="articolo subarticolo" Abbonamento ='<%=abbonamentoCarrello.getAbbonamentoId()%>'>
                                        <h3 >Abbonamento</h3>
                                        <h5>€<%=String.format("%.2f",abbonamentoCarrello.getPrezzo())%></h5>
                                    </div>
                                    <div class="articolo" id="subarticolo">
                                        <h5>└ Numero accessi: <%=abbonamentoCarrello.getNumAccessi()%></h5>
                                    </div>
                                <%}%>
                                </div>
                            </div>
                            <div class="totale">
                                <h2>Totale articoli:</h2>
                                <h2 id="valoreSomma">€<%=String.format("%.2f", sommaTotale)%></h2>
                            </div>
                        <%/*fine if*/}else{%>
                            <h1>Nessun articolo da visualizzare</h1>
                <%}%>
                <%if(numeroOggetti > 0){%>
                <form id='acquistaForm' name="acquistaForm" action="Dispatcher" method="post">
                    <input id="acquista" type="hidden" name="controllerAction" value="CarrelloManagement.acquista"/>
                    <input id="formAcquistaCarrelloID" type="hidden" name="carrelloID" value="">
                    <input id="abbonamentiUtilizzati" type="hidden" name="AbbUtilizzati" value="">
                    <button type="button" onclick="verificaCVV()">Acquista</button>
                </form>
                <%}%>
            </div>
        </aside>


    </div>


    <!-- funzione per il tilt delle carte -->
    <script src="script/Vanilla_tilts.js"></script>
    <script>
        VanillaTilt.init(document.querySelector(".intern"), {
            max: 25,
            speed: 400
        });

        //It also supports NodeList
        VanillaTilt.init(document.querySelectorAll(".intern"));
    </script>

    <!-- funzione per verificare il CVV per effetturare l'acquisto -->
    <script>
        function verificaCVV() {
            var CartaSelezionata = document.getElementById('CartaSelezionata').value;
            var contatore = document.getElementById("contatoreElementi");
            var abbonamento = document.getElementById("formAbbonamento");
            var valoreSomma = document.getElementById("valoreSomma").innerHTML.substring(1,document.getElementById("valoreSomma").innerHTML.length);
            var abbuti = 0;
            document.querySelectorAll('.subarticolo').forEach((elem) => {
                if (elem.style.color == 'green') {
                    abbuti++;
                }
            });
            document.getElementById('abbonamentiUtilizzati').value = abbuti;
            valoreSomma = parseFloat(valoreSomma);
            if (CartaSelezionata == "" && valoreSomma > 0) {
                alert("Seleziona una carta prima di procedere all'acquisto!");
            } else if((parseInt(contatore.value)) != 0 || abbonamento.value != ""){
                alert("Prima di acquistare rimuovi gli articoli selezionati dal carrello!");
            } else if (valoreSomma == 0) {
                document.getElementById('acquistaForm').submit();
            } else {
                cvv = prompt("Inserisci il CVV della carta selezionata");
                if (document.getElementById('cvv-'+CartaSelezionata).value == cvv) {
                    document.getElementById('acquistaForm').submit();
                } else {
                    alert("CVV errato");
                }
            }
        }
    </script>


    <!-- funzione che restituisce la posizione della carta selezionata e degli elementi del carrello -->
    <script>
        document.querySelectorAll('.card1').forEach((element) => {
            if (element.id != 'aggiungiMetodoForm') {
                document.getElementById('checked-'+element.id).style.display = 'none';
                element.onclick = function (event, id = element.id) {
                    document.querySelectorAll('.card1').forEach((element_1) => {
                        if (element_1.id != 'aggiungiMetodoForm') {
                            if (element_1.id == id) {
                                if (document.getElementById('checked-'+element_1.id).style.display == 'unset') {
                                    document.getElementById('checked-'+element_1.id).style.display = 'none';
                                    document.getElementById('CartaSelezionata').value = "";
                                } else {
                                    document.getElementById('checked-'+element_1.id).style.display = 'unset';
                                    document.getElementById('CartaSelezionata').value = id;
                                }
                            } else {
                                document.getElementById('checked-'+element_1.id).style.display = 'none';
                            }
                        }
                    });
                }
            }
        })
        document.querySelectorAll('.subarticolo').forEach((item) => {
            item.style.color = 'rgb(102, 102, 102)';
            item.onclick = function (event, elem=item) {
                var valore = document.querySelector('#valoreSomma').innerHTML;
                var contatore = document.getElementById("contatoreElementi");
                var contatoreAbbonamento = document.getElementById("contatoreAbbonamento");
                valore = valore.replaceAll(',','.');
                children = elem.children[1].innerHTML.replaceAll(',','.');

                if (abbonamento_rimozione && sconto_studente) {
                    if (elem.hasAttribute('id_biglietto') || elem.hasAttribute('abbonamento')) {
                        if (elem.style.color == 'rgb(102, 102, 102)') {
                            elem.style.color = "red";
                            elem.style.textDecoration = "line-through";
                            if(elem.hasAttribute('abbonamento')){
                                document.getElementById("formAbbonamento").value = elem.attributes['abbonamento'].value;
                                contatoreAbbonamento.value = parseInt(contatoreAbbonamento.value) + 1;
                            }else{
                                contatore.value = parseInt(contatore.value) + 1;
                                associaIDtoInput(contatore.value, elem.attributes["id_biglietto"].value);
                            }
                            document.querySelector('#valoreSomma').innerHTML = "€" + (parseFloat(valore.substring(1, valore.length)) -  parseFloat(children.substring(1, children.length))).toFixed(2).replace(".",",");
                        } else  if (elem.style.color == 'red') {
                            elem.style.color = "rgb(102, 102, 102)";
                            elem.style.textDecoration = "none";
                            if(elem.hasAttribute('abbonamento')){
                                document.getElementById("formAbbonamento").value = "";
                                contatoreAbbonamento.value = parseInt(contatoreAbbonamento.value) - 1;
                            }else {
                                dissociaIDtoInput(contatore.value, elem.attributes["id_biglietto"].value);
                                contatore.value = parseInt(contatore.value) - 1;
                            }
                            document.querySelector('#valoreSomma').innerHTML = "€" + (parseFloat(valore.substring(1, valore.length)) +  parseFloat(children.substring(1, children.length))).toFixed(2).replace(".",",");
                        }
                    }
                } else {
                    if (num_abbonamenti > 0 && !abbonamento_rimozione) {
                        if (elem.hasAttribute('id_biglietto')) {
                            if (elem.style.color == 'rgb(102, 102, 102)') {
                                num_abbonamenti--;
                                elem.style.color = 'green';
                                elem.style.textDecoration = 'line-through';
                                document.querySelector('#valoreSomma').innerHTML = "€" + (parseFloat(valore.substring(1, valore.length)) -  parseFloat(children.substring(1, children.length))).toFixed(2).replace(".",",");
                            } else if (elem.style.color == 'green') {
                                num_abbonamenti++;
                                elem.style.color = "rgb(102, 102, 102)";
                                elem.style.textDecoration = "none";
                                document.querySelector('#valoreSomma').innerHTML = "€" + (parseFloat(valore.substring(1, valore.length)) +  parseFloat(children.substring(1, children.length))).toFixed(2).replace(".",",");
                            }
                            document.querySelector(".abbonamento").children[0].innerHTML = 'Puoi ancora utilizzare: '+num_abbonamenti+' ingressi';
                        }
                    } else if (num_sconto_studente >= 0 && !sconto_studente) {
                        if (elem.hasAttribute('id_biglietto')) {
                            if (elem.style.color == 'rgb(102, 102, 102)' && num_sconto_studente < 1) {
                                num_sconto_studente++;
                                elem.style.color = 'rgb(218, 165, 32)';
                                elem.style.textDecoration = 'line-through';
                                let valoreNumerico = parseFloat(valore.substring(1).replace(",", "."));
                                valoreNumerico = valoreNumerico - 4;
                                document.querySelector('#valoreSomma').innerHTML = "€" + valoreNumerico.toFixed(2).replace(".", ",");
                            } else if (elem.style.color == 'rgb(218, 165, 32)') {
                                num_sconto_studente--;
                                elem.style.color = "rgb(102, 102, 102)";
                                elem.style.textDecoration = "none";
                                let valoreNumerico = parseFloat(valore.substring(1).replace(",", "."));
                                valoreNumerico = valoreNumerico + 4;
                                document.querySelector('#valoreSomma').innerHTML = "€" + valoreNumerico.toFixed(2).replace(".", ",");
                            }
                            document.querySelector(".abbonamento").children[0].innerHTML = 'Puoi ancora utilizzare: '+num_abbonamenti+' ingressi';
                        }
                    }
                }
            }
        })

    </script>

    <input type='hidden' id="CartaSelezionata">
    <input type="hidden" id="contatoreElementi" value="0">
    <input type="hidden" id="contatoreAbbonamento" value="0">



    <!-- funzione che scrive il valore di carrelloID recuperato dal cookie -->
    <script>
        document.getElementById("carrelloID").value = getValore(getCookie('cart'), 0);
        document.getElementById("cartItems").innerHTML = getValore(getCookie('cart'), 1);
        document.getElementById("formCancellaCarrelloID").value = getValore(getCookie('cart'), 0);
    </script>

    <%if(numeroOggetti > 0){%>
    <script>

        document.getElementById("formAcquistaCarrelloID").value = getValore(getCookie('cart'), 0);

    </script>
    <%}%>

    <!-- funzione che crea il div se studente -->
    <script>
        if (Studente) {
            var scntstudente = document.createElement('div');
            scntstudente.id = 'sconto';
            var testoh1 = document.createElement('h1');
            testoh1.innerHTML = 'Utilizza lo sconto studente';
            testoh1.style.textShadow = '2px 2px 2px black';
            var buttonstudente = document.createElement('input');
            buttonstudente.value = 'Usa';
            buttonstudente.id = 'button_studente';
            buttonstudente.onclick = function () {
                riceviValoreStudente();
            };
            scntstudente.style.display = 'flex';
            testoh1.style.width = '60%';
            buttonstudente.style.width = '20%';
            buttonstudente.style.marginLeft = '10%';
            buttonstudente.type = 'button';
            scntstudente.appendChild(testoh1);
            scntstudente.appendChild(buttonstudente);
            scntstudente.style.marginTop = '10px';
            document.querySelector('.container').appendChild(scntstudente);
        }
    </script>


    <!-- funzione che associa i valori dei biglietti da eliminare ai campi della form e submitta-->
    <script>

        function associaIDtoInput(valoreContatore,ID_posto){
            let nome = "formBiglietto"+valoreContatore;
            let inputID = document.getElementById(nome);
            inputID.value = ID_posto;
        }

        function dissociaIDtoInput(valoreContatore,ID_posto){
            let nome = "formBiglietto"+valoreContatore;
            let inputID = document.getElementById(nome);
            inputID.value = "";
        }

       function cancellaForm(){
           var form = document.getElementById("formCancella");
           var contatore = document.getElementById("contatoreElementi");
           var contatoreAbb = document.getElementById("contatoreAbbonamento");
           if(contatore.value == 0 && contatoreAbb.value == 0){
               alert("Selezione almeno un elemento da cancellare!");
           }
           else{
                form.submit();
           }
       }


    </script>


    <!-- funzione che riceve il numero di abbonamenti e sconta -->
    <script>

        function riceviValoreAbbonmaneto(){
            var val = document.getElementById('abbonamento').value
            if (val == 'Usa') {
                document.getElementById('abbonamento').value = 'Conferma';
                abbonamento_rimozione = false;
                document.getElementById('button_studente').disabled = true;
                document.querySelector('.fa-trash-can').disabled = true;
            } else if (val == 'Conferma') {
                document.getElementById('abbonamento').value = 'Usa';
                abbonamento_rimozione = true;
                document.getElementById('button_studente').disabled = false;
                document.querySelector('.fa-trash-can').disabled = false;
            }
        }
        function riceviValoreStudente() {
            var val = document.getElementById('button_studente').value;
            if (val == 'Usa') {
                document.getElementById('button_studente').value = 'Conferma';
                sconto_studente = false;
                document.getElementById('abbonamento').disabled = true;
                document.querySelector('.fa-trash-can').disabled = true;
            } else if (val == 'Conferma') {
                document.getElementById('button_studente').value = 'Usa';
                sconto_studente = true;
                document.getElementById('abbonamento').disabled = false;
                document.querySelector('.fa-trash-can').disabled = false;
            }
        }


    </script>

    <!-- definizione sezione footer -->
    <%@include file="/include/footer.inc"%>

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
