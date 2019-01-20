package retrivediscountmicro.retrive_discount.controll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import retrivediscountmicro.retrive_discount.service.RetriveDiscount;


@RestController
@RequestMapping("rest/v1")
public class RetrieveDiscountController {

    @Autowired
    RetriveDiscount retriveDiscount;


    @RequestMapping(value = "users/{uuid}/discounts",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity RetrieveDiscount(@PathVariable("uuid")String uuid,
                                           @RequestParam(value = "productId",defaultValue = "",required = false)String productid){

        return retriveDiscount.getRetrievedDiscound(uuid, productid);
    }

}
