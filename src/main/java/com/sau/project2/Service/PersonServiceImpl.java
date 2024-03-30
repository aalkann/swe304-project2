package com.sau.project2.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sau.project2.Entity.Person;
import com.sau.project2.Repository.PersonRepository;

@Service
public class PersonServiceImpl implements PersonService{

    @Autowired
    private PersonRepository personRepository;

    @Override
    public void delete(Long id) {
        personRepository.deleteById(id);
    }

    @Override
    public Person getById(Long id) {
        return  personRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Person> getAll() {
        return (List<Person>) personRepository.findAll();
    }

    @Override
    public Person save(Person person) {
        return personRepository.save(person);
    }

    @Override
    public Person update(Person person) {
        return personRepository.save(person);
    }
    
        
}
