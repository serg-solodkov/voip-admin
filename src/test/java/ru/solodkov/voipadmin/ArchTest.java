package ru.solodkov.voipadmin;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("ru.solodkov.voipadmin");

        noClasses()
            .that()
            .resideInAnyPackage("ru.solodkov.voipadmin.service..")
            .or()
            .resideInAnyPackage("ru.solodkov.voipadmin.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..ru.solodkov.voipadmin.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
