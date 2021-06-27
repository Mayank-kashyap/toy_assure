function getOrderUrl(){
var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order";
}

function getOrderItemsUrl(){
var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/orderItem";
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
		var buttonHtml = ' <button class="btn btn-primary" onclick="displayViewOrderItems(' + e.id + ')">View</button> '+'<button class="btn btn-primary" onclick="allocateOrder(' + e.id + ')">Allocate</button>';
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

function allocateOrder(id){
var url=getOrderUrl()+"/"+id;
}
function displayUploadData(){
resetUploadDialog();
	$('#upload-order-modal').modal('toggle');
}

var fileData = [];
var errorData = [];
var processCount = 0;

function processData(){
	var file = $('#orderFile')[0].files[0];
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

    var json = JSON.stringify(row);

    var clientName = $("#order-upload-form input[name=clientName]").val();
     var channelOrderId = $("#order-upload-form input[name=channelOrderId]").val();
      var customerName = $("#order-upload-form input[name=customerName]").val();
    var url = getOrderUrl()+"/"+clientName+"/"+channelOrderId+"/"+customerName;

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

function init(){

	$('#close-upload').click(getOrderList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
    $('#orderFile').on('change', updateFileName)
}

$(document).ready(init);
$(document).ready(getOrderList);