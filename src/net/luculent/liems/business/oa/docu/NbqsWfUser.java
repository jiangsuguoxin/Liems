package net.luculent.liems.business.oa.docu;

import java.io.PrintStream;
import java.util.Date;
import net.luculent.core.base.Charset;
import net.luculent.core.base.FormatDate;
import net.luculent.core.base.Log;
import net.luculent.core.database.Database;
import net.luculent.core.database.Rowset;
import net.luculent.liems.util.Tools;

public class NbqsWfUser
{
  public static String getWfObjUsrHbHq(String pkValue, String serverName, String serverPort)
  {
    String usr = "";
    Database db = null;
    try {
      db = Tools.getDatabase(true);
      String sql = "SELECT D.IMG_ID,A.instance_no,A.Prkey,B.OBJECT_NO,\r\n       D.IMG_NAM USRNAM,\r\n       (select t.usr_nam from UTUSRMST t where t.usr_id=D.IMG_NAM) NAM,\r\n       C.USR_NAM ,\r\n       B.OPERDTM,\r\n       nvl(B.MEMO_TXT, 'нч') MEMO_TXT,\r\n       E.OBJECT_SHT\r\n  FROM WFINSTRN A, WFACHTRN B, UTUSRMST C, DKIMGMST D, WFOBJMST E \r\n WHERE A.INSTANCE_NO = B.INSTANCE_NO\r\n   AND B.USRNAM = C.USR_ID\r\n   AND C.USR_ID = D.IMG_NAM(+)\r\n   AND A.DICTTBL_ID = 'OADOCQBDMST' \r\n   AND A.PRKEY =" + 
        pkValue + "\r\n" + 
        "   AND B.OBJECT_NO = E.OBJECT_NO\r\n" + 
        "   AND B.OBJECT_NO not in (507)\r\n" + 
        "   and B.OBJECT_NO in  (select object_no\r\n" + 
        "          from wfobjmst\r\n" + 
        "         where  model_no like '551700_' and (idxnum=12 or idxnum=26 or idxnum=15 or idxnum=13 or idxnum=16 or idxnum=19))\r\n" + 
        " order by OPERDTM";
      Rowset rs = Database.getRowset(sql);

      while (rs.next()) {
        String name = rs.getString("USRNAM");
        String usrnam = rs.getString("USRNAM");
        String user_name = rs.getString("USR_NAM");
        String MEMO_TXT = Charset.nullToEmpty(rs.getString("MEMO_TXT"));

        Date OPERDTM = rs.getDate("OPERDTM");
        if (usrnam == null)
          usrnam = "";

        if (!("".equals(usrnam)))
          usr = usr + "<tr>&nbsp;</tr><tr><td align = left>" + 
            Charset.nullToEmpty(MEMO_TXT) + "</td></tr>" + 
            "<tr><td align = center>" + DocsTool.getImage2(usrnam, serverName, serverPort) + "</td></tr>" + 
            "<tr><td height = 32 align = right>" + FormatDate.toY_M_D(OPERDTM) + "</td></tr>";
        else
          usr = usr + "<tr>&nbsp;</tr><tr><td align = left>" + 
            Charset.nullToEmpty(MEMO_TXT) + "</td></tr>" + 
            "<tr><td align = center>" + user_name + "</td></tr>" + 
            "<tr><td height = 32 align = right>" + FormatDate.toY_M_D(OPERDTM) + "</td></tr>";
      }
    }
    catch (Exception ex)
    {
      String str1;
      ex.printStackTrace();
      Log.error(ex);
      return usr;
    } finally {
      if (db != null)
        db.cleanup();
    }

    System.out.println(usr);
    return usr;
  }

  public static String getWfObjUsrLdps(String pkValue, String serverName, String serverPort)
  {
    String usr = "";
    Database db = null;
    try {
      db = Tools.getDatabase(true);
      String sql = "SELECT D.IMG_ID,A.instance_no,A.Prkey,B.OBJECT_NO,\r\n       D.IMG_NAM USRNAM,\r\n       (select t.usr_nam from UTUSRMST t where t.usr_id=D.IMG_NAM) NAM,\r\n       C.USR_NAM ,\r\n       B.OPERDTM,\r\n       nvl(B.MEMO_TXT, 'нч') MEMO_TXT,\r\n       E.OBJECT_SHT\r\n  FROM WFINSTRN A, WFACHTRN B, UTUSRMST C, DKIMGMST D, WFOBJMST E \r\n WHERE A.INSTANCE_NO = B.INSTANCE_NO\r\n   AND B.USRNAM = C.USR_ID\r\n   AND C.USR_ID = D.IMG_NAM(+)\r\n   AND A.DICTTBL_ID = 'OADOCQBDMST' \r\n   AND A.PRKEY =" + 
        pkValue + "\r\n" + 
        "   AND B.OBJECT_NO = E.OBJECT_NO\r\n" + 
        "   AND B.OBJECT_NO not in (507)\r\n" + 
        "   and B.OBJECT_NO in  (select object_no\r\n" + 
        "       from wfobjmst\r\n" + 
        "         where idxnum=17 or idxnum=22 or idxnum=23 or idxnum=27)\r\n" + 
        " order by OPERDTM";
      Rowset rs = Database.getRowset(sql);
      while (rs.next()) {
        String name = rs.getString("USRNAM");
        String user_name = rs.getString("USR_NAM");
        String usrnam = rs.getString("USRNAM");
        String MEMO_TXT = Charset.nullToEmpty(rs.getString("MEMO_TXT"));
        if (MEMO_TXT.trim().equals("нч"))
          MEMO_TXT = "";

        Date OPERDTM = rs.getDate("OPERDTM");
        if (usrnam == null)
          usrnam = "";

        if (!("".equals(usrnam)))
          usr = usr + "<tr>&nbsp;</tr><tr><td align = left>" + 
            Charset.nullToEmpty(MEMO_TXT) + "</td></tr>" + 
            "<tr><td align = center>" + DocsTool.getImage2(usrnam, serverName, serverPort) + "</td></tr>" + 
            "<tr><td height = 32 align = right>" + FormatDate.toY_M_D(OPERDTM) + "</td></tr>";
        else
          usr = usr + "<tr>&nbsp;</tr><tr><td align = left>" + 
            Charset.nullToEmpty(MEMO_TXT) + "</td></tr>" + 
            "<tr><td align = center>" + user_name + "</td></tr>" + 
            "<tr><td height = 32 align = right>" + FormatDate.toY_M_D(OPERDTM) + "</td></tr>";
      }
    }
    catch (Exception ex)
    {
      String str1;
      ex.printStackTrace();
      Log.error(ex);
      return usr;
    } finally {
      if (db != null)
        db.cleanup();
    }

    System.out.println(usr);
    return usr;
  }
}