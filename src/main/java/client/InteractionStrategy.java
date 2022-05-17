package client;

import java.io.IOException;

public interface InteractionStrategy {

    StateConfiguration handleAnswer(ConsoleReader reader, InteractionClient client) throws IOException, ClassNotFoundException;
}
