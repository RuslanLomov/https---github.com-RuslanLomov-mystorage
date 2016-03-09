package trainee.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import trainee.model.FileData;

public class Snippet {
	public static void main(String[] args) throws Exception {
		// String destinationFolder = "D:\\javaTest\\";
		String userRoot = "D:\\javaTest\\1\\";
		String fakeUserRoot = "src/";
		InputStream inStream = null;
		OutputStream outStream = null;

		ZipFile zipFile = new ZipFile("D:/javaTest2/mystoragezip.zip");

		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			FileData fileData;
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
			byte[] buffer = new byte[1024];
			int length;
			while ((length = inStream.read(buffer)) > 0) {
				outStream.write(buffer, 0, length);
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
					System.out.println(fileData);									
				}
			} else {
				name = entry.getName();
				path = userRoot + entry.getName();
				fakePath = fakeUserRoot + entry.getName() + "/";
				fileData = new FileData(name, path, fakePath, 2);
				System.out.println(fileData);				
			}
			// if (!fileDataFacade.isDatabaseFileExist(fileData)) {
			// fileDataFacade.insertIntoFiles(fileData);
			// }	
		}

		// HttpClient httpclient = new DefaultHttpClient();
		// httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
		// HttpVersion.HTTP_1_1);

		// HttpPost httppost = new HttpPost(
		// "http://localhost:8080/mystorage/upload_from_client_app");
		// File file = new File("D:/javaTest2/mystoragezip.zip");
		//
		// MultipartEntity mpEntity = new MultipartEntity();
		// ContentBody cbFile = new FileBody(file, "multipart/form-data");
		// mpEntity.addPart("file", cbFile);
		//
		// httppost.setEntity(mpEntity);
		// System.out.println("executing request " + httppost.getRequestLine());
		//
		// HttpResponse response = httpclient.execute(httppost);
		// HttpEntity resEntity = response.getEntity();
		//
		// System.out.println(response.getStatusLine());
		// if (resEntity != null) {
		// System.out.println(EntityUtils.toString(resEntity));
		// }
		// if (resEntity != null) {
		// resEntity.consumeContent();
		// }
		//
		// httpclient.getConnectionManager().shutdown();
	}
}
