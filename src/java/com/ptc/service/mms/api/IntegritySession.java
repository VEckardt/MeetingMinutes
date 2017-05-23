/*
 * Copyright:      Copyright 2017 (c) Parametric Technology GmbH
 * Product:        PTC Integrity Lifecycle Manager
 * Author:         Volker Eckardt, Principal Consultant ALM
 * Purpose:        Custom Developed Code
 * **************  File Version Details  **************
 * Revision:       $Revision: 1.1 $
 * Last changed:   $Date: 2017/05/16 01:58:51CEST $
 */
package com.ptc.service.mms.api;

import com.mks.api.CmdRunner;
import com.mks.api.Command;
import com.mks.api.IntegrationPoint;
import com.mks.api.IntegrationPointFactory;
import com.mks.api.Option;
import com.mks.api.Session;
import com.mks.api.response.APIException;
import com.mks.api.response.Item;
import com.mks.api.response.ItemList;
import com.mks.api.response.Response;
import com.mks.api.response.WorkItem;
import com.mks.api.response.WorkItemIterator;
import com.mks.api.util.ResponseUtil;
import java.io.IOException;
import static java.lang.System.out;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author veckardt
 */
public class IntegritySession {

    private static IntegrationPoint integrationPoint = null;
    private static Session apiSession = null;
    private static ConnectionDetails connection = null;
    // private static Map<String, String> allUsers = new HashMap<>();
    // https://github.com/jasonray/jersey-starterkit/wiki/Serializing-a-POJO-to-xml-or-json-using-JAXB

    public static void connect() throws APIException, IOException {
        connection = new ConnectionDetails(); // SecurityInterceptor.username, SecurityInterceptor.password);
        IntegrationPointFactory ipf = IntegrationPointFactory.getInstance();
        integrationPoint = ipf.createIntegrationPoint(connection.getHostname(), connection.getPort(), 4, 11);
        apiSession = integrationPoint.createNamedSession(null, null, connection.getUser(), connection.getPassword());
        apiSession.setDefaultUsername(connection.getUser());
        apiSession.setDefaultPassword(connection.getPassword());
        out.println(connection.getLoginInfo());
    }

    public static Response execute(Command cmd) throws APIException {
        long timestamp = System.currentTimeMillis();
        CmdRunner cmdRunner = apiSession.createCmdRunner();
        cmdRunner.setDefaultUsername(connection.getUser());
        cmdRunner.setDefaultPassword(connection.getPassword());
        cmdRunner.setDefaultHostname(connection.getHostname());
        cmdRunner.setDefaultPort(connection.getPort());
        Response response = cmdRunner.execute(cmd);
        cmdRunner.release();
        timestamp = System.currentTimeMillis() - timestamp;
        System.out.println("REST-API: " + response.getCommandString() + " [" + timestamp + "ms]");
        return response;
    }

    public static void release() throws APIException, IOException {
        if (apiSession != null) {
            apiSession.release();
        }
        if (integrationPoint != null) {
            integrationPoint.release();
        }
    }

    public static String getQueryFields(String queryName) throws APIException {
        // im queries --fields=fields "All Defects"
        Command cmd = new Command(Command.IM, "queries");
        cmd.addOption(new Option("fields", "fields"));
        cmd.addSelection(queryName);
        Response response = execute(cmd);
        WorkItem wi = response.getWorkItem(queryName);
        return wi.getField("fields").getValueAsString();
    }

