package com.stockquote.aidl.client;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.widget.TextView;

import com.stockquote.aidl.IStockQuoteService;
import com.stockquote.aidl.IStockQuoteServiceResponseListener;
import com.stockquote.aidl.StockQuote;
import com.stockquote.aidl.StockQuoteRequest;
import com.stockquote.aidl.StockQuoteResponse;

/**
 * Created by mSolo on 2015/3/26.
 */
public class MainActivity extends Activity
        implements ServiceConnection {

    private static final String TAG = "MainActivity";
    private static final int RESPONSE_MESSAGE_ID = 1;

    private ProgressDialog mDialog;

    private TextView mNameTv;
    private TextView mQuoteTv;

    private IStockQuoteService mService;

    private final Handler responseHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case RESPONSE_MESSAGE_ID:
                    mNameTv.setText(((StockQuote) message.obj).getStockName());
                    mQuoteTv.setText( ((StockQuote)message.obj).getStockPrice() );
                    mDialog.dismiss();
                    break;
            }
        }
    };

    private final IStockQuoteServiceResponseListener responseListener
            = new IStockQuoteServiceResponseListener.Stub() {

        // this method is executed on one of the pooled binder threads
        @Override
        public void onResponse(StockQuoteResponse response)
                throws RemoteException {
            Message message = responseHandler
                    .obtainMessage(RESPONSE_MESSAGE_ID, response.getResult());
            responseHandler.sendMessage(message);
        }
    };

    @Override
    protected void onCreate(Bundle savedBundle) {
        super.onCreate(savedBundle);
        setContentView(R.layout.main);
        mNameTv = (TextView) findViewById(R.id.name);
        mQuoteTv = (TextView) findViewById(R.id.quote);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!super.bindService(new Intent(IStockQuoteService.class.getName()),
                this, BIND_AUTO_CREATE)) {
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        super.unbindService(this);
    }

    public void onServiceConnected(ComponentName name, IBinder service) {
        mService = IStockQuoteService.Stub.asInterface(service);
        setText();
    }

    public void onServiceDisconnected(ComponentName name) {
        mService = null;
    }

    private void setText() {

        final StockQuoteRequest request = new StockQuoteRequest(null,
                new String[]{"002024"}, StockQuoteRequest.Type.STOCKQUOTE_MULTIPLE);
        mDialog = ProgressDialog.show(this, "",
                "正在获取...", true);

        try {
            mService.retrieveStockQuote(request, responseListener);
        } catch (RemoteException e) {
            mDialog.dismiss();
            return ;
        }
    }

}
