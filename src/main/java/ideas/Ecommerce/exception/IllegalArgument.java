package ideas.Ecommerce.exception;

import lombok.Getter;

@Getter
public class IllegalArgument extends RuntimeException{
    private final String ArgumentName;

    public IllegalArgument(String ArgumentName) {
        super(ArgumentName + " is Invalid");
        this.ArgumentName = ArgumentName;
    }
}
