package com.unionbigdata.managementplatform.hm.dfs;

/**
 * Created by Administrator on 14-3-5.
 * Directory of file system
 */
public class DFSDir implements DFSFileItem{
    private final String name;                  //local name of the directory.
    private final String owner;                 //owner's name of the directory
    private final long modificationTime;        //last modification time
    private final String permission;            //permission,in format like "rwxr-xr-x"
    private final String group;                 //group of this directory
    private final boolean isFile = false;

    private DFSDir(Builder builder){
        this.name = builder.name;
        this.owner = builder.owner;
        this.modificationTime = builder.modificationTime;
        this.permission = builder.permission;
        this.group = builder.group;
    }

    public static class Builder{
        private  String name;                       //local name of the directory.
        private  String owner = "";                 //default value ""
        private  long modificationTime = -1;        //default "-1"
        private  String permission = "---------";   //default "---------"
        private  String group = "";                 //group of this directory


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

        public Builder lastModificationTime(long time){
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

        public DFSDir newInstance(){
            return new DFSDir(this);
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("dir: ").append(name)
                .append("  owner: ").append(owner)
                .append("  lastModificationTime: ").append(modificationTime)
                .append("  permission: ").append(permission)
                .append("  group: ").append(group);
        return sb.toString();
    }
}
