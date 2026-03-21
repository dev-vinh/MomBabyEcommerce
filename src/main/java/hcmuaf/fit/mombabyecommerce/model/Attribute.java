package hcmuaf.fit.mombabyecommerce.model;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

public class Attribute {
    Integer id;
    String categoryId;
    String name;

    public Attribute(@ColumnName("id") Integer id,
                     @ColumnName("categoryId") String categoryId,
                     @ColumnName("name") String name) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
    }


}
