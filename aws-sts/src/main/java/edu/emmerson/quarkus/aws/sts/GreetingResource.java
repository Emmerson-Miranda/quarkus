package edu.emmerson.quarkus.aws.sts;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import edu.emmerson.poc.aws.assume.library.credentials.CredentialsDelegate;
import edu.emmerson.poc.aws.assume.library.credentials.CredentialsStrategy;
import edu.emmerson.poc.aws.assume.library.pojo.CredentialsRequest;
import edu.emmerson.poc.aws.assume.library.pojo.TemporalCredential;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection(targets = { CredentialsRequest.class, TemporalCredential.class })
@Path("/credentials")
public class GreetingResource {

    final CredentialsStrategy cs;
    final CredentialsDelegate cd;
    
    public GreetingResource() {
        cd = new CredentialsDelegate();
        cs = cd.getCredentialsStratey();
    }
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello RESTEasy";
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public TemporalCredential getCredentials(CredentialsRequest request) throws Exception {
        System.out.println("Request for temporal credentials: " + request);
        TemporalCredential cred = cd.loadCredentialsStrategy(cs, request);
        return cred;
    }
}