package org.sang.controller.system;

import org.sang.bean.Hr;
import org.sang.bean.RespBean;
import org.sang.bean.Role;
import org.sang.service.HrService;
import org.sang.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.SocketException;
import java.util.List;

/**
 * Created by sang on 2018/1/2.
 */
@RestController
@RequestMapping("/system/hr")
public class SystemHrController {
    @Autowired
    HrService hrService;
    @Autowired
    RoleService roleService;

    @RequestMapping("/id/{hrId}")
    public Hr getHrById(@PathVariable Long hrId) {
        return hrService.getHrById(hrId);
    }

    @RequestMapping(value = "/{hrId}", method = RequestMethod.DELETE)
    public RespBean deleteHr(@PathVariable Long hrId) {
        if (hrService.deleteHr(hrId) == 1) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public RespBean updateHr(Hr hr) {
        if (hrService.updateHr(hr) == 1) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @RequestMapping(value = "/roles", method = RequestMethod.PUT)
    public RespBean updateHrRoles(Long hrId, Long[] rids) {
        if (hrService.updateHrRoles(hrId, rids) == rids.length) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @RequestMapping("/{keywords}")
    public List<Hr> getHrsByKeywords(@PathVariable(required = false) String keywords) {
        List<Hr> hrs = hrService.getHrsByKeywords(keywords);
        return hrs;
    }

    @RequestMapping(value = "/roles",method = RequestMethod.GET)
    public List<Role> allRoles() {
        return roleService.getRolesByHrid();
    }

    @RequestMapping(value = "/hr/reg", method = RequestMethod.POST)
    public RespBean hrReg(String username, String password) {
        int i = hrService.hrReg(username, password);
        if (i == 1) {
            return RespBean.ok("注册成功!");
        } else if (i == -1) {
            return RespBean.error("用户名重复，注册失败!");
        }
        return RespBean.error("注册失败!");
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload(MultipartFile file) throws SocketException, IOException {
        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 在file文件夹中创建名为fileName的文件
        OutputStreamWriter op = new OutputStreamWriter(new FileOutputStream("./file/" + fileName), "UTF-8");
        // 获取文件输入流
        InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());
        char[] bytes = new char[12];
        // 如果这里的bytes不是数组，则每次只会读取一个字节，例如test会变成 t   e     s    t
        while (inputStreamReader.read(bytes) != -1){
            op.write(bytes);
        }
        // 关闭输出流
        op.close();
        // 关闭输入流
        inputStreamReader.close();
        return "上传成功";
    }

}
