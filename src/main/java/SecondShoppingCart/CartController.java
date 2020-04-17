package SecondShoppingCart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Random;

@Controller
public class CartController {
    private final Logger logger = LoggerFactory.getLogger(CartController.class);
    private final Cart cart;

    @Autowired
    ProductDao productDao;

    public CartController(Cart cart) {
        this.cart = cart;
    }

    @RequestMapping("/addtocart")
    @ResponseBody
    public String addtocart() {
        Random rand = new Random();
        cart.addToCart(new CartItem(rand.nextInt(5), new Product(rand.nextLong(), "prod" + rand.nextInt(10), rand.nextDouble()*100)));
        return "addtocart";
    }

    @RequestMapping("/cart")
    @ResponseBody
    public String cart(){
        cart.getCartItems().forEach(cartItem -> logger.info("product: {}, quantity: {}", cartItem.getProduct().getName(),
                cartItem.getQuantity()));
        return cart.toString();
    }

    @RequestMapping("/products")
    @ResponseBody
    public String products(){return productDao.getList().toString();}


    @GetMapping(value = "/addtocartfromlist", produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public void addtocartfromlist(@RequestParam String id, @RequestParam String quantity){
        List<Product> productList = productDao.getList();
        Product product = productList.stream()
                .filter(product1 -> product1.getId().equals(Long.valueOf(id)))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

        cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(product.getId()))
                .findFirst()
                .ifPresentOrElse(cartItem -> cartItem.setQuantity(cartItem.getQuantity() + Integer.valueOf(quantity)),
                        () -> cart.getCartItems().add(new CartItem(Integer.valueOf(quantity), product)));
    }

    @ResponseBody
    @GetMapping(value = "/cart2", produces = "text/plain;charset=UTF-8")
    String cart2() {
        return String.format("W koszyku jest %s pozycji." + "\n" +
                "W koszyku jest %s produktów" + "\n" +
                "Wartość koszyka to: %s ", cart.getCartItems().size(), cart.getCartItems().stream().mapToInt(CartItem::getQuantity).sum(),
                cart.getCartItems().stream().mapToDouble(cartItem -> cartItem.getQuantity() * cartItem.getProduct().getPrice()).sum());
    }
}
