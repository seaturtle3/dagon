let currentAjax = null;
let latestStationCode = null;

function loadRegions() {
    $.get("/api/multtae/regions/with-station", function (regionMap) {
        const regions = Object.keys(regionMap).sort();

        $("#regionButtons").empty();
        regions.forEach(region => {
            const hasStation = regionMap[region];
            const btn = $(`<button type="button" class="btn btn-outline-dark region-btn me-2 mb-2" data-region="${region}">${region}</button>`);
            if (!hasStation) btn.prop("disabled", true);
            $("#regionButtons").append(btn);
        });

        const defaultRegion = regionMap["부산"] ? "부산" : regions.find(r => regionMap[r]);
        if (!defaultRegion) {
            console.error("❌ 기본 지역 설정 실패 - 사용할 수 있는 지역 없음");
            return;
        }

        $("#selectedRegion").text(defaultRegion);
        highlightActiveRegion(defaultRegion);
        loadStations(defaultRegion);
    });
}

function highlightActiveRegion(region) {
    $(".region-btn").removeClass("active");
    $(`.region-btn[data-region="${region}"]`).addClass("active");
}

function loadStations(region) {
    if (!region) return;

    $("#selectedRegion").text(region);
    $("#stationSelect").empty();
    $("#stationSelect").append(`<option disabled selected>불러오는 중...</option>`);

    $.get("/api/multtae/stations", { region }, function (data) {
        $("#stationSelect").empty();
        data.forEach(station => {
            $("#stationSelect").append(`<option value="${station.stationCode}">${station.stationName}</option>`);
        });

        const defaultStation = data.find(s => s.stationName === "가덕도")?.stationCode || data[0]?.stationCode;
        if (defaultStation) {
            $("#stationSelect").val(defaultStation).trigger("change");
        }
    });
}

function loadStationData(stationCode) {
    if (!stationCode) return;

    latestStationCode = stationCode;

    if (currentAjax) {
        currentAjax.abort(); // ✅ 이전 요청 중단
    }

    $("#loading").show();

    currentAjax = $.get("/api/multtae/today", { stationCode }, function (data) {
        if (stationCode !== latestStationCode) return;

        $("#loading").hide();

        $("#date").text(data.date);
        $("#mulName").text(data.mulName);
        $("#lunarAge").text(data.lunarAge);
        $("#sunrise").text(data.sunrise);
        $("#sunset").text(data.sunset);
        $("#windSpeed").text(data.todayWindSpeed ?? "-");
        $("#windDir").text(data.todayWindDir ?? "-");
        $("#wave").text(data.hourlyData[0]?.wave ?? "-");
        $("#airTemp").text(data.hourlyData[0]?.air_temp ?? "-");

        $("#tideItems").empty();
        data.tideItems.forEach(item => {
            $("#tideItems").append(`<li>${item.hl_code} ${item.tph_time} (${item.tph_level})</li>`);
        });

        $("#hourlyData").empty();
        data.hourlyData.forEach(h => {
            $("#hourlyData").append(`
                <tr>
                    <td>${h.time}</td>
                    <td>${h.wind_speed ?? "-"}</td>
                    <td>${h.wind_dir ?? "-"}</td>
                    <td>${h.wave ?? "-"}</td>
                    <td>${h.air_temp ?? "-"}</td>
                    <td>${h.tide_level ?? "-"}</td>
                </tr>`);
        });
    }).fail(function (xhr, status) {
        if (status !== "abort") {
            alert("데이터 불러오기 실패");
            $("#loading").hide();
        }
    });

    loadWeekData(stationCode); // ✅ 주간 정보 로딩도 함께
}

function loadWeekData(stationCode) {
    $.get("/api/multtae/week", { stationCode }, function (weekData) {
        $("#weeklyData").empty();
        weekData.forEach(d => {
            $("#weeklyData").append(`
                <tr>
                    <td>${d.date}</td>
                    <td>${d.mulName}</td>
                    <td>${d.sunrise}</td>
                    <td>${d.sunset}</td>
                    <td>${d.windSpeed ?? "예보 예정"}</td>
                    <td>${d.windDir ?? "예보 예정"}</td>
                </tr>
            `);
        });
    });
}

$(function () {
    loadRegions();

    $(document).on("click", ".region-btn", function () {
        const region = $(this).data("region");
        highlightActiveRegion(region);
        loadStations(region);
    });

    $("#stationSelect").change(function () {
        const code = $(this).val();
        loadStationData(code);
    });
});