package com.bignerdranch.android.photogallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ThumbnailDownloader<Token> extends HandlerThread {

    private static final String TAG = "ThumbnailDownloader";
    private static final int MESSAGE_DOWNLOAD = 0;

    private Handler mHandler;
    private Map<Token, String> requestMap = Collections.synchronizedMap(new HashMap<Token, String>());

    private Handler mResponseHandler;
    private Listener<Token> mListener;

    private LruCache<String, Bitmap> mBitmapLruCache;

    public interface Listener<Token> {
        void onThumbnailDownloaded(Token token, Bitmap thumbnail);
    }

    public ThumbnailDownloader(Handler responseHandler) {
        super(TAG);
        mResponseHandler = responseHandler;

        int maxCacheSize = 50;
        mBitmapLruCache = new LruCache<String, Bitmap>(maxCacheSize);
    }

    @Override
    protected void onLooperPrepared() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                if (message.what == MESSAGE_DOWNLOAD) {
                    @SuppressWarnings("unchecked")
                    Token token = (Token) message.obj;

                    if (requestMap.get(token) == null) {
                        Log.e(TAG, "url is null for token: " + token.toString());
                    }
                    Log.i(TAG, "Got a request for URL: " + requestMap.get(token));
                    handleRequest(token);
                }
            }
        };
    }

    public void setListener(Listener<Token> listener) {
        mListener = listener;
    }

    public void queueThumbnail(Token token, String url) {
        Log.i(TAG, "Got a URL: " + url);
        requestMap.put(token, url);
        mHandler.obtainMessage(MESSAGE_DOWNLOAD, token).sendToTarget();
    }

    public void clearQueue() {
        mHandler.removeMessages(MESSAGE_DOWNLOAD);
        requestMap.clear();
    }

    private void handleRequest(final Token token) {
        try {
            final String url = requestMap.get(token);
            if (url == null) {
                return;
            }

            final Bitmap bitmap;

            // check cache
            if (mBitmapLruCache.get(url) != null) {
                bitmap = mBitmapLruCache.get(url);
                Log.d(TAG, "pulling from cache: " + url);
            } else { // download and add to cache
                Log.d(TAG, "downloading instead: " + url);
                byte[] bitmapBytes = new FlickrFetchr().getUrlBytes(url);
                bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
                Log.i(TAG, "bitmap created");
                mBitmapLruCache.put(url, bitmap);
            }

            mResponseHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (requestMap.get(token) != null && !(requestMap.get(token).equals(url))) {
                        return;
                    }

                    requestMap.remove(token);
                    mListener.onThumbnailDownloaded(token, bitmap);
                }
            });

        } catch (IOException exception) {
            Log.e(TAG, "Error downloading image: " + exception);
        }
    }
}
