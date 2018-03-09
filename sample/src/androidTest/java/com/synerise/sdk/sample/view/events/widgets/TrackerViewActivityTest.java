package com.synerise.sdk.sample.view.events.widgets;

import android.os.Build;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TimePicker;

import com.synerise.sdk.sample.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

@RunWith(AndroidJUnit4.class)
public class TrackerViewActivityTest {

    @Rule
    public ActivityTestRule<TrackerViewActivity> mActivityRule = new ActivityTestRule<TrackerViewActivity>(
            TrackerViewActivity.class);

    // ****************************************************************************************************************************************

    @Test
    public void testSeekBar() {
        onView(withId(R.id.seek_bar))
                .perform(scrollTo())
                .check(matches(isDisplayed()))
                .perform(setSeekBarProgress(10))
                .check(matches(matchSeekBarProgress(10)));
    }

    @Test
    public void testRatingBar() {
        onView(withId(R.id.rating_bar))
                .perform(scrollTo())
                .check(matches(isDisplayed()))
                .perform(setRatingBarValue(2.5f))
                .check(matches(matchRatingBarValue(2.5f)));
    }

    @Test
    public void testPre26DataPicker() {
        int year = 2017;
        int month = 5;
        int day = 12;

        onView(withId(R.id.date_picker_pre_26_api_button))
                .perform(scrollTo())
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .check(matches(isDisplayed()))
                .perform(PickerActions.setDate(year, month, day))
                .check(matches(matchDatePicker(year, month, day)));
        onView(withText("OK")).perform(click());
    }

