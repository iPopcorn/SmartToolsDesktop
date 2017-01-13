package main;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;


/**
 * Class that will contact the server and handle requests (primarily inserting tool(s))
 */

public class ServerRequest {

    private String serverIP;

    /*
     * Basic constructor that will initialize the base serverIP and blank response.
     */
    public ServerRequest() {
        this.serverIP = "http://129.107.132.26/"; // server IP that will be called to receive responses
    }

    /*
     * getResponseFromRequest(String serverPath)
     * This function will return the response from the passed path parameter on the server
     * and POST data sent.
     * An example path parameter input would be "tools/get-tools.php".
     */
    public void getResponseFromRequest(String serverPath, HashMap<String, String> POSTdata) {

        if (POSTdata == null || POSTdata.isEmpty()) { // if no hashmap is passed or the hashmap is empty
            System.out.println("NO POST DATA");
            return;
        }

        String myURL = serverIP + serverPath;
        StringBuilder sb = new StringBuilder();
        System.out.println("Requested URL: " + myURL);
        HttpURLConnection urlConn = null;
        String urlParameters = "";
        String tempVal = "";


        try {
            URL url = new URL(myURL);
            urlConn = (HttpURLConnection) url.openConnection(); // opening the connection

            // adding request header
            urlConn.setRequestMethod("POST"); // setting the request method

            if (POSTdata != null) {

                for (String key : POSTdata.keySet()) {
                    tempVal = POSTdata.get(key);
                    urlParameters += key + "=" + tempVal + "&";
                }
            }
            urlParameters = urlParameters.substring(0, urlParameters.length() - 1); // removing the final '&'
            System.out.println(urlParameters);

            // Send post request
            urlConn.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(urlConn.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int responseCode = urlConn.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + urlParameters);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(urlConn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            System.out.println(response.toString());


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
