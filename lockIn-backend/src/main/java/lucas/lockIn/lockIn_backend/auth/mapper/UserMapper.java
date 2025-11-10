package lucas.lockIn.lockIn_backend.auth.mapper;

import lucas.lockIn.lockIn_backend.auth.dto.response.UserResponse;
import lucas.lockIn.lockIn_backend.auth.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface UserMapper {

    User toUser(UserResponse userResponse);
    UserResponse toUserResponse(User user);
}
