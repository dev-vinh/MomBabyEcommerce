package hcmuaf.fit.mombabyecommerce.model;

import jakarta.annotation.Nullable;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

public class CategoriesWithStock {
    private Integer id;
    private String name;
    private Integer totalStock;
    private Boolean isActive;

    public CategoriesWithStock(@ColumnName("id") @Nullable Integer id, @ColumnName("name") @Nullable String name, @ColumnName("totalStock") @Nullable Integer totalStock, @ColumnName("isActive") @Nullable Boolean isActive) {
        this.id = id;
        this.name = name;
        this.totalStock = totalStock;
        this.isActive = isActive;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(Integer totalStock) {
        this.totalStock = totalStock;
    }

 
    /** Getter cho EL/JSP: ${category.isActive} */
    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "CategoriesWithStock{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", totalStock=" + totalStock +
                ", isActive=" + isActive +
                '}';
    }
}
