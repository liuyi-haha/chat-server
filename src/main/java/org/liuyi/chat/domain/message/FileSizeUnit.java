package org.liuyi.chat.domain.message;

public enum FileSizeUnit {
    B(1),
    KB(1024),
    MB(1024 * 1024),
    GB(1024 * 1024 * 1024);

    private final long multiplier;

    FileSizeUnit(long multiplier) {
        this.multiplier = multiplier;
    }

    public long convert(FileSizeUnit targetUnit, long value) {
        // 先转换成字节
        long sizeInBytes = value * this.multiplier;
        // 再转换成目标单位
        return sizeInBytes / targetUnit.multiplier;
    }
}
