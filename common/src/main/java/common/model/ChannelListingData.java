package common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ChannelListingData extends ChannelListingForm{
    private Long id;
    private Long channelId;
}
