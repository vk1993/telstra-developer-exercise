package retrivediscountmicro.retrive_discount.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import retrivediscountmicro.retrive_discount.controll.RetrieveDiscountController;
import retrivediscountmicro.retrive_discount.domain.CustomerResourse;
import retrivediscountmicro.retrive_discount.domain.Discount;
import retrivediscountmicro.retrive_discount.domain.PruchasedProduct;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@RunWith(MockitoJUnitRunner.class)
public class RetriveDiscountImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private RetriveDiscountImpl retriveDiscountimpl ;

    @Mock
    RetriveDiscount retriveDiscount;

    @LocalServerPort
    int randomServerPort;

    String discountWithOutPid;
    String discountWithtPid;
    String userNotFoundError;

     CustomerResourse customer_Resourse ;
     List<Discount> discountList = new ArrayList<>();
     List<PruchasedProduct> pruchasedProductsList = new ArrayList<>();

     PruchasedProduct pruchasedProduct;

    String customerResourses;

    @Before
    public void setUp() throws Exception {
        restTemplate =  new RestTemplate();
        retriveDiscountimpl = new RetriveDiscountImpl();

        customer_Resourse = new CustomerResourse();

        pruchasedProduct = new PruchasedProduct();
        pruchasedProduct.setDescription("12 month subscription to 'Horse and Hound'");
        pruchasedProduct.setFinalPrice(new BigDecimal(50));
        pruchasedProduct.setOriginalPrice(new BigDecimal(100));
        pruchasedProduct.setProductId("sku-1234567890");

        Discount discount1 = new Discount("10-percent-off",
                "PERCENT",
                "Reduce the purchase price by 10%",
                new BigDecimal(10));


        Discount discount2 = new Discount(
                "5-dollars-off",
                "AMOUNT",
                "Reduce the purchase price by $5",
                new BigDecimal(5),"sku-1234567890");

        pruchasedProduct.setDiscountInformation(discount1);
        pruchasedProductsList.add(pruchasedProduct);

        discountList.add(discount1);
        discountList.add(discount2);

        customer_Resourse.setAddress("1 Main St");
        customer_Resourse.setName( "Test User");
        customer_Resourse.setUuid("qa-test-user");
        customer_Resourse.setEligibleDiscounts(discountList);
        customer_Resourse.setProducts(pruchasedProductsList);

    }

    @Test
    public void getRetrievedDiscoundWithOutPid(){

            ResponseEntity result = new ResponseEntity(discountList,HttpStatus.OK);

            System.out.println(customer_Resourse.getName());

       when(restTemplate.getForEntity("http://localhost:9999/rest/v1/customers/qa-test-user",CustomerResourse.class))
                .thenReturn(new ResponseEntity(customer_Resourse,HttpStatus.OK));
////                .thenReturn( new ResponseEntity(customerResourse,HttpStatus.OK));
        when(retriveDiscountimpl.getRetrievedDiscound("qa-test-user","")).thenReturn(result);
        assertEquals(HttpStatus.OK,retriveDiscountimpl.getRetrievedDiscound("qa-test-user","").getStatusCode());
        assertEquals(discountWithOutPid,retriveDiscountimpl.getRetrievedDiscound("qa-test-user","").getBody());

    }
}