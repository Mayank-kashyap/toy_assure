var clientNameList=[];
var channelNameList=[];

function getChannelUrl(){
var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/channel";
}

function getClientUrl(){
var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/user";
}

function getChannelListingUrl(){
var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/channel_listing";
}

function getChannelListingList(){
var url = getChannelListingUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayChannelListingList(data);
	   },
	   error: handleAjaxError
	});
}

function displayChannelListingList(data){
var $tbody = $('#channel-listing-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>'  + e.channelSkuId + '</td>'
		+ '<td>'  + e.clientSkuId + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function displayUploadData(){
resetUploadDialog();
	$('#upload-channel-listing-modal').modal('toggle');
	var str='';
            for (var i=0; i < clientNameList.length;++i){
            str += '<option value="'+clientNameList[i]+'" />'; // Storing options in variable
            }
            var my_list=document.getElementById("clientName");
            my_list.innerHTML = str;
            var str1='';
                for (var i=0; i < channelNameList.length;++i){
                str1 += '<option value="'+channelNameList[i]+'" />'; // Storing options in variable

                }
                var my_list1=document.getElementById("channelName");
                my_list1.innerHTML = str1;
}

var fileData = [];
var errorData = [];
var processCount = 0;

function processData(){
    var clientName = $("#channel-listing-upload-form input[name=clientName]").val();
          var channelName = $("#channel-listing-upload-form input[name=channelName]").val();
          if(isBlank(clientName))
          {
          toastr.error("Client name cannot be empty");
          return false;
          }
          if(isBlank(channelName))
                    {
                    toastr.error("Channel name cannot be empty");
                    return false;
                    }

	var file = $('#channelListingFile')[0].files[0];
	if(isBlank(file))
              {
              toastr.error("Choose a file to upload");
              return false;
              }
	checkHeader(file,["channelSkuId","clientSkuId"],readFileDataCallback);
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

    var clientName = $("#channel-listing-upload-form input[name=clientName]").val();
      var channelName = $("#channel-listing-upload-form input[name=channelName]").val();
    var url = getChannelListingUrl()+"/list/"+clientName+"/"+channelName;

    //Make ajax call
    	$.ajax({
    	   url: url,
    	   type: 'POST',
    	   data: json,
    	   headers: {
           	'Content-Type': 'application/json'
           },
    	   success: function(response) {

    	   getChannelListingList();
    	   $('#upload-channel-listing-modal').modal('hide');
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
	var $file = $('#channelListingFile');
	$file.val('');
	$('#channelListingFileName').html("Choose File");
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
	var $file = $('#channelListingFile');
	var fileName = $file.val();
	var actualFileName=$file.get(0).files.item(0).name;
	$('#channelListingFileName').html(actualFileName);
}

function getClientList(){
var url=getClientUrl();
$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		addOptionsClient(data);
	   },
	   error: handleAjaxError
	});
}

function addOptionsClient(data){
for(var i in data){
		var e = data[i];
		if(e.type=="CLIENT")
		clientNameList.push(e.name);
		}
}

function getChannelList(){
var url=getChannelUrl();
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
		channelNameList.push(e.name);
		}
}

function init(){

	$('#close-upload').click(getChannelListingList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
    $('#channelListingFile').on('change', updateFileName)
}

$(document).ready(init);
$(document).ready(getChannelListingList);
$(document).ready(getChannelList);
$(document).ready(getClientList);