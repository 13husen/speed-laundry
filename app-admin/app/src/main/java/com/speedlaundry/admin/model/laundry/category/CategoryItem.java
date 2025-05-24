package com.speedlaundry.admin.model.laundry.category;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryItem implements Parcelable {
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("detail")
        @Expose
        private String detail;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("fee")
        @Expose
        private String fee;
        @SerializedName("unit")
        @Expose
        private String unit;
        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("deleted_at")
        @Expose
        private String deletedAt;
        @SerializedName("category_image_link")
        @Expose
        private String categoryImageLink;
        @Expose
        private String showedMenu;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getDeletedAt() {
            return deletedAt;
        }

        public void setDeletedAt(String deletedAt) {
            this.deletedAt = deletedAt;
        }

        public String getCategoryImageLink() {
            return categoryImageLink;
        }

        public void setCategoryImageLink(String categoryImageLink) {
            this.categoryImageLink = categoryImageLink;
        }

    @Override
    public String toString(){
        return
                "Category{" +
                        "id = '" + id + '\'' +
                        ",name = '" + updatedAt + '\'' +
                        ",image = '" + image + '\'' +
                        ",detail = '" + detail + '\'' +
                        ",description = '" + description + '\'' +
                        ",fee = '" + fee + '\'' +
                        ",unit = '" + unit + '\'' +
                        ",status = '" + status + '\'' +
                        ",created_at = '" + createdAt + '\'' +
                        ",updated_at = '" + updatedAt + '\'' +
                        ",deleted_at = '" + deletedAt + '\'' +
                        ",category_image_link = '" + categoryImageLink + '\'' +
                        "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeString(this.image);
        dest.writeString(this.detail);
        dest.writeString(this.description);
        dest.writeString(this.fee);
        dest.writeString(this.unit);
        dest.writeValue(this.status);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeString(this.deletedAt);
        dest.writeString(this.categoryImageLink);
    }

    public void readFromParcel(Parcel source) {
        this.id = (Integer) source.readValue(Integer.class.getClassLoader());
        this.name = source.readString();
        this.image = source.readString();
        this.detail = source.readString();
        this.description = source.readString();
        this.fee = source.readString();
        this.unit = source.readString();
        this.status = (Integer) source.readValue(Integer.class.getClassLoader());
        this.createdAt = source.readString();
        this.updatedAt = source.readString();
        this.deletedAt = source.readString();
        this.categoryImageLink = source.readString();
    }

    protected CategoryItem(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.image = in.readString();
        this.detail = in.readString();
        this.description = in.readString();
        this.fee = in.readString();
        this.unit = in.readString();
        this.status = (Integer) in.readValue(Integer.class.getClassLoader());
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.deletedAt = in.readParcelable(Object.class.getClassLoader());
        this.categoryImageLink = in.readString();
    }

    public static final Parcelable.Creator<CategoryItem> CREATOR = new Parcelable.Creator<CategoryItem>() {
        @Override
        public CategoryItem createFromParcel(Parcel source) {
            return new CategoryItem(source);
        }

        @Override
        public CategoryItem[] newArray(int size) {
            return new CategoryItem[size];
        }
    };
}