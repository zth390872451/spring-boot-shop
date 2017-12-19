package com.svlada.common.request;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Response JSON Object for REST controller
 *
 * @creator JT
 * @modifier JT
 * @date：2017-05-19
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class CustomResponse<T> {

    /**
     * response code, error code
     */
    private Integer code;

    /**
     * response desc, error msg
     */
    private String message;

    //response data
    private T data;
    
    //response time
    private Long timestamp;
    
    public CustomResponse(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }
    
    public CustomResponse(CustomResponseStatus responseStatus, String message, T data){
        this(responseStatus.getCode(),message,data);
    }

    public CustomResponse(CustomResponseStatus responseStatus, String message){
        this(responseStatus.getCode(),message,null);
    }

    public CustomResponse(CustomResponseStatus responseStatus, T data){
        this(responseStatus.getCode(),responseStatus.getReasonPhrase(),data);
    }

    public CustomResponse(CustomResponseStatus responseStatus){
        this(responseStatus.getCode(),responseStatus.getReasonPhrase(),null);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
