package org.ocean.authservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.ocean.authservice.dto.UserProfileDao;
import org.ocean.authservice.entity.User;
import org.ocean.authservice.entity.UserProfile;
import org.ocean.authservice.utils.GenericMapper;


@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserProfileMapper extends GenericMapper<UserProfile, UserProfileDao> {

    @Mapping(source = "lineManager",target = "line_manager",qualifiedByName = "format_lineManager")
    UserProfileDao toDto(UserProfile entity);

    @Named("format_lineManager")
    default String formatLineManager(User lineManager) {
        return (lineManager !=null && lineManager.getUsername()!=null) ? lineManager.getUsername() : "";
    }
}