    public static JSONArray getAllowedNodeCategories(String typeName) throws APIException, IOException, JSONException {
        JSONArray jsonArr = new JSONArray();
        try {
            connect();
            Command cmd = new Command(Command.IM, "types");
            cmd.addOption(new Option("fields", "fieldRelationships"));
            cmd.addSelection(typeName);
            Response response = execute(cmd);
            WorkItem group = response.getWorkItem(typeName);
            // ResponseUtil.printResponse(response, 1, System.out);
            ItemList wii = (ItemList) group.getField("fieldRelationships").getList();
            Item refItemType = wii.getItem("Referenced Item Type");

            ItemList targetFields = (ItemList) refItemType.getField("targetFields").getList();
            Iterator relationships = targetFields.getItems();
            while (relationships.hasNext()) {
                Item item = (Item) relationships.next();
                if (item.getId().equals("Category")) {
                    String cliSpec = item.getField("cliSpec").getValueAsString();
                    // Referenced Item Type=Shared Meeting Minutes Entry:Category=Entry,ToDo,Agenda,Decision,Info
                    if (cliSpec.contains("=Shared ")) {
                        String entries = cliSpec.split("=")[2];
                        for (String entry : entries.split(",")) {
                            JSONObject jsonObj = new JSONObject();
                            jsonObj.put(entry, entry);
                            jsonArr.put(jsonObj);
                        }
                    }
                }
            }
            release();
        } catch (APIException ex) {
            ExceptionHandler eh = new ExceptionHandler(ex);
            Logger.getLogger(IntegritySession.class.getName()).log(Level.SEVERE, eh.getMessage(), eh);
            throw new APIException(ex);
        } catch (IOException ex) {
            Logger.getLogger(IntegritySession.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new IOException(ex);
        }
        return jsonArr;
    }

    public static JSONArray getUsersFromGroup(String GroupName) throws APIException, IOException, JSONException {
        JSONArray jsonArr = new JSONArray();
        try {
            connect();
            // aa groups --members Managers
            Command cmd = new Command(Command.AA, "groups");
            cmd.addOption(new Option("members"));
            cmd.addSelection(GroupName);
            Response response = execute(cmd);
            WorkItem group = response.getWorkItem(GroupName);
            // ResponseUtil.printResponse(response, 1, System.out);
            ItemList wii = (ItemList) group.getField("members").getList();
            Iterator members = wii.getItems();
            while (members.hasNext()) {
                Item item = (Item) members.next();
                JSONObject jsonObj = new JSONObject();
                jsonObj.put(item.getId(), getFullName(item.getId()));
                jsonArr.put(jsonObj);
            }
            release();
        } catch (APIException ex) {
            ExceptionHandler eh = new ExceptionHandler(ex);
            Logger.getLogger(IntegritySession.class.getName()).log(Level.SEVERE, eh.getMessage(), eh);
            throw new APIException(ex);
        } catch (IOException ex) {
            Logger.getLogger(IntegritySession.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new IOException(ex);
        }
        return jsonArr;
    }

    private static String getFullName(String userName) throws APIException {
        // im users --fields=name,fullname
        try {
            Command cmd = new Command(Command.IM, "users");

            cmd.addOption(new Option("fields", "name,fullname"));
            cmd.addSelection(userName);
            Response response = execute(cmd);
            WorkItem user = response.getWorkItem(userName);
            return user.getField("fullname").getValueAsString();
        } catch (APIException ex) {
            ExceptionHandler eh = new ExceptionHandler(ex);
            Logger.getLogger(IntegritySession.class.getName()).log(Level.SEVERE, eh.getMessage(), eh);
            throw new APIException(ex);
        }
    }

    public static JSONArray getAllItems(String queryName) throws APIException, JSONException {
        // String fieldList = "ID,Summary,State,Description,Project";
        JSONArray jsonArr = new JSONArray();
        try {
            connect();
            String fieldList = getQueryFields(queryName);

            Command cmd = new Command(Command.IM, "issues");
            cmd.addOption(new Option("query", queryName));
            cmd.addOption(new Option("fields", fieldList));
            Response response = execute(cmd);
            WorkItemIterator wii = response.getWorkItems();
            while (wii.hasNext()) {
                WorkItem wi = wii.next();
                JSONObject jsonObj = new JSONObject();
                for (String fieldName : fieldList.split(",")) {
                    String fieldValue = wi.getField(fieldName).getValueAsString();
                    jsonObj.put(wi.getField(fieldName).getDisplayName(), fieldValue == null ? "" : fieldValue);
                    // System.out.println(wi.getField(fieldName).getDisplayName());
                }
                jsonArr.put(jsonObj);
            }
            release();
        } catch (APIException ex) {
            Logger.getLogger(IntegritySession.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new APIException(ex);
        } catch (IOException ex) {
            Logger.getLogger(IntegritySession.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new APIException(ex);
        }
        return jsonArr;
    }
}
