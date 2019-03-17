package univ.ducoroy.test;

import org.json.JSONException;


public class Tools {
    public static String[] parseInJSON(String theString) throws JSONException {
        return theString.split(" ");
    }
}

