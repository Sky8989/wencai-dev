/**
 * Created by huxingyue on 2017/8/25.
 */
$.ajax({
    url: config.hostAddress + '/cms/commonQuestion/list?pageNumKey=1&pageSizeKey=5',
    type: 'get',
    dataType: 'json',
    scriptCharset: 'utf-8',
    success: function (data) {

        for (var i = 0; i < data.result.length; i++) {
            var dataD = data.result[i];
            var id = dataD.id;
            var create_time = dataD.create_time;
            var title = dataD.title;
            var content = dataD.content;
            $('.question_list').append(
                '<tr class="cen">' +
                '<td>' + id + '</td>' +
                '<td>' + title + '</td>' +
                '<td class="lt td-content">' + content + '</td>' +
                '<td>' + timeTransfer(create_time) + '</td>' +
                '<td>' +
                '<a title="编辑" class="mr-5">其他</a>' +
                '<a title="删除" class="mr-5">其他</a>' +
                '</td>' +
                '</tr>'
            );


        }

    },
    error: function (data) {
        alert("resolve failed" + url);

    }
});
