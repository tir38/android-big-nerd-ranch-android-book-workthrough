package com.bignerdranch.android.criminalintent;

import android.annotation.TargetApi;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.view.View.OnClickListener;

import java.io.IOException;
import java.util.List;

public class CrimeCameraFragment extends Fragment {

    // ======== variables ================
    private static final String TAG = "CrimeCameraFragment"; // debugging tag

    private Camera mCamera;
    private SurfaceView mSurfaceView;

    // ======== methods ================
    @Override
    @SuppressWarnings("deprecation")
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup parent,
                             Bundle savedInstanceState){

        // --- inflate view
        View view = inflater.inflate(R.layout.fragment_crime_camera, parent, false);

        // --- handle button
        Button takePictureButton = (Button)view.findViewById(R.id.crime_camera_takePictureButton); // get button
        takePictureButton.setOnClickListener(new OnClickListener(){
            // anonymous inner class

            @Override
            public void onClick(View v) {
                getActivity().finish(); // close current activity  (i.e. CrimeCameraActivity.java)  this will call onPause() and release camera
            }
        });

        // --- handle surface view
        mSurfaceView = (SurfaceView)view.findViewById(R.id.crime_camera_surfaceView);
        SurfaceHolder holder = mSurfaceView.getHolder();                                // this is my connection to Surface object (i.e. raw pixels)
        // setType() and SURFACE_TYPE_PUSH_BUFFERS are both deprecated
        // bur are required for Camera preview to work on pre3.0 devices
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        holder.addCallback(new SurfaceHolder.Callback(){
            // anonymous inner class

            public void surfaceCreated(SurfaceHolder holder){
                // tell the camera to us this surface as its preview area
                try{
                    if (mCamera != null){
                        mCamera.setPreviewDisplay(holder);
                    }
                }
                catch(IOException exception){
                    Log.e(TAG, "Error setting up preview display", exception);
                }
            }

            public void surfaceDestroyed(SurfaceHolder holder){
                // we can no longer display on this surface, so kill preview
                if (mCamera != null){
                    mCamera.stopPreview();

                }
            }

            public void surfaceChanged(SurfaceHolder holder,
                                       int format,
                                       int w,
                                       int h){

                if(mCamera == null) return; // null test

                // resize preview
                Camera.Parameters parameters = mCamera.getParameters(); // get all params
//                Camera.Size size = null;                                // create new empty Size
                Camera.Size size = getBestPreviewSize(parameters.getSupportedPreviewSizes()); // create new best Size
                parameters.setPreviewSize(size.width, size.height);     // set params' size
                mCamera.setParameters(parameters);                      // push back params

                try{
                    mCamera.startPreview();
                }
                catch (Exception exception){
                    Log.e(TAG, "Could not start preview", exception);
                    mCamera.release();
                    mCamera = null;
                }
            }

        });

        return view;
    }

    @Override
    public void onPause(){
        super.onPause(); // call super class method

        // release camera hardware
        if(mCamera != null){
            mCamera.release();
            mCamera = null;
        }
    }

    @TargetApi(9) // needed for open(0)
    @Override
    public void onResume(){
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD){
            mCamera.open(0); // specify which camera
        }
        else {
            mCamera.open(); // don't specify which camera
        }
    }

    public Camera.Size getBestPreviewSize(List<Camera.Size>  sizes){

        // iterate through each item in list
        // find the one with the largest area
        // can't assume that sizes are in order of increasing area

        Camera.Size outputSize = sizes.get(0);
        double currentLargestArea = outputSize.height * outputSize.width;

        for (int i = 0; i < sizes.size(); i++){
            if ((sizes.get(i).width * sizes.get(i).height) > currentLargestArea){
                outputSize = sizes.get(i); // (re)set output
                currentLargestArea = sizes.get(i).width * sizes.get(i).height;
            }
        }

        return outputSize;
    }
}