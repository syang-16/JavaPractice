package io.practice;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class Solution_v2{
	public static void main(String[] args){
		System.exit(processFolder(args));

	}
	public static int processFolder(String[] args){
			try{
				if(RenameFolder.isValidInput(args)){//validate user input and files
					RenamePath rp = new RenamePath(args);
					if(RenameFolder.validateFile(rp)){
						RenameFolder.changeName(rp);//change folder names
						RenameFolder.changeEnv(rp);//change environmental variables
					}
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
	public static boolean isValidInput(String args[])throws IllegalArgumentException{
		if(args==null){//no input
			throw new IllegalArgumentException("No input is available");
		}
		if(args.length!=2){//wrong number of input
			throw new IllegalArgumentException("Wrong number of arguments");
		}
		System.out.println("Your original directory " + args[0] + "\n" + "Your new folder " + args[1]);
		return true;
	}
	
	public static boolean validateFile(RenamePath rp)throws Exception{//validate files

			//form new directory
			if(!Files.exists(rp.getoldpath())){
				throw new IllegalFileException("The old folder does not exist");
			}
			if(!Files.isDirectory(rp.getoldpath())){
				throw new IllegalFileException("Please input folder name,not file");
			}
			if(Files.exists(rp.getnewpath())){
				throw new IllegalFileException("The new folder already exists");
			}
			if(rp.getoldpath().equals(rp.getnewpath())){
				throw new IllegalFileException("The old folder is the same as the new folder");
			}
			System.out.println(rp.getoldpathstr() + " and " + rp.getnewpathstr());
			// Files are OK.
			return true;
}
	public static boolean changeName(RenamePath rp)throws IllegalFileException{//change file name
		try{
			
			Files.move(rp.getoldpath(),rp.getnewpath());
			return true;
			
		}catch(Exception e){
			
			throw new IllegalFileException("Renaming is not successful",e);
			
		}

	}
	public static boolean changeEnv(RenamePath rp) throws Exception{//change environmental variable
			//Prepare output .bat file
			File batfile = new File("c:" + File.separator + "interview" + File.separator + "result.bat" );
			if(!batfile.getParentFile().exists()){
				batfile.getParentFile().mkdir();
			}
			PrintStream ps = new PrintStream(batfile);//stream for bat file
		
			//read environmental variables
			Map<String,String> env = System.getenv();
			for(String vaname : env.keySet()){//visit each key-value
				//check possible value with old folder
				if(env.get(vaname).contains(rp.getoldpathstr())){
					boolean flag = false; //if replacement really occurred

					//split value into String array.
					String[] dirs = env.get(vaname).split(";");
					
					// Scan all directory, finish replacement
					StringBuilder newdirs = new StringBuilder();
					for(String dir:dirs){//visit each directory
						// Path.startsWith to handle two cases: 1. same folder 2. sub folder
						if(RenameFolder.isParent(rp.getoldpath(), dir)){
							newdirs.append(dir.replace(rp.getoldpathstr(),rp.getnewpathstr())+";");
							flag = true;
						}else{
							newdirs.append(dir+";");
						}
					}
					
					//If replacement occurred, write to bat file
					if(flag){
						ps.println("SET " + vaname + "=" + newdirs);
						System.out.format("%s = %s%n",vaname,env.get(vaname));
						System.out.format("%s = %s%n",vaname,newdirs);
					}
//						System.out.println(newdirs);
//					System.out.format("%s = %s%n",vaname,env.get(vaname));
				}
			}
			ps.close();
			return true;
	}
	public static boolean isParent(Path parent,String child){
		Path childpath = Paths.get(child).toAbsolutePath();
		return (childpath.startsWith(parent));
		
	}
}
class IllegalFileException extends Exception{
	public IllegalFileException(){
		super();
	}
	public IllegalFileException(String msg){
		super(msg);
	}
	public IllegalFileException(String msg,Exception e){
		super(msg);
	}
}
class RenamePath{//Convert and store old path and new path
	private Path oldpath;
	private Path newpath;
	private String oldpathstr;
	private String newpathstr;
	public RenamePath(String args[]){
		this.setoldpath(args);
		this.setnewpath(args);
		this.setoldpathstr(args);
		this.setnewpathstr(args);
	}
	public void setoldpath(String args[]){
		this.oldpath = Paths.get(args[0]);
	}
	public void setnewpath(String args[]){
		this.newpath = oldpath.resolveSibling(args[1]);
	}
	public void setoldpathstr(String args[]){
		this.oldpathstr = Paths.get(args[0]).toString();
	}
	public void setnewpathstr(String args[]){
		this.newpathstr = oldpath.resolveSibling(args[1]).toString();
	}
	public Path getoldpath(){
		return this.oldpath;
	}
	public Path getnewpath(){
		return this.newpath;
	}
	public String getoldpathstr(){
		return this.oldpathstr;
	}
	public String getnewpathstr(){
		return this.newpathstr;
	}
}





