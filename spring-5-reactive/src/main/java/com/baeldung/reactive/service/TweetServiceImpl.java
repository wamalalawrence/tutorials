package com.baeldung.reactive.service;


import org.springframework.stereotype.Service;

import com.baeldung.reactive.model.Tweet;
import com.baeldung.reactive.repository.TweetRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class TweetServiceImpl implements TweetService {
    
    private final TweetRepository tweetRepository;
    
    public TweetServiceImpl(TweetRepository tweetRepository) {
        this.tweetRepository = tweetRepository;
    }
    
    @Override
    public Flux<Tweet> getAllTweets() {
        return tweetRepository.findAll();
    }
    
    @Override
    public Flux<Tweet> streemTweets() {
        return tweetRepository.findWithTailableCursorBy();
    }
    
    public Mono<Tweet> getTweetById(String tweetId) {
        return tweetRepository.findById(tweetId);
    }

    @Override
    public Mono<Tweet> save(Tweet tweet) {
        return tweetRepository.save(tweet);
    }

    @Override
    public Mono<Void> delete(Tweet tweet) {
        return tweetRepository.delete(tweet);
        
    }
    

}
