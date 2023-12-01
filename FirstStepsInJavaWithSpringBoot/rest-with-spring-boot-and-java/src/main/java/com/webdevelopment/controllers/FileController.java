package com.webdevelopment.controllers;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.webdevelopment.data.vo.v1.UploadFileResponseVO;
import com.webdevelopment.services.FileStorageServices;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "File Endpoint")
@RestController
@RequestMapping("api/file/v1")
public class FileController {

	private Logger logger = Logger.getLogger(FileController.class.getName());
	private FileStorageServices service;

	@Autowired
	public FileController(FileStorageServices fileStorageServices) {
		this.service = fileStorageServices;
	}

	@PostMapping("/uploadFile")
	public UploadFileResponseVO uploadFile(@RequestParam("file") MultipartFile file) {
		logger.info("Storing file to disk");

		var filename = this.service.storeFile(file);
		String fileDownloadUri = ServletUriComponentsBuilder
				.fromCurrentContextPath()
				.path("api/file/v1/downloadFile/")
				.path(filename).toUriString();

		return new UploadFileResponseVO(filename, fileDownloadUri, file.getContentType(), file.getSize());
	}

}
