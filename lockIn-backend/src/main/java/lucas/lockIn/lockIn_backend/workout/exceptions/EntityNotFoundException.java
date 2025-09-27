package lucas.lockIn.lockIn_backend.workout.exceptions;

public class EntityNotFoundException extends RuntimeException {

    private final String objectName;
    private final String identifier;
    private final String identifierType;

    public EntityNotFoundException(String objectName, Object identifier){
        this(objectName, identifier, "id");
    }

    public EntityNotFoundException(String objectName, Object identifier, String identifierType){
        super(String.format("%s not found with %s: %s", objectName, identifierType, identifier));
        this.objectName = objectName;
        this.identifier = String.valueOf(identifier);
        this.identifierType = identifierType;
    }
}
