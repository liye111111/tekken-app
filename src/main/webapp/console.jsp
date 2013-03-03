<%@ include file="header.jsp"%>
<%@ page import="me.liye.tekken.wiki.SavePage" %>
<%@ page import="java.util.*" %>

<form method="post">
<caption>build wiki page</caption>
<input type="hidden" name="action" value="buildWikiPage">
<table border="1">
<tr>
<td>URL</td>
<td><input name="url" size="100">
<span class="memo" style="font:50%">
<br>TT2:http://wiki.livedoor.jp/inatekken/d/%c1%ed%b9%e7TUD
<br>BR:http://wiki.livedoor.jp/inatekken/d/%c1%ed%b9%e7TBR
</span>
</td>
</tr>
<tr>
<td>savn path</td>
<td><input name="path" size="50"></td>
</tr>
<tr>
<td>template</td>
<td><input name="template" size="100"></td>
</tr>

</table>
<input type="submit" value="execute">
</form>


<%
String action = request.getParameter("action");
if(null!=action){

String url = request.getParameter("url");
String path = request.getParameter("path");
String template = request.getParameter("template");


SavePage sp = new SavePage(url,path,template);
sp.execute();
Set<String> unknown = sp.getUnknown();
%>
unknown: 
<%=unknown%>

<%
}
%>

<%@ include file="footer.jsp"%>

