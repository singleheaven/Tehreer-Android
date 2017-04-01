/*
 * Copyright (C) 2017 Muhammad Tayyab Akram
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

package com.mta.tehreer.opentype;

import com.mta.tehreer.internal.Raw;

class StructTable implements SfntTable {

    private final Object source;
    private final long pointer;

    StructTable(Object source, long pointer) {
        this.source = source;
        this.pointer = pointer;
    }

    @Override
    public byte[] readBytes(int offset, int count) {
        byte[] array = new byte[count];
        Raw.copyInt8Array(pointer + offset, array, 0, count);

        return array;
    }

    @Override
    public short readInt16(int offset) {
        return (short) Raw.getUInt16FromArray(pointer + offset, 0);
    }

    @Override
    public int readUInt16(int offset) {
        return Raw.getUInt16FromArray(pointer + offset, 0);
    }

    @Override
    public int readInt32(int offset) {
        return Raw.getInt32FromArray(pointer + offset, 0);
    }

    @Override
    public long readUInt32(int offset) {
        return Raw.getInt32FromArray(pointer + offset, 0) & 0xFFFFFFFFL;
    }

    @Override
    public long readInt64(int offset) {
        return ((Raw.getInt32FromArray(pointer + offset, 0) & 0xFFFFFFFFL) << 32)
             | ((Raw.getInt32FromArray(pointer + offset, 1) & 0xFFFFFFFFL) << 0);
    }
}