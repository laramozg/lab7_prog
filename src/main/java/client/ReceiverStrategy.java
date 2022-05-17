package client;

import utility.Answer;

import java.io.IOException;

public class ReceiverStrategy implements InteractionStrategy {


    @Override
    public StateConfiguration handleAnswer(ConsoleReader reader, InteractionClient client) throws IOException {
        Answer answer = client.getAnswer();
        switch (answer.getType()) {
            case DIALOG_STATE:
                return new StateConfiguration(answer.getMessage(), new DialogStrategy());
            case RETURN_ACTION:
                break;

        }
        return new StateConfiguration(answer.getMessage(), new ReceiverStrategy());
    }
}
