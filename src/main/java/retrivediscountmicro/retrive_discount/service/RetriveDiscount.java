package retrivediscountmicro.retrive_discount.service;

        import org.springframework.http.ResponseEntity;
        import org.springframework.stereotype.Service;


public interface RetriveDiscount {

     ResponseEntity getRetrievedDiscound(String uuid,String pid);
}
