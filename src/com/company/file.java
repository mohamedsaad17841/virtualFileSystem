package com.company;

import java.io.Serializable;

public class file implements Serializable
{
    public String name;
    public String filePath;
    public int[] allocatedBlocks;

    public static void createFile(String path, int size)
    {
        String[] pathSplit = path.split("/");
        if(pathSplit.length < 2)
        {
            System.err.println("Cannot create file");
            return;
        }
        int depth = pathSplit.length - 2;
        Directory directory = Directory.directorySearch(virtualFileSystem.root, pathSplit, depth, 1);
        if(directory == null)
        {
            System.err.println("No such file or directory");
            return;
        }

        for(file f : directory.files)
        {
            if(f.name.equals(pathSplit[pathSplit.length-1]))
            {
                System.err.println("There is a file with the same name");
                return;
            }
        }

        file f = new file();
        f.allocatedBlocks = virtualFileSystem.contiguousAallocate(size);
        if(f.allocatedBlocks == null)
        {
            System.err.println("no enough contiguous space");
            return;
        }
        //add the file
        f.name = pathSplit[pathSplit.length-1];
        f.filePath = path;
        directory.files.add(f);
        System.out.println("File created successfully");
    }
    public static void deleteFile(String path)
    {
        String[] pathSplit = path.split("/");
        if(pathSplit.length < 2)
        {
            System.err.println("Cannot delete file");
            return;
        }
        int depth = pathSplit.length - 2;
        Directory directory = Directory.directorySearch(virtualFileSystem.root, pathSplit, depth, 1);
        if(directory == null)
        {
            System.err.println("No such file or directory");
            return;
        }

        for(file f : directory.files)
        {
            if(f.name.equals(pathSplit[pathSplit.length-1]))
            {
                for(int i = f.allocatedBlocks[0] ; i < f.allocatedBlocks[0] + f.allocatedBlocks.length ; i++)
                    virtualFileSystem.freeSpaceManager[i] = 0;
                directory.files.remove(f);
                System.out.println("File deleted successfully");
                return;
            }
        }
        System.err.println("no such file");
    }
}