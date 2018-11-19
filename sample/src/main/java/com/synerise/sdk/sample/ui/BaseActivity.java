package com.synerise.sdk.sample.ui;

import android.annotation.SuppressLint;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.error.ApiErrorBody;
import com.synerise.sdk.error.ApiErrorCause;
import com.synerise.sdk.sample.R;

import java.util.List;

import static com.synerise.sdk.error.HttpErrorCategory.UNAUTHORIZED;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    protected interface Args {
        String PARCEL = "parcel";
        String SERIALIZABLE = "serializable";
    }

    protected void showAlertError(ApiError apiError) {
        if (apiError != null) {
            ApiErrorBody errorBody = apiError.getErrorBody();
            int httpCode = apiError.getHttpCode();
            AlertDialog.Builder dialog = new AlertDialog.Builder(this).setIcon(R.drawable.sygnet_synerise);
            if (httpCode != ApiError.UNKNOWN_CODE) {
                dialog.setTitle(String.valueOf(httpCode));
            } else {
                dialog.setTitle(R.string.default_error);
            }
            if (errorBody != null) {
                List<ApiErrorCause> errorCauses = errorBody.getErrorCauses();
                StringBuilder message = new StringBuilder(errorBody.getMessage());
                if (!errorCauses.isEmpty()) {
                    for (ApiErrorCause errorCause : errorCauses) {
                        message.append("\n").append(errorCause.getCode()).append(": ").append(errorCause.getMessage());
                    }
                }
                dialog.setMessage(message.toString());
            } else {
                switch (apiError.getErrorType()) {
                    case HTTP_ERROR:
                        if (apiError.getHttpErrorCategory() == UNAUTHORIZED) {
                            dialog.setMessage(getString(R.string.error_unauthorized));
                        } else {
                            dialog.setMessage(getString(R.string.error_http));
                        }
                        break;
                    case NETWORK_ERROR:
                        dialog.setMessage(getString(R.string.error_network));
                        break;
                    case NO_TOKEN:
                        dialog.setMessage(getString(R.string.no_token));
                        break;
                    default:
                        dialog.setMessage(getString(R.string.error_default));
                }
            }
            dialog.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
