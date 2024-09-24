package ideas.Ecommerce.exception;

import lombok.Getter;

@Getter
public class ResourceNotFound extends RuntimeException{
    private String ResourceName;

    public ResourceNotFound(String ResourceName) {
        super(ResourceName + " not found");
        this.ResourceName = ResourceName;
    }

}
