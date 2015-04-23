package jug.ua.json.rest.repository;

import jug.ua.json.rest.domain.Tweet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * @author Oleg Tsal-Tsalko
 */
@RepositoryRestResource(collectionResourceRel = "tweets", path = "tweets")
public interface TweetRepository extends MongoRepository<Tweet, String> {

    @RestResource(path = "byUser", rel = "byUser")
    List<Tweet> findByUserName(@Param("user") String userName);

}