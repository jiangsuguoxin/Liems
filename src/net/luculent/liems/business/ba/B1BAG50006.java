package net.luculent.liems.business.ba;

import net.luculent.liems.business.*;
import net.luculent.liems.jsdom.*;
import net.luculent.liems.util.*;
import net.luculent.liems.action.service.*;
import net.luculent.liems.business.cm.cmcm.*;
import net.luculent.core.database.*;
import net.luculent.liems.business.dk.wfeg.*;
import net.luculent.liems.component.sequence.CommCodeNumber;
import net.luculent.liems.ldk.*;
import net.luculent.core.base.*;
import net.luculent.liems.jsdom.element.*;
import java.util.Iterator;

public class B1BAG50006 extends ModifyBusiness
{
    public void init(final DataDom dataDom, final SessionInfo sessionInfo, final ServiceRequest serviceRequest) {
        super.init(dataDom, sessionInfo, serviceRequest);
        DataRule.setOrg(dataDom, sessionInfo, serviceRequest, "XQTZBAMST", "", true);
        this.initData(dataDom, sessionInfo, serviceRequest);
        setFieldsReadonly(dataDom, sessionInfo);
    }
    
    public void initData(final DataDom dataDom, final SessionInfo sessionInfo, final ServiceRequest serviceRequest) {
        final Table table = dataDom.getTable("XQTZBAMST");
        final Row currRow = table.getSafeRow();
        if ("new".equals(currRow.getDataStatus())) {
            this.setColumnValue(currRow, "VALID_STA", "01");
            this.setColumnValue(currRow, "TCSJ_DTM", FormatDate.getTodayStr());
            this.setColumnValue(currRow, "ORG_NO", sessionInfo.getDefaultOrg());
            final String CST_NO = Database.getPrepareSqlValue("SELECT CST_NO FROM VW_CSTUSR WHERE USR_ID=? AND USR_VALID_STA='A' AND REGULAR_FLG='1'", new Object[] { sessionInfo.getUserId() });
            this.setColumnValue(currRow, "TC_BM", CST_NO);
            this.setColumnValue(currRow, "TC_YH", sessionInfo.getUserId());
        }
    }
    
    private void setFieldsReadonly(DataDom dataDom, SessionInfo sessionInfo)
    {
      Table table = dataDom.getTable("XQTZBAMST");
      String wtSta = table.getSafeRowColumnValue("VALID_STA");
      if ("06".equals(wtSta)) {
        Field field = null;
        Iterator fields = table.getFields().values().iterator();
        while (fields.hasNext()) {
          field = (Field)fields.next();
          field.setReadonly(true);
        }
      } else {
        Tools.resetFieldReadonly(sessionInfo, dataDom);
      }
    }
    
    public void DetailNavigation(DataDom dataDom, SessionInfo sessionInfo, ServiceRequest serviceRequest)
    {
      super.DetailNavigation(dataDom, sessionInfo, serviceRequest);
      setFieldsReadonly(dataDom, sessionInfo);
    }
    
    public static String updateClr(final Database db, final String userid, final String objectno, final String instanceno, final String tableid, final String prkey, final WorkFlowSql wfs, final String params) {
        try {
            final String sql = "update XQTZBAMST set CLR='" + userid + "',SJWCSJ_DTM=sysdate where WT_NO='" + prkey + "'";
            wfs.setSqlList(sql);
        }
        catch (Exception e) {
            Log.error(e);
            return "false";
        }
        return "true";
    }
    
    public void showModify(final DataDom dataDom, final SessionInfo sessionInfo, final ServiceRequest serviceRequest) {
        super.showModify(dataDom, sessionInfo, serviceRequest);
        DataRule.setOrg(dataDom, sessionInfo, serviceRequest, "XQTZBAMST", "", true);
        this.initData(dataDom, sessionInfo, serviceRequest);
    }
    
    public void save(final DataDom dataDom, final SessionInfo sessionInfo, final ServiceRequest serviceRequest) {
        final Table table = dataDom.getTable("XQTZBAMST");
        Row currRow = table.getRow(0L);
        if (currRow == null) {
            table.addRow(new Row(false, 0, 0, "new"));
            currRow = table.getRow(0L);
        }
        currRow.setFirstLastUserInfo(sessionInfo.getUserId());
        try {
            super.save(dataDom, sessionInfo, serviceRequest);
        }
        catch (Exception e) {
            Log.error(e);
            serviceRequest.getMessage().addError(CommMsg.getInstance("LIEMSMSG000005", sessionInfo.getCurrentLanguage()).getMsg_dsc());
        }
    }
    
