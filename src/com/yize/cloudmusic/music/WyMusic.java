package com.yize.cloudmusic.music;

import com.yize.cloudmusic.model.MusicHelper;
import com.yize.cloudmusic.model.SongInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class WyMusic implements MusicHelper {
    @Override
    public List<SongInfo> searchMusic(String keyword, int num) {
        return null;
    }

    @Override
    public SongInfo getDownloadLink(SongInfo songInfo) {
        return null;
    }

    @Override
    public SongInfo getDownloadLink(SongInfo songInfo, String quality) {
        return null;
    }

    @Override
    public String downloadWebSite(String website) {
        try {
            URL url=new URL(website);
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(4000);
            conn.setReadTimeout(15000);
            conn.setRequestProperty("User-Agent","Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Mobile Safari/537.36");
            BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
            StringBuilder sb=new StringBuilder();
            String line;
            while((line=reader.readLine())!=null){
                sb.append(line);
            }
            reader.close();
            conn.disconnect();
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String replaceFieldName(String info, String field, String newField) {
        return info.replaceAll("\""+field+"\"","\""+newField+"\"");
    }
}
