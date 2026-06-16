package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelPrivateCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelPublicCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    @RequestMapping(value = {"/createPublicChannel"}, method = RequestMethod.POST)
    public String createPublicChannel(@RequestBody ChannelPublicCreateRequest request) {
        channelService.createPublicChannel(request);

        return "public channel created";
    }

    @RequestMapping(value = {"/createPrivateChannel"}, method = RequestMethod.POST)
    public String createPrivateChannel(@RequestBody ChannelPrivateCreateRequest request) {
        channelService.createPrivateChannel(request);

        return "private channel created";
    }

    @RequestMapping(value = {"/updateChannel"}, method = RequestMethod.PUT)
    public String updateChannel(@RequestBody ChannelUpdateRequest request) {
        channelService.updateChannel(request);

        return "channel updated";
    }

    @RequestMapping(value = {"/deleteChannel"}, method = RequestMethod.DELETE)
    public String deleteChannel(@RequestParam("id") UUID channelId) {
        channelService.deleteChannel(channelId);

        return "channel deleted";
    }

    public void findChannelByUserId() {

    }

}
