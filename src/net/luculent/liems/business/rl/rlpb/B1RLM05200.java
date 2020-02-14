package net.luculent.liems.business.rl.rlpb;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import net.luculent.core.base.Charset;
import net.luculent.core.base.Log;
import net.luculent.core.database.Database;
import net.luculent.core.database.Rowset;
import net.luculent.core.exception.LiemsException;
import net.luculent.liems.action.service.ServiceRequest;
import net.luculent.liems.business.OneToManyModifyBusiness2;
import net.luculent.liems.business.cm.cmcm.DataRule;
import net.luculent.liems.business.cm.cmog.OGORGMST;
import net.luculent.liems.business.dk.wfeg.WorkFlowSql;
import net.luculent.liems.component.sequence.CommCodeNumber;
import net.luculent.liems.component.sequence.CommSequence;
import net.luculent.liems.jsdom.DataDom;
import net.luculent.liems.jsdom.element.Column;
import net.luculent.liems.jsdom.element.Row;
import net.luculent.liems.jsdom.element.Table;
import net.luculent.liems.ldk.CommMsg;
import net.luculent.liems.poi.ExcelUtil;
import net.luculent.liems.util.SessionInfo;
import net.luculent.liems.util.Tools;

public class B1RLM05200 extends OneToManyModifyBusiness2 {
    public B1RLM05200() {
    }

    public void init(DataDom dataDom, SessionInfo sessionInfo, ServiceRequest serviceRequest) {
        super.init(dataDom, sessionInfo, serviceRequest);
        DataRule.setOrg(dataDom, sessionInfo, serviceRequest, "GCYDLMQKMST", "", true);
        this.initData(dataDom, sessionInfo, serviceRequest);
        this.initSelect(dataDom, sessionInfo, serviceRequest);
    }

    public void initData(DataDom dataDom, SessionInfo sessionInfo, ServiceRequest serviceRequest) {
        Table table = dataDom.getTable("GCYDLMQKMST");
        Row row = table.getSafeRow();
        String orgNo = sessionInfo.getDefaultOrg();
        String TB_USR = Charset.nullToEmpty(Database.getValue("select TB_USR from JHYBPZLIN WHERE YBPZ_NO IN (select YBPZ_NO from JHYBPZMST WHERE TB_TYP='02' AND ST_TYP='60') AND BZ_TEXT='" + orgNo + "'"));
        String TB_TELEPHONE = Charset.nullToEmpty(Database.getValue("select TB_TELEPHONE from JHYBPZLIN WHERE YBPZ_NO IN (select YBPZ_NO from JHYBPZMST WHERE TB_TYP='02' AND ST_TYP='60') AND BZ_TEXT='" + orgNo + "'"));
        String TB_MOBILEPHONE = Charset.nullToEmpty(Database.getValue("select TB_MOBILEPHONE from JHYBPZLIN WHERE YBPZ_NO IN (select YBPZ_NO from JHYBPZMST WHERE TB_TYP='02' AND ST_TYP='60') AND BZ_TEXT='" + orgNo + "'"));
        if ("new".equals(row.getDataStatus())) {
            row.addColumn(new Column("GCLM_ID", "<自动生成编号>"));
            row.addColumn(new Column("VALID_STA", "01"));
            row.addColumn(new Column("TB_USR", TB_USR));
            row.addColumn(new Column("TB_TELEPHONE", TB_TELEPHONE));
            row.addColumn(new Column("TB_MOBILEPHONE", TB_MOBILEPHONE));
        }

    }

    public void showModify(DataDom dataDom, SessionInfo sessionInfo, ServiceRequest serviceRequest) {
        super.showModify(dataDom, sessionInfo, serviceRequest);
        this.initData(dataDom, sessionInfo, serviceRequest);
        this.initSelect(dataDom, sessionInfo, serviceRequest);
    }

    public void DetailNavigation(DataDom dataDom, SessionInfo sessionInfo, ServiceRequest serviceRequest) {
        super.DetailNavigation(dataDom, sessionInfo, serviceRequest);
        this.initSelect(dataDom, sessionInfo, serviceRequest);
    }

