package com.synerise.sdk.sample.view.profile;

import android.support.annotation.StringRes;
import android.support.test.espresso.Espresso;
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
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ProfileFeaturesActivityTest {

    @Rule
    public ActivityTestRule<ProfileFeaturesActivity> mActivityRule = new ActivityTestRule<>(ProfileFeaturesActivity.class);

    // ****************************************************************************************************************************************

    private String email, password;

    // ****************************************************************************************************************************************

    @Before
    public void registerIdlingResource() {
        email = "marcel.skotnicki@toreforge.com";
        password = "pass_1_&";
        IdlingRegistry.getInstance().register(EspressoTestingIdlingResource.getIdlingResource());
        Espresso.closeSoftKeyboard();
    }

    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoTestingIdlingResource.getIdlingResource());
    }

    // ****************************************************************************************************************************************

    @Test
    public void createClient() {
        onView(withId(R.id.profile_client_create)).perform(scrollTo())
                                                  .check(matches(isDisplayed()))
                                                  .perform(click());
        checkMessageDisplayed(R.string.message_create_client_success);
    }

    @Test
    public void registerClient() {
        onView(withId(R.id.profile_client_register)).perform(scrollTo())
                                                    .check(matches(isDisplayed()))
                                                    .perform(click());
        checkMessageDisplayed(R.string.message_register_client_success);
    }

    @Test
    public void updateClient() {
        getClientID();
        onView(withId(R.id.profile_client_update)).perform(scrollTo())
                                                  .check(matches(isDisplayed()))
                                                  .perform(click());
        checkMessageDisplayed(R.string.message_update_client_success);
    }

    @Test
    public void deleteClient() {
        getClientID();
        onView(withId(R.id.profile_client_delete)).perform(scrollTo())
                                                  .check(matches(isDisplayed()))
                                                  .perform(click());
        checkMessageDisplayed(R.string.message_delete_client_success);
    }

    @Test
    public void resetPassword() throws InterruptedException {
        onView(withId(R.id.profile_password_reset_email_edit)).perform(scrollTo())
                                                              .check(matches(isDisplayed()))
                                                              .perform(typeText(email), closeSoftKeyboard())
                                                              .check(matches(withText(email)));
        onView(withId(R.id.profile_password_reset)).perform(scrollTo()).perform(click());
        checkMessageDisplayed(R.string.message_reset_password_failure);
    }

    @Test
    public void confirmResetPassword() throws InterruptedException {
        onView(withId(R.id.profile_password_confirm_pass_edit)).perform(scrollTo())
                                                               .check(matches(isDisplayed()))
                                                               .perform(typeText(password), closeSoftKeyboard())
                                                               .check(matches(withText(password)));
        onView(withId(R.id.profile_password_confirm_token_edit)).perform(scrollTo())
                                                                .check(matches(isDisplayed()))
                                                                .perform(typeText(email), closeSoftKeyboard())
                                                                .check(matches(withText(email))); // todo: token instead of email
        onView(withId(R.id.profile_password_confirm)).perform(scrollTo())
                                                     .check(matches(isDisplayed()))
                                                     .perform(click());
        checkMessageDisplayed(R.string.message_confirm_reset_password_failure);
    }

    @Test
    public void getToken() {
        onView(withId(R.id.get_profile_token)).perform(scrollTo())
                                              .check(matches(isDisplayed()))
                                              .perform(click());
        checkMessageDisplayed(R.string.message_success);
    }

    // ****************************************************************************************************************************************

    private void getClientID() {
        onView(withId(R.id.profile_client_email_edit)).perform(scrollTo())
                                                      .check(matches(isDisplayed()))
                                                      .perform(typeText(email), closeSoftKeyboard())
                                                      .check(matches(withText(email)));
        onView(withId(R.id.profile_button_id)).perform(scrollTo())
                                              .check(matches(isDisplayed()))
                                              .perform(click());
    }

    private void checkMessageDisplayed(@StringRes int message) {
        onView(withText(mActivityRule.getActivity().getString(message))).check(
                matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }
}