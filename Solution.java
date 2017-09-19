package io.practice;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Map;

public class Solution{
	public static void main(String[] args){
		System.exit(processFolder(args));

	}
	public static int processFolder(String[] args){
			try{
				RenameFolder rf = new RenameFolder(args);
				if(rf.validateInput(args)&&rf.validateFile(args)){//validate user input and files
		//			rf.changeName(args);//change folder names
					rf.changeEnv(args);//change environmental variables
				}
			}catch(IllegalArgumentException e){
				System.out.println("Error occurred:" + e.getMessage());
				printUsage();
				return 1;
			}catch(Throwable t){
//				t.printStackTrace(e);
				return 2;
			}finally{
				return 3;
			}
		}
	public static void printUsage(){
		System.out.println("Please input origini dir and new folder");
		System.out.println("If there are space, please enclose with ");
	}
}

class RenameFolder{//Contains all methods in one class
	String args[];
	public RenameFolder(String args[]){
		this.args = args;
	}
	public boolean validateInput (String args[])throws Exception{//validate input
		if(args==null){//no input
			throw new IllegalArgumentException("No input is available");
		}
		if(args.length!=2){//wrong number of input
			throw new IllegalArgumentException("Wrong number of arguments");
		}
		System.out.println("Your orginal directory " + args[0] + "\n" + "Your new folder " + args[1]);
		return true;
	}
	public boolean validateFile(String args[])throws Exception{//validate files
		if(this.validateInput(args)){
			//form new directory
			String oldfolder = args[0];//read input from user
			File of = new File(oldfolder); //old folder directory
			String newfolder = of.getParent()+args[1]; //new foler directory
			if(!of.exists()){
				throw new IllegalFileException("Old folder doesnot exist");
			}
			if(!of.isDirectory()){
				throw new IllegalFileException("Please input folder name,not file");
			}
			File nf = new File(newfolder);//new file directory
			if(nf.exists()){
				throw new IllegalFileException("The new folder already exists");
			}
			if(oldfolder==newfolder){
				throw new IllegalFileException("The old folder is the same as the new folder");
			}
		}
		return true;
}
	public boolean changeName(String args[])throws Exception{//change file name
		String oldfolder = args[0];//read input from user
		String newfolder = args[1];
		File of = new File(oldfolder);
		newfolder = of.getParent() + File.separator + newfolder;
		File nf = new File(newfolder);
		if(!of.renameTo(nf)){
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
			File of = new File(args[0]);
			String oldfolder = args[0];
			String newfolder = of.getParent()+args[1];

			//read environmental variables
			Map<String,String> env = System.getenv();
			for(String vaname : env.keySet()){//visit each key-value
				//check possible value with old folder
				if(env.get(vaname).contains(oldfolder)){
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
						// Two cases: 1. same folder 2. sub folder
						if(dir.equals(oldfolder)||(dir.startsWith(oldfolder+File.separator))){
							newdirs.append(dir.replace(oldfolder,newfolder)+";");
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

	public static class IllegalArgumentException extends Exception{
		public IllegalArgumentException(){
			super();
		}
		public IllegalArgumentException(String msg){
			super(msg);
		}
		
	}
	public static class IllegalFileException extends Exception{
		public IllegalFileException(){
			super();
		}
		public IllegalFileException(String msg){
			super(msg);
		}
	}
}




