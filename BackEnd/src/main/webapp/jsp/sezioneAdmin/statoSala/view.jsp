<%@page session="false"%>
<%@page import="com.cinema.cin.model.mo.Admin"%>
<%@ page import="java.util.*" %>
<%@ page import="com.cinema.cin.model.mo.Proiezione" %>
<%@ page import="com.cinema.cin.model.mo.Ticket" %>
<%@ page import="com.cinema.cin.model.mo.Sala" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    Admin loggedAdmin = (Admin) request.getAttribute("loggedAdmin");
    String applicationMessage = (String)request.getAttribute("applicationMessage");
    String menuActiveLink = "Stato Sala - Admin";
    LinkedHashMap<String, LinkedHashMap<Long, LinkedHashMap<String, LinkedHashMap<Proiezione, ArrayList<Ticket>>>>> giorno_sala_ora_pro = (LinkedHashMap<String, LinkedHashMap<Long, LinkedHashMap<String, LinkedHashMap<Proiezione, ArrayList<Ticket>>>>>) request.getAttribute("mappa");
    ArrayList<Ticket> biglietti = (ArrayList<Ticket>) request.getAttribute("biglietti");
    ArrayList<Sala> sale = (ArrayList<Sala>) request.getAttribute("sale");
    HashMap<Long, String> id_num_sala = new HashMap<>();
    for (int i = 0; i < sale.size(); i++) {
        id_num_sala.put(sale.get(i).getSalaID(), sale.get(i).getNumeroSala());
    }
    Long sala_selezionata = null;
    String giorno_selezionato = null;
%>

<html>

<head>
    <link rel="icon" type="image/x-icon" href="images/popcorn.ico">
    <link rel="stylesheet" href="css/General_settings.css" type="text/css" media="screen">
    <link rel="stylesheet" href="css/Header_and_Nav.css" type="text/css" media="screen">
    <link rel="stylesheet" href="css/Admin/statoSala/View.css" type="text/css" media="screen">

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
        GSO_B = {};
        GSO_F = {};
    </script>

