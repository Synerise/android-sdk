package com.synerise.sdk.sample.ui;

import android.support.v4.app.Fragment;

import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.sample.R;

import static com.synerise.sdk.error.HttpErrorCategory.UNAUTHORIZED;

public class BaseFragment extends Fragment {

    protected interface Args {
        String CONTENT = "content";
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
}
