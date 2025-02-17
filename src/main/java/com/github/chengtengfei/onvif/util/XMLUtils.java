package com.github.chengtengfei.onvif.util;

import com.github.chengtengfei.onvif.model.ProfileInfo;
import com.github.chengtengfei.onvif.model.VideoInfo;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class XMLUtils {

    public static String parseOnvifAddress(String xmlContent) throws Exception {
        String address = "";
        Document document = DocumentHelper.parseText(xmlContent.trim());
        Element onvifSoapRoot = document.getRootElement();
        address = onvifSoapRoot.element("Body").element("ProbeMatches")
                .element("ProbeMatch").element("XAddrs").getTextTrim();
        return address;
    }

    public static String parseMediaUrl(String xmlContent) throws Exception {
        String mediaAddress = "";
        Document document = DocumentHelper.parseText(xmlContent);
        Element capabilitiesSoapRoot = document.getRootElement();
        mediaAddress = capabilitiesSoapRoot.element("Body").element("GetCapabilitiesResponse")
                .element("Capabilities").element("Media").element("XAddr").getTextTrim();
        return mediaAddress;
    }

    public static List<ProfileInfo> parseProfiles(String xmlContent) throws Exception {
        List<ProfileInfo> profiles = new ArrayList<>();
        Document document = DocumentHelper.parseText(xmlContent);
        Element root = document.getRootElement();
        List<Element> profileElements = root.element("Body").element("GetProfilesResponse").elements("Profiles");
        for (Element element : profileElements) {
            ProfileInfo profileInfo = new ProfileInfo();
            profileInfo.setName(element.element("Name").getTextTrim());
            profileInfo.setToken(element.attributeValue("token"));
            // 获取video信息
            VideoInfo videoInfo = new VideoInfo();
            if (null != element.element("VideoEncoderConfiguration")) {
                videoInfo.setVideoEncoding(element.element("VideoEncoderConfiguration").element("Encoding").getTextTrim());
                videoInfo.setVideoWidth(Integer.valueOf(element.element("VideoEncoderConfiguration").element("Resolution").element("Width").getTextTrim()));
                videoInfo.setVideoHeight(Integer.valueOf(element.element("VideoEncoderConfiguration").element("Resolution").element("Height").getTextTrim()));
                if (null != element.element("VideoEncoderConfiguration").element("RateControl")) {
                    videoInfo.setFrameRateLimit(Integer.valueOf(element.element("VideoEncoderConfiguration").element("RateControl").element("FrameRateLimit").getTextTrim()));
                    videoInfo.setBitrateLimit(Integer.valueOf(element.element("VideoEncoderConfiguration").element("RateControl").element("BitrateLimit").getTextTrim()));
                }
            }
            profileInfo.setVideoInfo(videoInfo);
            profiles.add(profileInfo);
        }
        return profiles;
    }

    public static String parseStreamUri(String xmlContent) throws Exception {
        String streamUri = "";
        Document document = DocumentHelper.parseText(xmlContent);
        Element root = document.getRootElement();
        streamUri = root.element("Body").element("GetStreamUriResponse").element("MediaUri").element("Uri").getTextTrim();
        streamUri = URLDecoder.decode(streamUri, StandardCharsets.UTF_8.name());
        return streamUri;
    }

    public static String parseSnapshotUri(String xmlContent) throws Exception {
        String snapshotUrl = "";
        Document document = DocumentHelper.parseText(xmlContent);
        Element root = document.getRootElement();
        snapshotUrl = root.element("Body").element("GetSnapshotUriResponse").element("MediaUri").element("Uri").getTextTrim();
        snapshotUrl = URLDecoder.decode(snapshotUrl, StandardCharsets.UTF_8.name());
        return snapshotUrl;
    }
}
