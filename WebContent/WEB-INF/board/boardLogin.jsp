<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>

<script type="text/javascript">
	$(document).ready(function(){
	    <c:if test="${!empty msg_register}"> // msg가 비어있지 않다면 = 회원가입 성공
	      alert("${msg_register}") // msg를 출력, 제이쿼리에 EL 쓰면 이클립스에서 에러 나는데 신경 안써도 된다.
	      <c:remove var="msg_register"/>
	    </c:if>
	      
	    <c:if test="${!empty msg_login}"> // msg가 비어있지 않다면 = 로그인 실패
	      alert("${msg_login}") // msg를 출력, 제이쿼리에 EL 쓰면 이클립스에서 에러 나는데 신경 안써도 된다.
	      <c:remove var="msg_login" scope="session"/> // session으로 객체 바인딩을 했기 때문에 한번 alert 해주고 삭제 해줘야 한다. 안 그러면 계속 alert 뜬다.
	    </c:if>
	      
	 });
	
	function check(){
		  if($('#user_id').val()==''){ // 아이디 입력하는 form의 id에 입력된 값을 가져온다.
			  alert("아이디를 입력해주세요");
			  return false;
		  }
		  
		  if($('#password').val()==''){ // 비밀번호 입력하는 form의 id에 입력된 값을 가져온다.
			  alert("비밀번호를 입력해주세요");
			  return false;
		  }
		  
		  return true; // 값이 모두 입력되어서 true를 리턴하면 그 때 action의 ${ctx}/memberLogin.do로 넘어간다.
	}
</script>

</head>
<body>
<div class="container" style="padding-top: 200px">
    <div class="row">
		<div class="col-md-4 col-md-offset-4">
    		<div class="panel panel-default">
			  	<div class="panel-heading">
			    	<label>게시판 로그인</label>
			 	</div>
			  	<div class="panel-body">
			    <form action="${ctx}/memberLogin.do" method="post">
		    	  	<div class="form-group">
		    		    <input class="form-control" type="text" placeholder="아이디를 입력해주세요" id="user_id" name="user_id">
		    		</div>
		    		<div class="form-group">
		    			<input class="form-control" type="password" placeholder="비밀번호를 입력해주세요" id="password" name="password">
		    		</div>
		    		<button class="btn btn-lg btn-success btn-block" type="submit" onclick="return check()">Login</button>
			    </form>
			    </div>
			    <div class="panel-footer">
			    	<button class="btn btn-sm btn-primary btn-block" onclick="location.href='${ctx}/memberRegister.do'">
			    		회원가입
			    	</button>
			    </div>
			</div>
		</div>
	</div>
</div>
</body>
</html>