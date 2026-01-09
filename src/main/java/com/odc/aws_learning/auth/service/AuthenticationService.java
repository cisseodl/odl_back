package com.odc.aws_learning.auth.service;

import com.odc.aws_learning.app.entity.Apprenant;
import com.odc.aws_learning.auth.dao.request.SignUpRequest;
import com.odc.aws_learning.auth.dao.request.SigninRequest;
import com.odc.aws_learning.auth.dao.request.UpdatePass;
import com.odc.aws_learning.auth.dao.response.JwtAuthenticationResponse;
import com.odc.aws_learning.auth.base.response.CResponse;
import org.springframework.web.multipart.MultipartFile;

public interface AuthenticationService {
    CResponse<JwtAuthenticationResponse> signup(SignUpRequest request, MultipartFile avatar);



    CResponse<?> updatePassword(UpdatePass updatePass);

    CResponse<JwtAuthenticationResponse> signin(SigninRequest request);

    CResponse<?> forgetPass(String username);

}
