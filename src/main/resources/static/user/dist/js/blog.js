$(function () {
    $("#jqGrid").jqGrid({
        url: '/blog/list',
        datatype: "json",
        colModel: [
            {label: 'id', name: 'blogId', index: 'blogId', width: 50, key: true, hidden: true},
            {label: '标题', name: 'title', index: 'title', width: 140},
            {label: '标签', name: 'tags', index: 'tags', width: 80}, // 新增标签列
            {label: '浏览量', name: 'views', index: 'views', width: 60},
            {label: '状态', name: 'status', index: 'status', width: 60, formatter: statusFormatter},
            // {label: '博客分类', name: 'blogCategoryName', index: 'blogCategoryName', width: 60},
            {label: '创建时间', name: 'createAt', index: 'createAt', width: 90}
        ],
        height: 700,
        rowNum: 10,
        rowList: [10, 20, 50],
        styleUI: 'Bootstrap',
        loadtext: '信息读取中...',
        rownumbers: false,
        rownumWidth: 20,
        autowidth: true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader: {
            root: "data.list",
            page: "data.currPage",
            total: "data.totalPage",
            records: "data.totalCount"
        },
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order",
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
    });

    $(window).resize(function () {
        $("#jqGrid").setGridWidth($(".card-body").width());
    });

    function coverImageFormatter(cellvalue) {
        return "<img src='" + cellvalue + "' height=\"120\" width=\"160\" alt='coverImage'/>";
    }

    function statusFormatter(cellvalue) {
        if (cellvalue == 0) {
            return "<button type=\"button\" class=\"btn btn-block btn-secondary btn-sm\" style=\"width: 50%;\">草稿</button>";
        }
        else if (cellvalue == 1) {
            return "<button type=\"button\" class=\"btn btn-block btn-success btn-sm\" style=\"width: 50%;\">发布</button>";
        }
    }

});

/**
 * 搜索功能
 */
function search() {
    // 标题关键字
    var keyword = $('#keyword').val();
    var searchType = $('#searchType').val();
    if (!validLength(keyword, 20)) {
        swal("搜索字段长度过大!", {
            icon: "error",
        });
        return false;
    }
    // 数据封装
    var searchData = {
        keyword: keyword,
        searchType: searchType
    };
    // 传入查询条件参数
    $("#jqGrid").jqGrid("setGridParam", { postData: searchData });
    // 点击搜索按钮默认都从第一页开始
    $("#jqGrid").jqGrid("setGridParam", { page: 1 });
    // 提交post并刷新表格
    $("#jqGrid").jqGrid("setGridParam", { url: '/blog/list' }).trigger("reloadGrid");
}

/**
 * jqGrid重新加载
 */
function reload() {
    var page = $("#jqGrid").jqGrid('getGridParam', 'page');
    $("#jqGrid").jqGrid('setGridParam', {
        page: page
    }).trigger("reloadGrid");
}

function addBlog() {
    window.location.href = "/blog/edit";
}

function editBlog() {
    let id = getSelectedRow();
    if (id == null) {
        return;
    }
    // console.log("/blog/edit/" + id);
    window.location.href = "/blog/edit/" + id;
}

function deleteBlog() {
    const ids = getSelectedRows();
    if (ids == null) {
        return;
    }
    swal({
        title: "确认弹框",
        text: "当真要删吗?",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    }).then((flag) => {
            if (flag) {
                $.ajax({
                    type: "POST",
                    url: "/blog/delete",
                    contentType: "application/json",
                    data: JSON.stringify(ids),
                    success: function (r) {
                        if (r.resultCode === 200) {
                            swal("删除成功", {
                                icon: "success",
                            });
                            $("#jqGrid").trigger("reloadGrid");
                        } else {
                            swal(r.message, {
                                icon: "error",
                            });
                        }
                    }
                });
            }
        }
    );
}