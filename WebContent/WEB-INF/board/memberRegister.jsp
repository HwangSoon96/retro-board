<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/> <%-- EL의 내장객체인 pageContext 이용하면 contextPath 가져올 수 있다. --%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<meta name='viewport' content='width=device-width, initial-scale=1'>
<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css'>
<script src='https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js'></script>
<script src='https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js'></script>

<script type="text/javascript">
	$(document).ready(function(){
		<c:if test="${!empty msg_register_error}"> // 서버 검사에서 가입이 거절된 경우
			alert("${msg_register_error}");
			<c:remove var="msg_register_error" scope="session"/>
		</c:if>
	});
	
	function register(){
		document.form1.action="<c:url value='/memberInsert.do'/>"; // form1의 action을 지정해서
		document.form1.submit(); // 값들을 보내준다.
	}

	function frmreset(){
		document.form1.reset(); // form1의 값들을 원래대로 리셋시켜 준다.
	}
	
	function doublecheck(){
		if($("#id").val()==''){
			alert("아이디를 입력해주세요");
			$("#id").focus(); // 아이디 입력창으로 커서 이동
			return;
		}
		var id=$("#id").val();
		$.ajax({ // 비동기 통신, 중복체크를 눌렀을 때는 다른 페이지로 이동하는 것이 아닌 회원가입 페이지를 그대로 유지해야 한다.
			url : "<c:url value='/memberDbcheck.do'/>", // 해당 url로 서버에 요청, c:url은 Context경로 포함한다.
			type : "POST", // POST 방식 통신
			data : {"id" : id}, // 클라에서 서버로 넘겨주는 데이터{"이름" : 실제 데이터}
			success : dbCheck, // callback 함수, 클라가 url로 요청 -> 서버가 클라에서 넘어오는 값을 받아 일을 한 후 -> 성공 결과를 callback 함수로 받아 처리
			error : function(){ alert("error"); }
		});
	}
	
	function dbCheck(data){
		if(data == "YES"){
			alert("중복된 아이디 입니다");
			$("#id").focus();
		}else{
			alert("사용 가능한 아이디 입니다");
			$("#pass").focus();
		}
	}
</script>

</head>
<body>
<div class="container">
  <h2>회원 가입 화면</h2>
  <div class="panel panel-default">
    <div class="panel-heading">
      <label>회원 가입을 진행해주세요</label>
    </div>
    <div class="panel-body">
	  <form id="form1" name="form1" class="form-horizontal" method="post">
	    <div class="form-group">
	      <label class="control-label col-sm-2" for="id">아이디:</label>
	      <div class="col-sm-10">
	      	<table>
	      	  <tr>
	      	    <td><input type="text" class="form-control" id="id" name="id" placeholder="아이디를 입력해주세요" style="width: 230px"></td>
	      	    <td style="padding-left: 5px"><input type="button" value="중복체크" onclick="doublecheck()" class="btn btn-warning"></td>
	      	  </tr>
	        </table>
	      </div>
	    </div>
	    <div class="form-group">
	      <label class="control-label col-sm-2" for="pass">비밀번호:</label>
	      <div class="col-sm-10">          
	        <input type="password" class="form-control" id="pass"  name="pass" placeholder="비밀번호를 입력해주세요" style="width: 230px">
	      </div>
	    </div>
	    <div class="form-group">
	      <label class="control-label col-sm-2" for="name">이름:</label>
	      <div class="col-sm-10">          
	        <input type="text" class="form-control" id="name"  name="name" style="width: 10%">
	      </div>
	    </div>
	    <div class="form-group">
	      <label class="control-label col-sm-2" for="age">나이:</label>
	      <div class="col-sm-10">          
	        <input type="text" class="form-control" id="age"  name="age" style="width: 10%">
	      </div>
	    </div>
	    <div class="form-group">
	      <label class="control-label col-sm-2" for="pass">이메일:</label>
	      <div class="col-sm-10">          
	        <input type="text" class="form-control" id="email"  name="email" placeholder="이메일을 입력해주세요" style="width: 230px">
	      </div>
	    </div>
	  </form>
    </div>
    <div class="panel-footer" style="text-align: center">
      <input type="button" value="가입" class="btn btn-primary" onclick="register()"/>
      <input type="button" value="취소" onclick="location.href='${ctx}/boardLogin.do'" class="btn btn-danger"/>
    </div>
  </div>
</div>
</body>
</html>