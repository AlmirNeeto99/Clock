package Util;

import java.net.Socket;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class MessageHandler {

    public static void handle(Socket s, String msg) {
        JSONParser parser = new JSONParser();
        JSONObject obj = null;
        try {
            obj = (JSONObject) parser.parse(msg);
            switch (obj.get("type").toString()) {
                case "christian":
                    break;
                case "bully":
                    break;
            }
        } catch (Exception ex) {

        }
    }
}