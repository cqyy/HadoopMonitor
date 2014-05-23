package com.unionbigdata.managementplatform.ws;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/test")
public class Echo {

    @GET
    @Path("echo")
    @Produces(MediaType.TEXT_PLAIN)
    public String echo(@QueryParam("str") String str){
        return str;
    }
}