    @Test
    public void testPost26DataPicker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int day = 20;
            int month = 5;
            int year = 2017;
            onView(withId(R.id.date_picker))
                    .perform(scrollTo())
                    .check(matches(isDisplayed()))
                    .perform(PickerActions.setDate(year, month, day))
                    .check(matches(matchDatePicker(year, month, day)));
        }
    }

    @Test
    public void testTimePicker() {
        int hour = 20;
        int minute = 5;
        onView(withId(R.id.time_picker))
                .perform(scrollTo())
                .check(matches(isDisplayed()))
                .perform(PickerActions.setTime(hour, minute))
                .check(matches(matchTimePicker(hour, minute)));
    }

    @Test
    public void testNumberPicker() {
        int number = 20;
        onView(withId(R.id.number_picker))
                .perform(scrollTo())
                .check(matches(isDisplayed()))
                .perform(setNumberPickerValue(number))
                .check(matches(matchNumberPicker(number)));
    }

    @Test
    public void testSpinner() {
        onView(withId(R.id.spinner))
                .perform(scrollTo())
                .check(matches(isDisplayed()))
                .perform(click());
        String selectionText = "Spinner item 2";
        onData(allOf(is(instanceOf(String.class)), is(selectionText))).perform(click());
        onView(withId(R.id.spinner)).check(matches(withSpinnerText(containsString(selectionText))));
    }

    @Test
    public void testCheckBox() {
        onView(withId(R.id.checkbox))
                .perform(scrollTo())
                .check(matches(isNotChecked()))
                .perform(click())
                .check(matches(isChecked()));
    }

    @Test
    public void testRadioButton() {
        onView(withId(R.id.radio_button))
                .perform(scrollTo())
                .check(matches(isNotChecked()))
                .perform(click())
                .check(matches(isChecked()));
    }

    @Test
    public void testSwitch() {
        onView(withId(R.id.a_switch))
                .perform(scrollTo())
                .check(matches(isNotChecked()))
                .perform(click())
                .check(matches(isChecked()));
    }

    @Test
    public void testRadioGroup() {
        onView(withId(R.id.radio_group))
                .perform(scrollTo())
                .check(matches(isDisplayed()));

        onView(withId(R.id.radio_button_1))
                .check(matches(isDisplayed()))
                .check(matches(isNotChecked()));
        onView(withId(R.id.radio_button_2))
                .check(matches(isDisplayed()))
                .check(matches(isNotChecked()));
        onView(withId(R.id.radio_button_3))
                .check(matches(isDisplayed()))
                .check(matches(isNotChecked()));

        //check first button
        onView(withId(R.id.radio_button_1))
                .perform(click())
                .check(matches(isChecked()));
        onView(withId(R.id.radio_button_2))
                .check(matches(isNotChecked()));
        onView(withId(R.id.radio_button_3))
                .check(matches(isNotChecked()));

        //check second button
        onView(withId(R.id.radio_button_2))
                .perform(click())
                .check(matches(isChecked()));
        onView(withId(R.id.radio_button_1))
                .check(matches(isNotChecked()));
        onView(withId(R.id.radio_button_3))
                .check(matches(isNotChecked()));

        //check third button
        onView(withId(R.id.radio_button_3))
                .perform(click())
                .check(matches(isChecked()));
        onView(withId(R.id.radio_button_1))
                .check(matches(isNotChecked()));
        onView(withId(R.id.radio_button_2))
                .check(matches(isNotChecked()));
    }

    @Test
    public void testEditTextClick() {
        onView(withId(R.id.edit_text))
                .perform(scrollTo())
                .check(matches(isDisplayed()))
                .perform(click());
    }

    @Test
    public void testCheckedTextView() {
        onView(withId(R.id.checked_text_view))
                .perform(scrollTo())
                .check(matches(isNotChecked()))
                .perform(click())
                .check(matches(isChecked()))
                .perform(longClick());
    }

    // ****************************************************************************************************************************************

    private Matcher<View> matchNumberPicker(final int number) {
        return new BoundedMatcher<View, NumberPicker>(NumberPicker.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("Checking the matcher on received view: ");
                description.appendText("with expected number=" + number);
            }

            @Override
            protected boolean matchesSafely(NumberPicker numberPicker) {
                return numberPicker.getValue() == number;
            }
        };
    }

    private ViewAction setNumberPickerValue(final int number) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return Matchers.allOf(
                        isAssignableFrom(NumberPicker.class),
                        withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE));
            }

            @Override
            public String getDescription() {
                return "setting number of NumberPicker";
            }

            @Override
            public void perform(UiController uiController, View view) {
                NumberPicker numberPicker = (NumberPicker) view;
                numberPicker.setValue(number);
                uiController.loopMainThreadUntilIdle();
            }
        };
    }

    private Matcher<View> matchDatePicker(final int year, final int month, final int day) {
        return new BoundedMatcher<View, DatePicker>(DatePicker.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("Checking the matcher on received view: ");
                description.appendText("with expected year=" + year + " month=" + month + " day=" + day);
            }

            @Override
            protected boolean matchesSafely(DatePicker datePicker) {
                //datePicker.getMonth() + 1, because January is 0, February is 1 etc.
                return datePicker.getYear() == year && datePicker.getMonth() + 1 == month && datePicker.getDayOfMonth() == day;
            }
        };
    }

    private Matcher<View> matchTimePicker(final int hour, final int minute) {
        return new BoundedMatcher<View, TimePicker>(TimePicker.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("Checking the matcher on received view: ");
                description.appendText("with expected hour=" + hour + " minute=" + minute);
            }

            @Override
            protected boolean matchesSafely(TimePicker timePicker) {
                if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                    return timePicker.getHour() == hour && timePicker.getMinute() == minute;
                } else {
                    return timePicker.getCurrentHour() == hour && timePicker.getCurrentMinute() == minute;
                }
            }
        };
    }

    private Matcher<View> matchSeekBarProgress(final int progress) {
        return new BoundedMatcher<View, SeekBar>(SeekBar.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("Checking the matcher on received view: ");
                description.appendText("with expected progress=" + progress);
            }

            @Override
            protected boolean matchesSafely(SeekBar seekBar) {
                return seekBar.getProgress() == progress;
            }
        };
    }

    private ViewAction setSeekBarProgress(final int progress) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return Matchers.allOf(
                        isAssignableFrom(SeekBar.class),
                        withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE));
            }

            @Override
            public String getDescription() {
                return "setting progress of SeekBar";
            }

            @Override
            public void perform(UiController uiController, View view) {
                SeekBar seekBar = (SeekBar) view;
                seekBar.setProgress(progress);
                uiController.loopMainThreadUntilIdle();
            }
        };
    }

    private Matcher<View> matchRatingBarValue(final float rating) {
        return new BoundedMatcher<View, RatingBar>(RatingBar.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("Checking the matcher on received view: ");
                description.appendText("with expected rating=" + rating);
            }

            @Override
            protected boolean matchesSafely(RatingBar ratingBar) {
                return ratingBar.getRating() == rating;
            }
        };
    }

    private ViewAction setRatingBarValue(final float rating) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return Matchers.allOf(
                        isAssignableFrom(RatingBar.class),
                        withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE));
            }

            @Override
            public String getDescription() {
                return "setting rating of RatingBar";
            }

            @Override
            public void perform(UiController uiController, View view) {
                RatingBar ratingBar = (RatingBar) view;
                ratingBar.setRating(rating);
                uiController.loopMainThreadUntilIdle();
            }
        };
    }
}