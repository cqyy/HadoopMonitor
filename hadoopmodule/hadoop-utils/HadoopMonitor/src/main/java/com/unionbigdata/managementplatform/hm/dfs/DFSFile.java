package com.unionbigdata.managementplatform.hm.dfs;

/**
 * Created by Administrator on 14-3-5.
 * represent a file in DFS.
 * Immutable.
 */
public class DFSFile implements DFSFileItem {

    private final String name;                  //local name of the file.
    private final String owner;                 //owner's name of the file
    private final long modificationTime;        //last modification time
    private final String permission;            //permission,in format like "rwxr-xr-x"
    private final String group;                 //group of this file
    private final boolean isFile = true;


    private final long blockSize;               //block size of this file. -1 if not set
    private final int replication;              //replication of this file.-1 if not set
    private final long length;                  //length of this file in bytes. -1 if not set

    private DFSFile(Builder builder){
        this.name = builder.name;
        this.owner = builder.owner;
        this.modificationTime = builder.modificationTime;
        this.permission = builder.permission;
        this.group = builder.group;
        this.blockSize = builder.blockSize;
        this.replication = builder.replication;
        this.length = builder.length;

    }

    //builder of DFSFile
    public static class Builder{
        private  String name;                       //local name of the file.
        private  String owner = "";                 //owner's name of the file
        private  long modificationTime = 0l;        //last modification time
        private  String permission = "---------";   //permission,in format like "rwxr-xr-x"
        private  String group = "";                 //group of this file


        private  long blockSize = -1;               //block size of this file
        private  int replication = -1;              //replication of this file.-1 if not set
        private  long length = -1;                  //length of this file in bytes

        public Builder(String name){
            if(name == null ){
                throw new NullPointerException();
            }
            this.name = name;
        }

        public Builder owner(String owner){
            this.owner = owner;
            return this;
        }

        public Builder lastModicicationTime(long time){
            this.modificationTime = time;
            return this;
        }

        public Builder permission(String permission){
            this.permission = permission;
            return this;
        }

        public Builder group(String group){
            this.group = group;
            return this;
        }

        public Builder blockSize(long size){
            this.blockSize = size;
            return this;
        }

        public Builder replication(int replication){
            this.replication = replication;
            return this;
        }

        public Builder length(long length){
            this.length = length;
            return this;
        }

        public DFSFile newInstance(){
            return new DFSFile(this);
        }

    }


    @Override
    public boolean isDir() {
        return !isFile;
    }

    @Override
    public boolean isFile() {
        return isFile;
    }

    @Override
    public String getLocalName() {
        return name;
    }

    @Override
    public long getModificationTime() {
        return modificationTime;
    }

    @Override
    public String owner() {
        return owner;
    }

    @Override
    public String group() {
        return group;
    }

    @Override
    public String permission() {
        return permission;
    }

    public long blockSize(){
        return blockSize;
    }

    public long replication(){
        return replication;
    }

    public long length(){
        return length;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("file: ").append(name)
                .append("  owner: ").append(owner)
                .append("  lastModificationTime: ").append(modificationTime)
                .append("  permission: ").append(permission)
                .append("  group: ").append(group)
                .append("  block size: ").append(blockSize)
                .append("  replication: ").append(replication)
                .append("  length: ").append(length);
        return sb.toString();
    }
}
