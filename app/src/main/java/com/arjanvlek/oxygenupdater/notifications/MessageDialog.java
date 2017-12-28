package com.arjanvlek.oxygenupdater.notifications;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;

/**
 * Usage: Title text, Message text, Positive button text, Negative button text.
 */
public class MessageDialog extends DialogFragment {
    private DialogListener dialogListener;

    private String title;
    private String message;
    private String positiveButtonText;
    private String negativeButtonText;
    private boolean closable;


    public interface DialogListener {
        void onDialogPositiveButtonClick(DialogFragment dialogFragment);
        void onDialogNegativeButtonClick(DialogFragment dialogFragment);
    }

    public MessageDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public MessageDialog setMessage(String message) {
        this.message = message;
        return this;
    }

    public MessageDialog setDialogListener(DialogListener listener) {
        dialogListener = listener;
        return this;
    }

    public MessageDialog setPositiveButtonText(String positiveButtonText) {
        this.positiveButtonText = positiveButtonText;
        return this;
    }

    public MessageDialog setNegativeButtonText(String negativeButtonText) {
        this.negativeButtonText = negativeButtonText;
        return this;
    }

    public MessageDialog setClosable(boolean closable) {
        this.closable = closable;
        return this;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(title);
        builder.setMessage(message);

        if(negativeButtonText != null) {
            builder.setNegativeButton(negativeButtonText, (dialog, which) -> {
                if (dialogListener != null) {
                    dialogListener.onDialogNegativeButtonClick(MessageDialog.this);
                }
                dismiss();
            });
        }
        if(positiveButtonText != null) {
            builder.setPositiveButton(positiveButtonText, (dialog, which) -> {
                if (dialogListener != null) {
                    dialogListener.onDialogPositiveButtonClick(MessageDialog.this);
                }
                dismiss();
            });
        }

        if(!closable) {
            builder.setCancelable(false);
            builder.setOnKeyListener((dialogInterface, i, keyEvent) -> {
                if (i == KeyEvent.KEYCODE_BACK) exit();
                return true;
            });
            builder.setOnDismissListener(dialogInterface -> exit());
        }
        return builder.create();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(!closable) {
            exit();
        }
    }


    private void exit() {
        exit(getActivity());
    }

    private void exit(Activity activity) {
        if (activity != null) {
            activity.finish();
            exit(activity.getParent());
        } else {
            new Handler().postDelayed(() -> System.exit(0), 2000);
        }
    }
}
