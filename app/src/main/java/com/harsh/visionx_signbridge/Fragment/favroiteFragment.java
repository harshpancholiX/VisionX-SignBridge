package com.harsh.visionx_signbridge.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harsh.visionx_signbridge.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link favroiteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class favroiteFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favroite, container, false);
    }
}