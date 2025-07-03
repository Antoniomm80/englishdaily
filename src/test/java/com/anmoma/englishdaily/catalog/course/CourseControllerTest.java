package com.anmoma.englishdaily.catalog.course;

import com.anmoma.englishdaily.IntegrationTest;
import com.teketik.test.mockinbean.MockInBean;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class CourseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockInBean(CourseController.class)
    private CreateCourse createCourse;

    @Test
    @DisplayName("Post to course creation endpoint should create a course")
    void givenPostToEndPointShouldCreateCourse() throws Exception {
        mockMvc.perform(post("/api/v1/englishdaily/courses").contentType(MediaType.APPLICATION_JSON)
                                                           .content(
                                                                   "{\"title\": \"New Course\", \"folderPath\": \"/path/to/course\", \"vocabularySupported\": true}"))
               .andExpect(status().isOk());

        then(createCourse).should()
                         .create(new CreateCourseCommand("New Course", "/path/to/course", true));
    }
}