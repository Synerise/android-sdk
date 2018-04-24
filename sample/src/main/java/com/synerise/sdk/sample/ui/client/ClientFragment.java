package com.synerise.sdk.sample.ui.client;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.synerise.sdk.client.Client;
import com.synerise.sdk.client.model.AccountInformation;
import com.synerise.sdk.core.listeners.DataActionListener;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.core.net.IDataApiCall;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.sample.App;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.persistence.AccountManager;
import com.synerise.sdk.sample.test.EspressoTestingIdlingResource;
import com.synerise.sdk.sample.ui.BaseFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ClientFragment extends BaseFragment {

    @BindView(R.id.require_sign_in) TextView requireSignIn;
    @BindView(R.id.client_features) LinearLayout clientFeatures;

    @BindView(R.id.get_account) Button getAccount;

    @BindView(R.id.input_city_update) TextInputLayout inputCityUpdate;
    @BindView(R.id.input_company_update) TextInputLayout inputCompanyUpdate;
    @BindView(R.id.update_account) Button updateAccount;

    @BindView(R.id.get_token) Button getToken;

    private Unbinder unbinder;

    private IApiCall updateAccountCall;
    private IDataApiCall<String> getTokenCall;
    private IDataApiCall<AccountInformation> accountInfoCall;

    @Inject AccountManager accountManager;

    public static ClientFragment newInstance() { return new ClientFragment(); }

    // ****************************************************************************************************************************************

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getActivity().getApplication()).getComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_client, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        if (Client.isSignedIn()) {
            clientFeatures.setVisibility(VISIBLE);
            requireSignIn.setVisibility(GONE);
        } else {
            clientFeatures.setVisibility(GONE);
            requireSignIn.setVisibility(VISIBLE);
        }

        getAccount.setOnClickListener(v -> getAccount());
        updateAccount.setOnClickListener(v -> updateAccount());
        getToken.setOnClickListener(v -> getToken());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (updateAccountCall != null) updateAccountCall.cancel();
        if (accountInfoCall != null) accountInfoCall.cancel();
        if (getTokenCall != null) getTokenCall.cancel();
    }

    // ****************************************************************************************************************************************

    private void getAccount() {
        if (accountInfoCall != null) accountInfoCall.cancel();
        accountInfoCall = Client.getAccount();
        EspressoTestingIdlingResource.increment();
        accountInfoCall.execute(success -> onSuccess(), this::onFailure);
    }

    @SuppressWarnings("ConstantConditions")
    private void updateAccount() {

        boolean isValid = true;

        inputCityUpdate.setError(null);
        inputCompanyUpdate.setError(null);

        String city = inputCityUpdate.getEditText().getText().toString();
        String company = inputCompanyUpdate.getEditText().getText().toString();

        if (TextUtils.isEmpty(city)) {
            inputCityUpdate.setError(getString(R.string.error_empty));
            isValid = false;
        }

        if (TextUtils.isEmpty(company)) {
            inputCompanyUpdate.setError(getString(R.string.error_empty));
            isValid = false;
        }

        if (isValid) {
            AccountInformation accountInformation = new AccountInformation();
            accountInformation.setCity(city).setCompany(company);

            if (updateAccountCall != null) updateAccountCall.cancel();
            updateAccountCall = Client.updateAccount(accountInformation);
            EspressoTestingIdlingResource.increment();
            updateAccountCall.execute(this::onSuccess, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {
                    ClientFragment.this.onFailure(apiError);}
            });
        }
    }

    private void getToken() {
        if (getTokenCall != null) getTokenCall.cancel();
        getTokenCall = Client.getToken();
        EspressoTestingIdlingResource.increment();
        getTokenCall.execute(success -> onSuccess(), this::onFailure);
    }

    // ****************************************************************************************************************************************

    private void onSuccess() {
        EspressoTestingIdlingResource.decrement();
        Snackbar.make(getAccount, getString(R.string.default_success), Snackbar.LENGTH_SHORT).show();
    }

    private void onFailure(ApiError apiError) {
        EspressoTestingIdlingResource.decrement();
        Snackbar.make(getAccount, getErrorMessage(apiError), Snackbar.LENGTH_SHORT).show();
    }
}
