/* 
 * Copyright:      Copyright 2017 (c) Parametric Technology GmbH
 * Product:        PTC Integrity Lifecycle Manager
 * Author:         Volker Eckardt, Principal Consultant ALM
 * Purpose:        Custom Developed Code
 * **************  File Version Details  **************
 * Revision:       $Revision$
 * Last changed:   $Date$
 */


function JSONtoSELECT(objArray, field) {

    var array = typeof objArray !== 'object' ? JSON.parse(objArray) : objArray;

    // console.log(array);

    var str = '<SELECT name="' + field + '">';

    // table body
    for (var i = 0; i < array.length; i++) {
        for (var index in array[i]) {
            // str += '<td>' + array[i][index] + '</td>';
            // console.log(index);
            // var name = index;
            str += '<OPTION value="' + index + '">' + array[i][index] + '</OPTION>';
        }

    }
    str += '</SELECT>';
    // console.log(str);
    return str;
}