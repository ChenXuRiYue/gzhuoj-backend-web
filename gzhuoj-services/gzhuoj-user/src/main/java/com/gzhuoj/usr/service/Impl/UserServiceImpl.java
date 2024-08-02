package com.gzhuoj.usr.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gzhuoj.usr.config.JwtProperties;
import com.gzhuoj.usr.dto.req.UserInfoUpdateReqDTO;
import com.gzhuoj.usr.dto.req.UserLoginReqDTO;
import com.gzhuoj.usr.dto.resp.UserInfoRespDTO;
import com.gzhuoj.usr.dto.resp.UserInfoUpdateRespDTO;
import com.gzhuoj.usr.dto.resp.UserLoginRespDTO;
import com.gzhuoj.usr.model.entity.UserDO;
import com.gzhuoj.usr.mapper.UserMapper;
import com.gzhuoj.usr.service.UserService;
import com.gzhuoj.usr.utils.JwtTool;
import common.biz.user.UserContext;
import common.convention.errorcode.BaseErrorCode;
import common.exception.ClientException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static common.convention.errorcode.BaseErrorCode.USER_ACCOUNT_VERIFY_ERROR;
import static common.convention.errorcode.BaseErrorCode.USER_PASSWORD_VERIFY_ERROR;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {
    private final StringRedisTemplate stringRedisTemplate;
    private final JwtTool jwtTool;
    private final JwtProperties jwtProperties;
    @Override
    public String test() {
        return "fuck";
    }

    /**
     * @param requestParam
     * @return
     */
    @Override
    public UserLoginRespDTO login(UserLoginReqDTO requestParam, HttpServletResponse response) {
        // TODO 覆盖更多的场景，用户名不存在。等等情形
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUserAccount, requestParam.getUserAccount())
                .eq(UserDO::getDeleteFlag, 0);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        if (userDO == null) {
            throw new ClientException(USER_ACCOUNT_VERIFY_ERROR);
        }
        if(!Objects.equals(userDO.getPassword(), requestParam.getPassword())){
            throw new ClientException(USER_PASSWORD_VERIFY_ERROR);
        }

//        response.addHeader("Access-Controler-Expose-headers", "token");
        Integer role = userDO.getRole() == null ? 2 : userDO.getRole();
        String token = jwtTool.createToken(userDO.getUserAccount(), role, jwtProperties.getTokenTTL());
        response.addHeader("token", token);
// token 本身就可以包含用户信息；
//        // 用redis存储用户信息 ->  返回一个token来证明用户已经登录
//        String KEY = "Login_" + requestParam.getUserAccount();
//        Map<Object, Object> keyMap = stringRedisTemplate.opsForHash().entries(KEY);
//        // 使用一套更加完善的校验工具。
//        if (CollUtil.isNotEmpty(keyMap)) {
//            String token = keyMap.keySet()
//                    .stream()
//                    .findFirst()
//                    .map(Object::toString)
//                    .orElseThrow(() -> new ClientException("用户登录失败"));
//            return new UserLoginRespDTO(token);
//        }
//        String uuid = UUID.randomUUID().toString();
//
//        stringRedisTemplate.opsForHash().put(KEY, uuid, JSON.toJSONString(userDO));
//        stringRedisTemplate.expire(KEY, 30L, TimeUnit.DAYS);
        return new UserLoginRespDTO(userDO.getUserAccount(), userDO.getUsername());
    }

    @Override
    public UserInfoUpdateRespDTO updateInfo(UserInfoUpdateReqDTO requestParam) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUserAccount, requestParam.getUserAccount())
                .eq(UserDO::getDeleteFlag, 0);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        if (userDO == null) {
            throw new ClientException("用户不存在");
        }
        if (StrUtil.isEmpty(requestParam.getPassword())) {
            throw new ClientException("密码不能为空");
        }
        if (!Objects.equals(requestParam.getPassword(), userDO.getPassword())) {
            throw new ClientException("密码错误");
        }

        userDO.setEmail(!StrUtil.isEmpty(requestParam.getEmail()) ? requestParam.getEmail() : userDO.getEmail());
        userDO.setOrganization(!StrUtil.isEmpty(requestParam.getOrganization()) ? requestParam.getOrganization() : userDO.getOrganization());
        userDO.setUsername(!StrUtil.isEmpty(requestParam.getUsername()) ? requestParam.getUsername() : userDO.getUsername());
        if (!StrUtil.isEmpty(requestParam.getNewPassword())) {
            if (!Objects.equals(requestParam.getNewPassword(), requestParam.getVerifyNewPassword())) {
                throw new ClientException("两次密码不一致");
            }
            userDO.setPassword(requestParam.getNewPassword());
        }
        LambdaUpdateWrapper<UserDO> updateWrapper = Wrappers.lambdaUpdate(UserDO.class)
                .eq(UserDO::getUserAccount, requestParam.getUserAccount())
                .eq(UserDO::getDeleteFlag, 0);
        baseMapper.update(userDO, updateWrapper);
        return new UserInfoUpdateRespDTO(requestParam.getUserAccount());
    }

    @Override
    public UserInfoRespDTO userInfo(String userAccount) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUserAccount, userAccount)
                .eq(UserDO::getDeleteFlag, 0);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        if(userDO == null){
            throw new ClientException("用户不存在");
        }
        return BeanUtil.toBean(userDO, UserInfoRespDTO.class);
    }

    public static void main(String[] args) {

    }
}
