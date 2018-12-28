var stompClient = null;

function connect() {
    console.log("Connecting to websocket");
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/state', function(frame){
            handleStateUpdate(JSON.parse(frame.body));
        });
    });
    stompClient.debug = null
}

function handleStateUpdate(state) {
    var positionSlider = $("#position" + state.servoId);
    var positionText = $("#textposition" + state.servoId);

    var temp = $("#temp" + state.servoId);
    var volt = $("#volt" + state.servoId);
    if(state.temperature > 0) {
        temp.val(state.temperature);
    }
    if(state.voltage > 0) {
        volt.val(state.voltage);
    }

    if(state.position > 0 && positionSlider.length) {
        // console.log("Received state update: " + state.position + " for servo " + state.servoId);

        if(positionSlider[0].getAttribute("changing") !== "true") {
            var position = positionSlider.slider('getValue');
            if(position !== state.position) {
                positionSlider.slider('setValue', state.position);


            }

            var targetPosition = positionSlider[0].getAttribute("targetPosition");
            if(Math.abs(targetPosition - state.position) < 10) {
                positionSlider.slider('setValue', state.position);
                // this.setAttribute("changing", "false");

                if(positionText.attr("changing") !== "true") {
                    positionText.val(state.position);
                }
            }
        }
    }
}

