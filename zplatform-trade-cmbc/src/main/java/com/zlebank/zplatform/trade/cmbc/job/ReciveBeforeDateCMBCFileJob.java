/* 
 * ReciveCMBCFileJob.java  
 * 
 * version TODO
 *
 * 2015年11月3日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.cmbc.job;

import java.io.File;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zlebank.zplatform.commons.utils.DateUtil;
import com.zlebank.zplatform.trade.cmbc.bean.enmus.DownLoadTypeEnmu;
import com.zlebank.zplatform.trade.cmbc.bean.enmus.FileTypeEnmu;
import com.zlebank.zplatform.trade.cmbc.security.CMBCAESUtils;
import com.zlebank.zplatform.trade.cmbc.service.IFTPCMBCService;
import com.zlebank.zplatform.trade.cmbc.service.IInsteadPayService;
import com.zlebank.zplatform.trade.dao.DownloadLogDAO;
import com.zlebank.zplatform.trade.model.PojoDownloadLog;
import com.zlebank.zplatform.trade.utils.ConsUtil;
/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年11月3日 下午5:49:29
 * @since 
 */
public class ReciveBeforeDateCMBCFileJob {
    private static final Log log = LogFactory.getLog(ReciveBeforeDateCMBCFileJob.class);
    private static final String TARGETPATH = ConsUtil.getInstance().cons.getCmbc_download_file_path();
    @Autowired
    private IInsteadPayService insteadPayService;
    @Autowired
    private IFTPCMBCService ftpcmbcService;
    @Autowired
    private DownloadLogDAO downloadLogDAO;
    
