function getUserUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/user";
}

function addUser(event){
	var $form = $("#user-add-form");
	var json = toJson($form);
	var url = getUserUrl();

    	$.ajax({
    	   url: url,
    	   type: 'POST',
    	   data: json,
    	   headers: {
           	'Content-Type': 'application/json'
           },
    	   success: function(response) {
    	   		getUserList();
    	   		$('#add-user-modal').modal('hide');
    	   		toastr.options.closeButton=false;
    	   		toastr.options.timeOut=3000;
    	   		toastr.success("User added successfully");
    	   		toastr.options.closeButton=true;
                toastr.options.timeOut=0;
    	   },
    	   error: handleAjaxError
    	});
	return false;
}


function getUserList(){
	var url = getUserUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayUserList(data);
	   },
	   error: handleAjaxError
	});
}

function displayUserList(data){
	var $tbody = $('#user-table').find('tbody');
	$tbody.empty();
	for(let i in data){
		let e = data[i];
		var row = '<tr>'
		+ '<td>' + e.name + '</td>'
		+ '<td>'  + e.type + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function displayAddUser(){
    $('#add-user-modal').modal('toggle');
}

function init(){
	$('#submit-user').click(addUser);
	$('#add-user').click(displayAddUser);
}

$(document).ready(init);
$(document).ready(getUserList);