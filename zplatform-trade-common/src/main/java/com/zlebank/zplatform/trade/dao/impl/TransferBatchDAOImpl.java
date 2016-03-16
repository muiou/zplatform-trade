package com.zlebank.zplatform.trade.dao.impl;

import java.awt.color.CMMException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zlebank.zplatform.commons.dao.impl.HibernateBaseDAOImpl;
import com.zlebank.zplatform.commons.utils.StringUtil;
import com.zlebank.zplatform.trade.bean.page.QueryTransferBean;
import com.zlebank.zplatform.trade.dao.TransferBatchDAO;
import com.zlebank.zplatform.trade.dao.TransferDataDAO;
import com.zlebank.zplatform.trade.model.PojoTranBatch;
import com.zlebank.zplatform.trade.model.PojoTranData;

@Repository("transferBatchDAO")
public class TransferBatchDAOImpl extends HibernateBaseDAOImpl<PojoTranBatch>
        implements
            TransferBatchDAO {
    private static final Log log = LogFactory
            .getLog(TransferBatchDAOImpl.class);

    @Autowired
    private TransferDataDAO transferDataDAO;

    @Transactional(readOnly = true)
    public Map<String, Object> queryTransferBatchByPage(QueryTransferBean queryTransferBean,
            int page,
            int pageSize) {
        StringBuffer sqlBuffer = new StringBuffer(
                "from PojoTranBatch where 1=1 ");
        StringBuffer sqlCountBuffer = new StringBuffer(
                "select count(1) as total from PojoTranBatch  where 1=1 ");
        List<String> parameterList = new ArrayList<String>();
        if (queryTransferBean != null) {
            if (StringUtil.isNotEmpty(queryTransferBean.getBatchNo())) {
                sqlBuffer.append(" and tid = ? ");
                sqlCountBuffer.append(" and tid = ? ");
                parameterList.add(queryTransferBean.getBatchNo());
            }
            if (StringUtil.isNotEmpty(queryTransferBean.getBeginDate())) {
                sqlBuffer.append(" and applyTime >= ? ");
                sqlCountBuffer.append(" and applyTime >= ? ");
                parameterList.add(queryTransferBean.getBeginDate());
            }
            if (StringUtil.isNotEmpty(queryTransferBean.getEndDate())) {
                sqlBuffer.append(" and applyTime <= ? ");
                sqlCountBuffer.append(" and applyTime <= ? ");
                parameterList.add(queryTransferBean.getEndDate());
            }
            if (StringUtil.isNotEmpty(queryTransferBean.getEndDate())) {
                sqlBuffer.append(" and status = ? ");
                sqlCountBuffer.append(" and status = ? ");
                parameterList.add(queryTransferBean.getStatus());
            }
        }
        Query query = getSession().createQuery(sqlBuffer.toString());
        // Query countQuery =
        // getSession().createQuery(sqlCountBuffer.toString());
        int i = 0;
        for (String parameter : parameterList) {
            query.setParameter(i, parameter);
            // countQuery.setParameter(i, parameter);
            i++;
        }
        query.setFirstResult((pageSize) * ((page == 0 ? 1 : page) - 1));
        query.setMaxResults(pageSize);

        // Map<String, BigDecimal> valueMap = (Map<String, BigDecimal>)
        // query.uniqueResult();
        // Long total = valueMap.get("TOTAL").longValue();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("total",
                queryTransferBatchCount(queryTransferBean, page, pageSize));
        resultMap.put("rows", query.list());
        return resultMap;
    }
    @Transactional(readOnly = true)
    public Long queryTransferBatchCount(QueryTransferBean queryTransferBean,
            int page,
            int pageSize) {
        StringBuffer sqlCountBuffer = new StringBuffer(
                "select count(*) from PojoTranBatch  where 1=1 ");
        List<String> parameterList = new ArrayList<String>();
        if (queryTransferBean != null) {
            if (StringUtil.isNotEmpty(queryTransferBean.getBatchNo())) {
                sqlCountBuffer.append(" and tid = ? ");
                parameterList.add(queryTransferBean.getBatchNo());
            }
            if (StringUtil.isNotEmpty(queryTransferBean.getBeginDate())) {
                sqlCountBuffer.append(" and applyTime >= ? ");
                parameterList.add(queryTransferBean.getBeginDate());
            }
            if (StringUtil.isNotEmpty(queryTransferBean.getEndDate())) {
                sqlCountBuffer.append(" and applyTime <= ? ");
                parameterList.add(queryTransferBean.getEndDate());
            }
            if (StringUtil.isNotEmpty(queryTransferBean.getEndDate())) {
                sqlCountBuffer.append(" and status = ? ");
                parameterList.add(queryTransferBean.getStatus());
            }
        }
        Query countQuery = getSession().createQuery(sqlCountBuffer.toString());
        int i = 0;
        for (String parameter : parameterList) {
            countQuery.setParameter(i, parameter);
            i++;
        }
        return ((Long) countQuery.uniqueResult()).longValue();
    }

    @Transactional
    public void updateBatchTransferFinish(PojoTranBatch transferBatch) {
        // TODO Auto-generated method stub
        try {
            this.update(transferBatch);
        } catch (HibernateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new CMMException("M002");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public List<PojoTranData> queryWaitTrialTranData(long tranBatchId) {
        StringBuffer sqlBuffer = new StringBuffer(
                "from PojoTranData where 1=1 and tranBatch.tid = ? and status = ?");
        Query query = getSession().createQuery(sqlBuffer.toString());
        query.setParameter(0, tranBatchId);
        query.setParameter(1, "01");
        return query.list();
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void batchTrailTransfer(String batchNo, String status) {

    }

    public PojoTranBatch getByBatchId(long batchId) {
        String queryString = "from PojoTranBatch where tid = ? ";
        try {
            if (log.isDebugEnabled()) {
                log.info("queryString:" + queryString);
            }
            Query query = getSession().createQuery(queryString);
            query.setParameter(0, batchId);
            return (PojoTranBatch) query.uniqueResult();
        } catch (HibernateException e) {
            e.printStackTrace();
            throw new CMMException("M001");
        }
    }

}
