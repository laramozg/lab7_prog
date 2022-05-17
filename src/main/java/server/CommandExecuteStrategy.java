package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.commands.ActionInvoker;
import server.database.UserDatabase;
import utility.Answer;
import utility.AnswerType;
import utility.Request;
import utility.exceptions.DatabaseElementError;
import utility.exceptions.ExecuteScriptException;
import utility.exceptions.IncorrectArgumentException;
import utility.exceptions.IncorrectCommandException;
import utility.interaction.Command;

import java.sql.SQLException;

public class CommandExecuteStrategy implements RequestHandlerStrategy {
    private Logger log = LoggerFactory.getLogger(MainServer.class);

    @Override
    public Answer handle(Request request) throws SQLException {
        UserDatabase.getInstance().checkUserPassword(request.getUserData());
        Command command = request.getCommand();
        return executeCommand(command);
    }

    private Answer executeCommand(Command command) {
        Answer answer;
        log.info("Обработка команды: " + command.getName());
        try {
            answer = ActionInvoker.getInstance().execute(command);
        } catch (IncorrectCommandException | IncorrectArgumentException | DatabaseElementError |
                ExecuteScriptException e) {
            answer = new Answer(AnswerType.RETURN_ACTION, e.getMessage());
            log.error("Команда " + command.getName() + " не выполнена. Ошибка: " + e.getMessage() + " Аргумент: " + command.getParameter());
        } catch (IllegalArgumentException e) {
            answer = new Answer(AnswerType.RETURN_ACTION, "Аргумент команды некорректен!");
            log.error("Аргумент команды " + command.getName() + " некорректен. Введено: " + command.getParameter());
        }
        return answer;
    }

}