    public void initSelect(DataDom dataDom, SessionInfo sessionInfo, ServiceRequest serviceRequest) {
        this.creatSel(dataDom.getTable("GCYDLMQKLIN").getField("HTXZ"), "select HTXZ_NAM,HTXZ_ID from HTXZKJPZMST where IS_VALID = 'A' order by SN", false, false);
    }

    public void save(DataDom dataDom, SessionInfo sessionInfo, ServiceRequest serviceRequest) {
        Table table = dataDom.getTable("GCYDLMQKMST");
        Row row = table.getSafeRow();
        String pkValue = table.getPkValue();
        String orgNo = table.getSafeRowColumnValue("ORG_NO");
        String date = table.getSafeRowColumnValue("GCLM_JHYF");
        String checkSql = "select count(1) from GCYDLMQKMST where GCLM_JHYF = '" + date + "' and ORG_NO = '" + orgNo + "' and VALID_STA in ('01','02','03') ";
        if (row.getDataStatus().equals("new")) {
            CommCodeNumber cn = new CommCodeNumber("GCLMID", sessionInfo.getCurrentLanguage(), sessionInfo.getUserId());
            row.addColumn("GCLM_ID", Charset.nullToEmpty(cn.getNextValue()));
            String CST_NO = Database.getPrepareSqlValue("SELECT cstno from vw_users where REGULAR_FLG = '1' and ID=?", new Object[]{sessionInfo.getUserId()});
            this.setColumnValue(row, "CST_NO", CST_NO);
        } else {
            checkSql = checkSql + " and GCLM_NO <> '" + pkValue + "'";
        }

        row.setFirstLastUserInfo(sessionInfo.getUserId());

        try {
            String res = Database.getPrepareSqlValue(checkSql, new Object[0]);
            if (!"0".equals(res)) {
                serviceRequest.getMessage().addError(OGORGMST.getDesc(orgNo) + date + "已存在未生效记录");
                return;
            }

            super.save(dataDom, sessionInfo, serviceRequest);
            this.updateInfo(dataDom, sessionInfo, serviceRequest);
        } catch (Exception var12) {
            Log.error(var12);
            serviceRequest.getMessage().setError(CommMsg.getMessage("LIEMSMSG000005", sessionInfo, "保存失败，请重试或联系系统管理员！"));
        }

    }

    public void Delete(DataDom dataDom, SessionInfo sessionInfo, ServiceRequest serviceRequest) {
        Table table = dataDom.getTable("GCYDLMQKMST");
        Row row = table.getRow(0L);
        if (row == null) {
            serviceRequest.getMessage().setInfo(CommMsg.getMessage("LIEMSMSG000006", sessionInfo, "没有需要删除的数据！"));
        } else {
            String pkValue = table.getPkValue();
            if (Charset.nullToEmpty(pkValue).equals("")) {
                serviceRequest.getMessage().setInfo(CommMsg.getMessage("LIEMSMSG000006", sessionInfo, "没有需要删除的数据！"));
            } else {
                super.deleteRootChildTable(dataDom, sessionInfo, serviceRequest);
            }
        }
    }

    public void deleteItem(DataDom dataDom, SessionInfo sessionInfo, ServiceRequest serviceRequest) {
        try {
            super.deleteItem(dataDom, sessionInfo, serviceRequest);
        } catch (LiemsException var5) {
            Log.error(var5);
        }

    }

    public String getSta(String params, HttpServletRequest request) {
        String result = "";
        params = params.replaceAll("\\|", ",");
        String sql = " select count(1) from GCYDLMQKLIN where GCLML_NO in (" + params + " ) and VALID_STA = '02'";

        try {
            String num = Database.getPrepareSqlValue(sql, new Object[0]);
            if (!"0".equals(num)) {
                result = "true";
            }
        } catch (Exception var6) {
            Log.error(var6);
        }

        return result;
    }

