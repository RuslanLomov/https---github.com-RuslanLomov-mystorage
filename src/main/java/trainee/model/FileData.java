package trainee.model;

public class FileData {
	private Integer id;
	private String name;
	private String path;
	private String fakePath;
	private Integer userId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String fileName) {
		name = fileName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFakePath() {
		return fakePath;
	}

	public void setFakePath(String fakePath) {
		this.fakePath = fakePath;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer fileRef) {
		userId = fileRef;
	}

	public FileData() {

	}

	public FileData(String name, String path) {
		this.name = name;
		this.path = path;
	}

	public FileData(Integer id, Integer userId) {
		this.id = id;
		this.userId = userId;
	}

	public FileData(String name, String path, Integer userId) {
		this.name = name;
		this.path = path;
		this.userId = userId;
	}

	public FileData(Integer id, String name, String path, Integer userId) {
		this.id = id;
		this.name = name;
		this.path = path;
		this.userId = userId;
	}

	public FileData(String name, String filePath, String fakePath, Integer userId) {
		this.name = name;
		this.path = filePath;
		this.fakePath = fakePath;
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "FileData [id=" + id + ", name=" + name + ", path=" + path + ", fakePath="
				+ fakePath + ", userId=" + userId + "]";
	}

}
