package net.luculent.liems.business.em;

import javax.servlet.http.HttpServletRequest;

import net.luculent.core.base.Charset;
import net.luculent.core.base.Log;
import net.luculent.core.database.Database;
import net.luculent.core.database.Rowset;
import net.luculent.liems.action.service.ServiceRequest;
import net.luculent.liems.business.dk.wfeg.WorkFlowSql;
import net.luculent.liems.business.em.emwr.RMTTKMST;
import net.luculent.liems.component.sequence.CommCodeNumber;
import net.luculent.liems.jsdom.DataDom;
import net.luculent.liems.util.SessionInfo;
import net.luculent.liems.util.Tools;

public class reWriteMlg {
	// 工作票，许可开工反写工作负责人
	public static String reWrite_AGREE_TTKPER_ID(String paramString1,
			String paramString2, String paramString3, String paramString4,
			String paramString5, WorkFlowSql paramWorkFlowSql,
			String paramString6) throws Exception {
		if (Charset.nullToEmpty(paramString5).equals("")) {
			return "false";
		}

		String str = "";
		str = "update RMTTKMST set AGREE_TTKPER_ID=TTKPER_ID  where TTK_NO="
				+ paramString5;
		paramWorkFlowSql.setSqlList(str);
		return "true";
	}
	
	// 动火票，终结反写动火执行人
		public static String reWrite_END_DOUSR_ID(String paramString1,
				String paramString2, String paramString3, String paramString4,
				String paramString5, WorkFlowSql paramWorkFlowSql,
				String paramString6) throws Exception {
			if (Charset.nullToEmpty(paramString5).equals("")) {
				return "false";
			}

			String str = "";
			str = "update WOFIREMST set END_DOUSR_ID = DOUSR_ID where FIR_NO="
					+ paramString5;
			paramWorkFlowSql.setSqlList(str);
			return "true";
		}

	// 工作票，生成工作票票号
	public 
	
	static String createTTK_ID(String paramString1, String paramString2,
			String paramString3, String paramString4, String paramString5,
			WorkFlowSql paramWorkFlowSql, String paramString6) throws Exception {
		System.out.println("paramString1---------------------------"
				+ paramString1);
		System.out.println("paramString2---------------------------"
				+ paramString2);
		System.out.println("paramString3---------------------------"
				+ paramString3);
		System.out.println("paramString4---------------------------"
				+ paramString4);
		System.out.println("paramString5---------------------------"
				+ paramString5);
		Database localDatabase = null;
		String str1 = "";
		String str2 = "";
		String str3 = "";
		String str4 = "";
		try {
			localDatabase = Tools.getDatabase(true);

			if (!("".equals(Database.getPrepareSqlValue(
					"SELECT t.TTK_ID FROM RMTTKMST t where t.TTK_NO = ?",
					new Object[] { paramString5 }))))
				return "true";

			Rowset localRowset = localDatabase
					.getRS("SELECT TKP_ID,B.ORG_NO,B.TTK_MAJOR FROM RMTTKTYPMST A,RMTTKMST B WHERE A.TKP_NO=B.TTK_TYP AND B.TTK_NO='"
							+ paramString5 + "'");
			if (!(localRowset.next())) {}
				str2 = localRowset.getString("TKP_ID");
				str3 = localRowset.getString("ORG_NO");
				str4 = localRowset.getString("TTK_MAJOR");
			System.out.println("str2---------------------------" + str2);
			System.out.println("str3---------------------------" + str3);
			CommCodeNumber localCommCodeNumber = new CommCodeNumber(str2,
					"CHZ", paramString1, str3, localDatabase);
			str1 = localCommCodeNumber.getNextValue();
			int i = RMTTKMST.checkKey(str1);
			if (i < 0)
				str1 = localCommCodeNumber.getNextValue();
			System.out.println("---------------------------" + str1);
			localDatabase.execSqlUpdate("update RMTTKMST set TTK_ID='" + str4
					+ "-" + str1 + "' where TTK_NO='" + paramString5 + "'");
			return "true";
		} catch (Exception localException) {
			Log.info(localException);
		} finally {
			if (localDatabase != null)
				localDatabase.cleanup();
		}
		if (localDatabase != null) {
			localDatabase.cleanup();
		}

		return "false";
	}
	
