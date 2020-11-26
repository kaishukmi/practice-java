import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;
import java.applet.*;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

class GetBGM
{
	MUSIC title;
	MUSIC elise;
	MUSIC elise2;
	//MyPlayer elise;
	GetBGM()
	{
		
		title = new MUSIC(".\\bgm\\neo29.wav");
		elise2 = new MUSIC(".\\bgm\\Œy—Ê‰»parallels.wav");
		elise = new MUSIC(".\\bgm\\own_little_world.wav");
		//elise = new MyPlayer();
		
	}
	
	void gotitle(){
		title.Play();
	}
	
	void stoptitle(){
		title.Stop();
	}
	
	void goelise(){
		elise.Play();
	}
	void reelise(){
		elise.rePlay();
	}
	
	void stopelise(){
		elise.Stop();
	}
	
	void goelise2(){
		elise2.Play();
	}
	void reelise2(){
		elise2.rePlay();
	}
	
	void stopelise2(){
		elise2.Stop();
	}
	
	/*void goelise(){
	try {
            elise.play(".\\bgm\\04-Parallels.mp3");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JavaLayerException e) {
            e.printStackTrace();
        } finally {
            if (elise != null) {
                try {
                    elise.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
	}*/
}