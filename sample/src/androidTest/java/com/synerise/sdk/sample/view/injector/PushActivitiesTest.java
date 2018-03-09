package com.synerise.sdk.sample.view.injector;

import android.content.Context;
import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.util.ArrayMap;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.UtcDateTypeAdapter;
import com.synerise.sdk.injector.net.model.push.ContentType;
import com.synerise.sdk.injector.net.model.push.MessageType;
import com.synerise.sdk.injector.resolver.push.DynamicContentResolver;
import com.synerise.sdk.sample.view.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Date;

public class PushActivitiesTest {

    private static final String SYNERISE_ISSUER = "Synerise";
    private static final String BANNER_URL = "http://www.mbank.pl";
    private static final String IMG_URL = "https://sii.pl/blog/wp-content/uploads/2017/01/kotlin_800x320.png";
    private static final String FULLSCREEN_CONTENT = "{\n" +
                                                     "  \"image\": {\n" +
                                                     "    \"url\": \"" + IMG_URL + "\"\n" +
                                                     "  },\n" +
                                                     "  \"url\": \"" + BANNER_URL + "\",\n" +
                                                     "  \"manuallyCloseable\": true,\n" +
                                                     "  \"autoCloseTimeout\": 1000\n" +
                                                     "}";
    private static final String MESSAGE_POP_UP_CONTENT = "{\n" +
                                                         "  \"title\": \"testTitle\",\n" +
                                                         "  \"message\": \"testMessage\",\n" +
                                                         "  \"closeable\": true\n" +
                                                         "}";
    private static final String BANNER_CONTENT = "{\n" +
                                                 "  \"image\": {\n" +
                                                 "    \"url\": \"" + IMG_URL + "\"\n" +
                                                 "  },\n" +
                                                 "  \"url\": \"" + BANNER_URL + "\"\n" +
                                                 "}";
    private static final String CUSTOM_BANNER_CONTENT = "{\n" +
                                                        "  \"image\": {\n" +
                                                        "    \"url\": \"" + IMG_URL + "\"\n" +
                                                        "  },\n" +
                                                        "  \"text\": \"testTest\",\n" +
                                                        "  \"url\": \"" + BANNER_URL + "\"\n" +
                                                        "}";

    // ****************************************************************************************************************************************

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    private DynamicContentResolver resolver;
    private ArrayMap<String, String> data;
    private ContentType contentType;
    private MessageType messageType;
    private String contentJson;
    private Context context;

    // ****************************************************************************************************************************************

    @Before
    public void setUp() throws Exception, InvalidPushException {
        context = mActivityRule.getActivity();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                                     .registerTypeAdapter(Date.class, new UtcDateTypeAdapter())
                                     .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                                     .setPrettyPrinting()
                                     .create();
        resolver = new DynamicContentResolver(gson);
    }

    // ****************************************************************************************************************************************

    @Test
    public void testBannerActivity() throws Exception, InvalidPushException {
        initBannerData();
        parseData();
        startActivity();
    }

    @Test
    public void testCustomBannerActivity() throws Exception, InvalidPushException {
        initCustomBannerData();
        parseData();
        startActivity();
    }

    @Test
    public void testMessagePopUpActivity() throws Exception, InvalidPushException {
        initMessagePopUpData();
        parseData();
        startActivity();
    }

    @Test
    public void testFullscreenActivity() throws Exception, InvalidPushException {
        initFullscreenData();
        parseData();
        startActivity();
    }

    // ****************************************************************************************************************************************

    private void startActivity() {
        Intent intent = resolver.resolve(context, contentType, contentJson);
        if (intent != null) {
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            }
        }
    }

    private void initMessagePopUpData() {
        data = new ArrayMap<>();
        data.put(SynerisePayloadKeys.ISSUER, SYNERISE_ISSUER);
        data.put(SynerisePayloadKeys.CONTENT_TYPE, ContentType.MESSAGE.getApiName());
        data.put(SynerisePayloadKeys.MESSAGE_TYPE, MessageType.DYNAMIC_CONTENT.getApiName());
        data.put(SynerisePayloadKeys.CONTENT, MESSAGE_POP_UP_CONTENT);
    }

    private void initBannerData() {
        data = new ArrayMap<>();
        data.put(SynerisePayloadKeys.ISSUER, SYNERISE_ISSUER);
        data.put(SynerisePayloadKeys.CONTENT_TYPE, ContentType.BANNER.getApiName());
        data.put(SynerisePayloadKeys.MESSAGE_TYPE, MessageType.DYNAMIC_CONTENT.getApiName());
        data.put(SynerisePayloadKeys.CONTENT, BANNER_CONTENT);
    }

    private void initCustomBannerData() {
        data = new ArrayMap<>();
        data.put(SynerisePayloadKeys.ISSUER, SYNERISE_ISSUER);
        data.put(SynerisePayloadKeys.CONTENT_TYPE, ContentType.CUSTOMIZABLE_BANNER.getApiName());
        data.put(SynerisePayloadKeys.MESSAGE_TYPE, MessageType.DYNAMIC_CONTENT.getApiName());
        data.put(SynerisePayloadKeys.CONTENT, CUSTOM_BANNER_CONTENT);
    }

    private void initFullscreenData() {
        data = new ArrayMap<>();
        data.put(SynerisePayloadKeys.ISSUER, SYNERISE_ISSUER);
        data.put(SynerisePayloadKeys.CONTENT_TYPE, ContentType.SCREEN.getApiName());
        data.put(SynerisePayloadKeys.MESSAGE_TYPE, MessageType.DYNAMIC_CONTENT.getApiName());
        data.put(SynerisePayloadKeys.CONTENT, FULLSCREEN_CONTENT);
    }

    private void parseData() throws InvalidPushException {
        boolean isPushValid = SYNERISE_ISSUER.equals(data.get(SynerisePayloadKeys.ISSUER));
        if (!isPushValid) throw new InvalidPushException();

        contentType = ContentType.getByApiName(data.get(SynerisePayloadKeys.CONTENT_TYPE));
        messageType = MessageType.getByApiName(data.get(SynerisePayloadKeys.MESSAGE_TYPE));
        contentJson = data.get(SynerisePayloadKeys.CONTENT);

        isPushValid = isPushValid && (contentType != ContentType.UNKNOWN);
        isPushValid = isPushValid && (messageType != MessageType.UNKNOWN);
        isPushValid = isPushValid && (contentJson != null);
        if (!isPushValid) throw new InvalidPushException();
    }

    // ****************************************************************************************************************************************

    private interface SynerisePayloadKeys {
        String ISSUER = "issuer";
        String MESSAGE_TYPE = "message-type";
        String CONTENT_TYPE = "content-type";
        String CONTENT = "content";
    }

    private class InvalidPushException extends Throwable {}
}