    public void insertData(DataDom dataDom, SessionInfo sessionInfo, ServiceRequest serviceRequest) {
        Table table = dataDom.getTable("GCYDLMQKMST");
        String pkls = Charset.nullToEmpty(serviceRequest.getSafeParameter("pkls"));
        Database db = null;

        try {
            db = Tools.getDatabase(false);
            String[] pkl = pkls.split("\\|");
            pkls = pkls.replaceAll("\\|", "','");
            String pk = Database.getPrepareSqlValue(db, "select MTCG_NO from MTCGTJMST where IS_FLG = '01'", new Object[0]);
            if (!"".equals(pk)) {
                CommSequence cs = CommSequence.getInstance("MTCGTJLIN_SEQ");
                String sql = "insert into MTCGTJLIN (MTCGL_NO,MTCG_NO,ORGL_NO,START_DAT,PARV_DAT,RLVEN_NO,CG_TYP,HTXZ,TRSTYP,SHIP_NUM,COAL_NO,JHNY,JK_FLG,ZHG_QTY,XHG_QTY,SLC,ZHG_LF,XHG_LF,LFC,ZHG_SF,XHG_SF,SFC,MTJS_QTY,YFJS_QTY,ZHG_FRL,XHG_FRL,RZC,JSRZ,HTJ,RZJFDJ,LFJFDJ,QTJF,PCJSDJ,PCBMJ,ZQ_DTM,ZQF,MDMYF,DCYMJ,DCBMJ,PCJSZJ,ZYF,DCYMZJ,LF,HF,HFF,QS,HTZJ,JSZRZ,LFZL,HFZL,HFFZL,QSZL,QE_FLG,COAL_NAM,SHIP_HC,CNDBH,SH_DESC,ZHG_ZRL,XHG_ZRL,CX_FLG)(SELECT ?,?,ORGL_NO,START_DAT,PARV_DAT,RLVEN_NO,CG_TYP,HTXZ,TRSTYP,SHIP_NUM,COAL_NO,JHNY,JK_FLG,ZHG_QTY,XHG_QTY,SLC,ZHG_LF,XHG_LF,LFC,ZHG_SF,XHG_SF,SFC,MTJS_QTY,YFJS_QTY,ZHG_FRL,XHG_FRL,RZC,JSRZ,HTJ,RZJFDJ,LFJFDJ,QTJF,PCJSDJ,PCBMJ,ZQ_DTM,ZQF,MDMYF,DCYMJ,DCBMJ,PCJSZJ,ZYF,DCYMZJ,LF,HF,HFF,QS,HTZJ,JSZRZ,LFZL,HFZL,HFFZL,QSZL,'01',COAL_NAM,SHIP_HC,CNDBH,SH_DESC,ZHG_ZRL,XHG_ZRL,CX_FLG FROM GCYDLMQKLIN WHERE GCLML_NO = ?)";
                String sql1 = "UPDATE GCYDLMQKLIN SET VALID_STA = '02' WHERE GCLML_NO = ?";

                for(int i = 0; i < pkl.length; ++i) {
                    db.execPrepareSqlUpdate(sql1, new Object[]{pkl[i]});
                }
            }

            db.commit();
            serviceRequest.getMessage().addInfo("数据取入成功");
            dataDom.getTable("GCYDLMQKLIN").refresh("B1RLM05200", sessionInfo);
        } catch (Exception var16) {
            Log.error(var16);
            if (db != null) {
                db.rollback();
            }
        } finally {
            if (db != null) {
                db.cleanup();
            }

        }

    }

    public String getCXFlg(String params, HttpServletRequest request) {
        Rowset rs = null;
        String result = "02";
        String sql = " select CX_FLG from HTXZKJPZMST where HTXZ_ID = '" + params + "' ";

        try {
            rs = Database.getRowset(sql);
            if (rs.next()) {
                result = Charset.nullToEmpty(rs.getString("CX_FLG"));
            }
        } catch (Exception var7) {
            Log.error(var7);
        }

        return result;
    }

    public String getSHDW(String params, HttpServletRequest request) {
        Rowset rs = null;
        String no = "";
        String desc = "";
        String sql = " select JCDC_NO,JCDC_NAM from RLJCDCMST where IS_VALID = 'A' and DC_NO = '" + params + "' ";

        try {
            rs = Database.getRowset(sql);
            if (rs.next()) {
                no = Charset.nullToEmpty(rs.getString("JCDC_NO"));
                desc = Charset.nullToEmpty(rs.getString("JCDC_NAM"));
            }
        } catch (Exception var8) {
            Log.error(var8);
        }

        return no + "@" + desc;
    }

