let currentAjax = null;
let latestStationCode = null;
let tideChartInstance = null;
let userLocation = null;

function loadRegions() {
    $.get("/api/multtae/regions/with-station", function (regionMap) {
        console.log("🌍 받은 지역 데이터:", regionMap);
        
        const regions = Object.keys(regionMap).sort();
        console.log("🌍 정렬된 지역 목록:", regions);

        $("#regionButtons").empty();
        regions.forEach(region => {
            const hasStation = regionMap[region];
            console.log(`🌍 ${region}: 관측소 ${hasStation ? '있음' : '없음'}`);
            const btn = $(`<button type="button" class="btn btn-outline-dark region-btn me-2 mb-2" data-region="${region}">${region}</button>`);
            if (!hasStation) btn.prop("disabled", true);
            $("#regionButtons").append(btn);
        });

        // 사용 가능한 지역 찾기
        const availableRegions = regions.filter(r => regionMap[r]);
        console.log("🌍 사용 가능한 지역:", availableRegions);
        
        // 기본 지역 설정: 부산 → 첫 번째 사용 가능한 지역
        const defaultRegion = regionMap["부산"] ? "부산" : availableRegions[0];
        
        if (!defaultRegion) {
            console.error("❌ 기본 지역 설정 실패 - 사용할 수 있는 지역 없음");
            $("#regionButtons").append('<div class="alert alert-warning">관측소 데이터가 없습니다. 관리자에게 문의하세요.</div>');
            return;
        }

        console.log("🌍 선택된 기본 지역:", defaultRegion);
        $("#selectedRegion").text(defaultRegion);
        highlightActiveRegion(defaultRegion);
        loadStations(defaultRegion);
    }).fail(function(xhr, status, error) {
        console.error("❌ 지역 데이터 로드 실패:", error);
        $("#regionButtons").append('<div class="alert alert-danger">지역 데이터를 불러올 수 없습니다.</div>');
    });
}

function highlightActiveRegion(region) {
    $(".region-btn").removeClass("btn-dark active").addClass("btn-outline-dark");
    $(`.region-btn[data-region="${region}"]`).removeClass("btn-outline-dark").addClass("btn-dark active");
}

