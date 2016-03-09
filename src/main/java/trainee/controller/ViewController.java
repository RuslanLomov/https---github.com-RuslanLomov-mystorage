package trainee.controller;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import trainee.facade.interf.FileDataFacade;
import trainee.facade.interf.UserFacade;
import trainee.model.FileData;
import trainee.model.User;
import trainee.tools.AllFilesForDirectory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Controller
public class ViewController {
	private static final Logger logger = LoggerFactory.getLogger("MyLogger_ALL");

	@Autowired
	private FileDataFacade fileDataFacade;
	@Autowired
	private UserFacade userFacade;

	@RequestMapping(value = "/create_new_folder")
	public ModelAndView createNewFolder(HttpServletRequest request,
			HttpServletResponse response) {
		UserDetails userDetail = ViewController.getUserDetails();
		User user = userFacade.getUserByLogin(userDetail.getUsername());
		String fakePath = request.getParameter("fakePath");
		String fileName = request.getParameter("fileName");
		File newFolder = new File(fileDataFacade.getFileRealPath(fakePath, user.getId())
				+ "/" + fileName);
		FileData currentFileData = new FileData(newFolder.getName(), newFolder.getPath(),
				fakePath + newFolder.getName() + "/", user.getId());
		if (!newFolder.exists()) {
			newFolder.mkdir();
			if (!fileDataFacade.isDatabaseFileExist(currentFileData)) {
				fileDataFacade.insertIntoFiles(currentFileData);
			}
		}
		return new ModelAndView("content/main", "", null);
	}

	@RequestMapping(value = "/delete_file")
	public ModelAndView deleteFile(HttpServletRequest request,
			HttpServletResponse response) {
		UserDetails userDetail = ViewController.getUserDetails();
		User user = userFacade.getUserByLogin(userDetail.getUsername());
		String fakePath = request.getParameter("fakePath");
		String fileName = request.getParameter("fileName");
		String deleteFile = request.getParameter("deleteAction");
		if (deleteFile != null && deleteFile.equals("deleteFile")) {
			fakePath += fileName + "/";
		}
		File fileToDelete = new File(fileDataFacade.getFileRealPath(fakePath,
				user.getId()));
		if (fileToDelete.isDirectory()) {
			AllFilesForDirectory.getFilesAndDirectories(fileToDelete);
			for (File f : AllFilesForDirectory.getList()) {
				fileDataFacade.deleteFile(f.getPath(), user.getId());
			}
			try {
				FileUtils.deleteDirectory(fileToDelete);
			} catch (IOException e) {
				logger.error("ViewController.deleteFile() threw ", e);
			}
			fileDataFacade.deleteFile(fileToDelete.getPath(), user.getId());
		} else {
			fileToDelete.delete();
			fileDataFacade.deleteFile(fileToDelete.getPath(), user.getId());
		}
		return new ModelAndView("content/main", "", null);
	}

