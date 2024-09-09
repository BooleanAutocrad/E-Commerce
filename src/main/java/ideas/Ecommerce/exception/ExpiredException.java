package ideas.Ecommerce.exception;

import lombok.Getter;

@Getter
public class ExpiredException extends RuntimeException{
    private final String ResourceName;

    public ExpiredException(String ResourceName) {
        super(ResourceName + " has Expired");
        this.ResourceName = ResourceName;
    }

}
