package com.example.demo.baiduyun;

import com.baidubce.BceClientConfiguration;
import com.baidubce.auth.BceCredentials;
import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.auth.DefaultBceSessionCredentials;
import com.baidubce.model.ListResponse;
import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.BosClientConfiguration;
import com.baidubce.services.bos.model.*;
import com.baidubce.services.sts.StsClient;
import com.baidubce.services.sts.model.GetSessionTokenRequest;
import com.baidubce.services.sts.model.GetSessionTokenResponse;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kejun
 * @date 2019/11/12 上午9:43
 */
public class BosTest {
    private static final String ak = "e806401a5a384990859bee8caef0eb9b";
    private static final String sk = "22cdaa8de7444b818b0c25abbb620f18";
    private static final String ep = "fsh.bcebos.com";
    private static final String stsep = "http://sts.bj.baidubce.com";

    private static final String ak2 = "865180850f1f456186c85e729c29bd8f";
    private static final String sk2 = "72cfe2036a8244f9972f0ce1cf529175";
    private static final String ep2 = "dashu-ocr-img.su.bcebos.com";


    public static void main(String[] args) throws IOException {
        String ACCESS_KEY_ID = ak2;                   // 用户的Access Key ID
        String SECRET_ACCESS_KEY = sk2;           // 用户的Secret Access Key

        // 初始化一个BosClient
        BosClientConfiguration config = new BosClientConfiguration();
        config.setCredentials(new DefaultBceCredentials(ACCESS_KEY_ID, SECRET_ACCESS_KEY));
        config.setEndpoint(ep);
        BosClient client = new BosClient(config);
//        createBucket(client,"ocr-test2");

//        PutObject(client,"ocr-test2","img");//3c507ffa84e2d8508b93db30de34098d
//        generatePresignedUrl(client,"ocr-test2","img",1000);
//        getFile("ocr-test2","img",client);
        listObjects(client,"ocr-test2");
        getObject(client,"ocr-test2","img");
//        BceCredentials credentials = new DefaultBceCredentials(ACCESS_KEY_ID, SECRET_ACCESS_KEY);
//        StsClient client = new StsClient(
//                new BceClientConfiguration().withEndpoint(ep2).withCredentials(credentials)
//        );
//        GetSessionTokenResponse response = client.getSessionToken(new GetSessionTokenRequest());
//        BceCredentials bosstsCredentials = new DefaultBceSessionCredentials(
//                response.getAccessKeyId(),
//                response.getSecretAccessKey(),
//                response.getSessionToken());
//        System.out.println("==================================");
//        System.out.println("GetSessionToken result:");
//        System.out.println("    accessKeyId:  " + response.getAccessKeyId());
//        System.out.println("    secretAccessKey:  " + response.getSecretAccessKey());
//        System.out.println("    securityToken:  " + response.getSessionToken());
//        System.out.println("    expiresAt:  " + response.getExpiration().toString());
//        System.out.println("==================================");
//
//        // build bos client
//        BosClientConfiguration config = new BosClientConfiguration();
//        config.setCredentials(bosstsCredentials);
//        BosClient bosClient = new BosClient(config);
//        createBucket(bosClient, "test");
    }

    /**
     * Bucket 管理 start
     **/
    //Bucket设置权限
    public void setBucketPrivate(BosClient client, String bucketName) {
        client.setBucketAcl("", CannedAccessControlList.Private);
    }

    public static void createBucket(BosClient client, String bucketName) {
        // 新建一个Bucket
        client.createBucket(bucketName);                               //指定Bucket名称
    }

    // 遍历用户的Bucket列表中所有Bucket
    public void listBuckets(BosClient client) {
        // 获取用户的Bucket列表
        List<BucketSummary> buckets = client.listBuckets().getBuckets();

        // 遍历Bucket
        for (BucketSummary bucket : buckets) {
            System.out.println(bucket.getName());
        }
    }

    // 删除Bucket
    public void deleteBucket(BosClient client, String bucketName) {
        client.deleteBucket(bucketName);                                //指定Bucket名称
    }

    //判断某个Bucket是否存在
    public void doesBucketExist(BosClient client, String bucketName) {

        // 获取Bucket的存在信息
        boolean exists = client.doesBucketExist(bucketName);                //指定Bucket名称

        // 输出结果
        if (exists) {
            System.out.println("Bucket exists");
        } else {
            System.out.println("Bucket not exists");
        }
    }
    /**Bucket 管理 end**/

    /**
     * 上传文件start
     **/

    //用户操作的基本数据单元是Object。Object包含Key、Meta和Data
    //其中，Key是Object的名字；Meta是用户对该Object的描述，由一系列Name-Value对组成；Data是Object的数据

