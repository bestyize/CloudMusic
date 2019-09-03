package com.yize.cloudmusic.servlet;

import com.yize.cloudmusic.model.SongInfo;
import com.yize.cloudmusic.music.KwMusic;
import com.yize.cloudmusic.music.MgMusic;
import com.yize.cloudmusic.music.XmMusic;
import org.junit.Test;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/download")
public class SearchDownloadLink extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/json");
        PrintWriter writer=response.getWriter();
        String ip=request.getRemoteAddr();
        System.out.println(ip);
        String songid=request.getParameter("songid");
        String accessKey=request.getParameter("accessKey");
        String quality=request.getParameter("quality");
        String source=request.getParameter("source");
        if(source==null){
            source="";
        }
        if(songid==null||songid.length()<3){
            writer.write("error songid");
            writer.flush();
            writer.close();
            return;
        }
        if(accessKey==null||accessKey.length()<10){
            writer.write("error accessKey");
            writer.flush();
            writer.close();
            return;
        }

        if(quality==null||quality.length()!=2){
            writer.write("error quality");
            writer.flush();
            writer.close();
            return;
        }

        if(!identifyAccessKey(accessKey,songid,300)){
            writer.write("accessKey error");
            writer.flush();
            writer.close();
            return;
        }
        String downloadLink="";
        try{
            downloadLink=getDonwloadLinkById(songid,quality,source);
        }catch (Exception e){
            writer.write("request error");
            writer.flush();
            writer.close();
            return;
        }

        writer.write(downloadLink);
        writer.flush();
        writer.close();

    }
    @Test
    public void test(){
        String songid="60054704037";
        String quality="HQ";

        String accessKey=createAccessKey(songid);
        System.out.println(accessKey);
        boolean is=identifyAccessKey(accessKey,songid,60);
        System.out.println(is);
    }


    public static boolean identifyAccessKey(String accessKey,String songid,double timeout){
        BASE64Decoder decoder=new BASE64Decoder();
        try {
            accessKey=accessKey.replaceAll("_","=");
            String decrypt=new String(decoder.decodeBuffer(accessKey));
            decrypt=decrypt.replaceAll(songid.substring(0,4),"=");
            String songidDecrypt=decrypt.substring(0,decrypt.indexOf("@"));
            songidDecrypt=new String(decoder.decodeBuffer(songidDecrypt));
            String timeDecrypt=decrypt.substring(decrypt.indexOf("@")+1);
            timeDecrypt=new String(decoder.decodeBuffer(timeDecrypt));
            long requestTime=Long.valueOf(timeDecrypt);
            if(System.currentTimeMillis()-requestTime>1000*timeout){
                return false;
            }
            if(songidDecrypt.equals(songid)){
                return true;
            }

        }catch (Exception e){

        }
        return false;
    }

    public static String createAccessKey(String songid){
        BASE64Encoder encoder=new BASE64Encoder();
        String encParams1=encoder.encode(songid.getBytes());
        String encParams2=encoder.encode(String.valueOf(System.currentTimeMillis()).getBytes());
        String encStr=encParams1+"@"+encParams2;
        encStr=encStr.replaceAll("=",songid.substring(0,4));
        encStr=encoder.encode(encStr.getBytes()).replaceAll("=","_");
        return encStr;
    }


    public static List<SongInfo> searchAllDownloadLink(List<SongInfo> songInfoList){
        for(SongInfo songInfo:songInfoList){
            if(songInfo.getSongid().length()<=9){
                songInfo=new KwMusic().getDownloadLink(songInfo);
            }else if(songInfo.getSongid().length()>10){
                songInfo=new MgMusic().getDownloadLink(songInfo);
            }
        }
        return songInfoList;
    }

    public static String getDonwloadLinkById(String songid,String quality,String source){
        SongInfo songInfo=new SongInfo();
        songInfo.setSongid(songid);
        if(source.equals("Xm")){
            songInfo=new XmMusic().getDownloadLink(songInfo);
        }else if(source.equals("Kw")){
            songInfo=new KwMusic().getDownloadLink(songInfo);

        }else if(source.equals("Mg")){
            songInfo=new MgMusic().getDownloadLink(songInfo);
        }else {
            return "";
        }
        String downloadLink=songInfo.getSqDownloadLink();
        if(quality.equals("SQ")){
            if(downloadLink==null&&downloadLink.length()<10){
                downloadLink=songInfo.getHqDownloadLink();
                if(downloadLink==null&&downloadLink.length()<10){
                    downloadLink=songInfo.getPqDownloadLink();
                    if(downloadLink==null&&downloadLink.length()<10){
                        downloadLink=songInfo.getLqDownloadLink();
                        if(downloadLink==null&&downloadLink.length()<10){
                            return null;
                        }
                    }
                }
            }
        }else if(quality.equals("HQ")||quality.equals("PQ")||quality.equals("LQ")){
            downloadLink=songInfo.getHqDownloadLink();
            if(downloadLink==null&&downloadLink.length()<10){
                downloadLink=songInfo.getPqDownloadLink();
                if(downloadLink==null&&downloadLink.length()<10){
                    downloadLink=songInfo.getLqDownloadLink();
                    if(downloadLink==null&&downloadLink.length()<10){
                        return null;
                    }

                }
            }
        }
        return downloadLink;

    }


}
