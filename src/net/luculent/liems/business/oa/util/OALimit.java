package net.luculent.liems.business.oa.util;

import net.luculent.liems.jsdom.DataDom;
import net.luculent.liems.jsdom.element.Table;
import net.luculent.liems.jsdom.element.WhereLimit;
import net.luculent.liems.util.SessionInfo;

public class OALimit
{
  public static void setMyLimit(DataDom dataDom, SessionInfo sessionInfo)
  {
    String usr_id = sessionInfo.getUserId();
    if ("SYS".equals(usr_id)||"YUXY".equals(usr_id)||"WANGJIE".equals(usr_id)||"CHENXIAOMING".equals(usr_id))
      return;

    Table table = dataDom.getRootTable();
    WhereLimit wl = table.getWhereLimit();
    if (wl == null)
      wl = new WhereLimit();

    String tablename = table.getName();
    String pkField = table.getPkFieldName();

    String sql = " 1=1 AND (FSTUSR_ID='" + usr_id + "' ";

    sql = sql + " OR " + pkField + " IN (SELECT B.PRKEY FROM WFTODOTRN A,WFINSTRN B WHERE A.INSTANCE_NO=B.INSTANCE_NO AND " + 
      "B.DICTTBL_ID='" + tablename + "' AND A.USRNAM='" + usr_id + "')";

    sql = sql + " OR " + pkField + " IN (SELECT DISTINCT(B.PRKEY) FROM WFACHTRN A,WFINSTRN B WHERE A.INSTANCE_NO=B.INSTANCE_NO " + 
      "AND B.DICTTBL_ID='" + tablename + "' AND A.USRNAM='" + usr_id + "')";
    sql = sql + " OR " + pkField + " IN (SELECT DISTINCT(B.PRKEY) FROM WFAVISOTRN A,WFINSTRN B WHERE A.INSTANCE_NO=B.INSTANCE_NO " + 
      "AND B.DICTTBL_ID='" + tablename + "' AND A.AVISOUSR_ID='" + usr_id + "')";
    sql = sql + ")";
    wl.addColumn("new_sql_wherelimit", sql);
    table.setWhereLimit(wl);
  }
}