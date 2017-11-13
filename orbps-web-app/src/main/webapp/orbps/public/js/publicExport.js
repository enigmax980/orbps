com.orbps.publicExport= {};
$(function(){
	var idTmr;
	com.orbps.publicExport.getExplorer = function() {
	    var explorer = window.navigator.userAgent ;
	    //ie 
	    if (explorer.indexOf("MSIE") >= 0) {
	        return 'ie';
	    }
	    //firefox 
	    else if (explorer.indexOf("Firefox") >= 0) {
	        return 'Firefox';
	    }
	    //Chrome
	    else if(explorer.indexOf("Chrome") >= 0){
	        return 'Chrome';
	    }
	    //Opera
	    else if(explorer.indexOf("Opera") >= 0){
	        return 'Opera';
	    }
	    //Safari
	    else if(explorer.indexOf("Safari") >= 0){
	        return 'Safari';
	    }
	}
	com.orbps.publicExport.outExcle = function(tableid) {//整个表格拷贝到EXCEL中
	    if(com.orbps.publicExport.getExplorer()==='ie'){
	        var curTbl = document.getElementById(tableid);
	        var oXL = new ActiveXObject("Excel.Application");

	        //创建AX对象excel 
	        var oWB = oXL.Workbooks.Add();
	        //获取workbook对象 
	        var xlsheet = oWB.Worksheets(1);
	        //激活当前sheet 
	        var sel = document.body.createTextRange();
	        sel.moveToElementText(curTbl);
	        //把表格中的内容移到TextRange中 
	       // sel.select;
	        //全选TextRange中内容 
	        sel.execCommand("Copy");
	        //复制TextRange中内容  
	        xlsheet.Paste();
	        //粘贴到活动的EXCEL中       
	        oXL.Visible = true;
	        //设置excel可见属性

	        try {
	            var fname = oXL.Application.GetSaveAsFilename("回执核销统计清单.xls", "Excel Spreadsheets (*.xls), *.xls");
	        } catch (e) {
	            print("Nested catch caught " + e);
	        } finally {
	            oWB.SaveAs(fname);

	            oWB.Close(savechanges = false);
	            //xls.visible = false;
	            oXL.Quit();
	            oXL = null;
	            //结束excel进程，退出完成
	            //window.setInterval("Cleanup();",1);
	            idTmr = window.setInterval("Cleanup();", 1);

	        }

	    }
	    else{
	        tableToExcel(tableid)
	    }
	}
	function Cleanup() {
	    window.clearInterval(idTmr);
	    CollectGarbage();
	}
	var tableToExcel = (function() {
	      var uri = 'data:application/vnd.ms-excel;base64,',
	      template = '<html><head><meta charset="UTF-8"></head><body><table border="1">{table}</table></body></html>',  
	        base64 = function(s) { return window.btoa(unescape(encodeURIComponent(s))) },
	        format = function(s, c) {
	            return s.replace(/{(\w+)}/g,
	            function(m, p) { return c[p]; }) }
	        return function(table, name) {
	        if (!table.nodeType) table = document.getElementById(table)
	        var ctx = {worksheet: name || 'Worksheet', table: table.innerHTML}
	        window.location.href = uri + base64(format(template, ctx))
	      }
	    })()
	    /**
	     * 判断是undefined直接返回空
	     */
	    com.orbps.publicExport.checkUndefined = function(value){
			if(value === undefined){
				return ""
			}
			return value;
		}
});