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

class NEGA_ELISE_SHOT extends JFrame{
	BufferedImage b1=invisi(loadImage (".\\picture\\button\\b1.png"));
	BufferedImage g1=invisi(loadImage (".\\picture\\button\\g1.png"));
	BufferedImage p1=invisi(loadImage (".\\picture\\button\\p1.png"));
	BufferedImage r1=invisi(loadImage (".\\picture\\button\\r1.png"));
	BufferedImage y1=invisi(loadImage (".\\picture\\button\\y1.png"));
	
	BufferedImage b2=invisi(loadImage (".\\picture\\button\\b2.png"));
	BufferedImage g2=invisi(loadImage (".\\picture\\button\\g2.png"));
	BufferedImage p2=invisi(loadImage (".\\picture\\button\\p2.png"));
	BufferedImage r2=invisi(loadImage (".\\picture\\button\\r2.png"));
	BufferedImage y2=invisi(loadImage (".\\picture\\button\\y2.png"));
	
	BufferedImage b3=invisi(loadImage (".\\picture\\button\\b3.png"));
	BufferedImage g3=invisi(loadImage (".\\picture\\button\\g3.png"));
	BufferedImage p3=invisi(loadImage (".\\picture\\button\\p3.png"));
	BufferedImage r3=invisi(loadImage (".\\picture\\button\\r3.png"));
	BufferedImage y3=invisi(loadImage (".\\picture\\button\\y3.png"));
	
	BufferedImage b4=invisi(loadImage (".\\picture\\button\\b4.png"));
	BufferedImage g4=invisi(loadImage (".\\picture\\button\\g4.png"));
	BufferedImage p4=invisi(loadImage (".\\picture\\button\\p4.png"));
	BufferedImage r4=invisi(loadImage (".\\picture\\button\\r4.png"));
	BufferedImage y4=invisi(loadImage (".\\picture\\button\\y4.png"));
	
	public BufferedImage getimg(int color,int size){
		BufferedImage img=b1;
		switch(size){
				case 1:
					switch(color){
						case 1:
							img=b1;
							break;
						case 2:
							img=g1;
							break;
						case 3:
							img=p1;
							break;
						case 4:
							img=r1;
							break;
						case 5:
							img=y1;
							break;
					}
					break;
				case 2:
					switch(color){
						case 1:
							img=b2;
							break;
						case 2:
							img=g2;
							break;
						case 3:
							img=p2;
							break;
						case 4:
							img=r2;
							break;
						case 5:
							img=y2;
							break;
					}
					break;
				case 3:
					switch(color){
						case 1:
							img=b3;
							break;
						case 2:
							img=g3;
							break;
						case 3:
							img=p3;
							break;
						case 4:
							img=r3;
							break;
						case 5:
							img=y3;
							break;
					}
					break;
				case 4:
					switch(color){
						case 1:
							img=b4;
							break;
						case 2:
							img=g4;
							break;
						case 3:
							img=p4;
							break;
						case 4:
							img=r4;
							break;
						case 5:
							img=y4;
							break;
					}
					break;
			}
		return img;
	}
	
	public BufferedImage invisi(BufferedImage i){
		return getinvisible(255,i,true);	
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
	
	public BufferedImage getinvisible(int count,BufferedImage changed,boolean nega) //3ì˙Ç©Ç©Ç¡ÇΩ count=0-255
	{
		int maxx = changed.getWidth();
		int maxy = changed.getHeight();
		BufferedImage invisible;//ìßâﬂâ¡çHå„
		invisible= new BufferedImage(maxx,maxy,BufferedImage.TYPE_4BYTE_ABGR);
		int     pixel,pixel1,wk1;
		for(int y=0; y<maxy; y++)
        {   for(int x=0; x<maxx; x++)
            {   pixel1= changed.getRGB(x,y);
				wk1= (count*16777216);
				wk1=wk1&0xFF000000;
				pixel = pixel1 & wk1;
				pixel = pixel | (pixel1 & 0x00FFFFFF);
				if(nega)		pixel ^= 0xFFFFFF;	//ÉlÉK
                invisible.setRGB(x,y,pixel);
            }
        }
		return invisible;
	}
	
}