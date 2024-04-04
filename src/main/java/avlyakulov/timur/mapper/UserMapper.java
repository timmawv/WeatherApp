package avlyakulov.timur.mapper;

import avlyakulov.timur.dto.UserDto;
import avlyakulov.timur.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "id", target = "userId")
    UserDto mapUserToUserDto(User user);
}