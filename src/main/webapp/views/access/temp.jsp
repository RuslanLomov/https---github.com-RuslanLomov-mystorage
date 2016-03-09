
							
							Dropzone.autoDiscover = false;
							// 							initDropzone();
							// 							function initDropzone() {
							var token = $('#csrfToken').val();
							var header = $('#csrfHeader').val();
							myDropzone = new Dropzone(
									"div#dropzoneWrapper",
									{
										//TODO
										url : "${pageContext.request.contextPath}/main/upload_file",
										maxFilesize : 1200,
										dictDefaultMessage : 'Drag a file here to upload, or click to select one',
										autoProcessQueue : false,
										uploadMultiple : true,
										method : "POST",
										// 											headers : {
										// 												'x-csrf-token' : $(
										// 														'meta[name="csrf-token"]')
										// 														.attr('content')
										// 											},
										sending : function(file, xhr, formData) {

											console.log(file);
											console.log(formData);
											// 												var token = $(
											// 														"meta[name='_csrf']")
											// 														.attr("content");
											// 												var header = $(
											// 														"meta[name='_csrf_header']")
											// 														.attr("content");
											console.log(header + " " + token);
											xhr.setRequestHeader(header, token);
											console.log(xhr);

										},
										init : function() {
											// 												$("#subBut").width(width);
											//init dropzone value
											myDropzone = this;

											this
													.on(
															'addedfile',
															function(file) {
																// 																		console
																// 																				.log("this.on(addedfile) function");
																if (file.type
																		.match('audio.*')) {
																	this
																			.emit(
																					"thumbnail",
																					file,
																					"./resources/icons/audio_120x120.png");
																} else if (file.type
																		.match('application/pdf')) {

																	this
																			.emit(
																					"thumbnail",
																					file,
																					"./resources/icons/pdf_120х120.jpg");

																} else if (file.type
																		.match('video.*')) {
																	this
																			.emit(
																					"thumbnail",
																					file,
																					"./resources/icons/video_120x120.png");
																} else {
																	this
																			.emit(
																					"thumbnail",
																					file,
																					"./resources/icons/default_150x150.png");
																}
																//hide progress bar
																$(
																		'div.dz-progress')
																		.hide();
															});

											this.on('queuecomplete',
													function() {

														// 																	$(
														// 																			'#directory')
														// 																			.empty();
														// 																	updateDirectoryTree();
														// 																	if (this.options.autoProcessQueue == true)
														// 																this.options.autoProcessQueue = false;
													});

											this
													.on(
															'success',
															function(file, resp) {
																console
																		.log("this.on(success) function");

																// 																																		console
																// 																																				.log(file.name);
																// 																	if (this.options.autoProcessQueue == false)
																// 																this.options.autoProcessQueue = true;
															});

											this.on('thumbnail',
													function(file) {

													});
											this.on('complete', function(file) {
												// 																		console
												// 																				.log("this.on(complete) function");
											});
										},
										accept : function(file, done) {
											console.log("inside accept");

											// 												console.log("before sent");

											done();
										},
										addRemoveLinks : true
									});
							$('#subBut').click(function() {

								myDropzone.options.headers = {
									"destRoot" : userRoot
								}
								// 									console.log(myDropzone.options.headers);
								myDropzone.processQueue();
								//show progress bar
								$('div.dz-progress').show();
							}).css({
								"width" : $("#drop_zone_draggable").width()
							});
							// 							}