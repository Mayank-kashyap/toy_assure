function getProductUrl(){
var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/product";
}

function updateProduct(event){
	$('#edit-product-modal').modal('toggle');
	//Get the ID
	var id = $("#product-edit-form input[name=id]").val();
	var url = getProductUrl() + "/" + id;

	//Set the values to update
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
	   error: handleAjaxError
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

var fileData = [];
var errorData = [];
var processCount = 0;

function processData(){
	var file = $('#productFile')[0].files[0];
	checkHeader(file,["clientSkuId","name","brandId","mrp","description"],readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	uploadRows();
}

function uploadRows(){
	//Update progress
	updateUploadDialog();
	var row = fileData;
	console.log(row);
    var json = JSON.stringify(row);
    console.log(json);
    var clientId = $("#product-upload-form input[name=clientId]").val();
    var url = getProductUrl()+"/list/"+clientId;

    //Make ajax call
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