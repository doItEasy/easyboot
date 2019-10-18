package com.github.doiteasy.easyboot.plus.ddd.event;



import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public abstract class DomainEvent implements Serializable {
    private static final long serialVersionUID = 5740150436439366761L;
    private String eventId = UUID.randomUUID().toString();
    protected String entityNo;
    protected LocalDateTime createdDate = LocalDateTime.now();
    protected LocalDateTime lastModifiedDate = LocalDateTime.now();
    protected String createdBy;
    protected String lastModifiedBy;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DomainEvent{");
        sb.append("eventId='").append(eventId).append('\'');
        sb.append(", entityNo='").append(entityNo).append('\'');
        sb.append(", createdDate=").append(createdDate);
        sb.append(", lastModifiedDate=").append(lastModifiedDate);
        sb.append(", createdBy='").append(createdBy).append('\'');
        sb.append(", lastModifiedBy='").append(lastModifiedBy).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
