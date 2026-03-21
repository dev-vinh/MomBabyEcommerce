package hcmuaf.fit.mombabyecommerce.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

public class Image {
    @JsonProperty("id")
    Integer id;
    @JsonProperty("url")
    String url;

    public Image(@ColumnName("id") Integer id, @ColumnName("url") String url) {
        this.id = id;
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

