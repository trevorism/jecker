package com.trevorism.gcloud.webapi.model

import groovy.transform.ToString

@ToString
class JobInfo {

    String id
    boolean building
    long duration
    long estimatedDuration
    String result
    long date
    String url

}
