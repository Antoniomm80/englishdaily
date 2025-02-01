package com.anmoma.englishdaily.vocabulary;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DeepSeekResposeProcessorTest {
    private static final String DEEP_SEEK_RESPONSE_WITH_WORD = """
            Okay, so I need to figure out how to respond to the user's query based on the given context. The task is to extract a random term from the vocabulary bank and provide its definition along with part of speech, pronunciation, example sentence, common collocations, and synonyms. Also, I must avoid certain terms like "yonks," "epic," "crush," and "crash."
            
            First, I'll look through the provided context to identify terms that fit the categories: idiom, noun, verb, adjective, adverb, abbreviation, phrasal verb.
            
            Looking at the vocabulary bank section:
            
            - "cut down" is listed as a phrasal verb (phr v). It's defined as reducing the use of something.
            - "put on" is another phrasal verb, meaning to gain weight.
            - There are other terms like "polish," "stem from," "cut out," and "give up," all of which are phrasal verbs.
            
            The user's question asks for a random term. I'll pick one that's not in the blacklist. Let's go with "put on."
            
            Now, I need to structure this information into JSON as per the instructions. The response should include word, definition, part of speech, pronunciation, example sentence, common collocations, and synonyms.
            
            For "put on":
            - Definition: To gain weight.
            - Part of Speech: Phrasal Verb (phr v)
            - Pronunciation: /put ɒn/
            - Example Sentence: The doctor told me I should put on some weight.
            - Common Collocations: gain, weight, clothes
            - Synonyms: gain, add
            
            I need to make sure the JSON is correctly formatted and only includes this information without any markdown. Also, avoid using any of the blacklisted terms.
            
            So, putting it all together in JSON format.
            </think>
            
            ```json
            {
              "word": "put on",
              "definition": "to gain weight",
              "part_of_speech": "phr v",
              "pronunciation": "/put ɒn/",
              "example_sentence": "The doctor told me I should put on some weight.",
              "common_collocations": ["gain", "weight", "clothes"],
              "synonyms": ["gain", "add"]
            }
            ```
            """;

    private static final String DEEP_SEEK_RESPONSE_WITH_TERM = """
            <think>
            Okay, so I need to help the user by extracting a term from the vocabulary bank provided in the context. The task is to pick one random term and provide its definition along with part of speech, pronunciation, example sentence, and any relevant collocations or synonyms.
            
            First, I'll look through the vocabulary bank section. It lists several terms with their parts of speech and definitions. Let's go through each one:
            
            - **epic**: This is an adjective meaning fantastic or really fun. The pronunciation is /ˈepɪk/, and an example sentence is "The holiday was epic!" Collocations might include "fun," "fantastic." Synonyms could be "awesome" or "incredible."
            
            - **the lads**: This is a noun referring to a group of young men. Pronunciation is /læd/. An example sentence is "The lads were all so friendly." Collocations might include "group," "men." Synonyms could be "guys" in American English.
            
            - **to go all out**: An idiom meaning to make a really big effort. Pronunciation isn't specified, but the example is "My dad went all out to make my birthday special." Synonyms might be "throwing everything into it."
            
            - **next-level**: Another adjective meaning very good or advanced. Pronunciation /ˈnɑːklevəl/. Example: "The costumes in the play were next-level!" Collocations could be "good," "advanced." Synonyms might include "excellent" or "outstanding."
            
            - **kiddie**: A noun specific to American English for a child. Pronunciation /ˈkɪdi/. Example: "It was a kiddie’s party." Synonyms would be "child" or "little one."
            
            - **wasted**: An adjective meaning very drunk or intoxicated. Pronunciation /ˈweɪstɪd/. Example: "Karl was far too wasted to drive." Collocations include "drunk," "intoxicated." Synonym could be "tipsy."
            
            - **to waffle**: A verb meaning to talk a lot without giving much information. Pronunciation /ˈwɒfl/. Example: "Zoran waffled for half an hour about his kids, but I didn’t even learn their names." Collocations include "talk," "ramble." Phrasal form is "to waffle on about sth."
            
            - **jog on**: A phrasal verb used to tell someone off. Pronunciation /dʒɒɡˈɒn/. Example: "I’m not interested! Jog on!" Synonyms might be "get lost" or "beat it."
            
            - **watch this space**: An idiom meaning to wait for news as something big is going to happen. Pronunciation isn't given, but the example is "It’s top secret right now, but watch this space!"
            
            Now, I need to pick one random term from these. Let's see:
            
            1. **epic**
            2. **the lads**
            3. **to go all out**
            4. **next-level**
            5. **kiddie**
            6. **wasted**
            7. **to waffle**
            8. **jog on**
            9. **watch this space**
            
            I'll choose "epic" as it's an adjective and has a clear meaning with examples.
            
            So, the term is "epic," an adjective. Pronunciation is /ˈepɪk/. Example sentence: "The holiday was epic!" Collocations include "fun," "fantastic." Synonyms are "awesome" or "incredible."
            </think>
            
            ```json
            {
              "term": "epic",
              "part_of_speech": "adj",
              "pronunciation": "/ˈepɪk/",
              "definition": "fantastic or really fun - The holiday was epic!",
              "example_sentence": "The holiday was epic!",
              "collocations": ["fun", "fantastic"],
              "synonyms": ["awesome", "incredible"]
            }
            ```
            """;

    private final DeepSeekResposeProcessor deepSeekResposeProcessor = new DeepSeekResposeProcessor();

    @Test
    @DisplayName("Respuesta de DeepSeek con json con campo word debe devolver el objecto correctamente formado")
    void givenDeepSeekResponseWithJsonPayloadThatHaveWordFieldShouldReturnVocabularyTermFullyPopulated() {
        VocabularyTerm vocabularyTerm = deepSeekResposeProcessor.process(DEEP_SEEK_RESPONSE_WITH_WORD);
        assertThat(vocabularyTerm.word()).isEqualTo("put on");
        assertThat(vocabularyTerm.definition()).isEqualTo("to gain weight");
        assertThat(vocabularyTerm.partOfSpeech()).isEqualTo("phr v");
        assertThat(vocabularyTerm.pronunciation()).isEqualTo("/put ɒn/");
        assertThat(vocabularyTerm.exampleSentence()).isEqualTo("The doctor told me I should put on some weight.");
        assertThat(vocabularyTerm.collocations()).isNull();
        assertThat(vocabularyTerm.synonyms()).containsExactly("gain", "add");
    }

    @Test
    @DisplayName("Respuesta de DeepSeek con json con campo term debe devolver el objecto correctamente formado")
    void givenDeepSeekResponseWithJsonPayloadThatHaveTermFieldShouldReturnVocabularyTermFullyPopulated() {
        VocabularyTerm vocabularyTerm = deepSeekResposeProcessor.process(DEEP_SEEK_RESPONSE_WITH_TERM);
        assertThat(vocabularyTerm.word()).isEqualTo("epic");
        assertThat(vocabularyTerm.definition()).isEqualTo("fantastic or really fun - The holiday was epic!");
        assertThat(vocabularyTerm.partOfSpeech()).isEqualTo("adj");
        assertThat(vocabularyTerm.pronunciation()).isEqualTo("/ˈepɪk/");
        assertThat(vocabularyTerm.exampleSentence()).isEqualTo("The holiday was epic!");
        assertThat(vocabularyTerm.collocations()).containsExactly("fun", "fantastic");
        assertThat(vocabularyTerm.synonyms()).containsExactly("awesome", "incredible");

    }
}