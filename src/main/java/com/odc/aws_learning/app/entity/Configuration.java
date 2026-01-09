package com.odc.aws_learning.app.entity;

import com.odc.aws_learning.auth.base.entity.BaseEntity;
// import lombok.AllArgsConstructor; // Removed
// import lombok.Getter; // Removed
// import lombok.NoArgsConstructor; // Removed
// import lombok.Setter; // Removed

import javax.persistence.*;
import java.util.Objects; // Added for equals/hashCode

// @AllArgsConstructor // Removed
// @NoArgsConstructor // Removed
// @Getter // Removed
// @Setter // Removed
@Entity
public class Configuration extends BaseEntity {

    @Lob
    private String homepageText;

    private String homepageImageUrl;

    private String loginImageUrl;
    @Lob
    private String aboutText;

    private String aboutImageUrl;

    public Configuration() {
        super();
    }

    public Configuration(String homepageText, String homepageImageUrl, String loginImageUrl, String aboutText, String aboutImageUrl) {
        this.homepageText = homepageText;
        this.homepageImageUrl = homepageImageUrl;
        this.loginImageUrl = loginImageUrl;
        this.aboutText = aboutText;
        this.aboutImageUrl = aboutImageUrl;
    }

    public String getHomepageText() {
        return homepageText;
    }

    public void setHomepageText(String homepageText) {
        this.homepageText = homepageText;
    }

    public String getHomepageImageUrl() {
        return homepageImageUrl;
    }

    public void setHomepageImageUrl(String homepageImageUrl) {
        this.homepageImageUrl = homepageImageUrl;
    }

    public String getLoginImageUrl() {
        return loginImageUrl;
    }

    public void setLoginImageUrl(String loginImageUrl) {
        this.loginImageUrl = loginImageUrl;
    }

    public String getAboutText() {
        return aboutText;
    }

    public void setAboutText(String aboutText) {
        this.aboutText = aboutText;
    }

    public String getAboutImageUrl() {
        return aboutImageUrl;
    }

    public void setAboutImageUrl(String aboutImageUrl) {
        this.aboutImageUrl = aboutImageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Configuration that = (Configuration) o;
        return Objects.equals(homepageText, that.homepageText) &&
               Objects.equals(homepageImageUrl, that.homepageImageUrl) &&
               Objects.equals(loginImageUrl, that.loginImageUrl) &&
               Objects.equals(aboutText, that.aboutText) &&
               Objects.equals(aboutImageUrl, that.aboutImageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), homepageText, homepageImageUrl, loginImageUrl, aboutText, aboutImageUrl);
    }

    @Override
    public String toString() {
        return "Configuration{" +
               "homepageText='" + homepageText + '\'' +
               ", homepageImageUrl='" + homepageImageUrl + '\'' +
               ", loginImageUrl='" + loginImageUrl + '\'' +
               ", aboutText='" + aboutText + '\'' +
               ", aboutImageUrl='" + aboutImageUrl + '\'' +
               ", id=" + id +
               '}';
    }
}
