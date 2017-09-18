package io.interview.practice;
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
			String originalfolder = args[0];//read input from user
			String newfolder = args[1];
			
			File ofs = new File(originalfolder); //old file directory
			if(!ofs.exists()){
				System.out.println("folder doesnot exist");
				return false;
			}
			if(!ofs.isDirectory()){
				System.out.println("pls input directory,not file");
				return false;
			}
			File nfs = new File(newfolder);//new file directory
			if(nfs.exists()){
				System.out.println("the new file already exists");
				return false;
			}
		}
		return true;
			
/*			try{
				
			}catch(){
				
			}finally{
				
			}*/

		
	}
	public boolean changeName(String args[]){//change file name
		String originalfolder = args[0];//read input from user
		String newfolder = args[1];
		File ofs = new File(originalfolder);
		if(ofs.getParentFile().exists()){
			newfolder = ofs.getParent() + File.separator + newfolder;
			File nfs = new File(newfolder);
//			ofs.renameTo(nfs);
			return true;
		}else{
			return false;
		}

	}
	public boolean changeEnv(String args[]){//change environmental variable
			File batfile = new File("c:" + File.separator + "interview" + File.separator + "result.bat" );
			if(!batfile.getParentFile().exists()){
				batfile.getParentFile().mkdir();
			}
			File of = new File(args[0]);
			String oldfolder = args[0];
			String newfolder = of.getParent()+args[1];
			try{
				PrintStream ps = new PrintStream(batfile);
				Map<String,String> env = System.getenv();
				for(String vaname : env.keySet()){//visit each key-value
					if(env.get(vaname).contains(oldfolder)){//find value with target
						boolean flag = false; //indicate replacement
						System.out.format("%s=%s%n",vaname,env.get(vaname));
						String[] dirs = env.get(vaname).split(";");//split value with ";"
						StringBuilder newdirs = new StringBuilder();
						for(String dir:dirs){//visit each directory
							if(dir.equals(oldfolder)||(dir.startsWith(oldfolder+File.separator))){
								newdirs.append(dir.replace(oldfolder,newfolder)+";");
								flag = true;
							}else{
								newdirs.append(dir+";");
							}
						}
						if(flag){
							ps.println("SET " + vaname + "=" + newdirs);
							System.out.format("%s=%s%n",vaname,env.get(vaname));
							System.out.println(newdirs);
						}
//						System.out.println(newdirs);
					}
				}
				ps.close();
			}catch(FileNotFoundException e){
				System.exit(2);
			}

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
}




