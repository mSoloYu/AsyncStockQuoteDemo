package com.stockquote.aidl.server;

import android.os.RemoteException;

import com.stockquote.aidl.IStockQuoteService;
import com.stockquote.aidl.IStockQuoteServiceResponseListener;
import com.stockquote.aidl.StockQuote;
import com.stockquote.aidl.StockQuoteRequest;
import com.stockquote.aidl.StockQuoteResponse;

/**
 * Created by mSolo on 2015/3/27.
 */
public class StockQuoteServiceImpl extends IStockQuoteService.Stub {

    @Override
    public void retrieveStockQuote(StockQuoteRequest request, IStockQuoteServiceResponseListener listener) throws RemoteException {

        String[] stockIdArray = null;
        if (request.getType() == StockQuoteRequest.Type.STOCKQUOTE_MULTIPLE) {
            stockIdArray = request.getStockIdArray();
        } else {
            return ;
        }

        StockQuote stockQuote = StockNetResourceManager.getInstance()
                .setUpStockQuotes(stockIdArray);

        listener.onResponse(new StockQuoteResponse(stockQuote));

    }

}
