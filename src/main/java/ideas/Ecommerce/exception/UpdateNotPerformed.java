package ideas.Ecommerce.exception;

import lombok.Getter;

@Getter
public class UpdateNotPerformed extends RuntimeException{
    private final String ErrorMessage;

    public UpdateNotPerformed(String ErrorMessage) {
        super(ErrorMessage + " Something went wrong");
        this.ErrorMessage = ErrorMessage;
    }
}
