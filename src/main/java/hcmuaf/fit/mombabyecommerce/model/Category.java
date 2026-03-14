package hcmuaf.fit.mombabyecommerce.model;


import org.jdbi.v3.core.mapper.reflect.ColumnName;
import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;

public class Category {
    Integer id;
    String name;
    Boolean isActive;

    @JdbiConstructor
    public Category(@ColumnName("id") Integer id,
                    @ColumnName("name")  String name,
                    @ColumnName("isActive") Boolean isActive) {
        this.id = id;
        this.name = name;
        this.isActive = isActive;
    }

    public Category() {
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

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
