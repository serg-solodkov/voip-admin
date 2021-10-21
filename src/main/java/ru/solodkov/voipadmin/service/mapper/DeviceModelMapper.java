package ru.solodkov.voipadmin.service.mapper;

import java.util.Set;
import org.mapstruct.*;
import ru.solodkov.voipadmin.domain.*;
import ru.solodkov.voipadmin.service.dto.DeviceModelDTO;

/**
 * Mapper for the entity {@link DeviceModel} and its DTO {@link DeviceModelDTO}.
 */
@Mapper(componentModel = "spring", uses = { OtherDeviceTypeMapper.class, VendorMapper.class, OptionMapper.class })
public interface DeviceModelMapper extends EntityMapper<DeviceModelDTO, DeviceModel> {
    @Mapping(target = "otherDeviceType", source = "otherDeviceType", qualifiedByName = "id")
    @Mapping(target = "vendor", source = "vendor", qualifiedByName = "name")
    @Mapping(target = "options", source = "options", qualifiedByName = "codeSet")
    DeviceModelDTO toDto(DeviceModel s);

    @Mapping(target = "removeOptions", ignore = true)
    DeviceModel toEntity(DeviceModelDTO deviceModelDTO);

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "isConfigurable", source = "isConfigurable")
    @Mapping(target = "linesCount", source = "linesCount")
    DeviceModelDTO toDtoName(DeviceModel deviceModel);
}
