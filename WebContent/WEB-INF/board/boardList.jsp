<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> <!-- format 라이브러리 -->
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 리스트</title>
<meta name='viewport' content='width=device-width, initial-scale=1'>
<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css'>
<script src='https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js'></script>
<script src='https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js'></script>

<script type="text/javascript">
	window.history.forward();
	function noBack(){window.history.forward();}

	$(document).ready(function(){ // 제이쿼리 문법
		$("#writeBtn").click(function(){
			location.href="<c:url value='/boardWrite.do'/>"; // c:url은 컨텍스트 경로를 달고 가지만, c:out은 컨텍스트 경로를 달고 가지 않는다. get 방식
		});
	});
	
	$(document).ready(function(){ // 제이쿼리 문법
		$("#logoutBtn").click(function(){
			postTo("<c:url value='/memberLogout.do'/>", {});
		});
	});
	
	$(document).ready(function(){ // 제이쿼리 문법
		$("#contentBtn").click(function(){
			location.href="<c:url value='/memberContent.do'><c:param name='userId' value='${userId}'/></c:url>"; // c:url은 컨텍스트 경로를 달고 가지만, c:out은 컨텍스트 경로를 달고 가지 않는다. get 방식
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
  <h2>메인화면</h2>
  <div class="panel panel-default">
    <div class="panel-heading"><b><c:out value="${userId}"/></b> 님, 환영합니다.
      <button id="contentBtn" type="button" class="btn btn-xs btn-info">내정보</button>
      <button id="logoutBtn" type="button" class="btn btn-xs btn-warning">로그아웃</button>
      <button id="writeBtn" type="button" class="btn btn-xs pull-right btn-primary">게시물 쓰기</button>
    </div>
    <div class="panel-body">
	  <div class="table-responsive">          
	  <table class="table table-hover">
	    <thead>
	      <tr>
	        <th>게시물 번호</th>
	        <th>제목</th>
	        <th>조회수</th>
	        <th>등록자</th>
	        <th>등록일</th>
	      </tr>
	    </thead>
	    <tbody>
	    <c:forEach var="b_vo" items="${b_list}">
	      <tr>
	        <td>${b_vo.idx}</td>
	        <td><a href="<c:url value='/boardContent.do?bno=${b_vo.idx}&mode=get'/>"><c:out value="<c:out value='${b_vo.title}'/>"/></a></td>
	        <td>${b_vo.count}</td>
	        <td><c:out value="<c:out value='${b_vo.writer}'/>"/></td>
	        <td><fmt:formatDate pattern="yyyy-MM-dd" value="${b_vo.indate}"/></td> <!-- 내가 원하는 패턴으로 시간 표시(Date -> String) -->
	      </tr>
	    </c:forEach>
	    </tbody>
	  </table>
	  </div>
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