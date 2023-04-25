package lunch.record.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class LunchRecord<T> {

    private Integer id;
    private String restaurant;
    private String menu;
    private T image;
    private BigDecimal price;
    private Float grade;
    private Float averageGrade;
    private LocalDateTime updateAt;
    private LocalDateTime createAt;

    public LunchRecord() {
    }

    public LunchRecord(int id, String restaurant, String menu, T image, BigDecimal price, Float grade, LocalDateTime updateAt, LocalDateTime createAt) {
        this.id = id;
        this.restaurant = restaurant;
        this.menu = menu;
        this.image = image;
        this.price = price;
        this.grade = grade;
        this.updateAt = updateAt;
        this.createAt = createAt;
    }

    public LunchRecord(String restaurant, String menu, T image, BigDecimal price, Float grade, LocalDateTime updateAt, LocalDateTime createAt) {
        this.restaurant = restaurant;
        this.menu = menu;
        this.image = image;
        this.price = price;
        this.grade = grade;
        this.updateAt = updateAt;
        this.createAt = createAt;
    }

}
