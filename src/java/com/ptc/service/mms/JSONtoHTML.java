/*
 * Copyright:      Copyright 2017 (c) Parametric Technology GmbH
 * Product:        PTC Integrity Lifecycle Manager
 * Author:         Volker Eckardt, Principal Consultant ALM
 * Purpose:        Custom Developed Code
 * **************  File Version Details  **************
 * Revision:       $Revision$
 * Last changed:   $Date$
 */
package com.ptc.service.mms;

/**
 *
 * @author veckardt
 */
import com.mks.api.response.APIException;
import com.ptc.service.mms.api.IntegritySession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Crunchify.com
 *
 */
public class JSONtoHTML {

    public static JSONArray getJSONArrayFromFile() throws JSONException {
        JSONObject jsonObj = new JSONObject();
        JSONObject jsonObj2 = new JSONObject();
        JSONObject jsonObj3 = new JSONObject();

        JSONArray jsonArr = new JSONArray();

        jsonObj.put("Name", "eBay");
        jsonObj.put("Phone", "123-123-1234");
        jsonObj.put("Address", "Bay Area");

        jsonObj2.put("Name", "Paypal");
        jsonObj2.put("Phone", "345-345-3456");
        jsonObj2.put("Address", "1st North First Street, San Jose");

        jsonObj3.put("Name", "Google");
        jsonObj3.put("Phone", "890-890-8909");
        jsonObj3.put("Address", "Mountain View");

        jsonArr.put(jsonObj);
        jsonArr.put(jsonObj2);
        jsonArr.put(jsonObj3);
        return jsonArr;
    }

    public static JSONArray getJSONArrayFromIntegrity() throws JSONException, APIException {
        return IntegritySession.getAllItems("All Meeting Minutes");
    }
}
