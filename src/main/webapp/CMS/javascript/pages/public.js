var config = {
    dev_hostAddress: 'https://sftc.dankal.cn/sftc',
    localhost_hostAddress: 'http://localhost:8080/sftc',
    dev_qiniu_getToken: 'https://sftc.dankal.cn/sftc/qiniu/uptoken',
    localhost_qiniu_getToken: 'https://localhost:8080/sftc/qiniu/uptoken',
    hostAddress: 'http://localhost:8080/sftc',
    qiniu_getToken: 'https://localhost:8080/sftc/qiniu/uptoken',
    pageSizeKey:10
};

$(function () {
    custom_navbar_left();
    custom_footer();
});

//时间戳转换函数
var timeTransfer = function (time) {
    return new Date(parseInt(time)).toLocaleString().replace(/:\d{1,2}$/, ' ');
}