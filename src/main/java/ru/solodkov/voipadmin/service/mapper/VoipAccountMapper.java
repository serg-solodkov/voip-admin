package ru.solodkov.voipadmin.service.mapper;

import org.mapstruct.*;
import ru.solodkov.voipadmin.domain.*;
import ru.solodkov.voipadmin.service.dto.VoipAccountDTO;

/**
 * Mapper for the entity {@link VoipAccount} and its DTO {@link VoipAccountDTO}.
 */
@Mapper(componentModel = "spring", uses = { AsteriskAccountMapper.class, DeviceMapper.class })
public interface VoipAccountMapper extends EntityMapper<VoipAccountDTO, VoipAccount> {
    @Mapping(target = "asteriskAccount", source = "asteriskAccount", qualifiedByName = "id")
    @Mapping(target = "device", source = "device", qualifiedByName = "mac")
    VoipAccountDTO toDto(VoipAccount s);
}
