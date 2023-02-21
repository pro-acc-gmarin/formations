package board.infrastructure.adapter;

import board.domain.data.Board;
import board.domain.ports.spi.BoardPersistencePort;
import board.infrastructure.dao.BoardDao;
import board.infrastructure.entity.BoardPersistence;
import board.infrastructure.mapper.BoardMapper;

import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class BoardRepository implements BoardPersistencePort {

    BoardDao repository;
    private BoardMapper mapper;

    public BoardRepository() throws NamingException {
        this.repository = new board.infrastructure.dao.BoardDao();
        this.mapper = BoardMapper.INSTANCE;
    }

    @Override
    public Board add(Board board) throws SQLException {
        BoardPersistence boardPersistence = this.repository.add(mapper.domainToPersistence(board));
        return mapper.persistenceToDomain(boardPersistence);
    }

    @Override
    public void delete(String id) {
        this.repository.delete(id);
    }

    @Override
    public Board update(Board board, String id) throws SQLException {
        Optional<BoardPersistence> oBoardPersistence= this.repository.update(mapper.domainToPersistence(board), id);
        if(oBoardPersistence.isPresent()){
            return mapper.persistenceToDomain(oBoardPersistence.get());
        }
        return null;
    }

    @Override
    public List<Board> getAll() throws SQLException {
        List<BoardPersistence> boardPersistenceList = this.repository.getAll();
        return mapper.persistenceListToDomainList(boardPersistenceList);
    }

    @Override
    public Board getById(String id) {
        Optional<BoardPersistence> optionalTask = this.repository.getById(id);
        if(optionalTask.isPresent()){
            return mapper.persistenceToDomain(optionalTask.get());
        }
        return null;
    }
}
