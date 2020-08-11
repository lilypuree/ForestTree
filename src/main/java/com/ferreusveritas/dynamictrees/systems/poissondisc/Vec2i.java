package com.ferreusveritas.dynamictrees.systems.poissondisc;

/**
 * MIT License
 *
 * Copyright (c) 2016,2017 Ferreus Veritas
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

public class Vec2i {

    public int x;
    public int z;
    public boolean tight;//for use with 2nd and 3rd circle finder algorithm

    public Vec2i() {}

    public Vec2i(int x, int z) {
        this(x, z, false);
    }

    public Vec2i(int x, int z, boolean tight) {
        set(x, z);
        this.tight = tight;
    }

    public Vec2i(Vec2i aCoord) {
        this(aCoord.x, aCoord.z);
        setTight(aCoord.tight);
    }

    public Vec2i set(int x, int z) {
        this.x = x;
        this.z = z;
        return this;
    }

    public Vec2i set(Vec2i other) {
        set(other.x, other.z);
        this.tight = other.tight;
        return this;
    }

    public Vec2i setTight(boolean state) {
        tight = state;
        return this;
    }

    public boolean isTight() {
        return tight;
    }

    public Vec2i add(int x, int z) {
        this.x += x;
        this.z += z;
        return this;
    }

    public Vec2i sub(int x, int z) {
        this.x -= x;
        this.z -= z;
        return this;
    }

    public Vec2i add(Vec2i other) {
        return add(other.x, other.z);
    }

    public Vec2i sub(Vec2i other) {
        return sub(other.x, other.z);
    }

    public double len() {
        return Math.sqrt(x * x + z * z);//Pythagoras winks
    }

    public double angle() {
        return Math.atan2(z, x);
    }

    public static int crossProduct(Vec2i c1, Vec2i c2) {
        return (c1.x * c2.z) - (c1.z * c2.x);
    }

    @Override
    public boolean equals(Object o) {
        Vec2i v = (Vec2i) o;
        return this.x == v.x && this.z == v.z;
    }

    @Override
    public int hashCode() {
        return x ^ (z * 98764313);
    }

    @Override
    public String toString() {
        return "Coord " + x + "," + z + "," + (tight ? "T" : "L");
    }

}
