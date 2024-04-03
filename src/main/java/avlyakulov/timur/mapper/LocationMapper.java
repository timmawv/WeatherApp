package avlyakulov.timur.mapper;

import avlyakulov.timur.dto.LocationDto;
import avlyakulov.timur.model.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LocationMapper {
    LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);

    @Mapping(source = "userId", target = "user.id")
    Location mapLocationDtoToLocation(LocationDto locationDto);
}