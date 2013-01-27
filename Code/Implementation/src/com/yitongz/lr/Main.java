package com.yitongz.lr;
import com.yitongz.svm.Svm;
/** This is Main Class of the Text Catergorization
*/
public class Main{
	public static void main(String argv[]){
		run_lr(argv);
		//run_svm();  //This is the java code for running the SVM, typically, I will close this off
	}
	public static void run_svm(){
		new Svm();
	}
	private static void run_lr(String argv[]){
		if (argv.length!=2)
			new Train("DATA.txt", "Implementation/CONF.txt");
		else 
			new Train(argv[0],argv[1]);
	}
}