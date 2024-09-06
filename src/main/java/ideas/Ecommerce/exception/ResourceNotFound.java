package ideas.Ecommerce.exception;

import lombok.Getter;

@Getter
public class ResourceNotFound extends RuntimeException{
    private final String ResourceName;

    public ResourceNotFound(String ResourceName) {
        super(ResourceName + " not deleted");
        this.ResourceName = ResourceName;
    }

}
