package com.baeldung.reactive;

import java.util.Collections;

import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.baeldung.reactive.controller.TweetController;
import com.baeldung.reactive.model.Tweet;
import com.baeldung.reactive.repository.TweetRepository;

import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

  private static WebTestClient webTestClient;
    
    @Autowired
    TweetRepository tweetRepository;
    
    @Autowired
    private TweetController tweetController;
    
    private SoftAssertions assertions;
    
    @Before
    public void setUp() throws Exception {
        assertions = new SoftAssertions();
        webTestClient = WebTestClient.bindToController(tweetController)
            .build();
    }
    
    @Test
    public void testCreateTweet() {
            Tweet tweet = new Tweet("Hi there, this is tweet 1");
    
            webTestClient.post()
            .uri("/tweets")
            .body(Mono.just(tweet), Tweet.class)
            .exchange()
            .expectStatus()
            .is2xxSuccessful()
            .expectBody()
            .jsonPath("$.id").isNotEmpty()
            .jsonPath("$.text").isEqualTo("Hi there, this is tweet 1")
            ;
    }
    
    @Test
    public void testGetAllTweets() {
        webTestClient.get()
        .uri("/tweets")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBodyList(Tweet.class);
    }
    
    @Test
    public void testGetSingleTweet() {
        Tweet tweet = tweetRepository.save(new Tweet("Hello, World!")).block();
    
        webTestClient.get()
                .uri("/tweets/{id}", Collections.singletonMap("id", tweet.getId()))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response ->
                assertions.assertThat(response.getResponseBody()).isNotNull());
    }
    
    @Test
    public void testUpdateTweet() {
        Tweet tweet = tweetRepository.save(new Tweet("Initial Tweet")).block();
    
        Tweet newTweetData = new Tweet("Updated Tweet");
    
        webTestClient.put()
                .uri("/tweets/{id}", Collections.singletonMap("id", tweet.getId()))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(newTweetData), Tweet.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.text").isEqualTo("Updated Tweet");
    }
    
//    @Test
//    public void testDeleteTweet() {
//            Tweet tweet = tweetRepository.save(new Tweet("To be deleted")).block();
//    
//            webTestClient.delete()
//                .uri("/tweets/{id}", Collections.singletonMap("id",  tweet.getId()))
//                .exchange()
//                .expectStatus().isOk();
//    }
    
    @After
    public void finish() {
        assertions.assertAll();
    }
    
}
