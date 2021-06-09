
package airbnb.external;

import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="rentcar", url="${prop.room.url}")
public interface RentcarService {

    @RequestMapping(method= RequestMethod.GET, path="/chkcar/chkStatus")
    public boolean chkStatus(@RequestParam("carId") long carId);

}