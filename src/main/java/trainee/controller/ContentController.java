package trainee.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import trainee.facade.interf.FileDataFacade;
import trainee.facade.interf.UserFacade;
import trainee.model.FileData;
import trainee.model.User;

@Controller
public class ContentController {
	private static final Logger logger = LoggerFactory.getLogger("MyLogger_ALL");
	private static final String MAIN_ROOT = "D:/javaTest/";
	@Autowired
	private UserFacade userFacade;
	@Autowired
	private FileDataFacade fileDataFacade;

	@RequestMapping("/main")
	public ModelAndView main() {
		return new ModelAndView("content/main", "", null);
	}

	@RequestMapping("/download/{fileId}")
	public void downloadFile(@PathVariable Integer fileId, HttpServletRequest req,
			HttpServletResponse resp) {
		UserDetails userDetails = ContentController.getUserDetails();
		User user = userFacade.getUserByLogin(userDetails.getUsername());
		PrintWriter out = null;
		try {
			out = resp.getWriter();
		} catch (IOException e) {
			logger.error("ContentController.downloadFile() threw ", e);
		}
		FileData file = new FileData(fileId, user.getId());
		file = fileDataFacade.getFileById(file);
		String contentType = null;
		try {
			contentType = Files.probeContentType(Paths.get(new File(file.getPath())
					.toURI()));
		} catch (IOException e) {
			logger.error("ContentController.downloadFile() threw ", e);
		}
		resp.setContentType(contentType);
		resp.setHeader("Content-Disposition", "filename=\"" + file.getName() + "\"");
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(file.getPath());
			int i;
			while ((i = fileInputStream.read()) != -1) {
				out.write(i);
			}
			fileInputStream.close();
		} catch (IOException e) {
			logger.error("ContentController.downloadFile() threw ", e);
		}
		out.close();
	}

	@RequestMapping(value = "/upload_file", method = RequestMethod.POST)
	public ModelAndView uploadFile(HttpServletRequest req) {
		try {
			req.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("ContentController.uploadFile() threw ", e);
		}
		UserDetails userDetails = ContentController.getUserDetails();
		User user = userFacade.getUserByLogin(userDetails.getUsername());
		String fakePath = req.getHeader("destRoot");
		String realPath = fileDataFacade.getFileRealPath(fakePath, user.getId());
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			List<FileItem> items = upload.parseRequest(req);
			Iterator<FileItem> i = items.iterator();
			while (i.hasNext()) {
				FileItem fi = (FileItem) i.next();
				File file = new File(realPath + "/" + fi.getName());
				FileData currentFileData = new FileData(fi.getName(), file.getPath(),
						fakePath + fi.getName() + "/", user.getId());
				if (!fileDataFacade.isDatabaseFileExist(currentFileData)) {
					fileDataFacade.insertIntoFiles(currentFileData);
				}
				fi.write(file);
			}
		} catch (Exception e) {
			logger.error("ContentController.uploadFile() threw ", e);
		}
		return main();
	}

	// @RequestMapping(value = "/upload_from_client_app", method =
	// RequestMethod.POST)
	// public @ResponseBody
	// String handleFileUpload(@RequestParam("file") MultipartFile file) {
	// System.out.println("Inside /upload_from_client_app");
	// String name = "test11";
	// // System.out.println(file.getName());
	// if (!file.isEmpty()) {
	// try {
	// byte[] bytes = file.getBytes();
	// BufferedOutputStream stream = new BufferedOutputStream(
	// new FileOutputStream(
	// new File("D:/javaTest/" + name + "-uploaded")));
	// stream.write(bytes);
	// stream.close();
	// return "You successfully uploaded " + name + " into " + name
	// + "-uploaded !";
	// } catch (Exception e) {
	// return "You failed to upload " + name + " => " + e.getMessage();
	// }
	// } else {
	// return "You failed to upload " + name + " because the file was empty.";
	// }
	// // return "DONE!";
	// }

	@RequestMapping(value = "/upload_from_client_app", method = RequestMethod.POST)
	public ModelAndView uploadClientAppFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		System.out.println("Inside /upload_from_client_app");
		// String SAVE_DIR = "D:/javaTest/";
		// Gets file name for HTTP header
		String fileName = request.getHeader("fileName");
		File saveFile = new File(MAIN_ROOT + fileName);

		// prints out all header values
		System.out.println("===== Begin headers =====");
		Enumeration<String> names = request.getHeaderNames();
		while (names.hasMoreElements()) {
			String headerName = names.nextElement();
			System.out.println(headerName + " = " + request.getHeader(headerName));
		}
		System.out.println("===== End headers =====\n");

		// opens input stream of the request for reading data
		InputStream inputStream = null;
		FileOutputStream outputStream = null;

		inputStream = request.getInputStream();
		// opens an output stream for writing file
		outputStream = new FileOutputStream(saveFile);

		byte[] buffer = new byte[4096];
		int bytesRead = -1;
		// System.out.println("Receiving data...");

		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, bytesRead);
		}
		System.out.println("Data received.");
		outputStream.close();
		inputStream.close();

		String userRoot = "D:\\javaTest\\1\\";
		String fakeUserRoot = "src/";
		InputStream inStream = null;
		OutputStream outStream = null;

		ZipFile zipFile = new ZipFile(saveFile.getPath());

		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			FileData fileData = null;
			String name;
			String path;
			String fakePath;
			File output = new File(userRoot + entry.getName());
			if (!output.getParentFile().exists()) {
				output.getParentFile().mkdirs();
			}
			InputStream stream = zipFile.getInputStream(entry);
			inStream = stream;
			outStream = new FileOutputStream(output);
			// byte[] buffer = new byte[1024];
			// int length;
			while ((bytesRead = inStream.read(buffer)) > 0) {
				outStream.write(buffer, 0, bytesRead);
			}
			if (inStream != null)
				inStream.close();
			if (outStream != null)
				outStream.close();

			if (entry.getName().contains("\\")) {
				String files[] = entry.getName().split("\\\\");
				for (int i = 0; i < files.length; i++) {
					if (i == 0) {
						name = files[i];
						path = userRoot + files[i];
						fakePath = fakeUserRoot + files[i] + "/";
						fileData = new FileData(name, path, fakePath, 2);
					} else {
						String resultPath = "";
						String resulFaketPath = "";
						for (int j = 0; j <= i; j++) {
							if (j != i) {
								resultPath += files[j] + "\\";
							} else {
								resultPath += files[j];
							}
							resulFaketPath += files[j] + "/";
						}
						name = files[i];
						path = userRoot + resultPath;
						fakePath = fakeUserRoot + resulFaketPath;
						fileData = new FileData(name, path, fakePath, 2);
					}
					if (!fileDataFacade.isDatabaseFileExist(fileData)) {
						System.out.println("file to insert into database: " + fileData);
						fileDataFacade.insertIntoFiles(fileData);
					}
				}
			} else {
				name = entry.getName();
				path = userRoot + entry.getName();
				fakePath = fakeUserRoot + entry.getName() + "/";
				fileData = new FileData(name, path, fakePath, 2);
				// System.out.println(fileData);
			}
			if (!fileDataFacade.isDatabaseFileExist(fileData)) {
				System.out.println("file to insert into database: " + fileData);
				fileDataFacade.insertIntoFiles(fileData);
			}
		}
		zipFile.close();
		System.out.println("file to delete: " + saveFile);
		saveFile.delete();
		// sends response to client
		response.getWriter().print("UPLOAD DONE");
		return null;
	}

	private static UserDetails getUserDetails() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			return (UserDetails) auth.getPrincipal();
		}
		return null;
	}
}