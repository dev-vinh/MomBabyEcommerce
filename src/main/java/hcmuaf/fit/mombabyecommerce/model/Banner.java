package hcmuaf.fit.mombabyecommerce.model;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import java.time.LocalDate;

public class Banner {
    Integer id;
    String imageId;
    LocalDate startDate;
    LocalDate endDate;
    String status;

    public Banner(@ColumnName("id") Integer id,
                  @ColumnName("imageId") String imageId,
                  @ColumnName("startDate") LocalDate startDate,
                  @ColumnName("endDate") LocalDate endDate,
                  @ColumnName("status") String status
    ) {
        this.id = id;
        this.imageId = imageId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
