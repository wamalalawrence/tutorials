package com.baeldung.reactive.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baeldung.reactive.model.Tweet;
import com.baeldung.reactive.service.TweetService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

import javax.validation.Valid;

@RestController
public class TweetController {
   
    private final com.baeldung.reactive.service.TweetService tweetService;

    public TweetController(TweetService tweetService) {
        this.tweetService = tweetService;
    }
    
 // Tweets to be sent to the client from server every 2 seconds
    @GetMapping(value = "/stream/tweets", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    Flux<Tweet> streemTweets() {
        return tweetService.streemTweets().delayElements(Duration.ofMillis(2000));
    }
    
    
    @GetMapping("/tweets")
    public Flux<Tweet> getAll() {
        return tweetService.getAllTweets();
    }
    
    @GetMapping("/tweets/{id}")
    public Mono<ResponseEntity<Tweet>> getTweetById(@PathVariable(value = "id") String tweetId) {
        return tweetService.getTweetById(tweetId)
                .map(savedTweet -> ResponseEntity.ok(savedTweet))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/tweets/{id}")
    public Mono<ResponseEntity<Tweet>> updateTweet(@PathVariable(value = "id") String id,
                                                   @Valid @RequestBody Tweet tweet) {
        return tweetService.getTweetById(id)
                .flatMap(existingTweet -> {
                    existingTweet.setText(tweet.getText());
                    return tweetService.save(existingTweet);
                })
                .map(updatedTweet -> new ResponseEntity<>(updatedTweet, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @PostMapping("/tweets")
    public Mono<Tweet> create(@Valid @RequestBody Tweet tweet) {
        return tweetService.save(tweet);
    }
    
    @DeleteMapping("/tweets/{id}")
    public Mono<ResponseEntity<Void>> deleteTweet(@PathVariable(value = "id") String id) {
        
        return tweetService.getTweetById(id)
                .flatMap(existingTweet ->
                tweetService.delete(existingTweet)
                            .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                );
//                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/")
    Mono<String> home() {
        return Mono.just("tweets");
    }



}
