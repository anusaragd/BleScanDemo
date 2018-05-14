package com.minewbeacon.blescan.demo;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class connectservice extends AppCompatActivity {

    public void CallWebservice(String mac_addr, String requester) {


        String strResponse = "";

        String URL = "http://203.151.213.80/ibecon/WebService1.asmx";
        String NAMESPACE = "http://tempuri.org/";
        String METHOD_NAME = "ibecon_status";
        String SOAP_ACTION = "http://tempuri.org/ibecon_status/";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        /**** with parameter *****/
        PropertyInfo pi;

        pi = new PropertyInfo();
        pi.setName("serial");
        pi.setValue(mac_addr);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("name");
        pi.setValue(requester);
        pi.setType(String.class);
        request.addProperty(pi);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        androidHttpTransport.debug = true;
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject response;
            response = (SoapObject) envelope.bodyIn;
            strResponse = response.getProperty(0).toString();
        } catch (Exception e) {
            //e.printStackTrace();
            strResponse = e.toString();
            Toast.makeText(this, strResponse, Toast.LENGTH_SHORT).show();
        }
    }
}

