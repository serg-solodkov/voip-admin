package ru.solodkov.voipadmin.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ru.solodkov.voipadmin.domain.ResponsiblePerson} entity.
 */
public class ResponsiblePersonDTO implements Serializable {

    private Long id;

    @NotNull
    private String code;

    @NotNull
    private String firstName;

    private String secondName;

    @NotNull
    private String lastName;

    private String position;

    private String room;

    private DepartmentDTO department;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public DepartmentDTO getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentDTO department) {
        this.department = department;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResponsiblePersonDTO)) {
            return false;
        }

        ResponsiblePersonDTO responsiblePersonDTO = (ResponsiblePersonDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, responsiblePersonDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResponsiblePersonDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", secondName='" + getSecondName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", position='" + getPosition() + "'" +
            ", room='" + getRoom() + "'" +
            ", department=" + getDepartment() +
            "}";
    }
}
