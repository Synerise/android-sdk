package com.synerise.sdk.sample.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.sample.R;

import static com.synerise.sdk.error.HttpErrorCategory.UNAUTHORIZED;

public abstract class BaseDialog extends DialogFragment {

    protected interface Args {
        String CONTENT = "args";
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(setLayout(), null);
        setViewContent(context, view);
        Dialog dialog = new AlertDialog.Builder(context, R.style.AppAlertDialogTheme)
                .setView(view)
                .setCancelable(setCancelable())
                .create();
        dialog.setCanceledOnTouchOutside(setCancelable());
        super.setCancelable(setCancelable());
        return dialog;
    }

    @LayoutRes
    protected abstract int setLayout();

    protected abstract boolean setCancelable();

    protected abstract void setViewContent(Context context, View view);

    protected String getErrorMessage(ApiError apiError) {
        switch (apiError.getErrorType()) {
            case HTTP_ERROR:
                if (apiError.getHttpErrorCategory() == UNAUTHORIZED) {
                    return getString(R.string.error_unauthorized);
                } else {
                    return getString(R.string.error_http);
                }
            case NETWORK_ERROR:
                return getString(R.string.error_network);
            default:
                return getString(R.string.error_default);
        }
    }
}