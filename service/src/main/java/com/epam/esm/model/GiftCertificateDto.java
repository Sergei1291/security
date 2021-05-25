package com.epam.esm.model;

import org.springframework.hateoas.RepresentationModel;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class GiftCertificateDto extends RepresentationModel<GiftCertificateDto>
        implements Serializable {

    private int id;
    @NotEmpty(message = "constraint.gift.certificate.name")
    private String name;
    private String description;
    @PositiveOrZero(message = "constraint.gift.certificate.price")
    private int price;
    @PositiveOrZero(message = "constraint.gift.certificate.duration")
    private int duration;
    private String createDate;
    private String lastUpdateDate;
    private boolean isDeleted;
    private List<@Valid TagDto> tags;

    public GiftCertificateDto() {
    }

    public GiftCertificateDto(String name) {
        this.name = name;
    }

    public GiftCertificateDto(int id,
                              String name,
                              String description,
                              int price,
                              int duration,
                              String createDate,
                              String lastUpdateDate,
                              List<TagDto> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.tags = tags;
    }

    public GiftCertificateDto(String name,
                              String description,
                              int price,
                              int duration,
                              String createDate,
                              String lastUpdateDate,
                              List<TagDto> tags) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.tags = tags;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public List<TagDto> getTags() {
        return tags;
    }

    public void setTags(List<TagDto> tags) {
        this.tags = tags;
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
        GiftCertificateDto that = (GiftCertificateDto) o;
        return id == that.id
                && isDeleted == that.isDeleted
                && Objects.equals(name, that.name)
                && Objects.equals(description, that.description)
                && Objects.equals(price, that.price)
                && Objects.equals(duration, that.duration)
                && Objects.equals(createDate, that.createDate)
                && Objects.equals(lastUpdateDate, that.lastUpdateDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, description, price,
                duration, createDate, lastUpdateDate, isDeleted);
    }

}