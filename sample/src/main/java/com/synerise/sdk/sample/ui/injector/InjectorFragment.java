package com.synerise.sdk.sample.ui.injector;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.BaseFragment;
import com.synerise.sdk.sample.util.FirebaseIdChangeBroadcastReceiver;
import com.synerise.sdk.sample.util.SystemUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class InjectorFragment extends BaseFragment {

    @BindView(R.id.firebase_id) TextView firebaseId;
    @BindView(R.id.click_copy) TextView clickCopy;
    private Unbinder unbinder;

    private FirebaseIdChangeBroadcastReceiver broadcastReceiver;

    public static InjectorFragment newInstance() { return new InjectorFragment(); }

    // ****************************************************************************************************************************************

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_injector, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        firebaseId.setOnClickListener(v -> copyFirebaseIdTv());
        clickCopy.setOnClickListener(v -> copyFirebaseIdTv());

        broadcastReceiver = new FirebaseIdChangeBroadcastReceiver();
        broadcastReceiver.setListener(this::onFirebaseIdChanged);
    }

    @Override
    public void onStart() {
        super.onStart();
        onFirebaseIdChanged();
        IntentFilter filter = new IntentFilter(FirebaseIdChangeBroadcastReceiver.ACTION_FIREBASE_ID_CHANGE);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    // ****************************************************************************************************************************************

    private void copyFirebaseIdTv() {
        SystemUtils.copyTextToClipboard(getContext(), firebaseId.getText());
    }

    private void onFirebaseIdChanged() {

        // get your `google-services.json` from Firebase console first
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        if (TextUtils.isEmpty(refreshedToken)) {
            firebaseId.setText(R.string.unknown_firebase_id);
        } else {
            firebaseId.setText(refreshedToken);
        }
    }
}
