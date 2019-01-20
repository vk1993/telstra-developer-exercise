package retrivediscountmicro.retrive_discount.utill;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Data
@AllArgsConstructor
public class ErrorResource {

    private String status;
    private String message;

}
