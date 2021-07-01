var clientNameList=[];
var customerNameList=[];

function getOrderUrl(){
var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order";
}

function getOrderItemsUrl(){
var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/orderItem";
}

function getInvoiceUrl(){
var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/invoice";
}

function getClientUrl(){
var baseUrl=$("meta[name=baseUrl]").attr("content")
return baseUrl+"/api/user";
}

function getChannelUrl(){
var baseUrl=$("meta[name=baseUrl]").attr("content")
return baseUrl+"/api/channel";
}

function getOrderList(){
var url = getOrderUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayOrderList(data);
	   },
	   error: handleAjaxError
	});
}

function displayOrderList(data){
var $tbody = $('#order-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = ' <button class="btn btn-primary" onclick="displayViewOrderItems(' + e.id + ')">View</button> '+'<button class="btn btn-primary" onclick="allocateOrder(' + e.id + ')">Allocate</button> '+'</button> '+'<button class="btn btn-primary" onclick="generateInvoice(' + e.id + ')">Generate Invoice</button>';
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
	   error: function(response){
	   handleAjaxError(response);
	   }
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

function allocateOrder(id){
var url=getOrderUrl()+"/"+id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(response) {
	   console.log(response);
	   if(response.status!="CREATED")
	   {
	   toastr.warning("Order already allocated");
	   return false;
	   }
	   else
	   allocateOrderFinal(id);
	   },
	   error: function(response){
                  				handleAjaxError(response);
                  		 }
	});
}
function allocateOrderFinal(id){
var url=getOrderUrl() + "/update/" + id;
console.log(url);
$.ajax({
	   url: url,
	   type: 'PUT',
	   success: function(response) {
	    getOrderDetails(id);
	   		getOrderList();

	   },
	   error: function(response){
                  				handleAjaxError(response);
                  		 }
	});
}

function getOrderDetails(id){
var url=getOrderUrl()+"/"+id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(response) {
	   console.log(response);
	   if(response.status=="CREATED")
	   {
	   toastr.options.closeButton=false;
                   toastr.options.timeOut=5000;
                   toastr.success("Order partially allocated");
                   toastr.options.closeButton=true;
                   toastr.options.timeOut=0;
	   }
	   else{
	   toastr.options.closeButton=false;
                          toastr.options.timeOut=5000;
                          toastr.success("Order allocated successfully");
                          toastr.options.closeButton=true;
                          toastr.options.timeOut=0;
	   }

	   },
	   error: function(response){
                  				handleAjaxError(response);
                  		 }
	});
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
	   if(response.invoiceType!="SELF")
	   {
	   toastr.warning("Invoice to be generated from channel api");
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

function displayUploadData(){
resetUploadDialog();
	$('#upload-order-modal').modal('toggle');
	var str='';
        for (var i=0; i < clientNameList.length;++i){
        str += '<option value="'+clientNameList[i]+'" />'; // Storing options in variable
        }
        var my_list=document.getElementById("clientName");
        my_list.innerHTML = str;
        var str1='';
            for (var i=0; i < customerNameList.length;++i){
            str1 += '<option value="'+customerNameList[i]+'" />'; // Storing options in variable
            }
            var my_list1=document.getElementById("customerName");
            my_list1.innerHTML = str1;
}
function validateUpload(arr){
    for(var i in arr){
        var row=arr[i];
       	if(isNaN((row.sellingPricePerUnit))){
       	    toastr.error("File cannot be uploaded: Selling price field must be a float value: "+ row.sellingPricePerUnit);
                   		return false;
       	}
        if(isNaN(parseInt(row.orderedQuantity)) || !isInt(row.orderedQuantity)){
                	    toastr.error("File cannot be uploaded: Quantity field must be an integer value: "+ row.orderedQuantity);
                                		return false;
                	}
    }
    return true;
}
var fileData = [];
var errorData = [];
var processCount = 0;

function processData(){
    var clientName = $("#order-upload-form input[name=clientName]").val();
         var channelOrderId = $("#order-upload-form input[name=channelOrderId]").val();
          var customerName = $("#order-upload-form input[name=customerName]").val();
          if(isBlank(clientName))
          {
          toastr.error("Client name cannot be empty")
          return false;
          }
          if(isBlank(customerName))
                    {
                    toastr.error("Customer name cannot be empty")
                    return false;
                    }
                    if(isBlank(channelOrderId))
                              {
                              toastr.error("Channel orderID cannot be empty")
                              return false;
                              }
	var file = $('#orderFile')[0].files[0];
	if(isBlank(file))
    	{
    	toastr.error("Choose a file to be uploaded");
    	return false;
    	}
	checkHeader(file,["clientSkuId","orderedQuantity","sellingPricePerUnit"],readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	uploadRows();
}

function uploadRows(){
	//Update progress
	updateUploadDialog();
	var row = fileData;
    if(!validateUpload(row))
    {
    return false;
    }
    var json = JSON.stringify(row);

    var clientName = $("#order-upload-form input[name=clientName]").val();
     var channelOrderId = $("#order-upload-form input[name=channelOrderId]").val();
      var customerName = $("#order-upload-form input[name=customerName]").val();

    var url = getOrderUrl()+"/list/"+clientName+"/"+channelOrderId+"/"+customerName;

    //Make ajax call
    	$.ajax({
    	   url: url,
    	   type: 'POST',
    	   data: json,
    	   headers: {
           	'Content-Type': 'application/json'
           },
    	   success: function(response) {

    	   getOrderList();
    	   $('#upload-order-modal').modal('hide');
    	   		toastr.options.closeButton=false;
                toastr.options.timeOut=3000;
                toastr.success("File uploaded successfully");
                toastr.options.closeButton=true;
                toastr.options.timeOut=0;
    	   },
    	   error: function(response){
    	        console.log(response);
                toastr.error("File cannot be uploaded: "+JSON.parse(response.responseText).message);
    	   }
    	});
}

function downloadErrors(){
	writeFileData(errorData);
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#orderFile');
	$file.val('');
	$('#orderFileName').html("Choose File");
	//Reset various counts
	processCount = 0;
	fileData = [];
	errorData = [];
	//Update counts
	updateUploadDialog();
}

function updateUploadDialog(){
	$('#rowCount').html("" + fileData.length);
}

function updateFileName(){
	var $file = $('#orderFile');
	var fileName = $file.val();
	var actualfilename=$file.get(0).files.item(0).name;
	$('#orderFileName').html(actualfilename);
}

function getClientList(){
var url=getClientUrl();
$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		addOptions(data);
	   },
	   error: handleAjaxError
	});
}

function addOptions(data){
for(var i in data){
		var e = data[i];
		if(e.type=="CLIENT")
		clientNameList.push(e.name);
		else
		customerNameList.push(e.name);
		}
}


function init(){

	$('#close-upload').click(getOrderList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
    $('#orderFile').on('change', updateFileName)
}

$(document).ready(init);
$(document).ready(getOrderList);
$(document).ready(getClientList);