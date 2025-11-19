package com.kreggscode.greekalphabets.data.translation

import com.kreggscode.greekalphabets.utils.containsGreek
import com.kreggscode.greekalphabets.utils.pronounceGreek
import java.util.Locale

data class ObjectTranslation(
    val canonicalEnglish: String,
    val Greek: String,
    val romanization: String
)

object ObjectTranslator {
    private data class TranslationEntry(
        val Greek: String,
        val aliases: List<String>
    )

    private val dictionaryEntries = listOf(
        entry("사과", "apple", "apples"),
        entry("바나나", "banana", "bananas"),
        entry("오렌지", "orange", "oranges"),
        entry("포도", "grape", "grapes"),
        entry("복숭아", "peach", "peaches"),
        entry("딸기", "strawberry", "strawberries"),
        entry("수박", "watermelon"),
        entry("파인애플", "pineapple", "pineapples"),
        entry("레몬", "lemon", "lemons"),
        entry("라임", "lime", "limes"),
        entry("토마토", "tomato", "tomatoes"),
        entry("감자", "potato", "potatoes"),
        entry("당근", "carrot", "carrots"),
        entry("양파", "onion", "onions"),
        entry("마늘", "garlic"),
        entry("브로콜리", "broccoli"),
        entry("상추", "lettuce"),
        entry("시금치", "spinach"),
        entry("계란", "egg", "eggs"),
        entry("우유", "milk"),
        entry("치즈", "cheese"),
        entry("빵", "bread", "loaf", "toast"),
        entry("케이크", "cake"),
        entry("쿠키", "cookie", "cookies"),
        entry("초콜릿", "chocolate"),
        entry("아이스크림", "ice cream"),
        entry("커피", "coffee"),
        entry("차", "tea"),
        entry("주스", "juice"),
        entry("물", "water", "bottle of water", "water bottle"),
        entry("음료수", "drink", "beverage", "soda"),
        entry("밥", "rice", "cooked rice"),
        entry("국수", "noodles"),
        entry("수프", "soup"),
        entry("샐러드", "salad"),
        entry("고기", "meat"),
        entry("생선", "fish"),
        entry("닭고기", "chicken"),
        entry("쇠고기", "beef"),
        entry("돼지고기", "pork"),
        entry("햄버거", "burger", "hamburger"),
        entry("피자", "pizza"),
        entry("샌드위치", "sandwich"),
        entry("김치", "kimchi"),
        entry("비빔밥", "bibimbap"),
        entry("라면", "ramen", "ramyeon", "instant noodles"),
        entry("김밥", "gimbap", "kimbap"),
        entry("불고기", "bulgogi"),
        entry("떡볶이", "tteokbokki"),
        entry("냉면", "naengmyeon", "cold noodles"),
        entry("컵", "cup"),
        entry("머그컵", "mug"),
        entry("접시", "plate"),
        entry("그릇", "bowl"),
        entry("숟가락", "spoon"),
        entry("포크", "fork"),
        entry("칼", "knife"),
        entry("젓가락", "chopsticks"),
        entry("병", "bottle"),
        entry("유리컵", "glass"),
        entry("냄비", "pot"),
        entry("프라이팬", "pan", "frying pan", "skillet"),
        entry("도마", "cutting board"),
        entry("현미경", "microscope"),
        entry("카메라", "camera"),
        entry("렌즈", "lens"),
        entry("삼각대", "tripod"),
        entry("폰", "phone", "cell phone", "mobile phone", "smartphone"),
        entry("태블릿", "tablet"),
        entry("노트북", "laptop", "notebook computer"),
        entry("컴퓨터", "computer", "pc", "desktop"),
        entry("모니터", "monitor", "screen"),
        entry("키보드", "keyboard"),
        entry("마우스", "mouse", "computer mouse"),
        entry("프린터", "printer"),
        entry("스캐너", "scanner"),
        entry("스마트워치", "smartwatch"),
        entry("헤드폰", "headphones"),
        entry("이어폰", "earphones", "earbuds"),
        entry("스피커", "speaker"),
        entry("마이크", "microphone"),
        entry("텔레비전", "television", "tv"),
        entry("라디오", "radio"),
        entry("시계", "clock"),
        entry("손목시계", "watch", "wristwatch"),
        entry("램프", "lamp", "desk lamp"),
        entry("전등", "light", "ceiling light"),
        entry("촛불", "candle"),
        entry("거울", "mirror"),
        entry("문", "door"),
        entry("창문", "window"),
        entry("벽", "wall"),
        entry("천장", "ceiling"),
        entry("바닥", "floor"),
        entry("계단", "stairs"),
        entry("엘리베이터", "elevator"),
        entry("의자", "chair"),
        entry("소파", "sofa", "couch"),
        entry("탁자", "table"),
        entry("책상", "desk"),
        entry("침대", "bed"),
        entry("매트리스", "mattress"),
        entry("베개", "pillow"),
        entry("담요", "blanket", "comforter"),
        entry("의류", "clothing", "clothes"),
        entry("셔츠", "shirt"),
        entry("티셔츠", "t-shirt"),
        entry("바지", "pants", "trousers"),
        entry("청바지", "jeans"),
        entry("스커트", "skirt"),
        entry("드레스", "dress"),
        entry("재킷", "jacket"),
        entry("코트", "coat"),
        entry("모자", "hat", "cap"),
        entry("장갑", "gloves"),
        entry("양말", "socks"),
        entry("신발", "shoes", "sneakers"),
        entry("구두", "dress shoes"),
        entry("슬리퍼", "slippers"),
        entry("가방", "bag"),
        entry("백팩", "backpack"),
        entry("핸드백", "handbag", "purse"),
        entry("지갑", "wallet"),
        entry("열쇠", "key", "keys"),
        entry("우산", "umbrella"),
        entry("책", "book"),
        entry("노트", "notebook"),
        entry("잡지", "magazine"),
        entry("신문", "newspaper"),
        entry("서류", "document", "paperwork"),
        entry("지도", "map"),
        entry("사진", "photo", "picture"),
        entry("달력", "calendar"),
        entry("필통", "pencil case"),
        entry("연필", "pencil"),
        entry("펜", "pen"),
        entry("지우개", "eraser"),
        entry("자", "ruler"),
        entry("가위", "scissors"),
        entry("풀", "glue", "glue stick"),
        entry("테이프", "tape"),
        entry("상자", "box"),
        entry("봉투", "envelope"),
        entry("쓰레기통", "trash can", "garbage can"),
        entry("청소기", "vacuum", "vacuum cleaner"),
        entry("빗자루", "broom"),
        entry("걸레", "cloth", "rag"),
        entry("양동이", "bucket"),
        entry("세탁기", "washing machine"),
        entry("건조기", "dryer"),
        entry("다리미", "iron"),
        entry("냉장고", "refrigerator", "fridge"),
        entry("오븐", "oven"),
        entry("가스레인지", "stove", "cooktop", "range"),
        entry("전자레인지", "microwave"),
        entry("식기세척기", "dishwasher"),
        entry("커피메이커", "coffee maker"),
        entry("에어컨", "air conditioner", "ac"),
        entry("선풍기", "fan"),
        entry("히터", "heater"),
        entry("라디에이터", "radiator"),
        entry("가습기", "humidifier"),
        entry("공기청정기", "air purifier"),
        entry("자동차", "car", "auto", "vehicle"),
        entry("택시", "taxi"),
        entry("버스", "bus"),
        entry("기차", "train"),
        entry("지하철", "subway", "metro"),
        entry("자전거", "bicycle", "bike"),
        entry("오토바이", "motorcycle", "motorbike"),
        entry("비행기", "airplane", "plane"),
        entry("배", "boat", "ship"),
        entry("트럭", "truck"),
        entry("헬리콥터", "helicopter"),
        entry("신호등", "traffic light"),
        entry("도로", "road", "street"),
        entry("다리", "bridge"),
        entry("공원", "park"),
        entry("정원", "garden"),
        entry("나무", "tree"),
        entry("꽃", "flower"),
        entry("풀", "grass"),
        entry("바다", "sea", "ocean"),
        entry("강", "river"),
        entry("호수", "lake"),
        entry("산", "mountain"),
        entry("하늘", "sky"),
        entry("구름", "cloud"),
        entry("해", "sun"),
        entry("달", "moon"),
        entry("별", "star"),
        entry("비", "rain"),
        entry("눈", "snow"),
        entry("바람", "wind"),
        entry("고양이", "cat"),
        entry("개", "dog"),
        entry("새", "bird"),
        entry("말", "horse"),
        entry("소", "cow"),
        entry("돼지", "pig"),
        entry("양", "sheep"),
        entry("닭", "chicken animal"),
        entry("오리", "duck"),
        entry("물고기", "fish animal"),
        entry("사람", "person", "human"),
        entry("남자", "man", "male"),
        entry("여자", "woman", "female"),
        entry("아이", "child", "kid"),
        entry("소년", "boy"),
        entry("소녀", "girl"),
        entry("아기", "baby"),
        entry("악기", "instrument", "musical instrument"),
        entry("기타", "guitar"),
        entry("피아노", "piano"),
        entry("바이올린", "violin"),
        entry("드럼", "drum", "drums"),
        entry("플루트", "flute"),
        entry("트럼펫", "trumpet"),
        entry("색소폰", "saxophone"),
        entry("친구", "friend"),
        entry("가족", "family"),
        entry("학생", "student"),
        entry("교사", "teacher"),
        entry("의사", "doctor"),
        entry("간호사", "nurse"),
        entry("경찰", "police", "police officer"),
        entry("소방관", "firefighter"),
        entry("요리사", "chef", "cook"),
        entry("운동선수", "athlete"),
        entry("음악가", "musician"),
        entry("예술가", "artist"),
        entry("배우", "actor", "actress")
    )

