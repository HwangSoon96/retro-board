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
	window.history.forward();
	function noBack() {window.history.forward();}
	
	$(document).ready(function(){ // 제이쿼리 문법
		$("#listBtn").click(function(){
			location.href="<c:url value='/boardList.do'/>"; // c:url은 컨텍스트 경로를 달고 가지만, c:out은 컨텍스트 경로를 달고 가지 않는다. get 방식
		});
	});
	
	$(document).ready(function(){ // 제이쿼리 문법
		$("#logoutBtn").click(function(){
			postTo("<c:url value='/memberLogout.do'/>", {});
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
<!-- 관리자 -->
<c:if test="${sessionScope.userId!=null && sessionScope.userAuth.equals('A') && sessionScope.userId!=m_vo.id}">
<div class="container" style="padding-top: 5px">
	<h2>상세 정보 보기</h2>
	<div class="panel panel-default">
    <div class="panel-heading"><b><c:out value="${userId}"/></b> 님, 환영합니다.
      <button id="logoutBtn" type="button" class="btn btn-xs btn-warning">로그아웃</button><br><br>
      <b><c:out value="${m_vo.id}"/></b> 회원의 상세 정보
    </div>
    <div class="panel-body">
	<input type="hidden" name="num" value="${m_vo.num}"/>
	<div class="table-responsive"> 
	<table class="table table-bordered">
		<c:if test="${m_vo!=null}">
		<tr>
		  <th class="col-md-1">회원 번호</th>
		  <td class="col-md-3">${m_vo.num}</td>
		</tr>
		<tr>
		  <th>아이디</th>
		  <td><c:out value="${m_vo.id}"/></td>
		</tr>
		<tr>
		  <th>이름</th>
		  <td><c:out value="${m_vo.name}"/></td>
		</tr>
		<tr>
		  <th>나이</th>
		  <td>${m_vo.age}</td>
		</tr>
		<tr>
		  <th>이메일</th>
		  <td><c:out value="${m_vo.email}"/></td>
		</tr>
		</c:if>
		</table>		
		</div>
		<div align="center">
	  		<button id="adminListBtn" class="btn btn-default">목록</button>
		</div>
	</div>
    <div class="panel-footer text-right">최태순</div>
    </div>
</div>
</c:if>
<!-- 일반 사용자 -->
<c:if test="${sessionScope.userId!=null && sessionScope.userAuth.equals('U') && sessionScope.userId.equals(m_vo.id)}">
<div class="container" style="padding-top: 5px">
  <h2>상세 정보 보기</h2>
  <div class="panel panel-default">
    <div class="panel-heading"><b><c:out value="${userId}"/></b> 님의 상세 정보
      <button id="logoutBtn" type="button" class="btn btn-xs btn-warning">로그아웃</button>
    </div>
	<div class="panel-body">
	<input type="hidden" name="num" value="${m_vo.num}"/>
	<div class="table-responsive"> 
	<table class="table table-bordered">
		<c:if test="${m_vo!=null}">
		<tr>
		  <th class="col-md-1">회원 번호</th>
		  <td class="col-md-3">${m_vo.num}</td>
		</tr>
		<tr>
		  <th>아이디</th>
		  <td><c:out value="${m_vo.id}"/></td>
		</tr>
		<tr>
		  <th>이름</th>
		  <td><c:out value="${m_vo.name}"/></td>
		</tr>
		<tr>
		  <th>나이</th>
		  <td>${m_vo.age}</td>
		</tr>
		<tr>
		  <th>이메일</th>
		  <td><c:out value="${m_vo.email}"/></td>
		</tr>
		</c:if>
		</table>		
		</div>
		<div align="center">
		    <button id="listBtn" class="btn btn-default">목록</button>
		</div>
	</div>
    <div class="panel-footer text-right">최태순</div>
    </div>
</div>
</c:if>
<c:if test="${sessionScope.userId==null || sessionScope.userAuth==null || sessionScope.userId!=m_vo.id}">
	<c:if test="${sessionScope.userAuth!='A'}">
	<div align="center" style="padding-top: 200px">
		<button id="errorBtn" type="button" class="btn btn-lg btn-danger">눌러주세요</button>
	</div>
	</c:if>
</c:if>
<c:if test="${sessionScope.userId==null || sessionScope.userAuth==null || sessionScope.userId.equals(m_vo.id)}">
	<c:if test="${sessionScope.userAuth.equals('A')}">
	<div align="center" style="padding-top: 200px">
		<button id="errorBtn" type="button" class="btn btn-lg btn-danger">눌러주세요</button>
	</div>
	</c:if>
</c:if>
</body>
</html>