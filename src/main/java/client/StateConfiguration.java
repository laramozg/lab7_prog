package client;

public class StateConfiguration {
    private String message;
    private InteractionStrategy strategy;

    public StateConfiguration(String message, InteractionStrategy strategy){
        this.message=message;
        this.strategy=strategy;

    }

    public InteractionStrategy getStrategy() {
        return strategy;
    }

    public String getMessage() {
        return message;
    }
}
