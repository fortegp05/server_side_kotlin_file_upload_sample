package com.example.file_upload_sample.app.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

@Controller
class FileUploadController {

    @GetMapping("/fileUpload")
    fun getSaveFilePage(): String {
        return "fileUpload"
    }

    @PostMapping("/fileUpload")
    fun saveFile(
            @RequestParam("file") file: MultipartFile,
            redirectAttributes: RedirectAttributes
    ): String {
        if (file.isEmpty) {
            redirectAttributes.addFlashAttribute("message", "file is empty")
            return "redirect:/fileUpload"
        }

        val saveFile = File(file.originalFilename ?: SimpleDateFormat("yyyyMMddHHmmss").format(Date()))
        val bytes = file.bytes
        var bos: BufferedOutputStream? = null

        val result = kotlin.runCatching {
            bos = BufferedOutputStream(FileOutputStream(saveFile))
            bos?.write(bytes)
        }

        bos?.close()

        if (result.isSuccess) {
            redirectAttributes.addFlashAttribute("message", "save success!")
        } else {
            redirectAttributes.addFlashAttribute("message", "error! exception=" + result.exceptionOrNull()?.toString())
        }

        return "redirect:/fileUpload"
    }
}