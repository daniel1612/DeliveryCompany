package components;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/** 
 * @author Tal Ohayon - 205597701, Daniel Dahan - 208906909
 * @version 09.05.21
 */

public class WriterTracking {
	
	/**
	 * this class write to a file all trakcing report
	 */
	private static ReadWriteLock rw = new ReentrantReadWriteLock();
	private Lock read = rw.readLock();
	private Lock write = rw.writeLock();
	private static String FileName = "C:\\Users\\falfo\\eclipse-workspace\\H.M3\\src\\tracking.txt";
	private BufferedReader br;
	private BufferedWriter bw;
	private static int n = 1;
	private static final String pID =  "packageID=";
	static String s;

	/**
	 * constructor
	 */
	public WriterTracking() 
	{
		//FileName = fn;
		try {
			//br = new BufferedReader(new FileReader(FileName));
			bw = new BufferedWriter(new FileWriter(FileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * get a string and write it to the file
	 * @param Pid
	 */
	public void Write(String Pid) {
		write.lock();
		try {
			bw = new BufferedWriter(new FileWriter(FileName , true));
			s = n + ". " +  Pid;
			bw.write(s);
			bw.newLine();
			n++;

			bw.newLine();
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		write.unlock();
	}
	
	
	/**
	 * get file method
	 * @return
	 */
	public static String getFileName() {
		return FileName;
	}
	/**
	 *set file name method
	 * @param fileName
	 */
	public static void setFileName(String fileName) {
		FileName = fileName;
	}
	/**
	 * get Track to write method
	 * @return
	 */
	public static String GetTrackToWrite()
	{
		return s;
	}
}
