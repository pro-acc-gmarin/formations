package user.infrastructure.adapter;

import user.domain.data.User;
import user.domain.ports.spi.UserPersistencePort;
import user.infrastructure.dao.UserDao;
import user.infrastructure.entity.UserPersistence;
import user.infrastructure.mapper.UserMapper;

import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserRepository implements UserPersistencePort {

    UserDao repository;
    private UserMapper mapper;

    public UserRepository() throws NamingException {
        this.repository = new user.infrastructure.dao.UserDao();
        this.mapper = UserMapper.INSTANCE;
    }

    @Override
    public User add(User user) throws SQLException {
        UserPersistence userPersistence = this.repository.add(mapper.domainToPersistence(user));
        return mapper.persistenceToDomain(userPersistence);
    }

    @Override
    public void delete(String id) {
        this.repository.delete(id);
    }

    @Override
    public User update(User user, String id) throws SQLException {
        Optional<UserPersistence> oUserPersistence = this.repository.update(mapper.domainToPersistence(user), id);
        if(oUserPersistence.isPresent()){
            return mapper.persistenceToDomain(oUserPersistence.get());
        }
        return null;
    }

    @Override
    public List<User> getAll() throws SQLException {
        List<UserPersistence> userPersistenceList = this.repository.getAll();
        return mapper.persistenceListToDomainList(userPersistenceList);
    }

    @Override
    public User getById(String id) {
        Optional<UserPersistence> optionalUser = this.repository.getById(id);
        if(optionalUser.isPresent()){
            return mapper.persistenceToDomain(optionalUser.get());
        }
        return null;
    }
}
