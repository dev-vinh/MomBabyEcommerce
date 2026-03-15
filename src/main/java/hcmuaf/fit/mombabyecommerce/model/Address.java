package hcmuaf.fit.mombabyecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.Nullable;
import org.jdbi.v3.core.mapper.reflect.ColumnName;
import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Address {
    Integer id;
    Integer userId;
    String addressType; // shipping | billing
    String fullName;
    String phoneNumber;
    String street;
    String city;
    String state;
    String country;
    Boolean isDefault;

    @JdbiConstructor
    public Address(@ColumnName("id") Integer id,
                   @ColumnName("userId") @Nullable Integer userId,
                   @ColumnName("addressType") @Nullable String addressType,
                   @ColumnName("fullName") @Nullable String fullName,
                   @ColumnName("phoneNumber") @Nullable String phoneNumber,
                   @ColumnName("street") @Nullable String street,
                   @ColumnName("city") @Nullable String city,
                   @ColumnName("state") @Nullable String state,
                   @ColumnName("country") @Nullable String country,
                   @ColumnName("isDefault") @Nullable Boolean isDefault) {
        this.id = id;
        this.userId = userId;
        this.addressType = addressType;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.street = street;
        this.city = city;
        this.state = state;
        this.country = country;
        this.isDefault = isDefault;
    }

    public Address() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", userId=" + userId +
                ", addressType='" + addressType + '\'' +
                ", fullName='" + fullName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", isDefault=" + isDefault +
                '}';
    }
}
