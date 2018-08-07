package fastdfs;

import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.ProtoCommon;
import org.csource.fastdfs.StorageClient1;
/**
* @author 作者
* @version 创建时间：2018年6月7日 上午11:10:21
* 类说明
*/
import org.junit.Test;

import com.webapp.util.FastDFSClient;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2016
 * </p>
 *
 * @author yangxin
 * @version 1.0
 * @date 2016/10/19
 */
public class FastDFSClientTest {

	/**
	 * 文件上传测试
	 * 
	 * @throws MyException
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 * @throws NumberFormatException
	 */
	@Test
	public void testUpload()
			throws NumberFormatException, UnsupportedEncodingException, NoSuchAlgorithmException, MyException {
		File file = new File("E:\\JavaSoft\\tx.jpg");
		Map<String, String> metaList = new HashMap<String, String>();
		metaList.put("width", "1024");
		metaList.put("height", "768");
		metaList.put("author", "yanlulu");
		metaList.put("date", "20180607");
		String fid = FastDFSClient.uploadFile(file, file.getName(), metaList);
		System.out.println("upload local file " + file.getPath() + " ok, fileid=" + fid);
		// 上传成功返回的文件ID： group1/M00/00/00/wKgAyVgFk9aAB8hwAA-8Q6_7tHw351.jpg

		int ts = (int) Instant.now().getEpochSecond()+100;
//
//		String token = ProtoCommon.getToken(file.getName(), ts, ClientGlobal.getG_secret_key());
		System.out.println(StringUtils.substringAfter(fid, "/")+"---"+ClientGlobal.getG_secret_key());
		String token = ProtoCommon.getToken(StringUtils.substringAfter(fid, "/"), ts, ClientGlobal.getG_secret_key());
		
		System.out.println(fid + "?token=" + token + "&ts=" + ts);
	}

	/**
	 * 文件下载测试
	 */
	@Test
	public void testDownload() {
		int r = FastDFSClient.downloadFile("group1/M00/00/ED/rBEADlsgt4aAR4goAAAzlsIUj6o13.DOCX",
				"\\data\\dispute_https\\a\\rBEADlsgt4aAR4goAAAzlsIUj6o13.DOCX");
		System.out.println(r == 0 ? "下载成功" : "下载失败");
	}

	/**
	 * 获取文件元数据测试
	 * group1/M00/00/00/rBEADlsaGkaAPp2NAABSHUi4pBA023.jpg?token=49faf27e89401cf28e32f97623a84d78&ts=1528437424
	 */
	@Test
	public void testGetFileMetadata() {
		Map<String, String> metaList = FastDFSClient
				.getFileMetadata("group1/M00/00/00/rBEADlsaGkaAPp2NAABSHUi4pBA023.jpg");
		for (Iterator<Map.Entry<String, String>> iterator = metaList.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<String, String> entry = iterator.next();
			String name = entry.getKey();
			String value = entry.getValue();
			System.out.println(name + " = " + value);
		}
	}
	
	@Test
	public void Test01(){
		try { 
            FastDFSClient.getFileInfo("group1", "M00/00/00/rBEADlsaIE2ATLWDAABSHUi4pBA129.jpg");
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
	}

	/**
	 * 文件删除测试
	 */
	@Test
	public void testDelete() {
		int r = FastDFSClient.deleteFile("group1/M00/00/00/wKgAyVgFk9aAB8hwAA-8Q6_7tHw351.jpg");
		System.out.println(r == 0 ? "删除成功" : "删除失败");
	}
}

