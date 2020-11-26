import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import java.util.Random;
import java.util.*;
import java.io.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;
import javax.imageio.ImageIO;
import java.awt.image.*;



class ELISE extends JFrame{
	int x;			//x座標
	int y;			//y座標
	int cx;			//体の中心点x座標
	int cy;			//体の中心点y座標
	int fx;			//顔の中心点x座標
	int fy;			//顔の中心点y座標
	int cr=30;			//体の半径
	int fr=10;			//顔の半径
	int w;			//横幅		いらない
	int h;			//縦幅		いらない
	int changetimer;//画像の変化タイミング
	int hp=69999;	//体力 80000でMadElise 70000でEliseOverweg
	int maxhp=100000;//最高体力
	int form=0;		//現在の行動パターン
	int formcount=0;	//行動パターンの現在の状態
	int dioset;	//時を止める前の前回のフレームでの描画パターン
	
	BufferedImage bimg;		//前フレームでの表示画像
	BufferedImage nbimg;
	
	BufferedImage elise1=loadImage(".\\picture\\enemy\\elise\\elise1.png");
	BufferedImage elise2=loadImage(".\\picture\\enemy\\elise\\elise2.png");
	BufferedImage elise3=loadImage(".\\picture\\enemy\\elise\\elise3.png");
	BufferedImage elise4=loadImage(".\\picture\\enemy\\elise\\elise4.png");
	
	BufferedImage elisea1=loadImage(".\\picture\\enemy\\elise\\elisea1.png");
	BufferedImage elisea2=loadImage(".\\picture\\enemy\\elise\\elisea2.png");
	BufferedImage elisea3=loadImage(".\\picture\\enemy\\elise\\elisea3.png");
	
	BufferedImage eliseo1=loadImage(".\\picture\\enemy\\elise\\eliseoverweg1.png");
	BufferedImage eliseo2=loadImage(".\\picture\\enemy\\elise\\eliseoverweg1.png");
	BufferedImage eliseo3=loadImage(".\\picture\\enemy\\elise\\eliseoverweg1.png");
	BufferedImage eliseo4=loadImage(".\\picture\\enemy\\elise\\eliseoverweg1.png");
	BufferedImage eliseo5=loadImage(".\\picture\\enemy\\elise\\eliseoverweg1.png");
	
	BufferedImage nelise1;
	BufferedImage nelise2;
	BufferedImage nelise3;
	BufferedImage nelise4;
	
	BufferedImage nelisea1;
	BufferedImage nelisea2;
	BufferedImage nelisea3;
	
	BufferedImage neliseo1;
	BufferedImage neliseo2;
	BufferedImage neliseo3;
	BufferedImage neliseo4;
	BufferedImage neliseo5;
	
	Random rnd = new Random();
	
