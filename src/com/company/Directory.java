package com.company;

import java.io.Serializable;
import java.util.ArrayList;

public class Directory implements Serializable
{
    public Directory()
    {
        name = "";
        directoryPath = "";
        files = new ArrayList<>();
        subDirectories = new ArrayList<>();
    }
    public Directory(String name, String directoryPath)
    {
        this.name = name;
        this.directoryPath = directoryPath;
        files = new ArrayList<>();
        subDirectories = new ArrayList<>();
    }

    public String name;
    public String directoryPath;
    public ArrayList<file> files;
    public ArrayList<Directory> subDirectories;

    public static Directory directorySearch(Directory directory, String[] pathSplit, int depth, int level)
    {
        if(depth == 0)
        {
            return directory;
        }
        for(int i = 0 ; i<directory.subDirectories.size() ; i++)
        {
            if(directory.subDirectories.get(i).name.equals(pathSplit[level]))
            {
                return directorySearch(directory.subDirectories.get(i), pathSplit, depth - 1, level + 1);
            }
        }
        return null;
    }

    public static void createFolder(String path)
    {
        String[] pathSplit = path.split("/");
        if(pathSplit.length < 2)
        {
            System.err.println("Cannot create folder");
            return;
        }
        int depth = pathSplit.length - 2;
        Directory directory = directorySearch(virtualFileSystem.root, pathSplit, depth, 1);
        if(directory == null)           //Wrong path
        {
            System.err.println("No such file or directory");
            return;
        }
        for(Directory d : directory.subDirectories)
        {
            if(d.name.equals(pathSplit[pathSplit.length-1]))
            {
                System.err.println("There is a directory with the same name");
                return;
            }
        }

        Directory d = new Directory();
        //add the direcotory
        d.name = pathSplit[pathSplit.length-1];
        d.directoryPath = path;
        d.files = new ArrayList<>();
        d.subDirectories = new ArrayList<>();
        directory.subDirectories.add(d);
        System.out.println("Directory created successfully");
    }

    public static void deleteFolder(String path)
    {
        String[] pathSplit = path.split("/");
        if(pathSplit.length < 2)
        {
            System.err.println("Cannot delete folder");
            return;
        }
        int depth = pathSplit.length - 2;
        Directory directory = directorySearch(virtualFileSystem.root, pathSplit, depth, 1);
        if(directory == null)
        {
            System.err.println("No such file or directory");
            return;
        }

        for(Directory d : directory.subDirectories)
        {
            if(d.name.equals(pathSplit[pathSplit.length-1]))
            {
                for(int i = 0 ; i < d.files.size() ; i++)           //delete all files under this directory
                {
                    file.deleteFile(path + "/" + d.files.get(i).name);
                }
                directory.subDirectories.remove(d);
                System.out.println("Folder deleted successfully");
                return;
            }
        }
        System.err.println("no such folder");
    }
}
