package com.synerise.sdk.sample.ui.auth;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.synerise.sdk.core.listeners.DataActionListener;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.injector.net.exception.InvalidPhoneNumberException;
import com.synerise.sdk.profile.Profile;
import com.synerise.sdk.sample.App;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.persistence.AccountManager;
import com.synerise.sdk.sample.ui.BaseDialog;

import javax.inject.Inject;

import static android.view.View.VISIBLE;

public class ConfirmNumberDialog extends BaseDialog {

    private EditText confirmationCode;
    private ProgressBar confirmationProgress;

    private String phoneNumber;

    private IApiCall confirmPhoneCall;
    private OnPhoneConfirmedListener phoneNumberConfirmedListener;

    @Inject AccountManager accountManager;

    public static ConfirmNumberDialog newInstance(String phone) {
        Bundle args = new Bundle();
        args.putString(Args.CONTENT, phone);
        ConfirmNumberDialog confirmNumberDialog = new ConfirmNumberDialog();
        confirmNumberDialog.setArguments(args);
        return confirmNumberDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getActivity().getApplication()).getComponent().inject(this);
    }

    @Override
    protected int setLayout() {
        return R.layout.dialog_confirm_phone_number;
    }

    @Override
    protected boolean setCancelable() {
        return false;
    }

    @Override
    protected void setViewContent(Context context, View view) {
        confirmationCode = view.findViewById(R.id.text_confirmation_code);
        confirmationProgress = view.findViewById(R.id.confirmation_code_progress);
        toggleLoading(false);

        phoneNumber = getArguments().getString(Args.CONTENT);
        if (phoneNumber == null || TextUtils.isEmpty(phoneNumber)) {
            dismiss();
        }
        confirmationCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 4) {
                    confirmCode(String.valueOf(s));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void confirmCode(String confirmationCode) {
        try {
            confirmPhoneCall = Profile.confirmPhoneRegistration(phoneNumber, confirmationCode);
        } catch (InvalidPhoneNumberException e) {
            dismiss();
        }
        if (confirmPhoneCall != null) {
            confirmPhoneCall.onSubscribe(() -> toggleLoading(true))
                            .doFinally(() -> toggleLoading(false))
                            .execute(this::onConfirmPhoneNumberSuccess, new DataActionListener<ApiError>() {
                                @Override
                                public void onDataAction(ApiError apiError) {
                                    onConfirmPhoneNumberError(apiError);
                                }
                            });
        }
    }

    private void onConfirmPhoneNumberSuccess() {
        phoneNumberConfirmedListener.onPhoneNumberConfirmed();
        dismiss();
    }

    private void onConfirmPhoneNumberError(ApiError apiError) {
        Snackbar.make(confirmationCode, getErrorMessage(apiError), Snackbar.LENGTH_SHORT).show();
        confirmationCode.setText("");
    }

    private void toggleLoading(boolean isLoading) {
        if (isLoading) {
            confirmationProgress.setVisibility(VISIBLE);
            confirmationCode.setVisibility(View.INVISIBLE);
        } else {
            confirmationCode.setVisibility(VISIBLE);
            confirmationProgress.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (confirmPhoneCall != null) confirmPhoneCall.cancel();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        phoneNumberConfirmedListener = (OnPhoneConfirmedListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        phoneNumberConfirmedListener = OnPhoneConfirmedListener.NULL;
    }
}
