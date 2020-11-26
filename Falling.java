import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

class Falling extends JFrame
{
	int x;
	int y;
	int speed;
	Image img;
	
	Falling()	//コンストラクタ
	{
		x=0;
		y=0;
		speed=0;
	}
	public void setpara(int myx,int myy,int myspeed,int myimg)
	{
		x=myx;
		y=myy;
		speed=myspeed;
		switch(myimg){
			case 1:
				img = getToolkit().getImage(".\\picture\\falling\\s1.png");
				break;
			case 2:
				img = getToolkit().getImage(".\\picture\\falling\\s2.png");
				break;
			case 3:
				img = getToolkit().getImage(".\\picture\\falling\\s3.png");
				break;
			case 4:
				img = getToolkit().getImage(".\\picture\\falling\\s4.png");
				break;
			case 5:
				img = getToolkit().getImage(".\\picture\\falling\\s5.png");
				break;
			case 6:
				img = getToolkit().getImage(".\\picture\\falling\\sn1.png");
				break;
			case 7:
				img = getToolkit().getImage(".\\picture\\falling\\sn2.png");
				break;
			case 8:
				img = getToolkit().getImage(".\\picture\\falling\\sn3.png");
				break;
			case 9:
				img = getToolkit().getImage(".\\picture\\falling\\sn4.png");
				break;
			case 10:
				img = getToolkit().getImage(".\\picture\\falling\\sn5.png");
				break;
			case 11:
				img = getToolkit().getImage(".\\picture\\falling\\sn6.png");
				break;
			/*case 12:
				img = getToolkit().getImage(".\\picture\\falling\\sn7.png");
				break;
			case 13:
				img = getToolkit().getImage(".\\picture\\falling\\sn8.png");
				break;
			case 14:
				img = getToolkit().getImage(".\\picture\\falling\\sn9.png");
				break;
			case 15:
				img = getToolkit().getImage(".\\picture\\falling\\sn10.png");
				break;*/
			default:
				img = getToolkit().getImage(".\\picture\\falling\\s1.png");
				break;
		}
	}
	
	public void setpara2(int myx,int myy,int myspeed,Image myimg)
	{
		x=myx;
		y=myy;
		speed=myspeed;
		img=myimg;
	}
	
	public void fallingdown()
	{
		x=0;
		y=0;
		speed=0;
	}
	
	public void setx(int myx){
		x=myx;
	}
	
	public int getx(){
		return x;
	}
	
	public void sety(int myy){
		y=myy;
	}
	
	public int gety(){
		return y;
	}
	
	public int getspeed(){
		return speed;
	}
	
	public Image getimg(){
		return img;
	}
}