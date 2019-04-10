package com.Reap.ReapProject.controller;

import com.Reap.ReapProject.entity.Recognition;
import com.Reap.ReapProject.entity.User;
import com.Reap.ReapProject.service.RecognitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
public class CsvController {
    @Autowired
    RecognitionService recognitionService;

    @GetMapping("/downloadCsv")
    public void downloadCsv(HttpServletRequest request,HttpServletResponse response) throws IOException {
        HttpSession session=request.getSession();
        User user=(User)session.getAttribute("loginUser");

        if(user==null){
            throw new RuntimeException("unauthorized Access");
        }

        String csvFileName="recognitions.csv";
        response.setContentType("text/csv");

        List<Recognition> recognitions=recognitionService.getListOfRecognitions();

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.EXCEL_PREFERENCE);

        String[] header = { "id", "badge", "comment", "date",
                "reason", "receiverId", "receiverName","senderId","senderName","goldRedeemable","silverRedeemable","bronzeRedeemable" };

        csvWriter.writeHeader(header);
        for (Recognition recognition:recognitions){
            csvWriter.write(recognition,header);
        }
        csvWriter.close();

    }
}
