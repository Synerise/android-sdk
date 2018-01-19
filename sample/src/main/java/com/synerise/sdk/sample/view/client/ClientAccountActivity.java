package com.synerise.sdk.sample.view.client;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.synerise.sdk.client.Client;
import com.synerise.sdk.client.model.AccountInformation;
import com.synerise.sdk.core.listeners.ActionListener;
import com.synerise.sdk.core.listeners.DataActionListener;
import com.synerise.sdk.core.model.Sex;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.core.net.IDataApiCall;
import com.synerise.sdk.core.utils.Lh;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.sample.R;


public class ClientAccountActivity extends AppCompatActivity {

    public static final String TAG = ClientAccountActivity.class.getSimpleName();

    private IDataApiCall<AccountInformation> accountInfoCall;
    private IApiCall updateAccountCall;
    private IDataApiCall<String> getTokenCall;

    public static Intent createIntent(Context context) {
        return new Intent(context, ClientAccountActivity.class);
    }

    // ****************************************************************************************************************************************

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_account);

        Button getAccountButton = findViewById(R.id.client_get_account);
        getAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {ClientAccountActivity.this.getAccount();}
        });

        Button updateAccountButton = findViewById(R.id.client_update_account);
        updateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {ClientAccountActivity.this.updateAccount();}
        });
        Button getTokenButton = findViewById(R.id.client_get_token);
        getTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClientAccountActivity.this.getToken();
            }
        });
    }

    // ****************************************************************************************************************************************

    private void getAccount() {
        if (accountInfoCall != null) accountInfoCall.cancel();
        accountInfoCall = Client.getAccount();
        accountInfoCall.execute(new DataActionListener<AccountInformation>() {
            @Override
            public void onDataAction(AccountInformation accountInformation) {
                ClientAccountActivity.this.onGetAccountSuccess(accountInformation);
            }
        }, new DataActionListener<ApiError>() {
            @Override
            public void onDataAction(ApiError apiError) {ClientAccountActivity.this.onGetAccountFailure(apiError);}
        });
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

        if (updateAccountCall != null) updateAccountCall.cancel();
        updateAccountCall = Client.updateAccount(accountInformation);
        updateAccountCall.execute(new ActionListener() {
            @Override
            public void onAction() {ClientAccountActivity.this.onUpdateAccountSuccess();}
        }, new DataActionListener<ApiError>() {
            @Override
            public void onDataAction(ApiError apiError) {ClientAccountActivity.this.onUpdateAccountFailure(apiError);}
        });
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
    public void getToken() {
        if (getTokenCall != null) getTokenCall.cancel();
        getTokenCall = Client.getToken();
        getTokenCall.execute(new DataActionListener<String>() {
            @Override
            public void onDataAction(String token) {
                onGetTokenSuccess(token);
            }
        }, new DataActionListener<ApiError>() {
            @Override
            public void onDataAction(ApiError apiError) {
                onGetTokenFailure(apiError);
            }
        });
    }

    private void onGetTokenSuccess(String token) {
        Toast.makeText(this, R.string.message_success + " " + token, Toast.LENGTH_LONG).show();
    }

    private void onGetTokenFailure(ApiError apiError) {
        Log.d(getClass().getSimpleName(), "apiError= " + apiError.getErrorBody());
        String errorCategory = apiError.getHttpErrorCategory().toString();
        Toast.makeText(this, getString(R.string.message_failure) + " " + errorCategory, Toast.LENGTH_LONG).show();
    }
    // ****************************************************************************************************************************************

    @Override
    protected void onStop() {
        super.onStop();
        if (updateAccountCall != null) updateAccountCall.cancel();
        if (accountInfoCall != null) accountInfoCall.cancel();
        if (getTokenCall != null) getTokenCall.cancel();
    }
}
