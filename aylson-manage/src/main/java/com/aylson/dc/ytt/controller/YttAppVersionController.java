package com.aylson.dc.ytt.controller;

import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aylson.core.easyui.EasyuiDataGridJson;
import com.aylson.core.frame.controller.BaseController;
import com.aylson.core.frame.domain.Result;
import com.aylson.core.frame.domain.ResultCode;
import com.aylson.dc.ytt.search.YttAppVersionSearch;
import com.aylson.dc.ytt.service.YttAppVersionService;
import com.aylson.dc.ytt.vo.YttAppVersionVo;
import com.aylson.dc.sys.common.SessionInfo;
import com.aylson.utils.DateUtil2;
import com.aylson.utils.UUIDUtils;

/**
 * APK渠道版本配置
 * @author Minbo
 */
@Controller
@RequestMapping("/ytt/yttAppVersion")
public class YttAppVersionController extends BaseController {
	
	protected static final Logger logger = Logger.getLogger(YttAppVersionController.class);

	@Autowired
	private YttAppVersionService yttAppVersionService;
	
	/**
	 * 后台-首页
	 * @return
	 */
	@RequestMapping(value = "/admin/toIndex", method = RequestMethod.GET)
	public String toIndex() {
		return "/jsp/ytt/admin/yttAppVersion/index";
	}
	
	/**
	 * 获取列表
	 * @return list
	 */
	@RequestMapping(value = "/admin/list", method = RequestMethod.GET)
	@ResponseBody
	public EasyuiDataGridJson list(YttAppVersionSearch yttAppVersionSearch){
		EasyuiDataGridJson result = new EasyuiDataGridJson();//页面DataGrid结果集
		try{
			yttAppVersionSearch.setIsPage(true);
			List<YttAppVersionVo> list = this.yttAppVersionService.getList(yttAppVersionSearch);
			result.setTotal(this.yttAppVersionService.getRowCount(yttAppVersionSearch));
			result.setRows(list);
			return result;
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * 后台-添加页面
	 * @param yttAppVersionVo
	 * @return
	 */
	@RequestMapping(value = "/admin/toAdd", method = RequestMethod.GET)
	public String toAdd(YttAppVersionVo yttAppVersionVo) {
		this.request.setAttribute("yttAppVersionVo", yttAppVersionVo);
		return "/jsp/ytt/admin/yttAppVersion/add";
	}
	
	/**
	 * 后台-添加保存
	 * @param yttAppVersionVo
	 * @return
	 */
	@RequestMapping(value = "/admin/add", method = RequestMethod.POST)
	@ResponseBody
	public Result add(YttAppVersionVo yttAppVersionVo) {
		Result result = new Result();
		try{
			SessionInfo sessionInfo = (SessionInfo)this.request.getSession().getAttribute("sessionInfo");
			yttAppVersionVo.setId(UUIDUtils.create());
			String cTime = DateUtil2.getCurrentLongDateTime();
			yttAppVersionVo.setCreatedBy(sessionInfo.getUser().getUserName() + "/" + sessionInfo.getUser().getRoleName());
			yttAppVersionVo.setCreateDate(cTime);
			yttAppVersionVo.setUpdateDate(cTime);
			Boolean flag = this.yttAppVersionService.add(yttAppVersionVo);
			if(flag){
				result.setOK(ResultCode.CODE_STATE_200, "操作成功");
			}else{
				result.setError(ResultCode.CODE_STATE_4006, "操作失败");
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			result.setError(ResultCode.CODE_STATE_500, e.getMessage());
		}
		return result;
	}
	
	/**
	 * 后台-编辑页面
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/admin/toEdit", method = RequestMethod.GET)
	public String toEdit(String id) {
		if(id != null){
			YttAppVersionVo yttAppVersionVo = this.yttAppVersionService.getById(id);
			this.request.setAttribute("yttAppVersionVo", yttAppVersionVo);
		}
		return "/jsp/ytt/admin/yttAppVersion/add";
	}
	
	/**
	 * 后台-编辑保存
	 * @param yttAppVersionVo
	 * @return
	 */
	@RequestMapping(value = "/admin/update", method = RequestMethod.POST)
	@ResponseBody
	public Result update(YttAppVersionVo yttAppVersionVo) {
		Result result = new Result();
		try {
			SessionInfo sessionInfo = (SessionInfo)this.request.getSession().getAttribute("sessionInfo");
			yttAppVersionVo.setUpdatedBy(sessionInfo.getUser().getUserName() + "/" + sessionInfo.getUser().getRoleName());
			yttAppVersionVo.setUpdateDate(DateUtil2.getCurrentLongDateTime());
			Boolean flag = this.yttAppVersionService.edit(yttAppVersionVo);
			if(flag){
				result.setOK(ResultCode.CODE_STATE_200, "操作成功");
			}else{
				result.setError(ResultCode.CODE_STATE_4006, "操作失败");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setError(ResultCode.CODE_STATE_500, e.getMessage());
		}
		return result;
	}
	
	/**
	 * 根据id删除
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/admin/deleteById", method = RequestMethod.POST)
	@ResponseBody
	public Result deleteById(String id) {
		Result result = new Result();
		try{
			Boolean flag = this.yttAppVersionService.deleteById(id);
			if(flag){
				result.setOK(ResultCode.CODE_STATE_200, "删除成功");
			}else{
				result.setError(ResultCode.CODE_STATE_4006, "删除失败");
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			result.setError(ResultCode.CODE_STATE_500, e.getMessage());
		}
		return result;
	}
}
