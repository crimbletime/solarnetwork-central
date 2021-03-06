<%@ page contentType="application/xhtml+xml; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://packtag.sf.net" prefix="pack" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="nodeId">
	<c:choose>
		<c:when test='${param["nodeId"] != null}'>${param["nodeId"]}</c:when>
		<c:otherwise>11</c:otherwise>
	</c:choose>
</c:set>
<head>
	<title><fmt:message key="node.displayName"/> ${nodeId} <fmt:message key="dashboard.generation.displayName"/></title>
	<meta name="viewport" content="width=1120" />
	<pack:style>
		<src>/css/global.css</src>
		<src>/css/node-dashboard.css</src>
		<src>/css/smoothness/jquery-ui-1.7.2.custom.css</src>
		<src>/css/ui.daterangepicker.css</src>
		<src>/css/jquery.jqplot.css</src>
	</pack:style>
	<pack:script> 
		<src>/js/jquery-1.3.2.js</src> 
		<src>/js/jquery-ui-1.7.2.custom.min.js</src>
		<%--src>/js/daterangepicker.jQuery.js</src--%>
		<src>/js/jquery.jqplot.js</src>
		<src>/js/jqplot-plugins/jqplot.canvasTextRenderer.js</src>
		<src>/js/jqplot-plugins/jqplot.canvasAxisLabelRenderer.js</src>
		<src>/js/jqplot-plugins/jqplot.categoryAxisRenderer.js</src>
		<src>/js/jqplot-plugins/jqplot.dateAxisRenderer.js</src>
		<src>/js/jqplot-plugins/jqplot.barRenderer.js</src>
		<src>/js/jqplot-plugins/jqplot.ohlcRenderer.js</src>
		<%--src>/js/jqplot-plugins/jqplot.trendline.js</src--%>
		<src>/js/jqplot-plugins/jqplot.cursor.js</src>
		<src>/js/jqplot-plugins/jqplot.highlighter.js</src>
		<src>/js/jqplot-plugins/jqplot.pointLabels.js</src>
		<%--src>/js/date.js</src --%>
		<src>/js/node-dashboard.js</src>
	</pack:script>
</head>
<body>
<div id="current-weather-div" class="chart-box" style="display: none;">
	<h2><fmt:message key="weather.currentConditions"/></h2>
	<div class="current-temp">11&#xb0;</div>
	<div class="highlow-temp high"><span class="label"><fmt:message key="weather.high.label"/></span><span class="value">15&#xb0;</span></div>
	<div class="highlow-temp low"><span class="label"><fmt:message key="weather.low.label"/></span><span class="value">9&#xb0;</span></div>
	<div class="weather-icon"/>
	<div class="weather-extra humidity"><span class="label"><fmt:message key="weather.humidity.label"/>:</span><span class="value">89%</span></div>
	<div class="weather-extra sunrise"><span class="label"><fmt:message key="weather.sunrise.label"/>:</span><span class="value">7:09</span></div>
	<div class="weather-extra sunset"><span class="label"><fmt:message key="weather.sunset.label"/>:</span><span class="value">17:41</span></div>
	<div class="updated"><fmt:message key="updated.displayName"/> <span class="value">2009-08-01 08:05</span></div>
</div>
<div id="chart-main" class="chart-box">
	<div id="chart-div" class="chart-container" style="width:840px;height:400px;"></div>
	<div id="chart-overview-div" class="chart-container" style="width:840px;height:80px;"></div>
	<div id="date-picker-div">
		<div id="date-slider"/>
		<input type="text" id="date-range-display" />
	</div>
</div>
<div id="chart-box2" class="chart-box">
	<div class="chart-switcher">~</div>
	<div id="week-generation-div" class="switchable">
		<h2><fmt:message key="dashboard.dailyGeneration.title"/></h2>
		<div id="week-generation-chart-div" class="chart-container" style="width:425px;height=300px"></div>
	</div>
	<div id="monthly-generation-div" class="switchable" style="display: none;">
		<h2><fmt:message key="dashboard.monthlyGeneration.title"/></h2>
		<div id="monthly-generation-chart-div" class="chart-container" style="width:425px;height=300px"></div>
	</div>
</div>
<div id="chart-box3" class="chart-box">
	<div class="chart-switcher">~</div>
	<div id="week-weather-div" class="switchable">
		<h2><fmt:message key="dashboard.dailyWeather.title"/></h2>
		<div id="week-weather-chart-div" class="chart-container" style="width:375px;height=300px"></div>
	</div>
	<div id="monthly-weather-div" class="switchable" style="display: none;">
		<h2><fmt:message key="dashboard.monthlyWeather.title"/></h2>
		<div id="monthly-weather-chart-div" class="chart-container" style="width:375px;height=300px"></div>
	</div>
</div>
<div style="display: none">
	<input type="hidden" id="nodeId" name="nodeId" value="${nodeId}" />
	<input type="hidden" id="feature-consumption" name="feature.consumption" value="false" />
	<input type="hidden" id="feature-gridPrice" name="feature.gridPrice" value="false" />
</div>
</body>
