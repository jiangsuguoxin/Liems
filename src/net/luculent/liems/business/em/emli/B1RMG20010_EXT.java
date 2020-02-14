package net.luculent.liems.business.em.emli;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import net.luculent.core.base.Charset;
import net.luculent.core.base.FormatDate;
import net.luculent.core.base.Log;
import net.luculent.core.database.DBException;
import net.luculent.core.database.Database;
import net.luculent.core.database.Rowset;
import net.luculent.core.database.muitidb.DBFuncTool;
import net.luculent.core.exception.LiemsException;
import net.luculent.liems.action.service.ServiceRequest;
import net.luculent.liems.business.ModifyBusiness;
import net.luculent.liems.business.cm.cmcm.CMUNITMST;
import net.luculent.liems.business.cm.cmcm.DataRule;
import net.luculent.liems.business.cm.cmem.EQELCMST;
import net.luculent.liems.business.cm.cmem.EQEQPMST;
import net.luculent.liems.business.cm.cmem.ForeignOBJforCheck;
import net.luculent.liems.business.cm.cmpg.pgop.PGOPTMST;
import net.luculent.liems.business.cm.cmut.UTUSRMST;
import net.luculent.liems.business.dk.wfeg.WorkFlowReturnObject;
import net.luculent.liems.business.em.emeq.EqTools;
import net.luculent.liems.business.em.emop.RMOPERMST;
import net.luculent.liems.component.sequence.CommCodeNumber;
import net.luculent.liems.jsdom.DataDom;
import net.luculent.liems.jsdom.element.Column;
import net.luculent.liems.jsdom.element.Extends;
import net.luculent.liems.jsdom.element.Field;
import net.luculent.liems.jsdom.element.Label;
import net.luculent.liems.jsdom.element.Option;
import net.luculent.liems.jsdom.element.PageControl;
import net.luculent.liems.jsdom.element.Row;
import net.luculent.liems.jsdom.element.Select;
import net.luculent.liems.jsdom.element.Table;
import net.luculent.liems.jsdom.element.Text;
import net.luculent.liems.jsdom.element.WhereLimit;
import net.luculent.liems.ldk.CommMsg;
import net.luculent.liems.msg.Message;
import net.luculent.liems.util.SessionInfo;
import net.luculent.liems.util.Tools;

