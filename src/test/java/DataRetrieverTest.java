import com.Nj.code.Category;
import com.Nj.code.DataRetriever;
import com.Nj.code.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DataRetrieverTest {

    private DataRetriever dr;

    @BeforeEach
    void setup() {
        dr = new DataRetriever();
    }

    @Test
    void testGetAllCategories() {
        List<Category> cats = dr.getAllCategories();
        assertNotNull(cats);
        assertTrue(cats.size() >= 1, "Il doit y avoir au moins une catégorie (vérifie data.sql)");
    }

    @Test
    void testGetProductListPagination() {
        List<Product> page1 = dr.getProductList(1, 3);
        List<Product> page2 = dr.getProductList(2, 3);

        assertNotNull(page1);
        assertNotNull(page2);
        assertTrue(page1.size() <= 3);
        assertTrue(page2.size() <= 3);
    }

    @Test
    void testGetProductsByCriteriaName() {
        List<Product> res = dr.getProductsByCriteria("iPhone", null, null, null);
        assertNotNull(res);
    }

    @Test
    void testGetProductsByCriteriaCategoryAndPagination() {
        List<Product> res = dr.getProductsByCriteria(null, "Laptop", null, null, 1, 2);
        assertNotNull(res);
    }

    @Test
    void testGetProductsByCriteriaDateRange() {
        Instant min = Instant.parse("2024-02-01T00:00:00Z");
        Instant max = Instant.parse("2024-04-30T23:59:59Z");
        List<Product> res = dr.getProductsByCriteria(null, null, min, max);
        assertNotNull(res);
    }
}
