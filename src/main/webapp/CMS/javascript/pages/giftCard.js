/**
 * Created by huxingyue on 2017/8/26.
 */
var pageNumKey = 1;//接口 页码
var pageSizeKey = config.pageSizeKey;//接口 每页条数
var totalPage_self;//js插件 总页数
var currentPage_self;//js插件 当前页数
var conditions = '';


//ajax更新数据
function ajax_self(currentPage, count, conditions) {
    $.ajax({
        url: config.hostAddress + '/cms/giftcard/list?pageNumKey=' + currentPage + '&pageSizeKey=' + count + conditions,
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
$('#giftCard_list').ready(
    function () {
        ajax_self(1, pageSizeKey, conditions);
        //分页操作
        register_pagehelper();

    }
);

// layui.use('upload', function(){
//     var upload = layui.upload;
//
//     //执行实例
//     var uploadInst = upload.render({
//         elem: '#test1' //绑定元素
//         ,url: '/upload/' //上传接口
//         ,done: function(res){
//             //上传完毕回调
//         }
//         ,error: function(){
//             //请求异常回调
//         }
//     });
// });


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
    id = $("#condition_giftcard_id").val();
    name = $("#condition_giftcard_name").val();
    type = $("#condition_giftcard_type").val();
    if (id != null && id != undefined && id != '') {
        //执行条件查询操作
        // 根据接口参数拼接
        var temp = '&id=' + id;
        conditions = conditions + temp;
    }
    if (name != null && name != undefined && name != '') {
        //执行条件查询操作
        // 根据接口参数拼接
        var temp = '&name=' + name;
        conditions = conditions + temp;
    }
    if (type != null && type != undefined && type != '') {
        //执行条件查询操作
        // 根据接口参数拼接
        var temp = '&type=' + type;
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
    $("#condition_giftcard_id").val('');
    $("#condition_giftcard_name").val('');
    $("#condition_giftcard_type").val('');
    conditions = '';
}


//数据渲染
function data_rendering(data) {
    $('#giftCard_list').empty();//清空上一次的分页查询结果
    for (var i = 0; i < data.result.list.length; i++) {
        var dataD = data.result.list[i];
        var id = dataD.id;
        var create_time = dataD.create_time;
        var name = dataD.name;
        var icon = dataD.icon;
        var type = dataD.type;
        $('#giftCard_list').append(
            '<tr class="cen">' +
            '<td>' + id + '</td>' +
            '<td>' + name + '</td>' +
            '<td><img class="gift-card-icon" src="' + icon + '" /></td>' +
            '<td>' + type + '</td>' +
            '<td>' + timeTransfer(create_time) + '</td>' +
            '<td>' +
            '<button class="layui-btn layui-btn-small"><i class="layui-icon"></i>其他</button>' +
            '<button  data-id=' + dataD.id + ' onclick="delete_self(' + dataD.id + ')" ' + ' class=" layui-btn layui-btn-small layui-btn-normal delete_giftcard "><i class="layui-icon"></i> 删除</button>' +
            '</td>' +
            '</tr>'
        );
    }

}


/////添加礼品卡的相关脚本

//监听新增礼品卡的按钮
$("#button_add_giftcarc").click(function () {
    $("#display_form").toggle("slow");
});

//layui的渲染脚本
var layui_context = $("#display_form").html();

layui.use(['layer', 'form', 'upload'], function () { //独立版的layer无需执行这一句
    var $ = layui.jquery, layer = layui.layer; //独立版的layer无需执行这一句
    var form = layui.form;
    var upload = layui.upload;


    //触发事件
    var active = {
        setTop: function () {
            var that = this;
            //多窗口模式，层叠置顶
            layer.open({
                type: 1 //此处以iframe举例
                , title: '当你选择该窗体时，即会在最顶端'
                , area: 'auto' //宽高
                , shade: 0
                , maxmin: true
                , content: layui_context
                , btn: ['全部关闭'] //只是为了演示
                , yes: function () {
                    $(that).click();
                }
                , zIndex: layer.zIndex //重点1
                , success: function (layero) {
                    layer.setTop(layero); //重点2
                }
            });
        }
    };

    $('#layerDemo .layui-btn').on('click', function () {
        var othis = $(this), method = othis.data('method');
        active[method] ? active[method].call(this, othis) : '';
    });


    //执行实例
    //  var uploadInst = upload.render({
    //  elem: '#test1' //绑定元素
    //  , url: '/upload/' //上传接口
    //  , field: 'icon' //上传接口
    //  , done: function (res) {
    //  //上传完毕回调
    //  }
    //  , error: function () {
    //  //请求异常回调
    //  }
    //  });

    //监听提交
    form.on('submit(submit_1)', function (data) {
        //layer.alert(JSON.stringify(data.field), {
        //    title: '最终的提交信息'
        //})
        var json_data = JSON.stringify(data.field);
        ajax_addGiftCard(json_data);
    });


});

//新增giftcard
function ajax_addGiftCard(json_data) {

    var post_param = JSON.parse(json_data);
    post_param.icon = (icon == undefined) ? 'uploadurl_param' : icon;


    console.log('上传之后的icon是：' + icon + '整个json是：' + JSON.stringify(post_param));

    $.ajax({
        type: "post",
        url: config.hostAddress + '/cms/giftcard/add',
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
            alert('ajax添加礼品卡成功');
            //上传成功 则隐藏 新增礼品卡的表单 并重新查询
            $("#display_form").toggle();
            ajax_self(1, pageSizeKey, '');
        },
        error: function (data) {
            console.log('数据添加失败' + 'url是：' + this.url + '数据内容是：' + JSON.stringify(post_param));
            console.log('原因：' + data.message);
            alert('添加失败，请30s后重试');
        }
    });
}

var icon;

// 七牛上传的组件 自动注册上传按钮
var uploader = Qiniu.uploader({
    runtimes: 'html5,flash,html4',      // 上传模式，依次退化
    browse_button: 'pickfiles',         // 上传选择的点选按钮，必需
    uptoken_url: config.qiniu_getToken,         // Ajax请求uptoken的Url，强烈建议设置（服务端提供）
    //uptoken_func: function () {    // 在需要获取uptoken时，该方法会被调用
    //    // do something
    //    var s;
    //    $.getJSON(config.qiniu_getToken, function (json) {
    //        var json_obj = JSON.stringify(json);
    //        s = json.uptoken;
    //        console.log('token是'+JSON.stringify(s));
    //        console.log('token 未转换是'+s);
    //        console.log('json 未转换是'+json);
    //        console.log('json_obj 未转换是'+json_obj);
    //
    //    });
    //    return s;
    //},
    get_new_uptoken: false,             // 设置上传文件的时候是否每次都重新获取新的uptoken
    // downtoken_url: '/downtoken',
    // Ajax请求downToken的Url，私有空间时使用，JS-SDK将向该地址POST文件的key和domain，服务端返回的JSON必须包含url字段，url值为该文件的下载地址
    unique_names: true,              // 默认false，key为文件名。若开启该选项，JS-SDK会为每个文件自动生成key（文件名）
    // save_key: true,                  // 默认false。若在服务端生成uptoken的上传策略中指定了sava_key，则开启，SDK在前端将不对key进行任何处理
    domain: 'http://sf.dankal.cn/',     // bucket域名，下载资源时用到，必需
    container: 'container',             // 上传区域DOM ID，默认是browser_button的父元素
    max_file_size: '5mb',             // 最大文件体积限制
    //flash_swf_url: 'path/of/plupload/Moxie.swf',  //引入flash，相对路径
    max_retries: 3,                     // 上传失败最大重试次数
    dragdrop: true,                     // 开启可拖曳上传
    drop_element: 'container',          // 拖曳上传区域元素的ID，拖曳文件或文件夹后可触发上传
    chunk_size: '1mb',                  // 分块上传时，每块的体积
    auto_start: true,                   // 选择文件后自动上传，若关闭需要自己绑定事件触发上传
    //x_vars : {
    //    查看自定义变量
    //    'time' : function(up,file) {
    //        var time = (new Date()).getTime();
    // do something with 'time'
    //        return time;
    //    },
    //    'size' : function(up,file) {
    //        var size = file.size;
    // do something with 'size'
    //        return size;
    //    }
    //},
    init: {
        'FilesAdded': function (up, files) {
            plupload.each(files, function (file) {
                // 文件添加进队列后，处理相关的事情
                console.log('处理上传ing');
            });
        },
        'BeforeUpload': function (up, file) {
            // 每个文件上传前，处理相关的事情
            console.log('开始处理上传');
        },
        'UploadProgress': function (up, file) {
            // 每个文件上传时，处理相关的事情
        },
        'FileUploaded': function (up, file, info) {
            // 每个文件上传成功后，处理相关的事情
            // 其中info是文件上传成功后，服务端返回的json，形式如：
            // {
            //    "hash": "Fh8xVqod2MQ1mocfI4S4KpRL6D98",
            //    "key": "gogopher.jpg"
            //  }
            // 查看简单反馈
            var domain = up.getOption('domain');
            //var res = JSON.parse(info);
            //console.log(res);
            var response = JSON.parse(info.response);
            var sourceLink = domain + "/" + response.key; //获取上传成功后的文件的Url
            //console.log('url' + sourceLink);
            icon = sourceLink;
            console.log('上传成功' + JSON.stringify(info));
            console.log('sourceLink是：' + sourceLink);
        },
        'Error': function (up, err, errTip) {
            //上传出错时，处理相关的事情
            console.log('上传失败');
            alert('上传失败，稍后重试');
        },
        'UploadComplete': function () {
            //队列文件处理完毕后，处理相关的事情
            console.log('队列文件处理完毕后，处理相关的事情');
            alert('上传成功，请提交');
        },
        'Key': function (up, file) {
            // 若想在前端对每个文件的key进行个性化处理，可以配置该函数
            // 该配置必须要在unique_names: false，save_key: false时才生效
            //var key = "";
            //// do something with key here
            //return key
        }
    }
});


// 删除礼品卡的请求 绑定在一个click事件上
function delete_self(id) {
    //var id = $(this).attr("data-id");
    var post_param = JSON.stringify({"id": id});
    post_param.id = id;
    $.ajax({
        type: "post",
        url: config.hostAddress + '/cms/giftcard/delete',
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
            alert('ajax删除礼品卡成功');
            //上传成功 则隐藏 新增礼品卡的表单 并重新查询
            ajax_self(1, pageSizeKey, '');
        },
        error: function (data) {
            console.log('数据删除败' + 'url是：' + this.url + '数据内容是：' + JSON.stringify(post_param));
            console.log('原因：' + data.message);
            alert('添加失败，请30s后重试');
        }
    });

}


