package com.synerise.sdk.sample.view.client;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.synerise.sdk.client.Client;
import com.synerise.sdk.client.model.AccountInformation;
import com.synerise.sdk.core.listeners.ActionListener;
import com.synerise.sdk.core.listeners.DataActionListener;
import com.synerise.sdk.core.model.Sex;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.core.net.IDataApiCall;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.sample.R;

public class ClientAccountActivity extends AppCompatActivity {

    public static final String TAG = ClientAccountActivity.class.getSimpleName();

    private IApiCall updateAccountCall;
    private IDataApiCall<String> getTokenCall;
    private IDataApiCall<AccountInformation> accountInfoCall;
    private View viewForSnackBar;

    public static Intent createIntent(Context context) {
        return new Intent(context, ClientAccountActivity.class);
    }

    // ****************************************************************************************************************************************

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_account);

        viewForSnackBar = findViewById(R.id.activity_client_account_view);
        findViewById(R.id.client_get_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {getAccount();}
        });
        findViewById(R.id.client_update_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {updateAccount();}
        });
        findViewById(R.id.client_get_token).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getToken();
            }
        });
    }

    // ****************************************************************************************************************************************

    @Override
    protected void onStop() {
        super.onStop();
        if (updateAccountCall != null) updateAccountCall.cancel();
        if (accountInfoCall != null) accountInfoCall.cancel();
        if (getTokenCall != null) getTokenCall.cancel();
    }

    private void getAccount() {
        if (accountInfoCall != null) accountInfoCall.cancel();
        accountInfoCall = Client.getAccount();
        accountInfoCall.execute(new DataActionListener<AccountInformation>() {
                                    @Override
                                    public void onDataAction(AccountInformation accountInformation) {onSuccess(accountInformation);}
                                },
                                new DataActionListener<ApiError>() {
                                    @Override
                                    public void onDataAction(ApiError apiError) {onFailure(apiError);}
                                });
    }

    private void updateAccount() {
        AccountInformation accountInformation = new AccountInformation();
        accountInformation.setCity("Warsaw").setSex(Sex.MALE).setCompany("Synerise");

        if (updateAccountCall != null) updateAccountCall.cancel();
        updateAccountCall = Client.updateAccount(accountInformation);
        updateAccountCall.execute(new ActionListener() {
                                      @Override
                                      public void onAction() {onSuccess(null);}
                                  },
                                  new DataActionListener<ApiError>() {
                                      @Override
                                      public void onDataAction(ApiError apiError) {onFailure(apiError);}
                                  });
    }

    // ****************************************************************************************************************************************

    private void getToken() {
        if (getTokenCall != null) getTokenCall.cancel();
        getTokenCall = Client.getToken();
        getTokenCall.execute(new DataActionListener<String>() {
                                 @Override
                                 public void onDataAction(String token) {
                                     onSuccess(token);
                                 }
                             },
                             new DataActionListener<ApiError>() {
                                 @Override
                                 public void onDataAction(ApiError apiError) {
                                     onFailure(apiError);
                                 }
                             });
    }

    private void onSuccess(Object object) {
        Snackbar.make(viewForSnackBar, getString(R.string.message_success), Snackbar.LENGTH_SHORT).show();
        Log.d(TAG, "onGetTokenSuccess: " + object);
    }

    // ****************************************************************************************************************************************

    private void onFailure(ApiError apiError) {
        Log.d(TAG, "onFailure: " + apiError.getErrorBody());
        String errorCategory = apiError.getHttpErrorCategory().toString();
        Snackbar.make(viewForSnackBar, getString(R.string.message_failure) + " " + errorCategory, Snackbar.LENGTH_SHORT).show();
    }
}
