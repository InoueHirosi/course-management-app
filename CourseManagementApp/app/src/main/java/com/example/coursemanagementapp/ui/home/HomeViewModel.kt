package com.example.coursemanagementapp.ui.home

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


class HomeViewModel : ViewModel() {

    val info = MutableLiveData<JSONArray>()
    var courseInfo: LiveData<JSONArray> = info
    private val isProgress = MutableLiveData<Boolean>()
    var progressBar: LiveData<Boolean> = isProgress
    private val client = OkHttpClient()
    private val mainUrl = "https://native-team-code-test-api.herokuapp.com/"

    //0 ~ apiLimit-1
    var counter = 0

    //the number of loop while api connection failed
    val apiLimit: Int = 5

    /**
     * control view model
     */
    fun doWork() {

        //the return of api connection
        var result: JSONArray

        //course_id
        var id: String

        //progress %
        var progress: String

        //expected response format api(api/courses)
        val courseInfoRes =
            "[{\"id\":\"\",\"name\":\"\",\"icon_url\":\"\",\"number_of_topics\":\"0\",\"teacher_name\":\"\",\"last_attempted_ts\":\"0\"}]"

        //expected response format api(/api/{course_id}/usage)
        val progressRes = "{\"course_id\":\"\",\"progress\":0}"
        viewModelScope.launch {
            do {
                counter++

                //post "api/courses" by okhttp3
                result = withContext(Dispatchers.IO) {
                    JSONArray(getCourseInfo(mainUrl + "api/courses", courseInfoRes))
                }

                //result = JSONArray("[{\"id\":\"5d662ee46fa3b05b18005223\",\"name\":\"高2 化学＜有機編＞\",\"icon_url\":\"https://mediacdn.studysapuri.jp/media/W1siZiIsIjIwMTkvMDQvMDgvMDkvMTUvNTQvMDBjYmFhMWYtMGEzYi00MDFiLWEzZjItOTM2YjAzOTE5ZTIzL3Nha2F0YS5wbmciXSxbInAiLCJ0aHVtYiIsIjYwMHg%2BIix7fV1d.png?sha=53cf02d67d8cd49e\",\"number_of_topics\":20,\"teacher_name\":\"坂田 薫\",\"last_attempted_ts\":1566977764},{\"id\":\"5d662ed608fc5b5e770033d6\",\"name\":\"高3 ハイ&スタンダードレベル英語＜英単語補充編＞\",\"icon_url\":\"https://mediacdn.studysapuri.jp/media/W1siZiIsIjIwMTYvMDIvMjQvMTMvMzkvNDYvYmFjMjZlODMtOWY2Yy00YmJjLWFhMGUtZjg5OWYwNTViYzNkL3Nla2kucG5nIl0sWyJwIiwidGh1bWIiLCI2MDB4PiIse31dXQ.png?sha=d4513b70bc0c565d\",\"number_of_topics\":3,\"teacher_name\":\"関 正生\",\"last_attempted_ts\":1566977750},{\"id\":\"5d662ed28946d334ee001eac\",\"name\":\"高3 スタンダードレベル英語＜文法編＞\",\"teacher_name\":\"関 正生\",\"number_of_topics\":24,\"icon_url\":\"https://mediacdn.studysapuri.jp/media/W1siZiIsIjIwMTYvMDIvMjQvMTQvMTMvMTgvYzA1MGNhM2YtYTU2OC00YmU1LTlmOTEtOGI2ZjEwNzA2Y2NjL3Nla2kucG5nIl0sWyJwIiwidGh1bWIiLCI2MDB4PiIse31dXQ.png?sha=5ae972bf50e3c405\",\"last_attempted_ts\":1566977746},{\"id\":\"5d662ebf02303b2a16000931\",\"name\":\"【高校受験】国語 岩手県 公立高校受験対策\",\"teacher_name\":\"笹森 義通\",\"number_of_topics\":28,\"icon_url\":\"https://mediacdn.studysapuri.jp/media/W1siZiIsIjIwMTcvMDcvMjUvMDEvMjAvNDcvN2M5MTA5YmUtOTc1Yi00YzMxLWExMzAtYmVmZWUwNTVhNGQ4LyVFNyVBQyVCOSVFNiVBMyVBRSVFNSU4NSU4OCVFNyU5NCU5Ri5wbmciXSxbInAiLCJ0aHVtYiIsIjYwMHg%2BIix7fV1d.png?sha=9bfb66106019f517\",\"last_attempted_ts\":1566977727},{\"id\":\"5d662eb175335604850002a2\",\"name\":\"中学 社会 地理(基礎)\",\"number_of_topics\":31,\"teacher_name\":\"伊藤 賀一\",\"icon_url\":\"https://mediacdn.studysapuri.jp/media/W1siZiIsIjIwMTYvMDIvMjQvMTMvNTUvMzEvMWFiNGU3YWUtNmU1NS00MDVmLTlmMDYtZGJmM2M0ZjZiMjJjL2l0by5wbmciXSxbInAiLCJ0aHVtYiIsIjYwMHg%2BIix7fV1d.png?sha=70730b0ab1ece4c4\",\"last_attempted_ts\":1566977713},{\"id\":\"5d662eab03f65065c7002bba\",\"name\":\"中3 理科(基礎)\",\"teacher_name\":\"佐川 大三\",\"number_of_topics\":26,\"icon_url\":\"https://mediacdn.studysapuri.jp/media/W1siZiIsIjIwMTcvMDIvMDEvMDIvMTcvMDYvMmFkMjZlZGMtYmRjOC00MTYzLWJjODItNWNmYzk0ZTQwN2NjLyVFNCVCRCU5MCVFNSVCNyU5RCVFNSU4NSU4OCVFNyU5NCU5RiVFMyU4MiVBNCVFMyU4MyVBOSVFMyU4MiVCOSVFMyU4MyU4OC5wbmciXSxbInAiLCJ0aHVtYiIsIjYwMHg%2BIix7fV1d.png?sha=ee79b50d4497cdc4\",\"last_attempted_ts\":1566977707},{\"id\":\"5d662ea4f506f4742a011a4b\",\"name\":\"中3 国語(応用)\",\"teacher_name\":\"笹森 義通\",\"number_of_topics\":35,\"icon_url\":\"https://mediacdn.studysapuri.jp/media/W1siZiIsIjIwMTcvMDQvMTMvMDYvNTIvNTUvYjFhNmNhYzYtNzIxNy00YjI4LThlOGEtYzJlMWViMDRiNmRlL3Nhc2Ftb3JpLnBuZyJdLFsicCIsInRodW1iIiwiNjAweD4iLHt9XV0.png?sha=0293c8040f6be1d1\",\"last_attempted_ts\":1566977700},{\"id\":\"5d662ea2736b6c536b00ece4\",\"name\":\"中3 国語(三省堂 現代の国語)\",\"number_of_topics\":18,\"teacher_name\":\"今中 陽子\",\"icon_url\":\"https://mediacdn.studysapuri.jp/media/W1siZiIsIjIwMTcvMDIvMTUvMDEvMDAvMTkvNGJiOWY2MDQtZjJmOS00MDg4LWE4MzEtMjJkNTdhMDI5YzE0L2ltYW5ha2EucG5nIl0sWyJwIiwidGh1bWIiLCI2MDB4PiIse31dXQ.png?sha=0dafaba3a6aa5ed3\",\"last_attempted_ts\":1566977698},{\"id\":\"5d662e9bd84d8f7ca5001532\",\"name\":\"【定期テスト対策講座】中3 英語(教育出版 ONE WORLD)\",\"number_of_topics\":12,\"teacher_name\":\"竹内健\",\"icon_url\":\"https://mediacdn.studysapuri.jp/media/W1siZiIsIjIwMTcvMDQvMjUvMDMvNDAvMTUvNTJlZGUyY2MtMjkyNy00ZWRlLThkZjEtYTA3ZWY5Y2JjNTQwLyVFNyVBQiVCOSVFNSU4NiU4NSVFNSU4MSVBNSVFNSU4NSU4OCVFNyU5NCU5Ri5wbmciXSxbInAiLCJ0aHVtYiIsIjYwMHg%2BIix7fV1d.png?sha=027451af05983716\",\"last_attempted_ts\":1566977691}]")
                id = result.getJSONObject(0).getString("id")
                if (id == "") {
                    continue
                }

                //post "api/{course_id}/usage" by okhttp3
                result = getCourseProgress(result, progressRes)

                progress = result.getJSONObject(0).getString("progress")
                //assume this case isn't used(at least progress == 0)
                if (progress == "") {
                    continue
                }
            } while (result.getJSONObject(0).isNull("progress") && counter < apiLimit)
            info.value = result

            //observer in HomeFragment observe this turns false
            isProgress.value = false
        }
    }

