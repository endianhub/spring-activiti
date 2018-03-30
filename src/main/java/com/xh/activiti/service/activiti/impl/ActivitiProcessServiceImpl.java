package com.xh.activiti.service.activiti.impl;

import java.util.LinkedList;
import java.util.List;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xh.activiti.commons.utils.PageData;
import com.xh.activiti.commons.utils.StringUtils;
import com.xh.activiti.service.activiti.IActivitiProcessService;

/**
 * <p>Title: Activiti流程-通用接口</p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月30日
 */
@Service
public class ActivitiProcessServiceImpl implements IActivitiProcessService {

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private RuntimeService runtimeService;

	/**
	 * <p>Title: 查询流程部署</p>
	 * <p>Description: 操作以下表：<br>
	 * 部署信息表ACT_RE_DEPLOYMENT</p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月30日
	 * 
	 * @return
	 */
	@Override
	public List<PageData> selectDeployList() {

		List<Deployment> list = repositoryService.createDeploymentQuery()//
				.orderByDeploymenTime()//
				.desc()//
				.list();

		LinkedList<PageData> linked = new LinkedList<>();
		PageData pd = null;
		for (Deployment deployment : list) {
			pd = new PageData();
			pd.put("id", deployment.getId());
			pd.put("name", deployment.getName());
			pd.put("deploymentTime", deployment.getDeploymentTime());
			linked.add(pd);
		}
		return linked;
	}

	/**
	 * <p>Title: 查询流程定义</p>
	 * <p>Description: 操作以下表：<br>
	 * 流程定义数据表ACT_RE_PROCDEF</p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月30日
	 * 
	 * @return
	 */
	@Override
	public List<PageData> selectDefinitionList() {
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()//
				.orderByDeploymentId()//
				.desc()//
				.list();

		LinkedList<PageData> linked = new LinkedList<>();
		PageData pd = null;
		for (ProcessDefinition processDefinition : list) {
			pd = new PageData();
			pd.put("id", processDefinition.getId());// 流程定义ID
			pd.put("name", processDefinition.getName());// 流程设计名称
			pd.put("key", processDefinition.getKey());// 流程定义key
			pd.put("version", processDefinition.getVersion());// 流程版本
			pd.put("diagramResourceName", processDefinition.getDiagramResourceName());// 流程图片文件
			pd.put("resourceName", processDefinition.getResourceName());// 流程定义文件
			pd.put("deploymentId", processDefinition.getDeploymentId());// 流程部署ID
			pd.put("description", processDefinition.getDescription());// 流程描述
			pd.put("category", processDefinition.getCategory());// 流程组织机构
			pd.put("tenantId", processDefinition.getTenantId());// 流程所有人ID
			linked.add(pd);
		}
		return linked;
	}

	/**
	 * <p>Title: 启动流程</p>
	 * <p>Description: 操作以下表：<br>
	 * 运行时流程执行实例表ACT_RU_EXECUTION<br>
	 * 历史流程实例表ACT_HI_PROCINST<br>
	 * 历史节点表ACT_HI_ACTINST<br>
	 * 运行时任务节点表ACT_RU_TASK<br>
	 * 历史任务实例表ACT_HI_TASKINST<br>
	 * 运行时流程人员表，主要存储任务节点与参与者的相关信息ACT_RU_IDENTITYLINK<br>
	 * 历史流程人员表ACT_HI_IDENTITYLINK<p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月30日
	 * 
	 * @param processDefinitionId
	 * @param variables
	 * @return
	 */
	@Override
	public boolean startProcess(String processDefinitionId, PageData variables) {

		ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinitionId, variables);

		// 流程实例中包含的信息
		System.out.println("当前活动节点  " + processInstance.getActivityId());
		System.out.println("关联业务键：  " + processInstance.getBusinessKey());
		System.out.println("流程部署id： " + processInstance.getDeploymentId());
		System.out.println("流程描述：       " + processInstance.getDescription());
		System.out.println("流程实例id： " + processInstance.getId());
		System.out.println("流程实例名称： " + processInstance.getName());
		System.out.println("父流程id：      " + processInstance.getParentId());
		System.out.println("流程定义id： " + processInstance.getProcessDefinitionId());
		System.out.println("流程定义key：    " + processInstance.getProcessDefinitionKey());
		System.out.println("流程定义名称： " + processInstance.getProcessDefinitionName());
		System.out.println("流程实例id： " + processInstance.getProcessInstanceId());
		System.out.println("流程所属人id：    " + processInstance.getTenantId());
		System.out.println("流程定义版本： " + processInstance.getProcessDefinitionVersion());
		System.out.println("流程变量：       " + processInstance.getProcessVariables().get("userName"));
		System.out.println("是否结束：       " + processInstance.isEnded());
		System.out.println("是否暂停：       " + processInstance.isSuspended());
		System.out.println("################################");
		return StringUtils.isNotBlank(processInstance.getId()) ? true : false;
	}

	/**
	 * <p>Title: 删除流程部署</p>
	 * <p>Description: 操作以下表：<br>
	 * 运行时流程执行实例表ACT_RU_EXECUTION<br>
	 * 历史流程实例表ACT_HI_PROCINST<br>
	 * 历史节点表ACT_HI_ACTINST<br>
	 * 运行时任务节点表ACT_RU_TASK<br>
	 * 历史任务实例表ACT_HI_TASKINST<br>
	 * 运行时流程人员表，主要存储任务节点与参与者的相关信息ACT_RU_IDENTITYLINK<br>
	 * 历史流程人员表ACT_HI_IDENTITYLINK<p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月30日
	 * 
	 * @param deploymentId
	 * @param cascade true:删除流程定义，包括启动过的流程;false:删除流程定义，只删除没有启动过的流程，如果流程启动则抛出异常
	 * @return
	 */
	@Override
	public boolean deleteDeployment(String deploymentId, boolean cascade) {
		try {
			repositoryService.deleteDeployment(deploymentId, cascade);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
