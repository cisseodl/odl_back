package com.odc.aws_learning.auth.base.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public interface CEntity extends Comparable<CEntity>, Serializable, Cloneable{
    Long getId();

    String getCreatedBy();

    String getLastModifiedBy();

    LocalDateTime getCreatedAt();

    LocalDateTime getLastModifiedAt();

    void setId(Long id);

    void setCreatedBy(String createdBy);

    void setLastModifiedBy(String lastModifiedBy);

    void setCreatedAt(LocalDateTime createdAt);

    void setLastModifiedAt(LocalDateTime lastModifiedAt);


    @Override
    default int compareTo(CEntity o) {
        if (o.getCreatedAt() == null || getCreatedAt() == null) return 0;
        return getCreatedAt().compareTo(o.getCreatedAt());
    }
}
