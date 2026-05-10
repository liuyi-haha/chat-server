package org.liuyi.chat.domain.message;

public record FileSize(long value, FileSizeUnit unit) {
    public long toBytes() {
        return unit.convert(FileSizeUnit.B, value);
    }

    public long toKB() {
        return unit.convert(FileSizeUnit.KB, value);
    }

    public long toMB() {
        return unit.convert(FileSizeUnit.MB, value);
    }

    public long toGB() {
        return unit.convert(FileSizeUnit.GB, value);
    }

}


