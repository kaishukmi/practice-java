import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.RenderingHints;
import java.awt.geom.*;
import java.awt.BasicStroke;
import java.awt.Rectangle;
import java.awt.Robot;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import java.util.Random;
import java.util.*;
import java.io.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;


class KMHOTF01 extends JFrame implements KeyListener
{	
    Dimension   size;								//��ʃT�C�Y�v��
    Image       back;								//�_�u���o�b�t�@�p
    Graphics    buffer;								//�`��p�O���t�B�b�N�̃��[�U�[���
	int         key_t[] = { 0,0,0,0,0,0,0,0,0 }; 	//UP, RIGHT, DOWN, LEFT, ENTER, Z, X, SPACE,  SHIFT,
	boolean		push=false;							//true�Ō��݃L�[�������Ă���
    int         pos=-1;								//�t���[���̑���
	byte 		game_step=0;						//�Q�[���̌��݂̏��
	//game_step=ALL
	double		pi=3.1415;							//�~����	�P���x��������
	double		cosine[] = new double[361];			//�R�T�C��	�����͊p�x		0~360�܂�361�̃��������		�g���ꍇ��(����)%360�̌`�Ŏg��
	double		sine[] = new double[361];			//�T�C��		�V						�V
	//game_step=0~1
	int			title_counter=200;					//�^�C�g���̏��
	int			title_pressed=100;					//�^�C�g����pressenter�̏��100�ȏ�ŏ㏸,�����l100  200�ȏ�Ō���,�����l251
	//game_step2		�L�����N�^�[�I�����̃t���[���̃T�C�Y�̃��M�����[�V������500*320px���x
	int			frame_position=1;					//�X�B�[�̃t���[���̏ꏊ 0=��ʒu�Œ��� 1=��\����ԏ����l�̂� 2>=���݂̏ꏊ
	boolean		selectedswitch=false;				//�L�����N�^�[�I����������Ԃł͂Ȃ�	�g��Ȃ��\������
	//game_step3	
	double		loadrevo=0;							//���[�f�B���O�̊p�x
	//game_step4	
	int			tozyo=0;							//�g���ĂȂ�
	boolean		move=false;							//�v���C���[���ړ����Ă��邩�ǂ���
	int			shot_pointer=0;						//���˂����e�̌��݂̃p�X
	long		shot_time=0;						//���˂�������
	long		alltime=0;							//����
	byte 		pisuton=50;							//�e�̔��ˊԊu 50�ȏ�Ō��ʂ���						���~�����聙�~
	byte 		tanima=7;							//�e�̏㉺�̌���
	byte		plmspeed=8;							//�v���C���[�̑���
	byte		plmspeedl=3;						//�ᑬ�ړ����̑���
	boolean		dio=false;							//true�Ŏ��Ԓ�~ false�Œʏ�
	int			towardco=0;							//�G�̓o��J�E���g
	int			stoppos;							//pos�̕ۑ�
	int			elmoveco;							//�G���[�[�̈ړ��ɂ�����t���[����
	int			elpx;								//�G���[�[��1�t���[����x�������Ɉړ�����l
	int			elpy;								//�G���[�[��1�t���[����y�������Ɉړ�����l
	boolean		upper=true;							//true�ŃG���[�[�͏㏸���Ă���false�ł��Ă��Ȃ�
	int			elspointer=0;						//�G���[�[�V���b�g�|�C���^�[
	int			howattack=0;						//����U��������
	int			zyotai;								//�O��̃G���[�[�̏��
	int			twistcolor1;						//�G���[�[��]�e�����̐F�ݒ�
	int			twistcolor2;						//			�V
	//������
	byte		fall_count=0;						//�~���Ă���Ԋu 0��true
	int			fall_pointer=0;						//�������̌��݂̃p�X
	int			wind_count=0;						//�����ς��Ԋu 	�V
	int			windstrength=0;						//���̋���
	
	/*
	(��*1)Math���C�u������p���Ȃ��p�x�v�Z�@ ������̂ق����y�ʂȋC������(������)
	
	
	*/
	
	BG bg = new BG(); 								//�w�i�֌W���e�����ۑ���
	Falling[] fall = new Falling[100];				//�ᓙ�~���Ă�����̃I�u�W�F�N�g
	GetSE se = new GetSE();							//���ʉ��̌Ăяo��
	gameplayer pl = new gameplayer();				//�v���C���[�̓o�^
	ELISE el = new ELISE();							//�G���[�[�̓o�^
	PLAYER_SHOT[] pls = new PLAYER_SHOT[200];		//�v���C���[�̒e�̃I�u�W�F�N�g
	ELISE_SHOT[] els = new ELISE_SHOT[500];			//�G���[�[�̒e�̃I�u�W�F�N�g
	NEGA_ELISE_SHOT nes = new NEGA_ELISE_SHOT();	//�G���[�[�̒e�̃l�K�i�[��
	GetBGM bgm = new GetBGM();						//bgm�̌Ăяo��
	boolean 	open1=true;							//se��炵�����ǂ���
	boolean	 	open2=true;							//		�V
	Random rnd = new Random();  					//x = rnd.nextInt(a) + b; b����a-1+b�܂ł̒l���o��
	AffineTransform at = new AffineTransform(); 	//��]�����Ɏg�p
	