    private val translations: Map<String, ObjectTranslation> = buildMap {
        dictionaryEntries.forEach { word ->
            val romanization = pronounceGreek(word.Greek)
            val aliasForms = buildAliasForms(word.aliases)
            aliasForms.map { normalize(it) }
                .filter { it.isNotBlank() }
                .forEach { alias ->
                    if (!containsKey(alias)) {
                        put(alias, ObjectTranslation(aliasForms.first(), word.Greek, romanization))
                    }
                }
        }
    }

    fun translate(input: String): ObjectTranslation? {
        if (input.isBlank()) return null

        val normalized = normalize(input)
        translations[normalized]?.let { return it }

        val singularNormalized = normalize(singularizePhrase(input))
        if (singularNormalized.isNotBlank()) {
            translations[singularNormalized]?.let { return it }
        }

        val tokens = normalized.split(" ").filter { it.isNotBlank() }
        for (length in tokens.size downTo 1) {
            for (start in 0..tokens.size - length) {
                val phrase = tokens.subList(start, start + length).joinToString(" ")
                translations[phrase]?.let { return it }

                val singularPhrase = singularizeTokens(tokens.subList(start, start + length))
                if (singularPhrase.isNotBlank()) {
                    translations[singularPhrase]?.let { return it }
                }
            }
        }

        return translations.entries
            .asSequence()
            .filter { normalized.containsWord(it.key) }
            .sortedByDescending { it.key.length }
            .map { it.value }
            .firstOrNull()
    }