    //1.简单上传 - 支持以指定文件形式、以数据流方式、以二进制串方式、以字符串方式执行Object上传
    //PutObject函数支持不超过5GB的Object上传
    //在PutObject请求处理成功后，BOS会在Header中返回Object的ETag作为文件标识
    public static void PutObject(BosClient client, String bucketName, String objectKey) throws FileNotFoundException {
        // 获取指定文件
        File file = new File("/Users/juin/Downloads/front.jpg");
        // 获取数据流
        InputStream inputStream = new FileInputStream("/Users/juin/Downloads/front.jpg");

        // 以文件形式上传Object
//        PutObjectResponse putObjectFromFileResponse = client.putObject(bucketName, objectKey, file);
        // 以数据流形式上传Object
        PutObjectResponse putObjectResponseFromInputStream = client.putObject(bucketName, objectKey, inputStream);
        // 以二进制串上传Object
//        PutObjectResponse putObjectResponseFromByte = client.putObject(bucketName, objectKey, byte1);
        // 以字符串上传Object
//        PutObjectResponse putObjectResponseFromString = client.putObject(bucketName, objectKey, string1);

        // 打印ETag
        System.out.println(putObjectResponseFromInputStream.getETag());
    }

    //注意点 - putObject设置文件元信息
    public void putMetaData(BosClient client, String bucketName, String objectKey, String content) {
        // 初始化上传输入流
        ObjectMetadata meta = new ObjectMetadata();

        // 设置ContentLength大小
        meta.setContentLength(1000);

        // 设置ContentType
        meta.setContentType("application/json");

        // 设置cache-control
        meta.setCacheControl("no-cache");

        // 设置x-bce-content-crc32
        meta.setxBceCrc("crc");

        // 设置自定义元数据name的值为my-data
        meta.addUserMetadata("name", "my-data");

        client.putObject(bucketName, objectKey, content, meta);
    }

    //object的拷贝规则

    //2.追加上传-AppendObject，AppendObject大小限制为0~5G. 适用于日志、视频监控、视频直播等数据复写较频繁的场景中
    public void AppendObject(BosClient client, String bucketName, String objectKey, byte[] byte1, String string1) throws FileNotFoundException {
        // 获取指定文件
        File file = new File("/path/to/file.zip");
        // 获取数据流
        InputStream inputStream = new FileInputStream("/path/to/test.zip");

        // 以文件形式上传Object
        AppendObjectResponse appendObjectFromFileResponse = client.appendObject(bucketName, objectKey, file);
        // 以数据流形式上传Object
        AppendObjectResponse appendObjectResponseFromInputStream = client.appendObject(bucketName, objectKey, inputStream);
        // 以二进制串上传Object
        AppendObjectResponse appendObjectResponseFromByte = client.appendObject(bucketName, objectKey, byte1);
        // 以字符串上传Object
        AppendObjectResponse appendObjectResponseFromString = client.appendObject(bucketName, objectKey, string1);

        // 打印ETag
        System.out.println(appendObjectFromFileResponse.getETag());
        // 打印NextAppendOffset
        System.out.println(appendObjectFromFileResponse.getNextAppendOffset());
        // 打印ContentMd5
        System.out.println(appendObjectFromFileResponse.getContentMd5());

        // 追加上传的示例，需要在请求中加上下次追加写的位置
        Long nextAppendOffset = appendObjectFromFileResponse.getNextAppendOffset();
        AppendObjectRequest appendObjectFromFileRequest = new AppendObjectRequest(bucketName, objectKey, file);
        appendObjectFromFileRequest.setOffset(appendObjectFromFileResponse.getNextAppendOffset());
        AppendObjectResponse appendObjectFromFileResponse2 = client.appendObject(appendObjectFromFileRequest);
    }


    //3.分块上传-(Multipart Upload)
    //适用于 需要支持断点上传;上传超过5GB大小的文件;网络条件较差，和BOS的服务器之间的连接经常断开;需要流式地上传文件;上传文件之前，无法确定上传文件的大小

    //初始化Multipart Upload
    public void multipartUpload(String bucketName, String objectKey, BosClient client) {
        // 开始Multipart Upload
        InitiateMultipartUploadRequest initiateMultipartUploadRequest =
                new InitiateMultipartUploadRequest(bucketName, objectKey);
        InitiateMultipartUploadResponse initiateMultipartUploadResponse =
                client.initiateMultipartUpload(initiateMultipartUploadRequest);

        // 打印UploadId - initiateMultipartUpload 的返回结果中含有 UploadId,它是区分分块上传事件的唯一标识
        System.out.println("UploadId: " + initiateMultipartUploadResponse.getUploadId());
    }