    /**
     * 定时扫描当前日期的Ftp目录
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void execute() {
        String  beforeDate = DateUtil.getBeforeDate(-1);//格式：yyyyMMdd
     // 扫描本行回盘文件
        String path = "res/" + beforeDate;
        List<String> fileNameList = ftpcmbcService.scanningCMBCRESFile(path,beforeDate,FileTypeEnmu.RES);
        for (String fileName : fileNameList) {
            PojoDownloadLog downloadLog = downloadLogDAO.getLogByFileName(fileName);
            if(downloadLog!=null){
                if("00".equals(downloadLog.getRecode())){
                    continue;
                }
                downloadLog.setDownloadcount(downloadLog.getDownloadcount()+1);
                downloadLogDAO.update(downloadLog);
            }else{
                downloadLog = new PojoDownloadLog();
                downloadLog.setFilename(fileName);
                downloadLog.setDownloadcount(1);
                downloadLog.setDownloadtime(DateUtil.getCurrentDateTime());
                downloadLog.setCaid("93000001");
                downloadLog.setCaname("民生银行");
                downloadLog.setDownloaderid(0L);
                downloadLog.setDownloadername("system");
                downloadLog.setFileurl(fileName);
                downloadLogDAO.saveA(downloadLog);
            }
            

            try {
                ftpcmbcService.downloadCMBCFile(downloadLog.getFileurl(),TARGETPATH);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                downloadLog.setRecode(DownLoadTypeEnmu.FTPFAILED.getCode());
                downloadLogDAO.update(downloadLog);
                continue;
            }
            File file = null;
            try {
                String plainFilePath = ConsUtil.getInstance().cons.getCmbc_plainFilePath();
                CMBCAESUtils.decodeAESFile(ConsUtil.getInstance().cons.getCmbc_insteadpay_batch_md5().getBytes(), plainFilePath, TARGETPATH);
                file = new File(plainFilePath);
                String[] files =fileName.split("/");
                insteadPayService.analyzeCMBCFile(file,files[files.length-1],FileTypeEnmu.RES);
                downloadLog.setRecode(DownLoadTypeEnmu.SUCCESS.getCode());
            } catch (Exception e) {
                e.printStackTrace();
                // TODO Auto-generated catch block
                downloadLog.setRecode(DownLoadTypeEnmu.FAILED.getCode());
            } finally {
                downloadLogDAO.update(downloadLog);
                file.delete();
            }
            
        }
        // 扫描跨行回盘文件
        path = "resouter/" + beforeDate;
        fileNameList = ftpcmbcService.scanningCMBCRESFile(path,beforeDate,FileTypeEnmu.RESOUTER);
        for (String fileName : fileNameList) {
            PojoDownloadLog downloadLog = downloadLogDAO.getLogByFileName(fileName);
            
            if(downloadLog!=null){
                if("00".equals(downloadLog.getRecode())){
                    continue;
                }
                downloadLog.setDownloadcount(downloadLog.getDownloadcount()+1);
                downloadLogDAO.update(downloadLog);
            }else{
                downloadLog = new PojoDownloadLog();
                downloadLog.setFilename(fileName);
                downloadLog.setDownloadcount(1);
                downloadLog.setDownloadtime(DateUtil.getCurrentDateTime());
                downloadLog.setCaid("93000001");
                downloadLog.setCaname("民生银行");
                downloadLog.setDownloaderid(0L);
                downloadLog.setDownloadername("system");
                downloadLog.setFileurl(fileName);
                downloadLogDAO.saveA(downloadLog);
            }

            try {
                ftpcmbcService.downloadCMBCFile(downloadLog.getFileurl(),
                        TARGETPATH );
            } catch (Exception e) {
                // TODO: handle exception
                downloadLog.setRecode(DownLoadTypeEnmu.FTPFAILED.getCode());
                downloadLogDAO.update(downloadLog);
                continue;
            }
            File file = null;
            try {
                String plainFilePath = ConsUtil.getInstance().cons.getCmbc_plainFilePath();
                CMBCAESUtils.decodeAESFile(ConsUtil.getInstance().cons.getCmbc_insteadpay_batch_md5().getBytes(), plainFilePath, TARGETPATH);
                file = new File(plainFilePath);
                downloadLog.setRecode(DownLoadTypeEnmu.SUCCESS.getCode());
                String[] files =fileName.split("/");
                insteadPayService.analyzeCMBCFile(file,files[files.length-1],FileTypeEnmu.RESOUTER);
            } catch (Exception e) {
                e.printStackTrace();
                downloadLog.setRecode(DownLoadTypeEnmu.FAILED.getCode());
            } finally {
                downloadLogDAO.update(downloadLog);
                file.delete();
            }
            //break;
        }
        // 扫描退款回盘文件
        path = "reexchange/";
        fileNameList = ftpcmbcService.scanningCMBCRESFile(path,beforeDate,FileTypeEnmu.REEXCHANGE);
        for (String fileName : fileNameList) {
            PojoDownloadLog downloadLog = downloadLogDAO.getLogByFileName(fileName);
            if(downloadLog!=null){
                if("00".equals(downloadLog.getRecode())){
                    continue;
                }
                downloadLog.setDownloadcount(downloadLog.getDownloadcount()+1);
                downloadLogDAO.update(downloadLog);
            }else{
                downloadLog = new PojoDownloadLog();
                downloadLog.setFilename(fileName);
                downloadLog.setDownloadcount(1);
                downloadLog.setDownloadtime(DateUtil.getCurrentDateTime());
                downloadLog.setCaid("93000001");
                downloadLog.setCaname("民生银行");
                downloadLog.setDownloaderid(0L);
                downloadLog.setDownloadername("system");
                downloadLog.setFileurl(fileName);
                downloadLogDAO.saveA(downloadLog);
            }

            try {
                ftpcmbcService.downloadCMBCFile(downloadLog.getFileurl(),TARGETPATH);
            } catch (Exception e) {
                // TODO: handle exception
                downloadLog.setRecode(DownLoadTypeEnmu.FTPFAILED.getCode());
                downloadLogDAO.update(downloadLog);
                continue;
            }
            File file = null;
            try {
                //String plainFilePath = ConsUtil.getInstance().cons.getCmbc_plainFilePath();
                //CMBCAESUtils.decodeAESFile(ConsUtil.getInstance().cons.getCmbc_insteadpay_batch_md5().getBytes(), plainFilePath, TARGETPATH);
                file = new File(TARGETPATH);
                downloadLog.setRecode(DownLoadTypeEnmu.SUCCESS.getCode());
                String[] files =fileName.split("/");
                insteadPayService.analyzeCMBCFile(file,files[files.length-1],FileTypeEnmu.REEXCHANGE);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                downloadLog.setRecode(DownLoadTypeEnmu.FAILED.getCode());
            } finally {
                downloadLogDAO.update(downloadLog);
                file.delete();
            }
        }
        log.info("end RecivebeforeDateCMBCFileJob");
    }
}
