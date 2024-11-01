<%String contextPath=request.getContextPath();%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    Boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    String carrelloID = (String) request.getAttribute("carrelloID");
    String filmID = (String) request.getAttribute("filmID");
    String destinazione = (String) request.getAttribute("destinazione");
    String applicationMessage = (String) request.getAttribute("applicationMessage");

%>
<html>
<head>

    <link rel="icon" type="image/x-icon" href="images/popcorn.ico">
    <link rel="stylesheet" href="css/General_settings.css" type="text/css" media="screen">



    <title>Errore :|</title>
    <!-- Funzione per settare la stringa applicationMessage onLoad -->
    <script>

        function onLoadHandler() {
            setTimeout(() => {
                <%if(loggedOn){%>
                document.getElementById("carrelloID").value = getValore(getCookie('cart'), 0);
                <%}%>
                var form = document.getElementById("confermaForm");
                form.submit();
            }, 2000);
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

    <style>

        .errore{

            display: flex;
            justify-content: center;
            align-items: center;
            flex-wrap: wrap;
            flex-direction: column;

        }

        .errore img{

            width: 40%;
            border-radius: 6%;
            max-width: 100%;

        }

        .errore h1{

            font-size: 40px;
            font-weight: bold;
            margin-top: calc(5%);
            color: beige;
            margin-bottom: 20px;
        }



    </style>

</head>
<body>

<div class="errore">
<h1><%=applicationMessage%></h1>
<img src="images/errore.jpg">
</div>

<form id="confermaForm" action="Dispatcher" method="post">
    <input id="inpuType" type="hidden" name="controllerAction" value="<%=destinazione%>"/>
    <input id="filmID" type="hidden" name="filmID" value="<%=filmID%>">
    <input id="carrelloID" type="hidden" name="carrelloID" value=""/>
</form>


</body>
</html>
