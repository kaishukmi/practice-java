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
import java.util.Random;

class ELISE_SHOT extends JFrame {
	private double x;				//X座標
	private double y;				//Y座標
	private double cx;				//弾の中心点x座標  x座標+15
	private double cy;				//弾の中心点y座標  y座標+15
	private double px;				//1フレームでx軸方向に進む距離
	private double py;				//1フレームでy軸方向に進む距離
	private int r;				//半径
	private double powerr;			//半径+10の自乗(グレイス範囲)
	private int color;			//色
	private int	mysize;			//大きさ
	private int m;				//球の移動種類 0でまっすぐ 1でGo&Homing 2でsplit
	private	int ctime;			//変化するまでの時間
	private int cspeed=0;			//変化後のスピード
	BufferedImage img;			//画像
	
	Random rnd = new Random();
	
	public void setall(double mycx,double mycy,double mypx,double mypy,int size,int mycolor){			//sizeは1最小で4最大
		setcx(mycx);
		setcy(mycy);
		px=mypx;
		py=mypy;
		color = mycolor;
		if (mycolor==0) color = rnd.nextInt(5)+1;		//色
		mysize=size;
		m=0;
		switch(size){
			case 1:
				switch(color){
					case 1:
						img=loadImage (".\\picture\\button\\b1.png");
						break;
					case 2:
						img=loadImage (".\\picture\\button\\g1.png");
						break;
					case 3:
						img=loadImage (".\\picture\\button\\p1.png");
						break;
					case 4:
						img=loadImage (".\\picture\\button\\r1.png");
						break;
					case 5:
						img=loadImage (".\\picture\\button\\y1.png");
						break;
				}
				break;
			case 2:
				switch(color){
					case 1:
						img=loadImage (".\\picture\\button\\b2.png");
						break;
					case 2:
						img=loadImage (".\\picture\\button\\g2.png");
						break;
					case 3:
						img=loadImage (".\\picture\\button\\p2.png");
						break;
					case 4:
						img=loadImage (".\\picture\\button\\r2.png");
						break;
					case 5:
						img=loadImage (".\\picture\\button\\y2.png");
						break;
				}
				break;
			case 3:
				switch(color){
					case 1:
						img=loadImage (".\\picture\\button\\b3.png");
						break;
					case 2:
						img=loadImage (".\\picture\\button\\g3.png");
						break;
					case 3:
						img=loadImage (".\\picture\\button\\p3.png");
						break;
					case 4:
						img=loadImage (".\\picture\\button\\r3.png");
						break;
					case 5:
						img=loadImage (".\\picture\\button\\y3.png");
						break;
				}
				break;
			case 4:
				switch(color){
					case 1:
						img=loadImage (".\\picture\\button\\b4.png");
						break;
					case 2:
						img=loadImage (".\\picture\\button\\g4.png");
						break;
					case 3:
						img=loadImage (".\\picture\\button\\p4.png");
						break;
					case 4:
						img=loadImage (".\\picture\\button\\r4.png");
						break;
					case 5:
						img=loadImage (".\\picture\\button\\y4.png");
						break;
				}
				break;
		}
		
		if (img.getWidth()%2==0){		//偶数
			r=(img.getWidth())/2-1;
		}else{							//奇数
			r=(img.getWidth()-1)/2;
		}
		powerr=Math.pow((double)r+10,2);
	}
	
	public BufferedImage loadImage(String fileName)
    {   InputStream is = null;
        try
        {   is = new FileInputStream(fileName);
            BufferedImage imga = ImageIO.read(is);
            return imga;
        }
        catch (IOException e)
        {   throw new RuntimeException(e);  }
        finally
        {   if (is != null)
                try { is.close(); }
                catch (IOException e) {}
        }
    }
	
	public void setx(double myx){
		x=myx;
		cx=myx+r;
	}
	public double getx(){
		return x;
	}
	public void sety(double myy){
		y=myy;
		cy=myy+r;
	}
	public double gety(){
		return y;
	}
	public void setcx(double myx){
		cx=myx;
		x=myx-r;
	}
	public double getcx(){
		return cx;
	}
	public void setcy(double myy){
		cy=myy;
		y=myy-r;
	}
	public double getcy(){
		return cy;
	}
	
	public void setr(int myr){
		r = myr;
	}
	
	public int getr(){
		return r;
	}
	
	public double getpowerr(){
		return powerr;
	}
	
	public BufferedImage getimg(){
		return img;
	}
	
	public int getcolor(){
		return color;
	}
	
	public int getsize(){
		return mysize;
	}
	
	public void setpx(double mypx){
		px=mypx;
	}
	
	public double getpx(){
		return px;
	}
	
	public void setpy(double mypy){
		py=mypy;
	}
	
	public double getpy(){
		return py;
	}
	
	public void setm(int mym){
		m=mym;
	}
	
	public int getm(){
		return m;
	}
	
	public void setctime(int mytime){
		ctime=mytime;
	}
	
	public int getctime(){
		return ctime;
	}
	
	public void setcspeed(int mytime){
		cspeed=mytime;
	}
	
	public int getcspeed(){
		return cspeed;
	}
}