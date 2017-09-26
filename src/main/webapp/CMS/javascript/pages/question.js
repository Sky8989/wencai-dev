/**
 * Created by huxingyue on 2017/8/25.
 */
var pageNumKey = 1;//接口 页码
var pageSizeKey = config.pageSizeKey;//接口 每页条数
var totalPage_self;//js插件 总页数
var currentPage_self;//js插件 当前页数
var conditions = '';


//ajax更新数据
function ajax_self(currentPage, count, conditions) {
    $.ajax({
        url: config.hostAddress + '/cms/commonQuestion/list?pageNumKey=' + currentPage + '&pageSizeKey=' + count + conditions,
        type: 'get',
        dataType: 'json',
        scriptCharset: 'utf-8',
        async: false, // 同步执行
        cache: false,//不缓存
        success: function (data) {
            if (data.state != 200) {
                if (data.state == 500) {
                    alert('service error：' + data.message);
                    return;
                }
                if (data.state == 401) {
                    alert('查询到结果：' + data.message);
                    return;
                }

            }
            //写入本次查询 获取的结果 当前页数 总页数
            totalPage_self = data.result.pages;
            currentPage_self = data.result.pageNum;
            //数据渲染
            data_rendering(data);
        },
        error: function (data) {
            console.log('数据获取失败' + '请求url是：' + this.url);

        }
    });
}

/**
 * 列表加载好的时候，进行第一次数据渲染和分页插件的注册
 */
$('#question_list').ready(
    function () {
        ajax_self(1, pageSizeKey, conditions);
        //分页操作
        register_pagehelper();

    }
);

/**
 * 分页插件的初始化方法
 */
function register_pagehelper() {
    $("#pagination1").pagination({
        currentPage: currentPage_self,// 当前页数
        totalPage: totalPage_self,// 总页数
        isShow: true,// 是否显示首尾页
        count: totalPage_self > 10 ? 10 : totalPage_self,// 显示个数
        homePageText: "首页",// 首页文本
        endPageText: "尾页",// 尾页文本
        prevPageText: "上一页",// 上一页文本
        nextPageText: "下一页",// 下一页文本
        callback: function (currentPage) {
            ajax_self(currentPage, pageSizeKey, conditions);
            $("#pagination1").pagination("setPage", currentPage, totalPage_self);// 参数2：当前页数，参数3：总页数
        }
    });
}

//数据渲染
function data_rendering(data) {
    $('.question_list').empty();//清空上一次的分页查询结果
    for (var i = 0; i < data.result.length; i++) {
        var dataD = data.result[i];
        var id = dataD.id;
        var create_time = dataD.create_time;
        var title = dataD.title;
        var content = dataD.content;
        $('.question_list').append(
            '<tr class="cen">' +
            '<td>' + id + '</td>' +
            '<td class="td-title" contenteditable="true">' + title + '</td>' +
            '<td class="lt td-content" contenteditable="true">' + content + '</td>' +
            '<td>' + timeTransfer(create_time) + '</td>' +
            '<td>' +
            '<a title="保存" data-id=' + dataD + 'href="javascript:void(0);" onclick="ajax_updateCommonQuestion(' + dataD + ')" '+' class="mr-5" >保存</a>' +
            '<a title="删除" data-id=' + dataD.id + 'href="javascript:void(0);" onclick="delete_self(' + dataD.id + ')" '+' class="mr-5">删除</a>' +
            '</td>' +
            '</tr>'
        );

    }

}

//监听新增常见问题的按钮
$("#addCommonQuestion").click(function () {
    $("#display_form").toggle("slow");
});

//layui的渲染脚本
var layui_context = $("#display_form").html();

layui.use( 'form', function () { //独立版的layer无需执行这一句
    var layer = layui.layer; //独立版的layer无需执行这一句
    var form = layui.form;

    //监听提交
    form.on('submit(submit_1)', function (data) {
        // layer.alert(JSON.stringify(data.field), {
        //     title: '最终的提交信息'
        // })
        var json_data = JSON.stringify(data.field);
        ajax_addCommonQuestion(json_data);
    });

});

