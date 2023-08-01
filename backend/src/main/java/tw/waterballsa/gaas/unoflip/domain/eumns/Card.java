package tw.waterballsa.gaas.unoflip.domain.eumns;

import lombok.Getter;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum Card {
    CARD_1(1, LightCard.RED_4, DarkCard.ORANGE_DRAW_5),
    CARD_2(2, LightCard.YELLOW_DRAW_1, DarkCard.PINK_4),
    CARD_3(3, LightCard.RED_FLIP, DarkCard.ORANGE_7),
    CARD_4(4, LightCard.GREEN_8, DarkCard.TEAL_SKIP_EVERYONE),
    CARD_5(5, LightCard.YELLOW_SKIP, DarkCard.ORANGE_5),
    CARD_6(6, LightCard.YELLOW_2, DarkCard.PINK_6),
    CARD_7(7, LightCard.RED_REVERSE, DarkCard.TEAL_4),
    CARD_8(8, LightCard.YELLOW_7, DarkCard.TEAL_1),
    CARD_9(9, LightCard.GREEN_5, DarkCard.WILD),
    CARD_10(10, LightCard.BLUE_DRAW_1, DarkCard.PINK_8),
    CARD_11(11, LightCard.BLUE_4, DarkCard.ORANGE_FLIP),
    CARD_12(12, LightCard.RED_6, DarkCard.WILD),
    CARD_13(13, LightCard.RED_5, DarkCard.PURPLE_4),
    CARD_14(14, LightCard.BLUE_SKIP, DarkCard.TEAL_7),
    CARD_15(15, LightCard.GREEN_SKIP, DarkCard.PINK_DRAW_5),
    CARD_16(16, LightCard.RED_DRAW_1, DarkCard.PURPLE_1),
    CARD_17(17, LightCard.BLUE_5, DarkCard.ORANGE_DRAW_5),
    CARD_18(18, LightCard.BLUE_DRAW_1, DarkCard.ORANGE_5),
    CARD_19(19, LightCard.BLUE_9, DarkCard.PURPLE_5),
    CARD_20(20, LightCard.RED_9, DarkCard.PURPLE_9),
    CARD_21(21, LightCard.RED_7, DarkCard.PINK_9),
    CARD_22(22, LightCard.YELLOW_REVERSE, DarkCard.ORANGE_8),
    CARD_23(23, LightCard.GREEN_4, DarkCard.PINK_8),
    CARD_24(24, LightCard.WILD, DarkCard.ORANGE_7),
    CARD_25(25, LightCard.RED_1, DarkCard.PURPLE_3),
    CARD_26(26, LightCard.BLUE_2, DarkCard.ORANGE_8),
    CARD_27(27, LightCard.YELLOW_3, DarkCard.ORANGE_3),
    CARD_28(28, LightCard.BLUE_2, DarkCard.WILD_DRAW_COLOR),
    CARD_29(29, LightCard.GREEN_2, DarkCard.PINK_6),
    CARD_30(30, LightCard.GREEN_REVERSE, DarkCard.PURPLE_REVERSE),
    CARD_31(31, LightCard.YELLOW_9, DarkCard.PINK_1),
    CARD_32(32, LightCard.BLUE_SKIP, DarkCard.PURPLE_FLIP),
    CARD_33(33, LightCard.GREEN_1, DarkCard.PINK_7),
    CARD_34(34, LightCard.RED_9, DarkCard.ORANGE_9),
    CARD_35(35, LightCard.BLUE_8, DarkCard.PINK_SKIP_EVERYONE),
    CARD_36(36, LightCard.GREEN_SKIP, DarkCard.TEAL_2),
    CARD_37(37, LightCard.WILD, DarkCard.PURPLE_1),
    CARD_38(38, LightCard.BLUE_3, DarkCard.PINK_1),
    CARD_39(39, LightCard.RED_FLIP, DarkCard.PINK_REVERSE),
    CARD_40(40, LightCard.YELLOW_4, DarkCard.TEAL_8),
    CARD_41(41, LightCard.YELLOW_9, DarkCard.PURPLE_SKIP_EVERYONE),
    CARD_42(42, LightCard.YELLOW_8, DarkCard.TEAL_6),
    CARD_43(43, LightCard.GREEN_7, DarkCard.ORANGE_6),
    CARD_44(44, LightCard.YELLOW_REVERSE, DarkCard.PURPLE_7),
    CARD_45(45, LightCard.GREEN_1, DarkCard.TEAL_7),
    CARD_46(46, LightCard.RED_8, DarkCard.PINK_3),
    CARD_47(47, LightCard.RED_SKIP, DarkCard.TEAL_REVERSE),
    CARD_48(48, LightCard.WILD_DRAW_2, DarkCard.TEAL_8),
    CARD_49(49, LightCard.YELLOW_6, DarkCard.PINK_DRAW_5),
    CARD_50(50, LightCard.GREEN_8, DarkCard.PINK_REVERSE),
    CARD_51(51, LightCard.YELLOW_SKIP, DarkCard.PURPLE_SKIP_EVERYONE),
    CARD_52(52, LightCard.BLUE_4, DarkCard.PINK_REVERSE),
    CARD_53(53, LightCard.BLUE_9, DarkCard.PURPLE_6),
    CARD_54(54, LightCard.YELLOW_FLIP, DarkCard.PURPLE_5),
    CARD_55(55, LightCard.RED_5, DarkCard.PINK_FLIP),
    CARD_56(56, LightCard.GREEN_4, DarkCard.PURPLE_3),
    CARD_57(57, LightCard.BLUE_3, DarkCard.TEAL_REVERSE),
    CARD_58(58, LightCard.RED_1, DarkCard.PINK_2),
    CARD_59(59, LightCard.RED_2, DarkCard.PURPLE_2),
    CARD_60(60, LightCard.YELLOW_3, DarkCard.TEAL_FLIP),
    CARD_61(61, LightCard.BLUE_8, DarkCard.TEAL_FLIP),
    CARD_62(62, LightCard.BLUE_FLIP, DarkCard.ORANGE_9),
    CARD_63(63, LightCard.YELLOW_1, DarkCard.PURPLE_6),
    CARD_64(64, LightCard.WILD_DRAW_2, DarkCard.ORANGE_FLIP),
    CARD_65(65, LightCard.GREEN_9, DarkCard.ORANGE_REVERSE),
    CARD_66(66, LightCard.GREEN_FLIP, DarkCard.ORANGE_1),
    CARD_67(67, LightCard.GREEN_DRAW_1, DarkCard.TEAL_DRAW_5),
    CARD_68(68, LightCard.YELLOW_5, DarkCard.PURPLE_2),
    CARD_69(69, LightCard.GREEN_2, DarkCard.ORANGE_2),
    CARD_70(70, LightCard.GREEN_7, DarkCard.WILD),
    CARD_71(71, LightCard.WILD_DRAW_2, DarkCard.TEAL_5),
    CARD_72(72, LightCard.BLUE_7, DarkCard.PURPLE_SKIP_EVERYONE),
    CARD_73(73, LightCard.RED_6, DarkCard.PURPLE_8),
    CARD_74(74, LightCard.BLUE_7, DarkCard.PINK_3),
    CARD_75(75, LightCard.BLUE_5, DarkCard.ORANGE_3),
    CARD_76(76, LightCard.YELLOW_FLIP, DarkCard.TEAL_5),
    CARD_77(77, LightCard.GREEN_9, DarkCard.TEAL_1),
    CARD_78(78, LightCard.GREEN_REVERSE, DarkCard.TEAL_4),
    CARD_79(79, LightCard.RED_8, DarkCard.ORANGE_SKIP_EVERYONE),
    CARD_80(80, LightCard.WILD, DarkCard.PURPLE_7),
    CARD_81(81, LightCard.GREEN_FLIP, DarkCard.TEAL_9),
    CARD_82(82, LightCard.WILD, DarkCard.ORANGE_4),
    CARD_83(83, LightCard.RED_3, DarkCard.PINK_4),
    CARD_84(84, LightCard.YELLOW_6, DarkCard.WILD),
    CARD_85(85, LightCard.YELLOW_4, DarkCard.TEAL_6),
    CARD_86(86, LightCard.WILD_DRAW_2, DarkCard.WILD_DRAW_COLOR),
    CARD_87(87, LightCard.BLUE_FLIP, DarkCard.PURPLE_8),
    CARD_88(88, LightCard.RED_2, DarkCard.ORANGE_1),
    CARD_89(89, LightCard.BLUE_6, DarkCard.PINK_5),
    CARD_90(90, LightCard.BLUE_REVERSE, DarkCard.ORANGE_2),
    CARD_91(91, LightCard.GREEN_3, DarkCard.PINK_9),
    CARD_92(92, LightCard.BLUE_1, DarkCard.WILD_DRAW_COLOR),
    CARD_93(93, LightCard.YELLOW_DRAW_1, DarkCard.PURPLE_9),
    CARD_94(94, LightCard.GREEN_6, DarkCard.ORANGE_REVERSE),
    CARD_95(95, LightCard.BLUE_REVERSE, DarkCard.TEAL_DRAW_5),
    CARD_96(96, LightCard.GREEN_DRAW_1, DarkCard.PURPLE_DRAW_5),
    CARD_97(97, LightCard.BLUE_6, DarkCard.PURPLE_4),
    CARD_98(98, LightCard.BLUE_1, DarkCard.TEAL_9),
    CARD_99(99, LightCard.YELLOW_7, DarkCard.PURPLE_DRAW_5),
    CARD_100(100, LightCard.YELLOW_8, DarkCard.PINK_2),
    CARD_101(101, LightCard.YELLOW_1, DarkCard.PURPLE_FLIP),
    CARD_102(102, LightCard.YELLOW_2, DarkCard.ORANGE_4),
    CARD_103(103, LightCard.RED_DRAW_1, DarkCard.TEAL_2),
    CARD_104(104, LightCard.GREEN_3, DarkCard.WILD_DRAW_COLOR),
    CARD_105(105, LightCard.YELLOW_5, DarkCard.ORANGE_6),
    CARD_106(106, LightCard.GREEN_5, DarkCard.PINK_FLIP),
    CARD_107(107, LightCard.RED_3, DarkCard.TEAL_3),
    CARD_108(108, LightCard.RED_SKIP, DarkCard.ORANGE_SKIP_EVERYONE),
    CARD_109(109, LightCard.RED_REVERSE, DarkCard.TEAL_3),
    CARD_110(110, LightCard.GREEN_6, DarkCard.PINK_7),
    CARD_111(111, LightCard.RED_4, DarkCard.TEAL_SKIP_EVERYONE),
    CARD_112(112, LightCard.RED_7, DarkCard.PINK_5);

    private final int id;
    private final LightCard lightCard;
    private final DarkCard darkCard;

    private static final Map<Integer, Card> enumsByValue = Arrays.stream(values()).collect(Collectors.toMap(Card::getId, Function.identity()));

    Card(int id, LightCard lightCard, DarkCard darkCard) {
        this.id = id;
        this.lightCard = lightCard;
        this.darkCard = darkCard;
    }

    public static Card getLightInstance(int id) {
        return Optional.ofNullable(enumsByValue.get(id)).orElseThrow(() -> new IllegalArgumentException("unsupported id %d".formatted(id)));
    }

    public static List<Integer> getAllIds() {
        return new ArrayList<>(enumsByValue.keySet());
    }

}
