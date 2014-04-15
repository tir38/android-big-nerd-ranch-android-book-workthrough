package com.bignerdranch.android.criminalintent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

public class Crime {
    // JSON key strings
    private static final String JSON_ID="id";
    private static final String JSON_TITLE="title";
    private static final String JSON_SOLVED="solved";
    private static final String JSON_DATE="date";
    private static final String JSON_PHOTO="photo";
    private static final String JSON_SUSPECT="suspect";

    private UUID mID;
	private String mTitle;
	private Date mDate; // date crime occurred
	private boolean mSolved;
    private Photo mPhoto;
    private String mSuspect;

	public Crime(){ // constructor
		mID = UUID.randomUUID(); // generate random ID
		mDate = new Date(); // defaults to current date
	}

    /**
     * Overloaded constructor that accepts JSON object.
     * We may want to create a crime from json. In this example (Ch 17) we want to read crimes from a text file.
     * @param json
     * @throws JSONException
     */
	public Crime(JSONObject json) throws JSONException{
		mTitle = json.getString(JSON_TITLE);
		mSolved = json.getBoolean(JSON_SOLVED);
		mDate = new Date(json.getLong(JSON_DATE));
		mID = UUID.fromString(json.getString(JSON_ID));
        if (json.has(JSON_PHOTO)) {
            mPhoto = new Photo((json.getJSONObject(JSON_PHOTO)));
        }
        if (json.has(JSON_SUSPECT)) {
            mSuspect = json.getString(JSON_SUSPECT);
        }
	}
	
	// getters and setters
	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public UUID getID() {
		return mID;
	}
	
	public Date getDate() {
		return mDate;
	}

	public void setDate(Date date) {
		mDate = date;
	}

	public boolean isSolved() {
		return mSolved;
	}

	public void setSolved(boolean isSolved) {
		mSolved = isSolved;
	}

    public Photo getPhoto() {
        return mPhoto;
    }

    public void setPhoto(Photo photo) {
        mPhoto = photo;
    }

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

    @Override
	public String toString(){
		return mTitle;
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject(); // create new JSONOject object
		
		// add key,value pairs
		json.put(JSON_ID, mID.toString());
		json.put(JSON_DATE, mDate.getTime()); // don't format date string any fancy way; otherwise I'll have to "unfancy" it when I bring it back from JSON file
		json.put(JSON_SOLVED, mSolved);
		json.put(JSON_TITLE, mTitle);
		if (mPhoto != null) {
            json.put(JSON_PHOTO, mPhoto);
        }
        json.put(JSON_SUSPECT, mSuspect);
		return json;
	}
}
