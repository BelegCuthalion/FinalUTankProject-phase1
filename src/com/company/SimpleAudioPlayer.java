package com.company;

// Java program to play an Audio
// file using Clip Object
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.sound.sampled.*;

public class SimpleAudioPlayer
{

    // to store current position
    Long currentFrame;
    Clip clip;

    // current status of clip
    String status;

    AudioInputStream audioInputStream;
    String filePath;

    // constructor to initialize streams and clip
    public SimpleAudioPlayer(String filePath)
            throws UnsupportedAudioFileException,
            IOException, LineUnavailableException
    {
        this.filePath = filePath;
        // create AudioInputStream object
        audioInputStream =
                AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());


        AudioFormat format = audioInputStream.getFormat();
        DataLine.Info info = new DataLine.Info(Clip.class, format);
        clip = (Clip) AudioSystem.getLine(info);
        // create clip reference

        // to close the clip when it's not running
        clip.addLineListener(myLineEvent -> {
            if (myLineEvent.getType() == LineEvent.Type.STOP)
                clip.close();
        });

        // open audioInputStream to the clip
        clip.open(audioInputStream);

//        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public static void main(String[] args) {

        SimpleAudioPlayer laser = null;
        try {
            laser = new SimpleAudioPlayer("laser.wav");
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        laser.play();

//
////            Scanner sc = new Scanner(System.in);
//
////            while (true)
////            {
////                System.out.println("1. pause");
////                System.out.println("2. resume");
////                System.out.println("3. restart");
////                System.out.println("4. stop");
////                System.out.println("5. Jump to specific time");
////                int c = sc.nextInt();
////                audioPlayer.gotoChoice(3);
////                if (c == 4)
////                    break;
////            }
////            sc.close();
//
    }

    // Work as the user enters his choice

    private void gotoChoice(int c)
            throws IOException, LineUnavailableException, UnsupportedAudioFileException
    {
        switch (c)
        {
            case 1:
                pause();
                break;
            case 2:
                resumeAudio();
                break;
            case 3:
                restart();
                break;
            case 4:
                stop();
                break;
            case 5:
                System.out.println("Enter time (" + 0 +
                        ", " + clip.getMicrosecondLength() + ")");
                Scanner sc = new Scanner(System.in);
                long c1 = sc.nextLong();
                jump(c1);
                break;

        }

    }

    // Method to play the audio
    public void play()
    {
        //start the clip
        clip.start();

        status = "play";
    }

    // Method to pause the audio
    public void pause()
    {
        if (status.equals("paused"))
        {
            System.out.println("audio is already paused");
            return;
        }
        this.currentFrame =
                this.clip.getMicrosecondPosition();
        clip.stop();
        status = "paused";
    }

    // Method to resume the audio
    public void resumeAudio() throws UnsupportedAudioFileException,
            IOException, LineUnavailableException
    {
        if (status.equals("play"))
        {
            System.out.println("Audio is already "+
                    "being played");
            return;
        }
        clip.close();
        resetAudioStream();
        clip.setMicrosecondPosition(currentFrame);
        this.play();
    }

    // Method to restart the audio
    public void restart() throws IOException, LineUnavailableException,
            UnsupportedAudioFileException
    {
        clip.stop();
        clip.close();
        resetAudioStream();
        currentFrame = 0L;
        clip.setMicrosecondPosition(0);
        this.play();
    }

    // Method to stop the audio
    public void stop() throws UnsupportedAudioFileException,
            IOException, LineUnavailableException
    {
        currentFrame = 0L;
        clip.stop();
        clip.close();
    }

    // Method to jump over a specific part
    public void jump(long c) throws UnsupportedAudioFileException, IOException,
            LineUnavailableException
    {
        if (c > 0 && c < clip.getMicrosecondLength())
        {
            clip.stop();
            clip.close();
            resetAudioStream();
            currentFrame = c;
            clip.setMicrosecondPosition(c);
            this.play();
        }
    }

    // Method to reset audio stream
    public void resetAudioStream() throws UnsupportedAudioFileException, IOException,
            LineUnavailableException
    {
        audioInputStream = AudioSystem.getAudioInputStream(
                new File(this.filePath).getAbsoluteFile());
        clip.open(audioInputStream);
//        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    static public void playButtonBeep() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        SimpleAudioPlayer beep = new SimpleAudioPlayer("buttonBeep.wav");
        beep.play();
    }

    static void playGunShoot() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        SimpleAudioPlayer beep = new SimpleAudioPlayer("buttonBeep.wav");
        beep.play();
    }

}
