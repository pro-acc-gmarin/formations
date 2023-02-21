package user.infrastructure.dao.spi;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IDao<A, B>{
    A add(A object) throws SQLException;

    void delete(B id);

    Optional<A> update(A object, B id) throws SQLException;

    List<A> getAll() throws SQLException;

    Optional<A> getById(B id);
}
