package com.rayin.htmladapter.openhtmltopdf.utils;

import org.apache.pdfbox.text.TextPosition;

import java.util.List;

public class TextPositionSequence implements CharSequence{
    public TextPositionSequence(List<TextPosition> textPositions) {
        this(textPositions, 0, textPositions.size());
    }

    public TextPositionSequence(List<TextPosition> textPositions, int start, int end) {
        this.textPositions = textPositions;
        this.start = start;
        this.end = end;
    }

    @Override
    public int length() {
        return end - start;
    }

    @Override
    public char charAt(int index) {
        TextPosition textPosition = textPositionAt(index);
        String text = textPosition.getUnicode();
        return text.charAt(0);
    }

    @Override
    public TextPositionSequence subSequence(int start, int end) {
        return new TextPositionSequence(textPositions, this.start + start, this.start + end);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(length());
        for (int i = 0; i < length(); i++)
        {
            builder.append(charAt(i));
        }
        return builder.toString();
    }

    public TextPosition textPositionAt(int index) {
        return textPositions.get(start + index);
    }

    public float getX() {
        return textPositions.get(start).getXDirAdj();
    }

    public float getY() {
        return textPositions.get(start).getYDirAdj();
    }

    public float getWidth() {
        if (end == start)
            return 0;
        TextPosition first = textPositions.get(start);
        TextPosition last = textPositions.get(end - 1);
        return last.getWidthDirAdj() + last.getXDirAdj() - first.getXDirAdj();
    }

    final List<TextPosition> textPositions;
    final int start, end;
}
