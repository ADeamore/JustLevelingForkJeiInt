package com.seniors.justlevelingfork.config.models;

import com.seniors.justlevelingfork.JustLevelingFork;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class LockEnchant {

    public String Enchant = "minecraft:diamond";

    public List<AptitudeLevel> Aptitudes = List.of(new AptitudeLevel());

    public LockEnchant() {
    }

    public LockEnchant(String itemName) {
        Enchant = itemName;
    }

    public LockEnchant(String itemName, AptitudeLevel... aptitudes) {
        Enchant = itemName;
        Aptitudes = Arrays.stream(aptitudes).toList();
    }

    public static LockEnchant getLockEnchantFromString(String value, LockEnchant defaultValue) {
        try {
            return formatString(value);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    private static LockEnchant formatString(String value) {
        String[] initialSplit = value.split("#");
        LockEnchant lockEnchant = new LockEnchant(initialSplit[0]);

        List<AptitudeLevel> aptitudeList = new ArrayList<>();
        String[] aptitudeSplit = initialSplit[1].split(";");

        for (String aptitudeString : aptitudeSplit) {
            String[] aptitude = aptitudeString.split(":");
            int level = Integer.parseInt(aptitude[1]);
            if(level < 2 || level > 1000) throw new IndexOutOfBoundsException();
            aptitudeList.add(new AptitudeLevel(aptitude[0], level));
        }

        lockEnchant.Aptitudes = aptitudeList;
        return lockEnchant;
    }

    @Override
    public String toString() {
        if(Aptitudes.stream().anyMatch(Objects::isNull)){
            JustLevelingFork.getLOGGER().info(">> Found null aptitude at item {}", this.Enchant);
        }
        List<String> strings = new ArrayList<>();
        try{
            strings = Aptitudes.stream().map(AptitudeLevel::toString).toList();
        } catch (NullPointerException e){
            JustLevelingFork.getLOGGER().info(">> Found null aptitude at item {}", this.Enchant);
        }

        String aptitudeStringList = String.join(";", strings);

        return String.format("%s#%s", Enchant, aptitudeStringList);
    }

    public static class AptitudeLevel {

        public EAptitude Aptitude;

        public int Level;

        public AptitudeLevel(String aptitudeName, int level) {
            try {
                Aptitude = EAptitude.valueOf(StringUtils.capitalize(aptitudeName));
            } catch (IllegalArgumentException e){
                JustLevelingFork.getLOGGER().info(">> Wrong aptitude name {}", aptitudeName);
                Aptitude = EAptitude.Strength;
            }
            Level = level;
        }

        public AptitudeLevel() {
            Aptitude = EAptitude.Strength;
            Level = 2;
        }

        @Override
        public String toString() {
            return String.format("%s:%d", Aptitude.toString(), Level);
        }
    }
}
