package org.magnum.dataup;

import com.jayway.jsonassert.JsonAssert;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by vigi on 8/10/2014.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class VideoControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private final File testVideoFile = new File("src/test/resources/test.mp4");

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @AfterClass
    public static void cleanup() throws IOException {
        FileUtils.deleteDirectory(new File("videos"));
    }

    @Test
    public void should_add_a_video_metadata() throws Exception {
        ResultActions resultActions = performAddVideo("lotr", "fantasy", 567);
        int videoId = parseVideoId(resultActions);

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id").value(videoId))
                .andExpect(jsonPath("$.title").value("lotr"))
                .andExpect(jsonPath("$.subject").value("fantasy"))
                .andExpect(jsonPath("$.duration").value(567))
                .andExpect(jsonPath("$.dataUrl")
                        .value(String.format("http://localhost/video/%s/data", videoId)));
    }

    @Test
    public void should_get_all_videos() throws Exception {
        int videoId1 = parseVideoId(performAddVideo("title1", "subject1", 111));
        int videoId2 = parseVideoId(performAddVideo("title2", "subject2", 222));

        MvcResult resultActions = mockMvc.perform(get("/video")).andReturn();
        String json = resultActions.getResponse().getContentAsString();

        JsonAssert.with(json)
                .assertThat("$", is(JsonAssert.collectionWithSize(greaterThan(2))))
                .assertThat("$..id", hasItems(videoId1, videoId2))
                .assertThat("$..title", hasItems("title1", "title2"))
                .assertThat("$..duration", hasItems(111, 222))
                .assertThat("$..subject", hasItems("subject1", "subject2"));
    }

    @Test
    public void should_upload_video_data() throws Exception {
        int videoId = parseVideoId(performAddVideo());

        mockMvc.perform(fileUpload("/video/{id}/data", videoId)
                .file(fileToUpload()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("READY"));

        byte[] originalFile = IOUtils.toByteArray(new FileInputStream(testVideoFile));
        MvcResult downloadResult = mockMvc.perform(get("/video/{id}/data", videoId)).andReturn();
        byte[] downloadedFile = downloadResult.getResponse().getContentAsByteArray();

        assertThat(originalFile, equalTo(downloadedFile));
    }

    @Test
    public void should_not_be_able_to_add_video_data_for_a_nonexistent_video() throws Exception {
        mockMvc.perform(fileUpload("/video/{id}/data", -1)
                .file(fileToUpload()))
                .andExpect(status().is(404));
    }

    private MockMultipartFile fileToUpload() throws IOException {
        return new MockMultipartFile("data", "test.mp4", "video/mp4",
                new FileInputStream(testVideoFile));
    }

    @Test
    public void get_non_existent_video_data_should_return_a_404_not_found() throws Exception {
        mockMvc.perform(get("/video/{id}/data", -2))
                .andExpect(status().is(404));
    }

    private ResultActions performAddVideo(String title, String subject, int duration) throws Exception {
        String jsonTemplate = "{\"id\":0,\"title\":\"%s\",\"duration\":%s,\"location\":null,\"subject\":\"%s\",\"contentType\":\"video/mp4\"}";
        return mockMvc.perform(post("/video")
                .content(String.format(jsonTemplate, title, duration, subject))
                .contentType(MediaType.APPLICATION_JSON));
    }

    private ResultActions performAddVideo() throws Exception {
        // use some default values for video metadata
        return performAddVideo("test_title", "test_subject", 123);
    }

    private int parseVideoId(ResultActions addedVideoAction) throws Exception {
        String json = addedVideoAction.andReturn().getResponse().getContentAsString();
        return JsonPath.read(json, "$.id");
    }
}