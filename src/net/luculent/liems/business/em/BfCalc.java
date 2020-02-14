package net.luculent.liems.business.em;

import java.util.ArrayList;

import net.luculent.core.base.Log;
import net.luculent.core.database.Database;
import net.luculent.core.database.Rowset;
import net.luculent.liems.business.bf.engine.BaseBfForm;
import net.luculent.liems.business.bf.engine.BfColumn;
import net.luculent.liems.business.bf.engine.BfPageInfo;
import net.luculent.liems.business.bf.engine.BfRow;
import net.luculent.liems.business.bf.engine.BfTable;
import net.luculent.liems.component.sequence.CommSequence;
import net.luculent.liems.util.Tools;

public class BfCalc {
	/**   
	* 表单控件计算时调用的脚本方法   
	* @param bf 扩展脚本必须存在的形参   
	* @param params 如（"a;b;c"） 
	* @return   
	*/ 
	public String DemoVal(BaseBfForm bf, String params){
		String[] ss = params.split(";");//";"分隔符可以自定义，只要与传递参数的分隔符一致即可。
		String s0 = ss[0];
		String s1 = ss[1];
		String s2 = ss[2]; 
		
		String val = "";
		String s = "";
		BfPageInfo bpi = bf.getPageInfo();//表单基础信息对象
		
		//获取表单主表控件值
		BfTable tbl = bf.getRootTable();//获取表单主表对象
		ArrayList<BfRow> rows = tbl.getRows();//获取当前表单行记录对象（一 条表单记录主表记录只有一条，子表明细记录可以有多条）
		for(int i=0;i<rows.size();i++){    
			BfRow row = rows.get(i);//遍历得到行记录对象主表只有一条即只会遍历 一次，子表可以有多条
			String textId = "tab0_text1";
			BfColumn col = row.getColumnByName(textId);//根据控件id获取控件对象
			val = col.getColVal();//获取控件的值。
		} 
		return val;
	}

	public String getNomByCin(BaseBfForm bf){
		String val = "";
		
		//获取表单主表控件值
		BfTable tbl = bf.getRootTable();//获取表单主表对象
		ArrayList<BfRow> rows = tbl.getRows();//获取当前表单行记录对象（一 条表单记录主表记录只有一条，子表明细记录可以有多条）
		for(int i=0;i<rows.size();i++){    
			BfRow row = rows.get(i);//遍历得到行记录对象主表只有一条即只会遍历 一次，子表可以有多条
			String textId = "tab0_text95";
			BfColumn col = row.getColumnByName(textId);//根据控件id获取控件对象
			val = col.getColVal();//获取控件的值。
		} 
		Database localDatabase = null;
		String nom = "";
		try {
			localDatabase = Tools.getDatabase(true);
			Rowset localRowset = localDatabase.getRS("select tab0_text4 from B1BFG40022mst where tab0_text5 ='"+ val + "'");
			if (!(localRowset.next())) {}
				nom = localRowset.getString("tab0_text4");
			return nom;
		} catch (Exception localException) {
			Log.info(localException);
		} finally {
			if (localDatabase != null)
				localDatabase.cleanup();
		}
		return "请检查身份证号";
	}
	
	public String getTime1(BaseBfForm bf){		
		double time = 0;

		//获取表单主表控件值
		BfTable tbl = bf.getRootTable();//获取表单主表对象
		ArrayList<BfRow> rows = tbl.getRows();//获取当前表单行记录对象（一 条表单记录主表记录只有一条，子表明细记录可以有多条）
		for(int i=0; i<rows.size(); i++){    
			BfRow row = rows.get(i);//遍历得到行记录对象主表只有一条即只会遍历 一次，子表可以有多条
			for(int n=1; n<=31; n++){
				BfColumn col = row.getColumnByName("tab0_text"+n);//根据控件id获取控件对象
				String str = col.getColVal();//获取控件的值。
				if(str!=null && !"".equals(str)){
					time += Double.parseDouble(str);
				}
			}	
		} 
		return Double.toString(time);
	}
	
