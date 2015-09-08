package com.vbstudio.twittersearch.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.vbstudio.twittersearch.MainActivity;
import com.vbstudio.twittersearch.R;

public class LogoutConfirmationDialog extends Dialog implements View.OnClickListener {

    private Context context;

    public LogoutConfirmationDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void show() {
        setupView();
        super.show();
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnLogMeOut) {
            ((MainActivity) context).performActionLogout();
        }
        else if (id == R.id.btnDismissDialog) {

        }
        dismiss();
    }

    private void setupView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_logout_confirmation);

        Button btnDismissDialog = (Button) findViewById(R.id.btnDismissDialog);
        Button btnLogMeOut = (Button) findViewById(R.id.btnLogMeOut);

        (btnDismissDialog).setOnClickListener(this);
        (btnLogMeOut).setOnClickListener(this);
    }
}
