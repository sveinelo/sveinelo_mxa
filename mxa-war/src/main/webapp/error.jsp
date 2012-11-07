<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%--
  #%L
  Web Archive
  %%
  Copyright (C) 2009 - 2012 Patentstyret
  %%
  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as
  published by the Free Software Foundation, either version 3 of the 
  License, or (at your option) any later version.
  
  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  
  You should have received a copy of the GNU General Public 
  License along with this program.  If not, see
  <http://www.gnu.org/licenses/gpl-3.0.html>.
  #L%
  --%>
<%@ page
	import="java.util.List,java.io.PrintWriter,org.apache.myfaces.shared_tomahawk.util.ExceptionUtils"
	isErrorPage="true"%>
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core" xml:lang="en" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>MXA - Administrasjon - Vis melding</title>
<link href="styles/altut.css" rel="stylesheet" type="text/css"
	media="screen" />
</head>
<body>
	<div id="pageWrap">
		<div id="headerWrap">
			<div id="logo">
				<img src="img/Logo.png" alt="Logo" />
			</div>
			<div id="header">
				<p class="appNameShort">Administrasjonssider</p>
				<p class="appNameDesc">Message Exchange Altinn (MXA)</p>
			</div>
		</div>
		<div id="contentWrap">
			<div id="nav"></div>
			<div id="content">
				<div class="mssg error">
					<h1>Det har oppstått en feil</h1>
					<p>
						<%
							if (exception != null) {
								String exceptionMessage = ExceptionUtils.getExceptionMessage(ExceptionUtils.getExceptions(exception));
						%>Message: <span class="errorMessage"><%=exceptionMessage%></span>
						<%
							} else {
						%>Unknown error<%
							}
						%>
					</p>
				</div>
			</div>
		</div>
		<div id="footerWrap"></div>
	</div>
</body>
</html>
