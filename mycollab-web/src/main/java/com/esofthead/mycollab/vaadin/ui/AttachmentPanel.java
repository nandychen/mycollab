/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.vaadin.ui;

import com.esofthead.mycollab.core.utils.ImageUtil;
import com.esofthead.mycollab.core.utils.MimeTypesUtil;
import com.esofthead.mycollab.module.ecm.domain.Content;
import com.esofthead.mycollab.module.ecm.service.ResourceService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.easyuploads.MultiFileUploadExt;
import org.vaadin.maddon.layouts.MHorizontalLayout;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class AttachmentPanel extends VerticalLayout implements
        AttachmentUploadComponent {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(AttachmentPanel.class);
    private Map<String, File> fileStores;

    private MultiFileUploadExt multiFileUpload;
    private ResourceService resourceService;

    public AttachmentPanel() {
        resourceService = ApplicationContextUtil
                .getSpringBean(ResourceService.class);
        this.setSpacing(true);
    }

    @Override
    public void registerMultiUpload(MultiFileUploadExt fileUpload) {
        multiFileUpload = fileUpload;
    }

    private void displayFileName(final String fileName) {
        final MHorizontalLayout fileAttachmentLayout = new MHorizontalLayout();
        Button removeBtn = new Button(null, new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                File file = fileStores.get(fileName);
                if (file != null) {
                    file.delete();
                }
                fileStores.remove(fileName);
                AttachmentPanel.this.removeComponent(fileAttachmentLayout);
                if (multiFileUpload != null) {
                    multiFileUpload.removeAndReInitMultiUpload();
                }
            }
        });
        removeBtn.setIcon(FontAwesome.TRASH_O);
        removeBtn.setStyleName(UIConstants.BUTTON_ICON_ONLY);

        Label fileIcon = new Label(getFileIconResource(fileName).getHtml(), ContentMode.HTML);
        Label fileLbl = new Label(fileName);
        fileAttachmentLayout.with(fileIcon, fileLbl, removeBtn).withAlign(fileLbl, Alignment.MIDDLE_CENTER);
        this.addComponent(fileAttachmentLayout);
    }

    public void removeAllAttachmentsDisplay() {
        this.removeAllComponents();
        if (fileStores != null) {
            fileStores.clear();
        }
    }

    public void getAttachments(String attachmentPath) {
        List<Content> attachments = resourceService.getContents(attachmentPath);
        if (CollectionUtils.isNotEmpty(attachments)) {
            for (final Content attachment : attachments) {
                this.addComponent(AttachmentDisplayComponent
                        .constructAttachmentRow(attachment));
            }
        }
    }

    public void saveContentsToRepo(String attachmentPath) {
        if (MapUtils.isNotEmpty(fileStores)) {
            for (String fileName : fileStores.keySet()) {
                try {
                    String fileExt = "";
                    int index = fileName.lastIndexOf(".");
                    if (index > 0) {
                        fileExt = fileName.substring(index + 1,
                                fileName.length());
                    }

                    if ("jpg".equalsIgnoreCase(fileExt)
                            || "png".equalsIgnoreCase(fileExt)) {
                        try {
                            BufferedImage bufferedImage = ImageIO
                                    .read(fileStores.get(fileName));

                            int imgHeight = bufferedImage.getHeight();
                            int imgWidth = bufferedImage.getWidth();

                            float scale;
                            float destWidth = 974;
                            float destHeight = 718;

                            float scaleX = Math.min(destHeight / imgHeight, 1);
                            float scaleY = Math.min(destWidth / imgWidth, 1);
                            scale = Math.min(scaleX, scaleY);
                            BufferedImage scaledImage = ImageUtil.scaleImage(bufferedImage,
                                    scale);

                            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                            ImageIO.write(scaledImage, fileExt, outStream);

                            resourceService.saveContent(
                                    constructContent(fileName, attachmentPath),
                                    AppContext.getUsername(),
                                    new ByteArrayInputStream(outStream
                                            .toByteArray()), AppContext
                                            .getAccountId());
                        } catch (IOException e) {
                            LOG.error("Error in upload file", e);
                            resourceService.saveContent(
                                    constructContent(fileName, attachmentPath),
                                    AppContext.getUsername(),
                                    new FileInputStream(fileStores
                                            .get(fileName)), AppContext
                                            .getAccountId());
                        }
                    } else {
                        resourceService.saveContent(
                                constructContent(fileName, attachmentPath),
                                AppContext.getUsername(), new FileInputStream(
                                        fileStores.get(fileName)), AppContext
                                        .getAccountId());
                    }

                } catch (FileNotFoundException e) {
                    LOG.error("Error when attach content in UI", e);
                }
            }
        }
    }

    private Content constructContent(String fileName, String path) {
        Content content = new Content(path + "/" + fileName);
        content.setTitle(fileName);
        content.setDescription("");
        return content;
    }

    public List<File> getListFile() {
        List<File> listFile = null;
        if (MapUtils.isNotEmpty(fileStores)) {
            listFile = new ArrayList<>();
            for (String fileName : fileStores.keySet()) {
                File oldFile = fileStores.get(fileName);
                File parentFile = oldFile.getParentFile();
                File newFile = new File(parentFile, fileName);
                if (newFile.exists())
                    newFile.delete();
                if (oldFile.renameTo(newFile)) {
                    listFile.add(newFile);
                }

                if (listFile.size() <= 0)
                    return null;

            }
        }
        return listFile;
    }

    @Override
    public void receiveFile(File file, String fileName, String mimeType,
                            long length) {
        if (fileStores == null) {
            fileStores = new HashMap<>();
        }
        if (fileStores.containsKey(fileName)) {
            NotificationUtil.showWarningNotification("File " + fileName
                    + " is already existed.");
        } else {
            LOG.debug("Store file " + fileName + " in path "
                    + file.getAbsolutePath() + " is exist: " + file.exists());
            fileStores.put(fileName, file);
            displayFileName(fileName);
        }
    }

    private static FontAwesome getFileIconResource(String fileName) {
        String mimeType = MimeTypesUtil.detectMimeType(fileName);
        if (MimeTypesUtil.isImage(mimeType)) {
            return FontAwesome.FILE_IMAGE_O;
        } else if (MimeTypesUtil.isAudio(mimeType)) {
            return FontAwesome.FILE_AUDIO_O;
        } else if (MimeTypesUtil.isVideo(mimeType)) {
            return FontAwesome.FILE_VIDEO_O;
        } else if (MimeTypesUtil.isText(mimeType)) {
            return FontAwesome.FILE_TEXT_O;
        }

        return FontAwesome.FILE_O;
    }
}
