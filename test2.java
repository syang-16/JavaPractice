package io.test2;

import java.io.File;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class test2 {
	public static void main(String args[]){
		System.exit(processFolder(args));
	}
	public static int processFolder(String args[]){
		try{
			if(RenameFolder.isValidInput(args)){
				RenamePath rp = new RenamePath(args);
				if(RenameFolder.isValidFile(rp)){
//					RenameFolder.changeName(rp);
					RenameFolder.changeEnv(rp);
				}
			}
			return 0;
		}catch(IllegalArgumentException e){
			System.out.println("wrong input:" + e.getMessage());
			printUsage();
			return 1;
		}catch(ChangeNameException e){
			System.out.println("Cannot change name:" + e.getMessage());
			return 2;
		
		}catch(ChangeEnvException e){
			System.out.println("Cannot change environmental variable:" + e.getMessage());
			return 3;
			
		}catch(Exception e){
			System.out.println("Error occurred:" + e.getMessage());
			return 4;
			
		}
	}
	public static void printUsage(){
		System.out.println("Please input two arguments,");
		System.out.println("If there is space, please enclose in \",");
		System.out.println("Second one is a new folder name.");
	}
}
class RenamePath{
	Path oldfolder;
	Path newfolder;
	String oldfolderstr;
	String newfolderstr;
	public RenamePath(String args[]){
		this.oldfolder = Paths.get(args[0]);
		this.newfolder = Paths.get(args[0]).resolveSibling(args[1]);
		this.oldfolderstr = this.oldfolder.toString();
		this.newfolderstr = this.newfolder.toString();
	}
	public Path getOldfolder() {
		return oldfolder;
	}
	public Path getNewfolder() {
		return newfolder;
	}
	public String getOldfolderstr() {
		return oldfolderstr;
	}
	public String getNewfolderstr() {
		return newfolderstr;
	}
	
}
class RenameFolder{
	public static boolean isValidInput(String args[])throws Exception{
		if(args==null){
			throw new IllegalArgumentException("No input is found.");
		}
		if(args.length!=2){
			throw new IllegalArgumentException("Wrong number of input.");
		}
		return true;
		
		
	}
	public static boolean isValidFile(RenamePath rp)throws Exception{
		if(!Files.exists(rp.getOldfolder())){
			throw new ChangeNameException("Old folder does not exits.");
		}
		if(!Files.isDirectory(rp.getOldfolder())){
			throw new ChangeNameException("Old folder is not directory.");
		}
		if(Files.exists(rp.getNewfolder())){
			throw new ChangeNameException("New folder exists.");
		}
		if(rp.getNewfolder().equals(rp.getOldfolder())){
			throw new ChangeNameException("New folder is the same as the old folder.");
		}
		return true;
	}
	public static boolean changeName(RenamePath rp)throws Exception{
		try{
			Files.move(rp.getOldfolder(), rp.getNewfolder());
			return true;
		}catch(Exception e){
			throw new ChangeNameException("Error in changing folder name.",e);

		}
		
	}
	public static boolean changeEnv(RenamePath rp)throws Exception{
		String workingdir = System.getProperty("user.dir");
		File batfile = new File(workingdir + File.separator + "interview.bat");
		try(PrintStream ps = new PrintStream(batfile)){
			Map<String,String> env = System.getenv();
			for(String key:env.keySet()){
				String value = env.get(key);
				if(value.contains(rp.getOldfolderstr())){
					String[] dirs = value.split(";");
					StringBuilder sb = new StringBuilder();
					boolean flag = false;
					
					for(String dir:dirs){
						Path dirpath = Paths.get(dir);
						if(dirpath.startsWith(rp.getOldfolder())){
							sb.append(dir.replace(rp.getOldfolderstr(), rp.getNewfolderstr()) + ";");
							flag = true;
						}else{
							sb.append(dir + ";");
						}
					}
					
					if(flag){
						ps.println("SET " + key + "=" + sb.toString());
					}
				}
			}
			return true;
		}catch(Exception e){
			throw new ChangeEnvException("Error in changing environmental variable.", e);

		}
	}
}
class ChangeNameException extends Exception{
	public ChangeNameException(String msg){
		super(msg);
	}
	public ChangeNameException(String msg, Throwable t){
		super(msg,t);
	}
}
class ChangeEnvException extends Exception{
	public ChangeEnvException(String msg, Throwable t){
		super(msg,t);
	}
}
