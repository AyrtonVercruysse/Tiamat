package vub.templates;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.mt4j.MTAndroidApplication;

import android.content.Context;
import android.os.Environment;

import vub.ast.Node;
import vub.tiamat.StartTiamat;

public class TemplateWriter{

	public void write() {
		try {
			//StartTiamat.reOpen();
			String XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <templates>" + vub.tiamat.Tiamat.main.toXML()+ "</templates>";
			System.out.println("TemplateWriter"+ XML);
			StartTiamat.out.write("XML:" + XML+ "yr");
			StartTiamat.out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	} 

}
