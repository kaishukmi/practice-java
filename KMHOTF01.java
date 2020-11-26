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
    Dimension   size;								//画面サイズ計測
    Image       back;								//ダブルバッファ用
    Graphics    buffer;								//描画用グラフィックのユーザー空間
	int         key_t[] = { 0,0,0,0,0,0,0,0,0 }; 	//UP, RIGHT, DOWN, LEFT, ENTER, Z, X, SPACE,  SHIFT,
	boolean		push=false;							//trueで現在キーを押している
    int         pos=-1;								//フレームの測定
	byte 		game_step=0;						//ゲームの現在の状態
	//game_step=ALL
	double		pi=3.1415;							//円周率	単精度浮動小数
	double		cosine[] = new double[361];			//コサイン	引数は角度		0~360まで361個のメモリ空間		使う場合は(引数)%360の形で使う
	double		sine[] = new double[361];			//サイン		〃						〃
	//game_step=0~1
	int			title_counter=200;					//タイトルの状態
	int			title_pressed=100;					//タイトルのpressenterの状態100以上で上昇,初期値100  200以上で減少,初期値251
	//game_step2		キャラクター選択時のフレームのサイズのレギュレーションは500*320px程度
	int			frame_position=1;					//スィーのフレームの場所 0=定位置固定状態 1=非表示状態初期値のみ 2>=現在の場所
	boolean		selectedswitch=false;				//キャラクター選択が導入状態ではない	使わない可能性あり
	//game_step3	
	double		loadrevo=0;							//ローディングの角度
	//game_step4	
	int			tozyo=0;							//使ってない
	boolean		move=false;							//プレイヤーが移動しているかどうか
	int			shot_pointer=0;						//発射した弾の現在のパス
	long		shot_time=0;						//発射した時間
	long		alltime=0;							//時間
	byte 		pisuton=50;							//弾の発射間隔 50以上で効果あり						☆ミ仮決定☆ミ
	byte 		tanima=7;							//弾の上下の隙間
	byte		plmspeed=8;							//プレイヤーの速さ
	byte		plmspeedl=3;						//低速移動時の速さ
	boolean		dio=false;							//trueで時間停止 falseで通常
	int			towardco=0;							//敵の登場カウント
	int			stoppos;							//posの保存
	int			elmoveco;							//エリーゼの移動にかかるフレーム数
	int			elpx;								//エリーゼが1フレームでx軸方向に移動する値
	int			elpy;								//エリーゼが1フレームでy軸方向に移動する値
	boolean		upper=true;							//trueでエリーゼは上昇しているfalseでしていない
	int			elspointer=0;						//エリーゼショットポインター
	int			howattack=0;						//何回攻撃したか
	int			zyotai;								//前回のエリーゼの状態
	int			twistcolor1;						//エリーゼ回転弾幕時の色設定
	int			twistcolor2;						//			〃
	//落下物
	byte		fall_count=0;						//降ってくる間隔 0でtrue
	int			fall_pointer=0;						//落下物の現在のパス
	int			wind_count=0;						//風が変わる間隔 	〃
	int			windstrength=0;						//風の強さ
	
	/*
	(注*1)Mathライブラリを用いない角度計算法 こちらのほうが軽量な気がする(未検証)
	
	
	*/
	
	BG bg = new BG(); 								//背景関係リテラル保存先
	Falling[] fall = new Falling[100];				//雪等降ってくるものオブジェクト
	GetSE se = new GetSE();							//効果音の呼び出し
	gameplayer pl = new gameplayer();				//プレイヤーの登録
	ELISE el = new ELISE();							//エリーゼの登録
	PLAYER_SHOT[] pls = new PLAYER_SHOT[200];		//プレイヤーの弾のオブジェクト
	ELISE_SHOT[] els = new ELISE_SHOT[500];			//エリーゼの弾のオブジェクト
	NEGA_ELISE_SHOT nes = new NEGA_ELISE_SHOT();	//エリーゼの弾のネガ格納先
	GetBGM bgm = new GetBGM();						//bgmの呼び出し
	boolean 	open1=true;							//seを鳴らしたかどうか
	boolean	 	open2=true;							//		〃
	Random rnd = new Random();  					//x = rnd.nextInt(a) + b; bからa-1+bまでの値を出力
	AffineTransform at = new AffineTransform(); 	//回転処理に使用
	
	public static void main(String args[]) 
	{
		new KMHOTF01();
	}
	
	    
    public KMHOTF01()					// Constructor
    {   super("Image View");
		addKeyListener(this);
		//弾幕等のインスタンス化
		for (byte i=0;i<99;i++){
			fall[i] = new Falling();
		}
		for (int i=0;i<199;i++){
			pls[i] = new PLAYER_SHOT();
		}
		for (int i=0;i<499;i++){
			els[i] = new ELISE_SHOT();
		}
		//標準数学関数を使わないためのメモリ確保
		for (int i=0;i<360;i++){		//0~360まで361個の初期化
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

    
    public void paint(Graphics g)		// Paint Method  描画メソッド
    {   
		//背景
		if (back==null || pos<0)    return;
        buffer= back.getGraphics();
        if (buffer==null)   		return;
        size = getSize();
			switch(game_step){
			case 0:
				//背景
				buffer.drawImage(bg.st3_4(),0,0,this);
		        buffer.drawImage(bg.st3_3(),0,0,size.width,size.height,bg.getbgcount1(),0,bg.getbgcount1()+1040,size.height,this);
				buffer.drawImage(bg.st3_2(),0,0,size.width,size.height,bg.getbgcount2(),0,bg.getbgcount2()+1040,size.height,this);
				buffer.drawImage(bg.st3_1(),0,0,size.width,size.height,pos,0,pos+1040,size.height,this);
				//落下物
				for (byte i=0;i<=fall_pointer;i++){
					buffer.drawImage(fall[i].getimg(),fall[i].getx(),fall[i].gety(),this);
				}
				buffer.drawImage(bg.gettitle(),275,50,this);
				buffer.drawImage(bg.gettitlep(),275,50,this);
				//press enter描画
				if ((title_pressed>=100)&&(title_pressed<=151)){
					buffer.drawImage(bg.getinpressed((title_pressed-100)*5),320,390,this);
				}else if ((title_pressed<=251)&&(title_pressed>=200)){
					buffer.drawImage(bg.getinpressed((title_pressed-200)*5),320,390,this);
				}
				//暗転関係処理
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
										buffer.drawString(prestr,30,40);//デバッグ*/
					}else if ((title_pressed<=251)&&(title_pressed>=200)){
						buffer.drawImage(bg.getinpressed((title_pressed-200)*5),320,390,this);
										/*String prestr = String.valueOf((title_pressed-200)*5);
										buffer.setColor(Color.red);
										buffer.drawString(prestr,30,40);//デバッグ*/
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
      			//at.setToRotation(Math.toRadians(loadrevo), x, y);		Mathライブラリを用いた角度計算法
				at.setToRotation((loadrevo/180)*pi, x, y);				//(注*1)Mathライブラリを用いない角度計算法 こちらのほうが軽量な気がする(未検証)
				buffer2.translate(800, 350);
      			buffer2.drawImage(bg.loadback(), at, this);
				buffer2.translate(-800, -350);
				break;
			case 4:
				//背景
				if(dio){
					buffer.setColor(new Color(0, 0, 0, 255));
					buffer.fillRect(0,0,1040,520);
					//仮背景
					buffer.drawImage(bg.nst3_4(),0,0,this);			
		        	buffer.drawImage(bg.nst3_3(),0,0,size.width,size.height,bg.getbgcount1(),0,bg.getbgcount1()+1040,size.height,this);
					buffer.drawImage(bg.nst3_2(),0,0,size.width,size.height,bg.getbgcount2(),0,bg.getbgcount2()+1040,size.height,this);
					buffer.drawImage(bg.nst3_1(),0,0,size.width,size.height,stoppos,0,stoppos+1040,size.height,this);
				}else{
					buffer.setColor(new Color(255, 255, 255, 255));
					buffer.fillRect(0,0,1040,520);
					//仮背景
					buffer.drawImage(bg.st3_4(),0,0,this);			
		        	buffer.drawImage(bg.st3_3(),0,0,size.width,size.height,bg.getbgcount1(),0,bg.getbgcount1()+1040,size.height,this);
					buffer.drawImage(bg.st3_2(),0,0,size.width,size.height,bg.getbgcount2(),0,bg.getbgcount2()+1040,size.height,this);
					buffer.drawImage(bg.st3_1(),0,0,size.width,size.height,pos,0,pos+1040,size.height,this);
				}
				
				if (title_counter <=127){						//ブラックイン
					buffer.setColor(new Color(0, 0, 0, 255-title_counter*2));
					buffer.fillRect(0,0,1040,520);
					title_counter+=1;
				}else if(title_counter<=400){					//She comes towards you
					buffer.drawImage(bg.getshe(),245,80,this);
					title_counter+=1;
				}else if(title_counter<=1440){					//エネミーロゴ表示
					buffer2 = (Graphics2D)buffer;
					w = bg.getelsaw().getWidth(this);
	      			h = bg.getelsaw().getHeight(this);
	      			x = w / 2;
	      			y = h / 2;
	      			//at.setToRotation(Math.toRadians(title_counter*5), x, y);
					at.setToRotation(((double)title_counter*5/180)*pi, x, y);		//(注*1)		
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
				}else{		//ここからゲーム本編
					if(dio){//時間停止中
						//弾丸処理
						for (int i=0;i<shot_pointer;i++){
							buffer.drawImage(bg.negaplshot1(),pls[i].getx(),pls[i].gety(),this);
						}
						//敵処理
						if (towardco<300){	
								buffer.drawImage(el.getelise1(dio),el.getx(),el.gety(),this);
						}else{
							buffer.drawImage(el.getimg(0,dio),el.getx(),el.gety(),this);
						}
						//敵弾丸処理
						for (int i=0;i<elspointer;i++){
							buffer.drawImage(nes.getimg(els[i].getcolor(),els[i].getsize()),(int)els[i].getx(),(int)els[i].gety(),this);
						}
					}else{
						//弾丸処理
						for (int i=0;i<shot_pointer;i++){
							buffer.drawImage(pls[i].getimg(),pls[i].getx(),pls[i].gety(),this);
						}
						//敵処理
						if (towardco<300){	
								buffer.drawImage(el.getelise1(dio),el.getx(),el.gety(),this);
						}else{
							buffer.drawImage(el.getimg(0,dio),el.getx(),el.gety(),this);
						}
						//敵弾丸処理
						for (int i=0;i<elspointer;i++){
							buffer.drawImage(els[i].getimg(),(int)els[i].getx(),(int)els[i].gety(),this);
						}
					}
					
					//buffer.drawImage(bg.invisib(negative),0,0,this);
					//キャラクター処理
					buffer.drawImage(pl.getimg(move),pl.getx(),pl.gety(),this);
					//ライフサークルプレイヤー
					double hpper=Math.floor(pl.gethp()*1800/pl.getmaxhp());				//体力の割合
					int graphper=(int)hpper/10;
					String sthp = String.valueOf(pl.gethp());
					double crper=Math.floor(pl.getcr()*1800/pl.getmaxcr());				//狂気の割合
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
					//ライフサークルエリーゼ
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
		//落下物
		/*for (byte i=0;i<=fall_pointer;i++){
				buffer.drawImage(fall[i].getimg(),fall[i].getx(),fall[i].gety(),this);
		}*/
		//透過処理基本
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
	
	public void graphicC()		//画像関係演算処理
	{
		switch(game_step){
		case 0:
			//背景
			bg.setbgcount1(bg.getbgcount1()+3);
			if (bg.getbgcount1()>1040)	bg.setbgcount1(0);
			bg.setbgcount2(bg.getbgcount2()+2);
			if (bg.getbgcount2()>1040)	bg.setbgcount2(0);
			if(title_counter==127){
				game_step=1;	bgm.gotitle();
			}
			//落下物
			if ((fall_count<=0)&&(fall_pointer!=98)){
				fall[fall_pointer].setpara(rnd.nextInt(1030)+10,-14,rnd.nextInt(3) + 1,rnd.nextInt(1) + 8);
				fall_pointer+=1;
				fall_count=4;					//雪の量
			}
			for (byte i=0;i<=fall_pointer;i++){
				fall[i].sety(fall[i].gety()+fall[i].getspeed());
				//fall[i].setx(fall[i].getx()+windstrength);//風の処理
				if(fall[i].gety()>=520){
					fall[i].setpara2(fall[fall_pointer-1].getx(),fall[fall_pointer-1].gety(),fall[fall_pointer-1].getspeed(),fall[fall_pointer-1].getimg());
					fall[fall_pointer-1].fallingdown();
					fall_pointer-=1;
				}
			}
			if (fall_count>0){
				fall_count-=1;
			}
			//press enter処理
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
			//落下物
			if ((fall_count<=0)&&(fall_pointer!=98)){
				fall[fall_pointer].setpara(rnd.nextInt(1030)+10,-14,rnd.nextInt(3) + 1,rnd.nextInt(1) + 8);
				fall_pointer+=1;
				fall_count=4;					//雪の量
			}
			for (byte i=0;i<fall_pointer;i++){
				fall[i].sety(fall[i].gety()+fall[i].getspeed());
				//fall[i].setx(fall[i].getx()+windstrength);//風の処理
				if(fall[i].gety()>=520){
					fall[i].setpara2(fall[fall_pointer-1].getx(),fall[fall_pointer-1].gety(),fall[fall_pointer-1].getspeed(),fall[fall_pointer-1].getimg());
					fall[fall_pointer-1].fallingdown();
					fall_pointer-=1;
				}
			}
			if (fall_count>0){
				fall_count-=1;
			}
			//press enter処理
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
			//落下物
			if ((fall_count<=0)&&(fall_pointer!=98)){
				fall[fall_pointer].setpara(rnd.nextInt(1030)+10,-14,rnd.nextInt(3) + 1,rnd.nextInt(1) + 8);
				fall_pointer+=1;
				fall_count=4;					//雪の量
			}
			for (byte i=0;i<=fall_pointer;i++){
				fall[i].sety(fall[i].gety()+fall[i].getspeed());
				//fall[i].setx(fall[i].getx()+windstrength);//風の処理
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
				if (title_pressed==1){				//次の,キャラ選択へ初期値を送る
					title_pressed=0;
					frame_position=2;
					pl.setimg(1);//maisie
				}
			}
			
			//キャラフレーム等移動
			if (frame_position>=2){
				frame_position+=30;
				if(frame_position>=775)	frame_position=0;
			}
			//キャラクター移動 今回に限りキャラフレームと同期
			if (frame_position!=1){
				pl.setx(frame_position);
			}
			break;
		case 3:
			if (loadrevo==360) {
				game_step=4;
				title_counter=1441;										//タイトルカウンターは0に戻すこと デバッグ作業時のみ1441に設定
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
				//弾丸処理
				if (el.gethp()>80000){
					zyotai=0;
				}else if(el.gethp()>70000){
					zyotai=1;
				}else{
					zyotai=2;
				}
				for (int i=0;i<shot_pointer;i++){
					pls[i].setx(pls[i].getx()+pls[i].getspeed());
					//自弾の敵への当たり判定
					double at1;
					double at2;
					double at3;
					at1=0;
					at2=0;
					at3=0;
					if (Math.pow((double)Math.abs(pls[i].getcx()-el.getcx()),2)+ Math.pow((double)Math.abs(pls[i].getcy()-el.getcy()),2)<=Math.pow((double)(pls[i].getr()+el.getcr()),2)){
						shot_pointer-=1;		//体
						pls[i].setx(pls[shot_pointer].getx());
						pls[i].sety(pls[shot_pointer].gety());
						el.sethp(el.gethp()-15);//15ダメージ
						if(!dio) se.gobasi();
						i-=1;
						continue;
					}
					at1=0;
					at2=0;
					at3=0;
					if (Math.pow((double)Math.abs(pls[i].getcx()-el.getfx()),2)+ Math.pow((double)Math.abs(pls[i].getcy()-el.getfy()),2)<=Math.pow((double)(pls[i].getr()+el.getfr()),2)){
						shot_pointer-=1;		//頭
						pls[i].setx(pls[shot_pointer].getx());
						pls[i].sety(pls[shot_pointer].gety());
						el.sethp(el.gethp()-30);//30ダメージ
						if(!dio) se.gobasi();
						i-=1;
						continue;
					}
					//場外判定
					if((pls[i].getx()>=1040)&&(i!=shot_pointer-1))
						{shot_pointer-=1;pls[i].setx(pls[shot_pointer].getx());pls[i].sety(pls[shot_pointer].gety());}
				}
				if(el.gethp()<70000){
					if(zyotai!=2) el.setform(0);
				}else if(el.gethp()<80000){
					if(zyotai!=1) el.setform(0);
				}
				
				//dio処理
				if(dio){
					//CrazyPointの減少と時止めの終了
					pl.setcr(pl.getcr()-1123);
					if (pl.getcr()<=0){
						pl.setcr(0);
						dio=false;
						bgm.reelise();
						se.stopstop();
					}
				}else{
					//CrazyPointの回復
					if (pl.getcr()<pl.getmaxcr()){
						pl.setcr(pl.getcr()+117);
						if (pl.getcr()>=pl.getmaxcr()) pl.setcr(pl.getmaxcr());
					}
					//敵関係処理
					if (towardco<300){			//敵の登場シーン
						el.setx(1040-towardco);
						towardco+=2;
					}else{						//敵の行動等
						//敵動作処理
						if(el.gethp()>=80000){					//elise
							switch(el.getform()){
								case 0:					//次の行動選択
									el.setform(rnd.nextInt(2)+1);
									el.setformcount(0);
									howattack=0;
									break;
								case 1:				//上移動後＆上下移動＆正面直進乱射＆上下円状追尾
									if(el.getformcount()==0){			//移動方向決定
										int rndx=rnd.nextInt(100)+700;
										elmoveco=(int)Math.ceil(Math.sqrt(Math.pow((double)Math.abs(el.getx()-rndx),2) + Math.pow((double)Math.abs(el.gety()),2))/10);
										elpx=(int)((rndx-el.getx())/elmoveco);
										elpy=(int)((-el.gety())/elmoveco);
										el.setformcount(el.getformcount()+1);
									}else if(el.getformcount()<=79){	//移動
										if (el.getformcount()<=elmoveco){
											el.setx(el.getx()+elpx);
											el.sety(el.gety()+elpy);
										}
										upper=false;
										el.setformcount(el.getformcount()+1);
									}else{								//攻撃開始
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
										if (el.getformcount()%4==0){		//4フレームに一回打ってくる
												els[elspointer].setall(el.getcx(),el.getcy(),-5,0,1,0);
												elspointer+=1;
										}
										el.setformcount(el.getformcount()+1);
										if(el.getformcount()==350)	el.setform(0);
									}
									break;
								case 2:			//ランダム移動後＆円状追尾
									if(el.getformcount()==0){			//移動方向決定
										int rndx=rnd.nextInt(520)+520;
										int rndy=rnd.nextInt(320)+100;
										elmoveco=(int)Math.ceil(Math.sqrt(Math.pow((double)Math.abs(el.getcx()-rndx),2) + Math.pow((double)Math.abs(el.getcy()-rndy),2))/10);
										elpx=(int)((rndx-el.getcx())/elmoveco);
										elpy=(int)((rndy-el.getcy())/elmoveco);
										el.setformcount(el.getformcount()+1);
									}else if(el.getformcount()<=70){	//移動
										if (el.getformcount()<=elmoveco){
											el.setx(el.getx()+elpx);
											el.sety(el.gety()+elpy);
										}
										el.setformcount(el.getformcount()+1);
									}else{								//攻撃開始
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
								case 0:					//次の行動選択
									el.setform(rnd.nextInt(2)+1);
									el.setformcount(0);
									howattack=0;
									break;
								case 1:				//上移動後＆上下移動＆正面追尾乱射＆上下円状追尾
									if(el.getformcount()==0){			//移動方向決定
										int rndx=rnd.nextInt(100)+700;
										elmoveco=(int)Math.ceil(Math.sqrt(Math.pow((double)Math.abs(el.getx()-rndx),2) + Math.pow((double)Math.abs(el.gety()),2))/10);
										elpx=(int)((rndx-el.getx())/elmoveco);
										elpy=(int)((-el.gety())/elmoveco);
										el.setformcount(el.getformcount()+1);
									}else if(el.getformcount()<=79){	//移動
										if (el.getformcount()<=elmoveco){
											el.setx(el.getx()+elpx);
											el.sety(el.gety()+elpy);
										}
										upper=false;
										el.setformcount(el.getformcount()+1);
									}else{								//攻撃開始
										if (upper){
											el.sety(el.gety()-17);
										}else{
											el.sety(el.gety()+17);
										}
										if ((el.gety()<-125)||(el.gety()>412)) {		//バグる可能性あり 条件＝攻撃開始時にy座標が412より大きい(理論的にはほとんどありえない状況)
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
										if (el.getformcount()%2==0){		//4フレームに一回打ってくる
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
								case 2:				//下移動後＆上下移動＆円状追尾乱射
									if(el.getformcount()==0){			//移動方向決定
										int rndx=rnd.nextInt(100)+700;
										elmoveco=(int)Math.ceil(Math.sqrt(Math.pow((double)Math.abs(el.getx()-rndx),2) + Math.pow((double)Math.abs(el.gety()),2))/10);
										elpx=(int)((rndx-el.getx())/elmoveco);
										elpy=(int)((el.gety())/elmoveco);
										el.setformcount(el.getformcount()+1);
									}else if(el.getformcount()<=79){	//移動
										if (el.getformcount()<=elmoveco){
											el.setx(el.getx()+elpx);
											el.sety(el.gety()+elpy);
										}
										upper=false;
										el.setformcount(el.getformcount()+1);
									}else{								//攻撃開始
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
										if (el.getformcount()%15==0){		//15フレームに一回打ってくる
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
												
												/*els[elspointer].setpx(speeds*Math.cos(((180/((double)hows/2)*i-j)/180)*pi));				//(注*1)軽量化1段階目
												els[elspointer+1].setpx(-(speeds*Math.cos(((180/((double)hows/2)*i-j)/180)*pi)));
												els[elspointer].setpy(speeds*Math.sin(((180/((double)hows/2)*i-j)/180)*pi));	
												els[elspointer+1].setpy(-(speeds*Math.sin(((180/((double)hows/2)*i-j)/180)*pi)));*/
												
												els[elspointer].setpx(speeds*cosine[(180/(hows/2)*i-j)%360]);								//(注*1)軽量化2段階目実験成功
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
								case 0:					//次の行動選択
									el.setform(rnd.nextInt(1)+1);
									el.setformcount(0);
									howattack=0;
									break;
								case 1:			//移動アンド追尾
									if(el.getformcount()==0){			//移動方向決定
										int rndx=rnd.nextInt(520)+520;
										int rndy=rnd.nextInt(320)+100;
										elmoveco=(int)Math.ceil(Math.sqrt(Math.pow((double)Math.abs(el.getcx()-rndx),2) + Math.pow((double)Math.abs(el.getcy()-rndy),2))/10);
										elpx=(int)((rndx-el.getcx())/elmoveco);
										elpy=(int)((rndy-el.getcy())/elmoveco);
										el.setformcount(el.getformcount()+1);
									}else if(el.getformcount()<=70){	//移動
										if (el.getformcount()<=elmoveco){
											el.setx(el.getx()+elpx);
											el.sety(el.gety()+elpy);
										}
										el.setformcount(el.getformcount()+1);
										if (el.getformcount()==71){
											twistcolor1=rnd.nextInt(5)+1;
											twistcolor2=rnd.nextInt(5)+1;
										}
									}else{								//攻撃開始
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
						}//動作終了
						//敵弾丸処理
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
							if(true){	//複合当たり判定方式
								if(Math.pow((double)Math.abs(els[i].getcx()-pl.getcx()),2)+ Math.pow((double)Math.abs(els[i].getcy()-pl.getcy()),2)<=els[i].getpowerr()){	//二段階目
									if (Math.pow((double)Math.abs(els[i].getcx()-pl.getcx()),2)+ Math.pow((double)Math.abs(els[i].getcy()-pl.getcy()),2)<=Math.pow((double)(els[i].getr()+1),2)){//三段階目
										elspointer-=1;		//体
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
			//仮背景処理
			bg.setbgcount1(bg.getbgcount1()+3);
			if (bg.getbgcount1()>1040)	bg.setbgcount1(0);
			bg.setbgcount2(bg.getbgcount2()+2);
			if (bg.getbgcount2()>1040)	bg.setbgcount2(0);
			stoppos=pos;
			break;
		}
		//落下物	
		/*if ((fall_count<=0)&&(fall_pointer!=98)){
			fall[fall_pointer].setpara(rnd.nextInt(1030)+10,-14,rnd.nextInt(3) + 1,rnd.nextInt(2) + 6);
			fall_pointer+=1;
			fall_count=5;
		}
		for (byte i=0;i<=fall_pointer;i++){
			fall[i].sety(fall[i].gety()+fall[i].getspeed());
			//fall[i].setx(fall[i].getx()+windstrength);//風の処理
			if(fall[i].gety()>=520){
				fall[i].setpara2(fall[fall_pointer-1].getx(),fall[fall_pointer-1].gety(),fall[fall_pointer-1].getspeed(),fall[fall_pointer-1].getimg());
				fall[fall_pointer-1].fallingdown();
				fall_pointer-=1;
			}
		}
		if (fall_count>0){
			fall_count-=1;
		}*/
		//落下物に吹く風
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
				//キー判定
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
					pls[shot_pointer].setcy(pl.getcy()+tanima);				//プレイヤーからの距離
					pls[shot_pointer+1].setcy(pl.getcy()-tanima);
					shot_pointer+=2;
					if (!dio) se.goshot2();
				}
				if((key_t[6]==1)&&(!push)){			//時間停止
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
	
	
	
	
	
	
	    public void Update()				// 更新処理
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
	
	public void soundeffect(){				//効果音関係処理
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