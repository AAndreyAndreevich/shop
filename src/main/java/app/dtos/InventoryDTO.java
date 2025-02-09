package app.dtos;

import jakarta.validation.constraints.Min;

public class InventoryDTO {
    private Long id;
    private Long shopId;
    private ProductDTO productDTO;
    @Min(value = 0, message = "Количество не может быть отрицательным")
    private int quantity;

    public InventoryDTO() {
    }

    public InventoryDTO(Long id, Long shopId, ProductDTO productDTO, int quantity) {
        this.id = id;
        this.shopId = shopId;
        this.productDTO = productDTO;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public ProductDTO getProductDTO() {
        return productDTO;
    }

    public void setProductDTO(ProductDTO productDTO) {
        this.productDTO = productDTO;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}