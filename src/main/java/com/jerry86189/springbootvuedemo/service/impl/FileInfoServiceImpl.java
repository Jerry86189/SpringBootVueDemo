package com.jerry86189.springbootvuedemo.service.impl;/**
 * @project SpringBootVueDemo
 * @description TODO
 * @author Jerry
 * @date 2023/5/30 14:52
 * @version 1.0
 */

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jerry86189.springbootvuedemo.config.FileStorageProperties;
import com.jerry86189.springbootvuedemo.entity.FileInfo;
import com.jerry86189.springbootvuedemo.exceptions.*;
import com.jerry86189.springbootvuedemo.mapper.FileInfoMapper;
import com.jerry86189.springbootvuedemo.service.FileInfoService;
import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.net.MalformedURLException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Jerry
 * @version 1.0
 * @description: TODO
 * @date 2023/5/30 14:52
 */
@Service
public class FileInfoServiceImpl implements FileInfoService {

    @Autowired
    private FileInfoMapper fileInfoMapper;

    @Autowired
    private FileStorageProperties fileStorageProperties;

    /**
     * 上传文件
     *
     * 上传一个新的文件，包括文件信息和文件数据
     *
     * @param fileInfo 文件的相关信息
     * @param file 文件数据
     */
    @Override
    public void uploadFile(FileInfo fileInfo, MultipartFile file) {
        // 检查文件名是否合法
        String fileName = fileInfo.getFileName();
        if (!isValidFileName(fileName)) {
            throw new InvalidFileNameException(fileName, "File format is not supported. Only .csv files are allowed.");
        }

        QueryWrapper<FileInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_name", fileInfo.getFileName());
        if (fileInfoMapper.selectOne(queryWrapper) != null) {
            throw new FileNameDuplicateException("File name already exists: " + fileInfo.getFileName());
        }

        // 定义文件上传后保存的路径
        String filePath = fileStorageProperties.getUploadDir() + "/" + fileName;
        File destFile = new File(filePath);

        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }

        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            throw new UploadFailedException("File upload failed: " + e.getMessage(), e);
        }

        // 设置上传完成之后的信息
        fileInfo.setFilePath(filePath);
        fileInfo.setUploadTimestamp(Timestamp.valueOf(LocalDateTime.now()));

        int insert = fileInfoMapper.insert(fileInfo);
        System.out.println(insert);
        if (insert <= 0) {
            throw new RegisterFailedException("Your file" + fileInfo.getFileName() + "register failed");
        }
    }

    /**
     * 下载文件
     *
     * 下载指定ID的文件
     *
     * @param fileId 要下载的文件的ID
     * @return 一个可以用来读取文件数据的资源对象
     */
    @Override
    public Resource downloadFile(Long fileId) {
        FileInfo fileInfo = fileInfoMapper.selectById(fileId);

        if (fileInfo == null) {
            throw new NotFoundException("Your account: " + fileId + " not found");
        }

        try {
            Path filePath = Paths.get(fileInfo.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new DownloadFailedException("Could not read file: " + fileInfo.getFileName());
            }
        } catch (MalformedURLException e) {
            throw new DownloadFailedException("Error occurred while downloading file: " + fileInfo.getFileName(), e);
        }
    }

    /**
     * 分页查看所有文件（按照id升序查询）
     *
     * 按照文件的ID升序排序并分页返回所有文件的信息
     *
     * @param pageNum 页数
     * @param pageSize 每页数量
     * @return 分页文件信息列表
     */
    @Override
    public List<FileInfo> getAllFilesByIdAsc(int pageNum, int pageSize) {
        Page<FileInfo> page = new Page<>(pageNum, pageSize);
        QueryWrapper<FileInfo> queryWrapper = new QueryWrapper<>();
        IPage<FileInfo> fileInfoPage = fileInfoMapper.selectPage(page, queryWrapper);

        if (fileInfoPage == null || fileInfoPage.getRecords() == null) {
            throw new PageQueryFailedException("Query page: " + pageNum + " failed");
        }

        return fileInfoPage.getRecords();
    }

    /**
     * 分页查看所有文件（按照fileName的字母的abcd字母顺序）
     *
     * 按照文件名的字母顺序排序并分页返回所有文件的信息
     *
     * @param pageNum 页数
     * @param pageSize 每页数量
     * @return 分页文件信息列表
     */
    @Override
    public List<FileInfo> getAllFilesByNameAsc(int pageNum, int pageSize) {
        Page<FileInfo> page = new Page<>(pageNum, pageSize);
        QueryWrapper<FileInfo> queryWrapper = new QueryWrapper<>();

        // 按照 fileName 列进行升序排序
        queryWrapper.orderByAsc("file_name");

        IPage<FileInfo> fileInfoPage = fileInfoMapper.selectPage(page, queryWrapper);

        if (fileInfoPage == null || fileInfoPage.getRecords() == null) {
            throw new PageQueryFailedException("Query page: " + pageNum + " failed");
        }

        return fileInfoPage.getRecords();
    }

    /**
     * 删除文件
     *
     * 删除指定ID的文件
     *
     * @param fileId 要删除的文件的ID
     */
    @Override
    public void deleteFile(Long fileId) {
        FileInfo fileInfo = fileInfoMapper.selectById(fileId);

        if (fileInfo == null) {
            throw new NotFoundException("Your account: " + fileId + " not found");
        }

        Path filePath = Paths.get(fileInfo.getFilePath());
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new DeleteFailedException("Failed to delete file: " + fileInfo.getFileName());
        }

        int delete = fileInfoMapper.deleteById(fileId);
        if (delete <= 0) {
            throw new DeleteFailedException("File: " + fileId + " deletion failed");
        }
    }

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
    @Override
    public List<FileInfo> getFilesByName(String fileName, int pageNum, int pageSize) {
        Page<FileInfo> page = new Page<>();
        QueryWrapper<FileInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("file_name", fileName);
        IPage<FileInfo> fileInfoPage = fileInfoMapper.selectPage(page, queryWrapper);

        if (fileInfoPage == null || fileInfoPage.getRecords() == null) {
            throw new PageQueryFailedException("Query page: " + pageNum + " failed");
        }

        if (fileInfoPage.getRecords().isEmpty()) {
            throw new NotFoundException("No users found with username: " + fileName);
        }

        return fileInfoPage.getRecords();
    }

    /**
     * 通过id精确查询文件信息
     *
     * 根据提供的文件ID精确查询文件的信息
     *
     * @param fileId 要查询的文件的ID
     * @return 文件信息对象
     */
    @Override
    public FileInfo getFileById(Long fileId) {
        FileInfo fileInfo = fileInfoMapper.selectById(fileId);

        if (fileInfo == null) {
            throw new NotFoundException("Your account: " + fileId + " not found");
        }

        return fileInfo;
    }

    private boolean isValidFileName(@NotNull String fileName) {
        //在这里，我们假设一个合法的文件名只能包含字母、数字、下划线，并且必须是.csv文件
        return fileName.matches("\\w+\\.csv");
    }
}
