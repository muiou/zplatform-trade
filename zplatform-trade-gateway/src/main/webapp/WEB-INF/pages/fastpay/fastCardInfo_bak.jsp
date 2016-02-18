<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<title>收银台_快捷支付</title>
<link
	href="http://58.30.231.28:8080/static/css/v1/pay/public.css?v=V1.01.0044_Release"
	rel="stylesheet" type="text/css" />
<link
	href="http://58.30.231.28:8080/static/css/v1/pay/account.css?v=V1.01.0044_Release"
	rel="stylesheet" type="text/css" />
<link
	href="http://58.30.231.28:8080/static/css/v1/pay/pay.css?v=V1.01.0044_Release"
	rel="stylesheet" type="text/css" />
<script src="http://localhost:8080/zplatform-trade/js/jquery-1.11.2.js"></script>
<script
	src="http://58.30.231.28:8080/static/validate/formValidator-4.0.1.js"
	type="text/javascript" charset="UTF-8" />
<script
	src="http://58.30.231.28:8080/static/validate/formValidatorRegex.js?v=V1.01.0044_Release"
	type="text/javascript" charset="UTF-8" />
<script
	src="http://58.30.231.28:8080/static/js/gateway/recharge.js?v=V1.01.0044_Release" />
<script src="http://58.30.231.28:8080/static/js/plugins/jquery-ui.js" />
<script src="http://58.30.231.28:8080/static/js/common/pay-dialog.js" />
<link
	href="http://58.30.231.28:8080/static/css/dialog/jquery-ui.custom.css?v=V1.01.0044_Release"
	rel="stylesheet" type="text/css" />

</head>
<body>
	<!--header str-->
	<div class="header_box">
		<div class="header">
			<h1 class="logo">
				<a href="/website/"><img alt="证联金融" title="证联金融"
					src="http://58.30.231.28:8080/static/img/v1/pay/logo.png"></a><i
					class="ver_txt">收银台</i>
			</h1>
			<!-- <div class="header_bar">
				<a href="/website/">首页</a> |
				<a href="#">提点建议</a> |
				<a class="help" href="/website/help/index.htm" target="_blank">帮助中心</a>
			</div> -->
		</div>
	</div>


	<!--/header end-->
	<!--主内容 str -->
	<div class="container">
		<div class="step mb10">
			<ol class="step-2">
				<!-- 改变ol里的step-数字 即可 1.2.3.4 -->
				<li class="li-1">1.选择银行</li>
				<li class="li-2">2.填写银行卡信息</li>
				<li class="li-3">3.登录到网上银行充值</li>
				<li class="li-4">4.交易成功</li>
			</ol>
		</div>
		<!--支付信息 -->
		<div class="mod_box">

			<div class="bd pd10 pay_info">
				<div class="ov_hid bd mt30">
					<form method="post" action="/zplatform-trade/gateway/toPay.htm"
						id="form1" class="">
						<input type="hidden" name="bankCode" id="bankCode" value="${trade.bankCode}"  /> 
						<input type="hidden" name="orderId" value="${trade.orderId}" /> 
						<input type="hidden" name="amount" value="${trade.amount}" />
						<input type="hidden" name="txnseqno" value="${trade.txnseqno}" />
						<input type="hidden" name="merchId" value="${trade.merchId }" />
						<input type="hidden" name="cashCode" value="${trade.cashCode }" />
						<input type="hidden" name="busicode" value="${trade.busicode }" />
						<ul class="mt10 mod_list ">
							<li><label class="mod_side">银行：</label>
								<p class="myWay">
									<span id="myWay"><label class="bk construction bank_cur"
										title="中国建设银行"></label></span>
								</p>
							<li><label class="mod_side">银行卡卡号：</label><input
								name="cardNo" id="cardNo" type="text" class="w180 inp_normal"
								value="4367420010680459591"><span id="cardNoTip"></span></li>
							<li><label class="mod_side">持卡人姓名：</label><input
								name="acctName" id="acctName" type="text"
								class="w180 inp_normal" value="估计" /><span id="acctNameTip"></span></li>
							<li><label class="mod_side">持卡人身份证：</label><input
								name="certId" id="certId" type="text" class="w180 inp_normal"
								value="110105198610094115" /> <span id="idNoTip"></span></li>
							<li><label class="mod_side">交易金额：</label>${trade.amount/100}<span
								id="amountTip"></span></li>
							<li><label class="mod_side">手机号：</label><input name="mobile"
								id="mobile" type="text" class="w180 inp_normal"
								value="18600806796"> <span id="mobileTip"></span></li>
							<li><input type="checkbox" id="chkAgree"> <label
								for="fast_payment">同意 <span style="color: blue;"><a
										href="javascript:void()" class="blues" target="_blank">《快捷支付服务相关协议》</a></span>（本次即可享受快捷支付保障）
							</label></li>
							<li class="submit mt30"><button class="btn btn_off"
									id="btnSubmit" type="submit">下一步</button> <input type="button"
								class="btn btnA" id="cancelButton" value="取消" /></li>
						</ul>
					</form>
				</div>
				<!--/网银支付 -->
			</div>
		</div>
		<!--/支付信息 -->
	</div>
	<!--主内容 end -->

	<div id="openTip" style="display: none;">
		<div class="txtC">
			<div class="result_tips pop_tips">
				<div class="set_height clearfix">
					<!--icon调用的class   pop_s:√  pop_war:! pop_fail:X-->
					<span class="pop_war"></span>
					<p class="words f16b">充值完成前不要关闭此窗口!</p>
					<p class="mt10">请您在新打开的网上银行页面上完成充值。</p>
					<p>完成充值后请根据情况点击一下按钮：</p>
					<p class="link mt10">
						<a class="btn btnA" style="color: black;" type="button"
							onclick="javascript:location.href='/website/app/trade/recharge_record.htm';">已完成充值</a>
						<a class="btn btnA2" style="color: black;" type="button"
							onclick="javascript:location.href='/website/help/zhcz.html';">充值遇到问题</a>
					</p>
				</div>
			</div>
		</div>
	</div>

	<!--footer -->
	<div class="footer">
		<p class="foot_nav">
			<a href="/website/help/index.htm" target="_blank">关于我们</a> | <a
				href="/website/help/service.htm" target="_blank">隐私条款</a> | <a
				href="/website/help/mer.htm" target="_blank">合作加盟</a> | <a
				href="/website/help/reg.htm" target="_blank">会员服务</a>
		</p>
		<p class="copyright">Copyright &copy; 2015 北京证联资本管理有限责任公司版权所有</p>
		<p class="contact_info">
			<span>联系电话：010-84298418</span> <span>传真：010-84299579</span>
			<!--  <span>版本：2013V1.4 Beta</span> -->
		</p>
		<p class="icp">
			<a href=" http://www.miibeian.gov.cn/" target="_blank">京ICP备15034871号</a>
		</p>
	</div>
	<script src="/static/js/v1/pay/account.js?v=V1.01.0044_Release"></script>
	<script type="text/javascript">	
