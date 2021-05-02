package com.kiennt.alandung.controller;

import com.kiennt.alandung.dto.CustomerDTO;
import com.kiennt.alandung.entity.CartItem;
import com.kiennt.alandung.entity.Customer;
import com.kiennt.alandung.entity.Product;
import com.kiennt.alandung.entity.enums.Status;
import com.kiennt.alandung.repository.CustomerRepository;
import com.kiennt.alandung.service.PayPalService;
import com.kiennt.alandung.service.ShoppingCartService;
import com.kiennt.alandung.util.CommonConstant;
import com.kiennt.alandung.util.PayPalPaymentIntent;
import com.kiennt.alandung.util.PayPalPaymentMethod;
import com.kiennt.alandung.util.PayPalUtils;
import com.kiennt.alandung.util.mapper.CustomerMapper;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
public class PaymentController {

    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    private static final String URL_PAYPAL_SUCCESS = "pay/success";
    private static final String URL_PAYPAL_CANCEL = "pay/cancel";
    private static Customer customer;

    @Autowired
    private PayPalService payPalService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @PostMapping("/pay")
    public String pay(HttpServletRequest request, @Valid CustomerDTO customerDTO) {
        customer = customerMapper.toEntity(customerDTO);
        customerRepository.save(PaymentController.customer);
        List<CartItem> cartItems = shoppingCartService.getCartItemsByStatus(Status.PENDING);
        Double totalPrice = shoppingCartService.getTotalPrice(cartItems);
        String cancelUrl = PayPalUtils.getBaseURL(request) + "/" + URL_PAYPAL_CANCEL;
        String successUrl = PayPalUtils.getBaseURL(request) + "/" + URL_PAYPAL_SUCCESS;
        String paymentDescription = CommonConstant.PAYMENT_DESCRIPTION + totalPrice
                + CommonConstant.ONE_SPACE + CommonConstant.CURRENCY;
        try {
            Payment payment = payPalService.createPayment(
                    totalPrice,
                    CommonConstant.CURRENCY,
                    PayPalPaymentMethod.PAYPAL,
                    PayPalPaymentIntent.SALE,
                    paymentDescription,
                    cancelUrl,
                    successUrl);
            for(Links links : payment.getLinks()){
                if(links.getRel().equals("approval_url")){
                    return "redirect:" + links.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            LOGGER.error(e.getMessage());
        }
        return "redirect:/";
    }

    @GetMapping("pay/cancel")
    public String cancelPay(){
        List<CartItem> cartItems = shoppingCartService.getCartItemsByStatus(Status.PENDING);
        cartItems.forEach(cartItem -> {
            cartItem.setCustomer(customer);
            cartItem.setStatus(Status.UNPAID);
            shoppingCartService.upsertCartItem(cartItem);
        });
        return "cancel";
    }

    @GetMapping("pay/success")
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId){
        try {
            Payment payment = payPalService.executePayment(paymentId, payerId);
            if(payment.getState().equals("approved")){
                List<CartItem> cartItems = shoppingCartService.getCartItemsByStatus(Status.PENDING);
                cartItems.forEach(cartItem -> {
                    cartItem.setCustomer(customer);
                    cartItem.setStatus(Status.PAID);
                    shoppingCartService.upsertCartItem(cartItem);
                });
                return "success";
            }
        } catch (PayPalRESTException e) {
            LOGGER.error(e.getMessage());
        }
        return "redirect:/";
    }
}
