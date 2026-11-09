package in.smitapriya.billingsoftware.controller;

import in.smitapriya.billingsoftware.io.OrderRequest;
import in.smitapriya.billingsoftware.io.OrderResponse;
import in.smitapriya.billingsoftware.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(@RequestBody OrderRequest request){
        return orderService.createOrder(request);
    }

    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable String orderId){
        orderService.deleteOrder(orderId);
    }
    @GetMapping("/latest")
    public Page<OrderResponse> getOrders(@RequestParam(required = false, defaultValue = "0") int pageNo , @RequestParam(required = false, defaultValue = "5") int pageSize,@RequestParam String sortBy,@RequestParam String orderBy, @RequestParam(required = false) String searchValue){

        Sort sort=null;
        if(orderBy.equalsIgnoreCase("ASC")){
            sort=Sort.by(sortBy).ascending();
        }
        else{
            sort=Sort.by(sortBy).descending();
        }
        PageRequest pageable=PageRequest.of(pageNo ,pageSize,sort);
//        return  orderService.getOrders(pageable, searchByItemName);
        return  orderService.getOrders(pageable, searchValue);
    }
    @PostMapping("/summarize")
    public String summarizeOrders(){
        return orderService.summarizeOrders();
    }

}