	public static void main(String args[]) 
	{
		new KMHOTF01();
	}
	
	    
    public KMHOTF01()					// Constructor
    {   super("Image View");
		addKeyListener(this);
		//�e�����̃C���X�^���X��
		for (byte i=0;i<99;i++){
			fall[i] = new Falling();
		}
		for (int i=0;i<199;i++){
			pls[i] = new PLAYER_SHOT();
		}
		for (int i=0;i<499;i++){
			els[i] = new ELISE_SHOT();
		}
		//�W�����w�֐����g��Ȃ����߂̃������m��
		for (int i=0;i<360;i++){		//0~360�܂�361�̏�����
				cosine[i]=Math.cos(Math.toRadians(i));
				sine[i]=Math.sin(Math.toRadians(i));
		}
        ThreadClass threadcls = new ThreadClass();
        Thread thread = new Thread(threadcls);
        thread.start();
		setTitle("Keep My Hands Off The Fact");
		ImageIcon icon = new ImageIcon("./picture/icon2.png");
		setIconImage(icon.getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1040, 520);
        setVisible(true);
		setResizable(false);
        size = getSize();
        back= createImage(size.width, size.height);
        if (back==null) System.out.println("createImage Error");
    }

    
    class ThreadClass implements Runnable//  Runnable Class
    {
        public void run()
        {   long    nowTime,drawTime;

            nowTime= System.currentTimeMillis();
            drawTime= nowTime+500;
            while(true)
            {   nowTime= System.currentTimeMillis();
                if (drawTime<nowTime)
                {   drawTime= nowTime+30;
					alltime=nowTime;
					game();
					Update();
					repaint();
                }
            }
        }
    }

    
    public void paint(Graphics g)		// Paint Method  �`�惁�\�b�h
    {   
		//�w�i
		if (back==null || pos<0)    return;
        buffer= back.getGraphics();
        if (buffer==null)   		return;
        size = getSize();
			switch(game_step){
			case 0:
				//�w�i
				buffer.drawImage(bg.st3_4(),0,0,this);
		        buffer.drawImage(bg.st3_3(),0,0,size.width,size.height,bg.getbgcount1(),0,bg.getbgcount1()+1040,size.height,this);
				buffer.drawImage(bg.st3_2(),0,0,size.width,size.height,bg.getbgcount2(),0,bg.getbgcount2()+1040,size.height,this);
				buffer.drawImage(bg.st3_1(),0,0,size.width,size.height,pos,0,pos+1040,size.height,this);
				//������
				for (byte i=0;i<=fall_pointer;i++){
					buffer.drawImage(fall[i].getimg(),fall[i].getx(),fall[i].gety(),this);
				}
				buffer.drawImage(bg.gettitle(),275,50,this);
				buffer.drawImage(bg.gettitlep(),275,50,this);
				//press enter�`��
				if ((title_pressed>=100)&&(title_pressed<=151)){
					buffer.drawImage(bg.getinpressed((title_pressed-100)*5),320,390,this);
				}else if ((title_pressed<=251)&&(title_pressed>=200)){
					buffer.drawImage(bg.getinpressed((title_pressed-200)*5),320,390,this);
				}
				//�Ó]�֌W����
				if(title_counter>=200){
					if(title_counter==450){
						title_counter=1;
					}
					title_counter+=1;
					buffer.setColor(new Color(0, 0, 0, 255));
					buffer.fillRect(0,0,1040,520);
					if((title_counter>=250)&&(title_counter<400)) buffer.drawImage(bg.getmpp(),370,200,this);
				}else if(title_counter!=0){
					buffer.setColor(new Color(0, 0, 0, 255-title_counter*2));
					buffer.fillRect(0,0,1040,520);
					title_counter+=1;
				}
				buffer.drawImage(bg.allback(),0,0,this);
				break;
			case 1:
				buffer.drawImage(bg.st3_4(),0,0,this);
		        buffer.drawImage(bg.st3_3(),0,0,size.width,size.height,bg.getbgcount1(),0,bg.getbgcount1()+1040,size.height,this);
				buffer.drawImage(bg.st3_2(),0,0,size.width,size.height,bg.getbgcount2(),0,bg.getbgcount2()+1040,size.height,this);
				buffer.drawImage(bg.st3_1(),0,0,size.width,size.height,pos,0,pos+1040,size.height,this);
				for (byte i=0;i<=fall_pointer;i++){
					buffer.drawImage(fall[i].getimg(),fall[i].getx(),fall[i].gety(),this);
				}
				buffer.drawImage(bg.gettitle(),275,50,this);
				buffer.drawImage(bg.gettitlep(),275,50,this);
				if (title_pressed<100){
					if (title_pressed==0){
						switch(bg.getselect()){
						case 1:
							buffer.drawImage(bg.startd(),455,350,this);
							buffer.drawImage(bg.option(),427,390,this);
							buffer.drawImage(bg.exit(),461,430,this);
							buffer.drawImage(bg.d(),370,335,this);
							break;
						case 2:
							buffer.drawImage(bg.start(),455,350,this);
							buffer.drawImage(bg.optiond(),427,390,this);
							buffer.drawImage(bg.exit(),461,430,this);
							buffer.drawImage(bg.d(),370,375,this);
							break;
						case 3:
							buffer.drawImage(bg.start(),455,350,this);
							buffer.drawImage(bg.option(),427,390,this);
							buffer.drawImage(bg.exitd(),461,430,this);
							buffer.drawImage(bg.d(),370,415,this);
							break;
						default:
							break;
						}
					}else{
						buffer.drawImage(bg.getinstartd((title_pressed-1)*5),455,350,this);
						buffer.drawImage(bg.getinoption((title_pressed-1)*5),427,390,this);
						buffer.drawImage(bg.getinexit((title_pressed-1)*5),461,430,this);
						buffer.drawImage(bg.getind((title_pressed-1)*5),370,335,this);
					}
				}else{
					if ((title_pressed>=100)&&(title_pressed<=151)){
						buffer.drawImage(bg.getinpressed((title_pressed-100)*5),320,390,this);
										/*String prestr = String.valueOf((title_pressed-100)*5);
										buffer.setColor(Color.red);
										buffer.drawString(prestr,30,40);//�f�o�b�O*/
					}else if ((title_pressed<=251)&&(title_pressed>=200)){
						buffer.drawImage(bg.getinpressed((title_pressed-200)*5),320,390,this);
										/*String prestr = String.valueOf((title_pressed-200)*5);
										buffer.setColor(Color.red);
										buffer.drawString(prestr,30,40);//�f�o�b�O*/
					}
				}
				buffer.drawImage(bg.allback(),0,0,this);
				break;
			case 2:
				buffer.drawImage(bg.st3_4(),0,0,this);
		        buffer.drawImage(bg.st3_3(),0,0,size.width,size.height,bg.getbgcount1(),0,bg.getbgcount1()+1040,size.height,this);
				buffer.drawImage(bg.st3_2(),0,0,size.width,size.height,bg.getbgcount2(),0,bg.getbgcount2()+1040,size.height,this);
				buffer.drawImage(bg.st3_1(),0,0,size.width,size.height,pos,0,pos+1040,size.height,this);
				for (byte i=0;i<=fall_pointer;i++){
					buffer.drawImage(fall[i].getimg(),fall[i].getx(),fall[i].gety(),this);
				}
				if (title_pressed!=0){
					buffer.drawImage(bg.getintitle((title_pressed-1)*5),275,50,this);
					buffer.drawImage(bg.getinstartd((title_pressed-1)*5),455,350,this);
					buffer.drawImage(bg.getinoption((title_pressed-1)*5),427,390,this);
					buffer.drawImage(bg.getinexit((title_pressed-1)*5),461,430,this);
					buffer.drawImage(bg.getind((title_pressed-1)*5),370,335,this);
				}else{
					if (frame_position==0){
						buffer.drawImage(bg.maisie(),275,50,this);
						buffer.drawImage(pl.getimg(false),495,150,this);
					}else if (frame_position>=2){
						buffer.drawImage(bg.maisie(),frame_position-500,50,this);
						buffer.drawImage(pl.getimg(false),frame_position-280,150,this);
					}
				}
				buffer.drawImage(bg.allback(),0,0,this);
				break;
			case 3:
				buffer.setColor(new Color(0, 0, 0, 255));
				buffer.fillRect(0,0,1040,520);
				Graphics2D buffer2 = (Graphics2D)buffer;
				int w = bg.loadback().getWidth(this);
      			int h = bg.loadback().getHeight(this);
      			int x = w / 2;
      			int y = h / 2;
      			//at.setToRotation(Math.toRadians(loadrevo), x, y);		Math���C�u������p�����p�x�v�Z�@
				at.setToRotation((loadrevo/180)*pi, x, y);				//(��*1)Math���C�u������p���Ȃ��p�x�v�Z�@ ������̂ق����y�ʂȋC������(������)
				buffer2.translate(800, 350);
      			buffer2.drawImage(bg.loadback(), at, this);
				buffer2.translate(-800, -350);
				break;
			case 4:
				//�w�i
				if(dio){
					buffer.setColor(new Color(0, 0, 0, 255));
					buffer.fillRect(0,0,1040,520);
					//���w�i
					buffer.drawImage(bg.nst3_4(),0,0,this);			
		        	buffer.drawImage(bg.nst3_3(),0,0,size.width,size.height,bg.getbgcount1(),0,bg.getbgcount1()+1040,size.height,this);
					buffer.drawImage(bg.nst3_2(),0,0,size.width,size.height,bg.getbgcount2(),0,bg.getbgcount2()+1040,size.height,this);
					buffer.drawImage(bg.nst3_1(),0,0,size.width,size.height,stoppos,0,stoppos+1040,size.height,this);
				}else{
					buffer.setColor(new Color(255, 255, 255, 255));
					buffer.fillRect(0,0,1040,520);
					//���w�i
					buffer.drawImage(bg.st3_4(),0,0,this);			
		        	buffer.drawImage(bg.st3_3(),0,0,size.width,size.height,bg.getbgcount1(),0,bg.getbgcount1()+1040,size.height,this);
					buffer.drawImage(bg.st3_2(),0,0,size.width,size.height,bg.getbgcount2(),0,bg.getbgcount2()+1040,size.height,this);
					buffer.drawImage(bg.st3_1(),0,0,size.width,size.height,pos,0,pos+1040,size.height,this);
				}
				
				if (title_counter <=127){						//�u���b�N�C��
					buffer.setColor(new Color(0, 0, 0, 255-title_counter*2));
					buffer.fillRect(0,0,1040,520);
					title_counter+=1;
				}else if(title_counter<=400){					//She comes towards you
					buffer.drawImage(bg.getshe(),245,80,this);
					title_counter+=1;
				}else if(title_counter<=1440){					//�G�l�~�[���S�\��
					buffer2 = (Graphics2D)buffer;
					w = bg.getelsaw().getWidth(this);
	      			h = bg.getelsaw().getHeight(this);
	      			x = w / 2;
	      			y = h / 2;
	      			//at.setToRotation(Math.toRadians(title_counter*5), x, y);
					at.setToRotation(((double)title_counter*5/180)*pi, x, y);		//(��*1)		
					buffer2.translate(245,title_counter-900);
	      			buffer2.drawImage(bg.getelsaw(), at, this);
					buffer.drawImage(bg.getel(),0,0,this);
					buffer2.translate(-245,-title_counter+900);
					if (title_counter-400<=428){
						title_counter+=8;
					}else if(title_counter-400<=508){
						title_counter+=2;
					}else{
						title_counter+=8;
					}
				}else{		//��������Q�[���{��
					if(dio){//���Ԓ�~��
						//�e�ۏ���
						for (int i=0;i<shot_pointer;i++){
							buffer.drawImage(bg.negaplshot1(),pls[i].getx(),pls[i].gety(),this);
						}
						//�G����
						if (towardco<300){	
								buffer.drawImage(el.getelise1(dio),el.getx(),el.gety(),this);
						}else{
							buffer.drawImage(el.getimg(0,dio),el.getx(),el.gety(),this);
						}
						//�G�e�ۏ���
						for (int i=0;i<elspointer;i++){
							buffer.drawImage(nes.getimg(els[i].getcolor(),els[i].getsize()),(int)els[i].getx(),(int)els[i].gety(),this);
						}
					}else{
						//�e�ۏ���
						for (int i=0;i<shot_pointer;i++){
							buffer.drawImage(pls[i].getimg(),pls[i].getx(),pls[i].gety(),this);
						}
						//�G����
						if (towardco<300){	
								buffer.drawImage(el.getelise1(dio),el.getx(),el.gety(),this);
						}else{
							buffer.drawImage(el.getimg(0,dio),el.getx(),el.gety(),this);
						}
						//�G�e�ۏ���
						for (int i=0;i<elspointer;i++){
							buffer.drawImage(els[i].getimg(),(int)els[i].getx(),(int)els[i].gety(),this);
						}
					}
					
					//buffer.drawImage(bg.invisib(negative),0,0,this);
					//�L�����N�^�[����
					buffer.drawImage(pl.getimg(move),pl.getx(),pl.gety(),this);
					//���C�t�T�[�N���v���C���[
					double hpper=Math.floor(pl.gethp()*1800/pl.getmaxhp());				//�̗͂̊���
					int graphper=(int)hpper/10;
					String sthp = String.valueOf(pl.gethp());
					double crper=Math.floor(pl.getcr()*1800/pl.getmaxcr());				//���C�̊���
					int crgraphper=(int)crper/10;
					String stcr = String.valueOf(pl.getcr());
					buffer.setColor(new Color(255-(int)Math.floor(pl.gethp()*255/pl.getmaxhp()), (int)Math.floor(pl.gethp()*255/pl.getmaxhp()), 0, 255));
					buffer.drawArc(pl.getcx()-40, pl.getcy()-40, 80, 80,180-graphper, graphper);
					buffer.setColor(new Color(255-(int)Math.floor(pl.gethp()*255/pl.getmaxhp()), (int)Math.floor(pl.gethp()*255/pl.getmaxhp()), 0, 255));
					buffer.drawString(sthp,pl.getcx()-20-String.valueOf(pl.gethp()).length()*7, pl.getcy()+7);
					buffer.setColor(new Color(0, 0, 255, 255));
					buffer.drawArc(pl.getcx()-40, pl.getcy()-40, 80, 80,360-crgraphper, crgraphper);
					buffer.setColor(new Color(0, 0, 255, 255));
					buffer.drawString(stcr,pl.getcx()+58-String.valueOf(pl.getcr()).length()*7, pl.getcy()+7);
					buffer.drawImage(pl.center(),pl.getcx()-2,pl.getcy()-2,this);
					//���C�t�T�[�N���G���[�[
					hpper=Math.floor(el.gethp()*3600/el.getmaxhp());
					graphper=(int)hpper/10;
					buffer.setColor(new Color(255-(int)Math.floor(el.gethp()*255/el.getmaxhp()), (int)Math.floor(el.gethp()*255/el.getmaxhp()), 0, 255));
					buffer.drawArc(el.getcx()-120, el.getcy()-120, 240, 240,270-graphper, graphper);
					
				}
				if(dio){
					buffer.drawImage(bg.negaallback(),0,0,this);
				}else{
					buffer.drawImage(bg.allback(),0,0,this);
				}
				break;
			}
		//������
		/*for (byte i=0;i<=fall_pointer;i++){
				buffer.drawImage(fall[i].getimg(),fall[i].getx(),fall[i].gety(),this);
		}*/
		//���ߏ�����{
		/*buffer.setColor(new Color(255, 0, 0, 160));
		buffer.fillRect(0,0,1040,520);
		*/
		
		g.drawImage(back,0,0,this);
    }
	

