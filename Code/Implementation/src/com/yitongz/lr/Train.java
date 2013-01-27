package com.yitongz.lr;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.StringTokenizer;
import java.util.ArrayList;
public class Train{
	private String train_url;
	private String test_url;
	private String result_url;
	private ParameterOperator operator;
	private String mode;
	public Train(String data_url, String conf_url){
		try{
		BufferedReader br=new BufferedReader(new FileReader(new File(data_url)));
		String s=br.readLine();
		train_url=s.substring(s.indexOf('=')+1,s.length());

		s=br.readLine();
		test_url=s.substring(s.indexOf('=')+1,s.length());

		s=br.readLine();
		Parameter.c=Double.parseDouble(s.substring(s.indexOf('=')+1, s.length()));

		br=new BufferedReader(new FileReader(new File(conf_url)));
		s=br.readLine();
		result_url=s.substring(s.indexOf('=')+1,s.length());

		s=br.readLine();
		Parameter.x0=Double.parseDouble(s.substring(s.indexOf('=')+1, s.length()));

		s=br.readLine();
		if (Integer.parseInt(s.substring(s.indexOf('=')+1,s.length()))==0 )
			mode="stochastic";
		else 
			mode="batch";

		br.close();
		}catch(IOException e){
			e.printStackTrace();
		}

		operator=Parameter.init_Parameters();
		start_train();
		//check_w();
		start_test();
	}
	private void start_train(){
		try{
		BufferedReader br=new BufferedReader(new FileReader(new File(train_url)));
		String s=null;
		DocVector doc=null;
		String tmp=null;
		int len=0;

			ArrayList<DocVector> doc_list=new ArrayList<DocVector>();
			while((s=br.readLine())!=null && len>-1){
				StringTokenizer st=new StringTokenizer(s);
				doc=new DocVector(Integer.parseInt(st.nextToken()));
				while(st.hasMoreTokens()){
					tmp=st.nextToken();
					int split=tmp.indexOf(':');
					doc.addX(Integer.parseInt(tmp.substring(0,split)),  Double.parseDouble(tmp.substring(split+1,tmp.length())) );
				}
				doc_list.add(doc);
				len++;
			}
			operator.evolveW(doc_list,mode);

		br.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	private void start_test(){
		try{
		BufferedReader br=new BufferedReader(new FileReader(new File(test_url)));
		String s=null;
		DocVector doc=null;
		String tmp=null;
		BufferedWriter bw=new BufferedWriter(new FileWriter(new File(result_url)));
		while((s=br.readLine())!=null){
			StringTokenizer st=new StringTokenizer(s);
			String old_id=st.nextToken();
			doc=new DocVector(Integer.parseInt(old_id));
			while(st.hasMoreTokens()){
				tmp=st.nextToken();
				int split=tmp.indexOf(':');
				doc.addX(Integer.parseInt(tmp.substring(0,split)),  Double.parseDouble(tmp.substring(split+1,tmp.length())) );
			}
			operator.checkCategory(doc);
			System.out.println(doc.getCategory());
			bw.write(doc.getCategory()+" "+old_id);
			bw.newLine();
		}
		br.close();
		bw.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	private void check_w(){
		for (int k=1;k<Parameter.categories;k++){
			System.out.println(k+"=============");
			for (int i=0;i<Parameter.dimensions;i++){
				System.out.print("w"+"("+i+")"+operator.getW(k,i)+"  ");
			}
		}
	}
}