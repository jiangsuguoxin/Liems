package net.luculent.liems.business.em.empm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import net.luculent.core.base.Charset;
import net.luculent.core.base.FormatDate;
import net.luculent.core.base.Log;
import net.luculent.core.database.Database;
import net.luculent.core.database.Rowset;
import net.luculent.core.database.muitidb.DBFuncTool;
import net.luculent.liems.business.em.emeq.EqTools;
import net.luculent.liems.component.job.JobExecuteResult;
import net.luculent.liems.component.job.quartz.QuartzJob;
import net.luculent.liems.component.sequence.CommSequence;
import net.luculent.liems.util.Tools;

public class PmexeJob extends QuartzJob
  implements Serializable
{
  private static final long serialVersionUID = -4790012742729134330L;

  public JobExecuteResult execute()
    throws Exception
  {
    JobExecuteResult localJobExecuteResult = createPMTasks();
    return localJobExecuteResult;
  }

  public JobExecuteResult createPMTasks()
    throws Exception
  {
    int i = 0;
    int j = 0;
    Database localDatabase = null;
    JobExecuteResult localJobExecuteResult = new JobExecuteResult(false);
    Log.info("=====服务启动，生成预维护任务=====");
    List localList = getToBeCreatedPPA();
    if (localList != null) {
      j += localList.size();
      try {
        localDatabase = Tools.getDatabase(false);
        for (int k = 0; k < localList.size(); ++k) {
          String str = (String)localList.get(k);

          int l = createPMTask(str, localDatabase);
          Log.info("=====任务定义：" + str + "，生成任务=" + l + "条!\r\n");
          i += l;
        }
        localDatabase.commit();
        Log.info("=====预维护任务生成完成=====共生成=" + i + "条!");
        localJobExecuteResult.setSuccess(true);
        localJobExecuteResult.setLog("=====预维护任务生成完成=====共生成=" + i + "条!");
      } catch (Exception localException) {
        localDatabase.rollback();
        Log.error(localException);
        throw localException;
      } finally {
        if (localDatabase != null)
          localDatabase.cleanup();
      }
    }

    return localJobExecuteResult;
  }

  public List getToBeCreatedPPA()
  {
    ArrayList localArrayList = new ArrayList();
    Database localDatabase = null;
    try {
      String str1 = FormatDate.getTodayStr();
      String str2 = FormatDate.getTomorrow();

      String str3 = "select PPA_NO, CYC_TYP, SPA_TYP, SPA_NUM, PLA_DAT as PLA_DAT, PREWJSE_DAT as PREWJSE_DAT, PREWJSS_DAT as PREWJSS_DAT, (select max(PMEXE_NO) from EMPMEXEMST where EMPMPLAMST.PPA_NO = EMPMEXEMST.PPA_NO) PREPMEXENO, (select CYC_TYP from EMPMEXEMST where PMEXE_NO = (select max(PMEXE_NO) from EMPMEXEMST where EMPMPLAMST.PPA_NO = EMPMEXEMST.PPA_NO)) PRECYC, (select WJS_NO from EMPMEXEMST where PMEXE_NO = (select max(PMEXE_NO) from EMPMEXEMST where EMPMPLAMST.PPA_NO = EMPMEXEMST.PPA_NO)) PREWJSNO from EMPMPLAMST where PM_TYP = '0' and VALID_STA = '01' and USE_STA = '01' and PLA_DAT is not null and SPA_NUM > 0 and SPA_TYP is not null";

      StringBuffer localStringBuffer = new StringBuffer(str3);
      localStringBuffer.append(" and CYC_TYP in ('01', '03', '04') ");

      localStringBuffer.append("and PPA_NO not in (select PPA_NO from EMPMEXEMST where (PLA_DAT >= liems_to_date('" + str1 + 
        "', 'yyyy-mm-dd') and PLA_DAT < liems_to_date('" + str2 + 
        "', 'yyyy-mm-dd')) or (OPR_DAT >= liems_to_date('" + str1 + 
        "', 'yyyy-mm-dd') and OPR_DAT < liems_to_date('" + str2 + "', 'yyyy-mm-dd') ))");
      localStringBuffer.append(" union ");

      localStringBuffer.append(str3);
      localStringBuffer.append(" and CYC_TYP = '05' and PPA_NO not in (select PPA_NO from EMPMEXEMST)");
      localDatabase = Tools.getDatabase(true);
      Rowset localRowset = localDatabase.getRS(localStringBuffer.toString());
      while (localRowset.next()) {
        String str4 = Charset.nullToEmpty(localRowset.getString("ppa_no"));
        String str5 = Charset.nullToEmpty(localRowset.getString("cyc_typ"));
        String str6 = Charset.nullToEmpty(localRowset.getString("spa_typ"));
        Date localDate1 = FormatDate.fromY_M_D_H_M_S(localRowset.getString("pla_dat"));

        String str7 = EqTools.nullToEmpty(localRowset.getString("prePmExeNo"));
        String str8 = EqTools.nullToEmpty(localRowset.getString("preCyc"));
		
        String str9 = EqTools.nullToEmpty(localRowset.getString("preWjsNo"));

        Date localDate2 = localDate1;
        if ((!(str8.equals(""))) && (str8.equals(str5)))
        {
          Date localObject1 = FormatDate.fromY_M_D_H_M_S(localRowset.getString("PREWJSS_DAT"));

          Date localDate3 = FormatDate.fromY_M_D_H_M_S(localRowset.getString("PREWJSE_DAT"));

          if (str5.equals("01")) {
            localDate2 = EqTools.getMaxDate(localDate1, (Date)localObject1);
          }
          else if ((str5.equals("03")) || (str5.equals("04")))
            localDate2 = EqTools.getMaxDate(localDate1, localDate3);
        }

        Object localObject1 = FormatDate.toY_M_D(localDate2);

        if (((String)localObject1).equals(str1)) {
          boolean bool1 = checkWjsInfo(str5, str7, str9, localDatabase);
          if (bool1)
            localArrayList.add(str4);
        }
        else {
          String str10;
          int 
		  = localRowset.getInt("spa_num");

          int j = 5;

          if (str6.equals("Z")) {
            j = 3;
          }
          else if (str6.equals("M")) {
            j = 2;
          }
          else if (str6.equals("Y"))
            j = 1;

          Calendar localCalendar1 = Calendar.getInstance();
          localCalendar1.setTime(localDate2);

          Calendar localCalendar2 = Calendar.getInstance();
          Date localDate4 = EqTools.convertDate(str1);
          localCalendar2.setTime(localDate4);

          if (str5.equals("05"))
            while ((localCalendar1.before(localCalendar2)) || (localCalendar1.equals(localCalendar2))) {
              localCalendar1.add(j, i);
              str10 = FormatDate.toY_M_D(localCalendar1.getTime());

              if (str10.equals(str1)) {
                localArrayList.add(str4);

              }
            }

          else {
            do
            {
              str10 = FormatDate.toY_M_D(localCalendar1.getTime());

              if (str10.equals(str1)) {
                boolean bool2 = checkWjsInfo(str5, str7, str9, localDatabase);

                if (bool2) {
                  localArrayList.add(str4);
                  break;
                }
              }
              localCalendar1.add(j, i);
            }
            while ((localCalendar1.before(localCalendar2)) || (localCalendar1.equals(localCalendar2)));
          }

        }

      }

    }
    catch (Exception localException)
    {
      Log.error(localException);
      localArrayList = null;

      if (localDatabase == null) 
      localDatabase.cleanup();
    }
    finally
    {
      if (localDatabase != null)
        localDatabase.cleanup();
    }

    return ((List)localArrayList);
  }

  private int createPMTask(String paramString, Database paramDatabase)
    throws Exception
  {
    int i = -1;
    CommSequence localCommSequence = CommSequence.getInstance("EMPMEXEMST_SEQ");
    String str1 = localCommSequence.getNextValue();
    if ((str1 != null) && (!(str1.equals("")))) {
      String str2 = "select PLA_NO,CRW_NO,LOC_NO,CYC_TYP FROM empmplamst where ppa_no = " + paramString;
      Rowset localRowset = Database.getRowset(str2);
      if (localRowset.next()) {
        String str3 = localRowset.getString("PLA_NO");
        String str4 = localRowset.getString("CRW_NO");
        String str5 = localRowset.getString("LOC_NO");
        String str6 = localRowset.getString("CYC_TYP");
        str2 = "INSERT INTO empmexemst(pmexe_no, ppa_no, pmexe_sta, pla_dat, pla_no,  crw_no, cyc_typ ,loc_no) VALUES(" + 
          str1 + "," + paramString + ", '01', " + DBFuncTool.sysdate() + "," + str3 + "," + str4 + ",'" + 
          str6 + "'," + str5 + ")";
        i = paramDatabase.execSqlUpdate(str2);
      }
      return i;
    }
    return -1;
  }

  private boolean checkWjsInfo(String paramString1, String paramString2, String paramString3, Database paramDatabase) throws Exception {
    boolean bool = true;

    if (paramString1.equals("01")) {
      bool = checkWjsCreated(paramString2, paramString3);
    }
    else if (paramString1.equals("03"))
      bool = checkWjsClosed(paramString2, paramString3, paramDatabase);

    return bool;
  }

  private boolean checkWjsCreated(String paramString1, String paramString2)
  {
    int i = 1;
    if (!(paramString1.equals("")))
    {
      i = (paramString2.equals("")) ? 0 : 1;
    }
    return i;
  }

  private boolean checkWjsClosed(String paramString1, String paramString2, Database paramDatabase) throws Exception
  {
    int i = 1;

    if (!(paramString1.equals("")))
      if (paramString2.equals("")) {
        i = 0;
      }
      else {
        String str1 = "select SCH_STA  from WOWJSMST where WJS_NO = " + paramString2;
        Rowset localRowset = paramDatabase.getRS(str1);

        if (localRowset.next()) {
          String str2 = EqTools.nullToEmpty(localRowset.getString("SCH_STA"));

          i = ((!(str2.equals("06"))) && (!(str2.equals("07")))) ? 0 : 1;
        }
      }

    return i;
  }
}