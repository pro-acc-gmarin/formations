package user.domain.ports.api;

import user.domain.data.User;
import user.domain.ports.spi.UserPersistencePort;

import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements UserServicePort{

    private final UserPersistencePort userPersistencePort;

    public UserServiceImpl(final UserPersistencePort userPersistencePort) {
        this.userPersistencePort = userPersistencePort;
    }

    public User add(final User user) throws SQLException, NoSuchMethodException {
        return this.userPersistencePort.add(user);
    }

    public void delete(final String id) throws SQLException, NoSuchMethodException {
        this.userPersistencePort.delete(id);
    }

    public User update(final User user, final String id) throws SQLException, NoSuchMethodException {
        return this.userPersistencePort.update(user, id);
    }

    public List<User> getAll() throws SQLException, NoSuchMethodException {
        return this.userPersistencePort.getAll();
    }

    public User getById(final String id) throws SQLException, NoSuchMethodException {
        return this.userPersistencePort.getById(id);
    }
}
