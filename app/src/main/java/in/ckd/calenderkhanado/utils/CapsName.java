package in.ckd.calenderkhanado.utils;

public class CapsName {

    //------------ Capitalize full name ---------------//
    public static String CapitalizeFullName(String name){
        StringBuffer res = new StringBuffer();
        String[] strArr = name.split(" ");
        if(strArr.length>0){
            for (String str : strArr) {
                char[] stringArray = str.trim().toCharArray();
                if (stringArray.length>0) {
                    stringArray[0] = Character.toUpperCase(stringArray[0]);
                    String str1 = new String(stringArray);
                    res.append(str1).append(" ");
                }
            }
        }
        String result_name;
        if (!res.toString().trim().isEmpty()) {
            result_name = res.toString().trim();
        }else{
            result_name = name;
        }
        //Log.e("name",result_name);
        return result_name;
    }
}
