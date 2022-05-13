/*
Navicat MySQL Data Transfer

Source Server         : MySql5.5
Source Server Version : 50532
Source Host           : localhost:3306
Source Database       : book_shop

Target Server Type    : MYSQL
Target Server Version : 50532
File Encoding         : 65001

Date: 2022-05-13 12:27:43
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for bs_address
-- ----------------------------
DROP TABLE IF EXISTS `bs_address`;
CREATE TABLE `bs_address` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `province` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `area` varchar(255) DEFAULT NULL,
  `detail_address` varchar(255) DEFAULT NULL,
  `email_code` varchar(10) NOT NULL,
  `receiver` varchar(100) NOT NULL,
  `tel` varchar(255) DEFAULT NULL,
  `is_default` char(1) NOT NULL DEFAULT '0' COMMENT '0',
  PRIMARY KEY (`id`),
  KEY `fk_user_id` (`user_id`),
  CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `bs_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bs_address
-- ----------------------------
INSERT INTO `bs_address` VALUES ('14', '5', '北京市', '北京市', '东城区', '寸金路1号', '524666', '李四', '13531113375', '0');
INSERT INTO `bs_address` VALUES ('17', '5', '黑龙江省', '哈尔滨市', '道里区', 'asdasd', '23', 'aaa', '18219394877', '0');
INSERT INTO `bs_address` VALUES ('19', '3', '北京市', '北京市', '东城区', 'asdas', '465654', 'aaa', '18219394877', '1');
INSERT INTO `bs_address` VALUES ('23', '5', '广东省', '湛江市', '吴川市', '梅录街道82号', '524500', '张三', '18219394877', '1');
INSERT INTO `bs_address` VALUES ('24', '6', '广东省', '广州市', '海珠区', '风阳街道西畔里大街十一巷15号', '524000', '赵六', '18219394877', '0');

-- ----------------------------
-- Table structure for bs_admin
-- ----------------------------
DROP TABLE IF EXISTS `bs_admin`;
CREATE TABLE `bs_admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bs_admin
-- ----------------------------
INSERT INTO `bs_admin` VALUES ('1', 'admin', 'admin');
INSERT INTO `bs_admin` VALUES ('2', 'admin2', 'admin2');

-- ----------------------------
-- Table structure for bs_appeal
-- ----------------------------
DROP TABLE IF EXISTS `bs_appeal`;
CREATE TABLE `bs_appeal` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `appeal_reason` varchar(255) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `state` int(1) DEFAULT '1' COMMENT '1:待审核，2：通过，3：不通过',
  `forbid_reason` varchar(255) DEFAULT NULL COMMENT '封号理由',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bs_appeal
-- ----------------------------
INSERT INTO `bs_appeal` VALUES ('8', '5', '我错了。。。', '2022-05-07 21:36:46', '3', '发表不当言论');
INSERT INTO `bs_appeal` VALUES ('12', '5', '我错了/', '2022-05-11 21:08:08', '2', '对书本《北纬78°》发表不当评论');
INSERT INTO `bs_appeal` VALUES ('13', '5', '我错了', '2022-05-12 16:32:59', '2', '对书本《此刻花开》发表不当言论');

-- ----------------------------
-- Table structure for bs_book
-- ----------------------------
DROP TABLE IF EXISTS `bs_book`;
CREATE TABLE `bs_book` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `isbn` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `author` varchar(255) NOT NULL,
  `publisher` varchar(255) NOT NULL,
  `publish_date` datetime NOT NULL,
  `old_price` double(10,2) DEFAULT NULL,
  `new_price` double(10,2) NOT NULL,
  `category` int(2) NOT NULL COMMENT '1文学类，2经管类，3情感类',
  `recommend` int(1) DEFAULT '0' COMMENT '是否推荐 1是，0否',
  `info` varchar(255) DEFAULT NULL,
  `img_url` varchar(255) DEFAULT NULL,
  `count` int(11) DEFAULT '99' COMMENT '商品库存',
  `state` int(1) DEFAULT '1' COMMENT '0：已下架，1：在售',
  `book_type` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bs_book
-- ----------------------------
INSERT INTO `bs_book` VALUES ('1', '9787513911139', '北纬78°  ', '陈丹燕', '中山大学出版社', '2022-05-17 21:26:47', '100.50', '44.50', '1', '1', '千万年来从未被碰触过的冰雪世界展开……只有来到遥远的北极，才能体会到地球本身才是终极的文明。与自然相比，一切人类的文明都散发出塑料的味道。', 'beiwei78.jpg', '195', '1', '1');
INSERT INTO `bs_book` VALUES ('2', '9787513911139', '100の东京大人味发现', '吴东龙', '民主与建设出版社', '2021-01-01 15:15:33', '100.00', '55.50', '3', '1', '100の东京大人味发现：用生活的感官觉发现东京的精致', '100dongjindarenweifaxian.jpg', '206', '1', '3');
INSERT INTO `bs_book` VALUES ('3', '9787535477101', '别走，万一好笑呢', '银教授', '长江文艺出版社', '2019-09-02 15:15:33', '35.00', '26.30', '3', '0', '微博人气博主@银教授个人作品集 银教授本人含泪自荐，希望你排着队拿着笑的号码牌，按顺序笑', 'biezouwanyihaoxiaone.jpg', '159', '1', '3');
INSERT INTO `bs_book` VALUES ('4', '9787550019775', '此刻花开', '徐静', '百花洲文艺出版社', '2019-09-02 15:15:33', '68.00', '34.00', '3', '0', ' 一场轻松自由的创作体验！风靡全球的纸雕艺术，比《秘密花园》更给力的减压新玩法。虽然我们不断被这个世界雕刻着，但我们亦可以雕刻出一个世界！', 'cikehuakai.jpg', '89', '1', '3');
INSERT INTO `bs_book` VALUES ('5', '9787516413999', '洞见', '项保华', '企业管理出版社', '2019-09-02 15:15:33', '48.00', '34.60', '1', '1', '通过揭示体现在决策初心、判断、选择、落实背后的人性特征与事物规律，为提升个体及组织的决策管理水平与能力提供实用的操作指导。', 'dongjian.jpg', '99', '1', '1');
INSERT INTO `bs_book` VALUES ('6', '9787308164207', '腾讯传', '吴晓波', '浙江大学出版社', '2020-08-01 15:15:33', '58.00', '40.10', '2', '0', '全景式记录腾讯成长轨迹，回望一代人的互联网情怀，解读中国互联网企业领先全球的真正秘密。', 'tenxunchuan.jpg', '97', '1', '2');
INSERT INTO `bs_book` VALUES ('7', '9787550293151', '拉普拉斯的魔女', '东野圭吾', '北京联合出版公司', '2019-09-02 15:15:33', '39.80', '28.70', '1', '0', '东野圭吾：“我想摧毁自己以前写的小说，于是，这部作品就此诞生。”', 'lapulasidemonv.jpg', '96', '1', '1');
INSERT INTO `bs_book` VALUES ('8', '9787540478612', '愿你的青春不负梦想', '俞敏洪', '湖南文艺出版社', '2019-09-02 15:15:33', '36.00', '21.60', '2', '0', '50-心路历程×25-创业思考×80场演讲精华，与不甘平庸的你，谈谈如何度过不悔的青春，实现你心中的梦想。', 'yuannideqingchunbufumengxiang.jpg', '100', '1', '2');
INSERT INTO `bs_book` VALUES ('9', '9787540478612', '情商高，就是说话', '朱凌,常清', '延边大学出版社', '2019-09-02 15:15:33', '38.00', '19.00', '2', '0', '教你洞悉人性、说话动听！所谓情商高，就是会说话。不拆台不揭短，不生硬不伤人，让你的每一句话都说得得体又令人舒服，到哪都受欢迎！', 'qingshanggaojiushishuohuarangrenshufu.jpg', '96', '1', '2');
INSERT INTO `bs_book` VALUES ('10', '9787507838992', '销售心理战', '霍金斯', '中国国际广播出版社', '2019-09-02 15:15:33', '36.80', '18.40', '2', '0', '销售就是察言、观色、攻心，销售就是要搞定人，各界的销售大师们强烈推崇的销售心理学！', 'xiaoshouxinlizhan.jpg', '100', '1', '2');
INSERT INTO `bs_book` VALUES ('11', '9787535448569', '小道理：分寸之间', '冯仑', '长江文艺出版社', '2019-09-02 15:15:33', '42.00', '33.10', '1', '0', '商界精英的时代沉思录，地产导师的人生理想国。', 'xiaodaolifencunzhijian.jpg', '99', '1', '1');
INSERT INTO `bs_book` VALUES ('12', '9787508667836', '名创优品没有秘密', '杜博奇', '中信出版社', '2019-09-02 15:15:33', '36.00', '27.00', '2', '0', ' 经济寒冬中的一匹黑马，关店浪潮下的逆势崛起！全面解读名创优品的商业模式，还原一个真实的零售世界！', 'NO SECRETS.jpg', '96', '1', '2');
INSERT INTO `bs_book` VALUES ('13', '9787538890433', '写给-轻人的创业课', '彭帅兴', '黑龙江科学技术出版社', '2019-09-02 15:15:33', '39.80', '19.90', '2', '0', ' 每一个怀揣梦想的创业者都应该细读的暖心之作“拿来即用”的实战经验，“一针见血”的创业指导！', 'xiegeinianqinrende1.jpg', '100', '1', '2');
INSERT INTO `bs_book` VALUES ('14', '9787201111728', '运营笔记', '类延昊', '天津人民出版社', '2019-09-02 15:15:33', '39.80', '19.90', '2', '0', '猫扑网辉煌时代缔造者之一类类告诉你：如何在互联网运营下半场到来之前，快速成长，实现运营人生的弯道超车。', 'yunyinbiji.jpg', '97', '1', '2');
INSERT INTO `bs_book` VALUES ('15', '9787221135506', '太空', '亚历山德拉·米热林斯', '贵州人民出版社', '2019-09-02 15:15:33', '68.00', '45.40', '1', '0', '浩瀚宇宙万千奇观，《太空》带你探索发现！本书献给喜欢仰望星空的少-科学家和梦想家！', 'taikong.jpg', '100', '1', '1');
INSERT INTO `bs_book` VALUES ('16', '9787113215491', '欧洲', '亲历者', '中国铁道出版社', '2019-09-02 15:15:33', '38.00', '29.30', '1', '1', '旅行达人实地感受，29项欧洲体验式旅行；全方位文化解读，你的欧洲之旅*精彩。', 'ouzhou.jpg', '100', '1', '1');
INSERT INTO `bs_book` VALUES ('17', '9787519219420', '西藏', '梅里', '世界图书出版公司', '2019-09-02 15:15:33', '39.80', '19.90', '1', '0', '西藏，一个可以改变人生的地方骑行，一种可以征服世界的方式不出门，你不知道你能走多远；不流泪，你不知道你有多坚强。西藏，一个可以改变人生的地方骑行，一种可以征服世界的方式不出门，你不知道你能走多远；不流泪，你不知道你有多坚强。', 'xizang.jpg', '94', '1', '1');
INSERT INTO `bs_book` VALUES ('18', '9787201111735', '我与世界只差一个你', '张皓宸', '天津人民出版社', '2019-09-02 15:15:33', '49.00', '38.70', '1', '1', '12个温馨治愈的情感故事，给-轻人爱的正能量和信心，让你在面对爱时无惧，怀疑爱时坚定。', 'woyushijiezhichayigeni.jpg', '100', '1', '1');
INSERT INTO `bs_book` VALUES ('19', '9787515818122', '卖产品就是卖自己', '梁汉桥', '中华工商联合出版社', '2019-09-02 15:15:33', '35.00', '17.50', '2', '0', '销售就是要玩转情商，销售就是要有效化解客户问题，销售就是要搞定人', 'maichanpinjiushimaiziji.jpg', '97', '1', '2');
INSERT INTO `bs_book` VALUES ('20', '9787555106920', '男孩的冒险书', '康恩·伊古尔登', '广西科学技术出版社', '2019-09-02 15:15:33', '198.00', '99.00', '3', '0', ' 有史以来给男孩的完美手册升级！英国作者献给小男子汉的重磅手绘大作！原版连续12周《纽约时报》排行榜冠军，《时代周刊》鼎力推荐！', 'nanhaidemaoxianshu.jpg', '100', '1', '3');
INSERT INTO `bs_book` VALUES ('21', '999999', '销售的诀窍', '张三', '中国出版社', '2022-04-11 00:03:03', '20.50', '55.50', '3', '1', '销售的诀窍在于口才。', 'get_thumbnail (6).jpg', '99', '0', '3');

-- ----------------------------
-- Table structure for bs_book_type
-- ----------------------------
DROP TABLE IF EXISTS `bs_book_type`;
CREATE TABLE `bs_book_type` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `type_name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bs_book_type
-- ----------------------------
INSERT INTO `bs_book_type` VALUES ('1', null);
INSERT INTO `bs_book_type` VALUES ('2', null);
INSERT INTO `bs_book_type` VALUES ('3', null);

-- ----------------------------
-- Table structure for bs_cart
-- ----------------------------
DROP TABLE IF EXISTS `bs_cart`;
CREATE TABLE `bs_cart` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `book_id` int(11) NOT NULL,
  `count` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_cart_user_id` (`user_id`),
  KEY `fk_cart_book_id` (`book_id`),
  CONSTRAINT `fk_cart_user_id` FOREIGN KEY (`user_id`) REFERENCES `bs_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bs_cart
-- ----------------------------
INSERT INTO `bs_cart` VALUES ('9', '5', '2', '1');

-- ----------------------------
-- Table structure for bs_comment
-- ----------------------------
DROP TABLE IF EXISTS `bs_comment`;
CREATE TABLE `bs_comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `book_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `create_time` datetime NOT NULL,
  `content` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bs_comment
-- ----------------------------
INSERT INTO `bs_comment` VALUES ('1', '1', '5', '2022-03-16 23:45:25', '真是TMD烂书一本！！！');
INSERT INTO `bs_comment` VALUES ('2', '7', '5', '2022-03-16 23:45:25', 'aaaaaaaaaaaaaaaaa啊啊啊啊啊啊啊啊啊啊啊啊啊素数是');
INSERT INTO `bs_comment` VALUES ('3', '11', '5', '2022-03-16 23:45:25', 'aaaaaaaaaaaaaaaaa啊啊啊啊啊啊啊啊啊啊啊啊啊素数是');
INSERT INTO `bs_comment` VALUES ('4', '1', '4', '2022-03-16 23:45:25', 'dddddff的做法都是');
INSERT INTO `bs_comment` VALUES ('5', '3', '5', '2022-03-20 22:11:21', '内容十分丰富。。。');
INSERT INTO `bs_comment` VALUES ('6', '2', '5', '2022-03-20 22:39:59', '书很好看，内容丰富。。。');
INSERT INTO `bs_comment` VALUES ('7', '12', '5', '2022-03-20 22:39:59', '书很好看，内容丰富。。。');
INSERT INTO `bs_comment` VALUES ('8', '6', '5', '2022-03-22 21:34:27', '书很好看，内容丰富');
INSERT INTO `bs_comment` VALUES ('9', '20', '5', '2022-03-22 21:34:27', '书很好看，内容丰富');
INSERT INTO `bs_comment` VALUES ('10', '12', '5', '2022-03-26 21:57:34', '名创优品yyds');
INSERT INTO `bs_comment` VALUES ('11', '7', '5', '2022-03-26 21:58:59', '东野圭吾yyds');
INSERT INTO `bs_comment` VALUES ('12', '3', '5', '2022-03-26 23:05:16', '书很好看');
INSERT INTO `bs_comment` VALUES ('13', '3', '5', '2022-03-27 20:50:52', '好看的不要不要的啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊          ');
INSERT INTO `bs_comment` VALUES ('14', '14', '5', '2022-03-28 21:53:12', '运营笔记真好看。把我运营地明明白白！！！');
INSERT INTO `bs_comment` VALUES ('15', '3', '3', '2022-03-28 23:25:00', '确实好笑，笑得我满地找头。。。');
INSERT INTO `bs_comment` VALUES ('16', '16', '5', '2022-04-01 18:20:40', '欧洲真的欧气满满~');
INSERT INTO `bs_comment` VALUES ('17', '4', '5', '2022-04-08 20:32:59', '花开半夏');
INSERT INTO `bs_comment` VALUES ('18', '4', '5', '2022-04-08 20:38:55', '花开二度❀');
INSERT INTO `bs_comment` VALUES ('19', '4', '5', '2022-04-27 17:14:01', 'yyds');
INSERT INTO `bs_comment` VALUES ('20', '6', '5', '2022-04-27 22:49:06', '真好看！！！');
INSERT INTO `bs_comment` VALUES ('21', '7', '5', '2022-04-29 00:34:01', '书真好看！！');
INSERT INTO `bs_comment` VALUES ('22', '4', '5', '2022-05-07 21:33:35', '真是TMD烂书一本！！！');
INSERT INTO `bs_comment` VALUES ('23', '1', '5', '2022-05-11 21:05:19', '好看');

-- ----------------------------
-- Table structure for bs_express
-- ----------------------------
DROP TABLE IF EXISTS `bs_express`;
CREATE TABLE `bs_express` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `exp_num` varchar(255) DEFAULT NULL COMMENT '订单号',
  `exp_company` varchar(255) DEFAULT NULL COMMENT '快递公司',
  `address_id` int(11) DEFAULT NULL COMMENT '地址对象id',
  `send_time` datetime DEFAULT NULL COMMENT '发货时间',
  `receive_time` datetime DEFAULT NULL COMMENT '收货时间',
  `courier` varchar(255) DEFAULT NULL COMMENT '快递员',
  `courier_phone` varchar(255) DEFAULT NULL COMMENT '快递员电话',
  `order_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bs_express
-- ----------------------------
INSERT INTO `bs_express` VALUES ('1', '20223301225959', '韵达', null, '2022-04-27 00:14:58', '2022-04-27 22:39:27', '易仲威', '18219394877', '86');
INSERT INTO `bs_express` VALUES ('2', '20223313822722', '中通', null, '2022-04-27 01:38:40', null, '易仲威', '18219394877', null);
INSERT INTO `bs_express` VALUES ('3', '20223314831116', '韵达', null, '2022-04-27 01:48:39', '2022-04-27 16:18:04', '易仲威', '18219394877', '90');
INSERT INTO `bs_express` VALUES ('4', '202234152559760', '中通', null, '2022-04-28 15:26:00', null, '一一一', '18219394877', '92');
INSERT INTO `bs_express` VALUES ('5', '202234152737149', '中通', null, '2022-04-28 15:27:44', '2022-04-28 15:28:29', '易仲威', '18219394877', '91');
INSERT INTO `bs_express` VALUES ('6', '20224103719767', '中通', null, '2022-05-09 00:37:20', '2022-05-09 00:39:48', '张三', '18219394877', '95');
INSERT INTO `bs_express` VALUES ('7', '202241213518340', '顺丰', null, '2022-05-09 21:35:31', null, '张三', '18219394877', '94');
INSERT INTO `bs_express` VALUES ('8', '20224321337216', '中通', null, '2022-05-11 21:03:56', '2022-05-11 21:04:50', '张三', '18219394877', '99');

-- ----------------------------
-- Table structure for bs_order
-- ----------------------------
DROP TABLE IF EXISTS `bs_order`;
CREATE TABLE `bs_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_num` varchar(50) NOT NULL,
  `create_date` datetime NOT NULL,
  `user_id` int(11) NOT NULL,
  `address_id` int(11) DEFAULT NULL,
  `order_status` varchar(2) DEFAULT NULL COMMENT '1待支付，2已支付/待发货，3支付失败，4取消支付，5已发货/待收货，6已收货/待评价',
  `express_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_order_user_id` (`user_id`),
  KEY `fk_order_addr_id` (`address_id`),
  CONSTRAINT `fk_order_addr_id` FOREIGN KEY (`address_id`) REFERENCES `bs_address` (`id`),
  CONSTRAINT `fk_order_user_id` FOREIGN KEY (`user_id`) REFERENCES `bs_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bs_order
-- ----------------------------
INSERT INTO `bs_order` VALUES ('91', '202204272240230001', '2022-04-27 22:40:23', '5', '14', '6', null);
INSERT INTO `bs_order` VALUES ('94', '202204292219070001', '2022-04-29 22:19:07', '6', '24', '5', null);
INSERT INTO `bs_order` VALUES ('95', '202205062351170001', '2022-05-06 23:51:17', '5', '17', '6', null);
INSERT INTO `bs_order` VALUES ('96', '202205090045340001', '2022-05-09 00:45:34', '5', '17', '4', null);
INSERT INTO `bs_order` VALUES ('97', '202205092207320001', '2022-05-09 22:07:32', '5', '17', '4', null);
INSERT INTO `bs_order` VALUES ('98', '202205112054130001', '2022-05-11 20:54:13', '5', '23', '4', null);
INSERT INTO `bs_order` VALUES ('99', '202205112055500001', '2022-05-11 20:55:50', '5', '23', '6', null);

-- ----------------------------
-- Table structure for bs_order_item
-- ----------------------------
DROP TABLE IF EXISTS `bs_order_item`;
CREATE TABLE `bs_order_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) NOT NULL,
  `book_id` int(11) NOT NULL,
  `count` int(11) NOT NULL,
  `state` int(1) NOT NULL DEFAULT '1' COMMENT '1：未评价，2：已评价，3：已取消',
  `single_price` double(10,2) DEFAULT NULL,
  `price` double(10,2) DEFAULT '0.00' COMMENT 'item价格：书本单价×数量',
  PRIMARY KEY (`id`),
  KEY `fk_item_order_id` (`order_id`),
  KEY `fk_book_id` (`book_id`),
  CONSTRAINT `fk_item_order_id` FOREIGN KEY (`order_id`) REFERENCES `bs_order` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=155 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bs_order_item
-- ----------------------------
INSERT INTO `bs_order_item` VALUES ('138', '91', '7', '3', '2', '28.70', '86.10');
INSERT INTO `bs_order_item` VALUES ('139', '91', '4', '6', '2', '34.00', '204.00');
INSERT INTO `bs_order_item` VALUES ('140', '91', '12', '2', '1', '27.00', '54.00');
INSERT INTO `bs_order_item` VALUES ('143', '94', '2', '3', '1', '22.00', '66.00');
INSERT INTO `bs_order_item` VALUES ('144', '94', '19', '3', '1', '17.50', '52.50');
INSERT INTO `bs_order_item` VALUES ('145', '94', '12', '2', '1', '27.00', '54.00');
INSERT INTO `bs_order_item` VALUES ('146', '95', '11', '1', '1', '33.10', '33.10');
INSERT INTO `bs_order_item` VALUES ('147', '95', '5', '4', '1', '34.60', '138.40');
INSERT INTO `bs_order_item` VALUES ('148', '96', '2', '2', '3', '55.50', '111.00');
INSERT INTO `bs_order_item` VALUES ('149', '96', '1', '3', '3', '78.20', '234.60');
INSERT INTO `bs_order_item` VALUES ('150', '97', '7', '1', '3', '28.70', '28.70');
INSERT INTO `bs_order_item` VALUES ('151', '98', '5', '3', '3', '34.60', '103.80');
INSERT INTO `bs_order_item` VALUES ('152', '98', '21', '3', '3', '55.50', '166.50');
INSERT INTO `bs_order_item` VALUES ('153', '98', '14', '3', '3', '19.90', '59.70');
INSERT INTO `bs_order_item` VALUES ('154', '99', '1', '5', '2', '78.20', '391.00');

-- ----------------------------
-- Table structure for bs_user
-- ----------------------------
DROP TABLE IF EXISTS `bs_user`;
CREATE TABLE `bs_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `state` int(1) NOT NULL DEFAULT '1' COMMENT '状态：1正常，2封号',
  `email` varchar(255) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `school` varchar(255) DEFAULT NULL,
  `forbid_reason` varchar(255) DEFAULT NULL COMMENT '封号理由',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bs_user
-- ----------------------------
INSERT INTO `bs_user` VALUES ('3', 'jack1', '123456', '1', '2037086512@qq.com', '15250420158', '广州大学', '发表不当评价');
INSERT INTO `bs_user` VALUES ('4', 'jack2', '123456', '1', '1104975916@qq.com', '15250420158', '广东海洋大学', '不当言论');
INSERT INTO `bs_user` VALUES ('5', 'locus', '520wei', '1', '994783520@qq.com', '18219394879', '岭南师范学院', '对书本《此刻花开》发表不当言论');
INSERT INTO `bs_user` VALUES ('6', 'test', '123', '1', '228469437@qq.com', '13531113375', '岭南师范学院', null);
INSERT INTO `bs_user` VALUES ('7', 'angle', '520wei', '1', '794387386@qq.com', '5555270', '岭南师范学院', null);
