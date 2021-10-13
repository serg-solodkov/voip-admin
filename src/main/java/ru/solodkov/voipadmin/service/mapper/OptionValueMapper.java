package ru.solodkov.voipadmin.service.mapper;

import java.util.Set;
import org.mapstruct.*;
import ru.solodkov.voipadmin.domain.*;
import ru.solodkov.voipadmin.service.dto.OptionValueDTO;

/**
 * Mapper for the entity {@link OptionValue} and its DTO {@link OptionValueDTO}.
 */
@Mapper(componentModel = "spring", uses = { OptionMapper.class })
public interface OptionValueMapper extends EntityMapper<OptionValueDTO, OptionValue> {
    @Mapping(target = "option", source = "option", qualifiedByName = "id")
    OptionValueDTO toDto(OptionValue s);

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<OptionValueDTO> toDtoIdSet(Set<OptionValue> optionValue);
}
