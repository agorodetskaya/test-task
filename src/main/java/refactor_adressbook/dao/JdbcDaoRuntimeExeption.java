package refactor_adressbook.dao;

public class JdbcDaoRuntimeExeption extends RuntimeException {
    public JdbcDaoRuntimeExeption(Throwable cause) {
        super(cause);
    }

    public JdbcDaoRuntimeExeption(String message, Throwable cause) {
        super(message, cause);
    }
}
