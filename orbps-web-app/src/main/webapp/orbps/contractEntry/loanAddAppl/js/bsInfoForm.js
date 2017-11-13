//定义form
com.orbps.contractEntry.loanAddAppl.bsInfoForm = $('#bsInfoForm');
// 定义公共空间和list
com.orbps.common = {};
com.orbps.common.list = new Array();

$(function() {
    // 初始化edittable组件
    $('#polListTb').editDatagridsLoadById();

    // combo组件初始化
    $('*').comboInitLoad();

    // 增加表格
    $('#bsInfoForm #btnAdd').click(function() {
        $('#polListTb').editDatagrids('addRows');
    });

    // 删除表格
    $('#bsInfoForm #btnDel').click(function() {
        $('#polListTb').editDatagrids('delRows');
    });

    // 查询责任信息
    $('#bsInfoForm #btnSelect')
            .click(
                    function() {
                        var selectData = $('#bsInfoForm #polListTb')
                                .editDatagrids('getSelectRows');
                        // 判断选择的是否是一个主险，判断是否添加主险信息
                        if ((null == selectData) || (selectData.length == 0)
                                || (selectData.length > 1)) {
                            lion.util.warning('警告', '请选择一个主险信息');
                            return false;
                        }
                        com.orbps.contractEntry.loanAddAppl.addDialog.empty();
                        com.orbps.contractEntry.loanAddAppl.addDialog
                                .load(
                                        '/orbps/orbps/public/modal/html/insurRespModal.html',
                                        function() {
                                            $('#polListTb')
                                                    .editDatagridsLoadById();
                                            $(this).modal('toggle');
                                            // combo组件初始化
                                            $('#polListTb').editDatagrids(
                                                    'queryparams', selectData);
                                            // 重新加载数据
                                            $('#polListTb').editDatagrids(
                                                    'reload');
                                            setTimeout(
                                                    function() {
                                                        // 循环已经选择的责任信息
                                                        if (com.orbps.common.list != 'undefined') {
                                                            for (var i = 0; i < com.orbps.common.list.length; i++) {
                                                                var array_element = com.orbps.common.list[i];
                                                                // 判断选择的责任信息是一条还是多条
                                                                if (array_element.length >= 0) {
                                                                    if (selectData.busiPrdCode == array_element[0].busiPrdCode) {
                                                                        // 回显责任信息
                                                                        $(
                                                                                '#coverageInfo_list')
                                                                                .editDatagrids(
                                                                                        'selectRows',
                                                                                        array_element);
                                                                    }
                                                                } else {
                                                                    if (selectData.busiPrdCode == array_element.busiPrdCode) {
                                                                        $(
                                                                                '#coverageInfo_list')
                                                                                .editDatagrids(
                                                                                        'selectRows',
                                                                                        array_element);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }, 800);
                                        });
                        return false;
                    });

});