    private fun normalize(text: String): String {
        return text.lowercase(Locale.ROOT)
            .replace(Regex("[^a-z0-9\\s]"), " ")
            .replace(Regex("\\s+"), " ")
            .trim()
    }

    private fun String.containsWord(word: String): Boolean {
        if (word.isBlank()) return false
        val regex = Regex("\\b${Regex.escape(word)}\\b")
        return regex.containsMatchIn(this)
    }

    private fun entry(Greek: String, vararg aliases: String): TranslationEntry {
        val allAliases = buildSet {
            addAll(aliases)
            if (!containsGreek(Greek)) {
                add(Greek)
            }
        }.toList()
        return TranslationEntry(
            Greek = Greek,
            aliases = allAliases
        )
    }

    private fun buildAliasForms(aliases: List<String>): List<String> {
        val forms = linkedSetOf<String>()
        aliases.forEach { alias ->
            forms += alias
            forms += singularizePhrase(alias)
        }
        return forms.filter { it.isNotBlank() }
    }

    private fun singularizePhrase(text: String): String {
        return text.split(" ")
            .filter { it.isNotBlank() }
            .joinToString(" ") { word -> singularizeWord(word) }
    }

    private fun singularizeTokens(tokens: List<String>): String {
        return tokens.joinToString(" ") { singularizeWord(it) }
    }

    private fun singularizeWord(word: String): String {
        return when {
            word.endsWith("ies") -> word.dropLast(3) + "y"
            word.endsWith("ves") -> word.dropLast(3) + "f"
            word.endsWith("oes") -> word.dropLast(2)
            word.endsWith("ses") || word.endsWith("xes") || word.endsWith("zes") -> word.dropLast(2)
            word.endsWith("s") && word.length > 3 -> word.dropLast(1)
            else -> word
        }
    }
}