function loadHandlers() {
    var positionSliders = $("input[slider=position]");
    // positionSliders.each(function(index){
    //     $(this).slider();
    //     $(this).on("slide", handleSlideEvent);
    // });
    positionSliders.each(function (index) {
        $(this).slider();
        $(this).on("slideStart", handleSlideStart);
    });
    positionSliders.each(function (index) {
        $(this).slider();
        $(this).on("slideStop", handleSlideStop);
    });

    var inputPositions = $(".positionInput");
    inputPositions.each(function(index) {
        $(this).blur(function (e) {
            this.setAttribute('changing', "false");
        });
    });
    inputPositions.each(function(index) {
        $(this).focus(function (e) {
            this.setAttribute('changing', "true");
        });
    });

    $("button.torgueEnable").click(function (e) {
        e.preventDefault();

        var servoId = this.getAttribute('servoId');
        $.ajax({
            url: "/servos/enable/" + servoId + "/torgue",
            type: "POST",
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                console.log("Enable torgue for servo successfully");
            }
        });

    });

    $("button.torgueDisable").click(function (e) {
        e.preventDefault();

        var servoId = this.getAttribute('servoId');
        $.ajax({
            url: "/servos/disable/" + servoId + "/torgue",
            type: "POST",
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                console.log("Disable torgue for servo successfully");
            }
        });
    });

    $("button.setMaxPosition").click(function (e) {
        e.preventDefault();

        var servoId = this.getAttribute('servoId');
        setAngleLimits(servoId);
    });

    $("button.setMinPosition").click(function (e) {
        e.preventDefault();

        var servoId = this.getAttribute('servoId');
        setAngleLimits(servoId);
    });

    function setAngleLimits(servoId) {
        var minAngle = $("#cw" + servoId).val();
        var maxAngle = $("#ccw" + servoId).val();

        $.ajax({url: "/servos/set/" + servoId + "/angle/" + minAngle + "/" + maxAngle, type: "POST", contentType: "application/json; charset=utf-8", success: function(data) {
            console.log("Set servo: " + servoId + " Angle limits to min: " + minAngle + " and max: " + maxAngle + " successfully");
        }});
    }

    $("button.setOperatingMode").click(function (e) {
        e.preventDefault();

        var servoId = this.getAttribute('servoId');
        var mode = $("#opMode" + servoId).val();

        console.log("Setting operating mode for servo %s to %s", servoId, mode)

        $.ajax({url: "/servos/set/" + servoId + "/mode/" + mode, type: "POST", contentType: "application/json; charset=utf-8", success: function(data) {
                console.log("Set servo: " + servoId + " Operating mode to: " + mode + " successfully");
            }});
    });

    $("button.setSpeed").click(function (e) {
        e.preventDefault();

        var servoId = this.getAttribute('servoId');
        var speed = $("#textspeed" + servoId).val();

        setServoProperty(servoId, "speed", speed);
    });


    $("button.setTorgue").click(function (e) {
        e.preventDefault();

        var servoId = this.getAttribute('servoId');
        var torgue = $("#texttorgue" + servoId).val();

        setServoProperty(servoId, "torgue", torgue);
    });

    $("button.setPosition").click(function (e) {
        e.preventDefault();

        var servoId = this.getAttribute('servoId');
        var position = $("#textposition" + servoId).val();

        setServoProperty(servoId, "position", position);
    });

    $("button.enableAllTorgue").click(function (e) {
        e.preventDefault();
        $.ajax({
            url: "/servos/enable/torgue", type: "POST", contentType: "application/json; charset=utf-8", success: function (data) {
                console.log("Enabled torgue for servos successfully");
            }
        });
    });

    $("button.disableAllTorgue").click(function (e) {
        e.preventDefault();
        $.ajax({
            url: "/servos/disable/torgue", type: "POST", contentType: "application/json; charset=utf-8", success: function (data) {
                console.log("Disabled torgue for servos successfully");
            }
        });
    });

    $("button.executeMotion").click(function (e) {
        e.preventDefault();

        var motionId = $("#motions").val();

        $.ajax({
            url: "/motions/run/" + motionId, type: "POST", contentType: "application/json; charset=utf-8", success: function (data) {
                console.log("Motion execution: " + motionId + " was triggered");
            }
        });
    });

    $("button.loadMotion").click(function (e) {
        e.preventDefault();

        var motionId = $("#motions").val();
        if (!isEmpty(motionId)) {
            $.ajax({
                url: "/motions/load/" + motionId, type: "GET", contentType: "application/json; charset=utf-8", success: function (data) {
                    console.log("Motion: " + motionId + " was loaded");
                    var kf = $("#keyframes");
                    kf.empty();
                    kf.attr("motionId", motionId);
                    $("#kfMotionName").text(motionId);

                    $.each(data.keyFrames, function (i, frame) {
                        addKeyFrame(frame);
                    })
                }
            });
        }
    });

    $("button.takeKeyFrame").click(function (e) {
        e.preventDefault();
        $.ajax({
            url: "/motions/keyframe", type: "POST", contentType: "application/json; charset=utf-8", success: function (data) {
                addKeyFrame(data);
            }
        });
    });

    $("button.setKeyFrame").click(function (e) {
        e.preventDefault();

        var json = $("#keyframes").find(":selected").val();
        console.log("Keyframe selected: " + json);

        $.ajax({
            url: "/motions/run/keyframe",
            type: "POST",
            data: json,
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                console.log("KeyFrame succesfully set");
            }
        });
    });

    $("button.runKeyFrames").click(function (e) {
        e.preventDefault();

        var data = getMotionJson();
        var postData = JSON.stringify(data);
        console.log("Posting data: " + postData);

        $.ajax({
            url: "/motions/run/keyframes",
            type: "POST",
            data: postData,
            contentType: "application/json; charset=utf-8",
            success: function (data) {

            }
        });
    });

    $("button.stopMotion").click(function (e) {
        e.preventDefault();

        $.ajax({
            url: "/motions/stop", type: "POST", contentType: "application/json; charset=utf-8", success: function (data) {
                console.log("Motion execution stopped");
            }
        });
    });

    $("#keyframes").on('change', function () {
        var selectedFrame = $("#keyframes").find('option:selected');

        var t = selectedFrame.attr("time");
        var name = selectedFrame.text().trim();

        $("#frameName").val(name);
        $("#frameTime").val(t);
    });

    $("button.frameSave").click(function (e) {
        e.preventDefault();

        var fName = $("#frameName").val();
        var fTime = $("#frameTime").val();

        var selectedFrame = $("#keyframes").find('option:selected');
        var jsonText = selectedFrame.val();
        var json = JSON.parse(jsonText);
        json.keyFrameId = fName;
        json.timeInMs = fTime;
        selectedFrame.attr("value", JSON.stringify(json));
        selectedFrame.attr("time", fTime);
        selectedFrame.text(fName);
    });

    $("button.saveKeyFrames").click(function (e) {
        e.preventDefault();

        if(isEmpty($("#keyframes").attr("motionId"))) {
            $('#saveMotionModal').modal();
        } else {
            saveMotion();
        }
    });

    $("#saveMotionBtn").click(function (e) {
        var motionId = $("#motionName").val();
        var kf = $("#keyframes");
        kf.attr("motionId", motionId);
        $("#kfMotionName").text(motionId);

        saveMotion();
    });

    $("button.frameDelete").click(function (e) {
        e.preventDefault();

        var selectedFrame = $("#keyframes").find('option:selected');
        selectedFrame.remove();
    });

    $("button.getJson").click(function (e) {
        e.preventDefault();

        var json = getMotionJson();
        $("#motionData").val(JSON.stringify(json, null, 4));
        $("#motionJson").modal();
    });

    $("button.clearEditor").click(function (e) {
        e.preventDefault();

        var kf = $("#keyframes");
        kf.empty();
        kf.attr("motionId", "");

        $("#frameName").val("");
        $("#frameTime").val("");
        $("#kfMotionName").text("");
    });
}

