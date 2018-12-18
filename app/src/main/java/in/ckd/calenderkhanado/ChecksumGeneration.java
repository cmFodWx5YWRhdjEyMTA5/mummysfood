package in.ckd.calenderkhanado;

import java.util.TreeMap;

public class ChecksumGeneration {

    //Below parameters provided by Paytm

    private static String MID = "XfLrgI86715347032972";
    private static String MercahntKey = "XfLrgI86715347032972";
    private static String INDUSTRY_TYPE_ID = "Retail";
    private static String CHANNLE_ID = "WAP";
    private static String WEBSITE = "WEBSTAGING";
    private static String CALLBACK_URL = "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=order1";

    public static void main(String[] a){

        TreeMap<String,String> paramMap = new TreeMap<String,String>();
        paramMap.put("MID" , MID);
        paramMap.put("ORDER_ID" , "ORDER00011");
        paramMap.put("CUST_ID" , "CUST00011");
        paramMap.put("INDUSTRY_TYPE_ID" , INDUSTRY_TYPE_ID);
        paramMap.put("CHANNEL_ID" , CHANNLE_ID);
        paramMap.put("TXN_AMOUNT" , "1.00");
        paramMap.put("WEBSITE" , WEBSITE);
        paramMap.put("EMAIL" , "seemanagar86@gmail.com");
        paramMap.put("MOBILE_NO" , "8602639858");
        paramMap.put("CALLBACK_URL" , CALLBACK_URL);

        try{
            String checkSum =  ""/*CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum(MercahntKey, paramMap)*/;
            paramMap.put("CHECKSUMHASH" , checkSum);

            System.out.println("Paytm Payload: "+ paramMap);

        }catch(Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