    public void putMultiUploadStorageClass(String bucketName, String objectKey, BosClient client){
        InitiateMultipartUploadRequest iniReq = new InitiateMultipartUploadRequest(bucketName, objectKey);
        //上传低频存储类型Object的初始化
        iniReq.withStorageClass(BosClient.STORAGE_CLASS_STANDARD_IA);
        ////上传冷存储类型Object的初始化
        iniReq.withStorageClass(BosClient.STORAGE_CLASS_COLD);
        client.initiateMultipartUpload(iniReq);
    }

    //上传分块
    //建议在UploadPart后，使用每个分块BOS返回的Content-MD5值分别验证已上传分块数据的正确性。当所有分块数据合成一个Object后，不再含MD5值
    public void startMultipartUpload(String bucketName, String objectKey,BosClient client,InitiateMultipartUploadResponse initiateMultipartUploadResponse) throws IOException {
        // 设置每块为 5MB
        final long partSize = 1024 * 1024 * 5L;

        File partFile = new File("/path/to/file.zip");

        // 计算分块数目
        int partCount = (int) (partFile.length() / partSize);
        if (partFile.length() % partSize != 0){
            partCount++;
        }

        // 新建一个List保存每个分块上传后的ETag和PartNumber
        List<PartETag> partETags = new ArrayList<PartETag>();
        for(int i = 0; i < partCount; i++){
            // 获取文件流
            FileInputStream fis = new FileInputStream(partFile);

            // 跳到每个分块的开头
            long skipBytes = partSize * i;
            fis.skip(skipBytes);

            // 计算每个分块的大小
            long size = partSize < partFile.length() - skipBytes ?
                    partSize : partFile.length() - skipBytes;

            // 创建UploadPartRequest，上传分块
            UploadPartRequest uploadPartRequest = new UploadPartRequest();
            uploadPartRequest.setBucketName(bucketName);
            uploadPartRequest.setKey(objectKey);
            uploadPartRequest.setUploadId(initiateMultipartUploadResponse.getUploadId());
            uploadPartRequest.setInputStream(fis);
            uploadPartRequest.setPartSize(size);
            uploadPartRequest.setPartNumber(i + 1);
            UploadPartResponse uploadPartResponse = client.uploadPart(uploadPartRequest);


            // 将返回的PartETag保存到List中。
            partETags.add(uploadPartResponse.getPartETag());

            // 关闭文件
            fis.close();
        }
    }

    //完成分块上传 - BOS收到用户提交的Part列表后，会逐一验证每个数据Part的有效性。当所有的数据Part验证通过后，BOS将把这些数据part组合成一个完整的Object
    public void finishMultiUpload(String bucketName, String objectKey,BosClient client,InitiateMultipartUploadResponse initiateMultipartUploadResponse,List<PartETag> partETags){
        CompleteMultipartUploadRequest completeMultipartUploadRequest =
                new CompleteMultipartUploadRequest(bucketName, objectKey, initiateMultipartUploadResponse.getUploadId(), partETags);

        CompleteMultipartUploadResponse completeMultipartUploadResponse =
                client.completeMultipartUpload(completeMultipartUploadRequest);

        // 打印Object的ETag
        System.out.println(completeMultipartUploadResponse.getETag());
    }

    //取消分块上传事件
    public void abortMultipartUploadByUploadId(String bucketName, String objectKey,BosClient client, String uploadId){
        AbortMultipartUploadRequest abortMultipartUploadRequest =
                new AbortMultipartUploadRequest(bucketName, objectKey, uploadId);

        // 取消分块上传
        client.abortMultipartUpload(abortMultipartUploadRequest);
    }

    //获取未完成的分块上传事件
    //1.如果Bucket中的分块上传事件的数目大于1000，则只会返回1000个Object，并且返回结果中IsTruncated的值为True，同时返回NextKeyMarker作为下次读取的起点
    //2.若想返回更多分块上传事件的数目，可以使用KeyMarker参数分次读取
    public void getUnCompleteMultipartUploadPart(String bucketName,BosClient client){
        ListMultipartUploadsRequest listMultipartUploadsRequest =
                new ListMultipartUploadsRequest(bucketName);

        // 获取Bucket内所有上传事件
        ListMultipartUploadsResponse listing = client.listMultipartUploads(listMultipartUploadsRequest);

        // 遍历所有上传事件
        for (MultipartUploadSummary multipartUpload : listing.getMultipartUploads()) {
            System.out.println("Key: " + multipartUpload.getKey() + " UploadId: " + multipartUpload.getUploadId());
        }
    }

    //获取所有已上传的块信息
    public void getCompleteMultipartUploadPart(String bucketName,String objectKey,String uploadId,BosClient client){
        ListPartsRequest listPartsRequest = new ListPartsRequest(bucketName, objectKey, uploadId);

        // 获取上传的所有Part信息
        ListPartsResponse partListing = client.listParts(listPartsRequest);

        // 遍历所有Part
        for (PartSummary part : partListing.getParts()) {
            System.out.println("PartNumber: " + part.getPartNumber() + " ETag: " + part.getETag());
        }

    }
    //查看Object的存储类型storage class
    public void listPartsStorageClass(BosClient client,String bucketName,String objectKey,String uploadId){
        ListPartsResponse listPartsResponse = client.listParts(bucketName, objectKey, uploadId);
        String storageClass = listPartsResponse.getStorageClass();
    }

