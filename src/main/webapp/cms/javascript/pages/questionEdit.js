/**
 * Created by samsunug on 2017/9/27.
 */

window.onload=function (){
    var id =  localStorage.getItem("id");
    var title =  localStorage.getItem("questEditTitle");
    var content =  localStorage.getItem("questEditContent");
    $("input[name='id']").val(id);
    $("input[name='title']").val(title);
    $("textarea[name='content']").val(content);
    localStorage.clear();
}

layui.use('form',function () {
    var layer = layui.layer;
    var form = layui.form;
    //监听提交
    form.on('submit(formDemo)',function (data) {
        var json_data = JSON.stringify(data.field);
        ajax_updateCommonQuestion(json_data);

    });
});

function ajax_updateCommonQuestion(json_data) {

    var post_param = JSON.parse(json_data);

    $.ajax({
        type: "post",
        url: config.hostAddress + '/cms/commonQuestion/update',
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
            window.location.href = 'question.html';
            //上传成功 则隐藏 新增常见问题的表单 并重新查询
        },
        error: function (data) {
            console.log('数据修改失败' + 'url是：' + this.url + '数据内容是：' + JSON.stringify(post_param));
            console.log('原因：' + data.message);
            alert('修改失败，请30s后重试');
        }
    });
}
