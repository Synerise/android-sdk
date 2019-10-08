package com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.synerise.sdk.client.Client;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.dev.BaseDevFragment;

public class ClientRegenerateUuidFragment extends BaseDevFragment {

    private TextView currentUuid;

    public static ClientRegenerateUuidFragment newInstance() {
        return new ClientRegenerateUuidFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_client_regenerate_uuid, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentUuid = view.findViewById(R.id.current_uuid);
        currentUuid.setText(Client.getUuid());
        view.findViewById(R.id.regenerate_uuid).setOnClickListener(v -> regenerateUuid());
    }

    @SuppressWarnings("ConstantConditions")
    private void regenerateUuid() {
        boolean success = Client.regenerateUuid();
        if (success) {
            onSuccess();
            currentUuid.setText(Client.getUuid());
        } else {
            Snackbar.make(currentUuid, R.string.regenerate_uuid_error, Snackbar.LENGTH_SHORT).show();
        }
    }
}
