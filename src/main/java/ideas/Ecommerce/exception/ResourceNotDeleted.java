package ideas.Ecommerce.exception;

import lombok.Getter;

@Getter
public class ResourceNotDeleted extends RuntimeException{
    private final String ResourceName;

    public ResourceNotDeleted(String ResourceName) {
        super(ResourceName + " not deleted");
        this.ResourceName = ResourceName;
    }
}