function loadStations(region) {
    if (!region) return;
    $("#selectedRegion").text(region);
    $("#stationSelect").empty().append(`<option disabled selected>불러오는 중...</option>`);

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

    if (currentAjax) currentAjax.abort();

    $("#loading").show();

    currentAjax = $.get("/api/multtae/today", { stationCode }, function (data) {
        if (stationCode !== latestStationCode) return;
        $("#loading").hide();
        displayStationData(data);
    }).fail(function (xhr, status) {
        if (status !== "abort") {
            alert("데이터 불러오기 실패");
            $("#loading").hide();
        }
    });

    loadWeekData(stationCode);
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
                </tr>
            `);
        });
    });
}

function formatTime(time) {
    return (time && time.length === 4) ? time.slice(0, 2) + ":" + time.slice(2) : "-";
}

// 위치 기반 관측소 찾기
function findNearestStation(latitude, longitude) {
    console.log("📍 위치 기반 관측소 찾기:", { latitude, longitude });
    
    return $.get("/api/multtae/nearest-station", { 
        latitude: latitude, 
        longitude: longitude 
    }).then(function(data) {
        console.log("📍 가장 가까운 관측소:", data);
        return data;
    }).fail(function(xhr, status, error) {
        console.error("❌ 가까운 관측소 찾기 실패:", error);
        throw error;
    });
}

// 위치 기반 물때 정보 조회
function loadLocationBasedData(latitude, longitude) {
    console.log("📍 위치 기반 물때 정보 조회:", { latitude, longitude });
    
    $("#loading").show();
    $("#loading").html('위치 기반 데이터 불러오는 중... <span class="spinner"></span>');
    
    $.get("/api/multtae/location", { 
        latitude: latitude, 
        longitude: longitude 
    }).then(function(data) {
        $("#loading").hide();
        displayStationData(data);
        
        // 위치 정보 표시
        $("#locationInfo").html(`
            <div class="alert alert-info">
                <i class="fas fa-map-marker-alt"></i> 
                현재 위치 기반 관측소: ${data.stationName} 
                (${Math.round(calculateDistance(latitude, longitude, data.latitude, data.longitude) * 100) / 100}km)
            </div>
        `);
        
    }).fail(function(xhr, status, error) {
        $("#loading").hide();
        console.error("❌ 위치 기반 데이터 로드 실패:", error);
        $("#locationInfo").html(`
            <div class="alert alert-warning">
                <i class="fas fa-exclamation-triangle"></i> 
                위치 기반 데이터를 불러올 수 없습니다. 수동으로 관측소를 선택해주세요.
            </div>
        `);
    });
}

// 위치 권한 요청 및 처리
function requestLocationPermission() {
    if (!navigator.geolocation) {
        console.warn("⚠️ 브라우저가 위치 정보를 지원하지 않습니다.");
        $("#locationInfo").html(`
            <div class="alert alert-warning">
                <i class="fas fa-exclamation-triangle"></i> 
                브라우저가 위치 정보를 지원하지 않습니다.
            </div>
        `);
        return;
    }

    console.log("📍 위치 권한 요청 중...");
    $("#locationInfo").html(`
        <div class="alert alert-info">
            <i class="fas fa-spinner fa-spin"></i> 
            위치 권한을 요청하고 있습니다...
        </div>
    `);

    navigator.geolocation.getCurrentPosition(
        function(position) {
            const latitude = position.coords.latitude;
            const longitude = position.coords.longitude;
            userLocation = { latitude, longitude };
            
            console.log("📍 위치 정보 획득:", { latitude, longitude });
            
            $("#locationInfo").html(`
                <div class="alert alert-success">
                    <i class="fas fa-check-circle"></i> 
                    위치 정보를 성공적으로 가져왔습니다.
                    <button type="button" class="btn btn-sm btn-outline-primary ms-2" onclick="loadLocationBasedData(${latitude}, ${longitude})">
                        <i class="fas fa-map-marker-alt"></i> 가까운 관측소 정보 보기
                    </button>
                </div>
            `);
            
            // 자동으로 가까운 관측소 정보 로드
            loadLocationBasedData(latitude, longitude);
        },
        function(error) {
            console.error("❌ 위치 정보 획득 실패:", error);
            let errorMessage = "위치 정보를 가져올 수 없습니다.";
            
            switch(error.code) {
                case error.PERMISSION_DENIED:
                    errorMessage = "위치 권한이 거부되었습니다. 브라우저 설정에서 위치 권한을 허용해주세요.";
                    break;
                case error.POSITION_UNAVAILABLE:
                    errorMessage = "위치 정보를 사용할 수 없습니다.";
                    break;
                case error.TIMEOUT:
                    errorMessage = "위치 정보 요청 시간이 초과되었습니다.";
                    break;
            }
            
            $("#locationInfo").html(`
                <div class="alert alert-warning">
                    <i class="fas fa-exclamation-triangle"></i> 
                    ${errorMessage}
                    <button type="button" class="btn btn-sm btn-outline-primary ms-2" onclick="requestLocationPermission()">
                        <i class="fas fa-redo"></i> 다시 시도
                    </button>
                </div>
            `);
        },
        {
            enableHighAccuracy: true,
            timeout: 10000,
            maximumAge: 60000
        }
    );
}

// 두 지점 간의 거리 계산 (Haversine 공식)
function calculateDistance(lat1, lon1, lat2, lon2) {
    const R = 6371; // 지구 반지름 (km)
    const dLat = (lat2 - lat1) * Math.PI / 180;
    const dLon = (lon2 - lon1) * Math.PI / 180;
    const a = Math.sin(dLat/2) * Math.sin(dLat/2) +
              Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
              Math.sin(dLon/2) * Math.sin(dLon/2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    return R * c;
}

// 관측소 데이터 표시 함수
function displayStationData(data) {
    $("#stationName").text(data.stationName);
    $("#date").text(data.date);
    $("#mulName").text(data.mulName);
    $("#lunarAge").text(data.lunarAge);
    $("#sunrise").text(data.sunrise);
    $("#sunset").text(data.sunset);
    $("#windSpeed").text(data.todayWindSpeed ?? "-");
    $("#windDir").text(data.todayWindDir ?? "-");
    
    // 파고 데이터 표시 개선
    const waveData = data.hourlyData[0]?.wave;
    if (waveData !== null && waveData !== undefined) {
        $("#wave").text(waveData + " m");
        $("#wave").removeClass("text-muted");
    } else {
        $("#wave").text("파고 데이터 없음");
        $("#wave").addClass("text-muted");
    }
    
    $("#airTemp").text(data.airTemp ?? "-");

    $("#tideItems").empty();
    data.tideItems.forEach(item => {
        $("#tideItems").append(`<li>${item.hl_code} ${item.tph_time} (${item.tph_level})</li>`);
    });

    $("#hourlyData").empty();
    data.hourlyData.forEach(h => {
        // 파고 데이터 표시 개선
        let waveDisplay = "-";
        if (h.wave !== null && h.wave !== undefined) {
            waveDisplay = h.wave + " m";
        } else {
            waveDisplay = "데이터 없음";
        }
        
        $("#hourlyData").append(`
            <tr>
                <td>${h.time}</td>
                <td>${h.wind_speed ?? "-"}</td>
                <td>${h.wind_dir ?? "-"}</td>
                <td class="${h.wave === null || h.wave === undefined ? 'text-muted' : ''}">${waveDisplay}</td>
                <td>${h.air_temp ?? "-"}</td>
                <td>${h.tide_level ?? "-"}</td>
            </tr>`);
    });

    // 조위 그래프 업데이트
    const tideLabels = data.hourlyData.map(h => h.time);
    const tideValues = data.hourlyData.map(h => h.tide_level);
    if (tideChartInstance) tideChartInstance.destroy();
    const ctx = document.getElementById("tideChart").getContext("2d");
    tideChartInstance = new Chart(ctx, {
        type: "line",
        data: {
            labels: tideLabels,
            datasets: [{
                label: "조위 (cm)",
                data: tideValues,
                borderColor: "#0d6efd",
                backgroundColor: "rgba(13,110,253,0.2)",
                tension: 0.3,
                fill: true,
                pointRadius: 3
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: false,
                    title: {
                        display: true,
                        text: "cm"
                    }
                }
            }
        }
    });
    
    // 파고 데이터 상태 정보 표시
    const hasWaveData = data.hourlyData.some(h => h.wave !== null && h.wave !== undefined);
    if (!hasWaveData) {
        $("#waveInfo").html(`
            <div class="alert alert-info mt-2">
                <i class="fas fa-info-circle"></i> 
                <strong>파고 정보 안내:</strong> 이 관측소는 내륙지방에 위치하여 파고 데이터를 제공하지 않습니다. 
                파고 정보가 필요한 경우 해안가 관측소를 선택해주세요.
            </div>
        `);
    } else {
        $("#waveInfo").empty();
    }
}

$(function () {
    loadRegions();

    $(document).on("click", ".region-btn:not(:disabled)", function () {
        const region = $(this).data("region");
        highlightActiveRegion(region);
        loadStations(region);
    });

    $("#stationSelect").change(function () {
        const code = $(this).val();
        loadStationData(code);
    });
});