package jug.ua.json.rest;

import jug.ua.json.rest.domain.Person;
import jug.ua.json.rest.repository.PersonRepository;
import jug.ua.json.rest.repository.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author Oleg Tsal-Tsalko
 */
@Component
public class Initializer implements CommandLineRunner{

    @Autowired
    private PersonRepository repository;

    @Autowired
    private TweetRepository tweets;

    @Override
    public void run(String... strings) throws Exception {
        repository.deleteAll();
        repository.save(new Person("Oleg", "Tsal-Tsalko"));
        repository.save(new Person("Lena", "Syrota"));
    }
}
