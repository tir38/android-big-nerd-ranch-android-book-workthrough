package com.bignerdranch.android.criminalintent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.content.Context;
import android.util.Log;

public class CriminalIntentJSONSerializer {
	private Context mContext;
	private String mFilename;
	private static final String TAG = "CriminalIntent";	
	
	public CriminalIntentJSONSerializer(Context c, String f){
		mContext = c;
		mFilename = f;	
	}
	
	// save crimes to file
	public void saveCrimes(ArrayList<Crime> crimes)
			throws JSONException, IOException{
		// Build an array in JSON
		JSONArray array = new JSONArray(); // create new array
		for (Crime c:crimes){				// for each crime
				array.put(c.toJSON());			// convert to JSON and put in array
		}
		// write the file to disk
		Writer writer = null;				// create new Writer
		try{								// try to save JSONarray to file
			OutputStream out = mContext	
								.openFileOutput(mFilename, Context.MODE_PRIVATE);
			writer = new  OutputStreamWriter(out);
			writer.write(array.toString());
		}
		finally{
			if(writer != null)
				writer.close();
		}
	}

	// load crimes from file
	public ArrayList<Crime> loadCrimes() throws IOException, JSONException{
		ArrayList<Crime> crimes = new ArrayList<Crime>(); // create new empty array list
		BufferedReader reader = null;						// create file reader
		try{
			// open and read the file into a StringBuilder
			InputStream inputStream = mContext.openFileInput(mFilename);  		// create input stream
			reader = new BufferedReader(new InputStreamReader(inputStream)); 	// set reader's input stream
			StringBuilder jsonStringBuilder = new StringBuilder(); 				// create new string builder
			String singleLine = null;
			
			while ((singleLine = reader.readLine()) != null){ // a lot going on in this line: reads a line from reader into singleLine, if this doesn't read in null then keep going
				// line breaks are ommited and irrelevant
				jsonStringBuilder.append(singleLine); // add single line to entire string builder
			}
			
			// parse string builder into json array
			JSONArray jsonArray = (JSONArray) new JSONTokener(jsonStringBuilder.toString()) // typecast string builder to json array
									.nextValue();
			
			// build array list from json array
			for (int i = 0; i < jsonArray.length(); i++){ // for each item in json array...
				crimes.add(new Crime(jsonArray.getJSONObject(i))); // ... create new Crime object from JSON object, and add to arraylist
			}
		}	
		
		catch (FileNotFoundException e){
			// Ignore this one; it happens when starting fresh
		}

		finally{	 // close reader
			if(reader != null){
				reader.close();
			}
		}
		
		
		return crimes;
		
	}
}
	
