package retrivediscountmicro.retrive_discount.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Data
public class PruchasedProduct {

     String productId;
     String description;
     BigDecimal originalPrice;
     BigDecimal finalPrice;
     Discount discountInformation;


}
