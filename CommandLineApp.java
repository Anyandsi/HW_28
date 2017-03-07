package com.company;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class CommandLineApp {

    private static final String DEFAULT_PATH = "C:\\";
    private static File currentDir = new File(DEFAULT_PATH);

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print(currentDir.getPath() + ">");

            String line = scanner.nextLine();

            if (line.startsWith("print")) {
                if (currentDir.isDirectory()) {
                    printAllDirectories(currentDir);
                }
            } else if (line.startsWith("mkDir")) {      //mkDir newFolderName
                createNewFolder(line, currentDir);
            } else if (line.startsWith("cd")) {
                changeDirectory(line);
            } else if(line.startsWith("mkFile")){
                createNewFile(line, currentDir);
            } else if(line.startsWith("delete")){
                delFile(line, currentDir);
            } else if(line.startsWith("open")){
                openTextFile(line, currentDir);
            } else if(line.startsWith("edit")){
                editTextFile(line, currentDir);
            }
        }





    }

    private static void editTextFile(String line, File parent) {
        String fileName = line.replaceFirst("edit ", "");
        System.out.println("Write what you want to add: ");
        openTextFile(fileName, parent);

        while(true){
            Scanner scanner = new Scanner((System.in));
            String lineOfChanges = scanner.nextLine();
            System.out.println("Do you want to save changes y/n ?");
            String answer = scanner.nextLine();
            if(answer.contains("y")){
                File file = new File(parent.getAbsolutePath() + "\\" + fileName);
                try(FileWriter writer = new FileWriter(parent.getAbsolutePath() + "\\" + fileName, true)) {
                    

                    writer.write(lineOfChanges);
                    writer.close();
                    
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }else{
                break;
            }

        }
    }

    private static void openTextFile(String line, File parent){
        String fileName = line.replaceFirst("open ", "");
        String path = parent.getAbsolutePath();
        try {
            File file = new File(parent.getAbsolutePath() + "\\" + fileName);
            FileReader reader = new FileReader(file);
            int b;
            while((b = reader.read()) != -1) {
                System.out.print((char) b);
            }
        } catch (Exception ex) {

        }
    }

    private static void delFile(String line, File currentDir) {
        File[] files =  currentDir.listFiles();
        String fileName = line.replaceFirst("delete ", "");
        for(int i = 0; i < files.length; i++){
            if(files[i].getName().equals(fileName) ){
                files[i].delete();
            }
        }

    }

    private static void createNewFile(String line, File parent) {
        String fileName = line.replaceFirst("mkFile ", "");
        File file = new File(parent, fileName);
        if(file.exists()){
            System.out.println("ERROR: File is already exists !");
        }else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.getStackTrace();
            }
        }
    }

    private static void changeDirectory(String line) {
        String folderName = line.split("\\s+")[1];

        if (folderName.contains("..")) {
            if (currentDir.getParentFile() != null) currentDir = currentDir.getParentFile();
            return;
        }

        File[] list = currentDir.listFiles(dir -> dir.isDirectory() && dir.getName().contains(folderName));

        if (list.length == 1) {
            File matchedFolder = list[0];
            currentDir = matchedFolder;
        } else if (list.length == 0) {
            System.out.println("No such file or directory");
        } else {
            for (File dir : list) {
                System.out.println(dir.getName());
            }
        }
    }

    private static void createNewFolder(String line, File parent) {
        String folderName = line.split("\\s+")[1];

        File directory = new File(parent, folderName);
        directory.mkdir();

    }

    private static void printAllDirectories(File file) {
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isDirectory() && !f.isHidden()) {
                System.out.println(f.getName());
            }
        }
    }
}
