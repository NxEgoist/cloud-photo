package com.cloud.photo.trans.utils;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSONObject;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.buf.HexUtils;

import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

public class S3Util {

//    华为云OBS
    private static String accessKey = "ZN6F6EFBKXFUOTDLERY0";
    private static String secretKey = "Bf5dR6d3vQC8fPbFL58zvI4C3LyWYQSZuXt3shj2";
    private static String bucketName = "cloud-photo-3121004567";
    private static String serviceEndpoint = "http://obs.cn-south-1.myhuaweicloud.com";

//    minio
//    private static String accessKey = "0GNFhjVHbHYvABsfh4J8";
//    private static String secretKey = "cay94SyfqcgkZG0DR2dRq49taD0Hexbd0YBruRkA";
//    private static String bucketName = "cloud-photo";
//    private static String serviceEndpoint = "http://127.0.0.1:9000";


    private static String containerId = "10001";

    /**
     * 获取上传地址
     * @param suffixName
     * @param fileMd5
     * @return
     */
    public static String getPutUploadUrl(String suffixName,String fileMd5) {

        //链接过期时间
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 10;
        expiration.setTime(expTimeMillis);
        String objectId = UUID.randomUUID().toString().replaceAll("-","");
        String base64Md5 = "";
        if(StringUtils.isNotBlank(suffixName)){
            objectId = objectId +"." + suffixName;
        }
        //建立S3客户端，获取上传地址
        AmazonS3 s3Client = getAmazonS3Client();
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, objectId)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(expiration);
        if(StringUtils.isNotBlank(fileMd5)){
            base64Md5= Base64.encode(HexUtils.fromHexString(fileMd5));
            generatePresignedUrlRequest = generatePresignedUrlRequest.withContentMd5(base64Md5);
        }
        URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);

        JSONObject jsonObject =new JSONObject();
        jsonObject.put("objectId",objectId);
        jsonObject.put("url",url);
        jsonObject.put("containerId",containerId);
        jsonObject.put("base64Md5",base64Md5);
        return jsonObject.toJSONString();
    }

    /**
     * 获取文件资源池信息
     * @param objectId
     * @return
     */
    public static S3ObjectSummary getObjectInfo(String objectId){
        AmazonS3 s3Client = getAmazonS3Client();

        ListObjectsV2Request listObjectsRequest = new ListObjectsV2Request()
                .withBucketName(bucketName)
                .withPrefix(objectId);

        ListObjectsV2Result listing = s3Client.listObjectsV2(listObjectsRequest);
        List<S3ObjectSummary> s3ObjectSummarieList = listing.getObjectSummaries();

        if(s3ObjectSummarieList!=null && s3ObjectSummarieList.size()>0){
            return s3ObjectSummarieList.get(0);
        }
        return null;
    }

    public static String getDownloadUrl(String containerId, String objectId){
        return getDownloadUrl(containerId, objectId,null);
    }

    public static String getDownloadUrl(String containerId, String objectId,String filename) {
        AmazonS3 s3Client = getAmazonS3Client();
        GeneratePresignedUrlRequest httpRequest = new GeneratePresignedUrlRequest(bucketName, objectId);
        //设置过期时间
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 10;
        expiration.setTime(expTimeMillis);
        httpRequest.setExpiration(expiration);

        if(StringUtils.isNotBlank(filename)){
            String responseContentDisposition = null;
            try {
                responseContentDisposition = "attachment;filename=" + URLEncoder.encode(filename, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            httpRequest.addRequestParameter("response-content-disposition", responseContentDisposition);
        }
        return s3Client.generatePresignedUrl(httpRequest).toString();

    }

    private static AmazonS3 getAmazonS3Client(){
        AwsClientBuilder.EndpointConfiguration endpointConfiguration =new AwsClientBuilder.EndpointConfiguration(serviceEndpoint,"");
        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new ProfileCredentialsProvider())
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withEndpointConfiguration(endpointConfiguration)
                .build();
        return s3Client;
    }

    /**
     * 查询桶列表
     */
    public static void getBuckets() {
        AmazonS3 s3Client = getAmazonS3Client();
        List<Bucket> bucketList = s3Client.listBuckets();
        for (int i = 0; i < bucketList.size(); i++) {
            System.out.println(bucketList.get(i).getName());
        }
    }



    /**
     * 查询桶的文件
     */

    public static void listObjects() {
        AmazonS3 s3Client = getAmazonS3Client();
        ListObjectsRequest listObjectRequest = new ListObjectsRequest();
        ObjectListing objectListing = null;
        listObjectRequest.setBucketName(bucketName);
        // listObjectRequest.setPrefix("DATALAKE/LF02/");

        int j=0;
        int num =0;
        do {
            objectListing = s3Client.listObjects(listObjectRequest);
            List<S3ObjectSummary> s3ObjectSummaries = objectListing.getObjectSummaries();
            if (s3ObjectSummaries != null && s3ObjectSummaries.size() > 0) {
                for (int i = 0; i < s3ObjectSummaries.size(); i++) {
                    num =j*1000+i;
                    System.out.println(num+"|"+j+"|"+i + "|" + s3ObjectSummaries.get(i).getKey() + "|" + s3ObjectSummaries.get(i).getETag());
                }
            }
            j++;
            listObjectRequest.setMarker(objectListing.getNextMarker());
        } while (objectListing.isTruncated());
    }

    /**
     * 生成下载地址
     */

    public static void  genDownloadUrl  (){
        AmazonS3 s3Client = getAmazonS3Client();
        //华为obs存储文件路径
        String key = "img.jpg";
        //一小时后
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR,1);
        Date expiration = calendar.getTime();
        //生成下载地址
        URL url = s3Client.generatePresignedUrl(bucketName,key,expiration);
        String downloadUrl = url.getProtocol()+"://"+url.getHost()+":9000"+url.getFile();

        System.out.println(downloadUrl);
    }

    /**
     * 一次上传
     */

    public static void putObjectTest(){
        AmazonS3 s3Client = getAmazonS3Client();
        String key = "img.jpg";
        File file = new File("C:\\Users\\kiyana\\Pictures\\Saved Pictures/2b-lollipop-nier-automata-uhdpaper.com-4K-6.603(1).png");
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,key,file);
        s3Client.putObject(putObjectRequest);
        System.out.println("putObjectTest success + src="+file.getName()+",obs dest ="+key);
    }

    /**
     * 分片上传
     */

    public static void multiPut()  {
        AmazonS3 s3Client = getAmazonS3Client();
        //初始化上传
        String key = "b800a_4k.jpg";
        InitiateMultipartUploadRequest initiateMultipartUploadRequest = new InitiateMultipartUploadRequest(bucketName,key);
        InitiateMultipartUploadResult initiateMultipartUploadResult = s3Client.initiateMultipartUpload(initiateMultipartUploadRequest);
        if(initiateMultipartUploadResult!=null){
            String uploadId = initiateMultipartUploadResult.getUploadId();
            //上传文件
            UploadPartRequest uploadPartRequest = new UploadPartRequest().withUploadId(uploadId);
            uploadPartRequest.setUploadId(uploadId);
            uploadPartRequest.setBucketName(bucketName);
            uploadPartRequest.setKey(key);
            List<PartETag> partETags = new ArrayList<>();
            byte[] content = new byte[5*1024*1024];
            String filePath = "C:\\Users\\kiyana\\Pictures\\Saved Pictures/b800a_奥兹4k.jpg";
            File file = new File(filePath);
            try{
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                int readSize = 0;
                int partNum =1;
                while ((readSize = bis.read(content)) > 0) {
                    uploadPartRequest.setPartSize(readSize);
                    uploadPartRequest.setPartNumber(partNum);
                    uploadPartRequest.setInputStream(new ByteArrayInputStream(content));
                    partNum++;
                    UploadPartResult  uploadPartResult= s3Client.uploadPart(uploadPartRequest);
                    partETags.add(uploadPartResult.getPartETag());
                    System.out.println("uploadPartResult:"+uploadPartResult.getPartNumber()+"|"+uploadPartResult.getETag()+"|"+uploadPartResult.getServerSideEncryption()+"|"+uploadPartResult.getPartETag());
                }


            }catch (Exception e){
                e.printStackTrace();
            }


            //合并提交
            CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(bucketName, key, uploadId, partETags);
            s3Client.completeMultipartUpload(compRequest);
        }
    }

    /**
     * 刪除文件
     */

    public static void delObject(){
        AmazonS3 s3Client = getAmazonS3Client();

        String key = "123.jpg";
        s3Client.deleteObject(bucketName,key);

    }






    public static void main(String[] args) {


//      getBuckets();

//        listObjects();

//       putObjectTest();

//        multiPut();


//       genDownloadUrl();
       // delObject();
        String url = getDownloadUrl("10001","b800a_4k.jpg");
       //  System.out.println("uuuuu======"+url);

        System.out.println(url);

    }
}
