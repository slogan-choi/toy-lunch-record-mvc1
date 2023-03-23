package lunch.record.servlet.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Blob;
import java.time.LocalTime;

@Data
public class LunchRecord {

    private Integer id;
    private String restaurant;
    private String menu;
    private Blob image;
    private BigDecimal price;
    private Float grade;
    private Float averageGrade;
    private LocalTime updateAt;
    private LocalTime createAt;

    public LunchRecord() {
    }

    public LunchRecord(int id, String restaurant, String menu, Blob image, BigDecimal price, Float grade, LocalTime updateAt, LocalTime createAt) {
        this.id = id;
        this.restaurant = restaurant;
        this.menu = menu;
        this.image = image;
        this.price = price;
        this.grade = grade;
        this.updateAt = updateAt;
        this.createAt = createAt;
    }

    public LunchRecord(int id, String restaurant, String menu, Blob image, BigDecimal price, Float grade) {
        this.id = id;
        this.restaurant = restaurant;
        this.menu = menu;
        this.image = image;
        this.price = price;
        this.grade = grade;
    }

    public LunchRecord(String restaurant, String menu, Blob image, BigDecimal price, Float grade, Float averageGrade) {
        this.restaurant = restaurant;
        this.menu = menu;
        this.image = image;
        this.price = price;
        this.grade = grade;
        this.averageGrade = averageGrade;
    }

    public LunchRecord(String restaurant, String menu, Blob image, BigDecimal price, Float grade, LocalTime updateAt, LocalTime createAt) {
        this.restaurant = restaurant;
        this.menu = menu;
        this.image = image;
        this.price = price;
        this.grade = grade;
        this.updateAt = updateAt;
        this.createAt = createAt;
    }

    public LunchRecord(String restaurant, String menu, Blob image, BigDecimal price, Float grade, Float averageGrade, LocalTime updateAt, LocalTime createAt) {
        this.restaurant = restaurant;
        this.menu = menu;
        this.image = image;
        this.price = price;
        this.grade = grade;
        this.averageGrade = averageGrade;
        this.updateAt = updateAt;
        this.createAt = createAt;
    }
}
