package com.sau.project2.Service;

import java.util.List;
import com.sau.project2.Entity.Person;

public interface PersonService {

    Person save(Person person);

    List<Person> getAll();

    Person update(Person person);

    void delete(Long id);

    Person getById(Long id);

}
