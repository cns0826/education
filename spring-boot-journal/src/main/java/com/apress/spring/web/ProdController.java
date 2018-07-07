package com.apress.spring.web;

import java.util.*;
import java.io.*;
import java.lang.*;

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
import org.springframework.transaction.annotation.Transactional;


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
import com.apress.spring.exception.ProductNotFoundException;
import com.apress.spring.exception.NoMoreStockException;

@Component
@PropertySource("classpath:global.properties")
@Controller
@Transactional
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
    public @ResponseBody Product jsonDetailProduct(Model model, @PathVariable long prod_id) {

        for(int i=0; i<1000; i++)
        {
	    log.info("<=========================== /jsonDetailProduct ===================================>");
	    log.info("param id : " + prod_id +" /n");
        }

        try{
            Thread.sleep(200);
        }catch(Exception e)
        {
            
        }
        Product result = repo.findByCustomQuery(prod_id);
        if(result == null)
        {
           throw new ProductNotFoundException("상품번호가 " + prod_id + "인 상품은 존재하지 않습니다");
            //throw new IllegalStateException("상품번호가 " + prod_id + "인 상품은 존재하지 않습니다");
            //throw new NotFoundException("상품번호가 " + prod_id + "인 상품은 존재하지 않습니다");
        }
        return result;
    }



    @RequestMapping(value="/prod/", method= RequestMethod.PUT)
    public @ResponseBody String updateProduct(@RequestBody Product product) {
        Product productInfo = repo.findByCustomQuery(product.getProd_id());
        if(productInfo == null)
        {
            throw new ProductNotFoundException("상품번호가 " + product.getProd_id()+ "인 상품은 존재하지 않습니다");
        }

	log.info("<=========================== /jsonUpdate===================================>");
	log.info("param id : " + product.getProd_id() +" /n");

//        product.setProd_id(prod_id);

//        repo.save(product);
        int updateCount =   repo.updateProdByProdId(product.getProd_nm(), product.getProd_price(), product.getProd_desc(), product.getStock_cnt(), product.getProd_id());

        if(updateCount == 1)
        {
            return "update Success  : " + product.toString();
        }
        else
        {
            return "update Fail  : " + product.getProd_nm();
        }
    }

    @RequestMapping(value="/prod/regist/", method= RequestMethod.POST)
    public @ResponseBody String insertProduct(@RequestBody Product product) {
        

	log.info("<=========================== /jsonInsert===================================>");
	log.info("product : " + product +" /n");

      // long max = repo.getMaxProdIdValue();
      // log.info("max value : " + max +" /n");


     //   product.setProd_id(max);
//	log.info("<=========================== /repo.getMaxProdIdValue ===================================>");
//	log.info("product : " + product +" /n");

        Product saveResult = repo.save(product);
        //repo.save(new Product("Performance Test Education1","This is Test 1","01/01/2016"));
	log.info("saveResult : " + saveResult +" /n");

        if(saveResult != null)
        {
            return "Result : Success \n";
        }
        else
        { 
            return "Result : FAIL \n " ;
        }
    }
/*
    @RequestMapping(value="/prod/update/", method= RequestMethod.PUT)
    public @ResponseBody String updateProductInfo(@RequestBody Product product) {

	log.info("<=========================== /updateProduct===================================>");
	log.info("product : " + product +" /n");

        int result = repo.updateProdNmByProdId(product.getStock_cnt(), product.getProd_id());
        
	log.info("<=========================== /updateProduct result ===================================>");
	log.info("result : " + result +" /n");

        if(result == 1)
        {
            return "Success";
        }
        else
        {
            return "FAIL";
        }
    }
*/

    @RequestMapping(value="/prod/buy/{prod_id}", method= RequestMethod.POST)
//    public @ResponseBody synchronized String buyProductById(@PathVariable long prod_id ) {
    public @ResponseBody String buyProductById(@PathVariable long prod_id ) {



	log.info("<=========================== 1. 상품정보조회 ===================================>");

        long stock_cnt = 0l;

        
        Product buyProduct = repo.selectForUpdateProduct(prod_id);

	log.info("1  buyProduct : " + buyProduct  +" /n");

	log.info("<=========================== 1. 상품정보조회 ===================================>");
	log.info("<=========================== 2. 재고 수 확인 ===================================>");
	log.info("stock count : " + buyProduct.getStock_cnt() +" /n");
	log.info("<=========================== 2. 재고 수 확인 ===================================>");

        if(buyProduct.getStock_cnt() <=0 )
        {
	    log.info("No more Stock prod_id : " + prod_id +" /n");
            repo.findByCustomQuery(prod_id);
            throw new NoMoreStockException("상품 : " + buyProduct.toString() + " 인 상품은 재고 건수가 0건 입니다.");
            //throw new ProductNotFoundException("상품 : " + buyProduct.toString() + " 인 상품은 존재하지 않습니다");
            // return "You Can't Buy"+ buyProduct.getProd_nm();
        }
        else
        {
             log.info("<================================== 3. 재고 차감  ==============================");
             log.info(" 재고 차감전 product 정보 : " + buyProduct);
             stock_cnt = buyProduct.getStock_cnt() - 1;
             buyProduct.setStock_cnt(stock_cnt);
             log.info(" 재고 차감 후  product 정보 : " + buyProduct);
             log.info(" after : " + buyProduct);
             int minusStockCnt = repo.updateProdNmByProdId(buyProduct.getStock_cnt(), buyProduct.getProd_id());
             log.info("<================================== 3. 재고 차감 결과 ==============================");
             log.info(" minusStockCnt : " + minusStockCnt );
             log.info("<================================== 3. 재고 차감 결과  ==============================");

             if( minusStockCnt >0)
             {
                 log.info("<================================== 4. 재고 차감 성공  ==============================");

             }
             log.info("<================================== 5. 재고 차감 상품 엔티티 저장 ==============================");
             repo.save(buyProduct);
             log.info("<================================== 5. 재고 차감 상품 엔티티 저장 ==============================");
             log.info("<================================== 6. 디비 조회를 통한 엔티티 저장 정보 확인 ==============================");
             Product checkProduct = repo.findByCustomQuery(buyProduct.getProd_id());
             log.info("checkProduct: " + checkProduct +" /n");
             log.info("<================================== 6. 디비 조회를 통한 엔티티 저장 정보 확인 ==============================");

             if(checkProduct.getStock_cnt() == buyProduct.getStock_cnt())
             {
	         return checkProduct.getProd_nm() + " 구매가 정상적으로 처리되었습니다.";
             }
             else
             {
                 return checkProduct.getProd_nm() + " 구매가 정상적으로 처리되지 않았습니다.";
             } 
        }

    }


    @RequestMapping(value="/prod/{prod_id}", method= RequestMethod.DELETE)
    public @ResponseBody String deleteProductById(@PathVariable long prod_id ) {

        log.info("<=========================== 1. 상품정보조회 ===================================>");

        long stock_cnt = 0l;

        Product deleteProduct = repo.findByCustomQuery(prod_id);
	log.info("1  deleteProduct : " + deleteProduct  +" /n");

        int deleteCount = repo.deleteProductById(deleteProduct.getProd_id());

        log.info("<=========================== 2. deleteCount ===================================>");
        log.info(" deleteCount : " + deleteCount );

        if(deleteCount > 0)
        {
            return deleteProduct.getProd_nm() + " : " + deleteCount + " 건 삭제 성공 "; 
        }
        else
        {
            return "삭제 실패";
        }
        
    }
}
