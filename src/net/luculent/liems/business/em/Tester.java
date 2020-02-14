package net.luculent.liems.business.em;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.text.*;

public class Tester {
	public static void main(String[] args) {
		String strDate1="2019-04-10 06:40:00";
		String strDate2="2019-04-11 00:30:00";
        //注意：SimpleDateFormat构造函数的样式与strDate的样式必须相符
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日 ");
        SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //加上时间
        //必须捕获异常
        try {
            Date date1=sDateFormat.parse(strDate1);
            Date date2=sDateFormat.parse(strDate2);
            System.out.println((date2.getTime()-date1.getTime())/1000/60);
            int i = 1-3>0?1:-1;
            System.out.println(i);
            System.out.println(sDateFormat.format(date1));
        } catch(ParseException px) {
            px.printStackTrace();
        }



	}
}
