package com.yitongz.lr;
import java.util.ArrayList;
public interface ParameterOperator{
	public double sigema(double z);
	public double getW(int k, int j);
	public void evolveW(DocVector doc);
	public void evolveW(ArrayList<DocVector> doc,String mode);
	public void checkCategory(DocVector doc);
}