	public String getTime2(BaseBfForm bf){		
		double time = 0;

		//获取表单主表控件值
		BfTable tbl = bf.getRootTable();//获取表单主表对象
		ArrayList<BfRow> rows = tbl.getRows();//获取当前表单行记录对象（一 条表单记录主表记录只有一条，子表明细记录可以有多条）
		for(int i=0; i<rows.size(); i++){    
			BfRow row = rows.get(i);//遍历得到行记录对象主表只有一条即只会遍历 一次，子表可以有多条
			for(int n=32; n<=62; n++){
				BfColumn col = row.getColumnByName("tab0_text"+n);//根据控件id获取控件对象
				String str = col.getColVal();//获取控件的值。
				if(str!=null && !"".equals(str)){
					time += Double.parseDouble(str);
				}
			}	
		} 
		return Double.toString(time);
	}
	
	public String getTime3(BaseBfForm bf){		
		double time = 0;

		//获取表单主表控件值
		BfTable tbl = bf.getRootTable();//获取表单主表对象
		ArrayList<BfRow> rows = tbl.getRows();//获取当前表单行记录对象（一 条表单记录主表记录只有一条，子表明细记录可以有多条）
		for(int i=0; i<rows.size(); i++){    
			BfRow row = rows.get(i);//遍历得到行记录对象主表只有一条即只会遍历 一次，子表可以有多条
			for(int n=63; n<=93; n++){
				BfColumn col = row.getColumnByName("tab0_text"+n);//根据控件id获取控件对象
				String str = col.getColVal();//获取控件的值。
				if(str!=null && !"".equals(str)){
					time += Double.parseDouble(str);
				}
			}	
		} 
		return Double.toString(time);
	}
	
	public String getTimeSaturday(BaseBfForm bf){		
		double time = 0;
		
		//获取表单主表控件值
		BfTable tbl = bf.getRootTable();//获取表单主表对象
		ArrayList<BfRow> rows = tbl.getRows();//获取当前表单行记录对象（一 条表单记录主表记录只有一条，子表明细记录可以有多条）
		BfRow row = rows.get(0);//遍历得到行记录对象主表只有一条即只会遍历 一次，子表可以有多条
		String year = row.getColumnByName("tab0_text96").getColVal();//年份
		String month = row.getColumnByName("tab0_text97").getColVal();//月份

		Database localDatabase = null;
		String val = "";
		try {
			localDatabase = Tools.getDatabase(true);
			Rowset localRowset = localDatabase.getRS("select * from B1BFG40048mst where tab0_text32 = '"+year+"' and tab0_text33 = '"+month+"'");
			if (!(localRowset.next())) {}
			
			for(int i=1; i<=31; i++){
				val = localRowset.getString("tab0_select"+i);
				if("1".equals(val)){
					String str = row.getColumnByName("tab0_text"+i).getColVal();
					if(str!=null && !"".equals(str)){
						time += Double.parseDouble(str);
					}
				}
			}
			return Double.toString(time);
		} catch (Exception localException) {
			Log.info(localException);
		} finally {
			if (localDatabase != null)
				localDatabase.cleanup();
		}
		return "计算出错，请联系管理员";
	}
	
	public String getTimeSunday(BaseBfForm bf){		
		double time = 0;
		
		//获取表单主表控件值
		BfTable tbl = bf.getRootTable();//获取表单主表对象
		ArrayList<BfRow> rows = tbl.getRows();//获取当前表单行记录对象（一 条表单记录主表记录只有一条，子表明细记录可以有多条）
		BfRow row = rows.get(0);//遍历得到行记录对象主表只有一条即只会遍历 一次，子表可以有多条
		String year = row.getColumnByName("tab0_text96").getColVal();//年份
		String month = row.getColumnByName("tab0_text97").getColVal();//月份

		Database localDatabase = null;
		String val = "";
		try {
			localDatabase = Tools.getDatabase(true);
			Rowset localRowset = localDatabase.getRS("select * from B1BFG40048mst where tab0_text32 = '"+year+"' and tab0_text33 = '"+month+"'");
			if (!(localRowset.next())) {}
			
			for(int i=1; i<=31; i++){
				val = localRowset.getString("tab0_select"+i);
				if("2".equals(val)){
					String str = row.getColumnByName("tab0_text"+i).getColVal();
					if(str!=null && !"".equals(str)){
						time += Double.parseDouble(str);
					}
				}
			}
			return Double.toString(time);
		} catch (Exception localException) {
			Log.info(localException);
		} finally {
			if (localDatabase != null)
				localDatabase.cleanup();
		}
		return "计算出错，请联系管理员";
	}
	
