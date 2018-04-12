package me.prateeksaigal.base;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by on 17/01/17.
 * To handle base common result.
 */

public class BaseResponse implements Parcelable {

    private String msg;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public BaseResponse(){

    }
    protected BaseResponse(Parcel in) {
        msg = in.readString();
        status= in.readString();
    }

    public static final Creator<BaseResponse> CREATOR = new Creator<BaseResponse>() {
        @Override
        public BaseResponse createFromParcel(Parcel in) {
            return new BaseResponse(in);
        }

        @Override
        public BaseResponse[] newArray(int size) {
            return new BaseResponse[size];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(msg);
        dest.writeString(status);

    }
}
