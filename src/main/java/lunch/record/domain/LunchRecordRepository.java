package lunch.record.domain;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class LunchRecordRepository {

    private static Map<Long, LunchRecord> store = new ConcurrentHashMap<>();
    private static long sequence = 0L;

    private static final LunchRecordRepository instance = new LunchRecordRepository();

    public static LunchRecordRepository getInstance() {
        return instance;
    }

    public LunchRecordRepository() {
    }

    public LunchRecord save(LunchRecord lunchRecord) {
        lunchRecord.setId((int) ++sequence);
        Integer id = lunchRecord.getId();
        store.put((long) id, lunchRecord);
        return store.get((long) id);
    }

    public List<LunchRecord> findAll() throws SQLException, IOException {
        return new ArrayList<>(convert(store.values()));
    }

    public LunchRecord findById(Long id) throws SQLException, IOException {
        return convert(store.get(id));
    }

    public List<LunchRecord> findByRestaurantMenu(String restaurant, String menu) {
        List<LunchRecord> lunchRecords = new ArrayList<>();
        for (LunchRecord lunchRecord : store.values()) {
            if (Objects.equals(lunchRecord.getRestaurant(), restaurant) && Objects.equals(lunchRecord.getMenu(), menu)) {
                lunchRecords.add(lunchRecord);
            }
        }
        return lunchRecords;
    }

    public <T> void update(Integer id, String restaurant, String menu, T image, BigDecimal price, Float grade) {
        for (LunchRecord lunchRecord : store.values()) {
            if (Objects.equals(lunchRecord.getId(), id)) {
                lunchRecord.setRestaurant(restaurant);
                lunchRecord.setMenu(menu);
                lunchRecord.setImage(image);
                lunchRecord.setPrice(price);
                lunchRecord.setGrade(grade);
                lunchRecord.setUpdateAt(LocalDateTime.now());
            }
        }
    }

    public void updateAverageGradeByRestaurantMenu(Float averageGrade, String restaurant, String menu) {
        for (LunchRecord lunchRecord : store.values()) {
            if (Objects.equals(lunchRecord.getRestaurant(), restaurant) && Objects.equals(lunchRecord.getMenu(), menu)) {
                lunchRecord.setAverageGrade(averageGrade);
                lunchRecord.setUpdateAt(LocalDateTime.now());
            }
        }
    }

    public void delete(Integer id) {
        for (LunchRecord lunchRecord : store.values()) {
            if (Objects.equals(lunchRecord.getId(), id)) {
                store.remove((long) id);
            }
        }
    }

    public void deleteAll() {
        for (LunchRecord lunchRecord : store.values()) {
            store.remove((long) lunchRecord.getId());
        }
    }

    private <T> T convert(T lunchRecords) throws SQLException, IOException {
        if (lunchRecords.getClass() == LunchRecord.class) {
            LunchRecord<Blob> storedLunchRecord = (LunchRecord) lunchRecords;
            LunchRecord<String> lunchRecord = new LunchRecord<>();

            lunchRecord.setId(storedLunchRecord.getId());
            lunchRecord.setRestaurant(storedLunchRecord.getRestaurant());
            lunchRecord.setMenu(storedLunchRecord.getMenu());
            lunchRecord.setImage(new String(Base64.getEncoder().encode(storedLunchRecord.getImage().getBinaryStream().readAllBytes()), StandardCharsets.UTF_8));
            lunchRecord.setGrade(storedLunchRecord.getGrade());
            lunchRecord.setPrice(storedLunchRecord.getPrice());
            lunchRecord.setAverageGrade(storedLunchRecord.getAverageGrade());

            LocalDateTime updateAt = storedLunchRecord.getUpdateAt();
            lunchRecord.setUpdateAt(LocalDateTime.of(updateAt.getYear(), updateAt.getMonth(), updateAt.getDayOfMonth(), updateAt.getHour(), updateAt.getMinute(), updateAt.getSecond()));
            LocalDateTime createAt = storedLunchRecord.getCreateAt();
            lunchRecord.setCreateAt(LocalDateTime.of(createAt.getYear(), createAt.getMonth(), createAt.getDayOfMonth(), createAt.getHour(), createAt.getMinute(), createAt.getSecond()));

            return (T) lunchRecord;
        } else {
            Collection<LunchRecord> collection = new ArrayList<>();
            for (LunchRecord<Blob> storedLunchRecord : (Collection<LunchRecord>) lunchRecords) {
                LunchRecord<String> lunchRecord = new LunchRecord<>();

                lunchRecord.setId(storedLunchRecord.getId());
                lunchRecord.setRestaurant(storedLunchRecord.getRestaurant());
                lunchRecord.setMenu(storedLunchRecord.getMenu());
                lunchRecord.setImage(new String(Base64.getEncoder().encode(storedLunchRecord.getImage().getBinaryStream().readAllBytes()), StandardCharsets.UTF_8));
                lunchRecord.setGrade(storedLunchRecord.getGrade());
                lunchRecord.setPrice(storedLunchRecord.getPrice());
                lunchRecord.setAverageGrade(storedLunchRecord.getAverageGrade());

                LocalDateTime updateAt = storedLunchRecord.getUpdateAt();
                lunchRecord.setUpdateAt(LocalDateTime.of(updateAt.getYear(), updateAt.getMonth(), updateAt.getDayOfMonth(), updateAt.getHour(), updateAt.getMinute(), updateAt.getSecond()));
                LocalDateTime createAt = storedLunchRecord.getCreateAt();
                lunchRecord.setCreateAt(LocalDateTime.of(createAt.getYear(), createAt.getMonth(), createAt.getDayOfMonth(), createAt.getHour(), createAt.getMinute(), createAt.getSecond()));

                collection.add(lunchRecord);
            }
            return (T) collection;
        }
    }

}
