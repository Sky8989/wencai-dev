/**
 * Created by huxingyue on 2017/8/26.
 */
var custom_navbar_left = function () { //公共的侧边栏部分
    $('#custom_side-nav').append(
        '<div class="side-logo">' +
        ' <div class= "logo" > <strong> 顺丰如时送达后台管理系统 </strong> </div>' +
        ' </div>' +

        ' <nav class="side-menu content mCustomScrollbar" data-mcs-theme="minimal-dark">' +
        ' <h2>' + ' <a href="user.html" class="InitialPage"><i class="icon-dashboard"></i>用户管理</a>' + ' </h2>' +
        '<h2>' + '<a href="order.html" class="InitialPage"><i class="icon-dashboard"></i>订单管理</a>' + '</h2>' +
        '<h2>' + '<a href="orderExpress.html" class="InitialPage"><i class="icon-dashboard"></i>快递管理（包裹）</a>' + '</h2>' +
        '<h2>' + '<a href="token.html" class="InitialPage"><i class="icon-dashboard"></i>token管理</a>' + '</h2>' +
        '<h2>' + '<a href="giftCard.html" class="InitialPage"><i class="icon-dashboard"></i>礼品卡管理</a>' + ' </h2>' +
        '<h2>' + '<a href="question.html" class="InitialPage"><i class="icon-dashboard"></i>常见问题管理</a>' + '</h2>' +
        '<h2>' + '<a href="orderExpressTransform.html" class="InitialPage"><i class="icon-dashboard"></i>兜底单列表</a>' + '</h2>' +
        '<h2>' + '<a href="cancleedOrder.html" class="InitialPage"><i class="icon-dashboard"></i>订单取消记录列表</a>' + '</h2>' +
        '<h2>' + '<a href="root.html" class="InitialPage"><i class="icon-dashboard"></i>管理员</a>' + '</h2>' +
        '</nav>' + '<footer class="side-footer">© DANKAL 版权所有</footer>'
    )


};

var custom_footer = function () { //公共的footer部分
    // $('#custom_btm-ft').append(
    //     '<p class="clear"> ' +
    //     '<span class="fr text-info"> <em class="uppercase"> <i class="icon-user"></i>系统技术支持 : *****</em> </span> ' +
    //     '</p>'
    // )
};