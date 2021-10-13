package ru.solodkov.voipadmin.service.mapper;

import java.util.Set;
import org.mapstruct.*;
import ru.solodkov.voipadmin.domain.*;
import ru.solodkov.voipadmin.service.dto.VendorDTO;

/**
 * Mapper for the entity {@link Vendor} and its DTO {@link VendorDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface VendorMapper extends EntityMapper<VendorDTO, Vendor> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    VendorDTO toDtoName(Vendor vendor);

    @Named("nameSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    Set<VendorDTO> toDtoNameSet(Set<Vendor> vendor);
}
