/* 
 * Copyright:      Copyright 2017 (c) Parametric Technology GmbH
 * Product:        PTC Integrity Lifecycle Manager
 * Author:         Volker Eckardt, Principal Consultant ALM
 * Purpose:        Custom Developed Code
 * **************  File Version Details  **************
 * Revision:       $Revision: 1.1 $
 * Last changed:   $Date: 2017/05/17 03:06:05CEST $
 */

// Standard Login Credentials
var username = "veckardt";
var password = "password";
var servername = "veckardt.de";
var port = "7001";

//        function addRow(tableID) {
//
//            var table = document.getElementById(tableID);
//            var rowCount = table.rows.length;
//            var colCount = table.rows[0].cells.length;
//
//            var row = table.insertRow(table.rows.length);
//            for (var i = 0; i < colCount; i++) {
//                var newcell = row.insertCell(i);
//                if (i === (colCount - 1)) {
//                    newcell.innerHTML = "<INPUT type=\"button\" value=\"Remove\" id=\"deletenode\" onclick=\"removeRow(this)\"/>";
//                } else {
//                    newcell.innerHTML = table.rows[1].cells[i].innerHTML;
//                }
//            }
//        } 

$(window).load(function() {
    // start a counter for new row IDs
    // by setting it to the number
    // of existing rows
    $('.datepick').datepicker({dateFormat: 'dd-mm-yy'});
    var newRowNum = 0;

    // bind a click event to the "Add" link
    $('#addnote').click(function() {
        // increment the counter
        // number of table rows + 1
        newRowNum = $(dataTable).children('tbody').children('tr').length + 1;
        // get the entire "Current" row --
        // "this" refers to the clicked element
        // and "parent" moves the selection up
        // to the parent node in the DOM
        // var currRow = $(this).parent().parent();
        var currRow = $("#dataTable TBODY tr:first");

        // copy the entire row from the DOM
        // with "clone"
        var newRow = currRow.clone();

        // set the values of the inputs
        // in the "Add" row to empty strings
        $('input', newRow).val('');
        // $('input.addnote', newRow).val('New Row');
        $('select', newRow).val('');
        $('textarea', newRow).val('');

        // insert a remove link in the last cell
        // $('td:last-child', newRow).html('<a href="" class="remove"><i class="icon-minus">Remove<\/i><\/a>');
        // onclick=\"removeRow(this)\"
        $('td:last-child', newRow).html('<INPUT type=\"button\" class="remove" value=\"Remove\" id=\"deletenode\"/>');

        // insert the new row into the table
        // "before" the Add row
        // currRow.after(newRow);
        $('#dataTable tr:last').after(newRow);

        // add the remove function to the new row
        $('INPUT.remove', newRow).click(function() {
            $(this).closest('tr').remove();
            return false;
        });

        $('#date', newRow).each(function(i) {
            var newID = 'date_' + newRowNum;
            $(this).attr('id', newID).removeClass('hasDatepicker')
                    .removeData('datepicker')
                    .unbind()
                    .datepicker({dateFormat: 'dd-mm-yy'});
            i++;
        });

        // prevent the default click
        return false;
    });

    // remove's default rows
    $('.removeDefault').click(function() {
        $(this).closest('tr').remove();
        return false;
    });

});

/**
 * This method deletes the specified section of the table
 * OR deletes the specified rows from the table.
 */
//        function removeRow(src) {
//
//            var oRow = src.parentElement.parentElement;
//            // delete it passing in its rowIndex
//            document.getElementById("dataTable").deleteRow(oRow.rowIndex);
//        }

function make_base_auth(user, password) {
    var tok = user + ':' + password;
    var hash = btoa(tok);
    return "Basic " + hash;
}
function jsonEscape(str) {
    return str.replace(/[\\]/g, '\\\\')
            .replace(/[\"]/g, '\\\"')
            .replace(/[\/]/g, '\\/')
            .replace(/[\b]/g, '\\b')
            .replace(/[\f]/g, '\\f')
            .replace(/[\n]/g, '\\n')
            .replace(/[\r]/g, '\\r')
            .replace(/[\t]/g, '\\t');
}

function convertTable(tableID)
{
    // var table = document.getElementById(tableID);
    // var formdata = tableToJson(table);
    document.getElementById("result").innerHTML = "";

    var table = $("#body").find('input,select,textarea,div').serializeArray();
    // var formdata = JSON.stringify(table);
    // console.log("Form Data before:");
    // console.log(formdata);
    var formdata = "{\"id\":\"" + document.getElementById("id").innerHTML + "\",";
    var i = 0;
    $.each(table, function() {
        i++;
        if (i === 5) {
            formdata += ",\"nodelist\" : [{";
        }
        if (i > 5 && this.name === "textkey") {
            formdata += "},{";
        }
        if (i > 1 && this.name !== "textkey") {
            formdata += ",";
        }
        formdata = formdata + "\"" + this.name + "\":\"" + jsonEscape(this.value) + "\"";
    });
    formdata = formdata + "}]}";
    console.log("Form Data: " + formdata);
    // JSON.parse(JSON.stringify(jsonObject));
    // formdate = JSON.stringify(formdata);
    JSON.parse(formdata);
    document.body.style.cursor = "wait";
    window.setTimeout(function() {
        document.body.style.cursor = "auto";
    }, 1000);

    var url = "http://" + servername + ":" + port + "/IntegrityREST/mms";

    if (1 === 1) {
        $.ajax({
            url: url,
            method: 'POST',
            crossDomain: true,
            async: false,
            dataType: 'json',
            data: formdata,
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            beforeSend: function(xhr) {
                xhr.setRequestHeader("Authorization", "Basic " + btoa(username + ":" + password));
            },
            success: function(data, txtStatus, xhr) {
                console.log("Return: " + data);
                console.log("Status: " + xhr.status);
                document.getElementById("result").innerHTML = "<h4>Result:</h4>Meeting Minutes with ID " + data.id + " and " + (data.nodelist.length) + " Notes created/updated." + "<br><br>Status: " + xhr.status;
                updateNodeIDs(data);
                document.getElementById("id").innerHTML = data.id;
            },
            error: function(data, txtStatus, xhr) {
                console.log(data);
                document.getElementById("result").innerHTML = data.responseText;
            }
        });
    }
    // document.body.style.cursor = "auto";
    return false;
}

function updateNodeIDs(data) {
    var rows = document.getElementById('dataTable').rows;
    console.log("rows.length: " + rows.length);
    for (k = 1; k < rows.length; k++) {

        console.log("ID: " + data.nodelist[k - 1].nodeid);

        var y = rows[k].cells;
        y[7].innerHTML = "<INPUT type=\"hidden\" id=\"nodeid\" name=\"nodeid\" value=\"" + data.nodelist[k - 1].nodeid + "\"/>";
    }
}

// Call Reload function
function reloadData() {
    return false;
}

window.onbeforeunload = function() {
    return false;
};


