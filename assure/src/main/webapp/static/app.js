//Function to find the current navbar item
$(function(){
    var current = location.pathname;
    $('#navbarNav a').each(function(){
        var $this = $(this);
        // if the current path is like this link, make it active
        if($this.attr('href').indexOf(current) !== -1){
            $this.addClass('active');
        }
    })
})

//function to convert form to json
function toJson($form){
    var serialized = $form.serializeArray();
    console.log(serialized);
    var s = '';
    var data = {};
    for(s in serialized){
        data[serialized[s]['name']] = serialized[s]['value']
    }
    var json = JSON.stringify(data);
    return json;
}

//function to handle error
function handleAjaxError(response){
    console.log(response.responseText);
	var response = JSON.parse(response.responseText);
    toastr.error(response.message);
}

function readFileData(file, callback){
	var config = {
		header: true,
		delimiter: ",",
		preview: 5000,
		skipEmptyLines: "greedy",
		complete: function(results) {
			callback(results);
	  	}
	}
	Papa.parse(file, config);
}


function writeFileData(arr){
	var config = {
		quoteChar: '',
		escapeChar: '',
		delimiter: ","
	};

	var data = Papa.unparse(arr, config);
    var blob = new Blob([data], {type: 'text/tsv;charset=utf-8;'});
    var fileUrl =  null;

    if (navigator.msSaveBlob) {
        fileUrl = navigator.msSaveBlob(blob, 'download.tsv');
    } else {
        fileUrl = window.URL.createObjectURL(blob);
    }
    var tempLink = document.createElement('a');
    tempLink.href = fileUrl;
    tempLink.setAttribute('download', 'download.tsv');
    tempLink.click();
}

function checkHeader(file,header_list,callback) {
	var allHeadersPresent = true;
	Papa.parse(file,{
		delimiter: ",",
		step: function(results, parser) {
		console.log(results.data);
        if(results.data.length!=header_list.length)
        allHeadersPresent=false;
        else
        {
        for(var i=0; i<header_list.length; i++){
        					if(!results.data.includes(header_list[i])){
        						allHeadersPresent = false;
        						break;
        					}
        				}
        }
        parser.abort();
        results=null;
        delete results;
    }, complete: function(results){
        results=null;
        delete results;
				if(allHeadersPresent) {
					readFileData(file,callback);
				}
				else{
					toastr.error("The file format is incorrect");
				}
    }
	});
}

function isBlank(str) {
    return (!str || /^\s*$/.test(str));
}

function isInt(n) {
   return n % 1 === 0;
}
//toast default options
toastr.options = {
  "closeButton": true,
  "debug": false,
  "progressBar": true,
  "positionClass": "toast-top-right",
  "showDuration": "none",
  "hideDuration": "none",
  "timeOut": "none",
  "extendedTimeOut": "none",
  "showEasing": "swing",
  "hideEasing": "linear",
  "showMethod": "fadeIn",
  "hideMethod": "fadeOut"
};
