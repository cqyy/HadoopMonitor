//package com.wsc.bigdata.communicate;
//
//import com.wsc.bigdata.databasemanage.DBConnectionManager;
//import com.wsc.bigdata.databasemanage.DatabaseManager;
//import com.wsc.bigdata.managelocalapps.AppManage;
//import com.wsc.bigdata.managelocalapps.threadRunApp;
//
//
//import javax.naming.NamingException;
//import javax.ws.rs.*;
//import javax.ws.rs.core.MediaType;
//import java.io.IOException;
//import java.sql.*;
//
///**
// * Created by lwj on 14-3-18.
// */
//@Path("hello")
//public class HelloResource {
//    AppManage appManage = AppManage.getInstance();
//
//    @GET
//    @Produces(MediaType.TEXT_PLAIN)
//    public String sayHello() throws NamingException, SQLException {
////        Connection conn=null;
////        PreparedStatement stmt=null;
////        ResultSet rs=null;
////        String sql=null;
////        String username=null;
////        String alias=null;
////        String pwd=null;
////        String strMsg=null;
////        Context context = new InitialContext();
////        DataSource ds = (DataSource) context.lookup("java:comp/env/jdbc/mysql");
////        conn = ds.getConnection();
//        Connection conn = DBConnectionManager.getInstance().getConnection("mypool");
//        if(!conn.isClosed())
//
//            System.out.println("Succeeded connecting to the Database!");
//
//// statement用来执行SQL语句
//
//        Statement statement = conn.createStatement();
//
//// 要执行的SQL语句
//
//        String sql = "select * from file";
//        ResultSet rs = statement.executeQuery(sql);
//        while(rs.next()){
//            System.out.println(rs.getString("name"));
//        }
//        statement.close();
//        return "hello";
//    }
////
////    @GET
////    @Path("1")
////    @Produces("text/plain")
////    public String getUser(@QueryParam("name") String name,
////                        @QueryParam("age") int age) {
////        return name+age;
////    }
////
////    @GET
////    @Path("{username}")
////    @Produces(MediaType.APPLICATION_JSON)
////    public String getMy(@PathParam("username") String username){
////        return username;
////    }
////
////    @GET
////    @Path("3")
////    @Produces(MediaType.APPLICATION_JSON)
////    public FileQuery getFile(@QueryParam("filename") String filename,
////                          @QueryParam("filepath") String filepath) {
////        return new FileQuery(filename,filepath,true);
////    }
////
////    @GET
////    @Path("4")
////    @Produces(MediaType.APPLICATION_OCTET_STREAM)
////    public byte[] download(@PathParam("id") long id)throws Exception{
////        //ApplicationContext ac=WebApplicationContextUtils.getWebApplicationContext(servletContext);
////        //resourceService=(ResourceService)ac.getBean("resourceService");
////
//////        Resource resource=(Resource)resourceService.getObject(Resource.class, id);
//////        String path=servletContext.getRealPath(resource.getPath());
////        String path = "/home/lwj/A3848EB3@7BB79E2D.EE550C53.png";
////        FileInputStream fis=new FileInputStream(new File(path));
////        byte [] b=new byte[fis.available()];
////        fis.read(b);
////        return b;
////    }
////
////    @POST
////    @Consumes("application/x-www-form-urlencoded")
////    public void post(@FormParam("name") String name) {
////        System.out.println(name);
////    }
////    @POST
////    @Path("5")
////    @Consumes("application/x-www-form-urlencoded")
////    public void update(@BeanParam FileQuery file) {
////        System.out.println(file.getFileName()+file.getUserName());
////    }
//
//    //get file
//
//}
