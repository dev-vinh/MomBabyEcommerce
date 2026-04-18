package hcmuaf.fit.mombabyecommerce.model;

import org.jdbi.v3.core.mapper.reflect.ColumnName;
import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;

import java.io.Serializable;

public class Attribute implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String categoryId;
    private String name;

    public Attribute() {}

    @JdbiConstructor
    public Attribute(@ColumnName("id") Integer id,
                     @ColumnName("categoryId") String categoryId,
                     @ColumnName("name") String name) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
