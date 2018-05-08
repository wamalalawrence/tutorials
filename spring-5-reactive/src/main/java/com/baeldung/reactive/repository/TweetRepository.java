package com.baeldung.reactive.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;

import com.baeldung.reactive.model.Tweet;

import reactor.core.publisher.Flux;


public interface TweetRepository extends ReactiveMongoRepository<Tweet, String> {

    @Tailable
    Flux<com.baeldung.reactive.model.Tweet> findWithTailableCursorBy();

}
