package com.synerise.sdk.sample.view.client;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.test.EspressoTestingIdlingResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;

@RunWith(AndroidJUnit4.class)
public class ClientSignInActivityTest {

    @Rule
    public ActivityTestRule<ClientSignInActivity> mActivityRule = new ActivityTestRule<>(ClientSignInActivity.class);

    // ****************************************************************************************************************************************

    private String email, password;
    private ClientSignInActivity activity;

    // ****************************************************************************************************************************************

    @Before
    public void registerIdlingResource() {
        email = "marcel.skotnicki@toreforge.com";
        password = "qwerty";
        activity = mActivityRule.getActivity();
        IdlingRegistry.getInstance().register(EspressoTestingIdlingResource.getIdlingResource());
    }

    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoTestingIdlingResource.getIdlingResource());
    }

    // ****************************************************************************************************************************************

    @Test
    public void signIn_validData() {

        // type email
        onView(withId(R.id.client_email_input_edit)).check(matches(isDisplayed()))
                                                    .perform(typeText(email))
                                                    .check(matches(withText(email)));

        // type password and close keyboard
        onView(withId(R.id.client_password_edit)).check(matches(isDisplayed()))
                                                 .perform(typeText(password), closeSoftKeyboard())
                                                 .check(matches(withText(password)));

        // click sign in button
        onView(withId(R.id.client_sign_in_button)).check(matches(isDisplayed()))
                                                  .perform(click());

        // expect failure due to 401
        checkMessageDisplayed();
    }

    // ****************************************************************************************************************************************

    private void checkMessageDisplayed() {
        onView(withText(containsString(activity.getString(R.string.message_sing_in_failure)))).check(matches(withEffectiveVisibility(
                ViewMatchers.Visibility.VISIBLE)));
    }
}