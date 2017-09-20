package io.practice;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class Solution_v1{
	public static void main(String[] args){
		System.exit(processFolder(args));

	}
	public static int processFolder(String[] args){
			try{
				RenameFolder rf = new RenameFolder(args);
				boolean validinput=rf.isValidInput(args)&&rf.validateFile(args);
				if(validinput){//validate user input and files
		//			rf.changeName(args);//change folder names
					rf.changeEnv(args);//change environmental variables

				}
			}catch(IllegalArgumentException e){
				System.out.println("Error occurred:" + e.getMessage());
				printUsage();
				return 1;
			}catch(IllegalFileException e){
				System.out.println("Error occurred:" + e.getMessage());
				return 2;
			}catch(Exception e){
				System.out.println("Error occurred:" + e.getMessage());
				return 3;
			}
			return 0;
		}
	public static void printUsage(){
		System.out.println("Please input original directory and new folder.");
		System.out.println("If there are space in path names, please enclose with \"\". ");
	}
}

class RenameFolder{//Contains all methods in one class
	public String args[];
	public RenameFolder(String args[]){
		this.args = args;
	}
	public boolean isValidInput(String args[])throws IllegalArgumentException{
		if(args==null){//no input
			throw new IllegalArgumentException("No input is available");
		}
		if(args.length!=2){//wrong number of input
			throw new IllegalArgumentException("Wrong number of arguments");
		}
		System.out.println("Your original directory " + args[0] + "\n" + "Your new folder " + args[1]);
		return true;
	}
	
	public boolean validateFile(String args[])throws Exception{//validate files
			RenamePath rp = new RenamePath(args);
			System.out.println(rp.oldpathstr + "and" + rp.newpathstr);

			//form new directory
			if(!Files.exists(rp.oldpath)){
				throw new IllegalFileException("The old folder does not exist");
			}
			if(!Files.isDirectory(rp.oldpath)){
				throw new IllegalFileException("Please input folder name,not file");
			}
			if(Files.exists(rp.newpath)){
				throw new IllegalFileException("The new folder already exists");
			}
			if(rp.oldpath.equals(rp.newpath)){
				throw new IllegalFileException("The old folder is the same as the new folder");
			}
			System.out.println(rp.oldpathstr + "and" + rp.newpathstr);
			// Files are OK.
			return true;
}
	public boolean changeName(String args[])throws IllegalFileException{//change file name
		RenamePath rp = new RenamePath(args);
		try{Files.move(rp.oldpath,rp.newpath);
		}catch(Exception e){
		throw new IllegalFileException("Renaming is not successful");
		}
		return true;
	}
	public boolean changeEnv(String args[]) throws Exception{//change environmental variable
			//Prepare output .bat file
			File batfile = new File("c:" + File.separator + "interview" + File.separator + "result.bat" );
			if(!batfile.getParentFile().exists()){
				batfile.getParentFile().mkdir();
			}
			PrintStream ps = new PrintStream(batfile);//stream for bat file
			
			//Form full new folder directory
			RenamePath rp = new RenamePath(args);
			
			//read environmental variables
			Map<String,String> env = System.getenv();
			for(String vaname : env.keySet()){//visit each key-value
				//check possible value with old folder
				if(env.get(vaname).contains(rp.oldpathstr)){
					boolean flag = false; //if replacement really occurred

					//split value into String array.
					String[] dirs = env.get(vaname).split(";");
//					if(oldfolder.indexOf(";")!=-1){//split in Windows
//						dirs = env.get(vaname).split(";");//split value with ";"
//					}else{//split in Linux
//						dirs = env.get(vaname).split(":");//split value with ":"
//					}
					
					// Scan all directory, finish replacement
					StringBuilder newdirs = new StringBuilder();
					for(String dir:dirs){//visit each directory
						// Path.startsWith to handle two cases: 1. same folder 2. sub folder
						Path dirpath = Paths.get(dir);
						if(dirpath.startsWith(rp.oldpathstr)){
							newdirs.append(dir.replace(rp.oldpathstr,rp.newpathstr)+";");
							flag = true;
						}else{
							newdirs.append(dir+";");
						}
					}
					
					//If replacement occurred, write to bat file
					if(flag){
						ps.println("SET " + vaname + "=" + newdirs);
						System.out.format("%s = %s%n",vaname,env.get(vaname));
						System.out.println(newdirs);
					}
//						System.out.println(newdirs);
//					System.out.format("%s = %s%n",vaname,env.get(vaname));
				}
			}
			ps.close();
			return true;
	}
}
class IllegalFileException extends Exception{
	public IllegalFileException(){
		super();
	}
	public IllegalFileException(String msg){
		super(msg);
	}
}
class RenamePath{//Convert and store old path and new path
	public Path oldpath;
	public Path newpath;
	public String oldpathstr;
	public String newpathstr;
	public RenamePath(String args[]){
		this.oldpath = Paths.get(args[0]);
		this.newpath = oldpath.resolveSibling(args[1]);
		this.oldpathstr = oldpath.toString();
		this.newpathstr = newpath.toString();
	}
}





