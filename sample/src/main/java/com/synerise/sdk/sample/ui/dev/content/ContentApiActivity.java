package com.synerise.sdk.sample.ui.dev.content;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;

import android.widget.TextView;
import android.widget.Toast;

import com.synerise.sdk.content.Content;
import com.synerise.sdk.content.model.DocumentsApiQueryType;
import com.synerise.sdk.content.model.document.Document;
import com.synerise.sdk.content.model.document.DocumentApiQuery;
import com.synerise.sdk.core.net.IDataApiCall;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.util.ToolbarHelper;
import com.synerise.sdk.sample.util.ViewUtils;

import java.util.List;

public class ContentApiActivity extends BaseActivity {

    private TextInputLayout valueInput;
    private IDataApiCall<Document> apiCall;
    private IDataApiCall<List<Document>> apiCallDocuments;
    private TextView getDocumentResponse;

    public static Intent createIntent(Context context) {
        return new Intent(context, ContentApiActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_api);

        ToolbarHelper.setUpChildToolbar(this, R.string.content_api);

        valueInput = findViewById(R.id.input_value);
        getDocumentResponse = findViewById(R.id.get_document_response);
        findViewById(R.id.slider_widget).setOnClickListener(v -> startActivity(WidgetHorizontalSliderActivity.createIntent(this)));
        findViewById(R.id.gridview_widget).setOnClickListener(v -> startActivity(WidgetGridViewActivity.createIntent(this)));
        findViewById(R.id.get_document).setOnClickListener(v -> getDocument());
    }

    private void getDocument() {
        if (!valueInput.getEditText().getText().toString().matches("")) {
            valueInput.getEditText().getText().toString();

            if (apiCall != null) {
                apiCall.cancel();
            }
            apiCall = Content.generateDocument(valueInput.getEditText().getText().toString());
            apiCall.execute(response -> {
                if (response != null) {
                    getDocumentResponse.setText(ViewUtils.formatStringToJsonLook(response.toString()));
                }
            }, this::onFailure);
        } else {
            Toast.makeText(getApplicationContext(), "Please fill value name", Toast.LENGTH_LONG).show();
        }
    }

    private void onFailure(ApiError apiError) {
        Toast.makeText(getApplicationContext(), apiError.toString(),Toast.LENGTH_LONG).show();
    }
}
