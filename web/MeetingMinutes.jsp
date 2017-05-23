<%@page import="com.ptc.service.mms.api.IntegritySession"%>
<!DOCTYPE HTML>
<HEAD>
    <meta charset="utf-8"/>
    <TITLE>Integrity Meeting Minutes</TITLE>
    <link rel="stylesheet" type="text/css" href="css/MeetingMinutes.css">
    <link rel="stylesheet" type="text/css" href="css/jquery-ui.css">
    <!-- script src="jquery.min.js"></script -->
    <script src="js/jquery-1.12.4.js"></script>
    <script src="js/jquery-ui.js"></script>
    <script type="text/javascript" src="js/JSONtoSELECT.js"></script>
    <script src="MeetingMinutes.js"></script>
</HEAD>
<BODY id="body" style="font-family:'Verdana'" onkeydown="return (event.keyCode !== 116)">

    <%
        // REad a specific List of Users from a specified Group
        String userGroup = "MeetingMinutes";
        String jsonData = IntegritySession.getUsersFromGroup(userGroup).toString();
        // System.out.println(jsonData);
        // parse this group into JS variable
        pageContext.setAttribute("userGroup", jsonData);

        String itemType = "Meeting Minutes Entry";
        jsonData = IntegritySession.getAllowedNodeCategories(itemType).toString();
        pageContext.setAttribute("categories", jsonData);

    %>    

    <script type="text/javascript">
        $(document).ready(function() {
            //alert(${message});
            var ccbGroup = {"d": ${userGroup}};
            $('#Creator').hide();
            $('#CreatorSection').append(JSONtoSELECT(ccbGroup.d, "assignedUser")).fadeIn();

            $('#Assignee').hide();
            $('#AssigneeSection').append(JSONtoSELECT(ccbGroup.d, "assignee")).fadeIn();

            var categories = {"d": ${categories}};
            $('#Category').hide();
            $('#CategorySection').append(JSONtoSELECT(categories.d, "category")).fadeIn();
        });
    </script>        

    <img src="images/MeetingMinutes.png" alt="Meeting Minutes" width="320" height="70">

    <TABLE id="headerTable">
        <TR>
            <TD>Doc ID:</TD><TD><div id="id">-</div></TD>
        </TR>
        <TR>
            <TD>Title:</TD><TD><INPUT type="text" id="summary" name="summary" size="55"/></TD>
        </TR>
        <TR>
            <TD>Attendees:</TD><TD><INPUT type="text" id="attendees" name="attendees" size="55"/></TD>
        </TR>
        <TR>
            <TD>Created By:</TD><TD><div id="CreatorSection"><div id="Creator"></div></div></TD>
        </TR>
        <TR style="display:none;">
            <TD>Project:</TD><TD><INPUT type="hidden" id="project" name="project" value="/Projects" /></TD>
        </TR>            
    </TABLE>
    <hr>
    <form name="Notes" id="Notes" method="post">
        <input type="submit" value="Submit" onclick="convertTable('dataTable');
                return false;" id="submitbutton" />
        <INPUT type="button" class="addnote" value="Add Note" name="addnote" id="addnote"  />
        <!-- input type="submit" value="Submit" onclick="return convertTable('dataTable');"  / -->
        <!-- input type="submit" value="Reload" onclick="return reloadData();" 
            onclick="addRow('dataTable')"
        / -->     

        <hr>
        <TABLE id="dataTable">
            <THEAD>
                <TR>
                    <TH>&nbsp;</TH>
                    <TH>Topic</TH>
                    <TH>Category</TH>
                    <TH>Text</TH>
                    <TH>Action</TH>
                    <TH>Assignee</TH>
                    <TH>Target Date</TH>
                    <TH>&nbsp;</TH>
                    <!-- TH>&nbsp;</TH -->
                </TR>
            </THEAD>
            <TBODY>
                <TR valign="top">
                    <TD>
                        <INPUT type="checkbox" name="chk"/>
                    </TD>
                    <TD>
                        <INPUT type="text" name="textkey"/>
                    </TD>
                    <TD>
                        <div id="CategorySection"><div id="Category"></div></div>
                    </TD>
                    <TD>
                        <textarea name="text" cols="50" ></textarea>
                    </TD>
                    <TD>
                        <textarea name="action"></textarea>
                    </TD>
                    <TD>
                        <div id="AssigneeSection"><div id="Assignee"></div></div>
                    </TD>
                    <TD>
                        <input id="date" class="datepick" name="targetdate" type="text" />
                        <!-- input type="text" id="targetdate" name="targetdate"/ -->
                    </TD>
                    <TD>
                        <INPUT type="hidden" id="nodeid" name="nodeid"/>
                    </TD>
                    <!-- TD><INPUT type="button" class="addnote" value="Add Note" name="addnote" id="addnote"  /></TD -->
                    <!-- td><a id="addnew" href="">New Row</a></td -->
                </TR>
            </TBODY>
        </TABLE>
    </FORM>

    <!-- the result of the REST request will be rendered inside this div -->
    <div id="result">-</div>

</BODY>
</HTML>