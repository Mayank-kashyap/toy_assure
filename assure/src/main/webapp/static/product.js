var clientIdList = [];

function getProductUrl(){
var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/product";
}

function getClientUrl(){
var baseUrl=$("meta[name=baseUrl]").attr("content")
return baseUrl+"/api/user";
}
function updateProduct(event){
	$('#edit-product-modal').modal('toggle');
	var id = $("#product-edit-form input[name=id]").val();
	var mrp=$("#product-edit-form input[name=mrp]").val();
	if(isNaN(mrp)){
               	    toastr.error("Mrp field must be a float value: "+ mrp);
                           		return false;
               	}
	var url = getProductUrl() + "/" + id;
	var $form = $("#product-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getProductList();
	   		toastr.options.closeButton=false;
            toastr.options.timeOut=3000;
            toastr.success("Product updated successfully");
            toastr.options.closeButton=true;
            toastr.options.timeOut=0;
	   },
	   error: function(response){
	   handleAjaxError(response);
	   }

	});
}

function getProductList(){
	var url = getProductUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayProductList(data);
	   },
	   error: handleAjaxError
	});
}

function displayProductList(data){
	var $tbody = $('#product-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = ' <button class="btn btn-primary" onclick="displayEditProduct(' + e.id + ')">Edit</button>';
		var row = '<tr>'
		+ '<td>' + e.clientSkuId + '</td>'
		+ '<td>'  + e.name + '</td>'
		+ '<td>'  + e.brandId + '</td>'
		+ '<td>'  + e.mrp + '</td>'
		+ '<td>'  + e.description + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function displayEditProduct(id){
	var url = getProductUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayProduct(data);
	   },
	   error: handleAjaxError
	});
}

function displayProduct(data){
    $("#product-edit-form input[name=clientSkuId]").val(data.clientSkuId);
    $("#product-edit-form input[name=name]").val(data.name);
	$("#product-edit-form input[name=brandId]").val(data.brandId);
	$("#product-edit-form input[name=mrp]").val(data.mrp);
	$("#product-edit-form input[name=description]").val(data.description);
	$("#product-edit-form input[name=id]").val(data.id);
	$('#edit-product-modal').modal('toggle');
}
function validateProductUpload(arr){
    for(var i in arr){
        var row=arr[i];
       	if(isNaN((row.mrp))){
       	    toastr.error("File cannot be uploaded: Mrp field must be a float value: "+ row.mrp);
                   		return false;
       	}

    }
    return true;
}
var fileData = [];
var errorData = [];
var processCount = 0;

function processData(){
    var clientName = $("#product-upload-form input[name=clientId]").val();
    if(isBlank(clientName))
    {
    toastr.error("Client name cannot be empty");
    return false;
    }
	var file = $('#productFile')[0].files[0];
	if(isBlank(file)){
	toastr.error("Choose a file to be uploaded");
	return false;
	}
	checkHeader(file,["clientSkuId","name","brandId","mrp","description"],readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	uploadRows();
}

function uploadRows(){
	updateUploadDialog();
	var row = fileData;
	var check=validateProductUpload(row);
	if(!check){
	return false;
	}
    var json = JSON.stringify(row);
    var clientName = $("#product-upload-form input[name=clientId]").val();
    var url = getProductUrl()+"/list/"+clientName;

    	$.ajax({
    	   url: url,
    	   type: 'POST',
    	   data: json,
    	   headers: {
           	'Content-Type': 'application/json'
           },
    	   success: function(response) {
    	   console.log(response);
    	   getProductList();
    	   $('#upload-product-modal').modal('hide');
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
	var $file = $('#productFile');
	$file.val('');
	$('#productFileName').html("Choose File");
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
	var $file = $('#productFile');
	var fileName = $file.val();
	var actualfilename=$file.get(0).files.item(0).name;
    console.log(actualfilename);
	$('#productFileName').html(actualfilename);
}

function displayUploadData(){
 	resetUploadDialog();
	$('#upload-product-modal').modal('toggle');
    var str='';
    for (var i=0; i < clientIdList.length;++i){
    str += '<option value="'+clientIdList[i]+'" />'; // Storing options in variable
    }
    var my_list=document.getElementById("clientId");
    my_list.innerHTML = str;
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
		clientIdList.push(e.name);
		}
}
function init(){
	$('#update-product').click(updateProduct);
	$('#close-upload').click(getProductList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
    $('#productFile').on('change', updateFileName)
}

$(document).ready(init);
$(document).ready(getProductList);
$(document).ready(getClientList);