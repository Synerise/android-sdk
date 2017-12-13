package com.synerise.sdk.sample.view.client;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.synerise.sdk.client.Client;
import com.synerise.sdk.client.model.AccountInformation;
import com.synerise.sdk.core.model.Sex;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.core.net.IDataApiCall;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.sample.R;

/**
 * Created by Marcel Skotnicki on 11/28/17.
 */

public class ClientAccountActivity extends AppCompatActivity {

    public static final String TAG = ClientAccountActivity.class.getSimpleName();

    private IDataApiCall<AccountInformation> dataCall;
    private IApiCall call;

    public static Intent createIntent(Context context) {
        return new Intent(context, ClientAccountActivity.class);
    }

    // ****************************************************************************************************************************************

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_account);

        Button getAccountButton = findViewById(R.id.client_get_account);
        getAccountButton.setOnClickListener(v -> getAccount());

        Button updateAccountButton = findViewById(R.id.client_update_account);
        updateAccountButton.setOnClickListener(v -> updateAccount());
    }

    // ****************************************************************************************************************************************

    private void getAccount() {
        if (dataCall != null) dataCall.cancel();
        dataCall = Client.getAccount();
        dataCall.execute(this::onGetAccountSuccess, this::onGetAccountFailure);
    }

    private void onGetAccountSuccess(AccountInformation accountInformation) {
        Toast.makeText(this, R.string.message_success, Toast.LENGTH_LONG).show();
    }

    private void onGetAccountFailure(ApiError apiError) {
        String errorCategory = apiError.getHttpErrorCategory().toString();
        Toast.makeText(this, getString(R.string.message_failure) + " " + errorCategory, Toast.LENGTH_LONG).show();
        Log.w(TAG, "onGetAccountFailure " + apiError.getErrorBody());
    }

    // ****************************************************************************************************************************************

    private void updateAccount() {
        AccountInformation accountInformation = new AccountInformation();
        accountInformation.setCity("Warsaw").setSex(Sex.MALE).setCompany("Synerise");

        if (call != null) call.cancel();
        call = Client.updateAccount(accountInformation);
        call.execute(this::onUpdateAccountSuccess, this::onUpdateAccountFailure);
    }

    private void onUpdateAccountSuccess() {
        Toast.makeText(this, R.string.message_success, Toast.LENGTH_LONG).show();
    }

    private void onUpdateAccountFailure(ApiError apiError) {
        String errorCategory = apiError.getHttpErrorCategory().toString();
        Toast.makeText(this, getString(R.string.message_failure) + " " + errorCategory, Toast.LENGTH_LONG).show();
        Log.w(TAG, "onUpdateAccountFailure " + apiError.getErrorBody());
    }

    // ****************************************************************************************************************************************

    @Override
    protected void onStop() {
        super.onStop();
        if (call != null) call.cancel();
        if (dataCall != null) dataCall.cancel();
    }
}
