package com.yize.cloudmusic.music;

import com.yize.cloudmusic.model.MusicHelper;
import com.yize.cloudmusic.model.SongInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class KgMusic implements MusicHelper {
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
            URL url = new URL(website);
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(4000);
            conn.setReadTimeout(4000);
            conn.setRequestProperty("User-Agent","Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1");
            conn.setRequestProperty("Host","wwwapi.kugou.com");
            conn.setRequestProperty("Upgrade-Insecure-Requests","1");
            conn.setRequestProperty("Cookie","kg_mid=bda31084a2849988b5d2490c8fdcfbf2; kg_dfid=3dKwVB1F0BjL0IQFVj4Um4dx; kg_dfid_collect=d41d8cd98f00b204e9800998ecf8427e; Hm_lvt_aedee6983d4cfc62f509129360d6bb3d=1566633410,1566648853; Hm_lpvt_aedee6983d4cfc62f509129360d6bb3d=1566649236");
            BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
            String line;
            StringBuilder sb=new StringBuilder();
            while((line=reader.readLine())!=null){
                sb.append(line);
            }
            reader.close();
            conn.disconnect();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String replaceFieldName(String info, String field, String newField) {
        return info.replaceAll("\""+field+"\"","\""+newField+"\"");
    }
}
