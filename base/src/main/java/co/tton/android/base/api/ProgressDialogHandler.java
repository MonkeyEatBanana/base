package co.tton.android.base.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

public class ProgressDialogHandler extends Handler {

    public static final int SHOW_PROGRESS_DIALOG = 1;
    public static final int DISMISS_PROGRESS_DIALOG = 2;

    private ProgressDialog mDialog;

    private Context mContext;
    private boolean mCancelable;
    private ProgressCancelListener mProgressCancelListener;

    public ProgressDialogHandler(Context context, ProgressCancelListener listener) {
        this(context, listener, false);
    }

    public ProgressDialogHandler(Context context, ProgressCancelListener listener, boolean cancelable) {
        mContext = context;
        mProgressCancelListener = listener;
        mCancelable = cancelable;
    }

    private void initProgressDialog() {
        if (mDialog == null) {
            mDialog = new ProgressDialog(mContext);
            mDialog.setCancelable(mCancelable);

            if (mCancelable) {
                mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        mProgressCancelListener.onCancelProgress();
                    }
                });
            }

            try {
                // fix: BadTokenException, Unable to add window -- token android.os.BinderProxy@44fbe5f is not valid; is your activity running?
                if (!mDialog.isShowing()) {
                    mDialog.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void dismissProgressDialog() {
        if (mDialog != null) {
            try {
                // fix: IllegalArgumentException, not attached to window manager
                mDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mDialog = null;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_PROGRESS_DIALOG:
                initProgressDialog();
                break;
            case DISMISS_PROGRESS_DIALOG:
                dismissProgressDialog();
                break;
        }
    }

    public interface ProgressCancelListener {
        void onCancelProgress();
    }
}