    public void updateInfo(DataDom dataDom, SessionInfo sessionInfo, ServiceRequest serviceRequest) {
        Table table = dataDom.getTable("GCYDLMQKMST");
        String pkValue = table.getPkValue();
        Database db = null;

        try {
            db = Tools.getDatabase(false);
            String sql = "update GCYDLMQKLIN set RZC = decode(nvl(JSRZ,0)*nvl(XHG_FRL,0),0,null,nvl(JSRZ,0)-nvl(XHG_FRL,0)), SLC = decode(nvl(ZHG_QTY,0)*nvl(XHG_QTY,0),0,null,nvl(ZHG_QTY,0)-nvl(XHG_QTY,0)), CGS = decode(nvl(MTJS_QTY,0)*nvl(JSRZ,0)*nvl(ZHG_QTY,0)*nvl(XHG_QTY,0),0,null,(1-nvl(XHG_QTY,0)*nvl(XHG_FRL,0)/(nvl(MTJS_QTY,0)*nvl(JSRZ,0)))*100) , PCJSDJ = decode(nvl(HTJ,0)*nvl(RZJFDJ,0)*nvl(LFJFDJ,0)*nvl(QTJF,0),0,null,nvl(HTJ,0)+nvl(RZJFDJ,0)+nvl(LFJFDJ,0)+nvl(QTJF,0)), PCBMJ = decode(nvl(JSRZ,0)*nvl(HTJ,0)*nvl(RZJFDJ,0)*nvl(LFJFDJ,0)*nvl(QTJF,0),0,null,(nvl(HTJ,0)+nvl(RZJFDJ,0)+nvl(LFJFDJ,0)+nvl(QTJF,0))*7000/nvl(JSRZ,0)), ZYF = decode(nvl(ZQF,0)*nvl(YCYF,0)*nvl(ECYF,0)*nvl(ZBF,0)*nvl(GKF,0)*nvl(QTF,0),0,null,nvl(ZQF,0)+nvl(YCYF,0)+nvl(ECYF,0)+nvl(ZBF,0)+nvl(GKF,0)+nvl(QTF,0)), MDMYF = decode(nvl(MTJS_QTY,0)*nvl(ZQF,0)*nvl(YCYF,0)*nvl(ECYF,0)*nvl(ZBF,0)*nvl(GKF,0)*nvl(QTF,0),0,null,(nvl(ZQF,0)+nvl(YCYF,0)+nvl(ECYF,0)+nvl(ZBF,0)+nvl(GKF,0)+nvl(QTF,0))/nvl(MTJS_QTY,0)), DCYMJ = decode(nvl(MTJS_QTY,0)*nvl(ZQF,0)*nvl(YCYF,0)*nvl(ECYF,0)*nvl(ZBF,0)*nvl(GKF,0)*nvl(QTF,0)*nvl(HTJ,0)*nvl(RZJFDJ,0)*nvl(LFJFDJ,0)*nvl(QTJF,0),0,null,nvl(HTJ,0)+nvl(RZJFDJ,0)+nvl(LFJFDJ,0)+nvl(QTJF,0)+(nvl(ZQF,0)+nvl(YCYF,0)+nvl(ECYF,0)+nvl(ZBF,0)+nvl(GKF,0)+nvl(QTF,0))/nvl(MTJS_QTY,0)) where GCLM_NO = ?";
            db.execPrepareSqlUpdate(sql, new Object[]{pkValue});
            db.commit();
            dataDom.refresh(sessionInfo);
        } catch (Exception var11) {
            db.rollback();
            Log.error(var11);
        } finally {
            if (db != null) {
                db.cleanup();
            }

        }

    }

