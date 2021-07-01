package channel.controller;

import channel.dto.ChannelDto;
import channel.util.ApiException;
import common.model.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api
public class ChannelController extends ExceptionHandler{
    @Autowired
    private ChannelDto channelDto;


    @ApiOperation(value = "Adds a channel")
    @RequestMapping(path = "/api/channel", method = RequestMethod.POST)
    public void add(@RequestBody ChannelForm channelForm) throws ApiException {
        channelDto.add(channelForm);
    }

    @ApiOperation(value = "Gets all channels")
    @RequestMapping(path = "/api/channel", method = RequestMethod.GET)
    public List<ChannelData> get() throws ApiException {
        return channelDto.get();
    }

    @ApiOperation(value = "Get channel")
    @RequestMapping(path = "/api/channel/{name}", method = RequestMethod.GET)
    public ChannelData get(@PathVariable String name) throws ApiException {
        return channelDto.getByName(name);
    }

    @ApiOperation(value = "Add a channel listing")
    @RequestMapping(path = "/api/channel_listing/{clientName}/{channelName}", method = RequestMethod.POST)
    public void add(@PathVariable String clientName, @PathVariable String channelName, @RequestBody ChannelListingForm channelListingForm) throws ApiException {
        channelDto.add(clientName,channelName,channelListingForm);
    }

    @ApiOperation(value = "Add list of channel listing")
    @RequestMapping(path = "/api/channel_listing/list/{clientName}/{channelName}", method = RequestMethod.POST)
    public void add(@PathVariable String clientName, @PathVariable String channelName, @RequestBody List<ChannelListingForm> channelListingFormList) throws ApiException {
        channelDto.add(clientName,channelName,channelListingFormList);
    }

    @ApiOperation(value = "Gets all channel listings")
    @RequestMapping(path = "/api/channel_listing", method = RequestMethod.GET)
    public List<ChannelListingData> getAllChannelListing() throws ApiException {
        return channelDto.getAllChannelListing();
    }

    @ApiOperation(value = "Gets all clients")
    @RequestMapping(path = "/api/user", method = RequestMethod.GET)
    public List<UserData> getAllUser() throws ApiException {
        return channelDto.getAllUser();
    }
}
