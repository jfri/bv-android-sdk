package com.bazaarvoice.bvandroidsdk;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockWebServer;
import okio.BufferedSource;
import okio.Okio;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricGradleTestRunner.class)
@Config(shadows = {Shadows.ShadowNetwork.class, Shadows.BvShadowAsyncTask.class, Shadows.ShadowAdIdClient.class})
public class BVAuthenticatedUserTest {

    BVAuthenticatedUser subject;
    String baseurl;
    String shopperApiKey;
    OkHttpClient okHttpClient;
    Gson gson;
    List<Integer> profilePollTimes;
    MockWebServer server;

    @Before
    public void setup() throws Exception {
        initMocks(this);
        server = new MockWebServer();
        server.start();
        baseurl = server.url("example.com").toString();
        shopperApiKey = "testString123";
        okHttpClient = new OkHttpClient();
        gson = new Gson();
        profilePollTimes = Arrays.asList(0, 10, 20, 30);
        Logger.setLogLevel(BVLogLevel.VERBOSE);
        subject = new BVAuthenticatedUser(RuntimeEnvironment.application, baseurl, shopperApiKey, okHttpClient, gson, profilePollTimes);
    }

    @Test
    public void setUserAuth() throws Exception {
        String newUserAuth = "blahbleh";
        subject.setUserAuthString(newUserAuth);
        assertEquals(newUserAuth, subject.getUserAuthString());
    }

//    @Test
//    public void updateUserEmptyResponse() throws Exception {
//        server.enqueue(new MockResponse().setResponseCode(200).setBody(getJsonFile(BVAuthenticatedUserTest.class, "empty_profile.json")));
//
//        subject.updateUser("testing");
//
//        String expectedPath = String.format("/example.com/users/magpie_idfa_%s?passkey=%s", "test_ad_id", shopperApiKey);
//
//        RecordedRequest req1 = server.takeRequest(50, TimeUnit.MILLISECONDS);
//        assertEquals(expectedPath, req1.getPath());
//        assertTrue(subject.getShopperProfile().getProfile().getTargetingKeywords().size() == 0);
//    }

//    @Test
//    public void updateUserFullResponse() throws Exception {
//        server.enqueue(new MockResponse().setResponseCode(200).setBody(getJsonFile(BVAuthenticatedUserTest.class, "full_profile.json")));
//
//        subject.updateUser("testing");
//
//        String expectedPath = String.format("/example.com/users/magpie_idfa_%s?passkey=%s", "test_ad_id", shopperApiKey);
//
//        ShadowLooper.unPauseLooper(subject.getShopperProfileHandlerThread().getLooper());
//        RecordedRequest req1 = server.takeRequest(50, TimeUnit.MILLISECONDS);
//        assertEquals(expectedPath, req1.getPath());
//        assertTrue(subject.getShopperProfile().getProfile().getTargetingKeywords().size() == 3);
//    }


    private static String getJsonFile(Class clazz, String fileName) throws IOException {
        InputStream in = clazz.getClassLoader().getResourceAsStream(fileName);
        BufferedSource bufferedSource = Okio.buffer(Okio.source(in));
        return bufferedSource.readString(Charset.defaultCharset());
    }

}