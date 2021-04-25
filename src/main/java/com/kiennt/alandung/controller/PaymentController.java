package com.kiennt.alandung.controller;

import com.kiennt.alandung.service.PayPalService;
import com.kiennt.alandung.util.PayPalPaymentIntent;
import com.kiennt.alandung.util.PayPalPaymentMethod;
import com.kiennt.alandung.util.PayPalUtils;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PaymentController {

//    private Logger log = LoggerFactory.getLogger(getClass());
//
//    public static final String URL_PAYPAL_SUCCESS = "pay/success";
//    public static final String URL_PAYPAL_CANCEL = "pay/cancel";
//
//    @Autowired
//    private PayPalService payPalService;
//
//    @GetMapping("/")
//    public String index(){
//        return "index";
//    }
//
//    @PostMapping("/pay")
//    public String pay(HttpServletRequest request, @RequestParam("price") double price ){
//        String cancelUrl = PayPalUtils.getBaseURL(request) + "/" + URL_PAYPAL_CANCEL;
//        String successUrl = PayPalUtils.getBaseURL(request) + "/" + URL_PAYPAL_SUCCESS;
//        try {
//            Payment payment = payPalService.createPayment(
//                    price,
//                    "USD",
//                    PayPalPaymentMethod.PAYPAL,
//                    PayPalPaymentIntent.SALE,
//                    "payment description",
//                    cancelUrl,
//                    successUrl);
//            for(Links links : payment.getLinks()){
//                if(links.getRel().equals("approval_url")){
//                    return "redirect:" + links.getHref();
//                }
//            }
//        } catch (PayPalRESTException e) {
//            log.error(e.getMessage());
//        }
//        return "redirect:/";
//    }
//
//    @GetMapping(URL_PAYPAL_CANCEL)
//    public String cancelPay(){
//        return "cancel";
//    }
//
//    @GetMapping(URL_PAYPAL_SUCCESS)
//    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId){
//        try {
//            Payment payment = payPalService.executePayment(paymentId, payerId);
//            if(payment.getState().equals("approved")){
//                return "success";
//            }
//        } catch (PayPalRESTException e) {
//            log.error(e.getMessage());
//        }
//        return "redirect:/";
//    }
}
