package com.example.coursemanagementapp.ui.home

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class HomeViewModelTest {

    @Test
    suspend fun `url and rep are null`() {
        val homeViewModel = HomeViewModel()
        val url = ""
        val res = ""
        val result = homeViewModel.getCourseInfo(url, res)
        assertEquals(result, res)
    }

    @Test
    suspend fun `url is null`() {
        val homeViewModel = HomeViewModel()
        val url = ""
        val res =
            "[{\"id\":\"\",\"name\":\"\",\"icon_url\":\"\",\"number_of_topics\":0,\"teacher_name\":\"\",\"last_attempted_ts\":0}]"
        val result = homeViewModel.getCourseInfo(url, res)
        assertEquals(result, res)
    }

    @Test
    suspend fun `res is null`() {
        val homeViewModel = HomeViewModel()
        val url = "https://native-team-code-test-api.herokuapp.com/api/courses"
        val res = ""
        val expected =
            "[{\"id\":\"5d662ee46fa3b05b18005223\",\"name\":\"高2 化学＜有機編＞\",\"icon_url\":\"https://mediacdn.studysapuri.jp/media/W1siZiIsIjIwMTkvMDQvMDgvMDkvMTUvNTQvMDBjYmFhMWYtMGEzYi00MDFiLWEzZjItOTM2YjAzOTE5ZTIzL3Nha2F0YS5wbmciXSxbInAiLCJ0aHVtYiIsIjYwMHg%2BIix7fV1d.png?sha=53cf02d67d8cd49e\",\"number_of_topics\":20,\"teacher_name\":\"坂田 薫\",\"last_attempted_ts\":1566977764},{\"id\":\"5d662ed608fc5b5e770033d6\",\"name\":\"高3 ハイ&スタンダードレベル英語＜英単語補充編＞\",\"icon_url\":\"https://mediacdn.studysapuri.jp/media/W1siZiIsIjIwMTYvMDIvMjQvMTMvMzkvNDYvYmFjMjZlODMtOWY2Yy00YmJjLWFhMGUtZjg5OWYwNTViYzNkL3Nla2kucG5nIl0sWyJwIiwidGh1bWIiLCI2MDB4PiIse31dXQ.png?sha=d4513b70bc0c565d\",\"number_of_topics\":3,\"teacher_name\":\"関 正生\",\"last_attempted_ts\":1566977750},{\"id\":\"5d662ed28946d334ee001eac\",\"name\":\"高3 スタンダードレベル英語＜文法編＞\",\"teacher_name\":\"関 正生\",\"number_of_topics\":24,\"icon_url\":\"https://mediacdn.studysapuri.jp/media/W1siZiIsIjIwMTYvMDIvMjQvMTQvMTMvMTgvYzA1MGNhM2YtYTU2OC00YmU1LTlmOTEtOGI2ZjEwNzA2Y2NjL3Nla2kucG5nIl0sWyJwIiwidGh1bWIiLCI2MDB4PiIse31dXQ.png?sha=5ae972bf50e3c405\",\"last_attempted_ts\":1566977746},{\"id\":\"5d662ebf02303b2a16000931\",\"name\":\"【高校受験】国語 岩手県 公立高校受験対策\",\"teacher_name\":\"笹森 義通\",\"number_of_topics\":28,\"icon_url\":\"https://mediacdn.studysapuri.jp/media/W1siZiIsIjIwMTcvMDcvMjUvMDEvMjAvNDcvN2M5MTA5YmUtOTc1Yi00YzMxLWExMzAtYmVmZWUwNTVhNGQ4LyVFNyVBQyVCOSVFNiVBMyVBRSVFNSU4NSU4OCVFNyU5NCU5Ri5wbmciXSxbInAiLCJ0aHVtYiIsIjYwMHg%2BIix7fV1d.png?sha=9bfb66106019f517\",\"last_attempted_ts\":1566977727},{\"id\":\"5d662eb175335604850002a2\",\"name\":\"中学 社会 地理(基礎)\",\"number_of_topics\":31,\"teacher_name\":\"伊藤 賀一\",\"icon_url\":\"https://mediacdn.studysapuri.jp/media/W1siZiIsIjIwMTYvMDIvMjQvMTMvNTUvMzEvMWFiNGU3YWUtNmU1NS00MDVmLTlmMDYtZGJmM2M0ZjZiMjJjL2l0by5wbmciXSxbInAiLCJ0aHVtYiIsIjYwMHg%2BIix7fV1d.png?sha=70730b0ab1ece4c4\",\"last_attempted_ts\":1566977713},{\"id\":\"5d662eab03f65065c7002bba\",\"name\":\"中3 理科(基礎)\",\"teacher_name\":\"佐川 大三\",\"number_of_topics\":26,\"icon_url\":\"https://mediacdn.studysapuri.jp/media/W1siZiIsIjIwMTcvMDIvMDEvMDIvMTcvMDYvMmFkMjZlZGMtYmRjOC00MTYzLWJjODItNWNmYzk0ZTQwN2NjLyVFNCVCRCU5MCVFNSVCNyU5RCVFNSU4NSU4OCVFNyU5NCU5RiVFMyU4MiVBNCVFMyU4MyVBOSVFMyU4MiVCOSVFMyU4MyU4OC5wbmciXSxbInAiLCJ0aHVtYiIsIjYwMHg%2BIix7fV1d.png?sha=ee79b50d4497cdc4\",\"last_attempted_ts\":1566977707},{\"id\":\"5d662ea4f506f4742a011a4b\",\"name\":\"中3 国語(応用)\",\"teacher_name\":\"笹森 義通\",\"number_of_topics\":35,\"icon_url\":\"https://mediacdn.studysapuri.jp/media/W1siZiIsIjIwMTcvMDQvMTMvMDYvNTIvNTUvYjFhNmNhYzYtNzIxNy00YjI4LThlOGEtYzJlMWViMDRiNmRlL3Nhc2Ftb3JpLnBuZyJdLFsicCIsInRodW1iIiwiNjAweD4iLHt9XV0.png?sha=0293c8040f6be1d1\",\"last_attempted_ts\":1566977700},{\"id\":\"5d662ea2736b6c536b00ece4\",\"name\":\"中3 国語(三省堂 現代の国語)\",\"number_of_topics\":18,\"teacher_name\":\"今中 陽子\",\"icon_url\":\"https://mediacdn.studysapuri.jp/media/W1siZiIsIjIwMTcvMDIvMTUvMDEvMDAvMTkvNGJiOWY2MDQtZjJmOS00MDg4LWE4MzEtMjJkNTdhMDI5YzE0L2ltYW5ha2EucG5nIl0sWyJwIiwidGh1bWIiLCI2MDB4PiIse31dXQ.png?sha=0dafaba3a6aa5ed3\",\"last_attempted_ts\":1566977698},{\"id\":\"5d662e9bd84d8f7ca5001532\",\"name\":\"【定期テスト対策講座】中3 英語(教育出版 ONE WORLD)\",\"number_of_topics\":12,\"teacher_name\":\"竹内健\",\"icon_url\":\"https://mediacdn.studysapuri.jp/media/W1siZiIsIjIwMTcvMDQvMjUvMDMvNDAvMTUvNTJlZGUyY2MtMjkyNy00ZWRlLThkZjEtYTA3ZWY5Y2JjNTQwLyVFNyVBQiVCOSVFNSU4NiU4NSVFNSU4MSVBNSVFNSU4NSU4OCVFNyU5NCU5Ri5wbmciXSxbInAiLCJ0aHVtYiIsIjYwMHg%2BIix7fV1d.png?sha=027451af05983716\",\"last_attempted_ts\":1566977691}]"
        val result = homeViewModel.getCourseInfo(url, res)
        assertEquals(result, expected)
    }

    @Test
    suspend fun `url nor res is null`() {
        val homeViewModel = HomeViewModel()
        val url = "https://native-team-code-test-api.herokuapp.com/api/courses"
        val res =
            "[{\"id\":\"\",\"name\":\"\",\"icon_url\":\"\",\"number_of_topics\":0,\"teacher_name\":\"\",\"last_attempted_ts\":0}]"
        val expected =
            "[{\"id\":\"5d662ee46fa3b05b18005223\",\"name\":\"高2 化学＜有機編＞\",\"icon_url\":\"https://mediacdn.studysapuri.jp/media/W1siZiIsIjIwMTkvMDQvMDgvMDkvMTUvNTQvMDBjYmFhMWYtMGEzYi00MDFiLWEzZjItOTM2YjAzOTE5ZTIzL3Nha2F0YS5wbmciXSxbInAiLCJ0aHVtYiIsIjYwMHg%2BIix7fV1d.png?sha=53cf02d67d8cd49e\",\"number_of_topics\":20,\"teacher_name\":\"坂田 薫\",\"last_attempted_ts\":1566977764},{\"id\":\"5d662ed608fc5b5e770033d6\",\"name\":\"高3 ハイ&スタンダードレベル英語＜英単語補充編＞\",\"icon_url\":\"https://mediacdn.studysapuri.jp/media/W1siZiIsIjIwMTYvMDIvMjQvMTMvMzkvNDYvYmFjMjZlODMtOWY2Yy00YmJjLWFhMGUtZjg5OWYwNTViYzNkL3Nla2kucG5nIl0sWyJwIiwidGh1bWIiLCI2MDB4PiIse31dXQ.png?sha=d4513b70bc0c565d\",\"number_of_topics\":3,\"teacher_name\":\"関 正生\",\"last_attempted_ts\":1566977750},{\"id\":\"5d662ed28946d334ee001eac\",\"name\":\"高3 スタンダードレベル英語＜文法編＞\",\"teacher_name\":\"関 正生\",\"number_of_topics\":24,\"icon_url\":\"https://mediacdn.studysapuri.jp/media/W1siZiIsIjIwMTYvMDIvMjQvMTQvMTMvMTgvYzA1MGNhM2YtYTU2OC00YmU1LTlmOTEtOGI2ZjEwNzA2Y2NjL3Nla2kucG5nIl0sWyJwIiwidGh1bWIiLCI2MDB4PiIse31dXQ.png?sha=5ae972bf50e3c405\",\"last_attempted_ts\":1566977746},{\"id\":\"5d662ebf02303b2a16000931\",\"name\":\"【高校受験】国語 岩手県 公立高校受験対策\",\"teacher_name\":\"笹森 義通\",\"number_of_topics\":28,\"icon_url\":\"https://mediacdn.studysapuri.jp/media/W1siZiIsIjIwMTcvMDcvMjUvMDEvMjAvNDcvN2M5MTA5YmUtOTc1Yi00YzMxLWExMzAtYmVmZWUwNTVhNGQ4LyVFNyVBQyVCOSVFNiVBMyVBRSVFNSU4NSU4OCVFNyU5NCU5Ri5wbmciXSxbInAiLCJ0aHVtYiIsIjYwMHg%2BIix7fV1d.png?sha=9bfb66106019f517\",\"last_attempted_ts\":1566977727},{\"id\":\"5d662eb175335604850002a2\",\"name\":\"中学 社会 地理(基礎)\",\"number_of_topics\":31,\"teacher_name\":\"伊藤 賀一\",\"icon_url\":\"https://mediacdn.studysapuri.jp/media/W1siZiIsIjIwMTYvMDIvMjQvMTMvNTUvMzEvMWFiNGU3YWUtNmU1NS00MDVmLTlmMDYtZGJmM2M0ZjZiMjJjL2l0by5wbmciXSxbInAiLCJ0aHVtYiIsIjYwMHg%2BIix7fV1d.png?sha=70730b0ab1ece4c4\",\"last_attempted_ts\":1566977713},{\"id\":\"5d662eab03f65065c7002bba\",\"name\":\"中3 理科(基礎)\",\"teacher_name\":\"佐川 大三\",\"number_of_topics\":26,\"icon_url\":\"https://mediacdn.studysapuri.jp/media/W1siZiIsIjIwMTcvMDIvMDEvMDIvMTcvMDYvMmFkMjZlZGMtYmRjOC00MTYzLWJjODItNWNmYzk0ZTQwN2NjLyVFNCVCRCU5MCVFNSVCNyU5RCVFNSU4NSU4OCVFNyU5NCU5RiVFMyU4MiVBNCVFMyU4MyVBOSVFMyU4MiVCOSVFMyU4MyU4OC5wbmciXSxbInAiLCJ0aHVtYiIsIjYwMHg%2BIix7fV1d.png?sha=ee79b50d4497cdc4\",\"last_attempted_ts\":1566977707},{\"id\":\"5d662ea4f506f4742a011a4b\",\"name\":\"中3 国語(応用)\",\"teacher_name\":\"笹森 義通\",\"number_of_topics\":35,\"icon_url\":\"https://mediacdn.studysapuri.jp/media/W1siZiIsIjIwMTcvMDQvMTMvMDYvNTIvNTUvYjFhNmNhYzYtNzIxNy00YjI4LThlOGEtYzJlMWViMDRiNmRlL3Nhc2Ftb3JpLnBuZyJdLFsicCIsInRodW1iIiwiNjAweD4iLHt9XV0.png?sha=0293c8040f6be1d1\",\"last_attempted_ts\":1566977700},{\"id\":\"5d662ea2736b6c536b00ece4\",\"name\":\"中3 国語(三省堂 現代の国語)\",\"number_of_topics\":18,\"teacher_name\":\"今中 陽子\",\"icon_url\":\"https://mediacdn.studysapuri.jp/media/W1siZiIsIjIwMTcvMDIvMTUvMDEvMDAvMTkvNGJiOWY2MDQtZjJmOS00MDg4LWE4MzEtMjJkNTdhMDI5YzE0L2ltYW5ha2EucG5nIl0sWyJwIiwidGh1bWIiLCI2MDB4PiIse31dXQ.png?sha=0dafaba3a6aa5ed3\",\"last_attempted_ts\":1566977698},{\"id\":\"5d662e9bd84d8f7ca5001532\",\"name\":\"【定期テスト対策講座】中3 英語(教育出版 ONE WORLD)\",\"number_of_topics\":12,\"teacher_name\":\"竹内健\",\"icon_url\":\"https://mediacdn.studysapuri.jp/media/W1siZiIsIjIwMTcvMDQvMjUvMDMvNDAvMTUvNTJlZGUyY2MtMjkyNy00ZWRlLThkZjEtYTA3ZWY5Y2JjNTQwLyVFNyVBQiVCOSVFNSU4NiU4NSVFNSU4MSVBNSVFNSU4NSU4OCVFNyU5NCU5Ri5wbmciXSxbInAiLCJ0aHVtYiIsIjYwMHg%2BIix7fV1d.png?sha=027451af05983716\",\"last_attempted_ts\":1566977691}]"
        val result = homeViewModel.getCourseInfo(url, res)
        assertEquals(result, expected)
    }

    @Test
    suspend fun `res is null, and url for progress`() {
        val homeViewModel = HomeViewModel()
        val url = "https://native-team-code-test-api.herokuapp.com/api/5d662ee46fa3b05b18005223/usage"
        val res = ""
        val expected = "[{\"course_id\":\"5d662ee46fa3b05b18005223\",\"progress\":68}"
        val result = homeViewModel.getCourseInfo(url, res)
        assertEquals(result, expected)
    }

    @Test
    suspend fun `url nor res is null, and url for progress`() {
        val homeViewModel = HomeViewModel()
        val url = "https://native-team-code-test-api.herokuapp.com/api/5d662ee46fa3b05b18005223/usage"
        val res = "[{\"course_id\":\"\",\"progress\":0}"
        val expected = "[{\"course_id\":\"5d662ee46fa3b05b18005223\",\"progress\":68}"
        val result = homeViewModel.getCourseInfo(url, res)
        assertEquals(result, expected)
    }
}