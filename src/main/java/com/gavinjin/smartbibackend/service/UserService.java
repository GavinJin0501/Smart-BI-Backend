package com.gavinjin.smartbibackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gavinjin.smartbibackend.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gavinjin.smartbibackend.model.dto.UserQueryRequest;
import com.gavinjin.smartbibackend.model.vo.LoginUserVO;
import com.gavinjin.smartbibackend.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * User service
 *
 * @author gavin
 * @description 针对表【user(User)】的数据库操作Service
 * @createDate 2023-09-12 21:22:35
 */
public interface UserService extends IService<User> {
    /**
     * User register
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * User login
     * @param userAccount
     * @param userPassword
     * @param request
     * @return
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * Get the current logged-in user
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * Get the current logged-in user (allow not logged in)
     * @param request
     * @return
     */
    User getLoginUserPermitNull(HttpServletRequest request);

    /**
     * Check if the user is admin
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * Check if the user is admin
     * @param user
     * @return
     */
    boolean isAdmin(User user);

    /**
     * User logout
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * Get user vo with sensitive data removed
     * @param user
     * @return
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * Get user vo with sensitive data removed
     * @param user
     * @return
     */
    UserVO getUserVO(User user);

    /**
     * Get user vo with sensitive data removed
     * @param userList
     * @return
     */
    List<UserVO> getUserVO(List<User> userList);

    /**
     * Get query requirements
     * @param userQueryRequest
     * @return
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);
}
