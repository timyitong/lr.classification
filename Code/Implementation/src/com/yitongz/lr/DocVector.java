package com.yitongz.lr;
import java.util.ArrayList;
import java.util.Iterator;
public class DocVector{
	private int category;
	private ArrayList<DocElement> x_list=new ArrayList<DocElement>();
	private double[] x;
	private Iterator <DocElement> iterator=null;
	private DocElement next_doc=null;
	private int next_x_index=0;
	public int x_index=0;
	private double x0=Parameter.x0;
	public DocVector(int category){
		this.category=category;
	}
	public double multiply(double [] w){
		double r=w[0]*x0;
		for (int i=0;i<x_list.size();i++){
			DocElement de=x_list.get(i);
			r+=w[de.index]*de.score;
		}
		return r;
	}
	public double index(int index){
		x_index=index;
		if (index==0){
			iterator=x_list.iterator();
			next_doc=iterator.next();
			next_x_index=next_doc.index;
			return x0;			
		}
		if (index<next_x_index){
			return 0;
		}else if (index==next_x_index){
			DocElement old_doc=next_doc;
			if (iterator.hasNext())
			{	next_doc=iterator.next();
				next_x_index=next_doc.index;
			}else{
				next_x_index=-1;
			}
			return old_doc.score;
		}else{
			return 0;
		}
	}
	public boolean hasNextX(){  // hasNextX(), and getNextX() is made for iterating the only the x>0 directly
		if (next_x_index!=-1)
			return true;
		else 
			return false;
	}
	public double getNextX(){
		return (index(next_x_index));
	}
	public void resetIterator(){
		next_x_index=0;
		next_doc=null;
		iterator=null;
	}
	public int getY(int k){
		return (category==k) ? 1 : 0; 
	}
	public void addX(int x_index,double x_score){
		x_list.add(new DocElement(x_index,x_score));
	}
	public void setCategory(int category){
		this.category=category;
	}
	public int getCategory(){
		return category;
	}
}