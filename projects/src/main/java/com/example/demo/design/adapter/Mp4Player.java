package com.example.demo.design.adapter;

/**
 * @author kejun
 * @date 2019/2/11 下午2:56
 */
public class Mp4Player implements AdvancedMediaPlayer{
    @Override
    public void playVlc(String fileName) {

    }

    @Override
    public void playMp4(String fileName) {
        System.out.println("play mp4 file: " + fileName);
    }
}
