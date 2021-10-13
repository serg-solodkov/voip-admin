package ru.solodkov.voipadmin.service.mapper;

import org.mapstruct.*;
import ru.solodkov.voipadmin.domain.*;
import ru.solodkov.voipadmin.service.dto.OtherDeviceTypeDTO;

/**
 * Mapper for the entity {@link OtherDeviceType} and its DTO {@link OtherDeviceTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OtherDeviceTypeMapper extends EntityMapper<OtherDeviceTypeDTO, OtherDeviceType> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OtherDeviceTypeDTO toDtoId(OtherDeviceType otherDeviceType);
}
