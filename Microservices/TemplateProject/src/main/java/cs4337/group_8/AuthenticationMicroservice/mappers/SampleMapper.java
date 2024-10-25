package cs4337.group_8.AuthenticationMicroservice.mappers;

import cs4337.group_8.AuthenticationMicroservice.DTO.SampleDTO;
import cs4337.group_8.AuthenticationMicroservice.entities.SampleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SampleMapper {
    // The purpose of a mapper is to convert from a DTO to an entity and vice versa
    SampleMapper INSTANCE = Mappers.getMapper(SampleMapper.class);

    SampleEntity fromDto(SampleDTO dto);
    SampleDTO toDto(SampleEntity entity);
}
