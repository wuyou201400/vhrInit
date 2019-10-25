package org.sang.controller.system;

import org.sang.bean.Hr;
import org.sang.bean.RespBean;
import org.sang.bean.Role;
import org.sang.mapper.HrMapper;
import org.sang.service.HrService;
import org.sang.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.io.File;
import java.util.Random;

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

    @RequestMapping(value = "/roles", method = RequestMethod.GET)
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

    @RequestMapping(value = "/reg", method = RequestMethod.POST)
    public RespBean hrReg(Hr hr) {
        int i = hrService.addHr(hr);
        if (i == 1) {
            return RespBean.ok("注册成功!");
        } else if (i == -1) {
            return RespBean.error("用户名重复，注册失败!");
        }
        return RespBean.error("注册失败!");
    }

    @RequestMapping(value = "/userface", method = RequestMethod.POST)
    public RespBean uploadUserface(MultipartFile file) throws SocketException, IOException {
        return SaveUserfaceFile(file);
    }

    @RequestMapping(value = "/changeUserface", method = RequestMethod.POST)
    public RespBean changeUserface(MultipartFile file, Long id) throws SocketException, IOException {
        RespBean savefile = SaveUserfaceFile(file);
        if (savefile.getStatus() != 200)
            return savefile;
        int effectrows = hrService.updateUserface(savefile.getMsg(), id);
        if (effectrows == 1)
            return savefile;
        else
            return RespBean.error("保存失败!");
    }

    private RespBean SaveUserfaceFile(MultipartFile file) {
        //返回上传的文件是否为空，即没有选择任何文件，或者所选文件没有内容。
        //防止上传空文件导致奔溃
        if (file.isEmpty()) {
            throw new NullPointerException("文件为空");
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        //为防止文件重名被覆盖，文件名取名为：当前日期-1-1000内随机数-原始文件名
        Random random = new Random();
        Integer randomFileName = random.nextInt(1000);
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
        fileName = sdf2.format(d) + "-" + randomFileName + "-" + fileName;

        //获取项目classes/static的地址
        String path = ClassUtils.getDefaultClassLoader().getResource("static").getPath();
        //图片访问URI(即除了协议、地址和端口号的URL)
        String url_path = "userface" + File.separator + sdf.format(d) + File.separator + fileName;
        String savePath = path + File.separator + url_path;  //图片保存路径
        File saveFile = new File(savePath);
        if (!saveFile.getParentFile().exists()) {
            saveFile.getParentFile().mkdirs();
        }

 /*       File path = new File(ResourceUtils.getURL("classpath:").getPath());
        if(!path.exists()) path = new File("");
        String savePath=path.getAbsolutePath(),"static/images/userface/";
        File upload = new File(savePath);
        if(!upload.exists()) upload.mkdirs();*/

        try {
            file.transferTo(saveFile);  //将临时存储的文件移动到真实存储路径下
        } catch (IOException e) {
            e.printStackTrace();
            return RespBean.error("保存失败");
        }
        return RespBean.ok(url_path);

    }
}
