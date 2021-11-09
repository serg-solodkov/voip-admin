package ru.solodkov.voipadmin.service.mapper;

import org.mapstruct.*;
import ru.solodkov.voipadmin.domain.*;
import ru.solodkov.voipadmin.service.dto.DeviceDTO;

/**
 * Mapper for the entity {@link Device} and its DTO {@link DeviceDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { DeviceModelMapper.class, ResponsiblePersonMapper.class, VoipAccountMapper.class, SettingMapper.class, VendorMapper.class }
)
public interface DeviceMapper extends EntityMapper<DeviceDTO, Device> {
    @Mapping(target = "model", source = "model", qualifiedByName = "name")
    @Mapping(target = "responsiblePerson", source = "responsiblePerson", qualifiedByName = "lastName")
    @Mapping(target = "parent", source = "parent", qualifiedByName = "id")
    @Mapping(target = "voipAccounts", source = "voipAccounts")
    @Mapping(source = "mac", target = "mac", qualifiedByName = "plainMacToFormatted")
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

    @Mapping(source = "mac", target = "mac", qualifiedByName = "formattedMacToPlain")
    Device toEntity(DeviceDTO deviceDTO);

    @Named("plainMacToFormatted")
    static String plainMacToFormatted(String plainMac) {
        if (plainMac.length() != 12) {
            return plainMac;
        }
        String[] octets = plainMac.split("(?<=\\G..)");
        return String.join(":", octets);
    }

    @Named("formattedMacToPlain")
    static String formattedMacToPlain(String formattedMac) {
        return formattedMac.replace(":", "").replace("-", "");
    }
}