var rechargeWin;
	$(function() {	
	$.formValidator.initConfig({
			formID : "form1",		
			debug : false,
			submitOnce : true,
			forceValid : false,	
			onError : function(msg, obj, errorlist) {
				//alert(msg);
			},
			submitAfterAjaxPrompt : '有数据正在异步验证，请稍等...'
		});
			
		
		$("#cardNo").formValidator({onShow:"请输入卡号",onFocus:"请输入卡号",onCorrect:""})
	    .inputValidator({min:12,max:19,onError:"银行卡号必须为12-19位"})
	  .regexValidator({regExp:"num",dataType:"enum",onError:"银行卡号必须为12-19位数字"});
	    
	    $("#acctName").formValidator({onShow:"请输入姓名",onFocus:"请输入姓名",onCorrect:""})
	    .inputValidator({min:1,max:20,empty:{leftEmpty:false,rightEmpty:false,emptyError:"真实姓名不能有空符号"},onError:"输入正确的真实姓名，请确认"});
	    
	    /* $("#idNo").formValidator({onShow:"请输入身份证号码",onFocus:"请输入身份证号码",onCorrect:""})
	    .inputValidator({min: 1,onError:"请输入正确的身份证号码"})
	    .regexValidator({regExp:"idcard",dataType:"enum",onError:"身份证号格式不正确"}) */
	    
	    
	    $("#idNo").formValidator({
			empty : false,
			onShow : "请填写真实的证件号码,身份证号码为15位或18位",
			onFocus : "请填写真实的证件号码,身份证号码为15位或18位",
			onCorrect : " "
		}).inputValidator({
			empty : {
				leftEmpty : false,
				rightEmpty : false,
				emptyError : "证件号码两边不能有空符号"
			},
			max : 18,
			onError : "请填写正确的证件号码"
		}).functionValidator({
			fun : isCardID
		});
	    
	    
	    
	    $("#amount").formValidator({validatorGroup:"1",onShow:"请输入金额",onFocus:"请输入金额",onCorrect:" "})
	.regexValidator({regExp:"^(-)?(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){1,2})?$",onError:"金额不正确"})
	.inputValidator({empty:false,min:0.01,max:999999999999.99,type:"number",onError:"请输入正确金额，请确认"});
	    
	    $("#mobile").formValidator({onShow:"请输入手机号",onFocus:"请输入手机号",onCorrect:" "})
		.inputValidator({min:11,max:11,onError:"请输入有效的手机号"})
		.regexValidator({regExp:"mobile",dataType:"enum",onError:"手机号格式不正确"});
	   
	    $("#form1").submit(function(){
	    	var cardNo=document.getElementsByName('cardNo')[0].value;
			var acctName=document.getElementsByName('acctName')[0].value;
			var idNo=document.getElementsByName('idNo')[0].value;
			var amount=document.getElementsByName('amount')[0].value;
			var mobile=document.getElementsByName('mobile')[0].value;
			
			/* if(cardNo!="" && acctName!="" && idNo!="" && amount!="" && mobile!="" ){
		    	rechargeWin=$.pay.window($("#openTip").html(),{title:'充值到账户',isClose:false,url:false,iframe:false,h:'auto',w:480});
		    } */
			
			
		});
	   
	    	
	$("#chkAgree").click(function(){
		if(this.checked==true){
			$("#btnSubmit").removeClass("btn_off").addClass("btnA").removeAttr("disabled");
		}else{
			$("#btnSubmit").removeClass("btnA").addClass("btn_off").attr("disabled","disabled");
		}
	});
	
	
	$("#cancelButton").click(function(){	
	  closeWin();
	});
	
	
	
	}
	);
	
	function closeWin() {
		history.go(-1);
	}
	
		
	
</script>
	<script type="text/javascript">
	
	
	
</script>
</body>
</html>
