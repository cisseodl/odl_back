package com.odc.aws_learning.app.entity;

import com.odc.aws_learning.auth.base.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Configuration extends BaseEntity {

    @Lob
    private String homepageText;

    private String homepageImageUrl;

    private String loginImageUrl;
    @Lob
    private String aboutText;

    private String aboutImageUrl;


}
