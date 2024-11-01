<!-- Defining the header and nav section of the page -->
             <header class="griglia_head">

                 <div class="logo_head">
                     <h1><a href="Dispatcher?controllerAction=AdminManagement.view">Cineplex - ADMIN</a></h1>
                 </div>

                 <div class="user_div">
                     <div class="Nome_head">
                         <p class="Benv_h1">Benvenuto,</br><b><%=loggedAdmin.getAdmin_name()%></b></p>
                     </div>

                     <div class="tooltip" onclick="logoutForm.submit()">
                         <form name="logoutForm" action="Dispatcher" method="post">
                             <input type="hidden" name="controllerAction" value="AdminManagement.logout"/>
                         </form>
                         <i class="fa-solid fa-arrow-right-from-bracket logout"></i>
                         <h1 class="testoLogout">Logout</h1>
                     </div>
                 </div>

             </header>

             <!-- Defining the navigation menu -->
             <nav class="navbar">
                 <ul>
                     <li <%=menuActiveLink.equals("Home")?"class=\"active\"":""%>>
                         <a href="Dispatcher?controllerAction=AdminManagement.creaFilm">AGGIUNGI FILM</a>
                     </li>
                     <li <%=menuActiveLink.equals("Home")?"class=\"active\"":""%>>
                         <a href="Dispatcher?controllerAction=AdminManagement.nuovaProiezione">NUOVA PROIEZIONE</a>
                     </li>
                     <li <%=menuActiveLink.equals("Home")?"class=\"active\"":""%>>
                         <a href="Dispatcher?controllerAction=AdminManagement.viewRew">RECENSIONI</a>
                     </li>
                     <li <%=menuActiveLink.equals("Home")?"class=\"active\"":""%>>
                         <a href="Dispatcher?controllerAction=AdminManagement.viewStatoSala">STATO SALA</a>
                     </li>
                     <%if(loggedAdmin.getAdmin_root()){%>
                     <li <%=menuActiveLink.equals("Home")?"class=\"active\"":""%>>
                         <a href="Dispatcher?controllerAction=AdminManagement.viewAggiungiAdmin">NUOVO ADMIN</a>
                     </li>
                     <%}%>
                 </ul>
             </nav>