	public void game()
	{
		action();	
		graphicC();
		soundeffect();
	}
	
	public void graphicC()		//�摜�֌W���Z����
	{
		switch(game_step){
		case 0:
			//�w�i
			bg.setbgcount1(bg.getbgcount1()+3);
			if (bg.getbgcount1()>1040)	bg.setbgcount1(0);
			bg.setbgcount2(bg.getbgcount2()+2);
			if (bg.getbgcount2()>1040)	bg.setbgcount2(0);
			if(title_counter==127){
				game_step=1;	bgm.gotitle();
			}
			//������
			if ((fall_count<=0)&&(fall_pointer!=98)){
				fall[fall_pointer].setpara(rnd.nextInt(1030)+10,-14,rnd.nextInt(3) + 1,rnd.nextInt(1) + 8);
				fall_pointer+=1;
				fall_count=4;					//��̗�
			}
			for (byte i=0;i<=fall_pointer;i++){
				fall[i].sety(fall[i].gety()+fall[i].getspeed());
				//fall[i].setx(fall[i].getx()+windstrength);//���̏���
				if(fall[i].gety()>=520){
					fall[i].setpara2(fall[fall_pointer-1].getx(),fall[fall_pointer-1].gety(),fall[fall_pointer-1].getspeed(),fall[fall_pointer-1].getimg());
					fall[fall_pointer-1].fallingdown();
					fall_pointer-=1;
				}
			}
			if (fall_count>0){
				fall_count-=1;
			}
			//press enter����
			if ((title_pressed>=100)&&(title_pressed<=151)){
				title_pressed+=1;
				if (title_pressed==151){
					title_pressed=251;
				}
			}else if ((title_pressed<=251)&&(title_pressed>=200)){
				title_pressed-=1;
				if (title_pressed==200){
					title_pressed=100;
				}
			}
			break;
		case 1:
			bg.setbgcount1(bg.getbgcount1()+3);
			if (bg.getbgcount1()>1040)	bg.setbgcount1(0);
			bg.setbgcount2(bg.getbgcount2()+2);
			if (bg.getbgcount2()>1040)	bg.setbgcount2(0);
			//������
			if ((fall_count<=0)&&(fall_pointer!=98)){
				fall[fall_pointer].setpara(rnd.nextInt(1030)+10,-14,rnd.nextInt(3) + 1,rnd.nextInt(1) + 8);
				fall_pointer+=1;
				fall_count=4;					//��̗�
			}
			for (byte i=0;i<fall_pointer;i++){
				fall[i].sety(fall[i].gety()+fall[i].getspeed());
				//fall[i].setx(fall[i].getx()+windstrength);//���̏���
				if(fall[i].gety()>=520){
					fall[i].setpara2(fall[fall_pointer-1].getx(),fall[fall_pointer-1].gety(),fall[fall_pointer-1].getspeed(),fall[fall_pointer-1].getimg());
					fall[fall_pointer-1].fallingdown();
					fall_pointer-=1;
				}
			}
			if (fall_count>0){
				fall_count-=1;
			}
			//press enter����
			if ((title_pressed>=100)&&(title_pressed<=151)){
				title_pressed+=1;
				if (title_pressed==151){
					title_pressed=251;
				}
			}else if ((title_pressed<=251)&&(title_pressed>=200)){
				title_pressed-=1;
				if (title_pressed==200){
					title_pressed=100;
				}
			}else if ((title_pressed<=52)&&(title_pressed>=1)){
				title_pressed+=1;
				if (title_pressed==52){
					title_pressed=0;
				}
			}
			break;
		case 2:
			bg.setbgcount1(bg.getbgcount1()+3);
			if (bg.getbgcount1()>1040)	bg.setbgcount1(0);
			bg.setbgcount2(bg.getbgcount2()+2);
			if (bg.getbgcount2()>1040)	bg.setbgcount2(0);
			//������
			if ((fall_count<=0)&&(fall_pointer!=98)){
				fall[fall_pointer].setpara(rnd.nextInt(1030)+10,-14,rnd.nextInt(3) + 1,rnd.nextInt(1) + 8);
				fall_pointer+=1;
				fall_count=4;					//��̗�
			}
			for (byte i=0;i<=fall_pointer;i++){
				fall[i].sety(fall[i].gety()+fall[i].getspeed());
				//fall[i].setx(fall[i].getx()+windstrength);//���̏���
				if(fall[i].gety()>=520){
					fall[i].setpara2(fall[fall_pointer-1].getx(),fall[fall_pointer-1].gety(),fall[fall_pointer-1].getspeed(),fall[fall_pointer-1].getimg());
					fall[fall_pointer-1].fallingdown();
					fall_pointer-=1;
				}
			}
			if (fall_count>0){
				fall_count-=1;
			}
			if ((title_pressed<=52)&&(title_pressed>=1)){
				title_pressed-=1;
				if (title_pressed==1){				//����,�L�����I���֏����l�𑗂�
					title_pressed=0;
					frame_position=2;
					pl.setimg(1);//maisie
				}
			}
			
			//�L�����t���[�����ړ�
			if (frame_position>=2){
				frame_position+=30;
				if(frame_position>=775)	frame_position=0;
			}
			//�L�����N�^�[�ړ� ����Ɍ���L�����t���[���Ɠ���
			if (frame_position!=1){
				pl.setx(frame_position);
			}
			break;
		case 3:
			if (loadrevo==360) {
				game_step=4;
				title_counter=1441;										//�^�C�g���J�E���^�[��0�ɖ߂����� �f�o�b�O��Ǝ��̂�1441�ɐݒ�
				bgm.goelise();
				pl.setcx(50);
				pl.setcy(251);
				el.setx(1040);
				el.sety(100);
				}
			if (loadrevo==300) se.gomecha();
			loadrevo +=5;
			break;
		case 4:
			if (title_counter>=1440){
				//�e�ۏ���
				if (el.gethp()>80000){
					zyotai=0;
				}else if(el.gethp()>70000){
					zyotai=1;
				}else{
					zyotai=2;
				}
				for (int i=0;i<shot_pointer;i++){
					pls[i].setx(pls[i].getx()+pls[i].getspeed());
					//���e�̓G�ւ̓����蔻��
					double at1;
					double at2;
					double at3;
					at1=0;
					at2=0;
					at3=0;
					if (Math.pow((double)Math.abs(pls[i].getcx()-el.getcx()),2)+ Math.pow((double)Math.abs(pls[i].getcy()-el.getcy()),2)<=Math.pow((double)(pls[i].getr()+el.getcr()),2)){
						shot_pointer-=1;		//��
						pls[i].setx(pls[shot_pointer].getx());
						pls[i].sety(pls[shot_pointer].gety());
						el.sethp(el.gethp()-15);//15�_���[�W
						if(!dio) se.gobasi();
						i-=1;
						continue;
					}
					at1=0;
					at2=0;
					at3=0;
					if (Math.pow((double)Math.abs(pls[i].getcx()-el.getfx()),2)+ Math.pow((double)Math.abs(pls[i].getcy()-el.getfy()),2)<=Math.pow((double)(pls[i].getr()+el.getfr()),2)){
						shot_pointer-=1;		//��
						pls[i].setx(pls[shot_pointer].getx());
						pls[i].sety(pls[shot_pointer].gety());
						el.sethp(el.gethp()-30);//30�_���[�W
						if(!dio) se.gobasi();
						i-=1;
						continue;
					}
					//��O����
					if((pls[i].getx()>=1040)&&(i!=shot_pointer-1))
						{shot_pointer-=1;pls[i].setx(pls[shot_pointer].getx());pls[i].sety(pls[shot_pointer].gety());}
				}
				if(el.gethp()<70000){
					if(zyotai!=2) el.setform(0);
				}else if(el.gethp()<80000){
					if(zyotai!=1) el.setform(0);
				}
				
				//dio����
				if(dio){
					//CrazyPoint�̌����Ǝ��~�߂̏I��
					pl.setcr(pl.getcr()-1123);
					if (pl.getcr()<=0){
						pl.setcr(0);
						dio=false;
						bgm.reelise();
						se.stopstop();
					}
				}else{
					//CrazyPoint�̉�
					if (pl.getcr()<pl.getmaxcr()){
						pl.setcr(pl.getcr()+117);
						if (pl.getcr()>=pl.getmaxcr()) pl.setcr(pl.getmaxcr());
					}
					//�G�֌W����
					if (towardco<300){			//�G�̓o��V�[��
						el.setx(1040-towardco);
						towardco+=2;
					}else{						//�G�̍s����
						//�G���쏈��
						if(el.gethp()>=80000){					//elise
							switch(el.getform()){
								case 0:					//���̍s���I��
									el.setform(rnd.nextInt(2)+1);
									el.setformcount(0);
									howattack=0;
									break;
								case 1:				//��ړ��さ�㉺�ړ������ʒ��i���ˁ��㉺�~��ǔ�
									if(el.getformcount()==0){			//�ړ���������
										int rndx=rnd.nextInt(100)+700;
										elmoveco=(int)Math.ceil(Math.sqrt(Math.pow((double)Math.abs(el.getx()-rndx),2) + Math.pow((double)Math.abs(el.gety()),2))/10);
										elpx=(int)((rndx-el.getx())/elmoveco);
										elpy=(int)((-el.gety())/elmoveco);
										el.setformcount(el.getformcount()+1);
									}else if(el.getformcount()<=79){	//�ړ�
										if (el.getformcount()<=elmoveco){
											el.setx(el.getx()+elpx);
											el.sety(el.gety()+elpy);
										}
										upper=false;
										el.setformcount(el.getformcount()+1);
									}else{								//�U���J�n
										if (upper){
											el.sety(el.gety()-23);
										}else{
											el.sety(el.gety()+23);
										}
										if (el.gety()<-125) {		//gah
											upper=false;
											els[elspointer].setall(el.getcx(),el.getcy(),-5,0,2,0);
											els[elspointer].setm(1);
											els[elspointer].setctime(5);
											elspointer+=1;
										}
										if (el.gety()>412) {
											upper=true;
											els[elspointer].setall(el.getcx(),el.getcy(),-5,0,2,0);
											els[elspointer].setm(1);
											els[elspointer].setctime(5);
											elspointer+=1;
										}
										if (el.getformcount()%4==0){		//4�t���[���Ɉ��ł��Ă���
												els[elspointer].setall(el.getcx(),el.getcy(),-5,0,1,0);
												elspointer+=1;
										}
										el.setformcount(el.getformcount()+1);
										if(el.getformcount()==350)	el.setform(0);
									}
									break;
								case 2:			//�����_���ړ��さ�~��ǔ�
									if(el.getformcount()==0){			//�ړ���������
										int rndx=rnd.nextInt(520)+520;
										int rndy=rnd.nextInt(320)+100;
										elmoveco=(int)Math.ceil(Math.sqrt(Math.pow((double)Math.abs(el.getcx()-rndx),2) + Math.pow((double)Math.abs(el.getcy()-rndy),2))/10);
										elpx=(int)((rndx-el.getcx())/elmoveco);
										elpy=(int)((rndy-el.getcy())/elmoveco);
										el.setformcount(el.getformcount()+1);
									}else if(el.getformcount()<=70){	//�ړ�
										if (el.getformcount()<=elmoveco){
											el.setx(el.getx()+elpx);
											el.sety(el.gety()+elpy);
										}
										el.setformcount(el.getformcount()+1);
									}else{								//�U���J�n
										int j=elspointer;
										int hows=20;
										int speeds=10;
										for(int i=j;i<j+(hows/2);i++){
											els[elspointer].setall(el.getcx(),el.getcy(),0,0,2,0);
											els[elspointer+1].setall(el.getcx(),el.getcy(),0,0,2,0);
											els[elspointer].setm(1);
											els[elspointer+1].setm(1);
											els[elspointer].setctime(15);
											els[elspointer+1].setctime(15);
											els[elspointer].setcspeed(15);
											els[elspointer+1].setcspeed(15);
											els[elspointer].setpx(speeds*Math.cos(Math.toRadians(180/(hows/2)*i-j)));
											els[elspointer+1].setpx(-(speeds*Math.cos(Math.toRadians(180/(hows/2)*i-j))));
											els[elspointer].setpy(speeds*Math.sin(Math.toRadians(180/(hows/2)*i-j)));	
											els[elspointer+1].setpy(-(speeds*Math.sin(Math.toRadians(180/(hows/2)*i-j))));
											elspointer+=2;
										}
										howattack+=1;
										if(howattack==7) el.setform(0);
										el.setformcount(0);
									}					
									break;
							}
						}else if(el.gethp()>=70000){			//mad elise
							switch(el.getform()){
								case 0:					//���̍s���I��
									el.setform(rnd.nextInt(2)+1);
									el.setformcount(0);
									howattack=0;
									break;
								case 1:				//��ړ��さ�㉺�ړ������ʒǔ����ˁ��㉺�~��ǔ�
									if(el.getformcount()==0){			//�ړ���������
										int rndx=rnd.nextInt(100)+700;
										elmoveco=(int)Math.ceil(Math.sqrt(Math.pow((double)Math.abs(el.getx()-rndx),2) + Math.pow((double)Math.abs(el.gety()),2))/10);
										elpx=(int)((rndx-el.getx())/elmoveco);
										elpy=(int)((-el.gety())/elmoveco);
										el.setformcount(el.getformcount()+1);
									}else if(el.getformcount()<=79){	//�ړ�
										if (el.getformcount()<=elmoveco){
											el.setx(el.getx()+elpx);
											el.sety(el.gety()+elpy);
										}
										upper=false;
										el.setformcount(el.getformcount()+1);
									}else{								//�U���J�n
										if (upper){
											el.sety(el.gety()-17);
										}else{
											el.sety(el.gety()+17);
										}
										if ((el.gety()<-125)||(el.gety()>412)) {		//�o�O��\������ �������U���J�n����y���W��412���傫��(���_�I�ɂ͂قƂ�ǂ��肦�Ȃ���)
											upper=(!upper);
											int j=elspointer;
											int hows=6;
											int speeds=10;
											for(int i=j;i<j+(hows/2);i++){
												els[elspointer].setall(el.getcx(),el.getcy(),0,0,2,0);
												els[elspointer+1].setall(el.getcx(),el.getcy(),0,0,2,0);
												els[elspointer].setm(1);
												els[elspointer+1].setm(1);
												els[elspointer].setctime(15);
												els[elspointer+1].setctime(15);
												els[elspointer].setcspeed(10);
												els[elspointer+1].setcspeed(10);
												els[elspointer].setpx(speeds*Math.cos(Math.toRadians(180/(hows/2)*i-j)));
												els[elspointer+1].setpx(-(speeds*Math.cos(Math.toRadians(180/(hows/2)*i-j))));
												els[elspointer].setpy(speeds*Math.sin(Math.toRadians(180/(hows/2)*i-j)));	
												els[elspointer+1].setpy(-(speeds*Math.sin(Math.toRadians(180/(hows/2)*i-j))));
												elspointer+=2;
											}
										}
										if (el.getformcount()%2==0){		//4�t���[���Ɉ��ł��Ă���
												els[elspointer].setall(el.getcx(),el.getcy(),-5,0,1,0);
												els[elspointer].setm(1);
												els[elspointer].setctime(5);
												els[elspointer].setcspeed(10);
												elspointer+=1;
										}
										el.setformcount(el.getformcount()+1);
										if(el.getformcount()==1000)	el.setform(0);
									}
									break;
								case 2:				//���ړ��さ�㉺�ړ����~��ǔ�����
									if(el.getformcount()==0){			//�ړ���������
										int rndx=rnd.nextInt(100)+700;
										elmoveco=(int)Math.ceil(Math.sqrt(Math.pow((double)Math.abs(el.getx()-rndx),2) + Math.pow((double)Math.abs(el.gety()),2))/10);
										elpx=(int)((rndx-el.getx())/elmoveco);
										elpy=(int)((el.gety())/elmoveco);
										el.setformcount(el.getformcount()+1);
									}else if(el.getformcount()<=79){	//�ړ�
										if (el.getformcount()<=elmoveco){
											el.setx(el.getx()+elpx);
											el.sety(el.gety()+elpy);
										}
										upper=false;
										el.setformcount(el.getformcount()+1);
									}else{								//�U���J�n
										if (upper){
											el.sety(el.gety()-17);
										}else{
											el.sety(el.gety()+17);
										}
										if (el.gety()<-125) {		//gah
											upper=false;
										}
										if (el.gety()>412) {
											upper=true;
										}
										if (el.getformcount()%15==0){		//15�t���[���Ɉ��ł��Ă���
											int j=elspointer;
											int hows=50;
											int speeds=5;
											for(int i=j;i<j+(hows/2);i++){
												els[elspointer].setall(el.getcx(),el.getcy(),0,0,2,0);
												els[elspointer+1].setall(el.getcx(),el.getcy(),0,0,2,0);
												els[elspointer].setm(1);
												els[elspointer+1].setm(1);
												els[elspointer].setctime(20);
												els[elspointer+1].setctime(20);
												els[elspointer].setcspeed(20);
												els[elspointer+1].setcspeed(20);
												
												/*els[elspointer].setpx(speeds*Math.cos(Math.toRadians(180/(hows/2)*i-j)));
												els[elspointer+1].setpx(-(speeds*Math.cos(Math.toRadians(180/(hows/2)*i-j))));
												els[elspointer].setpy(speeds*Math.sin(Math.toRadians(180/(hows/2)*i-j)));	
												els[elspointer+1].setpy(-(speeds*Math.sin(Math.toRadians(180/(hows/2)*i-j))));*/
												
												/*els[elspointer].setpx(speeds*Math.cos(((180/((double)hows/2)*i-j)/180)*pi));				//(��*1)�y�ʉ�1�i�K��
												els[elspointer+1].setpx(-(speeds*Math.cos(((180/((double)hows/2)*i-j)/180)*pi)));
												els[elspointer].setpy(speeds*Math.sin(((180/((double)hows/2)*i-j)/180)*pi));	
												els[elspointer+1].setpy(-(speeds*Math.sin(((180/((double)hows/2)*i-j)/180)*pi)));*/
												
												els[elspointer].setpx(speeds*cosine[(180/(hows/2)*i-j)%360]);								//(��*1)�y�ʉ�2�i�K�ڎ�������
												els[elspointer+1].setpx(-(speeds*cosine[(180/(hows/2)*i-j)%360]));
												els[elspointer].setpy(speeds*sine[(180/(hows/2)*i-j)%360]);	
												els[elspointer+1].setpy(-(speeds*sine[(180/(hows/2)*i-j)%360]));
												
												elspointer+=2;
											}
										}
										el.setformcount(el.getformcount()+1);
										if(el.getformcount()==1000)	el.setform(0);
									}
									break;
							}
						}else{									//elise overweg
							switch(el.getform()){
								case 0:					//���̍s���I��
									el.setform(rnd.nextInt(1)+1);
									el.setformcount(0);
									howattack=0;
									break;
								case 1:			//�ړ��A���h�ǔ�
									if(el.getformcount()==0){			//�ړ���������
										int rndx=rnd.nextInt(520)+520;
										int rndy=rnd.nextInt(320)+100;
										elmoveco=(int)Math.ceil(Math.sqrt(Math.pow((double)Math.abs(el.getcx()-rndx),2) + Math.pow((double)Math.abs(el.getcy()-rndy),2))/10);
										elpx=(int)((rndx-el.getcx())/elmoveco);
										elpy=(int)((rndy-el.getcy())/elmoveco);
										el.setformcount(el.getformcount()+1);
									}else if(el.getformcount()<=70){	//�ړ�
										if (el.getformcount()<=elmoveco){
											el.setx(el.getx()+elpx);
											el.sety(el.gety()+elpy);
										}
										el.setformcount(el.getformcount()+1);
										if (el.getformcount()==71){
											twistcolor1=rnd.nextInt(5)+1;
											twistcolor2=rnd.nextInt(5)+1;
										}
									}else{								//�U���J�n
										int j=elspointer;
										int hows=8;
										int speeds=20;
										if (howattack%3==0){
											for(int i=j;i<j+(hows/2);i++){
												els[elspointer].setall(el.getcx(),el.getcy(),0,0,1,twistcolor1);
												els[elspointer+1].setall(el.getcx(),el.getcy(),0,0,1,twistcolor1);
												els[elspointer].setm(0);
												els[elspointer+1].setm(0);
												els[elspointer].setctime(0);
												els[elspointer+1].setctime(0);
												els[elspointer].setcspeed(0);
												els[elspointer+1].setcspeed(0);
												els[elspointer].setpx(speeds*cosine[(180/(hows/2)*i-j)%360]);
												els[elspointer+1].setpx(-(speeds*cosine[(180/(hows/2)*i-j)%360]));
												els[elspointer].setpy(speeds*sine[(180/(hows/2)*i-j)%360]);
												els[elspointer+1].setpy(-(speeds*sine[(180/(hows/2)*i-j)%360]));
												elspointer+=2;
											}
											for(int i=j;i<j+(hows/2);i++){
												els[elspointer].setall(el.getcx(),el.getcy(),0,0,3,twistcolor2);
												els[elspointer+1].setall(el.getcx(),el.getcy(),0,0,3,twistcolor2);
												els[elspointer].setm(0);
												els[elspointer+1].setm(0);
												els[elspointer].setctime(0);
												els[elspointer+1].setctime(0);
												els[elspointer].setcspeed(0);
												els[elspointer+1].setcspeed(0);
												els[elspointer].setpx(speeds*cosine[(180/(hows/2)*i-j)%360]);
												els[elspointer+1].setpx(-(speeds*cosine[(180/(hows/2)*i-j)%360]));
												els[elspointer].setpy(speeds*sine[(180/(hows/2)*i-j)%360]);
												els[elspointer+1].setpy(-(speeds*sine[(180/(hows/2)*i-j)%360]));
												elspointer+=2;
											}
										}
										howattack+=1;
										if(howattack==500) el.setform(0);
									}					
									break;
							}
						}//����I��
						//�G�e�ۏ���
						for (int i=0;i<elspointer;i++){
							els[i].setx(els[i].getx()+els[i].getpx());
							els[i].sety(els[i].gety()+els[i].getpy());
							switch(els[i].getm()){
								case 1:
									if (els[i].getctime()==1){
										double elsmoveco;
										if (els[i].getcspeed()==0){
											elsmoveco=Math.ceil(Math.sqrt(Math.pow((double)Math.abs(pl.getcx()-els[i].getcx()),2) + Math.pow((double)Math.abs(pl.getcy()-els[i].getcx()),2))/10);
										}else{
											elsmoveco=Math.ceil(Math.sqrt(Math.pow((double)Math.abs(pl.getcx()-els[i].getcx()),2) + Math.pow((double)Math.abs(pl.getcy()-els[i].getcx()),2))/els[i].getcspeed());
										}
										els[i].setpx(((pl.getcx()-els[i].getcx())/elsmoveco));
										els[i].setpy(((pl.getcy()-els[i].getcy())/elsmoveco));
										els[i].setctime(els[i].getctime()-1);
									}else if (els[i].getctime()>=1){
										els[i].setctime(els[i].getctime()-1);
									}
									break;
								case 2:
									break;
							}
							if(true){	//���������蔻�����
								if(Math.pow((double)Math.abs(els[i].getcx()-pl.getcx()),2)+ Math.pow((double)Math.abs(els[i].getcy()-pl.getcy()),2)<=els[i].getpowerr()){	//��i�K��
									if (Math.pow((double)Math.abs(els[i].getcx()-pl.getcx()),2)+ Math.pow((double)Math.abs(els[i].getcy()-pl.getcy()),2)<=Math.pow((double)(els[i].getr()+1),2)){//�O�i�K��
										elspointer-=1;		//��
										els[i].setall(els[elspointer].getcx(),els[elspointer].getcy(),els[elspointer].getpx(),els[elspointer].getpy(),els[elspointer].getsize(),els[elspointer].getcolor());
										els[i].setm(els[elspointer].getm());
										els[i].setctime(els[elspointer].getctime());
										els[i].setcspeed(els[elspointer].getcspeed());
										pl.sethp(pl.gethp()-5612);
										if(pl.gethp()<0) pl.sethp(0);
										i-=1;
										continue;
									}else{
										pl.sethp(pl.gethp()+523);
										pl.setcr(pl.getcr()+321);
										if (pl.gethp()>=99999) pl.sethp(99999);
										if (pl.getcr()>=99999) pl.setcr(99999);
									}
								}
							}
							if((els[i].getx()<-100)||(els[i].getx()>1140)||(els[i].gety()<-100)||(els[i].gety()>620)){
								elspointer-=1;
								els[i].setall(els[elspointer].getcx(),els[elspointer].getcy(),els[elspointer].getpx(),els[elspointer].getpy(),els[elspointer].getsize(),els[elspointer].getcolor());
								els[i].setm(els[elspointer].getm());
								els[i].setctime(els[elspointer].getctime());
								els[i].setcspeed(els[elspointer].getcspeed());
								i-=1;
								continue;
							}
						}
					}
					
				}
			}	
			//���w�i����
			bg.setbgcount1(bg.getbgcount1()+3);
			if (bg.getbgcount1()>1040)	bg.setbgcount1(0);
			bg.setbgcount2(bg.getbgcount2()+2);
			if (bg.getbgcount2()>1040)	bg.setbgcount2(0);
			stoppos=pos;
			break;
		}
		//������	
		/*if ((fall_count<=0)&&(fall_pointer!=98)){
			fall[fall_pointer].setpara(rnd.nextInt(1030)+10,-14,rnd.nextInt(3) + 1,rnd.nextInt(2) + 6);
			fall_pointer+=1;
			fall_count=5;
		}
		for (byte i=0;i<=fall_pointer;i++){
			fall[i].sety(fall[i].gety()+fall[i].getspeed());
			//fall[i].setx(fall[i].getx()+windstrength);//���̏���
			if(fall[i].gety()>=520){
				fall[i].setpara2(fall[fall_pointer-1].getx(),fall[fall_pointer-1].gety(),fall[fall_pointer-1].getspeed(),fall[fall_pointer-1].getimg());
				fall[fall_pointer-1].fallingdown();
				fall_pointer-=1;
			}
		}
		if (fall_count>0){
			fall_count-=1;
		}*/
		//�������ɐ�����
		/*if (wind_count<=0){		
			windstrength=rnd.nextInt(3)-1;
			wind_count=180;
		}else{
			wind_count-=1;
		}*/
	}
	
