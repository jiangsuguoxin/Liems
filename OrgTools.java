package net.luculent.liems.business.ba.report;

import java.util.LinkedHashMap;
import net.luculent.core.base.Log;
import net.luculent.core.database.DBException;
import net.luculent.core.database.Database;
import net.luculent.core.database.Rowset;
import net.luculent.liems.action.service.ServiceRequest;
import net.luculent.liems.jsdom.DataDom;
import net.luculent.liems.jsdom.element.Field;
import net.luculent.liems.jsdom.element.Option;
import net.luculent.liems.jsdom.element.Select;
import net.luculent.liems.util.SessionInfo;
import net.luculent.liems.util.Tools;

public class OrgTools {

	public void initJhOrgNo(final DataDom dataDom, final SessionInfo sessionInfo, final ServiceRequest serviceRequest, final Select jhOrgF) {
        final String startOrg = this.getStartorg(sessionInfo);
        String sql1 = "select lpad('-', (level - 1) *4, '-') || ORG_DSC ORG_DSC_STR,JORG_NO,orgup_no  FROM JHORGMST start with JORG_NO in( " + startOrg + ") connect by PRIOR jorg_no = orgup_no  ORDER BY SORT_NO asc";
        final String sql2 = "select nvl(orgup_no,-1),jorg_no from jhorgmst order by nvl(orgup_no,-1),sort_no asc";
        String sql3 = "select\u3000jorg_no\u3000 FROM JHORGMST WHERE jorg_no in (" + startOrg + ") ORDER BY SORT_NO asc";
        final String orgNo = this.isGroup(sessionInfo);
        if ("true".equals(orgNo)) {
            sql1 = "select lpad('-', (level - 1) * 4, '-') || ORG_DSC ORG_DSC_STR,JORG_NO,orgup_no  FROM JHORGMST start with orgup_no is null connect by PRIOR jorg_no = orgup_no  ORDER BY SORT_NO asc";
            sql3 = "select\u3000jorg_no\u3000 FROM JHORGMST WHERE ORGUP_NO is null ORDER BY SORT_NO asc";
        }
        this.creatSelOrg(jhOrgF, sql1, sql2, sql3, false, false);
    }
    
