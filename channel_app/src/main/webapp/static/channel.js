function getChannelUrl(){
var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/channel";
}

function addChannel(event){
	//Set the values to update
	var $form = $("#channel-add-form");
	var json = toJson($form);
	var url = getChannelUrl();

    	$.ajax({
    	   url: url,
    	   type: 'POST',
    	   data: json,
    	   headers: {
           	'Content-Type': 'application/json'
           },
    	   success: function(response) {
    	   		getChannelList();
    	   		$('#add-channel-modal').modal('hide');
    	   		toastr.options.closeButton=false;
    	   		toastr.options.timeOut=3000;
    	   		toastr.success("Channel added successfully");
    	   		toastr.options.closeButton=true;
                toastr.options.timeOut=0;
    	   },
    	   error: function(response){
    	   handleAjaxError(response);
    	   }
    	});
	return false;
}

function getChannelList(){
var url = getChannelUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayChannelList(data);
	   },
	   error: function(response)
	   {
	   handleAjaxError(response);
	   }
	});
}

function displayChannelList(data){
var $tbody = $('#channel-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>'  + e.name + '</td>'
		+ '<td>'  + e.invoiceType + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function displayAddChannel(){
$('#add-channel-modal').modal('toggle');
}
function init(){

	$('#close-upload').click(getChannelList);
    $('#submit-channel').click(addChannel);
    $('#add-channel').click(displayAddChannel);
}

$(document).ready(init);
$(document).ready(getChannelList);