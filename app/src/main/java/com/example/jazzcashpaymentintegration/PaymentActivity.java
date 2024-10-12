package com.example.jazzcashpaymentintegration;

import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jazzcashpaymentintegration.databinding.ActivityPaymentBinding;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class PaymentActivity extends AppCompatActivity {
    ActivityPaymentBinding binding;
    WebView paymentWebView;
    String postData = "";
    private static final String paymentReturnUrl = "";  //Enter return url

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        paymentWebView = binding.paymentWebView;
        WebSettings webSettings = paymentWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        WebView.setWebContentsDebuggingEnabled(true);
        Intent intent = getIntent();
        paymentWebView.setWebViewClient(new myWebViewClient());
        paymentWebView.getSettings().setDomStorageEnabled(true);
        paymentWebView.addJavascriptInterface(new FormDataInterface(), "FORMOUT");

        String price = intent.getStringExtra("price");
        String[] values = price.split("\\.");
        price = values[0];
        price = price + "00";
        Log.d("price", "onCreate: " + price);
        Date Date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = simpleDateFormat.format(Date);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Date);
        calendar.add(Calendar.HOUR, 1);

        Date currentDatePLusOneHour = calendar.getTime();
        String expiryDate = simpleDateFormat.format(currentDatePLusOneHour);

        String transactionId = "T" + date;

        String pp_MerchantID = ""; //Enter merchant id
        String pp_Password = "";  //Enter password
        String IntegritySalt = "";  //Enter integrity Salt
        String pp_ReturnURL = paymentReturnUrl;

        String pp_Amount = price;
        String pp_TxnDateTime = date;
        String pp_TxnExpiryDateTime = expiryDate;
        String pp_TxnRefNo = transactionId;
        String pp_Version = "1.1";
        String pp_TxnType = "";
        String pp_Language = "EN";
        String pp_SubMerchantID = "";
        String pp_BankID = "TBANK";
        String pp_ProductID = "RETL";
        String pp_TxnCurrency = "PKR";
        String pp_BillReference = "Cart110";
        String pp_Description = "Description of Transaction";
        String pp_SecureHash = "";
        String pp_mpf_1 = "1";
        String pp_mpf_2 = "2";
        String pp_mpf_3 = "3";
        String pp_mpf_4 = "4";
        String pp_mpf_5 = "5";

        String sortedString = "";
        sortedString += IntegritySalt + "&";
        sortedString += pp_Amount + "&";
        sortedString += pp_BankID + "&";
        sortedString += pp_BillReference + "&";
        sortedString += pp_Description + "&";
        sortedString += pp_Language + "&";
        sortedString += pp_MerchantID + "&";
        sortedString += pp_Password + "&";
        sortedString += pp_ProductID + "&";
        sortedString += pp_ReturnURL + "&";
        //sortedString += pp_SubMerchantID + "&";
        sortedString += pp_TxnCurrency + "&";
        sortedString += pp_TxnDateTime + "&";
        sortedString += pp_TxnExpiryDateTime + "&";
        //sortedString += pp_TxnType + "&";
        sortedString += pp_TxnRefNo + "&";
        sortedString += pp_Version + "&";
        sortedString += pp_mpf_1 + "&";
        sortedString += pp_mpf_2 + "&";
        sortedString += pp_mpf_3 + "&";
        sortedString += pp_mpf_4 + "&";
        sortedString += pp_mpf_5;

        pp_SecureHash = php_hash_hmac(sortedString, IntegritySalt);
        System.out.println("AhmadLogs: sortedString : " +sortedString);
        System.out.println("AhmadLogs: pp_SecureHash : " +pp_SecureHash);

        try {
            postData += URLEncoder.encode("pp_Version", "UTF-8")
                    + "=" + URLEncoder.encode(pp_Version, "UTF-8") + "&";
            postData += URLEncoder.encode("pp_TxnType", "UTF-8")
                    + "=" + URLEncoder.encode(pp_TxnType, "UTF-8") + "&";
            postData += URLEncoder.encode("pp_Language", "UTF-8")
                    + "=" + URLEncoder.encode(pp_Language, "UTF-8") + "&";
            postData += URLEncoder.encode("pp_MerchantID", "UTF-8")
                    + "=" + URLEncoder.encode(pp_MerchantID, "UTF-8") + "&";
            postData += URLEncoder.encode("pp_SubMerchantID", "UTF-8")
                    + "=" + URLEncoder.encode(pp_SubMerchantID, "UTF-8") + "&";
            postData += URLEncoder.encode("pp_Password", "UTF-8")
                    + "=" + URLEncoder.encode(pp_Password, "UTF-8") + "&";
            postData += URLEncoder.encode("pp_BankID", "UTF-8")
                    + "=" + URLEncoder.encode(pp_BankID, "UTF-8") + "&";
            postData += URLEncoder.encode("pp_ProductID", "UTF-8")
                    + "=" + URLEncoder.encode(pp_ProductID, "UTF-8") + "&";
            postData += URLEncoder.encode("pp_TxnRefNo", "UTF-8")
                    + "=" + URLEncoder.encode(pp_TxnRefNo, "UTF-8") + "&";
            postData += URLEncoder.encode("pp_Amount", "UTF-8")
                    + "=" + URLEncoder.encode(pp_Amount, "UTF-8") + "&";
            postData += URLEncoder.encode("pp_TxnCurrency", "UTF-8")
                    + "=" + URLEncoder.encode(pp_TxnCurrency, "UTF-8") + "&";
            postData += URLEncoder.encode("pp_TxnDateTime", "UTF-8")
                    + "=" + URLEncoder.encode(pp_TxnDateTime, "UTF-8") + "&";
            postData += URLEncoder.encode("pp_BillReference", "UTF-8")
                    + "=" + URLEncoder.encode(pp_BillReference, "UTF-8") + "&";
            postData += URLEncoder.encode("pp_Description", "UTF-8")
                    + "=" + URLEncoder.encode(pp_Description, "UTF-8") + "&";
            postData += URLEncoder.encode("pp_TxnExpiryDateTime", "UTF-8")
                    + "=" + URLEncoder.encode(pp_TxnExpiryDateTime, "UTF-8") + "&";
            postData += URLEncoder.encode("pp_ReturnURL", "UTF-8")
                    + "=" + URLEncoder.encode(pp_ReturnURL, "UTF-8") + "&";
            postData += URLEncoder.encode("pp_SecureHash", "UTF-8")
                  + "=" + URLEncoder.encode(pp_SecureHash, "UTF-8") + "&";
            postData += URLEncoder.encode("ppmpf_1", "UTF-8")
                    + "=" + URLEncoder.encode(pp_mpf_1, "UTF-8") + "&";
            postData += URLEncoder.encode("ppmpf_2", "UTF-8")
                    + "=" + URLEncoder.encode(pp_mpf_2, "UTF-8") + "&";
            postData += URLEncoder.encode("ppmpf_3", "UTF-8")
                    + "=" + URLEncoder.encode(pp_mpf_3, "UTF-8") + "&";
            postData += URLEncoder.encode("ppmpf_4", "UTF-8")
                    + "=" + URLEncoder.encode(pp_mpf_4, "UTF-8") + "&";
            postData += URLEncoder.encode("ppmpf_5", "UTF-8")
                    + "=" + URLEncoder.encode(pp_mpf_5, "UTF-8");

            // Now postData contains all the parameters encoded and ready to send
            Log.d("postData", "onCreate: " + postData);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        paymentWebView.postUrl("https://sandbox.jazzcash.com.pk/CustomerPortal/transactionmanagement/merchantform/", postData.getBytes());
    }

    private class myWebViewClient extends WebViewClient {
        private final String jsCode = "" + "function parseForm(form){" +
                "var values='';" +
                "for(var i=0 ; i< form.elements.length; i++){" +
                "   values+=form.elements[i].name+'='+form.elements[i].value+'&'" +
                "}" +
                "var url=form.action;" +
                "console.log('parse form fired');" +
                "window.FORMOUT.processFormData(url,values);" +
                "   }" +
                "for(var i=0 ; i< document.forms.length ; i++){" +
                "   parseForm(document.forms[i]);" +
                "};";

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (url.equals(paymentReturnUrl)) {
                view.stopLoading(); //return url cancelling
                return;
            }
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            //Log.d(DEBUG_TAG, "Url: "+url);
            if (url.equals(paymentReturnUrl)) {
                return;
            }
            view.loadUrl("javascript:(function() { " + jsCode + "})()");
            super.onPageFinished(view, url);
        }
    }
    private class FormDataInterface{
        @JavascriptInterface
        public void processFormData(String url, String formData) {

            Intent i = new Intent(PaymentActivity.this, MainActivity.class);

            Log.d("formdata", "processFormData: " + url + formData);

            if (url.equals(paymentReturnUrl)) {
                String[] values = formData.split("&");
                for (String pair : values) {
                    String[] nameValue = pair.split("=");
                    if (nameValue.length == 2) {
                        System.out.println("AhmadLogs: Name:" + nameValue[0] + " value:" + nameValue[1]);
                        i.putExtra(nameValue[0], nameValue[1]);
                    }
                }

                setResult(RESULT_OK, i);
                finish();

                return;
            }
        }
    }
    public static String php_hash_hmac(String data, String secret) {
        String returnString = "";
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] res = sha256_HMAC.doFinal(data.getBytes());
            returnString = bytesToHex(res);

        }catch (Exception e){
            e.printStackTrace();
        }

        return returnString;
    }

    public static String bytesToHex(byte[] bytes) {
        final char[] hexArray = "0123456789abcdef".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0, v; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
