package org.ocean.authservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.ocean.authservice.dao.UserProfileDao;
import org.ocean.authservice.entity.UserProfile;
import org.ocean.authservice.utils.GenericMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserProfileMapper extends GenericMapper<UserProfile, UserProfileDao> {
}
