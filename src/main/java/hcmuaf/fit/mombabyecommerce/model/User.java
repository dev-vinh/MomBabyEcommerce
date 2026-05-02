package hcmuaf.fit.mombabyecommerce.model;

import jakarta.annotation.Nullable;
import org.jdbi.v3.core.mapper.Nested;
import org.jdbi.v3.core.mapper.reflect.ColumnName;
import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;

import java.io.Serializable;
import java.time.LocalDate;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String fullName;
    private String displayName;
    private LocalDate dOB;
    private String gender;
    private String phoneNumber;
    private String email;
    private String passwordUsername;
    private Integer avatarId;
    private String status;
    @Nested("r")
    private Role role;
    private String salt;
    private String avatarUrl;
    private String googleId;
    private String provider;
    // xem
    private String confirmationToken;
    private Boolean needRefresh;

    @JdbiConstructor
    public User(@ColumnName("id") Integer id,
                @ColumnName("fullName") @Nullable String fullName,
                @ColumnName("displayName") @Nullable String displayName,
                @ColumnName("dOB") @Nullable LocalDate dOB,
                @ColumnName("gender") @Nullable String gender,
                @ColumnName("phoneNumber") @Nullable String phoneNumber,
                @ColumnName("email") @Nullable String email,
                @ColumnName("passwordUserName") @Nullable String passwordUsername,
                @ColumnName("avatarId") @Nullable Integer avatarId,
                @ColumnName("status") @Nullable String status,
                @ColumnName("salt") @Nullable String salt,
                @ColumnName("avatar_url") @Nullable String avatarUrl,
                @ColumnName("confirmationToken") @Nullable String confirmationToken,
                @ColumnName("google_id") @Nullable String googleId,
                @ColumnName("provider") @Nullable String provider,
                @ColumnName("needRefresh") @Nullable Boolean needRefresh

    ) {
        this.id = id;
        this.fullName = fullName;
        this.displayName = displayName;
        this.dOB = dOB;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.passwordUsername = passwordUsername;
        this.avatarId = avatarId;
        this.status = status;
        this.salt = salt;
        this.avatarUrl = avatarUrl;
        this.confirmationToken = confirmationToken;
        this.googleId = googleId;
        this.provider = provider;
        this.needRefresh = needRefresh;
    }

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public LocalDate getdOB() {
        return dOB;
    }

    public void setdOB(LocalDate dOB) {
        this.dOB = dOB;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordUsername() {
        return passwordUsername;
    }

    public void setPasswordUsername(String passwordUsername) {
        this.passwordUsername = passwordUsername;
    }

    public Integer getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(Integer avatarId) {
        this.avatarId = avatarId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    public Boolean getNeedRefresh() {
        return needRefresh;
    }

    public void setNeedRefresh(Boolean needRefresh) {
        this.needRefresh = needRefresh;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", displayName='" + displayName + '\'' +
                ", dOB=" + dOB +
                ", gender='" + gender + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", passwordUsername='" + passwordUsername + '\'' +
                ", avatarId=" + avatarId +
                ", status='" + status + '\'' +
                ", role='" + role + '\'' +
                ", salt='" + salt + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", googleId='" + googleId + '\'' +
                ", provider='" + provider + '\'' +
                ", confirmationToken='" + confirmationToken + '\'' +
                ", needRefresh=" + needRefresh +
                '}';
    }
}