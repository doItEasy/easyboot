package com.github.doiteasy.easyboot.plus.ddd.support;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;


@Component
@Scope("prototype")
public abstract class Entity<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    protected T entityNo;

    protected LocalDateTime createdDate;

    protected LocalDateTime lastModifiedDate;

    protected String createdBy;

    protected String lastModifiedBy;

    public static enum EntityStatus {
        NEW, PERSISTED, ARCHIVE
    }

    private EntityStatus entityStatus = EntityStatus.PERSISTED;

    public void markAsRemoved() {
        entityStatus = EntityStatus.ARCHIVE;
    }

    public void markAsNew() {
        entityStatus = EntityStatus.NEW;
    }

    public void markAsPersisted() {
        entityStatus = EntityStatus.PERSISTED;
    }

    public boolean isRemoved() {
        return entityStatus == EntityStatus.ARCHIVE;
    }

    public boolean isPersisted() {
        return entityStatus == EntityStatus.PERSISTED;
    }

    public T getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(T entityNo) {
        this.entityNo = entityNo;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public EntityStatus getEntityStatus() {
        return entityStatus;
    }

    public void setEntityStatus(EntityStatus entityStatus) {
        this.entityStatus = entityStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Entity entity = (Entity) o;

        return new EqualsBuilder()
            .append(entityNo, entity.entityNo)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(entityNo)
            .toHashCode();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Entity{");
        sb.append("entityNo=").append(entityNo);
        sb.append(", createdDate=").append(createdDate);
        sb.append(", lastModifiedDate=").append(lastModifiedDate);
        sb.append(", createdBy='").append(createdBy).append('\'');
        sb.append(", lastModifiedBy='").append(lastModifiedBy).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
