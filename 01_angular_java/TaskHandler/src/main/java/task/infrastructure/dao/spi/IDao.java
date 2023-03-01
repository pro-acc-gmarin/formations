package task.infrastructure.dao.spi;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IDao<A, B>{
    A add(A object) throws SQLException, NoSuchMethodException;

    void delete(B id) throws SQLException, NoSuchMethodException;

    Optional<A> update(A object, B id) throws SQLException, NoSuchMethodException;

    List<A> getAll() throws SQLException, NoSuchMethodException;

    Optional<A> getById(B id) throws SQLException, NoSuchMethodException;
}
