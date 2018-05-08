package com.baeldung.reactive.service;

import com.baeldung.reactive.model.Tweet;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TweetService {

    Flux<Tweet> getAllTweets();
    Flux<Tweet> streemTweets();
    Mono<Tweet> getTweetById(String tweetId);
    Mono<Tweet> save(Tweet tweet);
    Mono<Void> delete(Tweet tweet);
}
