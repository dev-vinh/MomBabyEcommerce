package hcmuaf.fit.mombabyecommerce.model;

import org.jdbi.v3.core.mapper.reflect.ColumnName;
import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;

import java.io.Serializable;

public class CartDB implements Serializable {

    private Integer id;
    private Integer userId;
    private String sessionId;
    private String status;

    public CartDB() {
    }
    @JdbiConstructor
    public CartDB(
            @ColumnName("id") Integer id,
            @ColumnName("userId") Integer userId,
            @ColumnName("sessionId") String sessionId,
            @ColumnName("status") String status
    ) {
        this.id = id;
        this.userId = userId;
        this.sessionId = sessionId;
        this.status = status;
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

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
