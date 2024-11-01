 <!-- Defining the header and nav section of the page -->
             <header class="griglia_head">

                 <div class="logo_head">
                     <h1><a href="Dispatcher?controllerAction=HomeManagement.view">Cineplex</a></h1>
                 </div>


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



             </header>

    <nav class="navbar">
            <ul>
                <li <%=menuActiveLink.equals("Home")?"class=\"active\"":""%>>
                    <a href="Dispatcher?controllerAction=AccountManagement.view">INFO</a>
                </li>
                <li <%=menuActiveLink.equals("Home")?"class=\"active\"":""%>>
                    <a href="Dispatcher?controllerAction=AccountManagement.viewTicket">BIGLIETTI</a>
                </li>
                <li <%=menuActiveLink.equals("Home")?"class=\"active\"":""%>>
                    <a href="Dispatcher?controllerAction=AccountManagement.viewRew">RECENSIONI</a>
                </li>
                <li <%=menuActiveLink.equals("Home")?"class=\"active\"":""%>>
                    <a href="Dispatcher?controllerAction=AccountManagement.viewMetodiPagamento">METODI PAGAMENTO</a>
                </li>

                <%

                    if(loggedUser.getConfermaEmail().equals("false")){%>
                <li <%=menuActiveLink.equals("Home")?"class=\"active\"":""%>>
                    <a href="Dispatcher?controllerAction=AccountManagement.viewConferma">CONFERMA ACCOUNT</a>
                </li>
                <%}%>
            </ul>
    </nav>