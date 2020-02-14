package net.luculent.liems.business.em.emwo;

import net.luculent.core.base.Charset;
import net.luculent.core.base.Log;
import net.luculent.core.database.Database;
import net.luculent.core.database.muitidb.DBFuncTool;
import net.luculent.liems.action.service.ServiceRequest;
import net.luculent.liems.component.sequence.CommCodeNumber;
import net.luculent.liems.component.sequence.CommSequence;
import net.luculent.liems.jsdom.DataDom;
import net.luculent.liems.jsdom.element.Column;
import net.luculent.liems.jsdom.element.Row;
import net.luculent.liems.util.SessionInfo;
import net.luculent.liems.util.Tools;


public class B1WOM51010_EXT2 extends B1WOM51010 {
	public void creatFI(DataDom paramDataDom, SessionInfo paramSessionInfo, ServiceRequest paramServiceRequest)
	  {
	    Row localRow = paramDataDom.getRootTable().getRow(0L);
	    Database localDatabase = null;
	    String str1 = paramServiceRequest.getParameter("TKP_NO");
	    try {
	      localDatabase = Tools.getDatabase(true);

	      CommSequence localCommSequence = CommSequence.getInstance("WOFIREMST_SEQ");
	      String str2 = localCommSequence.getNextValue();

	      CommCodeNumber localCommCodeNumber = null;
	      localCommCodeNumber = new CommCodeNumber("FI", paramSessionInfo.getCurrentLanguage(), paramSessionInfo.getUserId(), paramSessionInfo.getOrg()[0].getValue(), localDatabase);
	      String str3 = localCommCodeNumber.getNextValue();
	      str3 ="";

	      String str4 = getColVal(localRow, "TTKPERNEW_ID");
	      if ((str4 == null) || (str4.equals("")))
	        str4 = getColVal(localRow, "TTKPER_ID");

	      String str5 = getColVal(localRow, "ORG_NO");
	      String[] arrayOfString = getsth_byusr(str4, str5, "PLA_NO,CRW_NO", paramServiceRequest);
	      if (arrayOfString == null) {
	        arrayOfString = new String[2];
	        arrayOfString[0] = "";
	        arrayOfString[1] = "";
	      }

	      String str6 = getColVal(localRow, "TTK_NO");
	      if (Charset.nullToEmpty(str6).equals("")) {
	        str6 = "-1";
	      }

	      String str7 = "insert into WOFIREMST (FIR_NO,FIR_ID,FIR_STA,ORG_NO,FIR_TYP,TTK_NO,FIRPER_ID,PLA_NO,CRW_NO,UNIN_NO,ELC_NO,FIR_DSC,FIR_ADR,FSTUSR_ID,FSTUSR_DTM) select '" + str2 + "', '" + str3 + "','01',ORG_NO,'" + str1 + "','" + str6 + "','" + str4 + "',PLA_NO,OTCRW_NO,UNIT_NO, ELC_NO,TTK_ADR,ADR_DSC,'" + paramSessionInfo.getUserName() + "'," + DBFuncTool.sysdate() + " from RMTTKMST where TTK_NO = " + str6;
	      localDatabase.execSqlUpdate(str7);

	      paramServiceRequest.addServiceReturnParameter("firId", str3);
	      paramServiceRequest.addServiceReturnParameter("firNo", str2);
	      paramServiceRequest.addServiceReturnParameter("ttk_no", str6);
	      paramServiceRequest.setSuccessFlag(true);
	      paramServiceRequest.getMessage().addInfo(str2);
	      localDatabase.commit();
	    } catch (Exception localException) {
	      localDatabase.rollback();
	      Log.error(localException);

	      if (localDatabase == null) return;
	      localDatabase.cleanup();
	    }
	    finally
	    {
	      if (localDatabase != null)
	        localDatabase.cleanup();
	    }
	  }
	
	private String getColVal(Row paramRow, String paramString) {
		String str = null;
		if (paramRow != null) {
			Column localColumn = paramRow.getColumn(paramString);
			if (localColumn != null)
				str = localColumn.getValue();
		}

		return str;
	}
}
