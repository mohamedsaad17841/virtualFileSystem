package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Available commands : ");
        System.out.println("createFile fullpath/fileName fileSize");
        System.out.println("createFolder fullpath/folderName");
        System.out.println("deleteFile fullpath/fileName");
        System.out.println("deleteFolder fullpath/folderName");
        System.out.println("displayDiskStatus");
        System.out.println("displayDiskStructure");
        System.out.println("displayAllocatedBlocks");
        System.out.println("Quit");
        virtualFileSystem.loadVFS();
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String command = in.nextLine();
            command = command.toLowerCase();
            if (command.equals("exit")) return;
            String[] commandSplit = command.split(" ");
            if (commandSplit[0].equals("createfile")) {
                if (commandSplit.length != 3) {
                    System.err.println("Wrong number of arguments");
                    continue;
                }
                file.createFile(commandSplit[1], Integer.parseInt(commandSplit[2]));
            } else if (commandSplit[0].equals("createfolder")) {
                if (commandSplit.length != 2) {
                    System.err.println("Wrong number of arguments");
                    continue;
                }
                Directory.createFolder(commandSplit[1]);
            } else if (commandSplit[0].equals("deletefile")) {
                if (commandSplit.length != 2) {
                    System.err.println("Wrong number of arguments");
                    continue;
                }
                file.deleteFile(commandSplit[1]);
            } else if (commandSplit[0].equals("deletefolder")) {
                if (commandSplit.length != 2) {
                    System.err.println("Wrong number of arguments");
                    continue;
                }
                Directory.deleteFolder(commandSplit[1]);
            } else if (commandSplit[0].equals("displaydiskstatus")) {
                if (commandSplit.length != 1) {
                    System.err.println("Wrong number of arguments");
                    continue;
                }
                virtualFileSystem.displayDiskStatus();
            } else if (commandSplit[0].equals("displaydiskstructure")) {
                if (commandSplit.length != 1) {
                    System.err.println("Wrong number of arguments");
                    continue;
                }
                virtualFileSystem.displayDiskStructure(virtualFileSystem.root, "", "   ");
            }
            else if(commandSplit[0].equals("displayallocatedblocks"))
            {
                if (commandSplit.length != 1) {
                    System.err.println("Wrong number of arguments");
                    continue;
                }
                System.out.println("Allocated Blocks for each file : ");
                for(String str : virtualFileSystem.allocatedBlocksForFiles) System.out.println(str);
            } else if (commandSplit[0].equals("quit")) {
                if (commandSplit.length != 1) {
                    System.err.println("Wrong number of arguments");
                    continue;
                }
                virtualFileSystem.saveVFS();
                return;
            } else {
                System.err.println("Invalid command!");
                continue;
            }
        }
    }
}
