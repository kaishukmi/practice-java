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


class BG extends JFrame
{
	int titleselect;
	int bgcounter1;
	int bgcounter2;
	int bgcounter3;
	int cnt =0;
	int shecounter=0;
	BufferedImage mpp;
	BufferedImage allbg;
	BufferedImage negaallbg;
	BufferedImage negapls1;
	Image loading;
	BufferedImage titlelogo;
	BufferedImage km1;
	BufferedImage km2;
	BufferedImage km3;
	BufferedImage pressed;
	BufferedImage startlogo;
	BufferedImage optionlogo;
	BufferedImage exitlogo;
	BufferedImage startdlogo;
	BufferedImage optiondlogo;
	BufferedImage exitdlogo;
	BufferedImage dlogo;
	Image maisieframe;
	Image stage2_1;
	BufferedImage stage3_1;
	BufferedImage stage3_2;
	BufferedImage stage3_3;
	BufferedImage stage3_4;
	BufferedImage nstage3_1;
	BufferedImage nstage3_2;
	BufferedImage nstage3_3;
	BufferedImage nstage3_4;
	Image she;
	Image she1;
	Image she2;
	Image she3;
	Image el;
	Image elsaw;
	
	PLAYER_SHOT pls = new PLAYER_SHOT();
	
	BG()		//コンストラクタ
	{
		titleselect = 1;
		bgcounter1 = 0;
		bgcounter2 = 0;
		bgcounter3 = 0;
		mpp =				loadImage(".\\picture\\logo\\logo3ex1.png");
		allbg =				loadImage(".\\picture\\bg\\allbg.png");
		negaallbg = 		invisi(allbg);
		negapls1 =			invisi(pls.getimg());
		loading =			getToolkit().getImage(".\\picture\\title\\loadingnega.png");
		titlelogo = 		loadImage(".\\picture\\title\\KMHOTF.png");
		km1 =				loadImage(".\\picture\\title\\KMHOTF1.png");
		km2 =				loadImage(".\\picture\\title\\KMHOTF2.png");
		km3 =				loadImage(".\\picture\\title\\KMHOTF3.png");
		pressed=			loadImage(".\\picture\\title\\pressenter.png");
		startlogo = 		loadImage(".\\picture\\title\\start.png");
		optionlogo = 		loadImage(".\\picture\\title\\option.png");
		exitlogo = 			loadImage(".\\picture\\title\\exit.png");
		startdlogo = 		loadImage(".\\picture\\title\\startd.png");
		optiondlogo = 		loadImage(".\\picture\\title\\optiond.png");
		exitdlogo = 		loadImage(".\\picture\\title\\exitd.png");
		dlogo = 			loadImage(".\\picture\\title\\d.png");
		maisieframe =		getToolkit().getImage(".\\picture\\me\\maisie\\selectframe.png");
		stage2_1 = 			getToolkit().getImage(".\\picture\\bg\\maryturara.png");
		stage3_1 = 			loadImage(".\\picture\\bg\\bill1.png");
		stage3_2 = 			loadImage(".\\picture\\bg\\bill2.png");
		stage3_3 = 			loadImage(".\\picture\\bg\\bill3.png");
		stage3_4 = 			loadImage(".\\picture\\bg\\utyu.jpg");
		nstage3_1 = 			invisi(stage3_1);
		nstage3_2 = 			invisi(stage3_2);
		nstage3_3 = 			invisi(stage3_3);
		nstage3_4 = 			invisi(stage3_4);
		she =				getToolkit().getImage(".\\picture\\title\\SheComes.png");
		she1 =				getToolkit().getImage(".\\picture\\title\\SheComes1.png");
		she2 =				getToolkit().getImage(".\\picture\\title\\SheComes2.png");
		she3 =				getToolkit().getImage(".\\picture\\title\\SheComes3.png");
		el =				getToolkit().getImage(".\\picture\\title\\el.png");
		elsaw =				getToolkit().getImage(".\\picture\\title\\elsaw.png");
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
	
	public BufferedImage getmpp() {
		return mpp;
	}
	
	public BufferedImage allback() {
		return allbg;
	}
	
	public BufferedImage negaallback() {
		return negaallbg;
	}
	
	public BufferedImage negaplshot1() {
		return negapls1;
	}
	
	public Image loadback() {
		return loading;
	}
	
	public void setselect(int select){
		titleselect=select;
	}
	
	public int getselect(){
		return titleselect;
	}
	
	public void setbgcount1(int count){
		bgcounter1=count;
	}
	
	public int getbgcount1(){
		return bgcounter1;
	}
	
	public void setbgcount2(int count){
		bgcounter2=count;
	}
	
	public int getbgcount2(){
		return bgcounter2;
	}
	
	public void setbgcount3(int count){
		bgcounter3=count;
	}
	
	public int getbgcount3(){
		return bgcounter3;
	}
	
	public BufferedImage gettitle(){
		return titlelogo;
	}
	
	public BufferedImage getintitle(int count){
		return getinvisible(count,titlelogo,false);
	}
	
	public BufferedImage gettitlep(){
		BufferedImage back=km1;
		if((cnt>=0)&&(cnt<2)){
			back=km1;
		}else if((cnt>=2)&&(cnt<4)){
			back=km2;
		}else if((cnt>=4)&&(cnt<19)){
			back=km3;
		}
		cnt+=1;
		if (cnt==19) cnt = 0;
		return back;
	}
	
	public BufferedImage getpressed(){
		return pressed;
	}
	
	public BufferedImage getinpressed(int count){
		return getinvisible(count,pressed,false);
	}
	
	public BufferedImage start(){
		return startlogo;
	}
	
	public BufferedImage getinstart(int count){
		return getinvisible(count,startlogo,false);
	}
	
	public BufferedImage option(){
		return optionlogo;
	}
	
	public BufferedImage getinoption(int count){
		return getinvisible(count,optionlogo,false);
	}
	
	public BufferedImage exit(){
		return exitlogo;
	}
	
	public BufferedImage getinexit(int count){
		return getinvisible(count,exitlogo,false);
	}
	
	public BufferedImage startd(){
		return startdlogo;
	}
	
	public BufferedImage getinstartd(int count){
		return getinvisible(count,startdlogo,false);
	}
	
	public BufferedImage optiond(){
		return optiondlogo;
	}
	
	public BufferedImage getinoptiond(int count){
		return getinvisible(count,optiondlogo,false);
	}
	
	public BufferedImage exitd(){
		return exitdlogo;
	}
	
	public BufferedImage getinexitd(int count){
		return getinvisible(count,exitdlogo,false);
	}
	
	public BufferedImage d(){
		return dlogo;
	}
	
	public BufferedImage getind(int count){
		return getinvisible(count,dlogo,false);
	}
	
	public Image maisie() {
		return maisieframe;
	}
	
	public Image st2_1() {
		return stage2_1;
	}
	
	public BufferedImage st3_1() {	
		return stage3_1;
	}
	
	public BufferedImage st3_2() {
		return stage3_2;
	}
	
	public BufferedImage st3_3() {
		return stage3_3;
	}
	
	public BufferedImage st3_4() {
		return stage3_4;
	}
	
	public BufferedImage nst3_1() {	
		return nstage3_1;
	}
	
	public BufferedImage nst3_2() {
		return nstage3_2;
	}
	
	public BufferedImage nst3_3() {
		return nstage3_3;
	}
	
	public BufferedImage nst3_4() {
		return nstage3_4;
	}
	
	public Image getshe(){
		Image back=she;
		if((shecounter>=0)&&(shecounter<4)){
			back=she1;
			shecounter+=1;
		}else if((shecounter>=4)&&(shecounter<8)){
			back=she2;
			shecounter+=1;
		}else if((shecounter>=8)&&(shecounter<10)){
			back=she3;
			shecounter+=1;
		}else if((shecounter>=10)){
			back=she;
		}
		return back;
	}
	
	public Image getel(){
		return el;
	}
	
	public Image getelsaw(){
		return elsaw;
	}
}