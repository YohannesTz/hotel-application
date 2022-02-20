package exceptions;

public class UnknownInputException extends Exception {
    public UnknownInputException(String message) {
        super(message);
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }
}
