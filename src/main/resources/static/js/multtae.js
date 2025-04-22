function loadRegions() {
    $.get("/api/multtae/regions", function(regions) {
        $("#regionButtons").empty();
        regions.forEach(region => {
            $("#regionButtons").append(`<button class="region-btn" data-region="${region}">${region}</button>`);
        });

        // 기본 지역 지정
        const defaultRegion = "경상북도";
        const targetRegion = regions.includes(defaultRegion) ? defaultRegion : regions[0];
        $("#selectedRegion").text(targetRegion);
        loadStations(targetRegion);
    });
}

function loadStations(region) {
    $("#selectedRegion").text(region);
    $("#stationSelect").empty();

    $.get("/api/multtae/stations", { region: region }, function(data) {
        data.forEach(station => {
            $("#stationSelect").append(`<option value="${station.stationCode}">${station.stationName}</option>`);
        });

        // 기본 관측소: 울릉도
        const defaultStationName = "울릉도";
        let selectedCode = data.find(s => s.stationName === defaultStationName)?.stationCode;

        if (!selectedCode && data.length > 0) {
            selectedCode = data[0].stationCode;
        }

        if (selectedCode) {
            $("#stationSelect").val(selectedCode).trigger("change");
        }
    });
}

$(function () {
    loadRegions();

    $(document).on("click", ".region-btn", function () {
        const region = $(this).data("region");
        loadStations(region);
    });

    $("#stationSelect").change(function () {
        const code = $(this).val();
        if (!code) return;

        $.get("/api/multtae/today", { stationCode: code }, function (data) {
            $("#date").text(data.date);
            $("#mulName").text(data.mulName);
            $("#lunarAge").text(data.lunarAge);
            $("#sunrise").text(data.sunrise);
            $("#sunset").text(data.sunset);
            $("#windSpeed").text(data.todayWindSpeed ?? "-");
            $("#windDir").text(data.todayWindDir ?? "-");
            $("#wave").text(data.hourlyData[0]?.wave ?? "-");
            $("#airTemp").text(data.hourlyData[0]?.air_temp ?? "-");

            // 조위 정보
            $("#tideItems").empty();
            data.tideItems.forEach(item => {
                $("#tideItems").append(`<li>${item.hl_code} ${item.tph_time} (${item.tph_level})</li>`);
            });

            // 시간대별 기상 정보
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
        });
    });
});