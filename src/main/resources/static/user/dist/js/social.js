$(function () {
    // 在页面加载时设置初始显示状态
    var currentTab = $("#currentTab").val();
    if (currentTab === 'follow') {
        $("#followsContainer").show();
        $("#fansContainer").hide();
    } else {
        $("#fansContainer").show();
        $("#followsContainer").hide();
    }

    $(window).resize(function () {
        $("#jqGrid").setGridWidth($(".card-body").width());
    });
});

function follow(toUserId) {
    $.ajax({
        type: "GET",
        url: "/user/follow",
        data: {
            "toUserId": toUserId
        },
        success: function (r) {
            if (r.resultCode === 200) {
                $('#del' + toUserId).attr("style", "display:block");
                $('#add' + toUserId).attr("style", "display:none");
            } else {
                swal(r.message, {
                    icon: "error"
                });
            }
        }
    });
}

function unfollow(toUserId) {
    $.ajax({
        type: "GET",
        url: "/user/unfollow",
        data: {
            "toUserId": toUserId
        },
        success: function (r) {
            if (r.resultCode === 200) {
                $('#del' + toUserId).attr("style", "display:none");
                $('#add' + toUserId).attr("style", "display:block");
            } else {
                swal(r.message, {
                    icon: "error"
                });
            }
        }
    });
}

function showFollow() {
    window.location.href = "/user/social?tab=follow";
}

function showFans() {
    window.location.href = "/user/social?tab=fans";
}

function handleSearch(event) {
    if (event.key === 'Enter') {
        triggerSearch();
    }
}

function triggerSearch() {
    var searchQuery = $("#searchInput").val();
    var currentTab = $("#currentTab").val();
    window.location.href = `/user/social/search?query=${encodeURIComponent(searchQuery)}&tab=${currentTab}`;
}
