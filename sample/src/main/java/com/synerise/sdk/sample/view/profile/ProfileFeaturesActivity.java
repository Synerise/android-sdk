package com.synerise.sdk.sample.view.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.synerise.sdk.client.model.AccountInformation;
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

import java.util.UUID;

@SuppressWarnings("ConstantConditions")
public class ProfileFeaturesActivity extends AppCompatActivity {

    public static final String TAG = ProfileFeaturesActivity.class.getSimpleName();

    private IApiCall call;
    private IDataApiCall<String> getTokenCall;
    private IDataApiCall<AccountInformation> getClientID;

    private TextInputLayout clientId, clientEmail, email, password, token;

    public static Intent createIntent(Context context) {
        return new Intent(context, ProfileFeaturesActivity.class);
    }

    // ****************************************************************************************************************************************

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_features);

        clientId = findViewById(R.id.profile_client_id);
        clientEmail = findViewById(R.id.profile_client_email);
        email = findViewById(R.id.profile_password_reset_email);
        password = findViewById(R.id.profile_password_confirm_pass);
        token = findViewById(R.id.profile_password_confirm_token);

        findViewById(R.id.profile_button_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIdFromEmail();
            }
        });
        findViewById(R.id.profile_client_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {createClient();}
        });
        findViewById(R.id.profile_client_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {registerClient();}
        });
        findViewById(R.id.profile_client_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {updateClient();}
        });
        findViewById(R.id.profile_client_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {deleteClient();}
        });
        findViewById(R.id.profile_password_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {resetPassword();}
        });
        findViewById(R.id.profile_password_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {confirmResetPassword();}
        });
        findViewById(R.id.get_profile_token).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProfileToken();
            }
        });
    }

    // ****************************************************************************************************************************************

    private void getIdFromEmail() {
        if (isValid(clientEmail)) {

            if (getClientID != null) getClientID.cancel();
            getClientID = Profile.getClientID(clientEmail.getEditText().getText().toString());
            getClientID.execute(new DataActionListener<AccountInformation>() {
                @Override
                public void onDataAction(AccountInformation data) {
                    clientId.getEditText().setText("0");//todo
                }
            }, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {
                    clientId.getEditText().setText("0");//todo
                }
            });
        }
    }

    private void createClient() {
        Attributes attributes = new Attributes()
                .add("someStringNoUnderscores", "Synerise");

        CreateClient createClient = new CreateClient();
        createClient.setCity("Paris").setPhone("123456789").setDisplayName("Joe Doe").setAttributes(attributes);

        if (call != null) call.cancel();
        call = Profile.createClient(createClient);
        call.execute(new ActionListener() {
                         @Override
                         public void onAction() {
                             onSuccess(R.string.message_create_client_success);
                         }
                     },
                     new DataActionListener<ApiError>() {
                         @Override
                         public void onDataAction(ApiError apiError) {
                             onFailure(apiError, R.string.message_create_client_failure);
                         }
                     });
    }

    private void registerClient() {
        RegisterClient registerClient = new RegisterClient(UUID.randomUUID().toString() + "@email.com", "pass_1_&");
        registerClient.setCity("London").setCompany("123456789").setFirstName("Tom");

        if (call != null) call.cancel();
        call = Profile.registerClient(registerClient);
        call.execute(new ActionListener() {
                         @Override
                         public void onAction() {
                             onSuccess(R.string.message_register_client_success);
                         }
                     },
                     new DataActionListener<ApiError>() {
                         @Override
                         public void onDataAction(ApiError apiError) {
                             onFailure(apiError, R.string.message_register_client_failure);
                         }
                     });
    }

    private void updateClient() {
        if (isValid(clientId)) {

            UpdateClient updateClient = new UpdateClient();
            updateClient.setCity("Warsaw").setPhone("987654321").setDisplayName("Bruce Lee");

            if (call != null) call.cancel();
            call = Profile.updateClient(getClientId(), updateClient);
            call.execute(new ActionListener() {
                             @Override
                             public void onAction() {onSuccess(R.string.message_update_client_success);}
                         },
                         new DataActionListener<ApiError>() {
                             @Override
                             public void onDataAction(ApiError apiError) {onFailure(apiError, R.string.message_update_client_failure);}
                         });
        }
    }

    private void deleteClient() {
        if (isValid(clientId)) {

            if (call != null) call.cancel();
            call = Profile.deleteClient(getClientId());
            call.execute(new ActionListener() {
                             @Override
                             public void onAction() {onSuccess(R.string.message_delete_client_success);}
                         },
                         new DataActionListener<ApiError>() {
                             @Override
                             public void onDataAction(ApiError apiError) {onFailure(apiError, R.string.message_delete_client_failure);}
                         });
        }
    }

    private void resetPassword() {
        if (isValid(email)) {
            if (call != null) call.cancel();
            call = Profile.requestPasswordReset(email.getEditText().getText().toString());
            call.execute(new ActionListener() {
                             @Override
                             public void onAction() {onSuccess(R.string.message_reset_password_success);}
                         },
                         new DataActionListener<ApiError>() {
                             @Override
                             public void onDataAction(ApiError apiError) {
                                 onFailure(apiError, R.string.message_reset_password_failure);
                             }
                         });
        }
    }

    private void confirmResetPassword() {
        if (isValid(password) && isValid(token)) {
            if (call != null) call.cancel();
            call = Profile.confirmResetPassword(password.getEditText().getText().toString(), token.getEditText().getText().toString());
            call.execute(new ActionListener() {
                             @Override
                             public void onAction() {onSuccess(R.string.message_confirm_reset_password_success);}
                         },
                         new DataActionListener<ApiError>() {
                             @Override
                             public void onDataAction(ApiError apiError) {
                                 onFailure(apiError, R.string.message_confirm_reset_password_failure);
                             }
                         });
        }
    }

    public void getProfileToken() {
        if (getTokenCall != null) getTokenCall.cancel();
        getTokenCall = Profile.getToken();
        getTokenCall.execute(new DataActionListener<String>() {
                                 @Override
                                 public void onDataAction(String token) {
                                     onSuccess(R.string.message_success, token);
                                 }
                             },
                             new DataActionListener<ApiError>() {
                                 @Override
                                 public void onDataAction(ApiError apiError) {
                                     onFailure(apiError, R.string.message_failure);
                                 }
                             });
    }

    // ****************************************************************************************************************************************

    private void onSuccess(@StringRes int message) {
        Snackbar.make(clientId, message, Snackbar.LENGTH_SHORT).show();
    }

    private void onSuccess(@StringRes int message, Object object) {
        Snackbar.make(clientId, message, Snackbar.LENGTH_SHORT).show();
        Log.d(TAG, "onSuccess: " + object);
    }

    private void onFailure(ApiError apiError, @StringRes int message) {
        String errorCategory = apiError.getHttpErrorCategory().toString();
        Snackbar.make(clientId, message, Snackbar.LENGTH_SHORT).show();
        Log.w(TAG, "onFailure " + apiError.getErrorBody());
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

    @Override
    protected void onStop() {
        super.onStop();
        if (call != null) call.cancel();
        if (getTokenCall != null) getTokenCall.cancel();
        if (getClientID != null) getClientID.cancel();
    }
}
