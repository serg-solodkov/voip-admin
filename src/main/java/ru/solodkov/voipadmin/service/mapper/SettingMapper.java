package ru.solodkov.voipadmin.service.mapper;

import java.util.Set;
import org.mapstruct.*;
import ru.solodkov.voipadmin.domain.*;
import ru.solodkov.voipadmin.service.dto.SettingDTO;

/**
 * Mapper for the entity {@link Setting} and its DTO {@link SettingDTO}.
 */
@Mapper(componentModel = "spring", uses = { OptionMapper.class, OptionValueMapper.class, DeviceMapper.class })
public interface SettingMapper extends EntityMapper<SettingDTO, Setting> {
    @Mapping(target = "option", source = "option", qualifiedByName = "code")
    @Mapping(target = "selectedValues", source = "selectedValues", qualifiedByName = "idSet")
    @Mapping(target = "device", source = "device", qualifiedByName = "mac")
    SettingDTO toDto(Setting s);

    @Mapping(target = "removeSelectedValues", ignore = true)
    Setting toEntity(SettingDTO settingDTO);
}
