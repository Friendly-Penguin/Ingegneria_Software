<%@page session="false"%>
<%@page import="com.cinema.cin.model.mo.User"%>
<%@ page import="com.cinema.cin.model.mo.Film" %>
<%@ page import="java.util.*" %>
<%@ page import="com.cinema.cin.model.mo.Review" %>
<%String contextPath=request.getContextPath();%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<%
    Boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    User loggedUser = (User) request.getAttribute("loggedUser");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    Film film = (Film) request.getAttribute("film");
    String menuActiveLink = film.getTitolo();
    List<Review> reviews = (List<Review>) request.getAttribute("rew");
    String mediaUtenti = (String) request.getAttribute("mediaUtenti");
    Boolean rec_fat = false;
    String rewID;
    Long ID = null;
    int posizione_lista = -1;
    if(loggedOn) {
        for (int i = 0; i < reviews.size(); i++) {
            if (reviews.get(i).getUsr().getEmail().equals(loggedUser.getEmail())) {
                rec_fat = true;
                ID = reviews.get(i).getRew_id();
                posizione_lista = i;
                break;
            }
        }
    }
    if(ID != null){
        rewID = ID.toString();
    }else{
        rewID = "";
    }
%>


<!DOCTYPE html>

<html>
        <head>
            <link rel="icon" type="image/x-icon" href="images/popcorn.ico">
            <link rel="stylesheet" href="css/General_settings.css" type="text/css" media="screen">
            <link rel="stylesheet" href="css/Header_and_Nav.css" type="text/css" media="screen">
            <link rel="stylesheet" href="css/ViewFilm/ViewFilm.css" type="text/css" media="screen">
            <!-- Font Awesome -->
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" integrity="sha512-SnH5WK+bZxgPHs44uWIX+LLJAJ9/2PkPKZ5QiAj6Ta86w+fsb2TkcmfRyVX3pBnMFcV7oQPJkl9QevSCWr3W6A==" crossorigin="anonymous" referrerpolicy="no-referrer" />


            <title>Cineplex: <%=menuActiveLink%></title>



            <style>
                .star {
                    color: yellow;
                    height: 20px;
                    width: 20px;
                }
            </style>
            <style id="stile">

            </style>
            <!-- Funzione per leggere il valore di un cookie dato il suo nome -->
            <script>
                <%if (rec_fat) {%>
                    var INFORMAZIONI = ["<%=reviews.get(posizione_lista).getTitolo()%>", "<%=reviews.get(posizione_lista).getTesto()%>", <%=reviews.get(posizione_lista).getVoto()%>];
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
                function set_star(ops) {
                    var stiloso = document.getElementById('stile');
                    if (ops) {
                        stiloso.innerHTML = '.starContainer{ \
                        position: relative; \
                        display: flex;  \
                        align-items: center; \
                        justify-content: center; \
                    } \
                .starContainer .star-widget input{ \
                        display: none; \
                    } \
                .star-widget label{ \
                        font-size: 20px; \
                        padding: 10px; \
                        float: right; \
                        transition: all 0.2s ease; \
                    } \
                    input:not(:checked) ~ label:hover, \
                        input:not(:checked) ~ label:hover ~ label{ \
                        color: #fd4; \
                    } \
                    input:checked ~ label{ \
                        color: #fd4; \
                    } \
                    input#rate-5:checked ~ label{ \
                        color: #fe7; \
                        text-shadow: 0 0 20px #952; \
                    }';
                    } else {
                        stiloso.innerHTML = '.starContainer{ \
                        position: relative; \
                        display: flex;  \
                        align-items: center; \
                        justify-content: center; \
                        flex-direction: column; \
                    } \
                .starContainer .star-widget input{ \
                        display: none; \
                    } \
                .star-widget label{ \
                        font-size: 20px; \
                        padding: 10px; \
                        float: right; \
                        transition: all 0.2s ease; \
                    } \
                    input:checked ~ label{ \
                        color: #fd4; \
                    } \
                    input#rate-5:checked ~ label{ \
                        color: #fe7; \
                        text-shadow: 0 0 20px #952; \
                    }';
                    }
                }
                function setStars(ID, valore) {
                    var cont = document.getElementById(ID).getElementsByClassName('star-widget')[0].getElementsByTagName('input');
                    for (var i = 0; i < cont.length; i++) {
                        item = cont[i];
                        item.disabled = false;
                        if (item.value == valore) {
                            item.click();
                        }
                        item.disabled = true;
                    }
                }
            </script>

        </head>
    <script>

    </script>
        <body>

            <!-- Defining the header and nav section of the page -->
            <%@include file="/include/header_and_nav.inc"%>

        <main>

            <div class="titolo">
                    <h1><%=film.getTitolo()%></h1>
                </div>

            <div class="contenitore">


                <div class="sinistra">

                    <div class="casella_film item_1 grigliaFilm">
                        <div class="sez_sup">
                            <div class="Img">
                                <img title="locandina" class="contenutoImg" src="images/locandine/<%=film.getLocandina()%>">
                            </div>
                            <div class="Info">
                                <div class="Informazioni">
                                    <h1>Informazioni</h1>
                                </div>
                                <div class="Caratteristiche">
                                    <h1><b>Regista</b>: <%=film.getRegista()%></h1>
                                    <h1><b>Durata</b>: <%=film.getDurata()%> </h1>
                                    <h1><b>Genere</b>: <%=film.getGenere()%> </h1>

                                    <%if (!reviews.isEmpty()) {%>
                                    <script>
                                        set_star(false);
                                    </script>
                                    <div class="mediaUtenti">
                                        <h1 id="mediaUt">Media Utenti: </h1>
                                        <div id="recensione_media">
                                            <div class="star-widget" id="media-1">
                                                <i class="fas fa-star" id="media-5-1"></i>
                                                <i class="fas fa-star" id="media-4-1"></i>
                                                <i class="fas fa-star" id="media-3-1"></i>
                                                <i class="fas fa-star" id="media-2-1"></i>
                                                <i class="fas fa-star" id="media-1-1"></i>
                                            </div>
                                            <div class="star-widget" id="media-2">
                                                <i class="fas fa-star" id="media-5-2"></i>
                                                <i class="fas fa-star" id="media-4-2"></i>
                                                <i class="fas fa-star" id="media-3-2"></i>
                                                <i class="fas fa-star" id="media-2-2"></i>
                                                <i class="fas fa-star" id="media-1-2"></i>
                                            </div>
                                        </div>
                                        <script>
                                            var divvone = document.getElementById('recensione_media');
                                            var media = (<%=mediaUtenti%>).toFixed(2);
                                            console.log(divvone);
                                            var div_2 = divvone.querySelector('#media-2');
                                            var valore = (90 / 100 * (media * 20));
                                            div_2.style.width = valore.toString()+"px";
                                        </script>
                                    </div>
                                    <%}%>
                                </div>
                            </div>
                        </div>
                        <div class="sez_inf">
                            <button type="button" class="apri_Trailer" id="1">Trailer</button>
                            <div class="Trama">
                                <h1 id="trama">Trama</h1>
                                <h1><%=film.getTrama()%></h1>
                            </div>

                        </div>

                        <div class="trailer-container active conta1">
                            <iframe class="schermo"  width="80%" src="<%=film.getLink_trailer()%>" allowfullscreen></iframe>
                            <i class="fa-solid fa-xmark close-icon" id="icon1" ></i>
                        </div>
                    </div>
                </div>

                <div class="destra">
                    <div class="casella_recensione">
                    <%if (loggedOn) {
                        if (rec_fat) {%>
                            <script>
                                set_star(true);
                            </script>
                            <div id="Titolo">
                                <h1>La tua recensione</h1>
                            </div>
                            <div class="recensioneScritta">
                                <div class="input-filed" id="inputTitolo">
                                    <h1><%=reviews.get(posizione_lista).getTitolo()%></h1>
                                </div>
                            </div>
                            <div class="starContainer" id="recensione_utente">
                                <div class="star-widget">
                                    <input type="radio" name="rate" id="rate-5" value="5">
                                    <label for="rate-5" class="fas fa-star"></label>
                                    <input type="radio" name="rate" id="rate-4" value="4">
                                    <label for="rate-4" class="fas fa-star"></label>
                                    <input type="radio" name="rate" id="rate-3" value="3">
                                    <label for="rate-3" class="fas fa-star"></label>
                                    <input type="radio" name="rate" id="rate-2" value="2">
                                    <label for="rate-2" class="fas fa-star"></label>
                                    <input type="radio" name="rate" id="rate-1" value="1">
                                    <label for="rate-1" class="fas fa-star"></label>
                                </div>
                            </div>
                            <div class="input-filed" id="inputTesto">
                                <textarea class="textarea" name="campoTesto" maxlength="500" readonly><%=reviews.get(posizione_lista).getTesto()%></textarea>
                            </div>
                            <div class="buttons">
                                <button class="bottoniRew" id="modify" onclick="modificaRecensione()">Modifica</button>
                                <button class="bottoniRew" id="delete" onclick="eliminaRecensione()">Elimina</button>
                            </div>
                            <script>
                                setStars('recensione_utente',INFORMAZIONI[2]);
                                function eliminaRecensione() {
                                    document.getElementById('inpuType').value = 'CreaElementiManagement.eliminaRewUser';
                                    document.getElementById('rewForm').submit();
                                }
                                function modificaRecensione() {
                                    set_star(true);
                                    document.getElementsByName('rate').forEach((item) => {
                                        item.disabled = false;
                                    })
                                    var but0 = document.querySelectorAll('.buttons button')[0];
                                    but0.innerHTML = 'Conferma';
                                    but0.onclick = function () {confermaRecensione(true);}
                                    var but1 = document.querySelectorAll('.buttons button')[1];
                                    but1.innerHTML = 'Cancella';
                                    but1.onclick = function () {confermaRecensione(false);}
                                    var in0 = document.createElement('input');
                                    in0.name = 'campoTitolo';
                                    in0.placeholder = 'Titolo';
                                    in0.maxLength = '40';
                                    in0.required = true;
                                    in0.value = INFORMAZIONI[0];
                                    document.querySelector('#inputTitolo').replaceChild(in0,document.querySelector('#inputTitolo h1'));
                                    document.querySelector('#inputTesto textarea').removeAttribute('readonly');
                                    document.querySelector('textarea').classList.remove('textarea');
                                    document.getElementById('inpuType').value = 'CreaElementiManagement.modificaRew';
                                }
                                function confermaRecensione(val) {
                                    if (val) {
                                        titolo = document.getElementById('inputTitolo').getElementsByTagName('input')[0].value;
                                        value = getValueRecensione('recensione_utente');
                                        testo = document.getElementById('inputTesto').getElementsByTagName('textarea')[0].value;
                                        if (titolo != INFORMAZIONI[0] || testo != INFORMAZIONI[1] || value != INFORMAZIONI[2]) {
                                            sendRecensione();
                                        } else {
                                            confermaRecensione(false);
                                        }
                                    } else {
                                        var h1 = document.createElement('h1');
                                        h1.name = 'campoTitolo';
                                        h1.innerHTML = INFORMAZIONI[0];
                                        document.querySelector('#inputTesto textarea').setAttribute('readonly',true);
                                        document.querySelector('textarea').classList.add('textarea');
                                        document.querySelector('#inputTesto textarea').value = INFORMAZIONI[1];
                                        document.querySelector('#inputTitolo').replaceChild(h1, document.querySelector('#inputTitolo input'));
                                        var but0 = document.querySelectorAll('.buttons button')[0];
                                        but0.innerHTML = 'Modifica';
                                        but0.onclick = function () {modificaRecensione();}
                                        var but1 = document.querySelectorAll('.buttons button')[1];
                                        but1.innerHTML = 'Elimina';
                                        but1.onclick = function () {eliminaRecensione();}
                                        setStars('recensione_utente', INFORMAZIONI[2]);
                                        set_star(false);
                                    }
                                }
                            </script>
                        <%} else {%>
                        <script>
                            set_star(true);
                        </script>
                            <div id="Titolo">
                                <h1>Scrivi la tua recensione</h1>
                            </div>
                            <div class="scriviRecensione" id="recensione_utente">
                                <div class="input-filed" id="inputTitolo">
                                    <input name="campoTitolo"  placeholder="Titolo" maxlength="40" required>
                                </div>
                                <div class="starContainer">
                                    <div class="star-widget">
                                        <input type="radio" name="rate" id="rate-5" value="5">
                                        <label for="rate-5" class="fas fa-star"></label>
                                        <input type="radio" name="rate" id="rate-4" value="4">
                                        <label for="rate-4" class="fas fa-star"></label>
                                        <input type="radio" name="rate" id="rate-3" value="3">
                                        <label for="rate-3" class="fas fa-star"></label>
                                        <input type="radio" name="rate" id="rate-2" value="2">
                                        <label for="rate-2" class="fas fa-star"></label>
                                        <input type="radio" name="rate" id="rate-1" value="1">
                                        <label for="rate-1" class="fas fa-star"></label>
                                    </div>
                                </div>
                                <div class="input-filed" id="inputTesto">
                                    <textarea class="textarea" name="campoTesto" placeholder="Testo" maxlength="500"></textarea>
                                </div>
                                <div class="btn-filed1">
                                    <button type="button" onclick="sendRecensione()" id="invia" >Invia</button>
                                </div>
                            </div>
                        <%}%>
                        <form id ="rewForm" name="rewForm" action="Dispatcher" method="post">
                            <input id="inpuType" type="hidden" name="controllerAction" value="CreaElementiManagement.createRew"/>
                            <input name="campoFilmID" type="hidden" value="<%=film.getFilmID().toString()%>">
                            <input name="campoEmail" type="hidden" value="<%=loggedUser.getEmail()%>">
                            <input name="campoData" type="hidden" id="campoDATA" value="">
                            <input name="campoTitolo" type="hidden" id="campoTitolo" value="">
                            <input name="rate" type="hidden" id="campoValue" value="">
                            <input name="campoTesto" type="hidden" id="campoTesto" value="">
                            <input name="reviewID" type="hidden" value="<%=rewID%>">
                        </form>
                    <%} else {%>
                        <div id="nologged">
                            <h1>Accedi per scrivere la tua recensione</h1>
                        </div>
                    <%}%>
                    </div>
                </div>

            </div>

            <div class="recensioni">
                <h1 id="TitoloRec">Recensioni utenti:</h1>
                <%if (!reviews.isEmpty() && (reviews.size() != 1 || !rec_fat)) {%>
                    <script>
                        set_star(false);
                    </script>
                    <%for (int i = 0; i < reviews.size(); i++) {
                        if (!(i == posizione_lista)) {%>
                            <div class="recensione">
                                <div class="titolo_recensione">
                                    <h1><%=reviews.get(i).getTitolo()%></h1>
                                    <div class="starContainer" id="recensione_<%=i%>">
                                        <div class="star-widget">
                                            <input type="radio" name="rate_<%=i%>" id="rate-5-<%=i%>" value="5">
                                            <label for="rate-5-<%=i%>" class="fas fa-star"></label>
                                            <input type="radio" name="rate_<%=i%>" id="rate-4-<%=i%>" value="4">
                                            <label for="rate-4-<%=i%>" class="fas fa-star"></label>
                                            <input type="radio" name="rate_<%=i%>" id="rate-3-<%=i%>" value="3">
                                            <label for="rate-3-<%=i%>" class="fas fa-star"></label>
                                            <input type="radio" name="rate_<%=i%>" id="rate-2-<%=i%>" value="2">
                                            <label for="rate-2-<%=i%>" class="fas fa-star"></label>
                                            <input type="radio" name="rate_<%=i%>" id="rate-1-<%=i%>" value="1">
                                            <label for="rate-1-<%=i%>" class="fas fa-star"></label>
                                        </div>
                                    </div>
                                </div>
                                <h4><%=reviews.get(i).getUsr().getNome() + ' ' + reviews.get(i).getUsr().getCognome()%> - <%=reviews.get(i).getData()%></h4>
                                <div class="testo_recensione"><%=reviews.get(i).getTesto()%></div>
                            </div>
                            <script>
                                setStars('recensione_<%=i%>',<%=reviews.get(i).getVoto()%>);
                            </script>
                        <%}
                    }%>
                <%}else{%>
                    <h2 style="text-align: center; color: whitesmoke">Nessuna recensione presente</h2>
                <%}%>
            </div>


            <%if (loggedOn) {%>
                <script>
                    var applicationMessage;
                    <%if (applicationMessage != null) {%>
                    applicationMessage="<%=applicationMessage%>";
                    <%}%>
                    function onLoadHandler() {
                        mostraDataCorrente();
                        if (applicationMessage!=undefined) alert(applicationMessage);
                    }
                    window.addEventListener("load", onLoadHandler);
                </script>
            <%}%>
            <script>
                function mostraDataCorrente() {
                    var oggi = new Date();
                    var giorno = oggi.getDate();
                    if(giorno.toString().length === 1){
                        giorno = '0' + giorno;
                    }
                    var mese = oggi.getMonth() + 1;
                    if(mese.toString().length === 1){
                        mese = '0' + mese;
                    }
                    var anno = oggi.getFullYear();
                    var dataCorrente = giorno + '/' + mese + '/' + anno;
                    document.getElementById('campoDATA').value = dataCorrente;
                }
            </script>

            <!-- script per visualizzare/nascondere il trailer -->
            <script>
                document.querySelectorAll('.apri_Trailer').forEach((elem) => {
                    elem.onclick = function () {
                        id = elem.id
                        document.querySelector('.conta'+id).classList.remove("active");
                        document.querySelector('.conta'+id).style.zIndex = "3";
                        document.getElementById("footer").style.display = "none";
                        document.getElementById("formConf").style.zIndex = "-2";
                    }
                })
                document.querySelectorAll('.close-icon').forEach((elem) => {
                    elem.onclick = function () {
                        id = elem.id.replace('icon','');
                        document.querySelector('.conta'+id).classList.add("active");
                        document.getElementById("footer").style.display = "block";
                        document.getElementById("formConf").style.zIndex = "1";
                    }
                })

            </script>



            <script>
                function sendRecensione() {
                    Titolo_recensione = document.getElementById('inputTitolo').getElementsByTagName('input')[0].value;
                    Value_recensione = getValueRecensione('recensione_utente');
                    Testo_recensione = document.getElementById('inputTesto').getElementsByTagName('textarea')[0].value;
                    if (Titolo_recensione.length > 0) {
                        if (Value_recensione != -1) {
                            if (Testo_recensione.length > 0) {
                                document.getElementById('campoTitolo').value = Titolo_recensione;
                                document.getElementById('campoValue').value = Value_recensione;
                                document.getElementById('campoTesto').value = Testo_recensione;
                                document.getElementById('rewForm').submit();
                            } else {
                                alert("Non hai inserito nessun tipo di testo nella tua recensione")
                            }
                        } else {
                            alert("Non hai inserito una valutazione alla tua recensione");
                        }
                    } else {
                        alert("Non hai inserito un titolo alla tua recensione");
                    }
                }
                function getValueRecensione(ID) {
                    valore = 0;
                    document.getElementById(ID).querySelector('.star-widget').querySelectorAll('input').forEach((item) => {
                        if (item.checked) {
                            valore = item.value;
                        }
                    })
                    if (valore != 0) {
                        return valore;
                    } else {
                        return -1;
                    }
                }
            </script>

        </main>

        <!-- definizione sezione footer -->
            <%@include file="/include/footer.inc"%>

            <%if(loggedOn){%>
            <script>
                document.getElementById("cartItems").innerHTML = getValore(getCookie('cart'), 1);
                document.getElementById("carrelloID").value = getValore(getCookie('cart'), 0);
            </script>
            <%}%>


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
