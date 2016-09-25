/*
 * Copyright (C) 2016 Muhammad Tayyab Akram
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#ifndef _TEHREER__OPEN_TYPE_TAG_H
#define _TEHREER__OPEN_TYPE_TAG_H

#include <jni.h>

namespace Tehreer {

class OpenTypeTag {
public:
    static jint make(jbyte a, jbyte b, jbyte c, jbyte d)
    {
        return (a << 24)
             | (b << 16)
             | (c <<  8)
             | (d <<  0);
    }
};

}

#endif