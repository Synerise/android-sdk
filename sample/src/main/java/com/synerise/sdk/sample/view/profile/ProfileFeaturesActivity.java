package com.synerise.sdk.sample.view.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.synerise.sdk.core.listeners.ActionListener;
import com.synerise.sdk.core.listeners.DataActionListener;
import com.synerise.sdk.core.model.Attributes;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.core.net.IDataApiCall;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.profile.Profile;
import com.synerise.sdk.profile.model.CreateClient;
import com.synerise.sdk.profile.model.RegisterClient;
import com.synerise.sdk.profile.model.UpdateClient;
import com.synerise.sdk.sample.R;

import java.util.Date;

/**
 * Created by Marcel Skotnicki on 11/28/17.
 */

@SuppressWarnings("ConstantConditions")
public class ProfileFeaturesActivity extends AppCompatActivity {

    public static final String TAG = ProfileFeaturesActivity.class.getSimpleName();

    private IApiCall call;
    private IDataApiCall<String> getTokenCall;

    private TextInputLayout clientId, email, password, token;

    public static Intent createIntent(Context context) {
        return new Intent(context, ProfileFeaturesActivity.class);
    }

    // ****************************************************************************************************************************************

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_features);

        clientId = findViewById(R.id.profile_client_id);

        Button createClient = findViewById(R.id.profile_client_create);
        createClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {ProfileFeaturesActivity.this.createClient();}
        });

        Button registerClient = findViewById(R.id.profile_client_register);
        registerClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {ProfileFeaturesActivity.this.registerClient();}
        });

        Button updateClient = findViewById(R.id.profile_client_update);
        updateClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {ProfileFeaturesActivity.this.updateClient();}
        });

        Button deleteClient = findViewById(R.id.profile_client_delete);
        deleteClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {ProfileFeaturesActivity.this.deleteClient();}
        });

        email = findViewById(R.id.profile_password_reset_email);

        Button resetPassword = findViewById(R.id.profile_password_reset);
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {ProfileFeaturesActivity.this.resetPassword();}
        });

        password = findViewById(R.id.profile_password_confirm_pass);

        token = findViewById(R.id.profile_password_confirm_token);

        Button confirmResetPassword = findViewById(R.id.profile_password_confirm);
        confirmResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {ProfileFeaturesActivity.this.confirmResetPassword();}
        });

        Button getTokenButton = findViewById(R.id.get_profile_token);
        getTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProfileToken();
            }
        });
    }

    // ****************************************************************************************************************************************

    private void createClient() {

        Attributes attributes = new Attributes()
                .add("some_string", "Synerise");

        CreateClient createClient = new CreateClient();
        createClient.setCity("Paris").setPhone("123456789").setDisplayName("Joe Doe").setAttributes(attributes);

        if (call != null) call.cancel();
        call = Profile.createClient(createClient);
        call.execute(new ActionListener() {
            @Override
            public void onAction() {ProfileFeaturesActivity.this.onCreateClientSuccess();}
        }, new DataActionListener<ApiError>() {
            @Override
            public void onDataAction(ApiError apiError) {ProfileFeaturesActivity.this.onCreateClientFailure(apiError);}
        });
    }

    private void onCreateClientSuccess() {
        Toast.makeText(this, R.string.message_create_client_success, Toast.LENGTH_LONG).show();
    }

    private void onCreateClientFailure(ApiError apiError) {
        String errorCategory = apiError.getHttpErrorCategory().toString();
        Toast.makeText(this, getString(R.string.message_create_client_failure) + " " + errorCategory, Toast.LENGTH_LONG).show();
        Log.w(TAG, "onCreateClientFailure " + apiError.getErrorBody());
    }

    // ****************************************************************************************************************************************

    private void registerClient() {
        RegisterClient registerClient = new RegisterClient("some@email.com", "some_password");
        registerClient.setCity("London").setCompany("123456789").setFirstName("Tom");

        if (call != null) call.cancel();
        call = Profile.registerClient(registerClient);
        call.execute(new ActionListener() {
            @Override
            public void onAction() {ProfileFeaturesActivity.this.onRegisterClientSuccess();}
        }, new DataActionListener<ApiError>() {
            @Override
            public void onDataAction(ApiError apiError) {ProfileFeaturesActivity.this.onRegisterClientFailure(apiError);}
        });
    }

    private void onRegisterClientSuccess() {
        Toast.makeText(this, R.string.message_register_client_success, Toast.LENGTH_LONG).show();
    }

    private void onRegisterClientFailure(ApiError apiError) {
        String errorCategory = apiError.getHttpErrorCategory().toString();
        Toast.makeText(this, getString(R.string.message_register_client_failure) + " " + errorCategory, Toast.LENGTH_LONG).show();
        Log.w(TAG, "onRegisterClientFailure " + apiError.getErrorBody());
    }

    // ****************************************************************************************************************************************

    private void updateClient() {
        if (isValid(clientId)) {

            UpdateClient updateClient = new UpdateClient();
            updateClient.setCity("Warsaw").setPhone("987654321").setDisplayName("Bruce Lee");

            if (call != null) call.cancel();
            call = Profile.updateClient(getClientId(), updateClient);
            call.execute(new ActionListener() {
                @Override
                public void onAction() {ProfileFeaturesActivity.this.onUpdateClientSuccess();}
            }, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {ProfileFeaturesActivity.this.onUpdateClientFailure(apiError);}
            });
        }
    }

    private void onUpdateClientSuccess() {
        Toast.makeText(this, R.string.message_update_client_success, Toast.LENGTH_LONG).show();
    }

    private void onUpdateClientFailure(ApiError apiError) {
        String errorCategory = apiError.getHttpErrorCategory().toString();
        Toast.makeText(this, getString(R.string.message_update_client_failure) + " " + errorCategory, Toast.LENGTH_LONG).show();
        Log.w(TAG, "onUpdateClientFailure " + apiError.getErrorBody());
    }

    // ****************************************************************************************************************************************

    private void deleteClient() {
        if (call != null) call.cancel();
        call = Profile.deleteClient(getClientId());
        call.execute(new ActionListener() {
            @Override
            public void onAction() {ProfileFeaturesActivity.this.onDeleteClientSuccess();}
        }, new DataActionListener<ApiError>() {
            @Override
            public void onDataAction(ApiError apiError) {ProfileFeaturesActivity.this.onDeleteClientFailure(apiError);}
        });
    }

    private void onDeleteClientSuccess() {
        Toast.makeText(this, R.string.message_delete_client_success, Toast.LENGTH_LONG).show();
    }

    private void onDeleteClientFailure(ApiError apiError) {
        String errorCategory = apiError.getHttpErrorCategory().toString();
        Toast.makeText(this, getString(R.string.message_delete_client_failure) + " " + errorCategory, Toast.LENGTH_LONG).show();
        Log.w(TAG, "onDeleteClientFailure " + apiError.getErrorBody());
    }

    // ****************************************************************************************************************************************

    private void resetPassword() {
        if (isValid(email)) {
            if (call != null) call.cancel();
            call = Profile.requestPasswordReset(email.getEditText().getText().toString());
            call.execute(new ActionListener() {
                @Override
                public void onAction() {ProfileFeaturesActivity.this.onResetPasswordSuccess();}
            }, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {ProfileFeaturesActivity.this.onResetPasswordFailure(apiError);}
            });
        }
    }

    private void onResetPasswordSuccess() {
        Toast.makeText(this, R.string.message_reset_password_success, Toast.LENGTH_LONG).show();
    }

    private void onResetPasswordFailure(ApiError apiError) {
        String errorCategory = apiError.getHttpErrorCategory().toString();
        Toast.makeText(this, getString(R.string.message_reset_password_failure) + " " + errorCategory, Toast.LENGTH_LONG).show();
        Log.w(TAG, "onResetPasswordFailure " + apiError.getErrorBody());
    }

    // ****************************************************************************************************************************************

    private void confirmResetPassword() {
        if (isValid(password) && isValid(token)) {
            if (call != null) call.cancel();
            call = Profile.confirmResetPassword(password.getEditText().getText().toString(), token.getEditText().getText().toString());
            call.execute(new ActionListener() {
                @Override
                public void onAction() {ProfileFeaturesActivity.this.onConfirmResetPasswordSuccess();}
            }, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {ProfileFeaturesActivity.this.onConfirmResetPasswordFailure(apiError);}
            });
        }
    }

    private void onConfirmResetPasswordSuccess() {
        Toast.makeText(this, R.string.message_confirm_reset_password_success, Toast.LENGTH_LONG).show();
    }

    private void onConfirmResetPasswordFailure(ApiError apiError) {
        String errorCategory = apiError.getHttpErrorCategory().toString();
        Toast.makeText(this, getString(R.string.message_confirm_reset_password_failure) + " " + errorCategory, Toast.LENGTH_LONG)
             .show();
        Log.w(TAG, "onConfirmResetPasswordFailure " + apiError.getErrorBody());
    }

    // ****************************************************************************************************************************************

    private long getClientId() {
        String text = clientId.getEditText().getText().toString();
        long id = 0;
        try {
            id = Long.parseLong(text);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            clientId.getEditText().setText(String.valueOf(id));
        }
        return id;
    }

    private boolean isValid(TextInputLayout inputLayout) {
        inputLayout.setError(null);
        String text = inputLayout.getEditText().getText().toString();
        if (TextUtils.isEmpty(text)) {
            inputLayout.setError(getString(R.string.error_required));
            return false;
        }
        return true;
    }

    // ****************************************************************************************************************************************

    public void getProfileToken() {
        if (getTokenCall != null) getTokenCall.cancel();
        getTokenCall = Profile.getToken();
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
        String errorCategory = apiError.getHttpErrorCategory().toString();
        Toast.makeText(this, getString(R.string.message_failure) + " " + errorCategory, Toast.LENGTH_LONG).show();
    }

    // ****************************************************************************************************************************************

    @Override
    protected void onStop() {
        super.onStop();
        if (call != null) call.cancel();
        if (getTokenCall != null) getTokenCall.cancel();
    }
}