public class B1RMG20010_EXT extends B1RMG20010 {
	public void init(DataDom paramDataDom, SessionInfo paramSessionInfo, ServiceRequest paramServiceRequest)
	  {
		Object localObject1;
		Object localObject2;
		Object localObject3;
		Object localObject4;
	    String str6;
	    Object localObject6;
	    Object localObject7;
	    String str1 = Charset.nullToEmpty(paramServiceRequest.getParameter("pkValue"));

	    String str2 = "";
	    String str3 = "";
	    String str4 = "";
	    WhereLimit localWhereLimit = null;
	    if ((paramSessionInfo.getParaByPgmNo("B1RMG20010") != null) && (!("".equals(paramSessionInfo.getParaByPgmNo("B1RMG20010"))))) {
	      localWhereLimit = new WhereLimit();
	      str4 = paramSessionInfo.getParaByPgmNo("B1RMG20010");
	      int i = str4.indexOf("order_By");
	      str2 = str4.substring(0, i);
	      str3 = str4.substring(i + 8, str4.length());
	      localWhereLimit.setRelationType("custom");
	      localWhereLimit.setRelation(str3);
	      paramSessionInfo.removeParaByPgmNo("B1RMG20010");
	    }
	    if (paramDataDom.getRootTable() != null)
	      paramDataDom.getRootTable().clearRow();

	    super.init(paramDataDom, paramSessionInfo, paramServiceRequest);
	    DataRule.setOrg(paramDataDom, paramSessionInfo, paramServiceRequest, "RMLIMMST", "ORG_NO", true);

	    String str4 = paramServiceRequest.getParameter("paraFrom");
	    if ((str4 != null) && 
	      (str4.indexOf(124) == 10)) {
	      localObject1 = str4.split("\\|");
	      localObject2 = localObject1[0];
	      localObject3 = localObject1[1];
	      localObject4 = localObject1[2];
	      paramSessionInfo.addObject("begdat", localObject2);
	      paramSessionInfo.addObject("enddat", localObject3);
	      paramSessionInfo.addObject("dectyp", localObject4);
	    }

	    Object localObject1 = Charset.nullToEmpty(paramServiceRequest.getParameter("eqMap"));
	    Object localObject2 = Charset.nullToEmpty(paramServiceRequest.getParameter("currViewStr"));
	    if ((!(((String)localObject1).equals(""))) && (!(((String)localObject2).equals(""))))
	      paramSessionInfo.addObject("EQMAP_SESSION_VIEWSAVE", localObject2);

	    if (localWhereLimit != null)
	      paramDataDom.getCurrentTable().setWhereLimit(localWhereLimit);

	    Object localObject3 = null;
	    try {
	      Object localObject8;
	      Object localObject9;
	      Object localObject10;
	      Rowset localRowset1;
	      Field localField1;
	      Object localObject11;
	      Object localObject12;
	      Object localObject13;
	      Object localObject14;
	      Object localObject15;
	      Object localObject16;
	      Object localObject17;
	      Object localObject18;
	      Object localObject19;
	      Object localObject20;
	      Object localObject21;
	      Option localOption1;
	      localObject3 = Tools.getDatabase(true);
	      localObject4 = null;
	      localObject5 = paramDataDom.getRootTable();
	      str6 = "";
	      localObject6 = ((Table)localObject5).getSafeRow();

	      if (((Table)localObject5).getSafeRowColumnValue(((Table)localObject5).getPkFieldName()).equals("")) {
	        localObject7 = "";
	        localObject8 = paramSessionInfo.getUserId();
	        ((Row)localObject6).addColumn(new Column("FUN_PER", (String)localObject8));
	        try {
	          localObject7 = FormatDate.toY_M_D_H_M_S(FormatDate.getCurrDate());
	        } catch (Exception localException3) {
	        }
	        ((Row)localObject6).addColumn(new Column("FUM_DTM", (String)localObject7));
	        ((Row)localObject6).addColumn(new Column("LIM_TYP", ""));
	        ((Row)localObject6).addColumn(new Column("LIM_STA", "01"));
	        paramDataDom.getRootTable().addRow((Row)localObject6);

	        if (((Table)localObject5).getField("ORG_NO").getOptions().size() > 0) {
	          if (((Table)localObject5).getRow(0L) == null)
	            ((Table)localObject5).addRow(new Row(false, 0, 0, "new"));

	          if ((((Table)localObject5).getRow(0L).getColumn("ORG_NO") == null) || (((Table)localObject5).getRow(0L).getColumn("ORG_NO").getValue() == null) || (((Table)localObject5).getRow(0L).getColumn("ORG_NO").getValue().equals(""))) {
	            localObject4 = (Option)((Table)localObject5).getField("ORG_NO").getOptions().get(0);
	            ((Table)localObject5).getRow(0L).addColumn(new Column("ORG_NO", ((Option)localObject4).getValue()));
	          }
	        }

	        str6 = ((Table)localObject5).getSafeRowColumnValue("ORG_NO");
	        localObject9 = new RMLIMMST();
	        localObject10 = ((RMLIMMST)localObject9).getsth_byusr((String)localObject8, str6, " CRW_NO,PLA_NO ", paramServiceRequest);
	        if (localObject10 != null) {
	          ((Row)localObject6).addColumn(new Column("FUN_CRW", localObject10[0]));
	          ((Row)localObject6).addColumn(new Column("PLA_NO", localObject10[1]));
	        }
	        localRowset1 = plano(str6);
	        localField1 = paramDataDom.getRootTable().getField("PLA_NO");
	        localObject11 = paramDataDom.getRootTable().getField("RMFURNA_NO");
	        localField1.clearOption();
	        localField1.addOption("", "");
	        ((Field)localObject11).clearOption();
	        localObject12 = null;
	        localObject13 = plano(str6);
	        localObject14 = paramDataDom.getRootTable().getField("DUTPLA_NO");
	        ((Field)localObject14).clearOption();
	        ((Field)localObject14).addOption("", "");
	        localObject15 = null;
	        while (((Rowset)localObject13).next()) {
	          localObject15 = new Option(((Rowset)localObject13).getString("PLA_NAM"), ((Rowset)localObject13).getString("PLA_NO"));
	          ((Field)localObject14).addOption((Option)localObject15);
	        }
	        CMUNITMST.getSel1((Field)localObject11, str6, true);
	        localObject16 = RMLIMMST.rs_skl1(str6);
	        localObject17 = paramDataDom.getRootTable().getField("SKL_NO");
	        ((Field)localObject17).clearOption();
	        ((Field)localObject17).addOption("", "");
	        localObject18 = null;
	        while (((Rowset)localObject16).next()) {
	          localObject18 = new Option(((Rowset)localObject16).getString("SKL_NAM"), ((Rowset)localObject16).getString("SKL_NO"));
	          ((Field)localObject17).addOption((Option)localObject18);
	        }
	        while (localRowset1.next()) {
	          localObject12 = new Option(localRowset1.getString("PLA_NAM"), localRowset1.getString("PLA_NO"));
	          localField1.addOption((Option)localObject12);
	          if (localRowset1.getCurrRowIndex() == 0) {
	            localObject19 = getfieldvalue(paramDataDom, "RMLIMMST", "PLA_NO");
	            localObject20 = RMOPERMST.crwno(str6, (String)localObject19);
	            localObject21 = paramDataDom.getRootTable().getField("FUN_CRW");
	            ((Field)localObject21).clearOption();
	            localOption1 = null;
	            ((Field)localObject21).addOption("", "");
	            if ((localObject19 != null) && (!("".equals(localObject19))))
	              while (((Rowset)localObject20).next()) {
	                localOption1 = new Option(((Rowset)localObject20).getString("CRW_NAM"), ((Rowset)localObject20).getString("CRW_NO"));
	                ((Field)localObject21).addOption(localOption1);
	              }

	            String str7 = getfieldvalue(paramDataDom, "RMLIMMST", "DUTPLA_NO");
	            Rowset localRowset2 = RMOPERMST.crwno(str6, str7);
	            Field localField2 = paramDataDom.getRootTable().getField("DUT_CRW");
	            localField2.clearOption();
	            localField2.addOption("", "");
	            Option localOption2 = null;
	            label1988: if ((str7 != null) && (!("".equals(str7))))
	              while (localRowset2.next()) {
	                localOption2 = new Option(localRowset2.getString("CRW_NAM"), localRowset2.getString("CRW_NO"));
	                localField2.addOption(localOption2);
	              }

	          }

	        }

	      }
	      else
	      {
	        if (((Table)localObject5).getField("ORG_NO").getOptions().size() > 0) {
	          if (((Table)localObject5).getRow(0L) == null)
	            ((Table)localObject5).addRow(new Row(false, 0, 0, "new"));

	          if ((((Table)localObject5).getRow(0L).getColumn("ORG_NO") == null) || (((Table)localObject5).getRow(0L).getColumn("ORG_NO").getValue() == null) || (((Table)localObject5).getRow(0L).getColumn("ORG_NO").getValue().equals(""))) {
	            localObject4 = (Option)((Table)localObject5).getField("ORG_NO").getOptions().get(0);
	            ((Table)localObject5).getRow(0L).addColumn(new Column("ORG_NO", ((Option)localObject4).getValue()));
	          }
	        }
	        str6 = paramDataDom.getRootTable().getRow(0L).getColumn("ORG_NO").getValue();
	        localObject7 = plano(str6);
	        localObject8 = paramDataDom.getRootTable().getField("PLA_NO");
	        localObject9 = paramDataDom.getRootTable().getField("RMFURNA_NO");
	        ((Field)localObject8).clearOption();
	        ((Field)localObject9).clearOption();
	        ((Field)localObject8).addOption("", "");
	        localObject10 = null;
	        CMUNITMST.getSel1((Field)localObject9, str6, true);
	        localRowset1 = RMLIMMST.rs_skl1(str6);
	        localField1 = paramDataDom.getRootTable().getField("SKL_NO");
	        localField1.clearOption();
	        localField1.addOption("", "");
	        localObject11 = null;
	        while (localRowset1.next()) {
	          localObject11 = new Option(localRowset1.getString("SKL_NAM"), localRowset1.getString("SKL_NO"));
	          localField1.addOption((Option)localObject11);
	        }
	        localObject12 = plano(str6);
	        localObject13 = paramDataDom.getRootTable().getField("DUTPLA_NO");
	        ((Field)localObject13).clearOption();
	        ((Field)localObject13).addOption("", "");
	        localObject14 = null;
	        while (((Rowset)localObject12).next()) {
	          localObject14 = new Option(((Rowset)localObject12).getString("PLA_NAM"), ((Rowset)localObject12).getString("PLA_NO"));
	          ((Field)localObject13).addOption((Option)localObject14);
	        }

	        while (((Rowset)localObject7).next()) {
	          localObject10 = new Option(((Rowset)localObject7).getString("PLA_NAM"), ((Rowset)localObject7).getString("PLA_NO"));
	          ((Field)localObject8).addOption((Option)localObject10);
	        }

	        if (!(((Rowset)localObject7).isEmpty())) {
	          localObject15 = getfieldvalue(paramDataDom, "RMLIMMST", "PLA_NO");
	          localObject16 = RMOPERMST.crwno(str6, (String)localObject15);
	          localObject17 = paramDataDom.getRootTable().getField("FUN_CRW");
	          ((Field)localObject17).clearOption();
	          ((Field)localObject17).addOption("", "");
	          localObject18 = null;
	          while (((Rowset)localObject16).next()) {
	            localObject18 = new Option(((Rowset)localObject16).getString("CRW_NAM"), ((Rowset)localObject16).getString("CRW_NO"));
	            ((Field)localObject17).addOption((Option)localObject18);
	          }

	          localObject19 = getfieldvalue(paramDataDom, "RMLIMMST", "DUTPLA_NO");
	          localObject20 = RMOPERMST.crwno(str6, (String)localObject19);
	          localObject21 = paramDataDom.getRootTable().getField("DUT_CRW");
	          ((Field)localObject21).clearOption();
	          ((Field)localObject21).addOption("", "");
	          localOption1 = null;
	          while (((Rowset)localObject20).next()) {
	            localOption1 = new Option(((Rowset)localObject20).getString("CRW_NAM"), ((Rowset)localObject20).getString("CRW_NO"));
	            ((Field)localObject21).addOption(localOption1);
	          }

	        }

	        if (paramDataDom.getRootTable().getRow(0L).getColumn("PLA_NO") != null) if (paramDataDom.getRootTable().getRow(0L).getColumn("PLA_NO").getValue() != null) break label1988;
	        while (((Rowset)localObject7).next()) {
	          localObject10 = new Option(((Rowset)localObject7).getString("PLA_NAM"), ((Rowset)localObject7).getString("PLA_NO"));
	          ((Field)localObject8).addOption((Option)localObject10);
	          if (((Rowset)localObject7).getCurrRowIndex() == 0) {
	            localObject15 = getfieldvalue(paramDataDom, "RMLIMMST", "PLA_NO");

	            localObject16 = getfieldvalue(paramDataDom, "RMLIMMST", "DUTPLA_NO");

	            localObject17 = RMOPERMST.crwno(str6, (String)localObject15);
	            localObject18 = RMOPERMST.crwno(str6, (String)localObject16);
	            localObject19 = paramDataDom.getRootTable().getField("DUT_CRW");
	            localObject20 = paramDataDom.getRootTable().getField("FUN_CRW");
	            ((Field)localObject19).clearOption();
	            ((Field)localObject20).clearOption();
	            ((Field)localObject19).addOption("", "");
	            ((Field)localObject20).addOption("", "");
	            localObject21 = null;
	            localOption1 = null;
	            while (((Rowset)localObject17).next()) {
	              localObject21 = new Option(((Rowset)localObject17).getString("CRW_NAM"), ((Rowset)localObject17).getString("CRW_NO"));
	              ((Field)localObject20).addOption((Option)localObject21);
	            }
	            while (((Rowset)localObject18).next()) {
	              localOption1 = new Option(((Rowset)localObject18).getString("CRW_NAM"), ((Rowset)localObject18).getString("CRW_NO"));
	              ((Field)localObject19).addOption(localOption1);
	            }
	          }
	        }

	        localObject15 = getfieldvalue(paramDataDom, "RMLIMMST", "PLA_NO");

	        localObject16 = getfieldvalue(paramDataDom, "RMLIMMST", "DUTPLA_NO");

	        localObject17 = RMOPERMST.crwno(str6, (String)localObject15);
	        localObject18 = RMOPERMST.crwno(str6, (String)localObject16);
	        localObject19 = paramDataDom.getRootTable().getField("DUT_CRW");
	        localObject20 = paramDataDom.getRootTable().getField("FUN_CRW");
	        ((Field)localObject19).clearOption();
	        ((Field)localObject20).clearOption();
	        ((Field)localObject19).addOption("", "");
	        ((Field)localObject20).addOption("", "");
	        localObject21 = null;
	        localOption1 = null;
	        while (((Rowset)localObject17).next()) {
	          localObject21 = new Option(((Rowset)localObject17).getString("CRW_NAM"), ((Rowset)localObject17).getString("CRW_NO"));

	          ((Field)localObject20).addOption((Option)localObject21);
	        }
	        while (((Rowset)localObject18).next()) {
	          localOption1 = new Option(((Rowset)localObject18).getString("CRW_NAM"), ((Rowset)localObject18).getString("CRW_NO"));

	          ((Field)localObject19).addOption(localOption1);
	        }
	      }

	      label2166: localObject7 = paramDataDom.getRootTable().getField("DUT_PER");
	      ((Field)localObject7).clearOption();
	      setDUTPERSELECT((Field)localObject7, str6);
	    } catch (Exception localException1) {
	      Log.error(localException1);
	      paramServiceRequest.getMessage().addError(localException1.getMessage());

	      if (localObject3 == null) break label2250;
	      ((Database)localObject3).cleanup();
	    }
	    finally
	    {
	      if (localObject3 != null)
	        ((Database)localObject3).cleanup();
	    }

	    if ((paramSessionInfo.getObject("whereSQL") != null) || (!("".equals(paramSessionInfo.getObject("whereSQL")))))
	      label2250: paramSessionInfo.removeObject("whereSQL");

	    creathld(paramDataDom, paramSessionInfo, paramServiceRequest);
	    setreadonly(paramDataDom, paramSessionInfo, paramServiceRequest);

	    getWoWt(paramDataDom);
	    getWhereSQL(paramDataDom, paramSessionInfo, paramServiceRequest);
	    try {
	      if ((paramDataDom.getCurrentTable().getRow(0L) == null) || (paramDataDom.getCurrentTable().getRow(0L).getColumn("LIM_NO") == null) || (paramDataDom.getCurrentTable().getRow(0L).getColumn("LIM_NO").getValue() == null) || ("".equals(paramDataDom.getCurrentTable().getRow(0L).getColumn("LIM_NO").getValue()))) break label2406;
	      paramDataDom.getCurrentTable().refreshG(paramDataDom.getCurrentTable().getRow(0L).getColumn("LIM_NO").getValue());
	    }
	    catch (Exception localException2)
	    {
	    }
	    String str5 = paramDataDom.getTable("RMLIMMST").getSafeRowColumnValue("FLT_NO");
	    creatSel(paramDataDom.getTable("RMLIMMST").getField("FLT_NO"), "SELECT DISTINCT FLT_NAM,FLT_NO from CMFLTMST where FLT_NO='" + str5 + "'", false, false);
	    Object localObject5 = paramDataDom.getTable("RMLIMMST").getSafeRowColumnValue("SYM_NO");
	    creatSel(paramDataDom.getTable("RMLIMMST").getField("SYM_NO"), "SELECT DISTINCT SYM_NAM,SYM_NO from CMSYMMST where SYM_NO='" + ((String)localObject5) + "'", false, false);

	    if (("true".equals(paramServiceRequest.getParameter("fromSIS"))) && ("-1".equals(str1))) {
	      showModify(paramDataDom, paramSessionInfo, paramServiceRequest);
	      str6 = Charset.nullToEmpty(paramServiceRequest.getParameter("orgId"));
	      localObject6 = Database.getValue("select org_no from ogorgmst where org_id='" + str6 + "'");
	      localObject7 = paramServiceRequest.getSafeParameter("detail");

	      paramDataDom.getTable("RMLIMMST").setSafeRowColumnValue("ORG_NO", (String)localObject6);
	      paramDataDom.getTable("RMLIMMST").setSafeRowColumnValue("LIM_SHT", (String)localObject7);
	      paramDataDom.getTable("RMLIMMST").setSafeRowColumnValue("ALM_ID", paramServiceRequest.getSafeParameter("alm_id"));

	      paramServiceRequest.getMessage().addJS("fromSIS('" + Charset.nullToEmpty(paramServiceRequest.getParameter("kksCode")) + "','" + Charset.nullToEmpty((String)localObject7) + "');");
	    }
	  }
}
