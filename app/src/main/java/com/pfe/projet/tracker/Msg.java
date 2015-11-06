package dz.finalprojectchp_1.appandroid.location_4;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Azz_B on 04/11/2015.
 */
public class Msg {

    public static void toastMsg(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }

    public static void toastMsgShort(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }
}
