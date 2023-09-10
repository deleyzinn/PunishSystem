package me.deley.punish.punishment;

import lombok.Getter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Classe base para os punishments
 */


@Getter public class Punishment {
    private final PunishmentConstructor category;
    private final String playerName;
    private final UUID playerUniqueId;
    private final String senderName;
    private final UUID senderUniqueId;
    private final long punishmentDate;
    private final long expirationTime;
    private final String customReason;
    private final String ipAddress;
    private final String banID;

    public Punishment(final PunishmentConstructor category, final String playerName, final UUID playerUniqueId,
                      final String senderName, final UUID senderUniqueId, final long expirationTime, long punishmentDate,
                      final String customReason, final String ipAddress, final String banID) {
        this.category = category;
        this.playerName = playerName;
        this.playerUniqueId = playerUniqueId;
        this.senderName = senderName;
        this.senderUniqueId = senderUniqueId;
        this.punishmentDate = System.currentTimeMillis();
        this.expirationTime = expirationTime;
        this.customReason = customReason;
        this.ipAddress = ipAddress;
        this.banID = banID;
    }
    public String toDateApplied() {
        Date date = new Date(this.punishmentDate);
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
        return df2.format(date);
    }
    public boolean hasIpAddress() {
        return ipAddress != null && !ipAddress.isEmpty();
    }

    public boolean hasCustomReason() {
        return this.customReason != null && !this.customReason.isEmpty();
    }
    public boolean hasExpiration() {
        return this.expirationTime != -1L;
    }

    public boolean isExpired() {
        return hasExpiration() && expirationTime < System.currentTimeMillis();
    }

}
