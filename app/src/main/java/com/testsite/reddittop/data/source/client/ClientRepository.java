package com.testsite.reddittop.data.source.client;

import com.testsite.reddittop.utils.models.UIListing;
import com.testsite.reddittop.data.source.client.remote.model.OAuthToken;

/**
 * Created by paulf
 */
public interface ClientRepository {
    UIListing<OAuthToken> authenticate();
}
