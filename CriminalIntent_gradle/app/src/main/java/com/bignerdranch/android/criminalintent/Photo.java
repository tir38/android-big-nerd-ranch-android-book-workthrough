package com.bignerdranch.android.criminalintent;

import org.json.JSONException;
import org.json.JSONObject;

public class Photo {

    private static final String JSON_FILENAME = "filename";

    private String mFilename;

    /**
     * constructor from filename string
     * @param filename
     */
    public Photo(String filename) {
        mFilename = filename;
    }

    /**
     * constructor from JSONObject
     * @param json
     * @throws JSONException
     */
    public Photo (JSONObject json) throws JSONException {
        mFilename = json.getString(JSON_FILENAME);
    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_FILENAME, mFilename);
        return json;
    }

    public String getFilename() {
        return mFilename;
    }
}
