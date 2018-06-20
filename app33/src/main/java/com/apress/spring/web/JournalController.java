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
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;





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
import com.apress.spring.domain.Journal;
import com.apress.spring.repository.JournalRepository;

@Component
@PropertySource("classpath:global.properties")
@Controller
public class JournalController {
    private static final Logger log = LoggerFactory.getLogger(JournalController.class);
    private static final String VIEW_INDEX = "index";
    private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");


    @Value("${remoteIP}")
    private String remoteIP;

    @Value("${remotePort}")
    private String remotePort;

    @Autowired
    JournalRepository repo;

    @RequestMapping(value = "/journal", produces = { MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody List<Journal> getJournal() {
        return repo.findAll();
    }
/*
    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("journal", repo.findAll());
        return "index";
    }
*/
    @RequestMapping(value="/", method = RequestMethod.GET)
    public ModelAndView index(ModelAndView modelAndView) {
	modelAndView.setViewName(VIEW_INDEX);
	modelAndView.addObject("journal", repo.findAll());
	return modelAndView;
    }

    @RequestMapping("/detail/{id}")
    public String detail(Model model, @PathVariable long id) {

        for(int i=0; i<1000; i++)
        {
	    log.info("<=========================== /search start ===================================>");
            log.info("<=========================== /search start ===================================>");
        }
        model.addAttribute("journal", repo.findByCustomQuery(id));

        for(int i=0; i<1000; i++)
        {
	    log.info("<=========================== /search end ===================================>");
            log.info("<=========================== /search end ===================================>");
        }

        return "index";

    }

    @RequestMapping(value = "/jsonDetail/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody List<Journal> jsonDetail(Model model, @PathVariable long id) {

        for(int i=0; i<1000; i++)
        {
	    log.info("<=========================== /jsonDetail ===================================>");
        }

        try{
            Thread.sleep(2000);
        }catch(Exception e)
        {
            
        }
        return repo.findByCustomQuery(id);
    }


    @RequestMapping(value = "/jsonsearch", produces = { MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody List<Journal> jsonsearch(Model model) {
        return repo.findAll();
    }

    @RequestMapping("/heavySql")
    public String count(Model model) {

        for(int i=0; i<1000; i++)
        {
	    log.info("<=========================== /heavySql start ===================================>");
            log.info("<=========================== /heavySql start ===================================>");
        }

        model.addAttribute("journal", repo.sleepCustomQuery());

        for(int i=0; i<1000; i++)
        {
	    log.info("<=========================== /heavySql end ===================================>");
            log.info("<=========================== /heavySql end ===================================>");
        }

        return "index";

    }


    @RequestMapping(value = "/remotecall/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody String test(@PathVariable long id)
    {
                String responseJSON = null;
		HttpClient httpClient = null;
		HttpGet httpGet = null;
		HttpResponse response = null;
                StringBuffer result = new StringBuffer();
		StatusLine statusLine = null;
		
		try {
			httpClient = ConnectionManager.getHttpClient();
			log.info("param id : " + id +" /n");
			log.info("remoteIP : " + remoteIP +" /n");
			log.info("remotePort : " + remotePort +" /n");
         		//httpGet = new HttpGet("http://35.187.207.139:8081/jsonDetail/"+id);
			//httpGet = new HttpGet("http://localhost:8080/jsonDetail/"+id);
			httpGet = new HttpGet("http://"+remoteIP+":"+remotePort+"/jsonDetail/"+id);
			response = httpClient.execute(httpGet);

			statusLine = response.getStatusLine();
			// 에러 발생
			if (statusLine.getStatusCode() < 200 || statusLine.getStatusCode() >= 300) {
			    log.info("<=========================== rest call Fail ===================================>");
                                throw new Exception(statusLine.getStatusCode()+" " + statusLine);

			}
			// 성공
			else {
                            
			  log.info("statusLine.getStatusCode : " + statusLine.getStatusCode()+"\n");
			  log.info("response : " + response +"\n");
			  log.info("response.getEntity : " + response.getEntity() +"\n");
			  log.info("response.getEntity.getContent : " + response.getEntity().getContent() +"\n");
			  log.info("<=========================== rest call success ===================================>");
			  BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			  String line2="";
			  while ((line2 = rd.readLine()) != null) {
				log.info(" line value : " + line2 + "\n");
				System.out.println(line2);
				result.append(line2);
 			 }
             
			}
		} catch (Exception e) {
			e.printStackTrace();
			ConnectionManager.abort(httpGet);
		} finally {
			ConnectionManager.release(response);
		}
            
                return result.toString();

    }

 @ResponseBody
 @RequestMapping(value="/longlong", method= RequestMethod.GET)
 public String test2(){
                String responseJSON = null;
                HttpClient httpClient = null;
                HttpGet httpGet = null;
                HttpResponse response = null;
                StringBuffer result = new StringBuffer();
		StatusLine statusLine = null;

                try {
                        httpClient = ConnectionManager.getHttpClient();
                        log.info("remoteIP : " + remoteIP +" /n");
                        log.info("remotePort : " + remotePort +" /n");
                        httpGet = new HttpGet("http://"+remoteIP+":"+remotePort+"/longlong");
                        response = httpClient.execute(httpGet);

                        statusLine = response.getStatusLine();
                        // 에러 발생
                        if (statusLine.getStatusCode() < 200 || statusLine.getStatusCode() >= 300) {
                            log.info("<=========================== rest call Fail ===================================>");
                                throw new Exception(statusLine.getStatusCode()+" " + statusLine);

                        }
                        // 성공
                        else {

                            log.info("statusLine.getStatusCode : " + statusLine.getStatusCode()+"\n");
                            log.info("response : " + response +"\n");
                            log.info("response.getEntity : " + response.getEntity() +"\n");
                            log.info("response.getEntity.getContent : " + response.getEntity().getContent() +"\n");
                            log.info("<=========================== rest call success ===================================>");
                            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                            String line2="";
	                    while ((line2 = rd.readLine()) != null) {
	                        log.info(" line value : " + line2 + "\n");	                      
	                        result.append(line2);
	                    }

                        }
                } catch (Exception e) {
                        e.printStackTrace();
                        ConnectionManager.abort(httpGet);
                } finally {
                        ConnectionManager.release(response);
                }

                return result.toString();
 }

 @RequestMapping(value = "/slow/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
 public @ResponseBody String slow(@PathVariable long id)
 {
                String responseJSON = null;
		HttpClient httpClient = null;
		HttpGet httpGet = null;
		HttpResponse response = null;
                StringBuffer result = new StringBuffer();
		
		try {
			httpClient = ConnectionManager.getHttpClient();
                        log.info("param id : " + id +" /n");
                        log.info("remoteIP : " + remoteIP +" /n");
                        log.info("remotePort : " + remotePort +" /n");
                        httpGet = new HttpGet("http://"+remoteIP+":"+remotePort+"/jsonDetail/"+id);

                        //httpGet = new HttpGet("http://35.187.207.139:8081/jsonDetail/"+id);
                        //httpGet = new HttpGet("http://localhost:8080/jsonDetail/"+id);
	        //	httpGet = new HttpGet("http://localhost:8080/jsonDetail/"+id);
		//	httpGet = new HttpGet("http://35.187.207.139:8081/jsonDetailSlow/"+id);
			response = httpClient.execute(httpGet);

			StatusLine statusLine = response.getStatusLine();
			// 에러 발생
			if (statusLine.getStatusCode() < 200 || statusLine.getStatusCode() >= 300) {
			    log.info("<=========================== rest call Fail ===================================>");
                                throw new Exception(statusLine.getStatusCode()+" " + statusLine);

			}
			// 성공
			else {
                            
			    log.info("statusLine.getStatusCode : " + statusLine.getStatusCode()+"\n");
			    log.info("response : " + response +"\n");
			    log.info("response.getEntity : " + response.getEntity() +"\n");
			    log.info("response.getEntity.getContent : " + response.getEntity().getContent() +"\n");
			    log.info("<=========================== rest call success ===================================>");
BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                  String line2="";
		  while ((line2 = rd.readLine()) != null) {
                        log.info(" line value : " + line2 + "\n");
			System.out.println(line2);
                        result.append(line2);
 			}
             
			}
		} catch (Exception e) {
			e.printStackTrace();
			ConnectionManager.abort(httpGet);
		} finally {
			ConnectionManager.release(response);
		}
            
                return result.toString();

    }

    @RequestMapping(value = "/jsonDetailSlow/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody List<Journal> jsonDetailSlow(Model model, @PathVariable long id) {

        for(int i=0; i<1000; i++)
        {
	    log.info("<=========================== /jsonDetail ===================================>");
        }

        try{
            Thread.sleep(20000);
        }catch(Exception e)
        {
            
        }
        return repo.findByIdAndSleepCustomQuery(id);
    }

    @RequestMapping(value="/jsonUpdate/{id}", method= RequestMethod.PUT)
    public @ResponseBody List<Journal> updateJournal(@RequestBody Journal journal, @PathVariable long id) {
        
        List<Journal> journalOptional = repo.findByCustomQuery(id);

	log.info("<=========================== /jsonUpdate===================================>");
	log.info("param id : " + id +" /n");

        journal.setId(id);

        repo.save(journal);

        return repo.findByCustomQuery(id);
    }

    @RequestMapping(value="/jsonInsert/", method= RequestMethod.POST)
    public @ResponseBody String updateJournal(@RequestBody Journal journal) {
        

	log.info("<=========================== /jsonInsert===================================>");
	log.info("journal : " + journal +" /n");

        repo.save(journal);
        //repo.save(new Journal("Performance Test Education1","This is Test 1","01/01/2016"));

        return "Success";
    }
}