	ELISE(){
		nelise1=invisi(elise1);
		nelise2=invisi(elise2);
		nelise3=invisi(elise3);
		nelise4=invisi(elise4);
		
		nelisea1=invisi(elisea1);
		nelisea2=invisi(elisea2);
		nelisea3=invisi(elisea3);
		
		neliseo1=invisi(eliseo1);
		neliseo2=invisi(eliseo1);
		neliseo3=invisi(eliseo1);
		neliseo4=invisi(eliseo1);
		neliseo5=invisi(eliseo1);
	}
	
	
	public BufferedImage invisi(BufferedImage i){
		return getinvisible(255,i,true);	
	}
	public BufferedImage invisib(Image img){
		BufferedImage bimg = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics g = bimg.getGraphics();
		g.drawImage(img, 0, 0, null);
		g.dispose();
		return invisi(bimg);
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
	
	
	
	public BufferedImage getinvisible(int count,BufferedImage changed,boolean nega) //3日かかった count=0-255
	{
		int maxx = changed.getWidth();
		int maxy = changed.getHeight();
		BufferedImage invisible;//透過加工後
		invisible= new BufferedImage(maxx,maxy,BufferedImage.TYPE_4BYTE_ABGR);
		int     pixel,pixel1,wk1;
		for(int y=0; y<maxy; y++)
        {   for(int x=0; x<maxx; x++)
            {   pixel1= changed.getRGB(x,y);
				wk1= (count*16777216);
				wk1=wk1&0xFF000000;
				pixel = pixel1 & wk1;
				pixel = pixel | (pixel1 & 0x00FFFFFF);
				if(nega)		pixel ^= 0xFFFFFF;	//ネガ
                invisible.setRGB(x,y,pixel);
            }
        }
		return invisible;
	}
	
	public void setx(int myx){
		x=myx;
		cx=myx+48;
		fx=myx+48;
	}
	
	public int getx(){
		return x;
	}
	
	public void sety(int myy){
		y=myy;
		cy=myy+124;
		fy=myy+65;
	}
	
	public int gety(){
		return y;
	}
	
	public void setcx(int myx){
		cx=myx;
		x=myx-48;
		fx=myx;
	}
	
	public int getcx(){
		return cx;
	}
	
	public void setcy(int myy){
		cy=myy;
		y=myy-124;
		fy=myy-59;
	}
	
	public int getcy(){
		return cy;
	}
	
	public int getfx(){
		return fx;
	}
	
	public int getfy(){
		return fy;
	}
	
	public int getcr(){
		return cr;
	}
	
	public int getfr(){
		return fr;
	}
	
	public BufferedImage getelise1(boolean dio){
		BufferedImage re=elise1;
		if(dio) re=nelise1;
		return re;
	}
	
	public BufferedImage getelisea1(boolean dio){
		BufferedImage re=elisea1;
		if(dio) re=nelisea1;
		return re;
	}
	
	public BufferedImage geteliseo1(boolean dio){
		BufferedImage re=eliseo1;
		if(dio) re=neliseo1;
		return re;
	}
	
	public BufferedImage getimg(int direction,boolean dio){
		BufferedImage reimg=elise1;
		int randset=0;
		byte damage=0; //あとで設定
		if (hp>80000){
			damage=0;
		}else if(hp>70000){
			damage=1;
		}else{
			damage=2;
		}
		if(dio){
			switch(damage){
				case 0:			//elise
					switch(dioset){
						case 0:
							reimg=nelise1;
							break;
						case 1:
							reimg=nelise2;
							break;
						case 2:
							reimg=nelise3;
							break;
						case 3:
							reimg=nelise4;
							break;
					}
					break;
				case 1:			//mad elise
					switch(dioset){
						case 0:
							reimg=nelisea1;
							break;
						case 1:
							reimg=nelisea2;
							break;
						case 2:
							reimg=nelisea3;
							break;
					}
					break;
				case 2:			//elise overweg
					switch(dioset){
						case 0:
							reimg=neliseo1;
							break;
						case 1:
							reimg=neliseo2; //右
							break;
						case 2:
							reimg=neliseo3; //左
							break;
						case 3:
							reimg=neliseo4; //上
							break;
						case 4:
							reimg=neliseo5; //下
							break;
					}
					break;
				}
		}else{
			if(changetimer==0){
				switch(damage){
				case 0:			//elise
					randset=rnd.nextInt(4);
					switch(randset){
						case 0:
							reimg=elise1;
							break;
						case 1:
							reimg=elise2;
							break;
						case 2:
							reimg=elise3;
							break;
						case 3:
							reimg=elise4;
							break;
					}
					dioset = randset;
					changetimer=30;
					bimg=reimg;
					break;
				case 1:			//mad elise
					randset=rnd.nextInt(3);
					switch(randset){
						case 0:
							reimg=elisea1;
							break;
						case 1:
							reimg=elisea2;
							break;
						case 2:
							reimg=elisea3;
							break;
					}
					dioset = randset;
					changetimer=50;
					bimg=reimg;
					break;
				case 2:			//elise overweg
					randset=rnd.nextInt(5);
					switch(randset){
						case 0:
							reimg=eliseo1;
							break;
						case 1:
							reimg=eliseo2; //右
							break;
						case 2:
							reimg=eliseo3; //左
							break;
						case 3:
							reimg=eliseo4; //上
							break;
						case 4:
							reimg=eliseo5; //下
							break;
					}
					dioset = randset;
					changetimer=40;
					bimg=reimg;
					break;
				}
			}else{
				changetimer-=1;
				reimg=bimg;
			}
		}
		return reimg;
	}
	
	public void sethp(int myhp){
		hp = myhp;
	}
	
	public int gethp(){
		return hp;
	}
	
	public void setform(int myform){
		form=myform;
	}
	
	public int getform(){
		return form;
	}
	
	public void setformcount(int myformcount){
		formcount=myformcount;
	}
	
	public int getformcount(){
		return formcount;
	}
	
	public int getmaxhp(){
		return maxhp;
	}
}