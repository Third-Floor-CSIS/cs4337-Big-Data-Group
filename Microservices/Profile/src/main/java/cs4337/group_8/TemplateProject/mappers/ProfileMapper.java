package cs4337.group_8.TemplateProject.mappers;

import cs4337.group_8.TemplateProject.DTO.ProfileDTO;
import cs4337.group_8.TemplateProject.entities.ProfileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProfileMapper {
    // The purpose of a mapper is to convert from a DTO to an entity and vice versa
    ProfileMapper INSTANCE = Mappers.getMapper(ProfileMapper.class);

    ProfileEntity fromDto(ProfileDTO dto);
    ProfileDTO toDto(ProfileEntity entity);
}
