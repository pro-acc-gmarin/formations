package board.infrastructure.adapter;

import board.domain.data.Board;
import board.domain.ports.spi.BoardPersistencePort;
import board.infrastructure.entity.BoardPersistence;
import board.infrastructure.mapper.BoardMapper;
import board.infrastructure.spi.BoardDaoSpi;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class BoardRepository implements BoardPersistencePort {

    private final BoardDaoSpi dao;
    private final BoardMapper mapper;

    public BoardRepository(final BoardDaoSpi dao) {
        this.dao = dao;
        this.mapper = BoardMapper.INSTANCE;
    }

    @Override
    public Board add(final Board board) throws SQLException, NoSuchMethodException {
        final BoardPersistence boardPersistence = this.dao.add(mapper.domainToPersistence(board));
        return mapper.persistenceToDomain(boardPersistence);
    }

    @Override
    public void delete(final String id) throws SQLException, NoSuchMethodException {
        this.dao.delete(id);
    }

    @Override
    public Board update(final Board board, final String id) throws SQLException, NoSuchMethodException {
        final Optional<BoardPersistence> oBoardPersistence= this.dao.update(mapper.domainToPersistence(board), id);
        return oBoardPersistence.map(mapper::persistenceToDomain).orElse(null);
    }

    @Override
    public List<Board> getAll() throws SQLException, NoSuchMethodException {
        final List<BoardPersistence> boardPersistenceList = this.dao.getAll();
        return mapper.persistenceListToDomainList(boardPersistenceList);
    }

    @Override
    public Board getById(final String id) throws SQLException, NoSuchMethodException {
        final Optional<BoardPersistence> optionalTask = this.dao.getById(id);
        return optionalTask.map(mapper::persistenceToDomain).orElse(null);
    }
}
