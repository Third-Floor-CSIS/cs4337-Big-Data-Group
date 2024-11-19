package cs4337.group_8.NotificationService.mappers;

import cs4337.group_8.NotificationService.DTO.NotificationDTO;
import cs4337.group_8.NotificationService.entities.NotificationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NotificationMapper {

    NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

    NotificationEntity fromDto(NotificationDTO dto);
    NotificationDTO toDto(NotificationEntity entity);

}
