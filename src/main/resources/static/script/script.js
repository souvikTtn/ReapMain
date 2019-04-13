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
            success:function (data,status,xhr) {
                setTimeout(location.reload.bind(location), 3000);
                var x=xhr.getResponseHeader("MyResponseHeader");
                if(x==="notExist"){
                   $("#errorAlert").append(data);
                   $("#errorAlert").show();
                }
                if(x==="selfRecognition"){
                    $("#selfRecogAlert").append(data);
                    $("#selfRecogAlert").show();
                }
                if(x==="successful"){
                    $("#successAlert").append(data);
                    $("#successAlert").show();
                }

            },
            error:function (data) {
                alert("failed")
            }
        })
    });

    $("#successAlert").click(function () {
       window.location.reload();
    });

    $("#errorAlert").click(function () {
        window.location.reload();
    });

    $("#selfRecogAlert").click(function () {
        window.location.reload();
    });

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

    $(".revokeRecognitionButtonClass").click(function () {
        var answer=confirm("Are you sure you want to revoke this recognition");
        if(answer==true){
            console.log($(this).closest(".recognitionIdClass").find("input[name='recognitionIdToRevoke']").val());
            var id=$(this).closest(".recognitionIdClass").find("input[name='recognitionIdToRevoke']").val();
            $.ajax({
                method:"PUT",
                url:"/revokeBadges/"+id,
                success:function (data) {
                    console.log("success")
                    alert("revoked badge sucessfully");
                    window.location.reload();
                }
            })
        }
        else {
            console.log("revoke");
        }
    });

    $(".addItemToCartButton").click(function () {
        var itemId = $(this).closest(".itemRow").find("input[name='itemId']").val();
        var userId = $(this).closest(".itemRow").find("input[name='userId']").val();
        console.log(itemId);
        $.ajax({
           method:"POST",
            url:"/addToCart/"+itemId,
            success:function (data) {
                alert("item successfully added to the cart");
            }
        })
    })

    $(".removeCartItemButton").click(function () {
        var itemId = $(this).closest(".cartRow").find("input[name='cartItemId']").val();
        $.ajax({
            method:"PUT",
            url:"/removeFromCart/"+itemId,
            success:function (data) {
                alert("item successfully removed")
                window.location.reload();
            }
        })
    })
});