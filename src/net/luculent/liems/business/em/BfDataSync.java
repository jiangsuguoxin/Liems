package net.luculent.liems.business.em;

import java.util.ArrayList;

import net.luculent.core.base.FormatDate;
import net.luculent.core.base.Log;
import net.luculent.core.database.Database;
import net.luculent.core.database.Rowset;
import net.luculent.liems.business.bf.engine.BaseBfForm;
import net.luculent.liems.business.bf.engine.BfPageInfo;
import net.luculent.liems.business.bf.engine.BfRow;
import net.luculent.liems.business.bf.engine.BfTable;
import net.luculent.liems.component.sequence.CommSequence;
import net.luculent.liems.util.Tools;

public class BfDataSync {
	public String createBf(BaseBfForm bf, String params) {
		String[] ss = params.split(";");//";"分隔符可以自定义，只要与传递参数的分隔符一致即可。
		String tblName = ss[0];
		String BFFRMED_NO = ss[1];
		
		CommSequence cs = CommSequence.getInstance(tblName+"_SEQ");
	    String BFFRMIN_NO = cs.getNextValue();
		
		BfPageInfo bpi = bf.getPageInfo();// 表单基础信息对象
		String FSTUSR_ID = bpi.getCurrUser();// 当前登录用户		
		String FRMTIME = bpi.getFrmTime();// 表单小时报的小时时间;	
		String BFFORM_DTM = bpi.getFormDtm();// 当前表单数据日期，日期格式（日报、小时报、班报、// 周报：yyyy-mm-dd；月报：yyyy-mm；年报：yyyy）	

		String BFFRMIN_STA = bpi.getFrmInSta();// 表单当前状态值		
		String ORG_NO = bpi.getOrgNo();// 表单当前记录所属公司		
		String RMSHIFT_ID = bpi.getRmShiftId();// 当前表单班次		
		String RMTEAM_ID = bpi.getRmTeamId();// 当前表单值次	
		
		String FSTUSR_DTM = FormatDate.toY_M_D_H_M_S(FormatDate.getCurrDate());
		
		//获取表单主表控件值
		BfTable tbl = bf.getRootTable();//获取表单主表对象
		ArrayList<BfRow> rows = tbl.getRows();//获取当前表单行记录对象（一 条表单记录主表记录只有一条，子表明细记录可以有多条）
		BfRow row = rows.get(0);//遍历得到行记录对象主表只有一条即只会遍历 一次，子表可以有多条
		String load = row.getColumnByName("tab0_select1").getColVal();//负荷
		String time = "";

		if("350".equals(load)){
			time = "2019-02-24 16:00:00";
		} else if("260".equals(load)){
			time = "2019-02-24 15:00:00";
		} else if("180".equals(load)){
			time = "2019-02-24 14:00:00";
		} else if("0".equals(load)){
			time = "2019-02-24 12:00:00";
		} else if("240".equals(load)){
			time = "2019-02-24 11:00:00";
		} else if("220".equals(load)){
			time = "2019-02-24 10:00:00";
		} else if("200".equals(load)){
			time = "2019-02-24 9:00:00";
		} 
		
		String selectSQL = "";
		String insertSQL = "";
		selectSQL = getSelectSQL(BFFRMED_NO);
		
		Database localDatabase = null;
		try {
			localDatabase = Tools.getDatabase(true);
			
			insertSQL = "insert into " + tblName + " (BFFRMIN_NO,BFFRMED_NO,BFFORM_DTM,RMSHIFT_ID,RMTEAM_ID,BFFRMIN_STA,FRMTIME,FSTUSR_ID,FSTUSR_DTM,ORG_NO,"
					+ selectSQL +") select '"
					+ BFFRMIN_NO + "','" 
					+ BFFRMED_NO + "',liems_to_date('"
					+ BFFORM_DTM + "','yyyy-mm-dd'),'"
					+ RMSHIFT_ID + "','"
					+ RMTEAM_ID + "','"
					+ BFFRMIN_STA + "',liems_to_date('"
					+ FRMTIME + "','yyyy-mm-dd hh24:mi:ss'),'"
					+ FSTUSR_ID + "',liems_to_date('"
					+ FSTUSR_DTM + "','yyyy-mm-dd hh24:mi:ss'),'"
					+ ORG_NO + "',"
					+ selectSQL + " from " + tblName + " where FRMTIME = to_date('"+ time +"','yyyy-mm-dd hh24:mi:ss')";
			localDatabase.execSqlUpdate(insertSQL);
			return "已建立";
		} catch (Exception localException) {
			Log.info(localException);
		} finally {
			if (localDatabase != null)
				localDatabase.cleanup();
		}
		return "出错，请联系管理员";
	}
	
	public String getSelectSQL(String BFFRMED_NO){
		String selectSQL = "";
		Database localDatabase = null;
		localDatabase = Tools.getDatabase(true);
		Rowset localRowset;
		try {
			localRowset = localDatabase.getRS("select BFFRMST_ID from bffrmstmst where BFFRMED_NO ='" + BFFRMED_NO + "'");
			int i=0;
			int n = localRowset.getRowCount();
			while(localRowset.next()){
				i++;
				selectSQL += localRowset.getString("BFFRMST_ID");
				if(i<n){
					selectSQL += ",";
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (localDatabase != null)
				localDatabase.cleanup();
		}
		return selectSQL;
	}
}
