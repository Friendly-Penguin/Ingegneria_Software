 <!-- Defining the header and nav section of the page -->
             <header class="griglia_head">

                 <div class="logo_head">
                     <h1><a href="Dispatcher?controllerAction=HomeManagement.view">Cineplex</a></h1>
                 </div>



                 <%if (!loggedOn) {%>

                 <div class="login_sigin">

                     <form name="loginForm" action="Dispatcher" method="post">
                         <input type="hidden" name="controllerAction" value="SignInManagement.view"/>
                     </form>

                     <a class="icona_log" href="javascript:loginForm.submit()">
                         <i class="fa-solid fa-pen-to-square icona_sign"></i></i>
                         <h1 class="testoLogout">Accedi/Registrati</h1>
                     </a>

                 </div>

                 <%}else{%>

                 <div class="bottone_head">
                 <form name="bottoneForm" action="Dispatcher" method="post">
                  <input type="hidden" name="controllerAction" value="AcquistaProiezioniManagement.view"/>
                     <button type="submit" class="bottone">Acquista/Prenota</br>il tuo spettacolo</button>
                     </form>
                 </div>

                 <div class="user_div">
                     <div class="Nome_head">
                         <p class="Benv_h1">Benvenuto,</br><b><%=loggedUser.getNome()%></b></p>
                     </div>

                     <div class="tooltip"  onclick="accountForm.submit()">
                         <form name="accountForm" action="Dispatcher" method="post">
                             <input type="hidden" name="controllerAction" value="AccountManagement.view"/>
                         </form>
                         <i class="fa-solid fa-user icona"></i>
                         <h1 class="testoAccount">Account</h1>
                     </div>

                     <div class="tooltip" onclick="logoutForm.submit()">
                         <form name="logoutForm" action="Dispatcher" method="post">
                             <input type="hidden" name="controllerAction" value="SignInManagement.logout"/>
                         </form>
                         <i class="fa-solid fa-arrow-right-from-bracket logout"></i>
                         <h1 class="testoLogout">Logout</h1>
                     </div>

                     <div class="tooltip" onclick="carrelloForm.submit()">
                         <form name="carrelloForm" action="Dispatcher" method="post">
                             <input type="hidden" name="controllerAction" value="CarrelloManagement.view"/>
                             <input id="carrelloID" type="hidden" name="carrelloID" value="">
                         </form>
                         <div id="cartItems"></div>
                         <i class="fa-solid fa-cart-shopping carrello"></i>
                         <h1 class="testoAccount">Carrello</h1>
                     </div>
                 </div>

                 <%}%>

             </header>

             <!-- Defining the navigation menu -->
             <nav class="navbar">
                                 <ul>
                                     <li <%=menuActiveLink.equals("Home")?"class=\"active\"":""%>>
                                         <a href="Dispatcher?controllerAction=HomeManagement.viewAbbonamento">PREZZI/ABBONAMENTI</a>
                                     </li>
                                     <li <%=menuActiveLink.equals("Home")?"class=\"active\"":""%>>
                                         <a href="Dispatcher?controllerAction=HomeManagement.view">IN SALA</a>
                                     </li>
                                     <li <%=menuActiveLink.equals("Home")?"class=\"active\"":""%>>
                                         <a href="Dispatcher?controllerAction=HomeManagement.viewProssimamente">PROSSIMAMENTE</a>
                                     </li>
                                 </ul>
                         </nav>

             <%if((loggedUser != null) && (loggedUser.getConfermaEmail().equals("false"))){%>
             <!-- Alert conferma account -->
             <form id="formConf" action="Dispatcher" method="post" >
                    <input id="inputType" type="hidden" name="controllerAction" value="AccountManagement.viewConferma"/>
                    <div class="AlertConferma" id="AlertConferma" onclick="formSuBmit()">
                        <h1>Non hai ancora verificato la tua email! <br>Clica qui per farlo ora!</h1>
                     </div>
                     <i class="fa-regular fa-circle-xmark cerchio" onclick="funzioneNascondi()"></i>
             </form>
            <%}%>

            <script>

                function formSuBmit(){
                    var form = document.getElementById("formConf");
                    form.submit();
                }

            </script>