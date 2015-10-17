package com.vbstudio.twittersearch.dialog;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vbstudio.twittersearch.R;
import com.vbstudio.twittersearch.dom.GenericDialogData;

public class GenericDialog extends BaseDialog {

	private Context context;
	private GenericDialogData genericDialogData;

	public GenericDialog(Context context, GenericDialogData genericDialogData) {
		super(context);	
		this.context = context;
		this.genericDialogData = genericDialogData;
	}

	@Override
	public void show() {
		setupView();
		super.show();

    }

    @Override
    protected void setupView() {
        super.setupView();

		setContentView(R.layout.dialog_generic);

		((TextView) findViewById(R.id.txtDialogServerHeader)).setText(genericDialogData.getTitle());
		((TextView) findViewById(R.id.txtServerMessage)).setText(processPromoDetails(genericDialogData.getMessage()));
		((Button) findViewById(R.id.btnDismissDialog)).setOnClickListener(this);

        if (((TextView) findViewById(R.id.txtServerMessage)).getLineCount() < 3) {
            ((TextView) findViewById(R.id.txtServerMessage)).setGravity(Gravity.CENTER);
        }
	}

	private Spanned processPromoDetails(String message) {
		return (Html.fromHtml(message));
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btnDismissDialog) {
			dismiss();
		}
	}
}