	@RequestMapping(value = "/rename_file")
	public ModelAndView renameFile(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		UserDetails userDetail = ViewController.getUserDetails();
		User user = userFacade.getUserByLogin(userDetail.getUsername());
		String fakePath = request.getParameter("fakePath");
		String fileName = request.getParameter("fileName");
		String renameAction = request.getParameter("renameAction");
		String fileOldName = request.getParameter("fileOldName");
		if (renameAction.equals("renameFile")) {
			fakePath += fileOldName + "/";
		}
		File fileToRename = new File(fileDataFacade.getFileRealPath(fakePath,
				user.getId()));
		List<File> cascadeFiles = null;
		if (renameAction.equals("renameFolder")) {
			// save all files and folders inside the directory that will be
			// renamed soon
			AllFilesForDirectory.resetList();
			AllFilesForDirectory.getFilesAndDirectories(fileToRename);
			cascadeFiles = AllFilesForDirectory.getList();
		}
		// File or directory with new name
		File newFile = new File(fileToRename.getParent() + "/" + fileName);
		// Rename file or directory
		boolean success = fileToRename.renameTo(newFile);
		if (success) {
			// receive id with old name
			FileData file = new FileData(fileOldName, fileToRename.getPath(), fakePath,
					user.getId());
			file = fileDataFacade.getFileId(file);
			String[] splittedFakePath = file.getFakePath().split("/");
			Integer position = splittedFakePath.length - 1;
			splittedFakePath[position] = fileName;
			StringBuffer newFakePath = new StringBuffer();
			for (String str : splittedFakePath) {
				newFakePath.append(str).append("/");
			}
			// set new information
			file.setName(fileName);
			file.setPath(newFile.getPath());
			file.setFakePath(newFakePath.toString());
			fileDataFacade.renameFile(file);
			// need to rename all paths for files that are present in
			// this folder
			if (cascadeFiles != null && !cascadeFiles.isEmpty()) {
				String newFilePath;
				String newFileFakePath;
				for (File childNode : cascadeFiles) {
					FileData fileData = new FileData();
					fileData.setPath(childNode.getPath());
					fileData.setUserId(user.getId());
					fileData = fileDataFacade.getFileByPath(fileData);
					newFilePath = fileData.getPath();
					String[] splittedReferingFileRealPath = Pattern.compile("\\\\")
							.split(newFilePath);
					// First position in src, second user login
					splittedReferingFileRealPath[position + 2] = fileName;
					StringBuffer collectedSplittedReferingFilePath = new StringBuffer();
					for (int i = 0; i < splittedReferingFileRealPath.length; i++) {
						if (i == splittedReferingFileRealPath.length - 1) {
							collectedSplittedReferingFilePath
									.append(splittedReferingFileRealPath[i]);
							break;
						}
						collectedSplittedReferingFilePath.append(
								splittedReferingFileRealPath[i]).append(File.separator);
					}
					fileData.setPath(collectedSplittedReferingFilePath.toString());
					newFileFakePath = fileData.getFakePath();
					String[] splittedReferingFileFakePath = newFileFakePath.split("/");
					splittedReferingFileFakePath[position] = fileName;
					StringBuffer collectedSplittedReferingFileFakePath = new StringBuffer();
					for (String str : splittedReferingFileFakePath) {
						collectedSplittedReferingFileFakePath.append(str).append("/");
					}
					fileData.setFakePath(collectedSplittedReferingFileFakePath.toString());
					fileDataFacade.renameReferencedFile(fileData);
				}
			}
		} else {
			// File was not successfully renamed
			logger.error("ViewController.renameFile() threw ", "renameFailException");
		}

		return new ModelAndView("content/main", "", null);
	}