    //4.断点续传

    /**上传文件end**/

    /**下载文件start**/
    //1.简单流式下载
    public static void getObject(BosClient client, String bucketName, String objectKey)
            throws IOException {

        // 获取Object，返回结果为BosObject对象
        BosObject object = client.getObject(bucketName, objectKey);

        // 获取ObjectMeta
        ObjectMetadata meta = object.getObjectMetadata();

        // 获取Object的输入流
        InputStream objectContent = object.getObjectContent();
        System.out.println(objectContent);
        // 处理Object
        //...

        // 关闭流
        objectContent.close();
    }

    //2.直接下载Object到文件
    public static void getFile(String bucketName,String objectKey,BosClient client){
        // 新建GetObjectRequest
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, objectKey);

        // 下载Object到文件
        ObjectMetadata objectMetadata = client.getObject(getObjectRequest, new File("/Users/juin/Desktop"));
        System.out.println(objectMetadata.getETag());
    }

    //3.范围下载 - 用户也可以用此功能实现文件的分段下载和断点续传
    public void getByRange(String bucketName,String objectKey,BosClient client){
        // 新建GetObjectRequest
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, objectKey);

        // 获取0~100字节范围内的数据
        getObjectRequest.setRange(0, 100);

        // 获取Object，返回结果为BosObject对象
        BosObject object = client.getObject(getObjectRequest);
    }

    //变更文件存储等级
    //。。。


    /**
     *
     * @param client
     * @param bucketName
     * @param objectKey
     * @param expirationInSeconds - 有效时间
     * @return 获取文件下载URL
     */
    //如果预期获取的文件时公共可读的，则对应URL链接可通过简单规则快速拼接获取: http://bucketName.$region.bcebos.com/$bucket/$object
    public static  String generatePresignedUrl(BosClient client, String bucketName, String objectKey, int expirationInSeconds) {

        URL url = client.generatePresignedUrl(bucketName, objectKey, expirationInSeconds);
        //指定用户需要获取的Object所在的Bucket名称、该Object名称、时间戳、URL的有效时长
        return url.toString();
    }

    /**下载文件end**/

    /**列举存储空间中的文件start**/
    //1.简单列举
    public static void listObjects(BosClient client, String bucketName) {

        // 获取指定Bucket下的所有Object信息
        ListObjectsResponse listing = client.listObjects(bucketName);

        // 遍历所有Object
        for (BosObjectSummary objectSummary : listing.getContents()) {
            System.out.println("ObjectKey: " + objectSummary.getKey());
        }

    }

    //2.通过参数复杂列举
    //Prefix;Delimiter;Marker;MaxKeys
    public void listObjectsByPrefix(BosClient client, String bucketName){
        // 指定返回前缀为test的object
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
        listObjectsRequest.withPrefix("test");
        ListObjectsResponse listObjectsResponse = client.listObjects(listObjectsRequest);
        for(BosObjectSummary objectSummary :listObjectsResponse.getContents()) {
            System.out.println("ObjectKey:" + objectSummary.getKey());
        }
    }
    /**列举存储空间中的文件end**/

    //模拟文件夹功能

    //Object权限控制

    //删除文件
    //1.删除单个文件
    public void deleteObject(BosClient client, String bucketName, String objectKey) {

        // 删除Object
        client.deleteObject(bucketName, objectKey);           //指定要删除的Object所在Bucket名称和该Object名称
    }
    //2.删除多个文件
    public void multiDelObjects(BosClient client){
        //1.通过json
        String jsonObjectKeys = "{\"objects\": ["+"{\"key\": \"token1.h\"},"+"{\"key\": \"token2.h\"}"+"]}";
        DeleteMultipleObjectsRequest request = new DeleteMultipleObjectsRequest();
        request.setBucketName("yourBucketName");
        request.setJsonDeleteObjects(jsonObjectKeys);
        client.deleteMultipleObjects(request);
        //2.用户只需指定指定参数即可
        List<String> objectKeys = new ArrayList<String>();
        objectKeys.add("object1");
        objectKeys.add("object2");
        request = new DeleteMultipleObjectsRequest();
        request.setBucketName("yourBucketName");
        request.setObjectKeys(objectKeys);
        DeleteMultipleObjectsResponse response = client.deleteMultipleObjects(request);

        //查看文件是否存在
        boolean exists = client.doesObjectExist("bucketName", "objectKey");
    }
}
