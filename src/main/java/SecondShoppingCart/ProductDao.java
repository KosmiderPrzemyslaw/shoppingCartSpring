package SecondShoppingCart;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductDao {
    public List<Product> getList(){
        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, "piłka", 22));
        products.add(new Product(2L, "sok", 321));
        return products;
    }
}