	@RequestMapping(value = "/file_tree", method = RequestMethod.GET)
	public void fileTreeViewHandler(HttpServletRequest request,
			HttpServletResponse response) {
		UserDetails userDetails = ViewController.getUserDetails();
		User user = userFacade.getUserByLogin(userDetails.getUsername());
		String fakePath = request.getParameter("fakePath");
		String realPath = fileDataFacade.getFileRealPath(fakePath, user.getId());
		response.setContentType("text/html;charset=UTF-8");
		if (new File(realPath).exists()) {
			String[] files = new File(realPath).list(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.charAt(0) != '.';
				}
			});
			Arrays.sort(files, String.CASE_INSENSITIVE_ORDER);
			PrintWriter out = null;
			try {
				out = response.getWriter();
			} catch (IOException e) {
				logger.error("ViewController.fileTreeViewHandler() threw ", e);
			}
			out.print("<ul class=\"jqueryFileTree\" style=\"display: none;\">");
			for (String file : files) {
				// All dirs
				if (new File(realPath, file).isDirectory()) {
					out.print("<li class=\"directory collapsed\"><a href=\"#\" rel=\""
							+ fakePath + file + "/\">" + file + "</a></li>");
				}
				// All files
				if (!new File(realPath, file).isDirectory()) {
					int dotIndex = file.lastIndexOf('.');
					String ext = dotIndex > 0 ? file.substring(dotIndex + 1) : "";
					out.print("<li class=\"file ext_" + ext + "\"><a href=\"#\">" + file
							+ "</a></li>");
				}
			}
			out.print("</ul>");
		}
	}

	@RequestMapping(value = "/directory_tree", method = RequestMethod.GET)
	public void directoryTreeViewHandler(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		UserDetails userDetail = ViewController.getUserDetails();
		User user = userFacade.getUserByLogin(userDetail.getUsername());
		String fakePath = request.getParameter("fakePath");
		String realPath = fileDataFacade.getFileRealPath(fakePath, user.getId());
		if (new File(realPath).exists()) {
			String[] files = new File(realPath).list(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.charAt(0) != '.';
				}
			});
			Arrays.sort(files, String.CASE_INSENSITIVE_ORDER);
			PrintWriter out = null;
			try {
				out = response.getWriter();
			} catch (IOException e) {
				logger.error("ViewController.directoryTreeViewHandler() threw ", e);
			}
			StringBuffer strBuf = new StringBuffer();
			Gson data = new Gson();
			JsonObject jsonObj = new JsonObject();
			strBuf.append("<div id=\"files_wrapper\">");
			for (String file : files) {
				File f = new File(realPath, file);
				if (f.isFile()) {
					int dotIndex = file.lastIndexOf('.');
					String ext = dotIndex > 0 ? file.substring(dotIndex + 1) : "";
					FileData fileData = new FileData(f.getName(), f.getPath(),
							user.getId());
					fileData = fileDataFacade.getFileId(fileData);
					strBuf.append("<div id=\"file_preview\">")
							.append("<div id=\"file_buttons\">")
							.append("<div id=\"file_delete_btn\"class=\"delete_file\">")
							.append("<img src=\"").append(request.getContextPath())
							.append("/resources/icons/delete_file.jpg\"/>")
							.append("</div>").append("<div id=\"file_rename_btn\">")
							.append("<img src=\"").append(request.getContextPath())
							.append("/resources/icons/rename_file.png\"/>")
							.append("</div>").append("</div>")
							.append("<div id=\"file_name\" class=\"file_name_class\">")
							.append("<span><a href=\"download/").append(fileData.getId())
							.append("\">").append(f.getName()).append("</a></span>")
							.append("</div>").append("<div id=\"file_img\">");
					if (ext.equals("jpg") || ext.equals("png")) {
						try {
							strBuf.append("<object data=\"download/")
									.append(fileData.getId()).append("\" type=\"")
									.append(Files.probeContentType(Paths.get(f.toURI())))
									.append("\"> </object>");
						} catch (IOException e) {
							logger.error("ViewController.fileTreeViewHandler() threw ", e);
						}
					} else if (ext.equals("pdf")) {
						strBuf.append("<img src=\"").append(request.getContextPath())
								.append("/resources/icons/pdf_150x150.jpg\"/>");
					} else if (ext.equals("txt")) {
						strBuf.append("<img src=\"").append(request.getContextPath())
								.append("/resources/icons/txt_150x150.jpg\"/>");
					} else {
						strBuf.append("<img src=\"").append(request.getContextPath())
								.append("/resources/icons/default_150x150.png\"/>");
					}
					strBuf.append("</div></div>");
					JsonElement element = data.toJsonTree(strBuf.toString());
					JsonElement fileId = data.toJsonTree(fileData.getId());
					jsonObj.add("html", element);
					jsonObj.add("fileId", fileId);
				}
			}
			strBuf.append("</div>");
			out.write(jsonObj.toString());
		}
	}

	@RequestMapping(value = "/view_file", method = RequestMethod.GET)
	public void fileViewer(HttpServletRequest request, HttpServletResponse response) {
		UserDetails userDetail = ViewController.getUserDetails();
		User user = userFacade.getUserByLogin(userDetail.getUsername());
		String fakePath = request.getParameter("fakePath");
		String realPath = fileDataFacade.getFileRealPath(fakePath, user.getId());
		if (realPath.charAt(realPath.length() - 1) == '\\') {
			realPath = realPath.substring(0, realPath.length() - 1) + "/";
		} else if (realPath.charAt(realPath.length() - 1) != '/') {
			realPath += "/";
		}
		String fileName = request.getParameter("fileName");
		File fileToPreview = new File(realPath, fileName);
		FileData fileData = new FileData(fileToPreview.getName(),
				fileToPreview.getPath(), user.getId());
		fileData = fileDataFacade.getFileId(fileData);
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			logger.error("ViewController.fileViewer() threw ", e);
		}
		Gson data = new Gson();
		JsonObject jsonObj = new JsonObject();
		if (fileToPreview.exists()) {
			JsonElement fileIdEl = data.toJsonTree(fileData.getId());
			jsonObj.add("fileId", fileIdEl);
			JsonElement contentType = null;
			try {
				String contentTypeFromFile = Files.probeContentType(Paths
						.get(fileToPreview.toURI()));
				contentType = data.toJsonTree(contentTypeFromFile);
			} catch (IOException e) {
				logger.error("ViewController.fileViewer() threw ", e);
			}
			jsonObj.add("contentType", contentType);
			out.write(jsonObj.toString());
		}
	}

	private static UserDetails getUserDetails() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			return (UserDetails) auth.getPrincipal();
		}
		return null;
	}
}