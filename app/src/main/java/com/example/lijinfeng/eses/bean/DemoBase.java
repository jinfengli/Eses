
package com.example.lijinfeng.eses.bean;


import com.example.lijinfeng.eses.R;
import com.example.lijinfeng.eses.base.BaseActivity;

/**
 * Baseclass of all Activities of the Demo ESApplication.
 * 
 * @author Philipp Jahoda
 */
public abstract class DemoBase extends BaseActivity {

//    protected String[] mMonths = new String[] {
//            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
//    };

    protected String[] mParties1 = new String[] {
            "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
            "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
            "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
            "Party Y", "Party Z"
    };

    protected String[] mParties = new String[] {"8","6","6","7","8","9","12","10","6","5","6","4"};

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    }
}