    public String getStartorg(final SessionInfo sessionInfo) {
        Database db = null;
        String res = "";
        try {
            db = Tools.getDatabase(false);
            String orgNo = "";
            final Option[] orgNos = sessionInfo.getOrg();
            for (int i = 0; i < orgNos.length; ++i) {
                orgNo = String.valueOf(orgNo) + ',' + orgNos[i].getValue();
            }
            orgNo = orgNo.substring(1);
            final String querysql = "SELECT A.JORG_NO FROM JHORGMST A WHERE A.MISORG_NO IN ( " + orgNo + " ) ORDER BY A.SEQ_NO ASC";
            final Rowset rs = db.getRS(querysql);
            while (rs.next()) {
                res = String.valueOf(res) + "," + rs.getSafeString(1);
            }
            if (!"".equals(res)) {
                res = res.substring(1);
                res = this.getReal(res, db);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            db.rollback();
            return res;
        }
        finally {
            if (db != null) {
                db.cleanup();
            }
        }
        if (db != null) {
            db.cleanup();
        }
        return res;
    }
    
    public String isGroup(final SessionInfo sessionInfo) {
        String res = "false";
        String orgNo = "";
        final Option[] orgNos = sessionInfo.getOrg();
        for (int i = 0; i < orgNos.length; ++i) {
            orgNo = String.valueOf(orgNo) + ',' + orgNos[i].getValue();
        }
        if (orgNo.indexOf("11") != -1) {
            res = "true";
        }
        return res;
    }
    
    public void creatSelOrg(final Select jhOrgF, final String strSQL, final String sql2, final String sql3, final boolean canNull, final boolean canAll) {
        Database db = null;
        try {
            db = Tools.getDatabase(false);
            final Rowset rs1 = db.getRS(strSQL);
            final Rowset rs2 = db.getRS(sql2);
            final Rowset rs3 = db.getRS(sql3);
            jhOrgF.ClearAll();
            if (canNull) {
                jhOrgF.addOption("", "");
            }
            if (canAll) {
                jhOrgF.addOption("\u5168\u90e8", "-1");
            }
            if (rs1 != null && rs2 != null && rs3 != null) {
                final LinkedHashMap Rdlink = new LinkedHashMap();
                final LinkedHashMap Rvalue = new LinkedHashMap();
                while (rs1.next()) {
                    Rdlink.put(rs1.getSafeString(2), rs1.getSafeString(3));
                    Rvalue.put(rs1.getSafeString(2), rs1.getSafeString(1));
                }
                final LinkedHashMap Rulink = this.getSelmap2(rs2, 2, 1);
                while (rs3.next()) {
                    final Option opt = new Option(String.valueOf(Rvalue.get(rs3.getSafeString(1))), rs3.getSafeString(1));
                    jhOrgF.addOption(opt);
                    this.initDown(Rulink, Rvalue, rs3.getSafeString(1), jhOrgF);
                }
            }
        }
        catch (Exception e) {
            Log.error(e);
            db.rollback();
            return;
        }
        finally {
            if (db != null) {
                db.cleanup();
            }
        }
        if (db != null) {
            db.cleanup();
        }
    }
    
    public LinkedHashMap getSelmap2(final Rowset rs, final int key, final int value) throws DBException {
        final LinkedHashMap res = new LinkedHashMap();
        String curup = "";
        String downNos = "";
        while (rs.next()) {
            if ("".equals(curup)) {
                curup = rs.getSafeString(1);
            }
            if (curup.equals(rs.getSafeString(1))) {
                if ("".equals(downNos)) {
                    downNos = rs.getSafeString(2);
                }
                else {
                    downNos = String.valueOf(downNos) + "@@" + rs.getSafeString(2);
                }
            }
            else {
                if (curup.equals(rs.getSafeString(1))) {
                    continue;
                }
                res.put(curup, downNos);
                curup = rs.getSafeString(1);
                downNos = rs.getSafeString(2);
            }
        }
        res.put(curup, downNos);
        return res;
    }
    
    public void initDown(final LinkedHashMap valueMap, final LinkedHashMap optionMap, final String key, final Select field) {
        if (valueMap.get(key) == null) {
            return;
        }
        final String[] sons = String.valueOf(valueMap.get(key)).split("@@", -1);
        for (int i = 0; i < sons.length; ++i) {
            final Option opt = new Option(String.valueOf(optionMap.get(sons[i])), sons[i]);
            field.addOption(opt);
            this.initDown(valueMap, optionMap, sons[i], field);
        }
    }
    
    public void initDown(final LinkedHashMap valueMap, final LinkedHashMap optionMap, final String key, final Field field) {
        if (valueMap.get(key) == null) {
            return;
        }
        final String[] sons = String.valueOf(valueMap.get(key)).split("@@", -1);
        for (int i = 0; i < sons.length; ++i) {
            final Option opt = new Option(String.valueOf(optionMap.get(sons[i])), sons[i]);
            field.addOption(opt);
            this.initDown(valueMap, optionMap, sons[i], field);
        }
    }
    
    public String getReal(final String MisOrgs, final Database db) throws Exception {
        String res = "";
        final String[] param = MisOrgs.split(",", -1);
        if (param.length <= 1) {
            return MisOrgs;
        }
        final String sql = "select jorg_no from jhorgmst where jorg_no in (" + MisOrgs + ") and orgup_no not in (" + MisOrgs + ") order by nvl(orgup_no,-1),sort_no asc";
        final Rowset rs = db.getRS(sql);
        while (rs.next()) {
            res = String.valueOf(res) + "," + rs.getSafeString(1);
        }
        res = res.substring(1);
        return res;
    }
}
