
package com.newcore.orbps.service.pcms.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.pcms.bo.JoinComMioRecBo;
import com.newcore.orbps.models.pcms.bo.JoinComOutBo;



/**
* <p>参与共保财务与财务队列落地服务</p>
* @author zhangyuan
* @date 2016年11月28日
*/
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@FunctionalInterface
public interface SetJoinComMioRecService {
	
	/**
	 * 
	* <p>参与共保的业务在协议落地之后，业务人员在确认财务已经到账只有，进行共保的财务数据流转，并将此动作记录老短险实收付流水，并流转转财务队列。 </p>
	* @author zhangyuan
	* @date 2016年11月28日
	 */
	@POST
    @Path("/setJoin")
	public JoinComOutBo setJoinComMioRecService(JoinComMioRecBo joinComMioRecBo);

}
