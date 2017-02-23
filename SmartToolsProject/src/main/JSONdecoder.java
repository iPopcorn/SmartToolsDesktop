package main;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/*
 * Class that will decode the JSON retrieved from the database, and return an ArrayList of tools
 * decoded from the JSON.
 */
public class JSONdecoder {

    /*
      decodeJSONToolResponse(String JSONstring)
        PARAMETERS: JSONString, a string in the JSON format in the form of
        "{"tool_name":"zzzzzz","tool_address":"xxxxx","tag_id":"yyyyy"}"
        RETURN VALUES: Returns an arraylist of class Tool with each tool read-in from the JSON response.
       */
    public ArrayList<Tool> decodeJSONToolResponse(String JSONstring) {
        String[] jsonStrings = JSONstring.split("(?<=})"); // reading
        ArrayList<Tool> toolList = new ArrayList<>(); // the list of tools to be returned
        Tool tempTool; // temporary tool object to be added to tool list

        JSONObject tempJSON; // temp JSON object from each tool read in
        try {
            for (String string: jsonStrings) {
                tempJSON = new JSONObject(string);
                tempTool = new Tool(tempJSON.getString("tool_name"), tempJSON.getString("tag_id"),
                        tempJSON.getString("tool_address"));
                toolList.add(tempTool);
            }
            for (Tool tool: toolList) {
                System.out.println(tool.toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("ERROR READING THE JSON");
        }
        return toolList;
    }

    public ArrayList<String> decodeJSONAddressResponse(String JSONstring) {
        String[] jsonStrings = JSONstring.split("(?<=})"); // reading
        ArrayList<String> openAddresses = new ArrayList<>(); // the list of tools to be returned

        JSONObject tempJSON; // temp JSON object from each tool read in
        try {
            for (String string: jsonStrings) {
                tempJSON = new JSONObject(string);

                openAddresses.add(tempJSON.getString("address"));
            }
            for (String string: openAddresses) {
                System.out.println(string);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("ERROR READING THE JSON");
        }
        return openAddresses;
    }
}