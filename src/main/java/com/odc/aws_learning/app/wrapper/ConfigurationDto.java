package com.odc.aws_learning.app.wrapper;

// import lombok.Getter; // Removed
// import lombok.Setter; // Removed

import java.util.Objects; // Added for equals/hashCode

// @Getter // Removed
// @Setter // Removed
public class ConfigurationDto {

    private String homepageText;
    private String homepageImageUrl;
    private String loginImageUrl;
    private String aboutText;
    private String aboutImageUrl;

    public ConfigurationDto() {
    }

    public ConfigurationDto(String homepageText, String homepageImageUrl, String loginImageUrl, String aboutText, String aboutImageUrl) {
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
        ConfigurationDto that = (ConfigurationDto) o;
        return Objects.equals(homepageText, that.homepageText) &&
               Objects.equals(homepageImageUrl, that.homepageImageUrl) &&
               Objects.equals(loginImageUrl, that.loginImageUrl) &&
               Objects.equals(aboutText, that.aboutText) &&
               Objects.equals(aboutImageUrl, that.aboutImageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homepageText, homepageImageUrl, loginImageUrl, aboutText, aboutImageUrl);
    }

    @Override
    public String toString() {
        return "ConfigurationDto{" +
               "homepageText='" + homepageText + '\'' +
               ", homepageImageUrl='" + homepageImageUrl + '\'' +
               ", loginImageUrl='" + loginImageUrl + '\'' +
               ", aboutText='" + aboutText + '\'' +
               ", aboutImageUrl='" + aboutImageUrl + '\'' +
               '}';
    }
}
