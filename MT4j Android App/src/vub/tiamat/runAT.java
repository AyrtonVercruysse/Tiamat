package vub.tiamat;
import java.io.*;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

public class runAT extends Activity {
	public void runAT(){
	boolean mExternalStorageAvailable = false;
	boolean mExternalStorageWriteable = false;
	String state = Environment.getExternalStorageState();

	if (Environment.MEDIA_MOUNTED.equals(state)) {
	    // We can read and write the media
	    mExternalStorageAvailable = mExternalStorageWriteable = true;
	   /* File root = Environment.getExternalStorageDirectory();
	    System.out.println("Goowd");
	    
        File file = new File(root, "tomato50.txt");
           try {
                    if (root.canWrite()){
                    FileWriter filewriter = new FileWriter(file);
                    BufferedWriter out = new BufferedWriter(filewriter);
                    out.close();
                }
           
            } catch (IOException e) {
                
            }

           

      */
  	} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	    // We can only read the media
	    mExternalStorageAvailable = true;
	    mExternalStorageWriteable = false;
	    System.out.println("Goowd2");
	} else {
	    // Something else is wrong. It may be one of many other states, but all we need
	    //  to know is we can neither read nor write
	    mExternalStorageAvailable = mExternalStorageWriteable = false;
	    System.out.println("Goowd3");
	}
	}
}
	
