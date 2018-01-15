package com.synerise.sdk.sample.view.events.widgets;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
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
import com.synerise.sdk.sample.view.events.widgets.nested_scroll.NestedScrollingActivity;
import com.synerise.sdk.sample.view.events.widgets.recycler.RecyclerAndHorizontalScrollActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marcel Skotnicki on 1/10/18.
 */

public class TrackerViewActivity extends AppCompatActivity {

    private static final String TAG = TrackerViewActivity.class.getSimpleName();

    public static Intent createIntent(Context context) {
        return new Intent(context, TrackerViewActivity.class);
    }

    // ****************************************************************************************************************************************

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker_view);

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

        ((RatingBar) findViewById(R.id.rating_bar)).setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Log.d(TAG, "RatingBar~onRatingChanged: " + rating);
            }
        });

        findViewById(R.id.progress_bar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "ProgressBar~onClick");
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ((DatePicker) findViewById(R.id.date_picker)).setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Log.d(TAG, "DatePicker~onDateChanged: " + year + "/" + monthOfYear + "/" + dayOfMonth);
                }
            });
        }

        ((TimePicker) findViewById(R.id.time_picker)).setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                Log.d(TAG, "TimePicker~onTimeChanged: " + hourOfDay + ":" + minute);
            }
        });

        NumberPicker numberPicker = findViewById(R.id.number_picker);
        numberPicker.setMaxValue(30);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.d(TAG, "NumberPicker~onValueChange: " + oldVal + " -> " + newVal);
            }
        });

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

        ((CheckBox) findViewById(R.id.checkbox)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "CheckBox~onCheckedChanged: " + isChecked);
            }
        });

        ((RadioButton) findViewById(R.id.radio_button)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "RadioButton~onCheckedChanged: " + isChecked);
            }
        });

        ((Switch) findViewById(R.id.a_switch)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "Switch~onCheckedChanged: " + isChecked);
            }
        });

        ((RadioGroup) findViewById(R.id.radio_group)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d(TAG, "RadioGroup~onCheckedChanged: " + checkedId);
            }
        });

        findViewById(R.id.edit_text).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "EditText~onTouch");
                return true;
            }
        });

        final CheckedTextView checkedTextView = findViewById(R.id.checked_text_view);
        checkedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "CheckedTextView~onClick");
                checkedTextView.setChecked(!checkedTextView.isChecked());
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            findViewById(R.id.text_view).setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    Log.d(TAG,
                          "TextView~onScrollChange: [" + oldScrollX + "," + oldScrollY + "] -> [" + scrollX + "," + scrollY + "]");
                }
            });
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            findViewById(R.id.scroll_view).setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    //Log.d(TAG, "ScrollView~onScrollChange: [" + oldScrollX + "," + oldScrollY + "] -> [" + scrollX + "," + scrollY + "]");
                }
            });
        }
    }

    // ****************************************************************************************************************************************

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.track_view_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_horizontal_scroll_view:
                startActivity(RecyclerAndHorizontalScrollActivity.createIntent(this));
                return true;
            case R.id.menu_nested_scroll_view:
                startActivity(NestedScrollingActivity.createIntent(this));
                return true;
            case R.id.menu_recycler:
                startActivity(RecyclerAndHorizontalScrollActivity.createIntent(this));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
