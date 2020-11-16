$(document).ready(function () {
    $("#search-form").submit(function (event) {
        //stop submit the form, we will post it manually.
        event.preventDefault();
        fire_ajax_submit();
    });
});

function fire_ajax_submit() {
    var search = {}
    search["filter"] = $("#filter").val();
    //search["email"] = $("#email").val();
    $("#btn-search").prop("disabled", true);
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/api/string",
        data: JSON.stringify(search),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            var netDisks = data.body;
            var tableNetDisks = document.getElementById("tableNetDisks");
            netDisks.foreach(function(netDisk, index) {
                var tr = tableNetDisks.insertRow(-1);
                var td1 = tr.insertCell(-1);
                td1.innerText = netDisk.name;
                var td2 = tr.insertCell(-1);
                td2.innerText = netDisk.path + "/" + netDisk.name;
            })

            console.log("SUCCESS : ", data);
            $("#btn-search").prop("disabled", false);
        },
        error: function (e) {
            var json = "<h4>Ajax Response</h4><pre>"
                + e.responseText + "</pre>";
            $('#feedback').html(json);
            console.log("ERROR : ", e);
            $("#btn-search").prop("disabled", false);
        }
    });
}