package com.rutershok.daily.model

import com.rutershok.daily.R
import java.util.*

enum class Category(private val imageRes: Int, private val titleRes: Int, private val keyRes: Int) {
    ALL(R.drawable.bg_all, R.string.all, R.string.key_all), DESIRE(
        R.drawable.bg_desire,
        R.string.desire,
        R.string.key_desire
    ),
    DESTINY(
        R.drawable.bg_destiny,
        R.string.destiny,
        R.string.key_destiny
    ),
    DREAM(R.drawable.bg_dream, R.string.dream, R.string.key_dream), EMOTIONS(
        R.drawable.bg_emotions,
        R.string.emotions,
        R.string.key_emotions
    ),
    EYES(R.drawable.bg_eyes, R.string.eyes, R.string.key_eyes), FAMILY(
        R.drawable.bg_family,
        R.string.family,
        R.string.key_family
    ),
    FAMOUS_PEOPLE(
        R.drawable.bg_famous_people,
        R.string.famous_people,
        R.string.key_famous_people
    ),
    FREEDOM(
        R.drawable.bg_freedom,
        R.string.freedom,
        R.string.key_freedom
    ),
    FRIENDSHIP(
        R.drawable.bg_friendship,
        R.string.friendship,
        R.string.key_friendship
    ),
    HUG(R.drawable.bg_hug, R.string.hug, R.string.key_hug), LIFE(
        R.drawable.bg_life,
        R.string.life,
        R.string.key_life
    ),
    LOVE(R.drawable.bg_love, R.string.love, R.string.key_love), MOODS(
        R.drawable.bg_moods,
        R.string.moods,
        R.string.key_moods
    ),
    MOTIVATIONAL(
        R.drawable.bg_motivational,
        R.string.motivational,
        R.string.key_motivational
    ),
    MUSIC(R.drawable.bg_music, R.string.music, R.string.key_music), NATURE(
        R.drawable.bg_nature,
        R.string.nature,
        R.string.key_nature
    ),
    POETRY(
        R.drawable.bg_poetry,
        R.string.poetry,
        R.string.key_poetry
    ),
    SILENCE(
        R.drawable.bg_silence,
        R.string.silence,
        R.string.key_silence
    ),
    SMILE(R.drawable.bg_smile, R.string.smile, R.string.key_smile), SPORT(
        R.drawable.bg_sport,
        R.string.sport,
        R.string.key_sport
    ),
    SUCCESS(
        R.drawable.bg_success,
        R.string.success,
        R.string.key_success
    ),
    TECHNOLOGY(
        R.drawable.bg_technology,
        R.string.technology,
        R.string.key_technology
    ),
    TIME(R.drawable.bg_time, R.string.time, R.string.key_time), WISDOM(
        R.drawable.bg_wisdom,
        R.string.wisdom,
        R.string.key_wisdom
    ),
    WORK(R.drawable.bg_work, R.string.work, R.string.key_work);

    companion object {
        @JvmStatic
        val list = Arrays.asList(*values())
        fun getImageRes(position: Int): Int {
            return list[position].imageRes
        }

        fun getTitleRes(position: Int): Int {
            return list[position].titleRes
        }

        fun getKeyRes(position: Int): Int {
            return list[position].keyRes
        }
    }
}