	public String getTimeHld(BaseBfForm bf){		
		double time = 0;
		
		//获取表单主表控件值
		BfTable tbl = bf.getRootTable();//获取表单主表对象
		ArrayList<BfRow> rows = tbl.getRows();//获取当前表单行记录对象（一 条表单记录主表记录只有一条，子表明细记录可以有多条）
		BfRow row = rows.get(0);//遍历得到行记录对象主表只有一条即只会遍历 一次，子表可以有多条
		String year = row.getColumnByName("tab0_text96").getColVal();//年份
		String month = row.getColumnByName("tab0_text97").getColVal();//月份

		Database localDatabase = null;
		String val = "";
		try {
			localDatabase = Tools.getDatabase(true);
			Rowset localRowset = localDatabase.getRS("select * from B1BFG40048mst where tab0_text32 = '"+year+"' and tab0_text33 = '"+month+"'");
			if (!(localRowset.next())) {}
			
			for(int i=1; i<=31; i++){
				val = localRowset.getString("tab0_select"+i);
				if("3".equals(val)){
					String str = row.getColumnByName("tab0_text"+i).getColVal();
					if(str!=null && !"".equals(str)){
						time += Double.parseDouble(str);
					}
				}
			}
			return Double.toString(time);
		} catch (Exception localException) {
			Log.info(localException);
		} finally {
			if (localDatabase != null)
				localDatabase.cleanup();
		}
		return "计算出错，请联系管理员";
	}
	
	public String createDOC(BaseBfForm bf){
		//获取表单主表控件值
		BfTable tbl = bf.getRootTable();//获取表单主表对象
		ArrayList<BfRow> rows = tbl.getRows();//获取当前表单行记录对象（一 条表单记录主表记录只有一条，子表明细记录可以有多条）
		BfRow row = rows.get(0);//遍历得到行记录对象主表只有一条即只会遍历 一次，子表可以有多条
		String oldYear = row.getColumnByName("tab0_text1").getColVal();//历史年份
		String oldMonth = row.getColumnByName("tab0_text2").getColVal();//历史月份
		String newYear = row.getColumnByName("tab0_text3").getColVal();//计划年份
		String newMonth = row.getColumnByName("tab0_text4").getColVal();//计划月份
		if(newYear.equals(oldYear) && newMonth.equals(oldMonth)){
			return "历史月份与计划月份重复";
		}else{
		    String TAB0_TEXT1="";
		    String TAB0_TEXT2="";
		    String TAB0_TEXT3="";
		    String TAB0_TEXT4="";
		    String TAB0_TEXT5="";
		    String TAB0_TEXT6="";
		    String TAB0_TEXT7="";
		    String TAB0_TEXT8="";
		    String TAB0_TEXT9="";
		    String insertSQL ="";
			
			Database localDatabase = null;
			try {
				localDatabase = Tools.getDatabase(true);
				Rowset localRowset = localDatabase.getRS("select TAB0_TEXT1,TAB0_TEXT2,TAB0_TEXT3,TAB0_TEXT4,TAB0_TEXT5,TAB0_TEXT6,TAB0_TEXT7,TAB0_TEXT8,TAB0_TEXT9 from B1BFG40022MST where tab0_text10 = '"+oldYear+"' and tab0_text11 = '"+oldMonth+"'");
				int i=0;
				while(localRowset.next()){
					i++;
					TAB0_TEXT1 = localRowset.getString("TAB0_TEXT1");
					TAB0_TEXT2 = localRowset.getString("TAB0_TEXT2");
					TAB0_TEXT3 = localRowset.getString("TAB0_TEXT3");
					TAB0_TEXT4 = localRowset.getString("TAB0_TEXT4");
					TAB0_TEXT5 = localRowset.getString("TAB0_TEXT5");
					TAB0_TEXT6 = localRowset.getString("TAB0_TEXT6");
					TAB0_TEXT7 = localRowset.getString("TAB0_TEXT7");
					TAB0_TEXT8 = localRowset.getString("TAB0_TEXT8");
					TAB0_TEXT9 = localRowset.getString("TAB0_TEXT9");
					insertSQL = "'"+TAB0_TEXT1+"','"+TAB0_TEXT2+"','"+TAB0_TEXT3+"','"+TAB0_TEXT4+"','"+TAB0_TEXT5+"','"+TAB0_TEXT6+"','"+TAB0_TEXT7+"','"+TAB0_TEXT8+"','"+TAB0_TEXT9+"','"+newYear+"','"+newMonth+"'";
					//System.out.println("-----------"+insertSQL);
					insertDOC(insertSQL);
				}
				if(i==0){
					return "请检查历史月份档案是否存在";
				}else{
					return "已生成";
				}
			} catch (Exception localException) {
				Log.info(localException);
			} finally {
				if (localDatabase != null)
					localDatabase.cleanup();
			}
			return "出错，请联系管理员";
		}
	}
	