	// 操作票，生成操作票票号
		public static String createOPERMST_ID(String paramString1, String paramString2,
				String paramString3, String paramString4, String paramString5,
				WorkFlowSql paramWorkFlowSql, String paramString6) throws Exception {
			System.out.println("paramString1---------------------------"
					+ paramString1);
			System.out.println("paramString2---------------------------"
					+ paramString2);
			System.out.println("paramString3---------------------------"
					+ paramString3);
			System.out.println("paramString4---------------------------"
					+ paramString4);
			System.out.println("paramString5---------------------------"
					+ paramString5);
			Database localDatabase = null;
			String str1 = "";
			String str2 = "";
			String str3 = "";
			String str4 = "";
			try {
				localDatabase = Tools.getDatabase(true);

				Rowset localRowset = localDatabase
						.getRS("SELECT OPERMST_TYP,ORG_NO,TTK_MAJOR FROM RMOPERMST WHERE OPERMST_NO='"
								+ paramString5 + "'");
				if (!(localRowset.next())) {}
					str2 = localRowset.getString("OPERMST_TYP");
					if("00".equals(str2)){
						str2 = "DQCZP";
					} else{
						str2 = "RJCZP";
					}
					str3 = localRowset.getString("ORG_NO");
					str4 = localRowset.getString("TTK_MAJOR");
				System.out.println("str2---------------------------" + str2);
				System.out.println("str3---------------------------" + str3);
				CommCodeNumber localCommCodeNumber = new CommCodeNumber(str2,
						"CHZ", paramString1, str3, localDatabase);
				str1 = localCommCodeNumber.getNextValue();
				int i = RMTTKMST.checkKey(str1);
				if (i < 0)
					str1 = localCommCodeNumber.getNextValue();
				System.out.println("---------------------------" + str1);
				localDatabase.execSqlUpdate("update RMOPERMST set OPERMST_ID='" + str4
						+ "-" + str1 + "' where OPERMST_NO='" + paramString5 + "'");
				return "true";
			} catch (Exception localException) {
				Log.info(localException);
			} finally {
				if (localDatabase != null)
					localDatabase.cleanup();
			}
			if (localDatabase != null) {
				localDatabase.cleanup();
			}

			return "false";
		}
	
	// 动火票，生成动火票票号
		public static String createFIR_ID(String paramString1, String paramString2,
				String paramString3, String paramString4, String paramString5,
				WorkFlowSql paramWorkFlowSql, String paramString6) throws Exception {
			System.out.println("paramString1---------------------------"
					+ paramString1);
			System.out.println("paramString2---------------------------"
					+ paramString2);
			System.out.println("paramString3---------------------------"
					+ paramString3);
			System.out.println("paramString4---------------------------"
					+ paramString4);
			System.out.println("paramString5---------------------------"
					+ paramString5);
			Database localDatabase = null;
			String str1 = "";
			String str2 = "";
			String str3 = "";
			String str4 = "";
			try {
				localDatabase = Tools.getDatabase(true);

				if (!("".equals(Database.getPrepareSqlValue(
						"SELECT t.FIR_ID FROM WOFIREMST t where t.FIR_NO = ?",
						new Object[] { paramString5 }))))
					return "true";

				Rowset localRowset = localDatabase
						.getRS("SELECT TKP_ID,B.ORG_NO,B.CHK_FLG FROM RMTTKTYPMST A,WOFIREMST B WHERE A.TKP_NO=B.FIR_TYP AND B.FIR_NO='"
								+ paramString5 + "'");
				if (!(localRowset.next())) {}
					str2 = localRowset.getString("TKP_ID");
					str3 = localRowset.getString("ORG_NO");
					str4 = localRowset.getString("CHK_FLG");
				System.out.println("str2---------------------------" + str2);
				System.out.println("str3---------------------------" + str3);
				CommCodeNumber localCommCodeNumber = new CommCodeNumber(str2,
						"CHZ", paramString1, str3, localDatabase);
				str1 = localCommCodeNumber.getNextValue();
				int i = RMTTKMST.checkKey(str1);
				if (i < 0)
					str1 = localCommCodeNumber.getNextValue();
				System.out.println("---------------------------" + str1);
				localDatabase.execSqlUpdate("update WOFIREMST set FIR_ID='" + str4
						+ "-" + str1 + "' where FIR_NO='" + paramString5 + "'");
				return "true";
			} catch (Exception localException) {
				Log.info(localException);
			} finally {
				if (localDatabase != null)
					localDatabase.cleanup();
			}
			if (localDatabase != null) {
				localDatabase.cleanup();
			}

			return "false";
		}
		
		  public static String checkPlanDtm(String params, HttpServletRequest request)
		  {
		    System.out.println("FIR_NO-------------"+ Charset.nullToEmpty(params));
		    return Database.getValue("select case when fir_typ = 1077237448326512640 and  planend_dtm-planbeg_dtm >1 then 1 "
		    		+ "when fir_typ = 1077237557562966016 and planend_dtm-planbeg_dtm >1 then 5 "
		    		+ "else 0 end from wofiremst where fir_no = '" + Charset.nullToEmpty(params) + "'");

		  }
}
