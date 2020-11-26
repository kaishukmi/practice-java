import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;
import java.applet.*;

class GetSE
{
	MUSIC ido;
	MUSIC susumu;
	MUSIC modoru;
	MUSIC kettei;
	MUSIC opening1;
	MUSIC opening2;
	MUSIC mecha;
	MUSIC stop;
	MUSIC[] shot1 = new MUSIC[20];
	MUSIC[] shot2 = new MUSIC[20];
	MUSIC[] shot3 = new MUSIC[20];
	MUSIC[] shyuin = new MUSIC[20];
	MUSIC[] basi = new MUSIC[20];
	int shot1co=0;
	int shot2co=0;
	int shot3co=0;
	int shyuinco=0;
	int basico=0;
	GetSE()
	{
		ido =new MUSIC(".\\soundeffect\\ido.wav");
		susumu = new MUSIC(".\\soundeffect\\susumu.wav");
		modoru = new MUSIC(".\\soundeffect\\modoru.wav");
		kettei = new MUSIC(".\\soundeffect\\kettei.wav");
		opening1 = new MUSIC(".\\soundeffect\\title\\1st.wav");
		opening2 = new MUSIC(".\\soundeffect\\title\\2nd.wav");
		mecha =	new MUSIC(".\\soundeffect\\mecha.wav");
		stop =	new MUSIC(".\\soundeffect\\shot\\tozyo2.wav");
		for(byte i=0;i<20;i++){
			shot1[i] =	new MUSIC(".\\soundeffect\\shot\\shot1.wav");
			shot2[i] =	new MUSIC(".\\soundeffect\\shot\\shot2.wav");
			shot3[i] =	new MUSIC(".\\soundeffect\\shot\\shot3.wav");
			shyuin[i] =	new MUSIC(".\\soundeffect\\shot\\shyuin.wav");
			basi[i] =	new MUSIC(".\\soundeffect\\shot\\basi.wav");
		}
	}
	
	void goido(){
		ido.Play();
	}
	void goshot1(){
		shot1[shot1co].Play();
		shot1co += 1;
		if (shot1co==20) shot1co=0;
	}
	void goshot2(){
		shot2[shot2co].sPlay(0.2);
		shot2co += 1;
		if (shot2co==20) shot2co=0;
	}
	void goshot3(){
		shot3[shot3co].Play();
		shot3co += 1;
		if (shot3co==20) shot3co=0;
	}
	void goshyuin(){
		shyuin[shyuinco].Play();
		shyuinco += 1;
		if (shyuinco==20) shyuinco=0;
	}
	void gobasi(){
		basi[basico].sPlay(0.1);
		basico += 1;
		if (basico==20) basico=0;
	}
	
	void gosusumu(){
		susumu.Play();
	}
	void gomodoru(){
		modoru.Play();
	}
	void gokettei(){
		kettei.Play();
	}
	void goopening1(){
		opening1.Play();
	}
	void goopening2(){
		opening2.Play();
	}
	void gomecha(){
		mecha.Play();
	}
	void gostop(){
		stop.Play();
	}
	void stopstop(){
		stop.Stop();
	}
}