package org.magnum.dataup;

import com.jayway.jsonpath.JsonPath;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.magnum.dataup.model.Video;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;

/**
 * Created by vigi on 8/10/2014.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VideoControllerTestIT {

    private final RestTemplate restTemplate = new TestRestTemplate();

    @AfterClass
    public static void cleanup() throws IOException {
        FileUtils.deleteDirectory(new File("videos"));
    }

    @Test
    public void should_add_a_video() throws Exception {
        // even though it is not a good practice, for this class we assume that the test methods
        // will be executed in the ascending order of their lexicographic names
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String videoJson = "{\"id\":0,\"title\":\"hodor\",\"duration\":123,\"location\":null,\"subject\":\"hodor hodor\",\"contentType\":\"video/mp4\"}";
        HttpEntity<String> entity = new HttpEntity<>(videoJson, headers);
        Video added = restTemplate.postForObject("http://localhost:8080/video", entity, Video.class);

        assertThat(added.getId(), is(1L));
    }

    @Test
    public void should_get_all_videos_and_read_them_as_Strings() {
        String json = restTemplate.getForObject("http://localhost:8080/video", String.class);
        List<Video> videos = JsonPath.read(json, "$");

        assertThat(videos, hasSize(1));
        assertThat((Integer) JsonPath.read(json, "$.[0].id"), is(1));
        assertThat((String) JsonPath.read(json, "$.[0].title"), is("hodor"));
        assertThat((Integer) JsonPath.read(json, "$.[0].duration"), is(123));
        assertThat((String) JsonPath.read(json, "$.[0].subject"), is("hodor hodor"));
        assertThat((String) JsonPath.read(json, "$.[0].contentType"), is("video/mp4"));
    }

    @Test
    public void should_get_all_videos_and_read_them_as_objects() {
        List<Map<String, Object>> videos = restTemplate.getForObject("http://localhost:8080/video", List.class);

        assertThat(videos, hasSize(1));
        assertThat((Integer) videos.get(0).get("id"), is(1));
        assertThat((String) videos.get(0).get("title"), is("hodor"));
        assertThat((Integer) videos.get(0).get("duration"), is(123));
        assertThat((String) videos.get(0).get("subject"), is("hodor hodor"));
        assertThat((String) videos.get(0).get("contentType"), is("video/mp4"));
    }

    @Test
    public void get_video_data_for_nonexistent_video_should_give_a_404() {
        ResponseEntity<String> entity = restTemplate.getForEntity("http://localhost:8080/video/-1/data", String.class);
        assertThat(entity.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void should_upload_video_data() throws IOException {
        ResponseEntity<String> noDataEntity = restTemplate.getForEntity("http://localhost:8080/video/1/data", String.class);
        assertThat(noDataEntity.getStatusCode(), is(HttpStatus.NOT_FOUND));

        ResponseEntity<String> uploadResult = uploadDataForVideo(1L);
        assertThat(uploadResult.getStatusCode(), is(HttpStatus.OK));
        assertThat((String) JsonPath.read(uploadResult.getBody(), "$.state"), is("READY"));

        ResponseEntity<String> entity = restTemplate.getForEntity("http://localhost:8080/video/1/data", String.class);
        assertThat(entity.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void upload_video_data_for_non_existent_video_should_return_a_404() {
        ResponseEntity<String> uploadResult = uploadDataForVideo(-1L);
        assertThat(uploadResult.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    private ResponseEntity<String> uploadDataForVideo(Long videoId) {
        Resource resource = new FileSystemResource("src/test/resources/test.mp4");
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("Content-Type", "video/mp4");
        parts.add("data", resource);
        return restTemplate.postForEntity("http://localhost:8080/video/{id}/data", parts, String.class, videoId);
    }

}


