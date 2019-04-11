$(document).ready(function (e) {

    $("#recognize").submit(function (e) {
        e.preventDefault();
    });
    $("#recognize").submit(function (e) {
        var form=$(this);
        $.ajax({
            type:'POST',
            url:'/recognizeNewer',
            data:form.serialize(),
            success:function (data) {
               // document.write(data);
                alert(data);
                window.location.reload();
            },
            error:function (data) {
                alert("failed")
            }
        })
    })

    $("#searchRecognition").submit(function (e) {
        e.preventDefault();
    });
    $("#searchRecognition").submit(function (e) {
        var form=$("#searchRecognition");
        $.ajax({
            type:'POST',
            url:"/searchRecogByName",
            data:form.serialize(),
            success: function (data) {
                $("#recognitionResults").hide();
                $("#userdataDiv").empty();
                data.forEach(function (e) {
                    $("#recognitionResults").show();
                    $("#userdataDiv").append(
                        "<strong> " +
                        e.receiverName +
                        "</strong> has received a " +
                        e.badge +
                        " from " +
                        e.senderName +
                        " for " +
                        e.reason +
                        " because " +
                        e.comment +
                        "<br>" +
                        " on " + e.date.year+"-"+e.date.monthOfYear+"-"+e.date.dayOfMonth +
                        "<br>"
                    )
                });
                $("#searchUser").val("");
            },
            error:function () {
                alert("failed");
            }
        })
    });
    $("#logout").click(function (e) {
        e.preventDefault();
        $.ajax({
            type:'POST',
            url:'/logout',
            success:function (data) {
                window.location.reload();
            },
            error:function () {
                alert("failed")
            }
        })
    })

    //for search by date and display
    $("#today").click(function (e) {
        ajaxDateSearch("today");
    })

    $("#yesterday").click(function (e) {
        ajaxDateSearch("yesterday");
    })

    $("#last7Days").click(function (e) {
        ajaxDateSearch("last7Days");
    })
    $("#last30Days").click(function (e) {
        ajaxDateSearch("last30Days");
    })

    function ajaxDateSearch(dateStr) {
        $.ajax({
            type:"GET",
            url:"/searchRecogByDate/"+dateStr,
            success: function (data) {

                $("#userdataDiv").empty();
                data.forEach(function (e) {
                    $("#recognitionResults").show()
                    $("#userdataDiv").append(
                        "<strong> " +
                        e.receiverName +
                        "</strong> has received a " +
                        e.badge +
                        " from " +
                        e.senderName +
                        " for " +
                        e.reason +
                        " because " +
                        e.comment +
                        "<br>" +
                        " on " + e.date.year+"-"+e.date.monthOfYear+"-"+e.date.dayOfMonth +
                        "<br>"
                    )
                });
            },
            error:function () {
                alert("failed");
            }
        })
    }

    $("#searchUser").autocomplete({
        source: function (request, response) {
            $.ajax({
                method: 'GET',
                url: "/autocomplete",
                data: {"pattern": $("#searchUser").val()},
                success: function (data) {
                    var availableUsers = [];
                    data.forEach(function (e) {
                        availableUsers.push(e.fullName)
                    });
                    response(availableUsers);
                }
            })
        }
    });

    $("#recognizeNewerSearch").autocomplete({
        source:function (request,response) {
            $.ajax({
                method:"GET",
                url:"/autocomplete",
                data: {"pattern": $("#recognizeNewerSearch").val()},
                success:function (data) {
                    var availableUsers=[];
                    data.forEach(function (e) {
                        availableUsers.push(e.fullName)

                    });
                    response(availableUsers);
                }
            })
        }
    });
    $('input[type="file"]').change(function(e){
        var fileName = e.target.files[0].name;
        $("#fileSelector").text(fileName)

    });
})