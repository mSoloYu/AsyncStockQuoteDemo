// IStockQuoteServiceResponseListener.aidl
package com.stockquote.aidl;

import com.stockquote.aidl.StockQuoteResponse;

oneway interface IStockQuoteServiceResponseListener {
    void onResponse(in StockQuoteResponse response);
}
