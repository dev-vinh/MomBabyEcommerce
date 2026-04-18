package hcmuaf.fit.mombabyecommerce.model;

import jakarta.annotation.Nullable;
import org.jdbi.v3.core.mapper.reflect.ColumnName;
import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;

import java.io.Serializable;

public class Brand implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private Boolean active;

    public Brand() {}

    @JdbiConstructor
    public Brand(@ColumnName("id") Integer id,
                 @ColumnName("name") String name,
                 @ColumnName("isActive") Boolean active) {
        this.id = id;
        this.name = name;
        this.active = active;
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
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Brand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", active=" + active +
                '}';
    }
}

