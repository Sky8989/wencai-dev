/**
 * Created by huxingyue on 2017/8/25.
 */
var pageNumKey = 1;//接口 页码
var pageSizeKey = config.pageSizeKey;//接口 每页条数
var totalPage_self;//js插件 总页数
var currentPage_self;//js插件 当前页数
var conditions = '';


//ajax更新数据
function ajax_self(currentPage, count, _conditions) {
    $.ajax({
        url: config.hostAddress + '/cms/orderExpressTransform/list?pageNumKey=' + currentPage + '&pageSizeKey=' + count + _conditions,
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
 * 页面加载好的时候，进行第一次数据渲染和分页插件的注册
 */
$('#data_list').ready(
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


//监听条件查询的按钮
//给条件查询添加 点击事件
$("#condition_select").click(function () {
    condition_select();
});


//条件查询的相关函数
/**
 * 先获取条件参数 ，拼接条件
 * 再重新注册分页插件，并查询
 */
function condition_select() {
    //清空之前的条件
    conditions = '';
    //读取现有的条件
    express_id = $("#condition_express_id").val();
    same_uuid = $("#condition_same_uuid").val();
    nation_uuid = $("#condition_nation_uuid").val();
    if (express_id != null && express_id != undefined && express_id != '') {
        //执行条件查询操作
        // 根据接口参数拼接
        var temp = '&express_id=' + express_id
        conditions = conditions + temp;
    }
    if (same_uuid != null && same_uuid != undefined && same_uuid != '') {
        //执行条件查询操作
        // 根据接口参数拼接
        var temp = '&same_uuid=' + same_uuid;
        conditions = conditions + temp;
    }
    if (nation_uuid != null && nation_uuid != undefined && nation_uuid != '') {
        //执行条件查询操作
        // 根据接口参数拼接
        var temp = '&nation_uuid=' + nation_uuid;
        conditions = conditions + temp;
    }
    console.log('条件查询的参数是：' + conditions);
    ajax_self(1, pageSizeKey, conditions);

}

//给清空按钮添加点击事件
$("#condition_empty").click(function () {
    emptyConditions();
    console.log('清空后，条件查询的参数是：' + conditions);
    ajax_self(1, pageSizeKey, conditions);
    $("#pagination1").pagination("setPage", 1, totalPage_self);// 参数2：当前页数，参数3：总页数

});


//清空查询条件
function emptyConditions() {
    $("#condition_express_id").val('');
    $("#condition_same_uuid").val('');
    $("#condition_nation_uuid").val('');
    conditions = '';
}

//数据渲染
function data_rendering(data) {
    $('#data_list').empty();//清空上一次的分页查询结果
    for (var i = 0; i < data.result.list.length; i++) {
        var dataD = data.result.list[i];
        if(dataD.is_read == 0){
            dataD.is_read = "未读"
        }if(dataD.is_read == 1){
            dataD.is_read = "已读"
        }
        $('#data_list').append(
            '<tr class="cen">' +

            '<td>' + dataD.id + '</td>' +
            '<td>' + dataD.express_id + '</td>' +
            '<td>' + dataD.same_uuid + '</td>' +
            '<td>' + dataD.nation_uuid + '</td>' +
            '<td>' + dataD.is_read + '</td>' +
            '<td>' + timeTransfer(dataD.create_time) + '</td>' +

            '<td>' +
            '<a title="其他" class="mr-5">其他</a>' +
            '</td>' +
            '</tr>'
        );
    }
}