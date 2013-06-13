<%@ include file="header.jsp"%>
<%@ page import="me.liye.tekken.www.BuildSite" %>
<%@ page import="java.util.*" %>

<form method="post">
<caption>build wiki page</caption>
<input type="hidden" name="action" value="buildWikiPage">
<table border="1">
<tr>
<td>www path</td>
<td><input name="wwwDir" size="100"></td>
</tr>
<tr>
<td>template path</td>
<td><input name="templateDir" size="100"></td>
</tr>
<tr>
<td>url</td>
<td><input name="url" size="100">
<span class="memo" style="font:50%">
<br>TT2:http://wiki.livedoor.jp/inatekken/d/%c1%ed%b9%e7TUD
<br>BR:http://wiki.livedoor.jp/inatekken/d/%c1%ed%b9%e7TBR
</span>

</td>

</tr>
<tr>
<td>type</td>
<td><input name="type" size="100"></td>
</tr>

</table>
<input type="submit" value="execute">
</form>
<%
String action = request.getParameter("action");
if(null!=action){

String wwwDir = request.getParameter("wwwDir");
String templateDir = request.getParameter("templateDir");
String url = request.getParameter("url");
String type = request.getParameter("type");

BuildSite bs = new BuildSite();
sp.build(wwwDir,templateDir,url,type);
}

%>


<%@ include file="footer.jsp"%>

