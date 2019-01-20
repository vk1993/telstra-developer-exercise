package retrivediscountmicro.retrive_discount.controll;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import retrivediscountmicro.retrive_discount.domain.CustomerResourse;
import retrivediscountmicro.retrive_discount.domain.Discount;
import retrivediscountmicro.retrive_discount.domain.PruchasedProduct;
import retrivediscountmicro.retrive_discount.service.RetriveDiscount;
import retrivediscountmicro.retrive_discount.utill.ErrorResource;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class RetrieveDiscountControllerTest {

    private MockMvc mockMvc;
    @InjectMocks
    RetrieveDiscountController retrieveDiscountController;
    @Mock
    RetriveDiscount retriveDiscount;

    CustomerResourse customer_Resourse ;
    List<Discount> discountList = new ArrayList<>();
    List<Discount> discountwithPid = new ArrayList<>();
    List<PruchasedProduct> pruchasedProductsList = new ArrayList<>();
    Discount discount1;
    Discount discount2;
    PruchasedProduct pruchasedProduct;
    ErrorResource errorResource;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(retrieveDiscountController)
                .alwaysExpect(content().contentType("application/json;charset=UTF-8")).build();

        customer_Resourse = new CustomerResourse();

        pruchasedProduct = new PruchasedProduct();
        pruchasedProduct.setDescription("12 month subscription to 'Horse and Hound'");
        pruchasedProduct.setFinalPrice(new BigDecimal(50));
        pruchasedProduct.setOriginalPrice(new BigDecimal(100));
        pruchasedProduct.setProductId("sku-1234567890");

         discount1 = new Discount("10-percent-off",
                "PERCENT",
                "Reduce the purchase price by 10%",
                new BigDecimal(10));


         discount2 = new Discount(
                "5-dollars-off",
                "AMOUNT",
                "Reduce the purchase price by $5",
                new BigDecimal(5),"sku-1234567890");
        discountwithPid.add(discount2);

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
    public void retrieveDiscount() throws Exception {

        ResponseEntity result = new ResponseEntity(discountList, HttpStatus.OK);
        when(retriveDiscount.getRetrievedDiscound("qa-test-user","")).thenReturn(result);
        mockMvc.perform(get("/rest/v1/users/qa-test-user/discounts"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.*", Matchers.hasSize(2)))
                .equals(customer_Resourse);
    }

    @Test
    public void retrieveDiscountWithPid() throws Exception {

        ResponseEntity result = new ResponseEntity(discountwithPid, HttpStatus.OK);
        when(retriveDiscount.getRetrievedDiscound("qa-test-user","sku-1234567890")).thenReturn(result);
        mockMvc.perform(get("/rest/v1/users/qa-test-user/discounts").param("productId","sku-1234567890"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.*",Matchers.hasSize(1))).equals(result);
    }


    @Test
    public void retrieveDiscountUserNotFound() throws Exception {

        errorResource = new ErrorResource("EE005",String.format("User Not Found with UUID: qa-test-use"));

        ResponseEntity result = new ResponseEntity(errorResource, HttpStatus.NOT_FOUND);
        when(retriveDiscount.getRetrievedDiscound("qa-test-use","")).thenReturn(result);
        mockMvc.perform(get("/rest/v1/users/qa-test-use/discounts"))
                .andExpect(status().isNotFound()).andExpect(jsonPath("$.*", Matchers.hasSize(2)))
                .equals(result);
    }

    @Test
    public void retrieveDiscountUserNotFoundforPid() throws Exception {

        errorResource = new ErrorResource("EE003",String.format("Product with sku-123456780 is not avaliable"));

        ResponseEntity result = new ResponseEntity(errorResource, HttpStatus.NOT_FOUND);
        when(retriveDiscount.getRetrievedDiscound("qa-test-user","sku-123456780")).thenReturn(result);
        mockMvc.perform(get("/rest/v1/users/qa-test-user/discounts").param("productId","sku-123456780"))
                .andExpect(status().isNotFound()).andExpect(jsonPath("$.*", Matchers.hasSize(2)))
                .equals(result);
    }

}