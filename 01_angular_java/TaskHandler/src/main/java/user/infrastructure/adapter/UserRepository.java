package user.infrastructure.adapter;

import user.domain.data.User;
import user.domain.ports.spi.UserPersistencePort;
import user.infrastructure.dao.UserDao;
import user.infrastructure.entity.UserPersistence;
import user.infrastructure.mapper.UserMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserRepository implements UserPersistencePort {

    final UserDao repository;
    private final UserMapper mapper;

    public UserRepository(final UserDao userDao) {
        this.repository = userDao;
        this.mapper = UserMapper.INSTANCE;
    }

    @Override
    public User add(User user) throws SQLException, NoSuchMethodException {
        UserPersistence userPersistence = this.repository.add(mapper.domainToPersistence(user));
        return mapper.persistenceToDomain(userPersistence);
    }

    @Override
    public void delete(String id) throws SQLException, NoSuchMethodException {
        this.repository.delete(id);
    }

    @Override
    public User update(User user, String id) throws SQLException, NoSuchMethodException {
        Optional<UserPersistence> oUserPersistence = this.repository.update(mapper.domainToPersistence(user), id);
        return oUserPersistence.map(mapper::persistenceToDomain).orElse(null);
    }

    @Override
    public List<User> getAll() throws SQLException, NoSuchMethodException {
        List<UserPersistence> userPersistenceList = this.repository.getAll();
        return mapper.persistenceListToDomainList(userPersistenceList);
    }

    @Override
    public User getById(String id) throws SQLException, NoSuchMethodException {
        Optional<UserPersistence> optionalUser = this.repository.getById(id);
        return optionalUser.map(mapper::persistenceToDomain).orElse(null);
    }
}
