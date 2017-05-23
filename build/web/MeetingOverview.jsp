<%@page import="com.ptc.service.mms.api.IntegritySession"%>
<%@page import="com.ptc.service.mms.JSONtoHTML"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!-- %@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"% -->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<!-- https://crunchify.com/crunchifyjsontohtml-js-json-to-html-table-converter-script/ -->

<html>
    <head>
        <title>Integrity Meetings Overview</title>
        <script type="text/javascript" src="js/JSONtoTABLE.js"></script>
        <!-- script src="http://code.jquery.com/jquery-1.10.1.min.js" type="text/javascript"></script -->
        <script type="text/javascript" src="js/jquery.min.js" ></script>
        <link rel="stylesheet" type="text/css" href="css/JSONtoHTML.css">
    </head>

    <body>

        <%
            // JSONtoHTML.getJSONArrayFromIntegrity()
            String jsonData = IntegritySession.getAllItems("All Meeting Minutes").toString();
            // System.out.println(jsonData);
            pageContext.setAttribute("jsonArr", jsonData);
        %>

        <script type="text/javascript">
            $(document).ready(function() {
                //alert(${message});
                var myJsonArr = {"d": ${jsonArr}};
                $('#Data').hide();
                $('#DataGroup').append(JSONtoTABLE(myJsonArr.d, "")).fadeIn();
            });
        </script>
        <div align="center">
            <div style="font-family: verdana; padding: 10px; border-radius: 10px; border: 3px solid #EE872A; width: 50%; font-size: 12px;">
                All Meeting Minutes
            </div>
        </div>
        <form id="form1" action="">
            <div id="DataGroup" align="center">
                <div id="Data"></div>
            </div>
        </form>


    </body>
</html>