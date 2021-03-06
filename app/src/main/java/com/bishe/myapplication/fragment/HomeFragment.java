package com.bishe.myapplication.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bishe.myapplication.R;
import com.bishe.myapplication.dayimarili.DateCardModel;
import com.bishe.myapplication.dayimarili.DateChange;
import com.bishe.myapplication.dayimarili.DateView2;
import com.bishe.myapplication.dayimarili.DateView3;
import com.bishe.myapplication.dayimarili.db.MenstruationCycle;
import com.bishe.myapplication.dayimarili.db.MenstruationModel;
import com.bishe.myapplication.dayimarili.db.MenstruationDao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    private TextView tvDate;
    private TextView tvDate2;
    private DateView2 dateView;
    private DateView3 dateView3;
    private TextView llMtCome, llMtBack;
    private MenstruationDao mtDao;
    private MenstruationCycle mCycle;
    private Date curDate; // 当前日历显示的月
    private Calendar calendar;
    private MenstruationModel mtmBass;//预测大姨妈的基础数据
    private long nowTime = 0;//点击的日期
    private DateCardModel dcm;//点击的月份
    private List<MenstruationModel> list;
    private Context mContext;
    private List<MenstruationModel> mtmList3;
    /**
     * 上次经期:3月10号 周日
     */
    private TextView mTvUpDay;
    /**
     * 预测经期:5月10号 周日
     */
    private TextView mTvDownDay;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = getActivity();
        initView(v);
        initData();
        setListener(v);
        return v;
    }

    private void initView(View v) {
        tvDate = (TextView) v.findViewById(R.id.tv_date);
        tvDate2 = (TextView) v.findViewById(R.id.tv_date2);
        dateView = (DateView2) v.findViewById(R.id.date_view);
        dateView3 = (DateView3) v.findViewById(R.id.date_view2);
        dateView.setOnItemClickListener(new DateView2.OnItemListener() {

            @Override
            public void onClick(long time, DateCardModel d) {
                nowTime = time;
                dcm = d;
                //判断经期开始日期是否大于现在时间
                if (time > DateChange.getDate()) {
                    llMtCome.setVisibility(View.GONE);
                    llMtBack.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "无法记录未来哦", Toast.LENGTH_SHORT).show();
                    return;
                } else if (dcm.type == 1) {
                    llMtCome.setVisibility(View.GONE);
                    llMtBack.setVisibility(View.VISIBLE);
                } else if (mtDao.getEndTimeNumber(nowTime) < 6) {
                    llMtCome.setVisibility(View.GONE);
                    llMtBack.setVisibility(View.VISIBLE);
                } else if (dcm.type != 1) {
                    llMtCome.setVisibility(View.VISIBLE);
                    llMtBack.setVisibility(View.GONE);
                }
            }
        });
        dateView3.setOnItemClickListener(new DateView3.OnItemListener() {

            @Override
            public void onClick(long time, DateCardModel d) {
                nowTime = time;
                dcm = d;
                if (time > DateChange.getDate()) {
                    llMtCome.setVisibility(View.GONE);
                    llMtBack.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "无法记录未来哦", Toast.LENGTH_SHORT).show();
                    return;
                } else if (dcm.type == 1) {
                    llMtCome.setVisibility(View.GONE);
                    llMtBack.setVisibility(View.VISIBLE);

                } else if (mtDao.getEndTimeNumber(nowTime) < 6) {
                    llMtCome.setVisibility(View.GONE);
                    llMtBack.setVisibility(View.VISIBLE);

                } else if (dcm.type != 1) {
                    llMtCome.setVisibility(View.VISIBLE);
                    llMtBack.setVisibility(View.GONE);

                }
            }
        });

        llMtCome = (TextView) v.findViewById(R.id.ll_mt_come);
        llMtBack = (TextView) v.findViewById(R.id.ll_mt_back);
        mTvUpDay = (TextView) v.findViewById(R.id.tv_up_day);
        mTvDownDay = (TextView) v.findViewById(R.id.tv_down_day);
    }

    /**
     * 初始化大姨妈数据
     */
    private void initData() {
        //获取日历对象
        calendar = Calendar.getInstance();
        curDate = new Date();
        //设置当前日期
        calendar.setTime(curDate);
        //获取数据库
        mtDao = new MenstruationDao(mContext);
        //获取周期与平均天数
        mCycle = mtDao.getMTCycle();
        long nowDate = DateChange.dateTimeStamp(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-1", "yyyy-MM-dd");
        long nextDate = DateChange.dateTimeStamp(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 2) + "-1", "yyyy-MM-dd");
        //获取当月数据 为空
        List<MenstruationModel> mtmList = mtDao.getMTModelList(nowDate, nextDate);
        //获取全部数据
        list = mtDao.getMTModelList(0, 0);
        mtmList.get(mtmList.size() - 1).setCon(true);
            mtmBass = mtmList.get(mtmList.size() - 1);

        long time = mtmBass.getBeginTime() - 86400000L * (mtDao.getMTCycle().getCycle()-1);
        //下一次的月经是否在当月
        MenstruationModel mtm = new MenstruationModel();
        mtm.setBeginTime(mtmBass.getBeginTime() + 86400000L * (mCycle.getCycle() - 1));
        mtm.setEndTime(mtm.getBeginTime() +  86400000L * (mCycle.getNumber() - 1));
        mtm.setDate(nowDate);
        mtm.setCon(false);
        if (nextDate > mtm.getBeginTime()) {
            if (mtm.getBeginTime() > DateChange.getDate()) {
                mtmList.add(mtm);
            } else {
                mtm.setBeginTime(DateChange.getDate());
                mtm.setEndTime(DateChange.getDate() + 86400000L *( mCycle.getNumber() - 1));
                mtmList.add(mtm);
            }
            mtmBass = mtm;
        }
        dateView.initData(mtmList);
        tvDate.setText(dateView.getYearAndmonth());
        //计算上一月月经开始时间

        Date date = new Date(time);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date);
        int year = calendar2.get(Calendar.YEAR);
        int month = calendar2.get(Calendar.MONTH) + 1;
        int day = calendar2.get(Calendar.DATE);//获取日
        int week2 = calendar2.get(Calendar.DAY_OF_WEEK);
        String weeks2;
        switch (week2) {
            case 1:
                weeks2 = "周日";
                break;
            case 2:
                weeks2 = "周一";
                break;
            case 3:
                weeks2 = "周二";
                break;
            case 4:
                weeks2 = "周三";
                break;
            case 5:
                weeks2 = "周四";
                break;
            case 6:
                weeks2 = "周五";
                break;
            case 7:
                weeks2 = "周六";
                break;
            default:
                weeks2 = "";
                break;
        }
        mTvUpDay.setText("上次经期:"+ + month + "月"  + day+ "日" + "    " + weeks2);

        //初始化下一月日历表格
        Calendar calendar = Calendar.getInstance();
        Date curDate = new Date();
        calendar.setTime(curDate);
        calendar.add(Calendar.MONTH, 1);
        curDate = calendar.getTime();
        long nowDate3 = DateChange.dateTimeStamp(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-1", "yyyy-MM-dd");
        long nextDate3 = DateChange.dateTimeStamp(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 2) + "-1", "yyyy-MM-dd");
        mtmList3 = calculateMt(nowDate3, nextDate3);
        dateView3.initData(mtmList);
        tvDate2.setText(dateView3.clickRightMonth(mtmList3));
        tvDate2.setText(dateView3.getYearAndmonth());
        Date date3 = new Date(mtmBass.getBeginTime() +86400000L * mtDao.getMTCycle().getCycle());
        Calendar calendar3 = Calendar.getInstance();
        calendar3.setTime(date3);
        int month3 = calendar3.get(Calendar.MONTH) + 1;
        int day3 = calendar3.get(Calendar.DATE);//获取日
        int week3 = calendar3.get(Calendar.DAY_OF_WEEK);
        String weeks = "";
        switch (week3) {
            case 1:
                weeks = "周日";
                break;
            case 2:
                weeks = "周一";
                break;
            case 3:
                weeks = "周二";
                break;
            case 4:
                weeks = "周三";
                break;
            case 5:
                weeks = "周四";
                break;
            case 6:
                weeks = "周五";
                break;
            case 7:
                weeks = "周六";
                break;
            default:
                weeks = "";
                break;
        }
        mTvDownDay.setText("预测经期:"+ + month3 + "月"  + day3+ "日" + "    " + weeks);
    }

    /**
     * 获取并预测大姨妈
     *
     * @param nowDate  当月时间
     * @param nextDate 下月时间
     * @return
     */
    private List<MenstruationModel> calculateMt(long nowDate, long nextDate) {
        //获取本月大姨妈数据
        List<MenstruationModel> mtmList = mtDao.getMTModelList(nowDate, nextDate);//将数据库中的当月大姨妈数据取出来
        for (int i = 0; i < mtmList.size(); i++) {
            mtmList.get(i).setCon(true);
        }
        //现在时间小于基础时间，不用计算其他的
        if (nowDate < mtmBass.getDate()) {
            return mtmList;
        }
        //如果当月没有大姨妈数据，就根据上一个月的数据预测这个月的姨妈周期
        if (nowDate == mtmBass.getDate()) {
            //现在时间跟基础时间相同
            if (!mtmBass.isCon()) {
                mtmList.add(mtmBass);
            }
        } else {
            //不同就根据基础时间预测
            MenstruationModel mtm1 = new MenstruationModel();
            mtm1.setBeginTime(mtmBass.getBeginTime() + intervalTime(mtmBass.getDate(), nowDate));
            mtm1.setEndTime(mtmBass.getBeginTime() + intervalTime(mtmBass.getDate(), nowDate) + 86400000L * (mCycle.getNumber() - 1));
            mtm1.setCon(false);
            mtmList.add(mtm1);
            //判断下一次的大姨妈是否在本月
            MenstruationModel mtm = new MenstruationModel();
            mtm.setBeginTime(mtmBass.getBeginTime() + intervalTime(mtmBass.getDate(), nowDate) + 86400000L * (mCycle.getCycle() - 1));
            mtm.setEndTime(mtm.getBeginTime() + intervalTime(mtm.getDate(), nowDate)  + 86400000L * (mCycle.getNumber() - 1));
            mtm.setCon(false);
            if (nextDate > mtm.getBeginTime()) {
                if (mtm.getBeginTime() > DateChange.getDate()) {
                    mtmList.add(mtm);
                } else {
                    mtm.setBeginTime(DateChange.getDate());
                    mtm.setEndTime(DateChange.getDate() + 86400000L * (mCycle.getNumber() - 1));
                    mtmList.add(mtm);
                }
            }
        }
        //判断上一次的大姨妈是否有在本月
        MenstruationModel mtm1 = new MenstruationModel();
        mtm1.setBeginTime(mtmBass.getBeginTime() + intervalTime(mtmBass.getDate(), nowDate) - 86400000L * 28);
        mtm1.setEndTime(mtmBass.getBeginTime() + intervalTime(mtmBass.getDate(), nowDate) - 86400000L * 28 + 86400000L * (mCycle.getNumber() - 1));
        mtm1.setCon(false);
        if (nowDate <= mtm1.getEndTime()) {
            mtmList.add(mtm1);
        }
        return mtmList;
    }


    private void setListener(View v) {
        /**
         * 回到当月
         */
        v.findViewById(R.id.back_taday).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                calendar.setTime(curDate);
                calendar.add(Calendar.MONTH, getNowTime("yyyy") * 12 + getNowTime("MM") - (calendar.get(Calendar.MONTH) + 1) - calendar.get(Calendar.YEAR) * 12);
                curDate = calendar.getTime();
                long nowDate = DateChange.dateTimeStamp(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-1", "yyyy-MM-dd");
                long nextDate = DateChange.dateTimeStamp(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 2) + "-1", "yyyy-MM-dd");
                List<MenstruationModel> mtmList = mtDao.getMTModelList(nowDate, nextDate);
                for (int i = 0; i < mtmList.size(); i++) {
                    mtmList.get(i).setCon(true);
                }
                if (mtmList.size() == 0) {
                    MenstruationModel mtm = new MenstruationModel();
                    mtm.setBeginTime(list.get(list.size() - 1).getBeginTime() + intervalTime(list.get(list.size() - 1).getBeginTime(), nowDate));
                    mtm.setEndTime(list.get(list.size() - 1).getBeginTime() + intervalTime(list.get(list.size() - 1).getBeginTime(), nowDate) + 86400000L * (mCycle.getNumber() - 1));
                    mtm.setCon(false);
                    if (mtm.getBeginTime() > DateChange.getDate()) {
                        mtmList.add(mtm);
                    } else {
                        mtm.setBeginTime(DateChange.getDate());
                        mtm.setEndTime(DateChange.getDate() + 86400000L * 4);
                        mtm.setCon(false);
                        mtmList.add(mtm);
                    }
                }
                mtmList.add(mtmBass);
                tvDate2.setText(dateView3.recurToday(mtmList));
            }
        });

        /**
         * 姨妈来了
         */
        llMtCome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                long startTime = mtDao.getStartTimeNumber(nowTime);
                if ((startTime - nowTime) / 86400000 < 9 && (startTime - nowTime) / 86400000 > 0) {
                    mtDao.updateMTStartTime(nowTime, startTime);
                } else {
                    MenstruationModel mtm = new MenstruationModel();
                    mtm.setDate(DateChange.dateTimeStamp(DateChange.timeStamp2Date(nowTime + "", "yyyy-MM") + "-1", "yyyy-MM-dd"));
                    mtm.setBeginTime(nowTime);
                    mtm.setEndTime(nowTime + 86400000L * (mCycle.getNumber() - 1));
                    mtm.setCycle(mCycle.getCycle());
                    mtm.setDurationDay(mCycle.getNumber());
                    mtDao.setMTModel(mtm);
                }
                refreshUI();

            }
        });

        /**
         * 姨妈走了
         */
        llMtBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mtDao.updateMTEndTime(nowTime);
                refreshUI();
            }
        });

    }

    /**
     * 刷新UI
     */
    private void refreshUI() {
        long nowDate = DateChange.dateTimeStamp(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-1", "yyyy-MM-dd");
        long nextDate = DateChange.dateTimeStamp(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 2) + "-1", "yyyy-MM-dd");
        //获取当月数据
        List<MenstruationModel> mtmList = mtDao.getMTModelList(nowDate, nextDate);
        //获取全部数据
        list = mtDao.getMTModelList(0, 0);
        for (int i = 0; i < mtmList.size(); i++) {
            mtmList.get(i).setCon(true);
        }
        //下一次的月经是否在当月
        MenstruationModel mtm = new MenstruationModel();
        mtm.setBeginTime(mtmBass.getBeginTime() + 86400000L * 28);
        mtm.setEndTime(mtmBass.getBeginTime() + 86400000L * 28 + 86400000L * (mCycle.getNumber() - 1));
        mtm.setDate(nowDate);
        mtm.setCon(false);
        if (nextDate > mtm.getBeginTime()) {
            if (mtm.getBeginTime() > DateChange.getDate()) {
                mtmList.add(mtm);
            } else {
                mtm.setBeginTime(DateChange.getDate());
                mtm.setEndTime(DateChange.getDate() + 86400000L * 4);
                mtmList.add(mtm);
            }
        }
        dateView.refreshUI(mtmList);
        dateView3.recurToday(mtmList3);

    }

    /**
     * 计算间隔时间
     *
     * @param startTime
     * @param endTime
     * @return
     */
    private long intervalTime(long startTime, long endTime) {
        int i = (int) ((endTime - startTime) / 86400000 / mCycle.getCycle());
        i = (endTime - startTime) / 86400000 % mCycle.getCycle() == 0 ? i - 1 : i;
        return i * 86400000L * mCycle.getCycle();
    }

    /**
     * 获取当天日期
     *
     * @param format
     * @return
     */
    public int getNowTime(String format) {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);// 可以方便地修改日期格式
        String hehe = dateFormat.format(now);
        return Integer.parseInt(hehe);
    }
}