    /**
     * get course data
     * @param url Api URL
     * @param res expected response format(empty)
     * @return http response
     */
    suspend fun getCourseInfo(url: String, res: String): String {
        var httpResult = res
        val request = Request.Builder().url(url).build()
        var body: ResponseBody? = null
        try {
            val response = withContext(Dispatchers.IO) {
                client.newCall(request).execute()
            }
            body = response.body
            if (response.code == 200) {
                httpResult = response.body!!.string()
            }
            //if code != 200, return empty response(param)

        } catch (e: UnknownHostException) {
            e.printStackTrace()
        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
        } catch (e: NetworkErrorException) {
            e.printStackTrace()
        } catch (e: ConnectException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: AccessDeniedException) {
            e.printStackTrace()
        } finally {
            body?.close()
        }
        return httpResult
    }

    /**
     * add progress data to course data
     * @param result course data without progress
     * @param res expected response format(empty)
     * @return course data with progress(int %)
     */
    private suspend fun getCourseProgress(result: JSONArray, res: String): JSONArray {
        for (i in 0 until result.length()) {
            val id = result.getJSONObject(i).getString("id")
            val progress = withContext(Dispatchers.IO) {
                JSONObject(getCourseInfo(mainUrl + "api/" + id + "/usage", res))
            }
            val progressValue = if (progress.getString("progress")
                    .isNullOrBlank()
            ) 0 else progress.getString("progress")
            result.getJSONObject(i).put("progress", progressValue)
        }
        return result
    }

}