import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.io.IOException;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.*;

class PLAYER_SHOT extends JFrame {
	private int x;				//X座標
	private int y;				//Y座標
	private int cx;				//弾の中心点x座標  x座標+15
	private int cy;				//弾の中心点y座標  y座標+15
	private int r=15;				//半径
	private int speed=35;			//速さ
	BufferedImage meimg;			//画像
	
	PLAYER_SHOT(){
		meimg = loadImage (".\\picture\\shot\\sshot.png");
		r=(meimg.getWidth()-1)/2;
	}
	
	public BufferedImage loadImage(String fileName)
    {   InputStream is = null;
        try
        {   is = new FileInputStream(fileName);
            BufferedImage img = ImageIO.read(is);
            return img;
        }
        catch (IOException e)
        {   throw new RuntimeException(e);  }
        finally
        {   if (is != null)
                try { is.close(); }
                catch (IOException e) {}
        }
    }
	
	public void setx(int myx){
		x=myx;
		cx=myx+r;
	}
	public int getx(){
		return x;
	}
	public void sety(int myy){
		y=myy;
		cy=myy+r;
	}
	public int gety(){
		return y;
	}
	public void setcx(int myx){
		cx=myx;
		x=myx-r;
	}
	public int getcx(){
		return cx;
	}
	public void setcy(int myy){
		cy=myy;
		y=myy-r;
	}
	public int getcy(){
		return cy;
	}
	
	public void setr(int myr){
		r = myr;
	}
	
	public int getr(){
		return r;
	}
	
	public void setspeed(int mys){
		speed = mys;
	}
	
	public int getspeed(){
		return speed;
	}
	
	public BufferedImage getimg(){
		return meimg;
	}
}