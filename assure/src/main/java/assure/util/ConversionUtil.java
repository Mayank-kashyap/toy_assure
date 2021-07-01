package assure.util;

import assure.pojo.*;
import common.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConversionUtil {
    public static UserPojo convert(UserForm userForm){
        UserPojo userPojo =new UserPojo();
        userPojo.setName(userForm.getName());
        userPojo.setType(userForm.getType());
        return userPojo;
    }

    public static UserData convert(UserPojo userPojo){
        UserData userData =new UserData();
        userData.setName(userPojo.getName());
        userData.setId(userPojo.getId());
        userData.setType(userPojo.getType());
        return userData;
    }
    public static List<UserData> convert(List<UserPojo> userPojoList){
        List<UserData> userDataList = new ArrayList<>();
        for(UserPojo userPojo : userPojoList){
            userDataList.add(convert(userPojo));
        }
        return userDataList;
    }

    public static ProductPojo convert(Long clientId, ProductForm productForm){
        ProductPojo productPojo=new ProductPojo();
        productPojo.setDescription(productForm.getDescription());
        productPojo.setMrp(productForm.getMrp());
        productPojo.setName(productForm.getName());
        productPojo.setBrandId(productForm.getBrandId());
        productPojo.setClientId(clientId);
        productPojo.setClientSkuId(productForm.getClientSkuId());
        return productPojo;
    }

    public static ProductData convert(ProductPojo productPojo){
        ProductData productData=new ProductData();
        productData.setBrandId(productPojo.getBrandId());
        productData.setDescription(productPojo.getDescription());
        productData.setMrp(productPojo.getMrp());
        productData.setClientSkuId(productPojo.getClientSkuId());
        productData.setName(productPojo.getName());
        productData.setId(productPojo.getId());
        productData.setClientId(productPojo.getClientId());
        return productData;
    }

    public static List<ProductData> convertProductList(List<ProductPojo> productPojoList){
        List<ProductData> productDataList =new ArrayList<>();
        for (ProductPojo productPojo:productPojoList){
            productDataList.add(convert(productPojo));
        }
        return productDataList;
    }

    public static BinSkuPojo convert(ProductPojo productPojo, BinSkuForm binSkuForm){
        BinSkuPojo binSkuPojo=new BinSkuPojo();
        binSkuPojo.setBinId(binSkuForm.getBinId());
        binSkuPojo.setGlobalSkuId(productPojo.getId());
        binSkuPojo.setQuantity(binSkuForm.getQuantity());
        return binSkuPojo;
    }

    public static BinSkuData convert(BinSkuPojo binSkuPojo,ProductPojo productPojo){
        BinSkuData binSkuData=new BinSkuData();
        binSkuData.setBinId(binSkuPojo.getBinId());
        binSkuData.setClientSkuId(productPojo.getClientSkuId());
        binSkuData.setQuantity(binSkuPojo.getQuantity());
        binSkuData.setId(binSkuPojo.getId());
        return binSkuData;
    }

    public static List<BinSkuData> convert(Map<BinSkuPojo,ProductPojo> binSkuPojoProductPojoMap){
        List<BinSkuData> binSkuDataList=new ArrayList<>();
        for(BinSkuPojo binSkuPojo:binSkuPojoProductPojoMap.keySet()){
            BinSkuData binSkuData=convert(binSkuPojo,binSkuPojoProductPojoMap.get(binSkuPojo));
            binSkuDataList.add(binSkuData);
        }
        return binSkuDataList;
    }

    public static ChannelPojo convert(ChannelForm channelForm){
        ChannelPojo channelPojo=new ChannelPojo();
        channelPojo.setName(channelForm.getName());
        channelPojo.setInvoiceType(channelForm.getInvoiceType());
        return channelPojo;
    }

    public static ChannelListingPojo convert(ChannelPojo channelPojo, ProductPojo productPojo, UserPojo userPojo, ChannelListingForm channelListingForm){
        ChannelListingPojo channelListingPojo=new ChannelListingPojo();
        channelListingPojo.setChannelId(channelPojo.getId());
        channelListingPojo.setChannelSkuId(channelListingForm.getChannelSkuId());
        channelListingPojo.setClientId(userPojo.getId());
        channelListingPojo.setGlobalSkuId(productPojo.getId());
        return channelListingPojo;
    }

    public static List<ChannelData> convertChannel(List<ChannelPojo> channelPojoList){
        List<ChannelData> channelDataList=new ArrayList<>();
        for (ChannelPojo channelPojo:channelPojoList){
            ChannelData channelData=new ChannelData();
            channelData.setId(channelPojo.getId());
            channelData.setName(channelPojo.getName());
            channelData.setInvoiceType(channelPojo.getInvoiceType());
            channelDataList.add(channelData);
        }
        return channelDataList;
    }

    public static ChannelData convert(ChannelPojo channelPojo){
        ChannelData channelData=new ChannelData();
        channelData.setId(channelPojo.getId());
        channelData.setName(channelPojo.getName());
        channelData.setInvoiceType(channelPojo.getInvoiceType());
        return channelData;
    }
    public static List<ChannelListingData> convertChannelListing(Map<ChannelListingPojo,ProductPojo> channelListingPojoProductPojoMap){
        List<ChannelListingData> channelListingDataList=new ArrayList<>();
        for(ChannelListingPojo channelListingPojo: channelListingPojoProductPojoMap.keySet()){
            ChannelListingData channelListingData=new ChannelListingData();
            ProductPojo productPojo=channelListingPojoProductPojoMap.get(channelListingPojo);
            channelListingData.setId(channelListingPojo.getId());
            channelListingData.setChannelId(channelListingPojo.getChannelId());
            channelListingData.setChannelSkuId(channelListingPojo.getChannelSkuId());
            channelListingData.setClientSkuId(productPojo.getClientSkuId());
            channelListingDataList.add(channelListingData);
        }
        return channelListingDataList;
    }

    public static OrderPojo convert(UserPojo userPojo, UserPojo userPojo1, ChannelPojo channelPojo, String channelOrderId){
        OrderPojo orderPojo=new OrderPojo();
        orderPojo.setClientId(userPojo.getId());
        orderPojo.setChannelId(channelPojo.getId());
        orderPojo.setCustomerId(userPojo1.getId());
        orderPojo.setChannelOrderId(channelOrderId);
        orderPojo.setStatus(Status.CREATED);
        return orderPojo;
    }

    public static OrderData convert(UserPojo userPojo,UserPojo userPojo1,ChannelPojo channelPojo,OrderPojo orderPojo){
        OrderData orderData =new OrderData();
        orderData.setId(orderPojo.getId());
        orderData.setChannelOrderId(orderPojo.getChannelOrderId());
        orderData.setStatus(orderPojo.getStatus());
        orderData.setChannelName(channelPojo.getName());
        orderData.setClientName(userPojo.getName());
        orderData.setCustomerName(userPojo1.getName());
        return orderData;
    }

    public static OrderItemData convert(OrderItemPojo orderItemPojo,ProductPojo productPojo){
        OrderItemData orderItemData=new OrderItemData();
        orderItemData.setId(orderItemPojo.getId());
        orderItemData.setOrderId(orderItemPojo.getOrderId());
        orderItemData.setOrderedQuantity(orderItemPojo.getOrderedQuantity());
        orderItemData.setClientSkuId(productPojo.getClientSkuId());
        orderItemData.setSellingPricePerUnit(orderItemPojo.getSellingPricePerUnit());
        return orderItemData;
    }

    public static List<OrderItemData> convertOrderItemsList(List<OrderItemPojo> orderItemPojoList,Map<OrderItemPojo,ProductPojo> orderItemPojoProductPojoMap){
        List<OrderItemData> orderItemDataList= new ArrayList<>();
        for (OrderItemPojo orderItemPojo:orderItemPojoList){
            OrderItemData orderItemData=convert(orderItemPojo,orderItemPojoProductPojoMap.get(orderItemPojo));
            orderItemDataList.add(orderItemData);
        }
        return orderItemDataList;
    }
    public static OrderItemPojo convert(Long orderId, ProductPojo productPojo, OrderItemForm orderItemForm){
        OrderItemPojo orderItemPojo=new OrderItemPojo();
        orderItemPojo.setOrderId(orderId);
        orderItemPojo.setGlobalSkuId(productPojo.getId());
        orderItemPojo.setOrderedQuantity(orderItemForm.getOrderedQuantity());
        orderItemPojo.setAllocatedQuantity(0L);
        orderItemPojo.setFulfilledQuantity(0L);
        orderItemPojo.setSellingPricePerUnit(orderItemForm.getSellingPricePerUnit());
        return orderItemPojo;
    }

    public static OrderItemPojo convert(Long orderId,ChannelListingPojo channelListingPojo,OrderItemsForm orderItemsForm){
        OrderItemPojo orderItemPojo=new OrderItemPojo();
        orderItemPojo.setOrderId(orderId);
        orderItemPojo.setGlobalSkuId(channelListingPojo.getGlobalSkuId());
        orderItemPojo.setOrderedQuantity(orderItemsForm.getQuantity());
        orderItemPojo.setAllocatedQuantity(0L);
        orderItemPojo.setFulfilledQuantity(0L);
        orderItemPojo.setSellingPricePerUnit(orderItemsForm.getSellingPricePerUnit());
        return orderItemPojo;
    }

    public static OrderInvoiceXmlList convertToInvoiceDataList(List<OrderItemPojo> orderItemPojoList, Map<OrderItemPojo,ProductPojo> productPojoList) {
        List<OrderInvoiceData> orderInvoiceDataList = new ArrayList<>();
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            OrderInvoiceData orderInvoiceData = new OrderInvoiceData();
            orderInvoiceData.setSellingPricePerUnit(orderItemPojo.getSellingPricePerUnit());
            orderInvoiceData.setQuantity(orderItemPojo.getOrderedQuantity());
            orderInvoiceData.setName(productPojoList.get(orderItemPojo).getName());
            orderInvoiceDataList.add(orderInvoiceData);
        }
        OrderInvoiceXmlList orderInvoiceXmlList = new OrderInvoiceXmlList();
        orderInvoiceXmlList.setOrderInvoiceData(orderInvoiceDataList);
        return orderInvoiceXmlList;
    }
}
