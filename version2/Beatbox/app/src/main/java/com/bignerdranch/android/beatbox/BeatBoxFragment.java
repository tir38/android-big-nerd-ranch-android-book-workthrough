package com.bignerdranch.android.beatbox;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class BeatBoxFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beat_box, container,false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fragment_beat_box_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setAdapter(new SoundAdapter());
        return view;
    }

    private class SoundAdapter extends RecyclerView.Adapter<SoundViewHolder> {

        @Override
        public SoundViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new SoundViewHolder(inflater, viewGroup);
        }

        @Override
        public void onBindViewHolder(SoundViewHolder soundViewHolder, int i) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }


    private class SoundViewHolder extends RecyclerView.ViewHolder{

        private final Button mButton;

        public SoundViewHolder(LayoutInflater inflater, ViewGroup container) {
            super(inflater.inflate(R.layout.list_item_sound, container, false));
            mButton = (Button) itemView.findViewById(R.id.list_item_sound_button);
        }
    }
}
