<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="csrf-token" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>content</title>
<script src="<c:url value="/resources/js/jquery-2.1.4.js" />"></script>
<script src="<c:url value="/resources/js/dropzone.js" />"></script>
<link href="<c:url value="/resources/css/dropzone.css"/>"
	rel="stylesheet">

<script src="<c:url value="/resources/js/jquery-ui.js" />"></script>

<script src="<c:url value="/resources/js/jQueryFileTree.js" />"></script>
<link href="<c:url value="/resources/css/jQueryFileTree.css"/>"
	rel="stylesheet">

<script src="<c:url value="/resources/js/jQueryDirectoryItems.js" />"></script>

<script src="<c:url value="/resources/js/jquery.mCustomScrollbar.js" />"></script>
<link href="<c:url value="/resources/css/jquery.mCustomScrollbar.css"/>"
	rel="stylesheet">

<link href="<c:url value="/resources/css/dialog.css"/>" rel="stylesheet">
<style>
html, body {
	font-family: Verdana, Arial, sans-serif;
	margin: 0;
	padding: 0;
	height: 100%;
}

#container {
	height: 100%;
	width: 100%;
}

#subBut {
	position: relative;
	padding: 0;
	margin: 0;
	color: white;
	background: black;
	text-align: center;
}

.dropzone .dz-preview.dz-success .dz-success-mark {
	opacity: 1;
	-webkit-animation: slide-in 3s cubic-bezier(0.77, 0, 0.175, 1);
	-moz-animation: slide-in 3s cubic-bezier(0.77, 0, 0.175, 1);
	-ms-animation: slide-in 3s cubic-bezier(0.77, 0, 0.175, 1);
	-o-animation: slide-in 3s cubic-bezier(0.77, 0, 0.175, 1);
	animation: slide-in 3s cubic-bezier(0.77, 0, 0.175, 1);
}

#top_navigation_panel {
	position: relative;
	height: 20px;
}

#show_user_message {
	display: none;
	color: red;
	float: left;
}

#log_out {
	float: right;
}

#file_dropzone {
	width: 400px;
	position: relative;
	float: left;
	padding: 0;
	margin: 0;
	height: calc(100% - 20px);
	border-right: 5px solid;
	position: relative;
}

#upload-widget {
	position: relative;
	height: 100%;
	float: left;
}

#dropzone_opener {
	position: relative;
	float: left;
	width: 20px;
	height: calc(100% - 20px);
	background: black;
}

#file_tree {
	margin-left: 5px;
	position: relative;
	float: left;
	position: relative;
}

#directory_tree {
	position: relative;
	float: left;
}

#files_wrapper {
	position: relative;
	padding: 15px 15px;
}

#file_preview {
	margin: 5px;
	display: inline-block;
}

#file_preview object {
	width: 150px;
	height: 150px;
}

#file_name {
	font-size: 16px;
	overflow: auto;
	width: 150px;
	height: 30px;
}

img {
	height: 100%;
	width: 100%;
}

#file_img img {
	height: 150px;
	width: 150px;
}

#showUserRoot {
	position: absolute;
	right: 0;
	top: 100px;
}

#create_new_folder {
	margin: 4px;
	display: inline-block;
	height: 30px;
	width: 30px;
}

#delete_folder {
	margin: 4px;
	display: inline-block;
	height: 30px;
	width: 30px;
}

#rename_folder {
	margin: 4px;
	display: inline-block;
	height: 30px;
	width: 30px;
}

#file_delete_btn {
	float: left;
	display: inline-block;
	height: 30px;
	width: 30px;
}