</head>

    <body>

        <%@include file="/include/header_and_nav_admin.inc"%>

            <div class="account">
                <div class="titoloAccount">
                    <h1>Admin</h1>
                    <h1 id="titoloSeleziona">Stato Sala</h1>
                </div>
            </div>

            <div class="contenitore">

                <div id="sez_giorni">
                    <div class="sinistra">
                    <h1>Giorno: </h1>
                    <%for(String giorno: giorno_sala_ora_pro.keySet()) {
                        if (giorno_selezionato == null) {
                            giorno_selezionato = giorno;
                        }%>
                        <button id="button_<%=giorno%>" onclick="selezione_giorno('<%=giorno%>')"><%=giorno%></button>
                    <%}%>
                    </div>
                    <div class="destra">
                        <div class="sopra">
                            <label for="campoData">Scegli una data:</label>
                            <button  id="bottoneForm" type="button" onclick="formSubmit()">Cerca</button>
                        </div>
                        <form id="formData" action="Dispatcher" method="post">
                            <input id="inputType" type="hidden" name="controllerAction" value="AdminManagement.viewStatoSala"/>
                            <input id="campoData" type="date" name="campoData" required>
                        </form>
                    </div>
                </div>

                <div id="sez_sale">
                    <h1>Sala: </h1>
                    <%for(Long sala_id: giorno_sala_ora_pro.get(giorno_selezionato).keySet()) {
                            sala_selezionata = sala_id;%>
                        <button id="button_<%=sala_id%>" onclick="selezione_sala('<%=sala_id%>')"><%=id_num_sala.get(sala_id)%></button>
                    <%}%>
                </div>
                <div id="sez_ore">
                    <h1>Orario: </h1>
                    <%for(String ora: giorno_sala_ora_pro.get(giorno_selezionato).get(sala_selezionata).keySet()) {%>
                        <button id="button_<%=ora%>" onclick="selezione_ora('<%=ora%>')"><%=ora%></button>
                    <%}%>
                </div>

                <div>
                    <h1 id="titolo_film"></h1>
                </div>

                <div id="sez_posti">

                    <div id="schermo">SCHERMO</div>
                    <div class="posti_sala">
                        <div id="colonna_1">
                            <%for (int row = 1; row <= 10; row++) {
                                for (int col = 1; col <= 4; col++) {%>
                            <button id="posto" class="_<%=row%>_<%=col%>" disabled></button>
                            <%}
                            }%>
                        </div>
                        <div id="colonna_2">
                            <%for (int row = 1; row <= 10; row++) {
                                for (int col = 5; col <= 16; col++) {%>
                            <button id="posto" class="_<%=row%>_<%=col%>" disabled></button>
                            <%}
                            }%>
                        </div>
                        <div id="colonna_3">
                            <%for (int row = 1; row <= 10; row++) {
                                for (int col = 17; col <= 20; col++) {%>
                            <button id="posto" class="_<%=row%>_<%=col%>" disabled></button>
                            <%}
                            }%>
                        </div>
                    </div>
                    <%for (String giorno: giorno_sala_ora_pro.keySet()) {
                        for (Long sala_id: giorno_sala_ora_pro.get(giorno).keySet()) {
                            for (String ora: giorno_sala_ora_pro.get(giorno).get(sala_id).keySet()) {
                                if (giorno_sala_ora_pro.get(giorno).get(sala_id).get(ora) != null) {
                                    for (Proiezione proiezione: giorno_sala_ora_pro.get(giorno).get(sala_id).get(ora).keySet()) {%>
                                        <script>
                                            GSO_B["<%=giorno%>_<%=sala_id%>_<%=ora%>"] = [];
                                            GSO_F["<%=giorno%>_<%=sala_id%>_<%=ora%>"] = "<%=proiezione.getFilm().getTitolo()%>";
                                            <%for (int i = 0; i < giorno_sala_ora_pro.get(giorno).get(sala_id).get(ora).get(proiezione).size(); i++) {%>
                                                <%if(giorno_sala_ora_pro.get(giorno).get(sala_id).get(ora).get(proiezione).get(i).isComprato() || (!(giorno_sala_ora_pro.get(giorno).get(sala_id).get(ora).get(proiezione).get(i).isComprato()) && giorno_sala_ora_pro.get(giorno).get(sala_id).get(ora).get(proiezione).get(i).getCart().getCarrello_id() != null)) {%>
                                                    GSO_B["<%=giorno%>_<%=sala_id%>_<%=ora%>"].push('._<%=giorno_sala_ora_pro.get(giorno).get(sala_id).get(ora).get(proiezione).get(i).getN_riga()%>_<%=giorno_sala_ora_pro.get(giorno).get(sala_id).get(ora).get(proiezione).get(i).getN_colonna()%>');
                                                <%}
                                            }%>
                                        </script>
                                    <%}
                                }
                            }
                        }
                    }%>
            </div>

        <script>
            function selezione_giorno(giorno) {
                if (document.getElementById('button_'+giorno).style.backgroundColor != 'green') {
                    document.querySelector('.sinistra').childNodes.forEach((item) => {
                        if (item.nodeName == 'BUTTON') {
                            item.style.backgroundColor = '#591fc1';
                        }
                    })
                    document.getElementById('sez_sale').childNodes.forEach((item) => {
                        if (item.nodeName == 'BUTTON') {
                            item.style.backgroundColor = '#591fc1';
                        }
                    })
                    document.getElementById('sez_ore').childNodes.forEach((item) => {
                        if (item.nodeName == 'BUTTON') {
                            item.style.backgroundColor = '#591fc1';
                        }
                    })
                    document.getElementById('titolo_film').innerHTML = '';
                    document.getElementById("button_" + giorno).style.backgroundColor = 'green';
                    document.getElementById('sez_sale').style.display = 'block';
                    document.getElementById('sez_ore').style.display = 'none';
                    document.getElementById('sez_posti').style.display = 'none';
                } else {
                    document.getElementById('button_'+giorno).style.backgroundColor = '#591fc1';
                    document.getElementById('sez_sale').style.display = 'none';
                    document.getElementById('sez_ore').style.display = 'none';
                    document.getElementById('sez_posti').style.display = 'none';
                }
            }
            function selezione_sala(sala_id) {
                if (document.getElementById('button_'+sala_id).style.backgroundColor != 'green') {
                    document.getElementById('sez_sale').childNodes.forEach((item) => {
                        if (item.nodeName == 'BUTTON') {
                            item.style.backgroundColor = '#591fc1';
                        }
                    })
                    document.getElementById('sez_ore').childNodes.forEach((item) => {
                        if (item.nodeName == 'BUTTON') {
                            item.style.backgroundColor = '#591fc1';
                        }
                    })
                    document.getElementById('titolo_film').innerHTML = '';
                    document.getElementById("button_" + sala_id).style.backgroundColor = 'green';
                    document.getElementById('sez_ore').style.display = 'block';
                    document.getElementById('sez_posti').style.display = 'none';
                } else {
                    document.getElementById('button_'+sala_id).style.backgroundColor = '#591fc1';
                    document.getElementById('sez_ore').style.display = 'none';
                    document.getElementById('sez_posti').style.display = 'none';
                }
            }
            function selezione_ora(ora) {
                if (document.getElementById('button_'+ora).style.backgroundColor != 'green') {
                    document.getElementById('sez_ore').childNodes.forEach((item) => {
                        if (item.nodeName == 'BUTTON') {
                            item.style.backgroundColor = '#591fc1';
                        }
                    })
                    document.getElementById("button_" + ora).style.backgroundColor = 'green';
                    document.getElementById('sez_posti').style.display = 'unset';
                    set_ticket();
                } else {
                    document.getElementById('button_'+ora).style.backgroundColor = '#591fc1';
                    document.getElementById('sez_posti').style.display = 'none';
                }
            }
            function set_ticket() {
                chiave = '';
                document.querySelector('.sinistra').childNodes.forEach((item) => {
                    if (item.nodeName == 'BUTTON') {
                        if (item.style.backgroundColor == 'green') {
                            chiave = chiave + item.innerHTML + "_";
                        }
                    }
                })
                document.getElementById('sez_sale').childNodes.forEach((item) => {
                    if (item.nodeName == 'BUTTON') {
                        if (item.style.backgroundColor == 'green') {
                            chiave = chiave + item.innerHTML + "_";
                        }
                    }
                })
                document.getElementById('sez_ore').childNodes.forEach((item) => {
                    if (item.nodeName == 'BUTTON') {
                        if (item.style.backgroundColor == 'green') {
                            chiave = chiave + item.innerHTML;
                        }
                    }
                })
                for (var col = 1; col < 21; col++) {
                    for (var row = 1; row < 11; row++) {
                        document.querySelector('._'+row+'_'+col).style.backgroundColor = 'rgb(68,68,68)';
                        document.querySelector('._'+row+'_'+col).innerHTML = '';
                    }
                }
                document.getElementById('titolo_film').innerHTML = '';
                if (Object.keys(GSO_B).includes(chiave)) {
                    document.getElementById('titolo_film').innerHTML = GSO_F[chiave];
                    for (var i = 0; i < GSO_B[chiave].length; i++) {
                        document.querySelector(GSO_B[chiave][i]).style.backgroundColor = 'red';
                        document.querySelector(GSO_B[chiave][i]).innerHTML = '<i class="fa-solid fa-user"></i>';
                        document.querySelector(GSO_B[chiave][i]).firstChild.style.color = 'black';
                    }
                } else {
                    document.getElementById('sez_posti').style.display = 'none';
                    document.getElementById('titolo_film').innerHTML = "Nessuna proiezione presente";
                }
            }
        </script>



                <script>
                    function formSubmit(){
                        var form = document.getElementById("formData");
                        var date = document.getElementById('campoData');
                        if (date.value == '') {
                            alert("Selezione una data");
                        } else {
                            form.submit();
                        }
                    }
                </script>

        <%@include file="/include/footer.inc"%>
    </body>
</html>
