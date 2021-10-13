package ru.solodkov.voipadmin.service.mapper;

import org.mapstruct.*;
import ru.solodkov.voipadmin.domain.*;
import ru.solodkov.voipadmin.service.dto.DeviceDTO;

/**
 * Mapper for the entity {@link Device} and its DTO {@link DeviceDTO}.
 */
@Mapper(componentModel = "spring", uses = { DeviceModelMapper.class, ResponsiblePersonMapper.class })
public interface DeviceMapper extends EntityMapper<DeviceDTO, Device> {
    @Mapping(target = "model", source = "model", qualifiedByName = "name")
    @Mapping(target = "responsiblePerson", source = "responsiblePerson", qualifiedByName = "lastName")
    @Mapping(target = "parent", source = "parent", qualifiedByName = "id")
    DeviceDTO toDto(Device s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DeviceDTO toDtoId(Device device);

    @Named("mac")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "mac", source = "mac")
    DeviceDTO toDtoMac(Device device);
}
