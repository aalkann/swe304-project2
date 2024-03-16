package com.sau.project2.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.sau.project2.Entity.Person;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {

    
}
