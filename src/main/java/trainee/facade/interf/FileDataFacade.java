package trainee.facade.interf;

import java.io.Serializable;

import trainee.model.FileData;

public interface FileDataFacade extends Serializable {

	public Integer insertIntoFiles(FileData file);

	public boolean isDatabaseFileExist(FileData file);

	public String getFileRealPath(String fakePath, Integer userId);

	public FileData getFileByPath(FileData file);

	public FileData getFileId(FileData file);

	public FileData getFileById(FileData file);

	public Integer deleteFile(String realPath, Integer userId);

	public Integer renameFile(FileData file);

	public Integer renameReferencedFile(FileData file);

}
