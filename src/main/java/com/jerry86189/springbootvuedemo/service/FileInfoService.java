package com.jerry86189.springbootvuedemo.service;

import com.jerry86189.springbootvuedemo.entity.FileInfo;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Jerry
 * @version 1.0
 * @project SpringBootVueDemo
 * @description 文件信息服务接口，这个接口定义了关于文件信息的一系列服务，包括文件的上传，下载，删除，查询等操作
 * @date 2023/5/30 14:44
 */
public interface FileInfoService {
    /**
     * 上传文件
     *
     * 上传一个新的文件，包括文件信息和文件数据
     *
     * @param fileInfo 文件的相关信息
     * @param file 文件数据
     */
    void uploadFile(FileInfo fileInfo, MultipartFile file);

    /**
     * 下载文件
     *
     * 下载指定ID的文件
     *
     * @param fileId 要下载的文件的ID
     * @return 一个可以用来读取文件数据的资源对象
     */
    Resource downloadFile(Long fileId);

    /**
     * 分页查看所有文件（按照id升序查询）
     *
     * 按照文件的ID升序排序并分页返回所有文件的信息
     *
     * @param pageNum 页数
     * @param pageSize 每页数量
     * @return 分页文件信息列表
     */
    List<FileInfo> getAllFilesByIdAsc(int pageNum, int pageSize);

    /**
     * 分页查看所有文件（按照fileName的字母的abcd字母顺序）
     *
     * 按照文件名的字母顺序排序并分页返回所有文件的信息
     *
     * @param pageNum 页数
     * @param pageSize 每页数量
     * @return 分页文件信息列表
     */
    List<FileInfo> getAllFilesByNameAsc(int pageNum, int pageSize);

    /**
     * 删除文件
     *
     * 删除指定ID的文件
     *
     * @param fileId 要删除的文件的ID
     */
    void deleteFile(Long fileId);

    /**
     * 通过文件名称模糊查询
     *
     * 根据提供的文件名（或部分文件名）进行模糊查询，并分页返回结果
     *
     * @param fileName 文件名称（或部分名称）
     * @param pageNum 页数
     * @param pageSize 每页数量
     * @return 分页文件信息列表
     */
    List<FileInfo> getFilesByName(String fileName, int pageNum, int pageSize);

    /**
     * 通过id精确查询文件信息
     *
     * 根据提供的文件ID精确查询文件的信息
     *
     * @param fileId 要查询的文件的ID
     * @return 文件信息对象
     */
    FileInfo getFileById(Long fileId);
}
