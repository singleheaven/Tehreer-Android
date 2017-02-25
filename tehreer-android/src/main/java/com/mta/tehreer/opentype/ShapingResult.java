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

import com.mta.tehreer.internal.util.Constants;
import com.mta.tehreer.internal.util.RawInt32Floats;
import com.mta.tehreer.internal.util.RawInt32Points;
import com.mta.tehreer.internal.util.RawSizeValues;
import com.mta.tehreer.internal.util.RawUInt16Values;
import com.mta.tehreer.util.Disposable;
import com.mta.tehreer.util.FloatList;
import com.mta.tehreer.util.IntList;
import com.mta.tehreer.util.PointList;

/**
 * An <code>ShapingResult</code> object is a container for the results of text shaping. It is filled
 * by <code>ShapingEngine</code> to provide the information related to characters, their glyphs,
 * offsets, and advances. It can be safely accessed from multiple threads but only one thread should
 * be allowed to manipulate it at a time.
 */
public class ShapingResult implements Disposable {

    private static class Finalizable extends ShapingResult {

        private Finalizable(ShapingResult parent) {
            super(parent);
        }

        @Override
        public void dispose() {
            throw new UnsupportedOperationException(Constants.EXCEPTION_FINALIZABLE_OBJECT);
        }

        @Override
        protected void finalize() throws Throwable {
            try {
                super.dispose();
            } finally {
                super.finalize();
            }
        }
    }

    /**
     * Wraps a shaping result object into a finalizable instance which is guaranteed to be
     * disposed automatically by the GC when no longer in use. After calling this method,
     * <code>dispose()</code> should not be called on either original object or returned object.
     * Calling <code>dispose()</code> on returned object will throw an
     * <code>UnsupportedOperationException</code>.
     * <p>
     * <strong>Note:</strong> The behaviour is undefined if an already disposed object is passed-in
     * as a parameter.
     *
     * @param shapingResult The shaping result object to wrap into a finalizable instance.
     * @return The finalizable instance of the passed-in shaping result object.
     */
    public static ShapingResult finalizable(ShapingResult shapingResult) {
        if (shapingResult.getClass() == ShapingResult.class) {
            return new Finalizable(shapingResult);
        }

        if (shapingResult.getClass() != Finalizable.class) {
            throw new IllegalArgumentException(Constants.EXCEPTION_SUBCLASS_NOT_SUPPORTED);
        }

        return shapingResult;
    }

    /**
     * Checks whether a shaping result object is finalizable or not.
     *
     * @param shapingResult The shaping result object to check.
     * @return <code>true</code> if the passed-in shaping result object is finalizable,
     *         <code>false</code> otherwise.
     */
    public static boolean isFinalizable(ShapingResult shapingResult) {
        return (shapingResult.getClass() == Finalizable.class);
    }

	long nativeResult;

    /**
     * Constructs a shaping result object.
     */
	ShapingResult() {
	    nativeResult = nativeCreate();
	}

    private ShapingResult(ShapingResult other) {
        this.nativeResult = other.nativeResult;
    }

    /**
     * Returns <code>true</code> if the text flows backward for this <code>ShapingResult</code>
     * object.
     *
     * @return <code>true</code> if the text flows backward, <code>false</code> otherwise.
     */
	public boolean isBackward() {
	    return nativeIsBackward(nativeResult);
	}

    /**
     * Returns the index to the first character in source text for this <code>ShapingResult</code>
     * object.
     *
     * @return The index to the first character in source text.
     */
	public int getCharStart() {
        return nativeGetCharStart(nativeResult);
    }

    /**
     * Returns the index after the last character in source text for this <code>ShapingResult</code>
     * object.
     *
     * @return The index after the last character in source text.
     */
    public int getCharEnd() {
        return nativeGetCharEnd(nativeResult);
    }

    /**
     * Returns the number of glyphs in this <code>ShapingResult</code> object.
     *
     * @return The number of glyphs in this <code>ShapingResult</code> object.
     */
	public int getGlyphCount() {
		return nativeGetGlyphCount(nativeResult);
	}

    /**
     * Returns a list of glyph IDs in this <code>ShapingResult</code> object.
     *
     * <strong>Note:</strong> The returned list might exhibit undefined behavior if the
     * <code>ShapingResult</code> object is disposed.
     *
     * @return A list of glyph IDs.
     */
    public IntList getGlyphIds() {
        return new RawUInt16Values(nativeGetGlyphIdsPtr(nativeResult),
                                   nativeGetGlyphCount(nativeResult));
    }

    /**
     * Returns a list of glyph offsets in this <code>ShapingResult</code> object.
     *
     * <strong>Note:</strong> The returned list might exhibit undefined behavior if the
     * <code>ShapingResult</code> object is disposed.
     *
     * @return A list of glyph offsets.
     */
    public PointList getGlyphOffsets() {
        return new RawInt32Points(nativeGetGlyphOffsetsPtr(nativeResult),
                                  nativeGetGlyphCount(nativeResult),
                                  nativeGetSizeByEm(nativeResult));
    }

    /**
     * Returns a list of glyph advances in this <code>ShapingResult</code> object.
     *
     * <strong>Note:</strong> The returned list might exhibit undefined behavior if the
     * <code>ShapingResult</code> object is disposed.
     *
     * @return A list of glyph advances.
     */
    public FloatList getGlyphAdvances() {
        return new RawInt32Floats(nativeGetGlyphAdvancesPtr(nativeResult),
                                  nativeGetGlyphCount(nativeResult),
                                  nativeGetSizeByEm(nativeResult));
    }

    public IntList getCharToGlyphMap() {
        return new RawSizeValues(nativeGetCharToGlyphMapPtr(nativeResult),
                                 nativeGetCharCount(nativeResult));
    }

	@Override
	public void dispose() {
        nativeDispose(nativeResult);
    }

    @Override
    public String toString() {
        return "ShapingResult{isBackward=" + Boolean.toString(isBackward())
                + ", charStart=" + getCharStart()
                + ", charEnd=" + getCharEnd()
                + ", glyphCount=" + getGlyphCount()
                + ", glyphIds=" + getGlyphIds().toString()
                + ", glyphOffsets=" + getGlyphOffsets().toString()
                + ", glyphAdvances=" + getGlyphAdvances().toString()
                + ", charToGlyphMap=" + getCharToGlyphMap().toString()
                + "}";
    }

	private static native long nativeCreate();
	private static native void nativeDispose(long nativeResult);

	private static native boolean nativeIsBackward(long nativeResult);
    private static native float nativeGetSizeByEm(long nativeResult);
	private static native int nativeGetCharStart(long nativeResult);
	private static native int nativeGetCharEnd(long nativeResult);
    private static native int nativeGetCharCount(long nativeResult);
	private static native int nativeGetGlyphCount(long nativeResult);

    private static native long nativeGetGlyphIdsPtr(long nativeResult);
    private static native long nativeGetGlyphOffsetsPtr(long nativeResult);
    private static native long nativeGetGlyphAdvancesPtr(long nativeResult);
    private static native long nativeGetCharToGlyphMapPtr(long nativeResult);
}
