package com.bignerdranch.android.photogallery;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

public class PhotoGalleryFragment extends Fragment {

    private static final String TAG = "PhotoGalleryFragment";
    private GridView mGridView;
    private ArrayList<GalleryItem> mItems;
//    private int mCurrentPageNumber;
    private ThumbnailDownloader<ImageView> mThumbnailThread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
//        mCurrentPageNumber = 1; // 1-indexed paging
//        new FetchItemsTask().execute(mCurrentPageNumber);
        new FetchItemsTask().execute();


        // setup background thread / looper / handler
        mThumbnailThread = new ThumbnailDownloader<ImageView>(new Handler()); // pass a handler for receiving responses from Thumbnail Downloader
        mThumbnailThread.setListener(new ThumbnailDownloader.Listener<ImageView>() {
            @Override
            public void onThumbnailDownloaded(ImageView imageView, Bitmap thumbnail) {
                if (isVisible()) {
                    imageView.setImageBitmap(thumbnail);
                }
            }
        });
        mThumbnailThread.start();
        mThumbnailThread.getLooper();
        Log.i(TAG, "Background thread started");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
        mGridView = (GridView) view.findViewById(R.id.gridView);
        setupAdapter();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mThumbnailThread.quit();
        Log.i(TAG, "Background thread destroyed");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mThumbnailThread.clearQueue();
    }

    private void setupAdapter() {
        if (getActivity() == null || mGridView == null) {
            return;
        }

        if (mItems != null) {
            mGridView.setAdapter(new GalleryItemAdapter(mItems));
        }  else {
            mGridView.setAdapter(null);
        }

//        mGridView.setOnScrollListener(new CustomScrollListener());
    }

    /**
     * private inner class to act as adapter: linking images w/ grid view
     */
    private class GalleryItemAdapter extends ArrayAdapter<GalleryItem> {
        public GalleryItemAdapter(ArrayList<GalleryItem> items) {
            super(getActivity(), 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity() .getLayoutInflater().inflate(R.layout.gallery_item, parent, false);
            }

            ImageView imageView = (ImageView) convertView.findViewById(R.id.gallery_item_image_view);
            imageView.setImageResource(R.drawable.brian_up_close); // set default image

            // load image from web
            GalleryItem item = getItem(position);
            mThumbnailThread.queueThumbnail(imageView, item.getUrl());

            return convertView;
        }
    }

//    /**
//     * private inner class to listen for scroll events
//     */
//    private class CustomScrollListener implements AbsListView.OnScrollListener {
//
//        @Override
//        public void onScrollStateChanged(AbsListView view, int scrollState) {
//            // do nothing
//        }
//
//        @Override
//        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//            if (totalItemCount == 0) {
//                // adapter is still loading
//                return;
//            }
//
//            // if at end of list
//            int lastItemCount = firstVisibleItem + visibleItemCount;
//
//            if (lastItemCount >= totalItemCount) {
//                Log.d(TAG, "at the end of the list");
//                mThumbnailThread.clearQueue();
//                mCurrentPageNumber++;
//                new FetchItemsTask().execute(mCurrentPageNumber); // API 13+ this will default to serial
//            }
//        }
//    }

    /**
     * private inner class to handle async task
     */
    private class FetchItemsTask extends AsyncTask<Integer, Void, ArrayList<GalleryItem> > {

        @Override
        protected ArrayList<GalleryItem> doInBackground(Integer... params) {

            String query = "android"; // TODO
            if (query != null ) {
                return new FlickrFetchr().search(query);
            } else {
                return new FlickrFetchr().getItems();
            }
//            Integer pageNumber = params[0];
//            return new FlickrFetchr().getPage(pageNumber);
        }

        @Override
        protected void onPostExecute(ArrayList<GalleryItem> items) {
            mItems = items;
            setupAdapter();
        }
    }
}
