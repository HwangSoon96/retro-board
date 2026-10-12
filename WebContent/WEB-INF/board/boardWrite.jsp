<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
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
	window.history.forward();
	function noBack(){window.history.forward();}

	$(document).ready(function(){ // 제이쿼리 문법
		$("#logoutBtn").click(function(){
			postTo("<c:url value='/memberLogout.do'/>", {});
		});
	});

	$(document).ready(function(){ // 제이쿼리 문법
		$("#listBtn").click(function(){
			location.href="<c:url value='/boardList.do'/>"; // c:url은 컨텍스트 경로를 달고 가지만, c:out은 컨텍스트 경로를 달고 가지 않는다. get 방식
		});
	});
	
	$(document).ready(function(){ // 제이쿼리 문법
		$("#contentBtn").click(function(){
			location.href="<c:url value='/memberContent.do'><c:param name='userId' value='${userId}'/></c:url>"; // c:url은 컨텍스트 경로를 달고 가지만, c:out은 컨텍스트 경로를 달고 가지 않는다. get 방식
		});
	});
	
	$(document).ready(function(){ // 제이쿼리 문법
		$("#adminListBtn").click(function(){
			location.href="<c:url value='/boardAdmin.do'/>"; // c:url은 컨텍스트 경로를 달고 가지만, c:out은 컨텍스트 경로를 달고 가지 않는다. get 방식
		});
	});
	
	$(document).ready(function(){ // 제이쿼리 문법
		$("#errorBtn").click(function(){
			alert("잘못된 접근입니다. 다시 로그인해주세요.");
			location.href="<c:url value='/boardLogin.do'/>"; // c:url은 컨텍스트 경로를 달고 가지만, c:out은 컨텍스트 경로를 달고 가지 않는다. get 방식
		});
	});
	// 데이터를 바꾸는 요청은 주소(GET)가 아닌 POST로 보내고, 세션의 CSRF 토큰을 함께 보낸다.
	function postTo(url, params){
		var form = $('<form method="post"></form>').attr("action", url);
		params._csrf = "${sessionScope.csrfToken}";
		$.each(params, function(name, value){
			form.append($('<input type="hidden">').attr("name", name).val(value));
		});
		$(document.body).append(form);
		form.submit();
	}
</script>
</head>
<body onload="noBack();" onpageshow="if(event.persisted) noBack();" onunload="">
<c:if test="${sessionScope.userId!=null && sessionScope.userAuth!=null}">
<div class="container" style="padding-top: 5px">
  <h2>게시판 글쓰기</h2>
  <div class="panel panel-default">
     <div class="panel-heading"><b><c:out value="<c:out value='${userId}'/>"/></b> 님, 환영합니다.
     <c:if test="${sessionScope.userId!=null && sessionScope.userAuth.equals('U')}">
      <button id="contentBtn" type="button" class="btn btn-xs btn-info">내정보</button>
     </c:if>
      <button id="logoutBtn" type="button" class="btn btn-xs btn-warning">로그아웃</button>
    </div>
    <div class="panel-body">
     <form action="<c:url value='/boardInsert.do'/>" method="post">
     <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}"/>
	  <div class="form-group">
	    <label>제목</label>
	    <input type="text" class="form-control" id="title" name="title"> <!-- form에서 넘어가는 데이터 이름은 name, id는 자바스크립트나 제이쿼리 -->
	  </div>
	  <div class="form-group">
	    <label>내용</label>
	    <textarea class="form-control" rows="4" id="contents" name="contents"></textarea>
	  </div>
	  <div class="form-group">
	    <label>작성자</label>
	    <input type="text" class="form-control" id="writer" name="writer" value="<c:out value='${userId}'/>" readonly="readonly">
	  </div >
	  <div class="form-group" align="center">
	  	<button type="submit" class="btn btn-success">등록</button>
	  <c:if test="${sessionScope.userId!=null && sessionScope.userAuth.equals('U')}">
	  	<button type="button" id="listBtn" class="btn btn-danger">취소</button>
	  </c:if>
	  <c:if test="${sessionScope.userId!=null && sessionScope.userAuth.equals('A')}">
	  	<button type="button" id="adminListBtn" class="btn btn-danger">취소</button>
	  </c:if>
	  </div >
	 </form>
    </div>
    <div class="panel-footer text-right">최태순</div>
  </div>
</div>
</c:if>
<c:if test="${sessionScope.userId==null || sessionScope.userAuth==null}">
	<div align="center" style="padding-top: 200px">
		<button id="errorBtn" type="button" class="btn btn-lg btn-danger">눌러주세요</button>
	</div>
</c:if>
</body>
</html>