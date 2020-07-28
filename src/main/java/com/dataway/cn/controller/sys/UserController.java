package com.dataway.cn.controller.sys;

import com.dataway.cn.service.sys.IUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 * @author phil
 * @date 2020/06/08 10:07
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final IUserService userService;
}
