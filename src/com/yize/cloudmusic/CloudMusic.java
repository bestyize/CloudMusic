package com.yize.cloudmusic;

import com.yize.cloudmusic.model.SongInfo;
import com.yize.cloudmusic.music.KwMusic;
import com.yize.cloudmusic.music.MgMusic;

import java.util.ArrayList;
import java.util.List;

public class CloudMusic {
    private static List<SongInfo> allSongInfoList=new ArrayList<>();
    public static KwMusic kwMusic=new KwMusic();
    private static MgMusic mgMusic=new MgMusic();
    public static void main(String[] args) {
        long startTime=System.currentTimeMillis();
        System.out.println("开始搜索...");
        allSongInfoList=searchMusic("陈奕迅",10);
        long searchTime=System.currentTimeMillis();
        System.out.println("搜索完毕，用时："+(searchTime-startTime)+"ms");
        System.out.println("开始搜索下载链接...");
        allSongInfoList=searchAllDownloadLink(allSongInfoList);
        System.out.println("搜索完毕，用时："+(System.currentTimeMillis()-searchTime)+"ms");
        System.out.println(allSongInfoList);

    }

    public static List<SongInfo> searchMusic(String keyword,int num){
        int other=num%2;
        List<SongInfo> songInfoList1= new KwMusic().searchMusic(keyword,num/2);
        List<SongInfo> songInfoList2= new MgMusic().searchMusic(keyword,num/2+other);
        List<SongInfo> songInfoList=new ArrayList<>();
        songInfoList.addAll(songInfoList2);
        songInfoList.addAll(songInfoList1);
        return songInfoList;
    }

    public static List<SongInfo> searchAllDownloadLink(List<SongInfo> songInfoList){
        for(SongInfo songInfo:songInfoList){
            if(songInfo.getSongid().length()<9){
                songInfo=new KwMusic().getDownloadLink(songInfo);
            }else if(songInfo.getSongid().length()>10){
                songInfo=new MgMusic().getDownloadLink(songInfo);
            }
        }
        return songInfoList;
    }
}
