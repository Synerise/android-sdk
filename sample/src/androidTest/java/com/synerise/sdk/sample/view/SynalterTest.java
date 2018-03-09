package com.synerise.sdk.sample.view;

import android.content.Context;
import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.typeadapters.UtcDateTypeAdapter;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.view.synalter.app.SynalterActivity;
import com.synerise.sdk.sample.view.synalter.support.SynalterSupportActivity;
import com.synerise.sdk.synalter.model.SynalterResponse;
import com.synerise.sdk.synalter.persistence.SynalterAccountManager;
import com.synerise.sdk.synalter.persistence.prefs.SynalterSharedPrefsStorage;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class SynalterTest {

    private final static String mockSynalter = "[\n" +
                                               "  {\n" +
                                               "    \"date\": \"2018-01-01T10:10:10.100Z\",\n" +
                                               "    \"origin\": \"some_test_origin\",\n" +
                                               "    \"component_path\": \"com.synerise.sdk.sample.view.MainActivity\",\n" +
                                               "    \"modified_data\": {\n" +
                                               "      \"id\": \"menu_button_tracker\",\n" +
                                               "      \"text\": \"Test\"\n" +
                                               "    },\n" +
                                               "    \"valid_through\": {\n" +
                                               "      \"valid_from\": \"2018-01-01T10:10:10.100Z\",\n" +
                                               "      \"valid_to\": \"2018-10-10T10:10:10.100Z\"\n" +
                                               "    }\n" +
                                               "  }," +
                                               "  {\n" +
                                               "    \"date\": \"2018-01-01T10:10:10.100Z\",\n" +
                                               "    \"origin\": \"some_test_origin\",\n" +
                                               "    \"component_path\": \"com.synerise.sdk.sample.view.synalter.support.SynalterSupportActivity\",\n" +
                                               "    \"modified_data\": {\n" +
                                               "      \"id\": \"button_1\",\n" +
                                               "      \"text\": \"Test\"\n" +
                                               "    },\n" +
                                               "    \"valid_through\": {\n" +
                                               "      \"valid_from\": \"2018-01-01T10:10:10.100Z\",\n" +
                                               "      \"valid_to\": \"2018-10-10T10:10:10.100Z\"\n" +
                                               "    }\n" +
                                               "  }," +
                                               "  {\n" +
                                               "    \"date\": \"2018-01-01T10:10:10.100Z\",\n" +
                                               "    \"origin\": \"some_test_origin\",\n" +
                                               "    \"component_path\": \"com.synerise.sdk.sample.view.synalter.app.SynalterActivity\",\n" +
                                               "    \"modified_data\": {\n" +
                                               "      \"id\": \"button_1\",\n" +
                                               "      \"text\": \"Test\"\n" +
                                               "    },\n" +
                                               "    \"valid_through\": {\n" +
                                               "      \"valid_from\": \"2018-01-01T10:10:10.100Z\",\n" +
                                               "      \"valid_to\": \"2018-10-10T10:10:10.100Z\"\n" +
                                               "    }\n" +
                                               "  }" +
                                               "]";

    private final static String dummySynalterData = "[\n" + //component path in this dummy data is invalid
                                                    "  {\n" +
                                                    "    \"date\": \"2018-01-01T10:10:10.100Z\",\n" +
                                                    "    \"origin\": \"some_test_origin\",\n" +
                                                    "    \"component_path\": \"some.random.path\",\n" +
                                                    "    \"modified_data\": {\n" +
                                                    "      \"id\": \"menu_button_tracker\",\n" +
                                                    "      \"text\": \"Test2\"\n" +
                                                    "    },\n" +
                                                    "    \"valid_through\": {\n" +
                                                    "      \"valid_from\": \"2018-01-01T10:10:10.100Z\",\n" +
                                                    "      \"valid_to\": \"2018-10-10T10:10:10.100Z\"\n" +
                                                    "    }\n" +
                                                    "  }" +
                                                    "]";
    // ****************************************************************************************************************************************

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class, false, false);
    @Rule
    public ActivityTestRule<SynalterSupportActivity> synalterSupportActivityTestRule = new ActivityTestRule<>(
            SynalterSupportActivity.class, false, false);
    @Rule
    public ActivityTestRule<SynalterActivity> synalterActivityTestRule = new ActivityTestRule<>(SynalterActivity.class, false, false);
    private SynalterAccountManager synalterAccountManager;
    private Gson gson;
    private Type listType;

    // ****************************************************************************************************************************************

    @Before
    public void setUp() throws Exception {
        initSynalterAccountManager();
        //put dummy invalid data into cache so it won't refresh
        synalterAccountManager.setSynalterResponseList((List<SynalterResponse>) gson.fromJson(dummySynalterData, listType));
    }

    @After
    public void tearDown() throws Exception {
        synalterAccountManager.setSynalterResponseList(new ArrayList<SynalterResponse>());
    }

    // ****************************************************************************************************************************************

    @Test
    public void testSynalterTextChange() throws Exception {
        Intent intent = new Intent();
        mainActivityTestRule.launchActivity(intent);
        onView(withId(R.id.menu_button_tracker))
                .check(matches(isDisplayed()))
                .check(matches(withText(getInstrumentation().getTargetContext().getString(R.string.menu_button_tracker))));
        synalterAccountManager.setSynalterResponseList((List<SynalterResponse>) gson.fromJson(mockSynalter, listType));
        restartActivity(mainActivityTestRule);
        onView(withId(R.id.menu_button_tracker))
                .check(matches(isDisplayed()))
                .check(matches(withText("Test")));
    }

    @Test
    public void testSynalterTextChangeOnSynalterSupportActivity() throws Exception {
        Intent intent = new Intent();
        synalterSupportActivityTestRule.launchActivity(intent);
        onView(withId(R.id.button_1))
                .check(matches(isDisplayed()))
                .check(matches(withText("Button 1")));
        synalterAccountManager.setSynalterResponseList((List<SynalterResponse>) gson.fromJson(mockSynalter, listType));
        restartActivity(synalterSupportActivityTestRule);
        onView(withId(R.id.button_1))
                .check(matches(isDisplayed()))
                .check(matches(withText("Test")));
    }

    @Test
    public void testSynalterTextChangeOnSynalterAppActivity() throws Exception {
        Intent intent = new Intent();
        synalterActivityTestRule.launchActivity(intent);
        onView(withId(R.id.button_1))
                .check(matches(isDisplayed()))
                .check(matches(withText("Button 1")));
        synalterAccountManager.setSynalterResponseList((List<SynalterResponse>) gson.fromJson(mockSynalter, listType));
        restartActivity(synalterActivityTestRule);
        onView(withId(R.id.button_1))
                .check(matches(isDisplayed()))
                .check(matches(withText("Test")));
    }

    // ****************************************************************************************************************************************

    private void restartActivity(final ActivityTestRule testRule) {
        getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                getInstrumentation().callActivityOnPause(testRule.getActivity());
                getInstrumentation().callActivityOnStop(testRule.getActivity());
                getInstrumentation().callActivityOnRestart(testRule.getActivity());
                getInstrumentation().callActivityOnStart(testRule.getActivity());
                getInstrumentation().callActivityOnResume(testRule.getActivity());
            }
        });
    }

    private void initSynalterAccountManager() {
        Context context = getInstrumentation().getTargetContext();
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                                .registerTypeAdapter(Date.class, new UtcDateTypeAdapter())
                                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                                .setPrettyPrinting()
                                .create();
        String syneriseBusinessProfileApiKey = context.getString(R.string.synerise_business_api_key);
        String syneriseClientApiKey = context.getString(R.string.synerise_client_api_key);
        synalterAccountManager = new SynalterAccountManager(context,
                                                            syneriseBusinessProfileApiKey,
                                                            syneriseClientApiKey,
                                                            new SynalterSharedPrefsStorage(context, gson, "SynalterSDKPrefs"),
                                                            10000);
        listType = new TypeToken<ArrayList<SynalterResponse>>() {}.getType();
    }
}