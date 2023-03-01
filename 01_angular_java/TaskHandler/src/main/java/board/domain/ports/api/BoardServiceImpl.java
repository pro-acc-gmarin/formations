package board.domain.ports.api;

import board.domain.data.Board;
import board.domain.ports.spi.BoardPersistencePort;
import task.domain.data.Task;
import task.domain.ports.api.TaskServicePort;
import task.domain.ports.spi.TaskPersistencePort;

import java.sql.SQLException;
import java.util.List;

public class BoardServiceImpl implements BoardServicePort {

    private final BoardPersistencePort boardPersistencePort;

    public BoardServiceImpl(BoardPersistencePort boardPersistencePort) {
        this.boardPersistencePort = boardPersistencePort;
    }

    public Board add(Board board) throws SQLException, NoSuchMethodException {
        return this.boardPersistencePort.add(board);
    }

    public void delete(String id) throws SQLException, NoSuchMethodException {
        this.boardPersistencePort.delete(id);
    }

    public Board update(Board board, String id) throws SQLException, NoSuchMethodException {
            return this.boardPersistencePort.update(board, id);
    }

    public List<Board> getAll() throws SQLException, NoSuchMethodException {
        return this.boardPersistencePort.getAll();
    }

    public Board getById(String id) throws SQLException, NoSuchMethodException {
        return this.boardPersistencePort.getById(id);
    }
}
