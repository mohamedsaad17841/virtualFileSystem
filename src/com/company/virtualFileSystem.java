package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class virtualFileSystem {

    private static final int diskSize = 10000;
    static Directory root = new Directory("root", "root");
    static int freeSpaceManager[] = new int[diskSize];        //initially all places are empty
    static ArrayList<String> allocatedBlocksForFiles = new ArrayList<>();


    public static int[] contiguousAallocate(int size)
    {
        if(size > diskSize)
        {
            System.err.println("no free space");
            return null;
        }
        int contiguousSpace = 0;
        int head = 0;
        int i;
        for(i = 0 ; i <diskSize ; )                  //point to the first free block
            if(freeSpaceManager[i] == 1) i++;
            else
            {
                head = i;
                break;
            }

        while(i < diskSize && freeSpaceManager[i] == 0 && contiguousSpace < size)
        {
            contiguousSpace++;
            i++;
            while(i < diskSize && freeSpaceManager[i] == 1)
            {
                i++;
                head = i;
                contiguousSpace = 0;
            }
        }

        if(contiguousSpace < size)
        {
            System.err.println("no free space");
            return null;
        }
        int[] allocated = new int[size]; int x = 0;
        for(int j = head ; j < head+size ; j++)
        {
            freeSpaceManager[j] = 1;
            allocated[x++] = j;
        }
        return allocated;

    }

    public static void displayDiskStatus()
    {
        int emptySpace = 0;
        int allocatedSpace = 0;
        ArrayList<Integer> emptyBlocks = new ArrayList<>();
        ArrayList<Integer> allocatedBlocks = new ArrayList<>();
        for(int i = 0 ; i < diskSize ; i++)
        {
            if(freeSpaceManager[i] == 0)
            {
                emptySpace++;
                emptyBlocks.add(i);
            }
            else
            {
                allocatedSpace++;
                allocatedBlocks.add(i);
            }
        }
        System.out.println("Empty Space = " + emptySpace);
        for(int i : emptyBlocks) System.out.print(i + ",");
        System.out.println("");
        System.out.println("Allocated Space = " + allocatedSpace);
        for(int i : allocatedBlocks) System.out.print(i + ",");
        System.out.println("");
    }

    public static void displayDiskStructure(Directory directory, String directoryOutputSpace, String fileOutputSpace)
    {
        System.out.println(directoryOutputSpace + directory.name);
        if(directory.files.size() > 0)
        {
            for(file f : directory.files)
                System.out.println(fileOutputSpace + f.name);
        }
        if(directory.subDirectories.size() > 0)
        {
            for(Directory d : directory.subDirectories)
                displayDiskStructure(d, directoryOutputSpace + "   ", fileOutputSpace + "   ");
        }
    }
    public static void traverse(Directory directory)
    {
        if(directory.files.size() > 0)
        {
            for(file f : directory.files)
                allocatedBlocksForFiles.add(f.name + " " + f.allocatedBlocks[0] + " " + f.allocatedBlocks.length);
        }
        if(directory.subDirectories.size() > 0)
        {
            for(Directory d : directory.subDirectories)
                traverse(d);
        }
    }

    public static void saveVFS()
    {
        try (
                OutputStream ss = new FileOutputStream("VFS.ser");
                ObjectOutput output = new ObjectOutputStream(ss);
        )
        {
            output.writeObject(root);                       //to save Files and Folders Directory Structure.
            output.writeObject(freeSpaceManager);           //to save free space manager
            allocatedBlocksForFiles.clear();                //embty the array before fill it again
            traverse(root);                                 //to generate allocated blocks for files
            output.writeObject(allocatedBlocksForFiles);    //to save The allocated blocks for files
            System.out.println("Virtual File System saved successfully");
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, "Cannot perform output.", e);
        }
    }

    public static void loadVFS()
    {
        try(
                InputStream ss = new FileInputStream("VFS.ser");
                ObjectInput input = new ObjectInputStream (ss);
        ){
            //deserialize
            root = (Directory)input.readObject();
            freeSpaceManager = (int[]) input.readObject();
            allocatedBlocksForFiles = (ArrayList<String>) input.readObject();
            System.out.println("Virtual File System loaded successfully");
        }
        catch(ClassNotFoundException ex){
            root = new Directory("root", "root");
            System.out.println("Virtual File System loaded successfully1");
        } catch (EOFException e){
            logger.log(Level.SEVERE, "Cannot perform input.", e);
        }
        catch(Exception ex){
            root = new Directory("root", "root");
            System.out.println("Virtual File System loaded successfully2");
           // ex.printStackTrace();
        }
    }

    private static final Logger logger =
            Logger.getLogger(virtualFileSystem.class.getPackage().getName());
}
