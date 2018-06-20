package com.apress.spring.web;

import java.util.*;
import java.io.*;

import java.nio.charset.Charset;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.http.ResponseEntity;

import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;





import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;

import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;
import org.springframework.context.annotation.Configuration;


import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.client.HttpClient;
import org.apache.http.StatusLine;




import com.apress.spring.config.ConnectionManager;
import com.apress.spring.domain.Product;
import com.apress.spring.repository.ProductRepository;

@Component
@PropertySource("classpath:global.properties")
@Controller
public class ProdController {
    private static final Logger log = LoggerFactory.getLogger(ProdController.class);
    private static final String VIEW_INDEX = "index";
    private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");

    @Value("${remoteIP}")
    private String remoteIP;

    @Value("${remotePort}")
    private String remotePort;

    @Autowired
    ProductRepository repo;

    @RequestMapping(value = "/prod/", produces = { MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody List<Product> listProduct() {

        log.info("<=========================== /listProduct start ===================================>");
        log.info("remoteIP : " + remoteIP + " remotePort : " + remotePort + " \n");
        log.info("<=========================== /listProduct   end ===================================>");
        return repo.findAll();
    }


    @RequestMapping(value = "/prod/{prod_id}", method=RequestMethod.GET)
    public @ResponseBody List<Product> jsonDetailProduct(Model model, @PathVariable long prod_id) {

        for(int i=0; i<1000; i++)
        {
	    log.info("<=========================== /jsonDetailProduct ===================================>");
	    log.info("param id : " + prod_id +" /n");
        }

        try{
            Thread.sleep(20000);
        }catch(Exception e)
        {
            
        }
        return repo.findByCustomQuery(prod_id);
    }



    @RequestMapping(value="/prod/{prod_id}", method= RequestMethod.PUT)
    public @ResponseBody List<Product> updateProduct(@RequestBody Product product, @PathVariable long prod_id) {
        
        List<Product> productInfo = repo.findByCustomQuery(prod_id);

	log.info("<=========================== /jsonUpdate===================================>");
	log.info("param id : " + prod_id +" /n");

        product.setProd_id(prod_id);

        repo.save(product);

        return repo.findByCustomQuery(prod_id);
    }

    @RequestMapping(value="/prod/regist/", method= RequestMethod.POST)
    public @ResponseBody String insertProduct(@RequestBody Product product) {
        

	log.info("<=========================== /jsonInsert===================================>");
	log.info("product : " + product +" /n");

        long max = repo.getMaxProdIdValue();
	log.info("max value : " + max +" /n");


        product.setProd_id(max);
	log.info("<=========================== /repo.getMaxProdIdValue ===================================>");
	log.info("product : " + product +" /n");

        repo.save(product);
        //repo.save(new Product("Performance Test Education1","This is Test 1","01/01/2016"));

        return "Success";
    }

    @RequestMapping(value="/prod/update/", method= RequestMethod.PUT)
    public @ResponseBody String updateProductInfo(@RequestBody Product product) {
        

	log.info("<=========================== /updateProduct===================================>");
	log.info("product : " + product +" /n");


        repo.updateProdNmByProdId(product.getProd_nm(), product.getProd_id());


        return "Success";

    }
}
