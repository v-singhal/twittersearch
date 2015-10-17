package com.vbstudio.twittersearch.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.vbstudio.twittersearch.MainActivity;
import com.vbstudio.twittersearch.R;

public class LogoutConfirmationDialog extends BaseDialog {

    private Context context;

    public LogoutConfirmationDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void show() {
        setupView();
        super.show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnLogMeOut) {
            ((MainActivity) context).performActionLogout();
        } else if (id == R.id.btnDismissDialog) {

        }
        dismiss();
    }

    @Override
    protected void setupView() {
        super.setupView();

        setContentView(R.layout.dialog_logout_confirmation);

        Button btnDismissDialog = (Button) findViewById(R.id.btnDismissDialog);
        Button btnLogMeOut = (Button) findViewById(R.id.btnLogMeOut);

        (btnDismissDialog).setOnClickListener(this);
        (btnLogMeOut).setOnClickListener(this);
    }
}
