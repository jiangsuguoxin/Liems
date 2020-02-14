package net.luculent.liems.business.em.emwo;

import net.luculent.core.database.Database;
import net.luculent.liems.action.service.ServiceRequest;
import net.luculent.liems.component.sequence.CommCodeNumber;
import net.luculent.liems.jsdom.DataDom;
import net.luculent.liems.jsdom.element.Column;
import net.luculent.liems.jsdom.element.Row;
import net.luculent.liems.jsdom.element.Table;
import net.luculent.liems.util.SessionInfo;

public class B1WOM51030_EXT extends B1WOM51030 {
	public void saveForLiems5(DataDom paramDataDom,
			SessionInfo paramSessionInfo, ServiceRequest paramServiceRequest,
			Database paramDatabase) throws Exception {
		Row localRow = paramDataDom.getRootTable().getRow(0L);
		localRow.setFirstLastUserInfo(paramSessionInfo.getUserName());
		if ("new".equals(localRow.getDataStatus())) {
			CommCodeNumber localCommCodeNumber = new CommCodeNumber("FI",
					paramSessionInfo.getCurrentLanguage(),
					paramSessionInfo.getUserId(),
					paramSessionInfo.getOrg()[0].getValue(), paramDatabase);
			String str = localCommCodeNumber.getNextValue();
			//localRow.addColumn("FIR_ID", str);
			localRow.addColumn("FIR_ID", "");
		}

		paramDataDom.getRootTable().save(paramDatabase);
		setFirTyp(paramDataDom, paramSessionInfo, paramServiceRequest);
	}

	private void setFirTyp(DataDom paramDataDom, SessionInfo paramSessionInfo,
			ServiceRequest paramServiceRequest) {
		String str1 = paramServiceRequest.getParameter("TKP_ID");
		Table localTable = paramDataDom.getRootTable();
		Row localRow = localTable.getRow(0L);
		if (localRow == null) {
			localRow = new Row(false, 0, 0, "new");
			localTable.addRow(localRow);
		}
		String str2 = getColVal(localRow, "ORG_NO");

		if (("".equals(str2)) || (str2 == null))
			str2 = "";

		String str3 = TtkPub.getTkpIdLstByTkpId(str1, str2);
		localRow.addColumn(new Column("FIR_TYP", str3));
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
