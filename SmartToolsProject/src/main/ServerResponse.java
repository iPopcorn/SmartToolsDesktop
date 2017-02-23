package main;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.net.URL;
import java.nio.charset.Charset;


/**
 * Class that will contact the server and handle responses (primarily getting tool reports and getting inventory checks).
 */

public class ServerResponse {

    private String serverIP;
    private String response;

    /*
     * Basic constructor that will initialize the base serverIP and blank response.
     */
    public ServerResponse() {
        this.serverIP = "http://52.32.197.221/"; // server IP that will be called to receive responses
        this.response = ""; // response string
    }

    /*
     * getResponse(String serverPath)
     * This function will return the response from the passed path parameter on the server.
     * An example path parameter input would be "tools/get-tools.php".
     */
    public String getResponse(String serverPath) {
        String myURL = serverIP + serverPath;
        StringBuilder sb = new StringBuilder();
        System.out.println("Requested URL: " + myURL);
        URLConnection urlConn = null;
        InputStreamReader in = null;

        try {
            URL url = new URL(myURL);
            urlConn = url.openConnection();

            if (urlConn != null) // if the url connection is not null (IP is valid)
                urlConn.setReadTimeout(60 * 1000);
            if (urlConn != null && urlConn.getInputStream() != null) {
                in = new InputStreamReader(urlConn.getInputStream(),
                        Charset.defaultCharset());
                BufferedReader bufferedReader = new BufferedReader(in);
                if (bufferedReader != null) {
                    int cp;
                    while ((cp = bufferedReader.read()) != -1) {
                        sb.append((char) cp);
                    }
                    bufferedReader.close();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        response = sb.toString();
        return response;
    }


}
