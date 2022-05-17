package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.database.UserDatabase;
import utility.Answer;
import utility.AnswerType;
import utility.Request;
import utility.exceptions.AuthorizationException;
import utility.exceptions.DatabaseElementError;

import java.sql.SQLException;

public class AuthorizationStrategy implements RequestHandlerStrategy {
    private Logger log = LoggerFactory.getLogger(MainServer.class);

    @Override
    public Answer handle(Request request) {
        log.info("Авторизация пользователя: " + request.getUserData().getLogin());
        return executeRequest(request);
    }

    private Answer executeRequest(Request request) {
        Answer answer;
        try {
            UserDatabase.getInstance().checkUserPassword(request.getUserData());
            answer = new Answer(AnswerType.SUCCESSFULLY, "");
        } catch (DatabaseElementError | AuthorizationException | SQLException e) {
            answer = new Answer(AnswerType.UNSUCCESSFULLY, e.getMessage());
        }
        return answer;
    }
}
