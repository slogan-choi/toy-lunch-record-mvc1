package lunch.record.servlet.domain;

import java.math.BigDecimal;
import java.sql.Blob;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LunchRecordRepository {

    private static Map<Long, LunchRecord> store = new HashMap<>();
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

    public List<LunchRecord> findAll() {
        return new ArrayList<>(store.values());
    }

    public LunchRecord findById(Long id) {
        return store.get(id);
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

    public void update(Integer id, String restaurant, String menu, Blob image, BigDecimal price, Float grade) {
        for (LunchRecord lunchRecord : store.values()) {
            if (Objects.equals(lunchRecord.getId(), id)) {
                lunchRecord.setRestaurant(restaurant);
                lunchRecord.setMenu(menu);
                lunchRecord.setImage(image);
                lunchRecord.setPrice(price);
                lunchRecord.setGrade(grade);
                lunchRecord.setUpdateAt(LocalTime.now());
            }
        }
    }

    public void updateAverageGradeByRestaurantMenu(Float averageGrade, String restaurant, String menu) {
        for (LunchRecord lunchRecord : store.values()) {
            if (Objects.equals(lunchRecord.getRestaurant(), restaurant) && Objects.equals(lunchRecord.getMenu(), menu)) {
                lunchRecord.setAverageGrade(averageGrade);
                lunchRecord.setUpdateAt(LocalTime.now());
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

}
