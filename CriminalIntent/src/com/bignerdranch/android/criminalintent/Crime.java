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

    private UUID mID;
	private String mTitle;
	private Date mDate; // date crime occurred
	private boolean mSolved;
    private Photo mPhoto;

	public Crime(){ // constructor
		mID = UUID.randomUUID(); // generate random ID
		mDate = new Date(); // defaults to current date
	}

	public Crime(JSONObject jsonObject) throws JSONException{ // overloaded constructor that accepts JSON object
		mTitle = jsonObject.getString(JSON_TITLE);
		mSolved = jsonObject.getBoolean(JSON_SOLVED);
		mDate = new Date(jsonObject.getLong(JSON_DATE));
		mID = UUID.fromString(jsonObject.getString(JSON_ID));
        mPhoto = new Photo((jsonObject.getJSONObject(JSON_PHOTO)));
		
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

    @Override
	public String toString(){
		return mTitle;
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject jsonObject = new JSONObject(); // create new JSONOject object
		
		// add key,value pairs
		jsonObject.put(JSON_ID, mID.toString());
		jsonObject.put(JSON_DATE, mDate.getTime()); // don't format date string any fancy way; otherwise I'll have to "unfancy" it when I bring it back from JSON file
		jsonObject.put(JSON_SOLVED, mSolved);
		jsonObject.put(JSON_TITLE, mTitle);
		
		return jsonObject;
	}
}
