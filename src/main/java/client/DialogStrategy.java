package client;

import utility.Answer;
import utility.Request;
import utility.RequestType;
import utility.element.Worker;
import utility.exceptions.ExitException;
import utility.interaction.Command;

import java.io.IOException;

public class DialogStrategy implements InteractionStrategy {
    @Override
    public StateConfiguration handleAnswer(ConsoleReader reader, InteractionClient client) throws IOException {
        Command command = reader.readCommand();
        Request request=new Request(RequestType.COMMAND_EXECUTION,command);
        client.sendRequest(request);
        Answer answer = client.getAnswer();
        switch (answer.getType()) {
            case EXIT:
                throw new ExitException("Exit");
            case RETURN_ACTION:
                if (!answer.getMessage().equals("")) {
                    return new StateConfiguration(answer.getMessage(),new DialogStrategy());
                }
                break;
            case NEED_ELEMENT:
                Worker worker = reader.readElement();
                command.setElement(worker);
                request=new Request(RequestType.COMMAND_EXECUTION,command);
                client.sendRequest(request);
                answer = client.getAnswer();
                return new StateConfiguration(answer.getMessage(),new DialogStrategy());
            case GETTER_STATE:
                return new StateConfiguration("",new ReceiverStrategy());
        }
        return new StateConfiguration(answer.getMessage(),new DialogStrategy());
    }
}