    public void impExcel(DataDom dataDom, SessionInfo sessionInfo, ServiceRequest serviceRequest) throws LiemsException {
        String filePath = serviceRequest.getSafeParameter("filePath");
        String fieldList = serviceRequest.getSafeParameter("zdArr");
        int startRow = Integer.parseInt(serviceRequest.getSafeParameter("startRow"));
        int startCol = Integer.parseInt(serviceRequest.getSafeParameter("startCol"));
        Database db = null;

        try {
            db = Tools.getDatabase(false);
            Table table = dataDom.getTable("GCYDLMQKMST");
            String pkValue = table.getPkValue();
            List<Map<String, String>> dataList = (new ExcelUtil()).excelToList(filePath, fieldList, startRow, startCol);
            if (dataList != null && !"null".equals(dataList)) {
                int length = dataList.size();
                if (length > 0) {
                	String insertSql = "insert into GCYDLMQKLIN(GCLML_NO,GCLM_NO,VALID_STA,DATA_STA,ORGL_NO,CNDBH,JHNY,PARV_DAT,START_DAT,TRSTYP,YSS,SHIP_NUM,CG_TYP,HTXZ,RLVEN_NO,COAL_NAM,RLVEN_NO_NX,COAL_NAM_NX,HTRZ,JSRZ,XHG_FRL,RZC,MTJS_QTY,XHG_QTY,SLC,ZHG_ZRL,XHG_ZRL,CGS,HTJ,RZJFDJ,LFJFDJ,QTJF,PCJSDJ,PCBMJ,ZQ_DTM,ZQF,YCYF,ECYF,ZBF,GKF,QTF,ZYF,MDMYF,DCYMJ,DCBMJ,PCJSZJ,DCYMZJ,HTZJ,JSZRZ,LF,HF,HFF,QS,LFZL,HFZL,HFFZL,QSZL)values(?,?,'01',(select prtl_id from pgprtlin where prt_no = (select prt_no from pgprtmst where ent_nam = 'GCYDLMQKLIN' and prt_id = 'DATA_STA' and LANG_ID = 'CHZ') and prtl_nam = ?),(select JCDC_NO from RLJCDCMST where JCDC_NAM = ?),?,?,to_date(?,'yyyy-MM-dd'),to_date(?,'yyyy-MM-dd'),(select prtl_id from pgprtlin where prt_no = (select prt_no from pgprtmst where ent_nam = 'GCYDLMQKLIN' and prt_id = 'TRSTYP' and LANG_ID = 'CHZ') and prtl_nam =?),(select YSGYS_NO from YSGYSMST where IS_VALID = 'A' and YSGYS_NAM = ?),?,(select prtl_id from pgprtlin where prt_no = (select prt_no from pgprtmst where ent_nam = 'GCYDLMQKLIN' and prt_id = 'CG_TYP' and LANG_ID = 'CHZ') and prtl_nam = ?),(select HTXZ_ID from HTXZKJPZMST where IS_VALID = 'A' AND HTXZ_NAM= ?),(SELECT CXGYS_NO FROM CXGYSMST WHERE IS_VALID = 'A' and CXGYS_NAM = ?),(select JCCOAL_NO from JCCOALMST where VALID_STA = '01' and JCCOAL_NAM = ?),(SELECT CXGYS_NO FROM CXGYSMST WHERE CXGYS_NAM = ?),(select JCCOAL_NO from JCCOALMST where VALID_STA = 'A' and JCCOAL_NAM = ?),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    Map<String, String> map = null;
                    CommSequence cs = CommSequence.getInstance("GCYDLMQKLIN_SEQ");

                    for(int i = 0; i < length; ++i) {
                        map = (Map)dataList.get(i);
                        String A = (String)map.get("A");
                        String B = (String)map.get("B");
                        String C = (String)map.get("C");
                        String D = (String)map.get("D");
                        String E = (String)map.get("E");
                        String F = (String)map.get("F");
                        String G = (String)map.get("G");
                        String H = (String)map.get("H");
                        String I = (String)map.get("I");
                        String J = (String)map.get("J");
                        String K = (String)map.get("K");
                        String L = (String)map.get("L");
                        String M = (String)map.get("M");
                        String N = (String)map.get("N");
                        String O = (String)map.get("O");
                        String P = (String)map.get("P");
                        String Q = (String)map.get("Q");
                        String R = (String)map.get("R");
                        String S = (String)map.get("S");
                        String T = (String)map.get("T");
                        String U = (String)map.get("U");
                        String V = (String)map.get("V");
                        String W = (String)map.get("W");
                        String X = (String)map.get("X");
                        String Y = (String)map.get("Y");
                        String Z = (String)map.get("Z");
                        String AA = (String)map.get("AA");
                        String AB = (String)map.get("AB");
                        String AC = (String)map.get("AC");
                        String AD = (String)map.get("AD");
                        String AE = (String)map.get("AE");
                        String AF = (String)map.get("AF");
                        String AG = (String)map.get("AG");
                        String AH = (String)map.get("AH");
                        String AI = (String)map.get("AI");
                        String AJ = (String)map.get("AJ");
                        String AK = (String)map.get("AK");
                        String AL = (String)map.get("AL");
                        String AM = (String)map.get("AM");
                        String AN = (String)map.get("AN");
                        String AO = (String)map.get("AO");
                        String AP = (String)map.get("AP");
                        String AQ = (String)map.get("AQ");
                        String AR = (String)map.get("AR");
                        String AS = (String)map.get("AS");
                        String AT = (String)map.get("AT");
                        String AU = (String)map.get("AU");
                        String AV = (String)map.get("AV");
                        String AW = (String)map.get("AW");
                        String AX = (String)map.get("AX");
                        String AY = (String)map.get("AY");
                        String AZ = (String)map.get("AZ");
                        String BA = (String)map.get("BA");
                        String BB = (String)map.get("BB");
                        String BC = (String)map.get("BC");
                        String pk = cs.getNextValue();
                        //String yssSta = Database.getValue(db, "select IS_VALID from YSGYSMST where YSGYS_NAM = '" + I + "'");
                        //if ("A".equals(yssSta)) {
                            db.execPrepareSqlUpdate(insertSql, new Object[]{pk, pkValue, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, AT, AU, AV, AW, AX, AY, AZ, BA, BB, BC});
                        //}
                    }

                    db.commit();
                    dataDom.refresh(sessionInfo);
                    serviceRequest.getMessage().addInfo("导入数据成功!");
                }
            }
        } catch (Exception var77) {
            if (db != null) {
                db.rollback();
            }

            Log.error(var77);
            serviceRequest.getMessage().addInfo("导入数据失败!");
        } finally {
            if (db != null) {
                db.cleanup();
            }

        }

    }

