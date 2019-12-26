package com.example.sejongbooks.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.sejongbooks.R;


public class MountMapFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mount_map, container, false);

        initView(view);

        return view;
    }

    private void initView(View view){

    }

    @Override
    public void onResume() {

        super.onResume();
    }
}

