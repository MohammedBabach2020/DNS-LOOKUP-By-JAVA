package org.domain.available;

import org.w3c.dom.Document;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.Set;

class Check_Availability_Namecheap extends  Thread {
    private int countForminutes =0;
    private  int countforHours = 0;
    private int CountFordays = 0;
    private Set<String> domains= new HashSet<>();

public void run(){

        check();


}


   private final String username ;
   private final String apiKey ;



    public Check_Availability_Namecheap(String username, String apiKey, Set<String> domains) {
        this.username = username;
        this.apiKey = apiKey;
        this.domains = domains;
    }


    public  void check(){

        for(String domain : this.domains){
        try {
            countForminutes++;
            if(countForminutes > 55){
                countForminutes=0;
                Thread.sleep(60000);
            }
            if(countforHours > 700){
                countforHours=0;
                Thread.sleep(60000*60);
            }

            if(CountFordays > 8000){

                Thread.currentThread().join();
            }
            String endpoint = "https://api.sandbox.namecheap.com/xml.response?ApiUser=" + this.username + "&ApiKey="+this.apiKey +"&UserName=" + this.username+"&Command=namecheap.domains.check&ClientIp=66.45.250.142&DomainList=" +  domain;

        // Create HttpClient
        HttpClient httpClient = HttpClient.newHttpClient();

        // Create HttpRequest
        HttpRequest request = HttpRequest.newBuilder()

                .uri(URI.create(endpoint))

                .build();


            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());



       Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
                    new InputSource(new StringReader(response.body())));

            NodeList nodeList = document.getDocumentElement().getChildNodes();

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                        if(node.getNodeName().equals("Errors")) {
                            if (node.hasChildNodes()){

                                break;
                            }
                        }
                        if (node.getNodeName().equals("CommandResponse")) {

                            if (node.hasChildNodes()) {

                                String availability = node.getChildNodes().item(1).getAttributes().getNamedItem("Available").getTextContent();
                                if(availability.equals("true"))
                                {

                                }
                            }


                        }

            }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                }


    }
}
