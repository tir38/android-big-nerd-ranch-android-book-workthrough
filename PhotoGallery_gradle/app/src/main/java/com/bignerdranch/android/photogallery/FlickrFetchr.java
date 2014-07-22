package com.bignerdranch.android.photogallery;

import android.net.Uri;
import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FlickrFetchr {

    private static final String TAG = "FlickrFetchr";

    // URL builder string constants
    private static final String ENDPOINT = "http://api.flickr.com/services/rest";
    private static final String API_KEY = "4f721bbafa75bf6d2cb5af54f937bb70";
    private static final String METHOD_GET_RECENT = "flickr.photos.getRecent";
    private static final String PARAMS_EXTRAS = "extras"; // not an intent extra
    private static final String EXTRA_SMALL_URL = "url_s"; // not an intent extra

    private static final String XML_PHOTO = "photo"; // xml tag that holds a photo


    /**
     * Do actual HTTP request and get bytes
     * @param urlSpec
     * @return
     * @throws IOException
     */
    public byte[] getUrlBytes(String urlSpec) throws IOException{

        // open connection with new url
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            InputStream inputStream = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = inputStream.read(buffer)) > 0) { // while there are bytes to read, keep reading
                outputStream.write(buffer, 0, bytesRead);
            }

            // close and cleanup
            outputStream.close();
            return outputStream.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    /**
     * Helper method to call getUrlBytes and save output as string
     * @param urlSpec url string
     * @return response as string
     * @throws IOException
     */
    public String getUrl(String urlSpec) throws IOException {
        return new  String(getUrlBytes(urlSpec));
    }


    public ArrayList<GalleryItem> fetchItems() {

        ArrayList<GalleryItem> items = new ArrayList<GalleryItem>();

        try {
            // build URL string
            String url = Uri.parse(ENDPOINT)
                    .buildUpon()
                    .appendQueryParameter("method", METHOD_GET_RECENT)
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter(PARAMS_EXTRAS, EXTRA_SMALL_URL)
                    .build()
                    .toString();

            // get XML from URL
            String xmlString = getUrl(url);

            // create parser
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xmlString));

            // do parsing
            parseItems(items, parser);

        } catch (IOException e) {
            Log.e(TAG, "Failed to fetch items", e);
        } catch (XmlPullParserException e) {
            Log.e(TAG, "Failed to parse items", e);
        }

        return items;
    }

    private void parseItems(ArrayList<GalleryItem> items, XmlPullParser parser) throws IOException, XmlPullParserException {
        int eventType = parser.next();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG &&
                    XML_PHOTO.equals(parser.getName())) {

                // get local variables from parser
                String id = parser.getAttributeValue(null, "id");
                String caption = parser.getAttributeValue(null, "title");
                String smallUrl = parser.getAttributeValue(null, EXTRA_SMALL_URL);

                // build item
                GalleryItem item = new GalleryItem();
                item.setId(id);
                item.setCaption(caption);
                item.setUrl(smallUrl);
                items.add(item);
            }

            eventType = parser.next();
        }

    }
}
