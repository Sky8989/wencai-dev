var config_url = {
    dev_hostAddress: 'https://sftc.dankal.cn/sftc',
    localhost_hostAddress: 'http://localhost:8080/sftc',
    dev_qiniu_getToken: 'https://sftc.dankal.cn/sftc/qiniu/uptoken',
    localhost_qiniu_getToken: 'http://localhost:8080/sftc/qiniu/uptoken'
};

var config = {

    hostAddress: config_url.dev_hostAddress,
    qiniu_getToken: config_url.dev_qiniu_getToken,
    pageSizeKey:10
};


$(function () {
    custom_navbar_left();
    custom_footer();
});

//时间戳转换函数
var timeTransfer = function (time) {
    return new Date(parseInt(time)).toLocaleString().replace(/:\d{1,2}$/, ' ');
};