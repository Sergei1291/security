package com.epam.esm.entity;

import javax.persistence.*;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "gift_certificate")
public class GiftCertificate {

    @PrePersist
    public void onPrePersist() {
        String createDateAudit = createTime();
        setCreateDate(createDateAudit);
        setLastUpdateDate(null);
        setIsDeleted(false);
    }

    @PreUpdate
    public void onPreUpdate() {
        String lastUpdateDateAudit = createTime();
        setLastUpdateDate(lastUpdateDateAudit);
    }

    private String createTime() {
        DateTimeFormatter dateTimeFormatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.ms");
        ZoneOffset zoneOffset = ZoneOffset.UTC;
        DateTimeFormatter dateTimeFormatterZone = dateTimeFormatter.withZone(zoneOffset);
        return dateTimeFormatterZone.format(Instant.now());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "price")
    private int price;
    @Column(name = "duration")
    private int duration;
    @Column(name = "create_date")
    private String createDate;
    @Column(name = "last_update_date")
    private String lastUpdateDate;
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "gift_certificate_tag",
            joinColumns = @JoinColumn(name = "certificate"),
            inverseJoinColumns = @JoinColumn(name = "tag"))
    private List<Tag> tags;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "giftCertificate")
    List<Order> orders;

    public GiftCertificate() {
    }

    public GiftCertificate(String name) {
        this.name = name;
    }

    public GiftCertificate(int id,
                           String name,
                           String description,
                           int price,
                           int duration,
                           String createDate,
                           String lastUpdateDate,
                           boolean isDeleted,
                           List<Tag> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.isDeleted = isDeleted;
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

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GiftCertificate that = (GiftCertificate) o;
        return id == that.id
                && price == that.price
                && duration == that.duration
                && isDeleted == that.isDeleted
                && Objects.equals(name, that.name)
                && Objects.equals(description, that.description)
                && Objects.equals(createDate, that.createDate)
                && Objects.equals(lastUpdateDate, that.lastUpdateDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, duration,
                createDate, lastUpdateDate, isDeleted);
    }

}