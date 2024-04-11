package avlyakulov.timur.dao;

import java.util.List;

public interface AbstractDao<T> {

    void create(T t);

    List<T> findAll();
}