    public static String UpdateGCYDLMQKMSTSta(String userid, String objectno, String instanceno, String tableid, String prkey, WorkFlowSql wfs, String param) {
        String sql = "UPDATE GCYDLMQKLIN SET VALID_STA = '02' WHERE GCLM_NO = '" + prkey + "'";
        wfs.setSqlList(sql);
        return "true";
    }

    public static String UpdateWXSta(String userid, String objectno, String instanceno, String tableid, String prkey, WorkFlowSql wfs, String param) {
        String sql = "update GCYDLMQKMST set VALID_STA = '10' where GCLM_JHYF = (select GCLM_JHYF from GCYDLMQKMST where GCLM_NO = '" + prkey + "') " + "and ORG_NO = (select ORG_NO from GCYDLMQKMST where GCLM_NO = '" + prkey + "') " + "and GCLM_NO <> '" + prkey + "'";
        String sql2 = "update GCYDLMQKLIN set VALID_STA = '03' where GCLM_NO in (select GCLM_NO from GCYDLMQKMST where GCLM_JHYF = (select GCLM_JHYF from GCYDLMQKMST where GCLM_NO = '" + prkey + "') " + "and ORG_NO = (select ORG_NO from GCYDLMQKMST where GCLM_NO = '" + prkey + "') and GCLM_NO <> '" + prkey + "' )";
        wfs.setSqlList(sql);
        wfs.setSqlList(sql2);
        return "true";
    }

    public static String getSelByCGTYP(String data, HttpServletRequest request) {
        StringBuffer sb = new StringBuffer();

        try {
            if (data != null) {
                if ("01".equals(data)) {
                    sb = Tools.getSelectJsonForWz("select HTXZ_NAM,HTXZ_ID from HTXZKJPZMST where IS_VALID = 'A' and HTXZ_ID in('01','06','07') order by SN", true);
                } else if ("02".equals(data)) {
                    sb = Tools.getSelectJsonForWz("select HTXZ_NAM,HTXZ_ID from HTXZKJPZMST where IS_VALID = 'A' order by SN", true);
                }
            }
        } catch (Exception var4) {
            Log.error(var4);
            sb = new StringBuffer();
        }

        return sb.toString();
    }
}
