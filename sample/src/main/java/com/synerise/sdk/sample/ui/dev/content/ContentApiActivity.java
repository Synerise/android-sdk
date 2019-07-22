package com.synerise.sdk.sample.ui.dev.content;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.synerise.sdk.content.Content;
import com.synerise.sdk.content.model.recommendation.Recommendation;
import com.synerise.sdk.content.model.recommendation.RecommendationRequestBody;
import com.synerise.sdk.content.widgets.viewModel.BaseViewModel;
import com.synerise.sdk.content.widgets.viewModel.RecommendationViewModel;
import com.synerise.sdk.core.net.IDataApiCall;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.util.ToolbarHelper;
import com.synerise.sdk.sample.util.ViewUtils;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;

public class ContentApiActivity extends BaseActivity {

    private TextInputLayout slugNameInput;
    private IDataApiCall<ResponseBody> apiCall;
    private TextView getDocumentResponse;

    public static Intent createIntent(Context context) {
        return new Intent(context, ContentApiActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_api);

        ToolbarHelper.setUpChildToolbar(this, R.string.content_api);

        slugNameInput = findViewById(R.id.input_slug_name);
        getDocumentResponse = findViewById(R.id.get_document_response);
        findViewById(R.id.slider_widget).setOnClickListener(v -> startActivity(WidgetHorizontalSliderActivity.createIntent(this)));
        findViewById(R.id.gridview_widget).setOnClickListener(v -> startActivity(WidgetGridViewActivity.createIntent(this)));
        findViewById(R.id.get_document).setOnClickListener(v -> getDocument());
    }

    private void getDocument() {
        if (!slugNameInput.getEditText().getText().toString().matches("")) {
            slugNameInput.getEditText().getText().toString();

            if (apiCall != null) {
                apiCall.cancel();
            }
            apiCall = Content.getDocument(slugNameInput.getEditText().getText().toString());
            apiCall.execute(response -> {
                if (response != null) {
                    try {
                        getDocumentResponse.setText(ViewUtils.formatStringToJsonLook(response.string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }, this::onFailure);
        } else {
            Toast.makeText(getApplicationContext(), "Please fill slug name",Toast.LENGTH_LONG).show();
        }
    }

    private void onFailure(ApiError apiError) {
        Toast.makeText(getApplicationContext(), apiError.toString(),Toast.LENGTH_LONG).show();
    }
}
