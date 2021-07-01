function getBinUrl(){
var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/bin";
}

function getBinSkuUrl(){
var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/binSku";
}

function add(event){
var url=getBinUrl();
$.ajax({
	   url: url,
	   type: 'POST',
	   success: function(data) {
	   		toastr.options.closeButton=false;
            toastr.options.timeOut=10000;
            toastr.success("Bin created successfully with binID: "+data);
            toastr.options.closeButton=true;
            toastr.options.timeOut=0;
	   },
	   error: handleAjaxError
	});
}

function displayUploadData(){
 	resetUploadDialog();
	$('#upload-inventory-modal').modal('toggle');
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#inventoryFile');
	$file.val('');
	$('#inventoryFileName').html("Choose File");
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
	var $file = $('#inventoryFile');
	var fileName = $file.val();
	var actualfilename=$file.get(0).files.item(0).name;
    console.log(actualfilename);
	$('#inventoryFileName').html(actualfilename);
}

var fileData = [];
var errorData = [];
var processCount = 0;

function processData(){
	var file = $('#inventoryFile')[0].files[0];
	if(isBlank(file))
    	toastr.error("Choose a file to be uploaded");
    	else
	checkHeader(file,["binId","clientSkuId","quantity"],readFileDataCallback);
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
    var url = getBinSkuUrl()+"/list";

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
    	   getBinList();
    	   $('#upload-inventory-modal').modal('hide');
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

function getBinList(){
var url = getBinSkuUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBinList(data);
	   },
	   error: handleAjaxError
	});
}

function displayBinList(data){
var $tbody = $('#bin-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>' + e.binId + '</td>'
		+ '<td>'  + e.clientSkuId + '</td>'
		+ '<td>'  + e.quantity + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function init(){
$('#create-bin').click(add);
$('#upload-data').click(displayUploadData);
$('#process-data').click(processData);
$('#inventoryFile').on('change', updateFileName)
}

$(document).ready(init);
$(document).ready(getBinList);