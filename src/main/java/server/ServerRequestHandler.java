package server;

import utility.*;
import server.database.UserDatabase;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerRequestHandler {
    ExecutorService pool = Executors.newCachedThreadPool();

    public ServerRequestHandler() throws SQLException {
        UserDatabase.getInstance().createTableIfNotExist();
    }

    public void handle() {
    InteractionServer server = new InteractionServer();
    Request request = server.getRequest();
    pool.execute(()-> {
        try {
            server.sendAnswer(getAnswer(request));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    });
    }

    private Answer getAnswer(Request request) throws SQLException {
        RequestHandlerStrategy answerHandler = null;
        switch (request.getRequestType()) {
            case COMMAND_EXECUTION:
                answerHandler = new CommandExecuteStrategy();
                break;
            case REGISTRATION_REQUEST:
                answerHandler = new RegistrationStrategy();
                break;
            case AUTHORIZATION_REQUEST:
                answerHandler = new AuthorizationStrategy();
                break;
        }
        return answerHandler.handle(request);
    }
}
