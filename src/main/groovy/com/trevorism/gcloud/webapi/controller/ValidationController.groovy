package com.trevorism.gcloud.webapi.controller

import com.google.gson.Gson
import com.trevorism.gcloud.webapi.model.JobInfo
import com.trevorism.gcloud.webapi.model.TestError
import com.trevorism.http.HttpClient
import com.trevorism.http.JsonHttpClient
import com.trevorism.https.DefaultSecureHttpClient
import com.trevorism.https.SecureHttpClient
import io.swagger.annotations.Api
import org.apache.tools.ant.taskdefs.condition.Http

import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import java.util.concurrent.TimeUnit

@Api("Validation Operations")
@Path("/validation")
class ValidationController {

    public static final int TWO_HOURS_SECONDS = 2 * 60 * 60
    private SecureHttpClient httpClient = new DefaultSecureHttpClient()
    private Gson gson = new Gson()

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    boolean createValidation() {
        validate()

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    boolean isValid() {
        validate()

    }

    boolean validate() {
        def jobNames = ["acceptance-schedule", "acceptance-eventhub"]
        boolean valid = true
        jobNames.each{app ->
             valid = valid && isAppValid(app)
        }

        if(!valid){
            String json = gson.toJson(new TestError([source: "jecker", message: "${jobNames} are not passing or were not run within the last two hours."]))
            httpClient.post("https://testing.trevorism.com/api/error", json)
        }

        return valid
    }

    boolean isAppValid(String app) {
        String json = httpClient.get("https://cinvoke.datastore.trevorism.com/info/${app}/completed")
        JobInfo info = gson.fromJson(json, JobInfo)
        Date now = new Date()

        long diff = TimeUnit.SECONDS.convert(now.getTime() - info.date, TimeUnit.MILLISECONDS)
        println diff

        if(info.result != "SUCCESS" || (diff > TWO_HOURS_SECONDS))
            return false

        return true
    }
}
