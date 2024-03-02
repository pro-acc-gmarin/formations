package user.infrastructure.adapter;

import user.domain.data.User;
import user.domain.ports.spi.UserPersistencePort;
import user.infrastructure.entity.UserPersistence;
import user.infrastructure.mapper.UserMapper;
import user.infrastructure.spi.UserDaoSpi;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserRepository implements UserPersistencePort {

    private final UserDaoSpi repository;
    private final UserMapper mapper;

    public UserRepository(final UserDaoSpi userDao) {
        this.repository = userDao;
        this.mapper = UserMapper.INSTANCE;
    }

    @Override
    public User add(final User user) throws SQLException, NoSuchMethodException {
        final UserPersistence userPersistence = this.repository.add(mapper.domainToPersistence(user));
        return mapper.persistenceToDomain(userPersistence);
    }

    @Override
    public void delete(final String id) throws SQLException, NoSuchMethodException {
        this.repository.delete(id);
    }

    @Override
    public User update(final User user, final String id) throws SQLException, NoSuchMethodException {
        final Optional<UserPersistence> oUserPersistence = this.repository.update(mapper.domainToPersistence(user), id);
        return oUserPersistence.map(mapper::persistenceToDomain).orElse(null);
    }

    @Override
    public List<User> getAll() throws SQLException, NoSuchMethodException {
        final List<UserPersistence> userPersistenceList = this.repository.getAll();
        return mapper.persistenceListToDomainList(userPersistenceList);
    }

    @Override
    public User getById(final String id) throws SQLException, NoSuchMethodException {
        final Optional<UserPersistence> optionalUser = this.repository.getById(id);
        return optionalUser.map(mapper::persistenceToDomain).orElse(null);
    }
}
