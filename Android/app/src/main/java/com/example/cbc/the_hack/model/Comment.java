package com.example.cbc.the_hack.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ABINGCBC
 * on 2019-11-20
 */
public class Comment {
    public static final int SAMPLE_DATA_ITEM_COUNT = 15;

    public static ArrayList<String> generateSampleData() {
        final ArrayList<String> data = new ArrayList<String>(SAMPLE_DATA_ITEM_COUNT);
        data.add("一般来说， 一般来讲，我们都必须务必慎重的考虑考虑。 可是，即使是这样，古诗的出现仍然代表了一定的意义。 古诗，发生了会如何，不发生又会如何。");
        data.add("罗素·贝克在不经意间这样说过，一个人即使已登上顶峰，也仍要自强不息。这不禁令我深思。");
        data.add("既然如何， 史美尔斯曾经提到过，书籍把我们引入最美好的社会，使我们认识各个时代的伟大智者。这不禁令我深思。");
        data.add("古诗的发生，到底需要如何做到，不古诗的发生，又会如何产生。 一般来说， 一般来说， 问题的关键究竟为何？");
        data.add("我们都知道，只要有意义，那么就必须慎重考虑。 我们一般认为，抓住了问题的关键，其他一切则会迎刃而解。");

        data.add("经过上述讨论， 经过上述讨论， 希腊说过一句富有哲理的话，最困难的事情就是认识自己。这不禁令我深思。");
        data.add("带着这些问题，我们来审视一下古诗。 可是，即使是这样，古诗的出现仍然代表了一定的意义。");
        data.add("这种事实对本人来说意义重大，相信对这个世界也是有一定意义的。 一般来讲，我们都必须务必慎重的考虑考虑。");
        data.add("维龙曾经提到过，要成功不需要什么特别的才能，只要把你能做的小事做得好就行了。我希望诸位也能好好地体会这句话。");
        data.add(" 经过上述讨论， 鲁巴金说过一句富有哲理的话，读书是在别人思想的帮助下，建立起自己的思想。这句话语虽然很短，但令我浮想联翩。");

        data.add("现在，解决古诗的问题，是非常非常重要的。 所以， 我们一般认为，抓住了问题的关键，其他一切则会迎刃而解。");
        data.add("一般来说， 我们都知道，只要有意义，那么就必须慎重考虑。 所谓古诗，关键是古诗需要如何写。");
        data.add("西班牙曾经提到过，自知之明是最难得的知识。这句话语虽然很短，但令我浮想联翩。 就我个人来说，古诗对我的意义，不能不说非常重大。");
        data.add("孔子在不经意间这样说过，知之者不如好之者，好之者不如乐之者。我希望诸位也能好好地体会这句话。");
        data.add(" 这样看来， 了解清楚古诗到底是一种怎么样的存在，是解决一切问题的关键。");

        return data;
    }
}

