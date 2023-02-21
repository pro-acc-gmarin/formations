package user.domain.ports.spi;

import user.domain.data.User;

import java.sql.SQLException;
import java.util.List;

public interface UserPersistencePort {

    User add(User user) throws SQLException;

    void delete(String id);

    User update(User user, String id) throws SQLException;

    List<User> getAll() throws SQLException;

    User getById(String id);

}
