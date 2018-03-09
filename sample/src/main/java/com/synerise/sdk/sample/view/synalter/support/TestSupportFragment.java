package com.synerise.sdk.sample.view.synalter.support;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.synerise.sdk.sample.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestSupportFragment extends Fragment {

    public TestSupportFragment() {
        // required empty public constructor
    }

    // ****************************************************************************************************************************************

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.fragment_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Fragment button clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
