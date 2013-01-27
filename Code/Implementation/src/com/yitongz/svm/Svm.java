package com.yitongz.svm;
import java.io.*;
import com.yitongz.lr.Parameter;
import java.util.ArrayList;
import java.util.Iterator;
public class Svm{
	private String c;
	private String train_url;
	private String test_url;
	private String result_url;
	private String svm_url;
	public Svm(){
		try{
		readConfig();
		train();
		test();
		}catch(Exception e){e.printStackTrace();}
	}
	private void readConfig(){
		try{
			BufferedReader br=new BufferedReader(new FileReader(new File("Implementation/SVM.txt")));
			String s=null;
			s=br.readLine();
			train_url=s.substring(s.indexOf('=')+1,s.length());

			s=br.readLine();
			test_url=s.substring(s.indexOf('=')+1,s.length());

			s=br.readLine();
			result_url=s.substring(s.indexOf('=')+1,s.length());

			s=br.readLine();
			c=s.substring(s.indexOf('=')+1, s.length());

			s=br.readLine();
			svm_url=s.substring(s.indexOf('=')+1,s.length());
			br.close();
		}catch(IOException e){e.printStackTrace();}
	}
	private void train(){
		split_input_file();
		for (int i=1;i<=Parameter.categories;i++)
			train(i);
	}
	private void train(int i){
		try{
		Runtime x = Runtime.getRuntime();
		String s=svm_url+"/"+"./svm_learn "+"-c "+c+" "+train_url+".c"+i+" "+train_url+".model"+i;
		Process prcs=x.exec(s);
		prcs.waitFor();

		}catch(Exception e){e.printStackTrace();}
	}
	private void split_input_file(){
		try{
			for (int i=1;i<=Parameter.categories;i++){
				BufferedReader br=new BufferedReader(new FileReader(new File(train_url)));
				BufferedWriter bw=new BufferedWriter(new FileWriter(new File(train_url+".c"+i)));
				String s=null;
				String tag=null;
				while((s=br.readLine())!=null){
					tag=s.substring(0,s.indexOf(' '));
					s=s.substring(s.indexOf(' ')+1, s.length());
					if (Integer.parseInt(tag)==i)
						s="1 "+s;
					else
						s="-1 "+s;
					bw.write(s);
					bw.newLine();
				}
				bw.close();
				br.close();
			}
			for (int i=1;i<=Parameter.categories;i++){
				BufferedReader br=new BufferedReader(new FileReader(new File(test_url)));
				BufferedWriter bw=new BufferedWriter(new FileWriter(new File(test_url+".c"+i)));
				String s=null;
				String tag=null;
				while((s=br.readLine())!=null){
					tag=s.substring(0,s.indexOf(' '));
					s=s.substring(s.indexOf(' ')+1, s.length());
					if (Integer.parseInt(tag)==i)
						s="1 "+s;
					else
						s="-1 "+s;
					bw.write(s);
					bw.newLine();
				}
				bw.close();
				br.close();
			}
		}catch(IOException e){e.printStackTrace();}
	}
	private void test(){
		try{
		for (int i=1;i<=Parameter.categories;i++)
			test(i);
		//Thread.sleep(10000);
		combine_result();
		}catch(Exception e){e.printStackTrace();}
	}
	private void test(int i){
		try{
		Runtime x = Runtime.getRuntime();
		String s=svm_url+"/"+"./svm_classify "+test_url+".c"+i+" "+train_url+".model"+i+" "+result_url+"/"+i+".txt";
		//System.out.println(s);
		Process prcs=x.exec(s);
		prcs.waitFor();
		}catch(Exception e){e.printStackTrace();}
	}
	private void combine_result(){
		try{
		ArrayList <Double> list=new ArrayList<Double>();
		ArrayList <Integer> answer=new ArrayList <Integer>();
		int index=0;
		
		for (int i=1;i<=Parameter.categories;i++){
			BufferedReader br=new BufferedReader(new FileReader(new File(result_url+"/"+i+".txt")));
			String s=null;
			while((s=br.readLine())!=null){
				if (i==1)
					System.out.println(index);
				Double score=Double.parseDouble(s);
				//System.out.println(s);
				if (i==1){
					list.add(score);
					answer.add(i);
					System.out.println(i);
				}
				else{
					Double max_score=list.get(index);
					if (max_score<score){
						list.set(index,score);
						answer.set(index,i);
					}
				}
				index++;
			}
			index=0;
			//br.close();
		}
		Iterator <Integer> it=answer.iterator();
		BufferedWriter bw=new BufferedWriter(new FileWriter(new File(result_url+"/0.txt")));
		BufferedReader br=new BufferedReader(new FileReader(new File(test_url)));
		while(it.hasNext()){
			Integer d=it.next();
			String s=br.readLine();
			s=s.substring(0, s.indexOf(' '));
			bw.write(d.toString()+" "+s);
			bw.newLine();
		}
		bw.close();
		br.close();
		}catch(IOException e){e.printStackTrace();}
	}
}