function saveMotion() {
    var data = getMotionJson();
    var postData = JSON.stringify(data);
    console.log("Posting data: " + postData);

    $.ajax({
        url: "/motions/store",
        type: "POST",
        data: postData,
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            console.log("Motion saved");
        }
    });
}

function getMotionJson() {
    var kf = $("#keyframes");
    var keyFrames = [];
    kf.find("option").each(function () {
        console.log("Keyframe: " + $(this).val());

        var json = $(this).val();
        var keyframe = JSON.parse(json);
        keyFrames.push(keyframe);
    });
    var motionId = kf.attr("motionId");

    return {
        "name": motionId,
        "id": motionId,
        "keyFrames": keyFrames,
        "nextMotion": "0",
        "exitMotion": "0"
    };
}

function addKeyFrame(keyFrameData) {
    var keyFrameList = $('#keyframes');
    var json = JSON.stringify(keyFrameData);
    var keyFrameId = keyFrameData.keyFrameId;

    keyFrameList.append($("<option></option>")
        .attr("value", json)
        .attr("time", keyFrameData.timeInMs)
        .text(keyFrameId));
}

// function handleSlideEvent(slideEvt) {
//     var val = slideEvt.value;
//     var servoId = this.getAttribute('servoId');
//
//     // console.log("Slide event: " + val);
// }

function handleSlideStart(slideEvt) {
    // var val = slideEvt.value;
    var servoId = this.getAttribute('servoId');
    this.setAttribute("changing", "true");
}

function handleSlideStop(slideEvt) {
    var val = slideEvt.value;
    var servoId = this.getAttribute('servoId');
    this.setAttribute('targetPosition', val);
    this.setAttribute("changing", "false");

    var speed = $("#textspeed" + servoId).val();

    setServoProperty(servoId, "speed", speed);
    setServoProperty(servoId, "position", val);
}

function setServoProperty(servoId, property, value) {
    $.ajax({url: "/servos/set/" + servoId + "/" + property + "/" + value, type: "POST", contentType: "application/json; charset=utf-8", success: function(data) {
        console.log("Set servo: " + servoId + " " + property + " to value: " + value + " successfully");
    }});
}

$(document).ready(function() {
    loadHandlers();
    connect();
});

function isEmpty(str) {
    return (!str || 0 === str.length);
}
