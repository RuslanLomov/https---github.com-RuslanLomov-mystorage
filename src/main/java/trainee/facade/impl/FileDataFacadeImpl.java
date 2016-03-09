package trainee.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import trainee.dao.interf.FileDataDao;
import trainee.facade.interf.FileDataFacade;
import trainee.model.FileData;

@Service
@Transactional
public class FileDataFacadeImpl implements FileDataFacade {
	private static final long serialVersionUID = 1L;
	@Autowired
	private FileDataDao fileDataDAO;

	@Override
	public Integer insertIntoFiles(FileData file) {
		return fileDataDAO.insertIntoFiles(file);
	}

	@Override
	public boolean isDatabaseFileExist(FileData file) {
		boolean isExist = false;
		FileData fileFromDatabase = fileDataDAO.getFileByPath(file);
		if (fileFromDatabase != null) {
			if (file.getName().equals(fileFromDatabase.getName())) {
				isExist = true;
			}
		}
		return isExist;
	}

	@Override
	public String getFileRealPath(String fakePath, Integer userId) {
		return fileDataDAO.getFileRealPath(fakePath, userId);
	}

	@Override
	public FileData getFileByPath(FileData file) {
		return fileDataDAO.getFileByPath(file);
	}

	@Override
	public FileData getFileId(FileData file) {
		return fileDataDAO.getFileId(file);
	}

	@Override
	public FileData getFileById(FileData file) {
		return fileDataDAO.getFileById(file);
	}

	@Override
	public Integer deleteFile(String realPath, Integer userId) {
		return fileDataDAO.deleteFile(realPath, userId);
	}

	@Override
	public Integer renameFile(FileData file) {
		return fileDataDAO.renameFile(file);
	}

	@Override
	public Integer renameReferencedFile(FileData file) {
		return fileDataDAO.renameReferencedFile(file);
	}

}
