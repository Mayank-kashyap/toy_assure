package channel.assure;

import channel.util.ApiException;
import common.model.*;
import common.util.AbstractRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
public class AssureClient extends AbstractRestTemplate {

    private String serverUrl="http://localhost:9000/toy_assure/api";

    public List<ChannelData> get() throws ApiException {
        String getChannelUrl=serverUrl+"/channel";
        try {
            List<ChannelData> response;
            HttpEntity<List<ChannelData>> entity=new HttpEntity<>(getHeaders());
            response = restTemplate.exchange(getChannelUrl,HttpMethod.GET,entity,new ParameterizedTypeReference<List<ChannelData>>() {}).getBody();
            return response;
        }
        catch (Exception exception){
            throw new ApiException("Server connection error");
        }
    }

    public ChannelData getByName(String name) throws ApiException {
        String getChannelUrl=serverUrl+"/channel/"+name;
        try {
            ChannelData response;
            HttpEntity<ChannelData> entity=new HttpEntity<>(getHeaders());
            response = restTemplate.exchange(getChannelUrl,HttpMethod.GET,entity,ChannelData.class).getBody();
            return response;
        }
        catch (Exception exception){
            throw new ApiException("Server connection error");
        }
    }

    public  List<ChannelListingData> getAllChannelListing() throws ApiException {
        String getChannelUrl=serverUrl+"/channel_listing";
        try {
            List<ChannelListingData> response;
            HttpEntity<List<ChannelListingData>> entity=new HttpEntity<>(getHeaders());
            response = restTemplate.exchange(getChannelUrl,HttpMethod.GET,entity,new ParameterizedTypeReference<List<ChannelListingData>>() {}).getBody();
            return response;
        }
        catch (Exception exception){
            throw new ApiException("Server connection error");
        }

    }

    public void add(ChannelForm channelForm) throws ApiException {
        String getChannelUrl=serverUrl+"/channel";
        try{
            HttpEntity<ChannelForm> entity=new HttpEntity<>(channelForm,getHeaders());
            restTemplate.exchange(getChannelUrl,HttpMethod.POST,entity,Void.class);
        } catch (Exception exception){
            throw new ApiException("Channel name repeated");
        }
    }

    public void add(String clientName,String channelName, ChannelListingForm channelListingForm) throws ApiException {
        String getChannelUrl=serverUrl+"/channel_listing/"+clientName+"/"+channelName;
        try {
            HttpEntity<ChannelListingForm> entity=new HttpEntity<>(channelListingForm,getHeaders());
            restTemplate.exchange(getChannelUrl,HttpMethod.POST,entity,Void.class);
        }
        catch (Exception exception){
            throw new ApiException("Repetition in input");
        }
    }

    public void add(String clientName,String channelName, List<ChannelListingForm> channelListingFormList) throws ApiException {
        String getChannelUrl=serverUrl+"/channel_listing/list/"+clientName+"/"+channelName;
        try{
            HttpEntity<List<ChannelListingForm>> entity=new HttpEntity<>(channelListingFormList,getHeaders());
            restTemplate.exchange(getChannelUrl,HttpMethod.POST,entity,Void.class);
        }
        catch (Exception exception){
            throw new ApiException("Invalid input");
        }
    }

    public void add(String channelName,String clientName,String customerName,String channelOrderId,List<OrderItemsForm> orderItemsFormList) throws ApiException {
        String getOrderUrl=serverUrl+"/order/"+channelName+"/"+clientName+"/"+customerName+"/"+channelOrderId;
        try {
            HttpEntity<List<OrderItemsForm>> entity=new HttpEntity<>(orderItemsFormList,getHeaders());
            restTemplate.exchange(getOrderUrl,HttpMethod.POST,entity,Void.class);
        }
        catch (Exception exception){
            throw new ApiException("Invalid input");
        }
    }

    public List<OrderData> getOrderList() throws ApiException {
        String getOrderUrl =serverUrl+"/order";
        try {
            List<OrderData> response;
            HttpEntity<List<OrderData>> entity=new HttpEntity<>(getHeaders());
            response = restTemplate.exchange(getOrderUrl,HttpMethod.GET,entity,new ParameterizedTypeReference<List<OrderData>>() {}).getBody();
            return response;
        }
        catch (Exception exception){
            throw new ApiException("Server connection error");
        }
    }

    public OrderData getOrder(Long id) throws ApiException {
        String getOrderUrl =serverUrl+"/order/"+id;
        try {
            OrderData response;
            HttpEntity<OrderData> entity=new HttpEntity<>(getHeaders());
            response = restTemplate.exchange(getOrderUrl,HttpMethod.GET,entity,OrderData.class).getBody();
            return response;
        }
        catch (Exception exception){
            throw new ApiException("Server connection error");
        }
    }

    public List<OrderItemData> get(Long orderId) throws ApiException {
        String getOrderUrl =serverUrl+"/orderItem/"+orderId;
        try {
            List<OrderItemData> response;
            HttpEntity<List<OrderItemData>> entity=new HttpEntity<>(getHeaders());
            response = restTemplate.exchange(getOrderUrl,HttpMethod.GET,entity,new ParameterizedTypeReference<List<OrderItemData>>() {}).getBody();
            return response;
        }
        catch (Exception exception){
            throw new ApiException("Server connection error");
        }
    }

    public List<UserData> getAllUser() throws ApiException {
        String getUserUrl=serverUrl+"/user";
        try{
            List<UserData> response;
            HttpEntity<List<UserData>> entity=new HttpEntity<>(getHeaders());
            response = restTemplate.exchange(getUserUrl,HttpMethod.GET,entity,new ParameterizedTypeReference<List<UserData>>() {}).getBody();
            return response;
        }
        catch (Exception exception){
            throw new ApiException("Server connection error");
        }
    }

    public void getInvoice(Long id,HttpServletResponse response) throws ApiException {
        String getInvoiceUrl=serverUrl+"/invoice/"+id;
        try{
            System.out.println("channel client "+id);
            HttpEntity<HttpServletResponse> entity=new HttpEntity<>(response,getHeaders());
            restTemplate.exchange(getInvoiceUrl,HttpMethod.GET,entity,Void.class);
        }
        catch (Exception exception){
            throw new ApiException("Server connection error");
        }
    }
    public OrderInvoiceXmlList generateInvoiceList(Long id) throws ApiException {
        String getInvoiceUrl=serverUrl+"/generateInvoice/"+id;
        try{
            System.out.println("channel client "+id);
            OrderInvoiceXmlList response;
            HttpEntity<OrderInvoiceXmlList> entity=new HttpEntity<>(getHeaders());
            response=restTemplate.exchange(getInvoiceUrl,HttpMethod.GET,entity,OrderInvoiceXmlList.class).getBody();
            return response;
        }
        catch (Exception exception){
            throw new ApiException("Server connection error");
        }
    }
}
