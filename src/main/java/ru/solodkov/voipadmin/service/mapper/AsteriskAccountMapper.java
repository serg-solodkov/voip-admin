package ru.solodkov.voipadmin.service.mapper;

import org.mapstruct.*;
import ru.solodkov.voipadmin.domain.*;
import ru.solodkov.voipadmin.service.dto.AsteriskAccountDTO;

/**
 * Mapper for the entity {@link AsteriskAccount} and its DTO {@link AsteriskAccountDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AsteriskAccountMapper extends EntityMapper<AsteriskAccountDTO, AsteriskAccount> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AsteriskAccountDTO toDtoId(AsteriskAccount asteriskAccount);
}