	public void insertDOC(String insertSQL){
		CommSequence cs = CommSequence.getInstance("B1BFG40022MST_SEQ");
		String BFFRMIN_NO = cs.getNextValue();
	    insertSQL = "insert into B1BFG40022MST (BFFRMIN_NO,BFFRMED_NO,BFFRMIN_STA,ORG_NO,TAB0_TEXT1,TAB0_TEXT2,TAB0_TEXT3,TAB0_TEXT4,TAB0_TEXT5,TAB0_TEXT6,TAB0_TEXT7,TAB0_TEXT8,TAB0_TEXT9,TAB0_TEXT10,TAB0_TEXT11) values('"
		+BFFRMIN_NO+"','1081279059456950272','04','1075310369292943360',"+insertSQL+")";
	    //System.out.println("=========="+insertSQL);
		Database localDatabase = null;
		try {
			localDatabase = Tools.getDatabase(true);
			localDatabase.execSqlUpdate(insertSQL);
		} catch (Exception localException) {
			Log.info(localException);
		} finally {
			if (localDatabase != null)
				localDatabase.cleanup();
		}
	}
	
	public String createEndDtm(BaseBfForm bf){
		BfPageInfo bpi = bf.getPageInfo();// 表单基础信息对象
		String BFFRMIN_NO = bpi.getPkValue();// 当前表单主键值		
		//获取表单主表控件值
		BfTable tbl = bf.getRootTable();//获取表单主表对象
		ArrayList<BfRow> rows = tbl.getRows();//获取当前表单行记录对象（一 条表单记录主表记录只有一条，子表明细记录可以有多条）
		BfRow row = rows.get(0);//遍历得到行记录对象主表只有一条即只会遍历 一次，子表可以有多条
		String days = row.getColumnByName("tab0_text21").getColVal();//合计天数
		String endDtm = "";

		Database localDatabase = null;
		try {
			localDatabase = Tools.getDatabase(true);
			Rowset localRowset = localDatabase.getRS("select to_char(to_date(TAB0_TEXT11, 'YYYY-MM-DD')+"+days+"-1, 'YYYY-MM-DD') from B1BFG40073MST where BFFRMIN_NO ="+BFFRMIN_NO);
			while(localRowset.next()){
				endDtm = localRowset.getString(1);
			}
			if("".equals(endDtm)){
				return "出错";
			}else{
				return endDtm;
			}
		} catch (Exception localException) {
			Log.info(localException);
		} finally {
			if (localDatabase != null)
				localDatabase.cleanup();
		}
		return "计算出错，请联系管理员";
	}
	
	public String calcDays(BaseBfForm bf){
		BfPageInfo bpi = bf.getPageInfo();// 表单基础信息对象
		String BFFRMIN_NO = bpi.getPkValue();// 当前表单主键值		
		String days = "";//合计天数

		Database localDatabase = null;
		try {
			localDatabase = Tools.getDatabase(true);
			Rowset localRowset = localDatabase.getRS("select TO_DATE(TAB0_TEXT8, 'YYYY-MM-DD')-TO_DATE(TAB0_TEXT7, 'YYYY-MM-DD') from B1BFG40074MST where BFFRMIN_NO ="+BFFRMIN_NO);
			while(localRowset.next()){
				days = localRowset.getString(1);
			}
			if("".equals(days)){
				return "出错";
			}else{
				return days;
			}
		} catch (Exception localException) {
			Log.info(localException);
		} finally {
			if (localDatabase != null)
				localDatabase.cleanup();
		}
		return "计算出错，请联系管理员";
	}
}
	