package jug.ua.json.rest.repository;

import jug.ua.json.rest.domain.Person;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * @author Oleg Tsal-Tsalko
 */
@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface PersonRepository extends MongoRepository<Person, String> {

    List<Person> findByLastName(@Param("name") String name);

    @RestResource(path = "user", rel = "user")
    Person findByFirstNameAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);

}