//新增commentQuestion
function ajax_addCommonQuestion(json_data) {

    var post_param = JSON.parse(json_data);

    $.ajax({
        type: "post",
        url: config.hostAddress + '/cms/commonQuestion/add',
        async: false, // 使用同步方式
        data: JSON.stringify(post_param),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data) {
            if (data.state != 200) {
                if (data.state == 500) {
                    alert('service error：' + data.message);
                    return;
                }
                if (data.state == 401) {
                    alert('新增失败：' + data.message);
                    return;
                }
                if (data.state == 403) {
                    alert('参数问题：' + data.message);
                    return;
                }
            }
            alert('ajax常见问题添加成功');
            //上传成功 则隐藏 新增常见问题的表单 并重新查询
            $('.question_list').empty();
            ajax_self(1, pageSizeKey, '');
        },
        error: function (data) {
            console.log('数据添加失败' + 'url是：' + this.url + '数据内容是：' + JSON.stringify(post_param));
            console.log('原因：' + data.message);
            alert('添加失败，请30s后重试');
        }
    });
}

// 删除常见问题
function delete_self(id) {
    //var id = $(this).attr("data-id");
    var post_param = JSON.stringify({"id": id});
    post_param.id = id;
    $.ajax({
        type: "post",
        url: config.hostAddress + '/cms/commonQuestion/delete',
        async: false, // 使用同步方式
        data: JSON.stringify(post_param),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data) {
            if (data.state != 200) {
                if (data.state == 500) {
                    alert('service error：' + data.message);
                    return;
                }
                if (data.state == 401) {
                    alert('新增失败：' + data.message);
                    return;
                }
                if (data.state == 403) {
                    alert('参数问题：' + data.message);
                    return;
                }
            }
            alert('ajax删除常见问题成功');
            //删除成功重新查询
            ajax_self(1, pageSizeKey, '');
        },
        error: function (data) {
            console.log('数据删除败' + 'url是：' + this.url + '数据内容是：' + JSON.stringify(post_param));
            console.log('原因：' + data.message);
            alert('添加失败，请30s后重试');
        }
    });
}


//修改commentQuestion
function ajax_updateCommonQuestion(dataD) {
    var layer = layui.layer;
    layer.alert(dataD);
    // var title = $(".td-title").val();
    // var content = $(".lt .td-content").val();
    // dataD.title = title;
    // dataD.content = content;
    // var post_param = JSON.parse(dataD);
    //
    // if (title != null && title != undefined && title != ''&& content != null
    //     && content != undefined && content != '') {
    //
    //     $.ajax({
    //         type: "post",
    //         url: config.hostAddress + '/cms/commonQuestion/update',
    //         async: false, // 使用同步方式
    //         data: JSON.stringify(post_param),
    //         contentType: "application/json; charset=utf-8",
    //         dataType: "json",
    //         success: function (data) {
    //             if (data.state != 200) {
    //                 if (data.state == 500) {
    //                     alert('service error：' + data.message);
    //                     return;
    //                 }
    //                 if (data.state == 401) {
    //                     alert('修改失败：' + data.message);
    //                     return;
    //                 }
    //                 if (data.state == 403) {
    //                     alert('参数问题：' + data.message);
    //                     return;
    //                 }
    //             }
    //             alert('ajax常见问题修改成功');
    //             //上传成功 则隐藏 修改常见问题的表单 并重新查询
    //             $('.question_list').empty();
    //             ajax_self(1, pageSizeKey, '');
    //         },
    //         error: function (data) {
    //             console.log('数据添加失败' + 'url是：' + this.url + '数据内容是：' + JSON.stringify(post_param));
    //             console.log('原因：' + data.message);
    //             alert('修改失败，请30s后重试');
    //         }
    //     });
    // }else {
    //     alert('标题或问题内容不可为空');
    // }

}