	public void action()
	{
		switch(game_step){
		case 0:
			break;
		case 1:
			if (title_pressed==0){
				if ((key_t[0]==1)&&(bg.getselect()!=1)&&(!push)) 
					{bg.setselect(bg.getselect()-1);	push=true;	se.goido();}
		        if ((key_t[2]==1)&&(bg.getselect()!=3)&&(!push))
					{bg.setselect(bg.getselect()+1);	push=true;	se.goido();}
				if ((key_t[0]!=1)&&(key_t[2]!=1)&&(key_t[4]!=1)) 	push=false;
				if ((key_t[4]==1)&&(bg.getselect()==3)&&(!push))	System.exit(0);
				if ((key_t[4]==1)&&(bg.getselect()==1)&&(!push))	
					{game_step=2;	se.gokettei();title_pressed=52;}	
			}else{
				if ((key_t[4]==1)&&(title_pressed>=100))
					{title_pressed=1;	push=true;	se.gosusumu();}
				if ((key_t[0]!=1)&&(key_t[2]!=1)&&(key_t[4]!=1)) 	push=false;
			}
			break;
		case 2:
			if (frame_position==0){
				if ((key_t[4]==1)&&(!push))	
					{game_step=3;	se.gokettei();	bgm.stoptitle();}
				if ((key_t[0]!=1)&&(key_t[2]!=1)&&(key_t[4]!=1)) 	push=false;
			}
			break;
		case 4:
			if (title_counter>=1440){
				//�L�[����
				move=false;
				if ((key_t[0]==1)&&(key_t[8]==1)){		//UP
					pl.sety(pl.gety()-plmspeedl);
					if(pl.getcy()<=20) pl.setcy(21);
				}else if(key_t[0]==1){
					pl.sety(pl.gety()-plmspeed);	move=true;
					if(pl.getcy()<=20) pl.setcy(21);}
		        if ((key_t[1]==1)&&(key_t[8]==1)){		//RIGHT
					pl.setx(pl.getx()+plmspeedl);
					if(pl.getcx()>=1040) pl.setcx(1039);
				}else if(key_t[1]==1){
					pl.setx(pl.getx()+plmspeed);	move=true;
					if(pl.getcx()>=1040) pl.setcx(1039);}
		        if ((key_t[2]==1)&&(key_t[8]==1)){		//DOWN
					pl.sety(pl.gety()+plmspeedl);
					if(pl.getcy()>=520) pl.setcy(519);
				}else if(key_t[2]==1){
					pl.sety(pl.gety()+plmspeed);	move=true;
					if(pl.getcy()>=520) pl.setcy(519);}
		        if ((key_t[3]==1)&&(key_t[8]==1)){		//LEFT
					pl.setx(pl.getx()-plmspeedl);
					if(pl.getcx()<=0) pl.setcx(1);
				}else if(key_t[3]==1){
					pl.setx(pl.getx()-plmspeed);	move=true;
					if(pl.getcx()<=0) pl.setcx(1);}
				if((key_t[5]==1)&&(shot_time<=alltime)){			//SHOT
					shot_time=alltime+pisuton;
					pls[shot_pointer].setcx(pl.getcx()+10);
					pls[shot_pointer+1].setcx(pl.getcx()+10);
					pls[shot_pointer].setcy(pl.getcy()+tanima);				//�v���C���[����̋���
					pls[shot_pointer+1].setcy(pl.getcy()-tanima);
					shot_pointer+=2;
					if (!dio) se.goshot2();
				}
				if((key_t[6]==1)&&(!push)){			//���Ԓ�~
					push=true;
					if(dio){
						dio =false;
						bgm.reelise();
						se.stopstop();
					}else{
						dio=true;
						bgm.stopelise();
						se.gostop();
					}
				}
				if (key_t[6]==0)	push=false;
			}
			break;
		}
	}
	
	
	
	
	
	
	    public void Update()				// �X�V����
    {   long wk= (System.currentTimeMillis()/30)%1040;
        pos= (int)wk;
    }
	
