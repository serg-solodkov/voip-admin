package ru.solodkov.voipadmin.service.mapper;

import org.mapstruct.*;
import ru.solodkov.voipadmin.domain.*;
import ru.solodkov.voipadmin.service.dto.ResponsiblePersonDTO;

/**
 * Mapper for the entity {@link ResponsiblePerson} and its DTO {@link ResponsiblePersonDTO}.
 */
@Mapper(componentModel = "spring", uses = { DepartmentMapper.class })
public interface ResponsiblePersonMapper extends EntityMapper<ResponsiblePersonDTO, ResponsiblePerson> {
    @Mapping(target = "department", source = "department", qualifiedByName = "name")
    ResponsiblePersonDTO toDto(ResponsiblePerson s);

    @Named("lastName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "secondName", source = "secondName")
    ResponsiblePersonDTO toDtoLastName(ResponsiblePerson responsiblePerson);
}
