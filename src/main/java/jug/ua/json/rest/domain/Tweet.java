package jug.ua.json.rest.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author: Oleg Tsal-Tsalko
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "twitter")
public class Tweet {

    @Id
    public String id;
    @Field("id")
    public Long tweetId;
    public String text;
    public User user;

    public Tweet() {
    }

    public Tweet(String id, Long tweetId, String text, User user) {
        this.id = id;
        this.tweetId = tweetId;
        this.text = text;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "id='" + id + '\'' +
                ", tweetId=" + tweetId +
                ", text='" + text + '\'' +
                ", user=" + user +
                '}';
    }
}
