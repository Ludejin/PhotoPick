package com.zero.photopicklib.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jin_ on 2016/6/11
 */
public class PhotoDir {

    private String id;
    private String coverPath;
    private String name;
    private long dateAdded;
    private List<Photo> photos = new ArrayList<>();
    private boolean isSel;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhotoDir)) return false;

        PhotoDir directory = (PhotoDir) o;

        if (!id.equals(directory.id)) return false;
        return name.equals(directory.name);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public boolean isSel() {
        return isSel;
    }

    public void setSel(boolean sel) {
        isSel = sel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public ArrayList<String> getPhotoPaths() {
        ArrayList<String> paths = new ArrayList<>(photos.size());
        for (Photo photo : photos) {
            paths.add(photo.getPath());
        }
        return paths;
    }

    public void addPhoto(int id, String path) {
        photos.add(new Photo(id, path));
    }
}
