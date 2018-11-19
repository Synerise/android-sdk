package com.synerise.sdk.sample.ui.dev.tracker;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.util.ToolbarHelper;

import java.util.ArrayList;
import java.util.List;

public class TrackerViewActivity extends BaseActivity {

    private static final String TAG = TrackerViewActivity.class.getSimpleName();

    public static Intent createIntent(Context context) {
        return new Intent(context, TrackerViewActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker_view);

        ToolbarHelper.setUpChildToolbar(this, R.string.tracker_view_title);

        ((SeekBar) findViewById(R.id.seek_bar)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "SeekBar~onProgressChanged: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ((RatingBar) findViewById(R.id.rating_bar)).setOnRatingBarChangeListener(
                (ratingBar, rating, fromUser) -> Log.d(TAG, "RatingBar~onRatingChanged: " + rating));

        findViewById(R.id.date_picker_pre_26_api_button).setOnClickListener(
                v -> new DatePickerDialog(TrackerViewActivity.this, (view, year, month, dayOfMonth) -> Log.d(TAG,
                                                                                                             "DatePicker~onDateChanged: " + year + "/" + month + "/" + dayOfMonth),
                                          10, 10, 10).show());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ((DatePicker) findViewById(R.id.date_picker)).setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> Log.d(TAG,
                                                                                                                                  "DatePicker~onDateChanged: " + year + "/" + monthOfYear + "/" + dayOfMonth));
        }

        ((TimePicker) findViewById(R.id.time_picker)).setOnTimeChangedListener(
                (view, hourOfDay, minute) -> Log.d(TAG, "TimePicker~onTimeChanged: " + hourOfDay + ":" + minute));

        NumberPicker numberPicker = findViewById(R.id.number_picker);
        numberPicker.setMaxValue(30);
        numberPicker.setOnValueChangedListener(
                (picker, oldVal, newVal) -> Log.d(TAG, "NumberPicker~onValueChange: " + oldVal + " -> " + newVal));

        Spinner spinner = findViewById(R.id.spinner);
        final List<String> list = new ArrayList<String>() {{
            add("Spinner item 1");
            add("Spinner item 2");
            add("Spinner item 3");
        }};
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Spinner~onItemSelected position: " + position);
                Object item = parent.getItemAtPosition(position);
                if (item instanceof String) {
                    Log.d(TAG, "Spinner~onItemSelected: " + item);
                } else if (item instanceof TextView) {
                    Log.d(TAG, "Spinner~onItemSelected: " + ((TextView) item).getText().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //        ((CheckBox) findViewById(R.id.checkbox)).setOnCheckedChangeListener(
        //                (buttonView, isChecked) -> Log.d(TAG, "CheckBox~onCheckedChanged: " + isChecked));

        ((RadioButton) findViewById(R.id.radio_button)).setOnCheckedChangeListener(
                (buttonView, isChecked) -> Log.d(TAG, "RadioButton~onCheckedChanged: " + isChecked));

        ((Switch) findViewById(R.id.a_switch)).setOnCheckedChangeListener(
                (buttonView, isChecked) -> Log.d(TAG, "Switch~onCheckedChanged: " + isChecked));

        ((RadioGroup) findViewById(R.id.radio_group)).setOnCheckedChangeListener(
                (group, checkedId) -> Log.d(TAG, "RadioGroup~onCheckedChanged: " + checkedId));

        findViewById(R.id.edit_text).setOnTouchListener((v, event) -> {
            Log.d(TAG, "EditText~onTouch");
            return false;
        });

        final CheckedTextView checkedTextView = findViewById(R.id.checked_text_view);
        checkedTextView.setOnClickListener(v -> {
            Log.d(TAG, "CheckedTextView~onClick");
            checkedTextView.setChecked(!checkedTextView.isChecked());
        });
        checkedTextView.setOnLongClickListener(v -> {
            Log.d(TAG, "CheckedTextView~OnLongClickListener");
            return false;
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            findViewById(R.id.text_view).setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> Log.d(TAG,
                                                                                                                          "TextView~onScrollChange: [" + oldScrollX + "," + oldScrollY + "] -> [" + scrollX + "," + scrollY + "]"));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            findViewById(R.id.scroll_view).setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                //Log.d(TAG, "ScrollView~onScrollChange: [" + oldScrollX + "," + oldScrollY + "] -> [" + scrollX + "," + scrollY + "]");
            });
        }
    }
}
