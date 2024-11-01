<%@page import="com.cinema.cin.model.mo.User"%>
<%@ page import="com.cinema.cin.model.mo.Admin" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<%
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Admin";
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    Admin loggedAdmin = (Admin) request.getAttribute("loggedAdmin");

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
            try { mainOnLoadHandler(); } catch (e) {}
            if (applicationMessage!=undefined) alert(applicationMessage);
        }
        window.addEventListener("load", onLoadHandler);
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

        <div class="sezioneTitolo">
            <a href="Dispatcher?controllerAction=SignInManagement.view">
                <i class="fa-solid fa-arrow-left"></i>
            </a>
            <h2 id="Titles">Accedi - Sezione Admin</h2>
        </div>
        <form name="adminForm" action="Dispatcher" method="post">
            <input id="inpuType" type="hidden" name="controllerAction" value="AdminManagement.logon"/>

            <div class="input-group">

                <div class="input-filed" id="inputEmail">
                    <i class="fa-solid fa-envelope"></i>
                    <input type="email" name="campoEmail" id="campoEmail" placeholder="Email" maxlength="40" required>
                </div>

                <div class="input-filed" id="inputPassword">
                    <i class="fa-solid fa-key"></i>
                    <input type="password" name="campoPassword" id="campoPassword" placeholder="Password" maxlength="40" required>
                </div>

                <div class="btn">
                    <button type="submit" id="invia" >Conferma</button>
                </div>

            </div>

        </form>
    </div>


</main>

</body>
</html>
