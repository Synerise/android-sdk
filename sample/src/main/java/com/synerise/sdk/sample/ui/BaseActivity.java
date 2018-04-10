package com.synerise.sdk.sample.ui;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.sample.R;

import static com.synerise.sdk.error.HttpErrorCategory.UNAUTHORIZED;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    protected interface Args {
        String PARCEL = "parcel";
    }

    // ****************************************************************************************************************************************

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

    // ****************************************************************************************************************************************

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
