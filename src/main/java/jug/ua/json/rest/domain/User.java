package jug.ua.json.rest.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author: Oleg Tsal-Tsalko
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    public String name;
    @Field("followers_count")
    public Integer followersNumber;

    public User() {
    }

    public User(String name, Integer followersNumber) {
        this.name = name;
        this.followersNumber = followersNumber;
    }

    @Override
    public String toString() {
        return "User {\n" +
                "\tname : \"" + name + "\",\n" +
                "\tfollowersNumber : " + followersNumber + "\n" +
                "}";
    }
}
