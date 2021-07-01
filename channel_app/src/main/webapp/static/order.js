var OrderItemList = [];

function getOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");

	return baseUrl + "/api/order";
}

function getOrderItemsUrl(){
var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/orderItem";
}

function getInvoiceUrl(){
var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/channel/invoice";
}


function getChannelUrl(){
var baseUrl=$("meta[name=baseUrl]").attr("content")
return baseUrl+"/api/channel";
}

function displayAddOrderModal() {
	$("#add-order-modal").modal('toggle');
}

function addOrderItemToList(event) {
	var $form = $("#orderItem-form");
	var json = toJson($form);
	console.log(json);
	var ind = checkIfAlreadyPresent(JSON.parse(json).channelSkuId);
	if(ind==-1){
    				OrderItemList.push(JSON.parse(json));
    			}
    else{
    var qty = parseLong(OrderItemList[ind].quantity) + parseLong(JSON.parse(json).quantity);
    OrderItemList[ind].quantity = qty;
    }

getOrderItemList();
	}

function checkIfAlreadyPresent(channelSkuId){
for(var i in OrderItemList) {
		var e = OrderItemList[i];
		if(e.channelSkuId.localeCompare(channelSkuId) == 0){
			return i;
		}
	}
	return -1;
}

function getOrderItemList() {
	displayOrderItemListFrontend(OrderItemList);
}

function displayOrderItemListFrontend(data){
	console.log('Printing Order items');
	var $tbody = $('#orderItem-table').find('tbody');
	$tbody.empty();
	var srlNo=0;
	for(var i in data){
		var e = data[i];
		srlNo++;
		var buttonHtml = '<button class="btn btn-primary" onclick="deleteOrderItem(' + i + ')">delete</button>'
		var row = '<tr>'
		+ '<td>' + srlNo + '</td>'
		+ '<td>' + e.channelSkuId + '</td>'
		+ '<td>'  + e.quantity + '</td>'
		+ '<td>'  + e.sellingPricePerUnit + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function deleteOrderItem(id) {
	OrderItemList.splice(id,1);
	getOrderItemList();
}

function addOrder(event) {

	if(OrderItemList.length == 0) {
		toastr.error("Add at least one order item");
		return;
	}

    var $form = $("#order-add-form");
    var channelName=$("#order-add-form input[name=channelName]").val();
    var clientName=$("#order-add-form input[name=clientName]").val();
    var customerName=$("#order-add-form input[name=customerName]").val();
    var channelOrderId=$("#order-add-form input[name=channelOrderId]").val();
    if(isBlank(channelName))
    {
    toastr.error("Channel Name cannot be empty");
    return false;
    }
    if(isBlank(clientName))
        {
        toastr.error("Client Name cannot be empty");
        return false;
        }
        if(isBlank(customerName))
            {
            toastr.error("Customer Name cannot be empty");
            return false;
            }
            if(isBlank(channelOrderId))
                {
                toastr.error("Channel order id cannot be empty");
                return false;
                }
	var json1 = JSON.stringify(OrderItemList);

	OrderItemList=[];
	var url = getOrderUrl()+"/"+channelName+"/"+clientName+"/"+customerName+"/"+channelOrderId;

	$.ajax({
        	   url: url,
        	   type: 'POST',
        	   data: json1,
        	   headers: {
               	'Content-Type': 'application/json'
               },
        	   success: function(response) {
        	   		getOrderList(response);
                    		$("#add-order-modal").modal('toggle');
                    		toastr.options.closeButton=false;
                            toastr.options.timeOut=3000;
                            toastr.success("Order added successfully");
                            toastr.options.closeButton=true;
                            toastr.options.timeOut=0;
        	   },
        	   error:function(response){
                         				handleAjaxError(response);
                         		 }
        	});

	return false;
}

function getOrderList() {
	var url = getOrderUrl();
	$.ajax({
    		 url: url,
    		 type: 'GET',
    		 headers: {
    				'Content-Type': 'application/json'
    			 },
    		 success: function(response) {
    				displayOrdersList(response);
    		 },
    		 error: function(response){
    				handleAjaxError(response);
    		 }
    	});
}

function displayOrdersList(data) {
	var $tbody = $('#order-table').find('tbody');
	$tbody.empty();
    	for(var i in data){
    		var e = data[i];
    		var buttonHtml = ' <button class="btn btn-primary" onclick="displayViewOrderItems(' + e.id + ')">View</button>'+' <button class="btn btn-primary" onclick="generateInvoice(' + e.id + ')">Generate Invoice</button>';
    		var row = '<tr>'
    		+ '<td>'  + e.clientName + '</td>'
    		+ '<td>'  + e.customerName + '</td>'
    		+ '<td>'  + e.channelName + '</td>'
    		+ '<td>'  + e.channelOrderId + '</td>'
    		+ '<td>'  + e.status + '</td>'
    		+ '<td>' + buttonHtml + '</td>'
    		+ '</tr>';
            $tbody.append(row);
	}
}

function displayViewOrderItems(id){
var url = getOrderItemsUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	        $('#view-order-modal').modal('toggle');
	   		displayOrderItems(data);
	   },
	   error: handleAjaxError
	});
}

function displayOrderItems(data){
var $tbody = $('#order-item-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>'  + e.clientSkuId + '</td>'
		+ '<td>'  + e.orderedQuantity + '</td>'
		+ '<td>'  + e.sellingPricePerUnit + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}
function generateInvoice(id){
var url=getOrderUrl()+"/"+id;
$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(response) {
	   console.log(response);
	   if(response.status=="CREATED")
	   {
	   toastr.warning("Order not allocated");
	   return false;
	   }
	   getChannel(response.channelName,id);
	   },
	   error: function(response){
                  				handleAjaxError(response);
                  		 }
	});
}
function getChannel(name,id){
var url=getChannelUrl()+"/"+name;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(response) {
	   console.log(response);
	   if(response.invoiceType=="SELF")
	   {
	   toastr.warning("Invoice to be generated from assure api");
	   return false;
	   }
	    else
	    downloadPDF(id);
	    },
	   error: function(response){
                  				handleAjaxError(response);
                  		 }
	});
}
function downloadPDF(id){
var url = getInvoiceUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	    xhrFields: {
        responseType: 'blob'
     },
	   success: function(blob) {
	   console.log(blob);
      	var link=document.createElement('a');
      	link.href=window.URL.createObjectURL(blob);
      	link.download="Invoice_" + new Date() + ".pdf";
      	link.click();
	   },
	   error: function(response){
	   		handleAjaxError(response);
	   }
	});
}

function init(){
$("#open-add-order").click(displayAddOrderModal);
$('#add-orderItem').click(addOrderItemToList);
$('#add-order').click(addOrder);
}

$(document).ready(init);
$(document).ready(getOrderList);
