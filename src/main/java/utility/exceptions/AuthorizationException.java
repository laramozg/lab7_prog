package utility.exceptions;

public class AuthorizationException extends RuntimeException{
    public AuthorizationException(String massage){
        super(massage);
    }
}
