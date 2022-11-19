package com.base.service.entity;

import com.base.commons.utils.Context;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 98858978867892073L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "created_by", updatable = false, length = 50)
    private String createdBy;

    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updated_at", nullable = false)
    protected LocalDateTime updatedAt;

    @Version
    @Column(name = "version")
    protected Long version = 0L;

    @Transient private String previousUpdatedBy;

    @PostLoad
    private void setPreviousUpdatedBy() {
        this.previousUpdatedBy = this.updatedBy;
    }

    private boolean isUpdatedByModified() {
        return !Objects.equals(this.updatedBy, this.previousUpdatedBy);
    }

    @PrePersist
    protected void onCreate() {
        this.updatedAt = this.createdAt = Objects.isNull(this.createdAt) ? LocalDateTime.now() : this.createdAt;
        this.updatedBy =
                this.createdBy = StringUtils.isNotBlank(createdBy) ? this.createdBy : Context.getContextInfo().getId();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = isUpdatedByModified() ? this.updatedBy : Context.getContextInfo().getId();
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = StringUtils.isNotBlank(createdBy) ? createdBy : Context.getContextInfo().getId();
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = StringUtils.isNotBlank(updatedBy) ? updatedBy : Context.getContextInfo().getId();
    }
}
