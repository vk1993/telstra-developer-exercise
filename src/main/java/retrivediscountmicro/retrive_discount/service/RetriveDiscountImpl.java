package retrivediscountmicro.retrive_discount.service;

        import lombok.extern.slf4j.Slf4j;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.beans.factory.annotation.Value;
        import org.springframework.http.HttpStatus;
        import org.springframework.http.MediaType;
        import org.springframework.http.ResponseEntity;
        import org.springframework.stereotype.Service;
        import org.springframework.web.client.RestTemplate;
        import retrivediscountmicro.retrive_discount.domain.CustomerResourse;
        import retrivediscountmicro.retrive_discount.utill.ErrorResource;
        import java.util.ArrayList;
        import java.util.List;

@Service
@Slf4j
public class RetriveDiscountImpl implements RetriveDiscount {

    @Value("${getDisountURL}")
    public String Url;

    @Autowired
    public RestTemplate restTemplate ;

    @Override
    public ResponseEntity getRetrievedDiscound(String uuid, String pid) throws NullPointerException {
        log.info(Url);

        ResponseEntity<CustomerResourse> customerResourse = restTemplate.getForEntity(Url, CustomerResourse.class);
        ErrorResource errorResource;

         if(uuid.equals("qa-test-user")){

            log.info(pid);

            if(!(pid.trim().equals(""))){
                log.info("inside if of pid");
                if(pid.trim().equals("sku-1234567890")){
                    log.info("inside trim");
                    List discountPid = new ArrayList();

              customerResourse.getBody().getEligibleDiscounts().forEach(discount -> {
                   if(discount.getProductId() != null && discount.getProductId().equals(pid)){
                       discountPid.add(discount);
                  }
               });
               return ResponseEntity.ok()
                          .body(discountPid);
                }else{
                    errorResource = new ErrorResource("EE003", String.format("Product with %s is not avaliable",pid));
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(errorResource);
                }
            }else{
                log.info("inside if of pid to discount");
                return ResponseEntity.ok()
                        .body(customerResourse.getBody().getEligibleDiscounts());
            }
         }else {
             errorResource = new ErrorResource("EE005", String.format("User Not Found with UUID: %s",uuid));
             return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(errorResource);
         }
    }
}
