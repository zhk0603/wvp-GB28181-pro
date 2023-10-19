package com.genersoft.iot.vmp.web.gb28181.base;


import com.genersoft.iot.vmp.conf.security.JwtUtils;
import com.genersoft.iot.vmp.conf.security.dto.JwtUser;
import com.genersoft.iot.vmp.conf.security.dto.LoginUser;
import com.genersoft.iot.vmp.service.IUserService;
import com.genersoft.iot.vmp.storager.dao.dto.User;
import com.genersoft.iot.vmp.vmanager.bean.WVPResult;
import com.topevery.sdk.sync.BaseSyncController;
import com.topevery.sdk.sync.query.BaseQuery;
import com.topevery.sdk.sync.service.ISyncDataHandle;
import com.topevery.sdk.sync.vo.OpenTokenVO;
import com.topevery.sdk.sync.vo.OpenUserVO;
import com.topevery.sdk.sync.web.DataPager;
import com.topevery.sdk.sync.web.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/base/")
public class BaseDataHandleController extends BaseSyncController {

    @Autowired
    private ISyncDataHandle dataHandle;

    @Autowired
    private IUserService userService;

    @GetMapping(value = "loginByToken")
    public WVPResult<LoginUser> loginByToken(@RequestParam(name = "token") String token, HttpServletResponse response) {
        OpenTokenVO openTokenVO = dataHandle.loginByToken(token, super.properties);
        JwtUser jwtUser = JwtUtils.verifyToken(openTokenVO.getAccess_token());
        String username = jwtUser.getUserName();
        User user = userService.getUserByUsername(username);
        LoginUser loginUser = new LoginUser(user, LocalDateTime.now());
        response.setHeader(JwtUtils.getHeader(), openTokenVO.getAccess_token());
        loginUser.setAccessToken(openTokenVO.getAccess_token());
        return WVPResult.success(loginUser);
    }



/*    @PostMapping(value = "getUsers")
    public JsonResult<DataPager<OpenUserVO>> getUsers(@RequestBody BaseQuery query) {
        return JsonResult.ok(dataHandle.getUsers(query,super.properties));
    }*/

}
