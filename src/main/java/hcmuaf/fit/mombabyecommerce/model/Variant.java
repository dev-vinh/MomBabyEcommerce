package hcmuaf.fit.mombabyecommerce.model;

import jakarta.annotation.Nullable;
import org.jdbi.v3.core.mapper.reflect.ColumnName;
import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;

public class Variant {
    Integer id;
    Integer categoryId;
    String name;
    String value;
    Integer optionId;

    @JdbiConstructor
    public Variant(@ColumnName("id") @Nullable Integer id,
                   @ColumnName("categoryId") @Nullable Integer categoryId,
                   @ColumnName("name") @Nullable String name,
                   @ColumnName("value") @Nullable String value,
                   @ColumnName("optionId") @Nullable Integer optionId
    ){
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.value = value;
        this.optionId = optionId;

    }

    public Integer getOptionId() {
        return optionId;
    }

    public void setOptionId(Integer optionId) {
        this.optionId = optionId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

