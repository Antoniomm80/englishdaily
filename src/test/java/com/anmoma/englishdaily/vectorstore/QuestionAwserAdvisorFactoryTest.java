package com.anmoma.englishdaily.vectorstore;

import com.anmoma.englishdaily.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class QuestionAwserAdvisorFactoryTest {
    @Autowired
    private QuestionAwserAdvisorFactory questionAwserAdvisorFactory;

    @Test
    @DisplayName("Factory method debe devolver un QuestionAwserAdvisor con la query dada y valores por defecto")
    void givenQuestionAwserAdvisorFactoryShouldReturnQuestionAwserAdvisor() {
        QuestionAnswerAdvisor advisorWithQuery = questionAwserAdvisorFactory.createAdvisorWithQuery("What is the capital of France?");
        SearchRequest searchRequest = (SearchRequest) ReflectionTestUtils.getField(advisorWithQuery, "searchRequest");
        assertThat(searchRequest.getQuery()).isEqualTo("What is the capital of France?");
        assertThat(searchRequest.getTopK()).isEqualTo(6);
        assertThat(searchRequest.getSimilarityThreshold()).isEqualTo(.7);
        assertThat(advisorWithQuery).isNotNull();

    }
}