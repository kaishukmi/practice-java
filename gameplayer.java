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

class gameplayer extends JFrame {
	private int x;				//X座標
	private int y;				//Y座標
	private int cx;				//体の中心点x座標  x座標+40
	private int cy;				//体の中心点y座標  y座標+40
	private int speed;			//移動距離
	private int power;			//攻撃力
	private int maxhp=99999;	//体力の最大値
	private int nowhp=99999;	//現在の体力
	private int maxcr=99999;	//狂気の最大値
	private int nowcr=99999;	//現在の狂気
	private boolean attachable;	//弾の発射間隔
	private boolean alive;		//生存の有無
	private int chara;			//1=maisie
	private int now;			//四枚一セットの画像のポインタ
	private Image error=getToolkit().getImage(".\\picture\\me\\maisie\\error.png");
	private Image el1=getToolkit().getImage(".\\picture\\me\\maisie\\me1.png");
	private Image el2=getToolkit().getImage(".\\picture\\me\\maisie\\me2.png");
	private Image el3=getToolkit().getImage(".\\picture\\me\\maisie\\me3.png");
	private Image el4=getToolkit().getImage(".\\picture\\me\\maisie\\me4.png");
	private Image el5=getToolkit().getImage(".\\picture\\me\\maisie\\me5.png");
	private Image el6=getToolkit().getImage(".\\picture\\me\\maisie\\me6.png");
	private Image el7=getToolkit().getImage(".\\picture\\me\\maisie\\me7.png");
	private Image el8=getToolkit().getImage(".\\picture\\me\\maisie\\me8.png");
	private Image el9=getToolkit().getImage(".\\picture\\me\\maisie\\me9.png");
	private Image centerpoint=getToolkit().getImage(".\\picture\\me\\center.png");
	
	public void setx(int myx){
		x=myx;
		cx=myx+40;
	}
	
	public int getx(){
		return x;
	}
	
	public void sety(int myy){
		y=myy;
		cy=myy+40;
	}
	
	public int gety(){
		return y;
	}
	
	public void setcx(int myx){
		cx=myx;
		x=myx-40;
	}
	
	public int getcx(){
		return cx;
	}
	
	public void setcy(int myy){
		cy=myy;
		y=myy-40;
	}
	
	public int getcy(){
		return cy;
	}
	
	public int getmaxhp(){
		return maxhp;
	}
	
	public void sethp(int myhp){
		nowhp=myhp;
	}
	
	public int gethp(){
		return nowhp;
	}
	
	public int getmaxcr(){
		return maxcr;
	}
	
	public void setcr(int myhp){
		nowcr=myhp;
	}
	
	public int getcr(){
		return nowcr;
	}
	
	public void setimg(int myimg){
		chara=myimg;
		now=1;
	}
	
	public Image getimg(boolean ido){					//2時間
		Image img =error;
		if 		((now>=1)&&(now<4)){
			img=el1;
		}else if((now>=4)&&(now<7)||(now>=36)&&(now<39)){
			img=el2;
		}else if((now>=7)&&(now<10)||(now>=33)&&(now<36)){
			img=el3;
		}else if((now>=10)&&(now<12)||(now>=30)&&(now<33)){
			img=el4;
		}else if((now>=12)&&(now<14)||(now>=28)&&(now<30)){
			img=el5;
		}else if((now>=14)&&(now<16)||(now>=26)&&(now<28)){
			img=el6;
		}else if((now>=16)&&(now<18)||(now>=24)&&(now<26)){
			img=el7;
		}else if((now>=18)&&(now<20)||(now>=22)&&(now<24)){
			img=el8;
		}else if((now>=20)&&(now<22)){
			img=el9;
		}
		if (ido){
			now+=3;
		}else{
			now+=1;
		}
		if (now>38)	now=1;
		return img;
	}
	
	public Image center(){
		return centerpoint;
	}
}
			
			