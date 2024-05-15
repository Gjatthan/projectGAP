package com.example.gap.misc;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gap.R;

import org.w3c.dom.Text;

import java.util.Optional;

import pl.droidsonroids.gif.GifImageView;

public class MsgDialogSetup {

    public Dialog msg_dialog;
    public LinearLayout layoutDisc,layoutSave;
    public TextView txtsave,txtdiscard;

    public MsgDialogSetup(Context mcontext) {

        msg_dialog=new Dialog(mcontext);
        msg_dialog.setContentView(R.layout.message_dialog);
        msg_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        msg_dialog.setCancelable(false);
        layoutDisc=msg_dialog.findViewById(R.id.msg_box_cancel);
        layoutSave=msg_dialog.findViewById(R.id.msg_box_ok);
        txtsave=msg_dialog.findViewById(R.id.txtok);
        txtdiscard=msg_dialog.findViewById(R.id.txtdiscard);

        msg_dialog.findViewById(R.id.msg_box_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg_dialog.dismiss();
            }
        });

    }

    public void setupDialog(String msg, Optional<Integer> img_src){
        TextView txt=msg_dialog.findViewById(R.id.msg_box_text);
        GifImageView iview=msg_dialog.findViewById(R.id.msg_img);

        iview.setImageResource(img_src.orElse(R.drawable.information));

        txt.setText(msg);
        msg_dialog.show();
    }
}
