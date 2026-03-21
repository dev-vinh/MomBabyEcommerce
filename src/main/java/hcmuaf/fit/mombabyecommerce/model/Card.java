package hcmuaf.fit.mombabyecommerce.model;

import jakarta.annotation.Nullable;
import org.jdbi.v3.core.mapper.reflect.ColumnName;
import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;

import java.time.LocalDate;

public class Card {
    Integer id;
    Integer userId;
    Integer last4;
    LocalDate duration;
    String type;
    Boolean isDefault;


    @JdbiConstructor
    public Card(
            @ColumnName("id") @Nullable Integer id,
            @ColumnName("last4") @Nullable Integer last4,
            @ColumnName("userId")  @Nullable Integer userId,
            @ColumnName("duration")  @Nullable LocalDate duration,
            @ColumnName("type")  @Nullable String type,
            @ColumnName("isDefault")  @Nullable Boolean isDefault) {

        this.id = id;
        this.userId = userId;
        this.duration = duration;
        this.type = type;
        this.isDefault = isDefault;
        this.last4 = last4;
    }


    public Card( ) {

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

    public Integer getLast4() {
        return last4;
    }

    public void setLast4(Integer last4) {
        this.last4 = last4;
    }

    public LocalDate getDuration() {
        return duration;
    }

    public void setDuration(LocalDate duration) {
        this.duration = duration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }
}
