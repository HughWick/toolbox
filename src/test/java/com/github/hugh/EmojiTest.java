package com.github.hugh;

import com.github.hugh.util.EmojiUtils;
import org.junit.jupiter.api.Test;


/**
 * @author AS
 * @date 2020/9/11 15:32
 */
public class EmojiTest {

    @Test
    public void etste(){
        // LY💰🤩😎
        String str = "LY💰🤩😎是是的";
        String tem2 = "🇦🇫🇧🇪🚔🚛🖱🖲🔋🎙🕉🇦🇮🤩";
        String result = EmojiUtils.toHtml(tem2);
        System.out.println("-1-->>" + result);
//        System.out.println("--1->" + complete(result));
        System.out.println("==2=>>" + EmojiUtils.toHtml(str));
        System.out.println("==2=>>" + EmojiUtils.complete(str));
        System.out.println("---->>" + EmojiUtils.isEmoji(str));
    }
}