    public void Delete(final DataDom dataDom, final SessionInfo sessionInfo, final ServiceRequest serviceRequest) {
        super.Delete(dataDom, sessionInfo, serviceRequest);
    }
    
    public void TabOnClick(final DataDom dataDom, final SessionInfo sessionInfo, final ServiceRequest serviceRequest) {
        final Table table = dataDom.getTable("XQTZBAMST");
        if (table != null) {
            final Field orgNoField = table.getField("ORG_NO");
            if (orgNoField != null) {
                final Row currRow = table.getRow(0L);
                String orgNo = "";
                if (currRow != null) {
                    orgNo = Charset.nullToEmpty(currRow.getColumnValue("ORG_NO"));
                }
                if (!Charset.nullToEmpty(orgNo).equals("")) {
                    serviceRequest.addParameter("selectorgno", (Object)orgNo);
                }
                else {
                    serviceRequest.addParameter("selectorgno", (Object)sessionInfo.getOrg()[0].getValue());
                }
            }
            else {
                serviceRequest.addParameter("selectorgno", (Object)sessionInfo.getOrg()[0].getValue());
            }
        }
        else {
            serviceRequest.addParameter("selectorgno", (Object)sessionInfo.getOrg()[0].getValue());
        }
        super.TabOnClick(dataDom, sessionInfo, serviceRequest);
    }
    


    public static String updateId(final String userid, final String objectno, final String instanceno, final String tableid, final String prkey, final WorkFlowSql wfs, final String params) {
	    Database db = null;
    	try {
			String id = Database.getPrepareSqlValue("select WT_ID from XQTZBAMST where WT_NO = ?",new Object[] { prkey });
			if("".equals(id) || id == null) {
			    db = Tools.getDatabase(false);
		        CommCodeNumber autoNum = new CommCodeNumber("QXTZ", "CHZ", userid, null, db);
		        String hth = autoNum.getNextValue();
		        id = hth.substring(0,8) + "-" + hth.substring(8);
		        db.execPrepareSqlUpdate("update XQTZBAMST set WT_ID = ? where WT_NO = ?", new Object[] { id , prkey });
		        db.commit();
			}
        }
        catch (Exception e) {
            Log.error(e);
  	        return "号码规则更新失败";
        } finally {
  	      if (db != null) {
  	        db.cleanup();
  	      }
  	    }
        return "true";
    }
    
    public void updateInfo(DataDom dataDom, SessionInfo sessionInfo, ServiceRequest serviceRequest) {
    	String wtno = Charset.nullToEmpty(dataDom.getTable("XQTZBAMST").getPkValue());
    	String wtSta = dataDom.getTable("XQTZBAMST").getSafeRowColumnValue("VALID_STA");
    	Database db = null;
    	try {
    		db = Tools.getDatabase(false);
    		if("01".equals(wtSta)){
    			String id = Database.getPrepareSqlValue("select WT_ID from XQTZBAMST where WT_NO = ?",new Object[] { wtno });
    			if("".equals(id) || id == null) {
    			    
    		        CommCodeNumber autoNum = new CommCodeNumber("XQTZ", "CHZ", "SYS", null, db);
    		        String hth = autoNum.getNextValue();
    		        id = hth.substring(0,8) + "-" + hth.substring(8);
    		        db.execPrepareSqlUpdate("update XQTZBAMST set WT_ID = ?, VALID_STA = '06', SJWCSJ_DTM = SYSDATE where WT_NO = ?", new Object[] { id , wtno });
    		        
    			}else{
    				db.execPrepareSqlUpdate("update XQTZBAMST set VALID_STA = '06' where WT_NO = ?", new Object[] { wtno });
    			}
    		}else{
    			db.execPrepareSqlUpdate("update XQTZBAMST set VALID_STA = '01' where WT_NO = ?", new Object[] { wtno });
			}
    		db.commit();
        }
        catch (Exception e) {
            Log.error(e);
        } finally {
  	      if (db != null) {
  	        db.cleanup();
  	      }
  	    }
    }
}
