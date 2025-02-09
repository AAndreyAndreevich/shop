package app.services;

import app.dtos.InventoryDTO;
import app.dtos.ProductDTO;
import app.entities.Inventory;
import app.entities.Product;
import app.entities.Shop;
import app.handlers.NotFoundException;
import app.repositories.InventoryRepository;
import app.repositories.ProductRepository;
import app.repositories.ShopRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);
    private final ProductRepository productRepo;
    private final ShopRepository shopRepo;
    private final InventoryRepository invRepo;

    @Autowired
    public InventoryService(ProductRepository productRepo, ShopRepository shopRepo, InventoryRepository invRepo) {
        this.productRepo = productRepo;
        this.shopRepo = shopRepo;
        this.invRepo = invRepo;
    }

    public List<InventoryDTO> getAllProducts(Long shopId) {
        List<Inventory> inventories = invRepo.findByShopId(shopId);
        if (inventories.isEmpty()) {
            log.warn("Продукты не найдены");
            return List.of();
        }

        return inventories.stream()
                .map(inventory -> {
                    Product product = inventory.getProduct();
                    if (product != null) {
                        ProductDTO productDTO = new ProductDTO(product.getId(), product.getName(), product.getPrice());
                        return new InventoryDTO(inventory.getId(), shopId, productDTO, inventory.getQuantity());
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Transactional
    public String addProductToInventory(Long shopId, Long productId, Integer count) {
        Shop shop = shopRepo.findById(shopId)
                .orElseThrow(() -> new NotFoundException("Магазин не найден"));
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new NotFoundException("Продукт не найден"));

        Optional<Inventory> optionalInventory = invRepo.findByShopIdAndProductId(shopId, productId);
        Inventory inventory = optionalInventory.orElseGet(() -> createNewInventory(shop, product));

        inventory.setQuantity(inventory.getQuantity() + count);
        invRepo.save(inventory);
        log.info("Магазин '{}' добавил '{}' в количестве {} штук на склад", shop.getName(), product.getName(), count);
        return "Товар успешно добавлен в инвентарь";
    }

    @Transactional
    public String removeProductFromInventory(Long shopId, Long productId, Integer count) {
        Shop shop = shopRepo.findById(shopId)
                .orElseThrow(() -> new NotFoundException("Магазин не найден"));
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new NotFoundException("Продукт не найден"));

        Inventory inventory = invRepo.findByShopIdAndProductId(shopId, productId)
                .orElseThrow(() -> new NotFoundException("Инвентарь не найден"));

        if (inventory.getQuantity() < count) {
            return "Недостаточно товара на складе";
        }

        inventory.setQuantity(inventory.getQuantity() - count);
        invRepo.save(inventory);
        log.info("Магазин '{}' удалил '{}' в количестве {} штук со склада", shop.getName(), product.getName(), count);
        return "Товар успешно удален из инвентаря";
    }

    private Inventory createNewInventory(Shop shop, Product product) {
        Inventory inventory = new Inventory();
        inventory.setShop(shop);
        inventory.setProduct(product);
        inventory.setQuantity(0);
        return invRepo.save(inventory);
    }
}
