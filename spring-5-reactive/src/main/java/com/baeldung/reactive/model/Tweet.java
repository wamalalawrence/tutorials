package com.baeldung.reactive.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@Document(collection = "tweets")
public class Tweet {

    @Id
    private String id;

    @NonNull
    private String text;

}
