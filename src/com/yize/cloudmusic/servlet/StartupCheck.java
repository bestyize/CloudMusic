package com.yize.cloudmusic.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.PreparedStatement;

@WebServlet("/api/check")
public class StartupCheck extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/json");
        PrintWriter writer=response.getWriter();
        ServletContext context=request.getServletContext();
        String path=context.getRealPath("/WEB-INF/config/config.json");
        File file=new File(path);

        BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));
        String line;
        StringBuilder sb=new StringBuilder();
        while ((line=reader.readLine())!=null){
            sb.append(line);
        }
        writer.write(sb.toString());
        writer.flush();
        writer.close();

    }
}
