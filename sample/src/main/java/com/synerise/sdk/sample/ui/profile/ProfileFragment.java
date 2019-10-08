package com.synerise.sdk.sample.ui.profile;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.synerise.sdk.sample.App;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.persistence.AccountManager;
import com.synerise.sdk.sample.ui.BaseFragment;

import javax.inject.Inject;

public class ProfileFragment extends BaseFragment {

    @Inject AccountManager accountManager;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getActivity().getApplication()).getComponent().inject(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String email = accountManager.getEmail();
        String firstName = accountManager.getFirstName();
        String lastName = accountManager.getLastName();

        TextView textName = view.findViewById(R.id.text_name);
        textName.setText(firstName);

        TextView textLastName = view.findViewById(R.id.text_last_name);
        textLastName.setText(lastName);

        TextView textEmail = view.findViewById(R.id.text_email);
        textEmail.setText(email);
    }
}