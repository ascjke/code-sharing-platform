package ru.borisov.repository;

import org.springframework.data.repository.CrudRepository;
import ru.borisov.model.Code;

import java.util.List;

public interface CodeRepository extends CrudRepository<Code, String> {

    @Override
    List<Code> findAll();
}
