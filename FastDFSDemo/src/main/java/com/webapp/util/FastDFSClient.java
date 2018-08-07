package com.webapp.util;

/**
* @author 作者
* @version 创建时间：2018年6月7日 上午9:39:47
* 类说明
*/
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * <p>Description: FastDFS文件上传下载工具类 </p>
 */
public class FastDFSClient {

//    private static final String FAST_DFS_CONF_FILE = "src/main/resources/fastdfs/fdfs_client.conf";
    
    private static final String FAST_DFS_CONF_FILE = "E:\\GitWorkSpace\\ODR_R253\\ODR2-Web\\dispute_DMMR\\src\\main\\resources\\fastdfs\\fdfs_client.conf";

    private static StorageClient1 storageClient1 = null;

    // 初始化FastDFS Client
    static {
        try {
            ClientGlobal.init(FAST_DFS_CONF_FILE);
            TrackerClient trackerClient = new TrackerClient(ClientGlobal.g_tracker_group);
            TrackerServer trackerServer = trackerClient.getConnection();
            if (trackerServer == null) {
                throw new IllegalStateException("getConnection return null");
            }

            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            if (storageServer == null) {
                throw new IllegalStateException("getStoreStorage return null");
            }

            storageClient1 = new StorageClient1(trackerServer,storageServer);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传文件
     * @param file 文件对象
     * @param fileName 文件名
     * @return
     */
    public static String uploadFile(File file, String fileName) {
        return uploadFile(file,fileName,null);
    }

    /**
     * 上传文件
     * @param file 文件对象
     * @param fileName 文件名
     * @param metaList 文件元数据
     * @return
     */
    public static String uploadFile(File file, String fileName, Map<String,String> metaList) {
    	InputStream ins = null;
    	try {
    		ins=new FileInputStream(file);
            byte[] buff = IOUtils.toByteArray(ins);
            NameValuePair[] nameValuePairs = null;
            if (metaList != null) {
                nameValuePairs = new NameValuePair[metaList.size()];
                int index = 0;
                for (Iterator<Map.Entry<String,String>> iterator = metaList.entrySet().iterator(); iterator.hasNext();) {
                    Map.Entry<String,String> entry = iterator.next();
                    String name = entry.getKey();
                    String value = entry.getValue();
                    nameValuePairs[index++] = new NameValuePair(name,value);
                }
            }
            fileName.substring(fileName.lastIndexOf("."));//如果想获得不带点的后缀，变为fileName.lastIndexOf(".")+1
            return storageClient1.upload_file1(buff,fileName.substring(fileName.lastIndexOf(".")+1),nameValuePairs);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
			if(ins!=null){
				try {
					ins.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
        return null;
    }
    
    /**
     * 上传文件
     * @param buff 文件流
     * @param fileName 文件名
     * @param metaList 文件元数据
     * @return
     */
    public static String uploadFileByByte(byte[] buff, String fileName, Map<String,String> metaList) {
    	try {
            NameValuePair[] nameValuePairs = null;
            if (metaList != null) {
                nameValuePairs = new NameValuePair[metaList.size()];
                int index = 0;
                for (Iterator<Map.Entry<String,String>> iterator = metaList.entrySet().iterator(); iterator.hasNext();) {
                    Map.Entry<String,String> entry = iterator.next();
                    String name = entry.getKey();
                    String value = entry.getValue();
                    nameValuePairs[index++] = new NameValuePair(name,value);
                }
            }
            fileName.substring(fileName.lastIndexOf("."));//如果想获得不带点的后缀，变为fileName.lastIndexOf(".")+1
            return storageClient1.upload_file1(buff,fileName.substring(fileName.lastIndexOf(".")+1),nameValuePairs);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
		}
        return null;
    }

    /**
     * 获取文件元数据
     * @param fileId 文件ID
     * @return
     */
    public static Map<String,String> getFileMetadata(String fileId) {
        try {
            NameValuePair[] metaList = storageClient1.get_metadata1(fileId);
            if (metaList != null) {
                HashMap<String,String> map = new HashMap<String, String>();
                for (NameValuePair metaItem : metaList) {
                    map.put(metaItem.getName(),metaItem.getValue());
                }
                return map;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除文件
     * @param fileId 文件ID
     * @return 删除失败返回-1，否则返回0
     */
    public static int deleteFile(String fileId) {
        try {
            return storageClient1.delete_file1(fileId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 下载文件
     * @param fileId 文件ID（上传文件成功后返回的ID）
     * @param outFile 文件下载保存位置
     * @return
     */
    public static int downloadFile(String fileId, String outFile) {
        FileOutputStream fos = null;
        try {
            byte[] content = storageClient1.download_file1(fileId);
            fos = new FileOutputStream(outFile);
            IOUtils.write(content, fos); 
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }
    
    /** 
     * 获取文件数组 
     * @param path 文件的路径 如group1/M00/00/00/wKgRsVjtwpSAXGwkAAAweEAzRjw471.jpg 
     * @return 
     */  
    public static byte[] download_bytes(String path) {  
        byte[] b=null;  
        try {  
            b = storageClient1.download_file1(path);  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (MyException e) {  
            e.printStackTrace();  
        }  
        return b;  
    }
    
    public static void getFileInfo(String group,String fileId) throws IOException, MyException{
         FileInfo fi = storageClient1.get_file_info(group, fileId); 
         System.out.println(fi.getSourceIpAddr()); 
         System.out.println(fi.getFileSize()); 
         System.out.println(fi.getCreateTimestamp()); 
         System.out.println(fi.getCrc32());
    }

    public static String getUrlByFileid(String fileId) throws NoSuchAlgorithmException, MyException, IOException{
    	String url_last="";
		try {
			int ts = (int) Instant.now().getEpochSecond()+100;
			String token = ProtoCommon.getToken(org.apache.commons.lang3.StringUtils.substringAfter(fileId, "/"), ts, ClientGlobal.getG_secret_key());
			url_last=fileId + "?token=" + token + "&ts=" + ts;
			System.out.println(url_last);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {

		}
		return url_last;
    }
    
    /**
     * 删除单个文件
     *
     * @param fileName
     *            要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile1(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }
}
