package org.ocean.authservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.ocean.authservice.entity.User;
import org.ocean.authservice.responses.AdminUserNamesResponseDto;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AdminUsernamesResponseMapper{

    @Mapping(source = "uuid",target = "uuid",qualifiedByName = "format_uuid")
    List<AdminUserNamesResponseDto> toAdminUsersNamesResponseDto(List<User> users);

    @Named("format_uuid")
    default String format_uuid(UUID uuid){
        return uuid.toString();
    }
}
