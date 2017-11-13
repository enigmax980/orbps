$(function() {
    // datagrid组件初始化
    $("*").datagridsInitLoad();
    // 上一步下一步控件初始化
    $("*").stepInitLoad();
    // 日期初始化插件
    $(".date-picker").datepicker({
        autoclose : true,
        language : 'zh-CN'
    });

    // 初始化edittable组件
    $("#fbp-editDataGrid-one").editDatagridsLoadById();
    // combo组件初始化
    $("*").comboInitLoad();
});