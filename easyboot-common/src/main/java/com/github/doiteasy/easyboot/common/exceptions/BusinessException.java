/*
 * Copyright 2019-2029 geekidea(https://github.com/geekidea)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.doiteasy.easyboot.common.exceptions;


import com.github.doiteasy.easyboot.common.result.ResultCodeEnum;
import org.apache.commons.lang3.ArrayUtils;

/**
 * 500打头的服务端异常
 */
public class BusinessException extends EasyBootException{

    public BusinessException(String message) {
        super(500,message);
    }
    public BusinessException(String message, Throwable cause) {
        super(500,message,cause);
    }
    public BusinessException(Integer errorCode, String message) {
        super(errorCode,message);
    }
    public BusinessException(Integer errorCode, String message, Throwable cause) {
        super(errorCode,message,cause);
    }

    public static BusinessException with(ResultCodeEnum codeEnum, Object... messages) {
        String message = codeEnum.getMessage();
        if (ArrayUtils.isNotEmpty(messages)) {
            message = String.format(message, messages);
        }
        return new BusinessException(codeEnum.getCode(),message);
    }

    public static BusinessException with(ResultCodeEnum codeEnum,Throwable cause, Object... messages) {
        String message = codeEnum.getMessage();
        if (ArrayUtils.isNotEmpty(messages)) {
            message = String.format(message, messages);
        }
        return new BusinessException(codeEnum.getCode(),message,cause);
    }


}
