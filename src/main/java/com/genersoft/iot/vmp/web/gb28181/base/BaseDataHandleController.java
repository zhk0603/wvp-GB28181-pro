package com.genersoft.iot.vmp.web.gb28181.base;


import com.topevery.sdk.sync.BaseSyncController;
import com.topevery.sdk.sync.query.BaseQuery;
import com.topevery.sdk.sync.service.ISyncDataHandle;
import com.topevery.sdk.sync.vo.OpenTokenVO;
import com.topevery.sdk.sync.vo.OpenUserVO;
import com.topevery.sdk.sync.web.DataPager;
import com.topevery.sdk.sync.web.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/base/")
public class BaseDataHandleController extends BaseSyncController {

    @Autowired
    private ISyncDataHandle dataHandle;

    @GetMapping(value = "loginByToken")
    public JsonResult<OpenTokenVO> loginByToken(@RequestParam(name = "token") String token) {
        return JsonResult.ok(dataHandle.loginByToken(token,super.properties));
    }



/*    @PostMapping(value = "getUsers")
    public JsonResult<DataPager<OpenUserVO>> getUsers(@RequestBody BaseQuery query) {
        return JsonResult.ok(dataHandle.getUsers(query,super.properties));
    }*/

}
