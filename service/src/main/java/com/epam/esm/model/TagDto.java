package com.epam.esm.model;

import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Objects;

public class TagDto extends RepresentationModel<TagDto> implements Serializable {

    private int id;
    @NotEmpty(message = "constraint.tag.name")
    private String name;

    public TagDto() {
    }

    public TagDto(String name) {
        this.name = name;
    }

    public TagDto(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        TagDto tag = (TagDto) o;
        return id == tag.id
                && Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name);
    }

}