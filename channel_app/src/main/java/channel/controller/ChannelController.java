package channel.controller;

import channel.dto.ChannelDto;
import channel.util.ApiException;
import common.model.ChannelForm;
import common.model.ChannelListingForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api
public class ChannelController {
    @Autowired
    private ChannelDto channelDto;


    @ApiOperation(value = "Adds a channel")
    @RequestMapping(path = "/api/channel", method = RequestMethod.POST)
    public void add(@RequestBody ChannelForm channelForm) throws ApiException {
        System.out.println("channel entered");
        channelDto.add(channelForm);
    }

    //todo get channels
    @ApiOperation(value = "Add a channel listing")
    @RequestMapping(path = "/api/channel_listing", method = RequestMethod.POST)
    public void add(@RequestParam String clientName, @RequestParam String channelName, @RequestBody ChannelListingForm channelListingForm) throws ApiException {
        //channelDto.add(clientName,channelName,channelListingForm);
    }

    @ApiOperation(value = "Add list of channel listing")
    @RequestMapping(path = "/api/channel_listing/list", method = RequestMethod.POST)
    public void add(@RequestParam String clientName, @RequestParam String channelName, @RequestBody List<ChannelListingForm> channelListingFormList) throws ApiException {
        //channelDto.add(clientName,channelName,channelListingFormList);
    }
}
