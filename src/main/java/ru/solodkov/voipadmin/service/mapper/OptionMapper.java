package ru.solodkov.voipadmin.service.mapper;

import java.util.Set;
import org.mapstruct.*;
import ru.solodkov.voipadmin.domain.*;
import ru.solodkov.voipadmin.service.dto.OptionDTO;

/**
 * Mapper for the entity {@link Option} and its DTO {@link OptionDTO}.
 */
@Mapper(componentModel = "spring", uses = { VendorMapper.class })
public interface OptionMapper extends EntityMapper<OptionDTO, Option> {
    @Mapping(target = "vendors", source = "vendors", qualifiedByName = "nameSet")
    @Mapping(target = "codeWithDescr", expression = "java(s.getCode() + \" (\" + s.getDescr() + \")\")")
    OptionDTO toDto(Option s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OptionDTO toDtoId(Option option);

    @Mapping(target = "removeVendors", ignore = true)
    Option toEntity(OptionDTO optionDTO);

    @Named("codeSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    Set<OptionDTO> toDtoCodeSet(Set<Option> option);

    @Named("code")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    OptionDTO toDtoCode(Option option);
}
