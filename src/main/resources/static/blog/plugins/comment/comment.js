$('#commentSubmit').click(function () {
    var blogId = $('#blogId').val();
    var commentBody = $('#commentBody').val();
    if (isNull(blogId)) {
        swal("参数异常", {
            icon: "warning",
        });
        return;
    }
    // if (isNull(commentator)) {
    //     swal("请输入你的称呼", {
    //         icon: "warning",
    //     });
    //     return;
    // }
    // if (isNull(email)) {
    //     swal("请输入你的邮箱", {
    //         icon: "warning",
    //     });
    //     return;
    // }
    // if (isNull(verifyCode)) {
    //     swal("请输入验证码", {
    //         icon: "warning",
    //     });
    //     return;
    // }
    // if (!validCN_ENString2_100(commentator)) {
    //     swal("请输入符合规范的名称(不要输入特殊字符)", {
    //         icon: "warning",
    //     });
    //     return;
    // }
    if (!validCN_ENString2_100(commentBody)) {
        swal("请输入符合规范的评论内容(不要输入特殊字符)", {
            icon: "warning",
        });
        return;
    }
    var data = {
        "blogId": blogId, commentBody: commentBody
    }
    console.log(data);
    $.ajax({
        type: 'POST',
        url: blogId + '/comment',
        data: data,
        success: function (result) {
            if (result.resultCode === 200) {
                swal("评论成功, 自动刷新网页", {
                    icon: "success",
                });
                setTimeout(function () {
                    location.reload();
                }, 1000);
            }
            else {
                swal(result.message, {
                    icon: "error",
                });
            }
        },
        error: function () {
            swal("操作失败", {
                icon: "error",
            });
        }
    });
});