#file_rename_btn {
	float: right;
	display: inline-block;
	height: 30px;
	width: 30px;
}
</style>
</head>
<body>
	<script>
		var mainRoot = 'src/';
		var userRoot = mainRoot;
		var dropzonejs;
		Dropzone.options.uploadWidget = {
			url : "${pageContext.request.contextPath}/upload_file",
			paramName : 'file',
			autoProcessQueue : false,
			uploadMultiple : true,
			parallelUploads : 2,
			addRemoveLinks : true,
			dictDefaultMessage : 'Drag a file here to upload, or click to select one',
			init : function() {
				dropzonejs = this;
				this
						.on(
								'addedfile',
								function(file) {
									if (file.type.match('audio.*')) {
										this
												.emit("thumbnail", file,
														"${pageContext.request.contextPath}/resources/icons/audio_120x120.png");
									} else if (file.type
											.match('application/pdf')) {

										this
												.emit("thumbnail", file,
														"${pageContext.request.contextPath}/resources/icons/pdf_120х120.jpg");
									} else if (file.type.match('video.*')) {
										this
												.emit("thumbnail", file,
														"${pageContext.request.contextPath}/resources/icons/video_120x120.png");
									}
									//hide progress bar
									$('div.dz-progress').hide();
								});
				this
						.on(
								'success',
								function(file, resp) {

									var dot_index = file.name.lastIndexOf(".");
									var file_ext = file.name.substring(
											dot_index + 1, file.name.length);
									if (file_ext == "png" || file_ext == "jpg") {
										file_ext = "picture"
									}
									var data = "fakePath=" + userRoot
											+ "&fileName=" + file.name;
									$
											.ajax(
													{
														type : "GET",
														url : "${pageContext.request.contextPath}/content/view_file",
														data : data,
														dataType : 'json',
													})
											.done(
													function(result) {
														var directoryTreeItem;
														//file tree
														var fileTreeItem = "<li class=\"file ext_"+ file_ext +"\"><a href=\"#\">"
																+ file.name
																+ "</a></li>";
														$("#fileTree")
																.find(
																		"li.directory.selected.expanded")
																.find(
																		"ul.jqueryFileTree")
																.append(
																		fileTreeItem);
														//directory tree														
														directoryTreeItem = "<div id=\"file_preview\"><div id=\"file_buttons\"><div id=\"file_delete_btn\"class=\"delete_file\"><img src=\"${pageContext.request.contextPath}/resources/icons/delete_file.jpg\"/></div><div id=\"file_rename_btn\"><img src=\"${pageContext.request.contextPath}/resources/icons/rename_file.png\"/></div></div>"
																+ "<div id=\"file_name\" class=\"file_name_class\"><span><a href=\"download/"
										+ result.fileId
										+ "\">"
																+ file.name
																+ "</a></span></div>";
														if (file_ext == "picture") {
															directoryTreeItem += "<div id=\"file_img\"><object data=\"download/"																		+ result.fileId
																		+ "\" type=\""
																		+ result.contentType
																		+ "\"></object></div>";
														} else if (file_ext == "pdf") {
															directoryTreeItem += "<div id=\"file_img\"><img src=\"${pageContext.request.contextPath}/resources/icons/pdf_150x150.jpg\"/></div></div>";
														} else if (file_ext == "txt") {
															directoryTreeItem += "<div id=\"file_img\"><img src=\"${pageContext.request.contextPath}/resources/icons/txt_150x150.jpg\"/></div></div>";
														} else {
															directoryTreeItem += "<div id=\"file_img\"><img src=\"${pageContext.request.contextPath}/resources/icons/default_150x150.png\"/></div></div>";
														}

														//if directory div is not empty
														if ($("#directory")
																.find(
																		"#files_wrapper").length) {
															$("#directory")
																	.find(
																			"#files_wrapper")
																	.append(
																			directoryTreeItem);
														} else {
															var wrappedDirectoryTreeItem = "<div id=\"files_wrapper\">"
																	+ directoryTreeItem
																	+ "</div>";
															$("#directory")
																	.append(
																			wrappedDirectoryTreeItem);
														}
														$('.file_name_class')
																.each(
																		function() {
																			$(
																					this)
																					.mCustomScrollbar(
																							{
																								axis : "x",
																								theme : "dark-thin",
																								autoHideScrollbar : true,
																								autoExpandScrollbar : true,
																								advanced : {
																									autoExpandHorizontalScroll : true
																								}
																							});
																		});
													});

									if (this.options.autoProcessQueue == false)
										this.options.autoProcessQueue = true;
								});
				this.on('queuecomplete', function() {
					if (this.options.autoProcessQueue == true)
						dropzonejs.options.autoProcessQueue = false;
				});
				this.on('thumbnail', function(file) {
				});
			},
		};
		function submitFiles() {
			if (userRoot != "src/") {
				dropzonejs.options.headers = {
					"destRoot" : userRoot,
					'x-csrf-token' : document
							.querySelectorAll('meta[name=csrf-token]')[0]
							.getAttributeNode('content').value,
				}
				$('div.dz-progress').show();
				dropzonejs.processQueue();
			} else {
				var show_user_message = $(document).find(
						"#top_navigation_panel").find("#show_user_message");
				show_user_message.empty();
				show_user_message
						.append("Please, select the directory to upload data");
				show_user_message.show().delay(3000).fadeOut();
			}
		}

		$(document)
				.ready(
						function() {

							updateDirectoryTree();

							function updateDirectoryTree() {
								$('.directoryTree')
										.directoryItems(
												{
													root : userRoot,
													script : "${pageContext.request.contextPath}/content/directory_tree",
												}, function(file) {
												});
							}

							$('.tree')
									.fileTree(
											{
												root : userRoot,
												script : "${pageContext.request.contextPath}/file_tree",
											}, function(file) {
											}).on('expandedSelected',
											function(e, data) {
												userRoot = data.rel;
												$('#directory').empty();
												updateDirectoryTree();
											}).on('collapsedSelected',
											function(e, data) {
												userRoot = data.rel;
												$('#directory').empty();
												updateDirectoryTree();
											});

							var thisFileTreeCreateNewFolder;
							$('#create_new_folder')
									.click(
											function() {
												thisFileTreeCreateNewFolder = $(
														this).parent();
												var id = $('#create_new_folder_window');
												// Get the screen height and width
												var maskHeight = $(document)
														.height();
												var maskWidth = $(window)
														.width();

												// Set height and width to mask to fill up the whole screen
												$('#mask').css({
													'width' : maskWidth,
													'height' : maskHeight
												});

												// transition effect
												$('#mask').fadeIn(1000);
												$('#mask').fadeTo("slow", 0.8);

												// Get the window height and width
												var winH = $(window).height();
												var winW = $(window).width();

												// Set the popup window to center
												$(id)
														.css(
																'top',
																winH
																		/ 2
																		- $(id)
																				.height()
																		/ 2);
												$(id).css(
														'left',
														winW / 2
																- $(id).width()
																/ 2);

												// transition effect
												$(id).fadeIn(2000);
											});

							$('#folder_name_submit')
									.click(
											function() {
												var name = $('#folder_name')
														.val();
												var data = "fakePath="
														+ userRoot
														+ "&fileName=" + name;
												var folderToAppend = "<li class=\"directory collapsed\"><a href=\"#\" rel=\"" + userRoot + name + "/\">"
														+ name + "</a></li>";
												var fileTreeData = $('#fileTree');

												$
														.ajax(
																{
																	type : "Get",
																	url : "${pageContext.request.contextPath}/content/create_new_folder",
																	data : data,
																})
														.done(
																function() {
																	$(
																			'#mask, .window')
																			.hide();
																	if (userRoot != "src/") {
																		thisFileTreeCreateNewFolder
																				.find(
																						"li.directory.selected.expanded")
																				.find(
																						"ul.jqueryFileTree")
																				.append(
																						folderToAppend);
																	} else {
																		if (thisFileTreeCreateNewFolder
																				.find(
																						"#fileTree")
																				.find(
																						"ul.jqueryFileTree").length) {
																			thisFileTreeCreateNewFolder
																					.find(
																							"#fileTree")
																					.find(
																							"ul.jqueryFileTree")
																					.first()
																					.append(
																							folderToAppend);
																		} else {
																			var wrappedFolderToAppend = "<ul class=\"jqueryFileTree\" style=\"display: block;\">"
																					+ folderToAppend
																					+ "</ul>";
																			thisFileTreeCreateNewFolder
																					.find(
																							"#fileTree")
																					.append(
																							wrappedFolderToAppend);
																		}
																	}
																});
											});

							$('.window .close').click(function(e) {
								e.preventDefault();
								$('#mask, .window').hide();
							});
							$('#mask').click(function() {
								$(this).hide();
								$('.window').hide();
							});

							$('#delete_folder')
									.click(
											function() {
												if (userRoot != "src/") {
													var deleteAction = 'deleteFolder';

													var data = +"deleteAction="
															+ deleteAction
															+ "&fakePath="
															+ userRoot;
													var url_for_view = "${pageContext.request.contextPath}/content/delete_file";
													$
															.ajax(
																	{
																		type : "Get",
																		url : url_for_view,
																		data : data,
																	})
															.done(
																	function() {
																		$(
																				'#fileTree')
																				.find(
																						"li.selected")
																				.remove();
																		userRoot = mainRoot;
																		$(
																				'#directory')
																				.empty();
																	});
												} else {
													var show_user_message = $(
															document)
															.find(
																	"#top_navigation_panel")
															.find(
																	"#show_user_message");
													show_user_message.empty();
													show_user_message
															.append("Please, select the directory to delete");
													show_user_message.show()
															.delay(3000)
															.fadeOut();
												}
											});

							$(document)
									.on(
											'click',
											'#file_delete_btn',
											function() {
												var file = $(this).parent()
														.parent();
												var fileName = file.find(
														"#file_name").find("a")
														.text();
												var url_for_view = "${pageContext.request.contextPath}/content/delete_file";
												var action = 'deleteFile';
												var deleteAction = 'deleteFile';
												var data = "action=" + action
														+ "&deleteAction="
														+ deleteAction
														+ "&fakePath="
														+ userRoot
														+ "&fileName="
														+ fileName;
												$
														.ajax({
															type : "Get",
															url : url_for_view,
															data : data,
														})
														.done(
																function() {
																	// delete for directory tree
																	file
																			.remove();
																	// delete for file tree																	
																	$(
																			"#fileTree")
																			.find(
																					"li.selected.directory a")
																			.each(
																					function() {
																						if (fileName == $(
																								this)
																								.text()) {
																							$(
																									this)
																									.remove();
																							return false;
																						}
																					});
																});
											});

							var folderName;
							$('#rename_folder')
									.click(
											function() {
												folderName = $("#fileTree")
														.find(
																"li.selected.directory>a")
														.text();
												if (folderName
														|| userRoot != "src/") {
													var id = $('#rename_folder_window');
													id.find("#folder_name")
															.val(folderName);
													// Get the screen height and width
													var maskHeight = $(document)
															.height();
													var maskWidth = $(window)
															.width();
													// Set height and width to mask to fill up the whole screen
													$('#mask').css({
														'width' : maskWidth,
														'height' : maskHeight
													});
													// transition effect
													$('#mask').fadeIn(1000);
													$('#mask').fadeTo("slow",
															0.8);
													// Get the window height and width
													var winH = $(window)
															.height();
													var winW = $(window)
															.width();
													// Set the popup window to center
													$(id)
															.css(
																	'top',
																	winH
																			/ 2
																			- $(
																					id)
																					.height()
																			/ 2);
													$(id)
															.css(
																	'left',
																	winW
																			/ 2
																			- $(
																					id)
																					.width()
																			/ 2);

													// transition effect
													$(id).fadeIn(2000);
												} else {
													console.log("else");
													//TODO
													var show_user_message = $(
															document)
															.find(
																	"#top_navigation_panel")
															.find(
																	"#show_user_message");
													show_user_message.empty();
													show_user_message
															.append("Please, select the directory to rename");
													show_user_message.show()
															.delay(3000)
															.fadeOut();
												}
											});

							$('#rename_folder_submit')
									.click(
											function() {
												var name = $(
														'#rename_folder_window')
														.find('#folder_name')
														.val();

												var renameAction = 'renameFolder';
												var data = "&renameAction="
														+ renameAction
														+ "&fakePath="
														+ userRoot
														+ "&fileName=" + name
														+ "&fileOldName="
														+ folderName;
												$
														.ajax(
																{
																	type : "Get",
																	url : "${pageContext.request.contextPath}/content/rename_file",
																	data : data,
																})
														.done(
																function() {
																	var str = $(
																			"#fileTree")
																			.find(
																					"li.directory.selected.expanded>a")
																			.attr(
																					"rel");
																	var splitStr = str
																			.split('/');
																	var offset = splitStr.length - 2;
																	splitStr[offset] = name;
																	var resultStr = splitStr
																			.join('/');

																	var selectedFolderA = $(
																			"#fileTree")
																			.find(
																					"li.directory.selected.expanded>a");

																	selectedFolderA
																			.attr(
																					"rel",
																					resultStr)
																			.text(
																					name);

																	$(
																			"#fileTree")
																			.find(
																					"li.directory.selected.expanded")
																			.find(
																					"ul.jqueryFileTree")
																			.find(
																					"li.directory a")
																			.each(
																					function(
																							index,
																							element) {
																						var strInnerDir = $(element);
																						var splitStrInnerDir = strInnerDir
																								.attr(
																										"rel")
																								.split(
																										'/');
																						splitStrInnerDir[offset] = name;
																						var resultInnerStr = splitStrInnerDir
																								.join('/');
																						strInnerDir
																								.attr(
																										"rel",
																										resultInnerStr);
																						console
																								.log($(
																										element)
																										.attr(
																												"rel"));
																					});

																	userRoot = selectedFolderA
																			.attr("rel");

																	$(
																			'#mask, .window')
																			.hide();
																});
											});

							var fileOldName;
							var file_ext;
							var file;
							$(document).on(
									'click',
									'#file_rename_btn',
									function() {
										file = $(this).parent().parent();
										fileOldName = file.find("#file_name")
												.find("a").text();
										var dot_index = fileOldName
												.lastIndexOf(".");
										var file_name = fileOldName.substring(
												0, dot_index);
										file_ext = fileOldName.substring(
												dot_index, fileOldName.length);
										console.log(file_name);

										var id = $('#rename_file_window');
										id.find("#file_name").val(file_name);
										var maskHeight = $(document).height();
										var maskWidth = $(window).width();
										$('#mask').css({
											'width' : maskWidth,
											'height' : maskHeight
										});
										$('#mask').fadeIn(1000);
										$('#mask').fadeTo("slow", 0.8);
										var winH = $(window).height();
										var winW = $(window).width();
										$(id).css('top',
												winH / 2 - $(id).height() / 2);
										$(id).css('left',
												winW / 2 - $(id).width() / 2);
										$(id).fadeIn(2000);
									});

							$(document)
									.on(
											'click',
											'#rename_file_submit',
											function() {
												var name = $(
														'#rename_file_window')
														.find('#file_name')
														.val();
												name += file_ext;
												var renameAction = 'renameFile';
												var data = "renameAction="
														+ renameAction
														+ "&fakePath="
														+ userRoot
														+ "&fileName=" + name
														+ "&fileOldName="
														+ fileOldName;
												$
														.ajax(
																{
																	type : "Get",
																	url : "${pageContext.request.contextPath}/content/rename_file",
																	data : data,
																})
														.done(
																function() {
																	file
																			.find(
																					"#file_name")
																			.find(
																					"a")
																			.text(
																					name);
																	//rename for file tree
																	$(
																			"#fileTree")
																			.find(
																					"li.selected.directory a")
																			.each(
																					function() {
																						if (fileOldName == $(
																								this)
																								.text()) {
																							$(
																									this)
																									.text(
																											name);
																							return false;
																						}
																					});

																	$(
																			'#mask, .window')
																			.hide();
																});
											});

							$(document)
									.on(
											'click',
											'#file_img',
											function() {
												var viewedFileName = $(this)
														.parent().find(
																"#file_name")
														.find("a").text();
												var dot_index = viewedFileName
														.lastIndexOf(".");
												var file_ext = viewedFileName
														.substring(
																dot_index + 1,
																viewedFileName.length);
												if (file_ext == "png"
														|| file_ext == "jpg") {
													file_ext = "picture"
												}
												if (file_ext == "picture"
														|| file_ext == "txt"
														|| file_ext == "pdf") {
													var id = $('#file_viewer_window');

													var maskHeight = $(document)
															.height();
													var maskWidth = $(window)
															.width();

													var winH = $(window)
															.height();
													var winW = $(window)
															.width();

													var modalH = winH - winH
															* 0.25;
													var modalW = winW - winW
															* 0.3;

													var data = "fakePath="
															+ userRoot
															+ "&fileName="
															+ viewedFileName;
													$
															.ajax(
																	{
																		type : "GET",
																		url : "${pageContext.request.contextPath}/view_file",
																		data : data,
																		dataType : 'json',

																		success : function(
																				data,
																				textStatus) {
																		},
																		error : function(
																				jqXHR,
																				textStatus,
																				errorThrown) {
																			console
																					.log("Something really bad happened "
																							+ textStatus);
																		}
																	})
															.done(
																	function(
																			result) {
																		if (file_ext == "picture"
																				|| file_ext == "pdf"
																				|| file_ext == "txt") {
																			$(
																					'#file_viewer_mask')
																					.css(
																							{
																								'width' : maskWidth,
																								'height' : maskHeight
																							});

																			$(
																					'#file_viewer_mask')
																					.show();

																			$(
																					id)
																					.show();
																		}
																		if (file_ext == "picture") {

																			var displayImage = "<object data=\"download/"
																		+ result.fileId
																		+ "\" type=\""
																		+ result.contentType
																		+ "\"></object>";

																			var imageHeight = maskHeight
																					- id
																							.find(
																									"#close_viewer")
																							.height();

																			id
																					.find(
																							"#img_src")
																					.append(
																							displayImage);

																			id
																					.find(
																							"#img_src")
																					.find(
																							'object')
																					.css(
																							{
																								"height" : (maskHeight - 20) * 0.97,
																							});

																			id
																					.find(
																							"#img_src")
																					.find(
																							'object')
																					.load(
																							function() {
																								var viewerWidth = id
																										.find(
																												"#img_src")
																										.find(
																												'object')
																										.width();

																								id
																										.css({
																											"left" : ((maskWidth - viewerWidth) / 2)
																										});
																							});
																		} else if (file_ext == "pdf") {
																			id
																					.css({
																						"bottom" : 0,
																					});
																			id
																					.find(
																							"#img_src")
																					.css(
																							{
																								"height" : "100%",
																								"width" : winW * 0.3
																							});
																			id
																					.find(
																							"#img_src")
																					.height(
																							id
																									.find(
																											"#img_src")
																									.height()
																									- id
																											.find(
																													'#close_viewer')
																											.height());

																			var pdfObj = "<object data=\"download/"
																				+ result.fileId
																				+ "\" type=\""
																				+ result.contentType
																				+ "\"> </object>"

																			id
																					.find(
																							"#img_src")
																					.append(
																							pdfObj);

																			id
																					.find(
																							"#img_src")
																					.find(
																							'object')
																					.width(
																							"100%")
																					.height(
																							maskHeight - 20);

																			viewerWidth = $(
																					"#file_viewer_window")
																					.width();

																			id
																					.css({
																						"left" : ((maskWidth - viewerWidth) / 2),
																						"bottom" : "0"
																					});
																		} else if (file_ext == "txt") {
																			var text = "<object data=\"download/"
																				+ result.fileId
																				+ "\" type=\""
																				+ result.contentType
																				+ "\"></object>";

																			id
																					.find(
																							"#img_src")
																					.append(
																							text);
																			id
																					.find(
																							"#img_src")
																					.find(
																							'object')
																					.width(
																							"100%")
																					.height(
																							maskHeight - 20);

																			id
																					.css({
																						"bottom" : 0,
																						"width" : winW * 0.3
																					});

																			viewerWidth = $(
																					"#file_viewer_window")
																					.width();

																			id
																					.css({
																						"left" : ((winW - viewerWidth) / 2),
																					});
																		}
																	});

													$(
															'.file_viewer .close_file_viewer')
															.click(
																	function(e) {
																		e
																				.preventDefault();

																		id
																				.find(
																						'#file_viewer_window')
																				.removeAttr(
																						"style");
																		id
																				.find(
																						'#img_src')
																				.find(
																						'object')
																				.remove();

																		$(
																				'#file_viewer_mask, .file_viewer')
																				.hide();
																	});

													$('#file_viewer_mask')
															.click(
																	function() {
																		id
																				.css({
																					"width" : "auto",
																					"height" : "auto"
																				});
																		id
																				.find(
																						'#img_src')
																				.find(
																						'object')
																				.remove();
																		$(this)
																				.hide();
																		$(
																				'.file_viewer')
																				.hide();
																	});
												}

											});
							$("#dropzone_opener").click(
									function() {
										var effect = 'slide';
										var options = {
											direction : "left"
										};
										var duration = 500;
										$('#file_dropzone').toggle(effect,
												options, duration);
									});
						});
	</script>
	<div id="container">
		<div id="top_navigation_panel">
			<div id="log_out">
				<c:if test="${pageContext.request.userPrincipal.name != null}">					
						User : ${pageContext.request.userPrincipal.name} | <a
						href="javascript:formSubmit()"> Logout</a>
				</c:if>
			</div>
			<div id="show_user_message"></div>
			<c:url value="/j_spring_security_logout" var="logoutUrl" />
			<form action="${logoutUrl}" method="post" id="logoutForm">
				<input type="hidden" name="${_csrf.parameterName}"
					value="${_csrf.token}" />
			</form>
			<script>
				function formSubmit() {
					document.getElementById("logoutForm").submit();
				}
			</script>
		</div>

		<div id="dropzone_opener"></div>
		<div id="file_dropzone">
			<form id="upload-widget" method="post"
				action="${pageContext.request.contextPath}/main/upload_file"
				class="dropzone">
				<div class="fallback">
					<input name="file" type="file" />
				</div>
				<div id="subBut" onclick="submitFiles()">submit</div>
			</form>
		</div>

		<div id="file_tree">
			<div id="create_new_folder">
				<img
					src="${pageContext.request.contextPath}/resources/icons/add_folder.png">
			</div>
			<div id="delete_folder">
				<img
					src="${pageContext.request.contextPath}/resources/icons/delete_folder.jpg">
			</div>
			<div id="rename_folder">
				<img
					src="${pageContext.request.contextPath}/resources/icons/rename_folder.png">
			</div>
			<div id="fileTree" class="tree"></div>
		</div>

		<div id="directory_tree">
			<div id="directory" class="directoryTree"></div>
		</div>

		<div id="boxes">
			<div id="mask"></div>
			<div class="window" id="create_new_folder_window">

				<table class="modalWindow">
					<tr>
						<td><input type="text" id="folder_name" value="" /></td>
					</tr>
					<tr>
						<td><input type="submit" id="folder_name_submit"
							value="Submit"></td>
					</tr>
					<tr>
						<td><a href="" id="window_close" class="close">close</a></td>
					</tr>
				</table>
			</div>
		</div>

		<div id="boxes">
			<div id="mask"></div>
			<div class="window" id="rename_folder_window">
				<table class="modalWindow">
					<tr>
						<td><input type="text" id="folder_name" value="" /></td>
					</tr>
					<tr>
						<td><input type="submit" id="rename_folder_submit"
							value="Submit"></td>
					</tr>
					<tr>
						<td><a href="" id="window_close" class="close">close</a></td>
					</tr>
				</table>
			</div>
		</div>

		<div id="boxes">
			<div id="mask"></div>
			<div class="window" id="rename_file_window">
				<table class="modalWindow">
					<tr>
						<td><input type="text" id="file_name" value="" /></td>
					</tr>
					<tr>
						<td><input type="submit" id="rename_file_submit"
							value="Submit"></td>
					</tr>
					<tr>
						<td><a href="" id="window_close" class="close">close</a></td>
					</tr>
				</table>
			</div>
		</div>

		<div id="file_viewer_boxes">
			<div id="file_viewer_mask"></div>
			<div class="file_viewer" id="file_viewer_window">
				<div id="close_viewer">
					<a href="" id="window_close" class="close_file_viewer">close</a>
				</div>
				<div id="img_src"></div>
			</div>
		</div>

	</div>
</body>
</html>