import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

class MUSIC
{
    File    soundFile;
    Clip    clip;
    AudioInputStream audioInputStream;
    AudioFormat     audioFormat;
    DataLine.Info   info;
	
    // Constructor
    MUSIC(String file)
    {   try
        {   soundFile = new File(file);
            audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            audioFormat = audioInputStream.getFormat();
            info = new DataLine.Info(Clip.class, audioFormat);
            clip = (Clip)AudioSystem.getLine(info);
            clip.open(audioInputStream);
        }
        catch (UnsupportedAudioFileException e)
        {   e.printStackTrace();  }
        catch (IOException e)
        {   e.printStackTrace();  }
        catch (LineUnavailableException e)
        {   e.printStackTrace();  }
    }
	
	private void controlByLinearScalar(FloatControl control, double linearScalar) {
		control.setValue((float)Math.log10(linearScalar) * 20);
	}

    void Play()		//ç≈èâÇ©ÇÁçƒê∂
    {   clip.stop();
		clip.setFramePosition(0);
        clip.start();
    }
	
	void sPlay(double des)	//âπó éOäÑÇ≈çƒê∂
	{	FloatControl control = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
		controlByLinearScalar(control, des); // 30%ÇÃâπó Ç≈çƒê∂Ç∑ÇÈ
		clip.stop();
		clip.setFramePosition(0);
        clip.start();
	}
	
	void rePlay()	//ìríÜÇ©ÇÁçƒê∂
	{	clip.start();
	}
	
	void Stop()		//çƒê∂àÍéûí‚é~
	{	clip.stop();
	}
}