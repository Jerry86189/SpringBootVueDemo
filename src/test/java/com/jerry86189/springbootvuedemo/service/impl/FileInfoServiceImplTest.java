package com.jerry86189.springbootvuedemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jerry86189.springbootvuedemo.config.FileStorageProperties;
import com.jerry86189.springbootvuedemo.entity.FileInfo;
import com.jerry86189.springbootvuedemo.exceptions.*;
import com.jerry86189.springbootvuedemo.mapper.FileInfoMapper;
import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;



/**
 * @author Jerry
 * @version 1.0
 * @project SpringBootVueDemo
 * @description TODO
 * @date 2023/5/30 20:31
 */
public class FileInfoServiceImplTest {
    @InjectMocks
    private FileInfoServiceImpl fileInfoService;

    @Mock
    private FileInfoMapper fileInfoMapper;

    @Mock
    private FileStorageProperties fileStorageProperties;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(fileStorageProperties.getUploadDir()).thenReturn("/tmp/");
    }

    @Test
    public void testUploadFile() throws IOException {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileName("test.csv");
        fileInfo.setFileSize(4L);

        MultipartFile multipartFile = new MockMultipartFile("test", "test.csv", "text/csv", "data".getBytes());

        // 设定当调用 fileInfoMapper.insert(fileInfo) 时，返回1
        when(fileInfoMapper.insert(any(FileInfo.class))).thenReturn(1);

        fileInfoService.uploadFile(fileInfo, multipartFile);
        verify(fileInfoMapper).insert(any(FileInfo.class));

        // 可以进一步检查fileInfo对象的状态是否正确，例如：
         assertEquals("test.csv", fileInfo.getFileName());
         assertNotNull(fileInfo.getUploadTimestamp());
         assertEquals(multipartFile.getSize(), fileInfo.getFileSize());
         assertTrue(fileInfo.getFilePath().endsWith("test.csv"));
    }

    @Test
    public void testUploadFileInvalidName() {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileName("test.txt");

        MultipartFile multipartFile = mock(MultipartFile.class);

        Assertions.assertThrows(InvalidFileNameException.class, () -> fileInfoService.uploadFile(fileInfo, multipartFile));
    }

    @Test
    public void testUploadFileDuplicateName() {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileName("test.csv");

        MultipartFile multipartFile = mock(MultipartFile.class);

        when(fileInfoMapper.selectOne(any(QueryWrapper.class))).thenReturn(fileInfo);

        Assertions.assertThrows(FileNameDuplicateException.class, () -> fileInfoService.uploadFile(fileInfo, multipartFile));
    }

    @Test
    public void testDownloadFile() {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileName("test.csv");
        fileInfo.setFilePath("/tmp/test.csv");

        when(fileInfoMapper.selectById(anyLong())).thenReturn(fileInfo);

        Resource resource = fileInfoService.downloadFile(1L);
        assertTrue(resource.exists());
    }

    @Test
    public void testDownloadFileNotFound() {
        when(fileInfoMapper.selectById(anyLong())).thenReturn(null);

        Assertions.assertThrows(NotFoundException.class, () -> fileInfoService.downloadFile(1L));
    }

    @Test
    public void testGetAllFilesByIdAsc() {
        IPage<FileInfo> fileInfoPage = mock(IPage.class);
        List<FileInfo> records = new ArrayList<>();
        records.add(new FileInfo());
        when(fileInfoPage.getRecords()).thenReturn(records);

        when(fileInfoMapper.selectPage(any(), any())).thenReturn(fileInfoPage);

        List<FileInfo> result = fileInfoService.getAllFilesByIdAsc(1, 10);

        assertEquals(1, result.size());
    }

    @Test
    public void testGetAllFilesByNameAsc() {
        IPage<FileInfo> fileInfoPage = mock(IPage.class);
        List<FileInfo> records = new ArrayList<>();
        records.add(new FileInfo());
        when(fileInfoPage.getRecords()).thenReturn(records);

        when(fileInfoMapper.selectPage(any(), any())).thenReturn(fileInfoPage);

        List<FileInfo> result = fileInfoService.getAllFilesByNameAsc(1, 10);

        assertEquals(1, result.size());
    }

    @Test
    public void testDeleteFile() {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileName("test.csv");
        fileInfo.setFilePath(Paths.get("/tmp/test.csv").toString());

        when(fileInfoMapper.selectById(anyLong())).thenReturn(fileInfo);
        when(fileInfoMapper.deleteById(anyLong())).thenReturn(1);  // 模拟 deleteById 方法返回1

        fileInfoService.deleteFile(1L);

        verify(fileInfoMapper).deleteById(eq(1L));
    }

    @Test
    public void testDeleteFileNotFound() {
        when(fileInfoMapper.selectById(anyLong())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> fileInfoService.deleteFile(1L));
    }

    @Test
    public void testGetFilesByName() {
        IPage<FileInfo> fileInfoPage = mock(IPage.class);
        List<FileInfo> records = new ArrayList<>();
        records.add(new FileInfo());
        when(fileInfoPage.getRecords()).thenReturn(records);

        when(fileInfoMapper.selectPage(any(), any())).thenReturn(fileInfoPage);

        List<FileInfo> result = fileInfoService.getFilesByName("test", 1, 10);

        assertEquals(1, result.size());
    }

    @Test
    public void testGetFileById() {
        FileInfo fileInfo = new FileInfo();

        when(fileInfoMapper.selectById(anyLong())).thenReturn(fileInfo);

        FileInfo result = fileInfoService.getFileById(1L);

        assertEquals(fileInfo, result);
    }

    @Test
    public void testGetFileByIdNotFound() {
        when(fileInfoMapper.selectById(anyLong())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> fileInfoService.getFileById(1L));
    }
}