package board.infrastructure.adapter;

import board.domain.data.Board;
import board.domain.ports.spi.BoardPersistencePort;
import board.infrastructure.dao.BoardDao;
import board.infrastructure.entity.BoardPersistence;
import board.infrastructure.mapper.BoardMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class BoardRepository implements BoardPersistencePort {

    final BoardDao dao;
    private final BoardMapper mapper;

    public BoardRepository(final BoardDao dao) {
        this.dao = dao;
        this.mapper = BoardMapper.INSTANCE;
    }

    @Override
    public Board add(Board board) throws SQLException, NoSuchMethodException {
        BoardPersistence boardPersistence = this.dao.add(mapper.domainToPersistence(board));
        return mapper.persistenceToDomain(boardPersistence);
    }

    @Override
    public void delete(String id) throws SQLException, NoSuchMethodException {
        this.dao.delete(id);
    }

    @Override
    public Board update(Board board, String id) throws SQLException, NoSuchMethodException {
        Optional<BoardPersistence> oBoardPersistence= this.dao.update(mapper.domainToPersistence(board), id);
        return oBoardPersistence.map(boardPersistence -> mapper.persistenceToDomain(boardPersistence)).orElse(null);
    }

    @Override
    public List<Board> getAll() throws SQLException, NoSuchMethodException {
        List<BoardPersistence> boardPersistenceList = this.dao.getAll();
        return mapper.persistenceListToDomainList(boardPersistenceList);
    }

    @Override
    public Board getById(String id) throws SQLException, NoSuchMethodException {
        Optional<BoardPersistence> optionalTask = this.dao.getById(id);
        return optionalTask.map(boardPersistence -> mapper.persistenceToDomain(boardPersistence)).orElse(null);
    }
}