	// KeyEvent Listener
    public void keyPressed(KeyEvent e)
    {   switch(e.getKeyCode( ))
        {   case KeyEvent.VK_ESCAPE: System.exit(0);  break;
			case KeyEvent.VK_UP :   key_t[0]= 1;  break;
            case KeyEvent.VK_RIGHT: key_t[1]= 1;  break;
            case KeyEvent.VK_DOWN : key_t[2]= 1;  break;
            case KeyEvent.VK_LEFT : key_t[3]= 1;  break;
			case KeyEvent.VK_ENTER:	key_t[4]= 1;  break;
			case KeyEvent.VK_Z	:	key_t[5]= 1;  break;
			case KeyEvent.VK_X	:	key_t[6]= 1;  break;
			case KeyEvent.VK_SPACE: key_t[7]= 1;  break;
			case KeyEvent.VK_SHIFT: key_t[8]= 1;  break;
        }
    }
    public void keyReleased(KeyEvent e)
    {   switch(e.getKeyCode( ))
        {   case KeyEvent.VK_UP :   key_t[0]= 0;  break;
            case KeyEvent.VK_RIGHT: key_t[1]= 0;  break;
            case KeyEvent.VK_DOWN : key_t[2]= 0;  break;
            case KeyEvent.VK_LEFT : key_t[3]= 0;  break;
			case KeyEvent.VK_ENTER:	key_t[4]= 0;  break;
			case KeyEvent.VK_Z	:	key_t[5]= 0;  break;
			case KeyEvent.VK_X	:	key_t[6]= 0;  break;
			case KeyEvent.VK_SPACE: key_t[7]= 0;  break;
			case KeyEvent.VK_SHIFT: key_t[8]= 0;  break;
        }
    }
    public void keyTyped(KeyEvent e) { }
	
	public void soundeffect(){				//���ʉ��֌W����
		switch(game_step){
		case 0:
			if(open1)
			{open1=false;	se.goopening1();}
			if((title_counter<200)&&(open2))
			{open2=false;	se.goopening2();}
			break;
		case 1:
			break;
		}
	}
}