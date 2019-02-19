package com.example.demo.design.adapter;

/**
 * @author kejun
 * @date 2019/2/11 下午2:55
 */
public class VlcPlayer implements AdvancedMediaPlayer {
    @Override
    public void playVlc(String fileName) {
        System.out.println("play vlc filename: "+fileName);
    }

    @Override
    public void playMp4(String fileName) {

    }
}
