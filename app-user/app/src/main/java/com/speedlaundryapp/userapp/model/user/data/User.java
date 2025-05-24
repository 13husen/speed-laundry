package com.speedlaundryapp.userapp.model.user.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User
{
	@SerializedName("id")
	@Expose
	private Integer id;
	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("email")
	@Expose
	private String email;
	@SerializedName("email_verified_at")
	@Expose
	private String emailVerifiedAt;
	@SerializedName("type")
	@Expose
	private String type;
	@SerializedName("status")
	@Expose
	private Integer status;
	@SerializedName("balance")
	@Expose
	private String balance;
	@SerializedName("user_detail_id")
	@Expose
	private Integer userDetailId;
	@SerializedName("deleted_at")
	@Expose
	private String deletedAt;
	@SerializedName("created_at")
	@Expose
	private String createdAt;
	@SerializedName("updated_at")
	@Expose
	private String updatedAt;
	@SerializedName("user_detail")
	@Expose
	private UserDetail userDetail;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmailVerifiedAt() {
		return emailVerifiedAt;
	}

	public void setEmailVerifiedAt(String emailVerifiedAt) {
		this.emailVerifiedAt = emailVerifiedAt;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public Integer getUserDetailId() {
		return userDetailId;
	}

	public void setUserDetailId(Integer userDetailId) {
		this.userDetailId = userDetailId;
	}

	public String getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(String deletedAt) {
		this.deletedAt = deletedAt;
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

	public UserDetail getUserDetail() {
		return userDetail;
	}

	public void setUserDetail(UserDetail userDetail) {
		this.userDetail = userDetail;
	}

//	@SerializedName("id")
//	@Expose
//	private Integer id;
//	@SerializedName("name")
//	@Expose
//	private String name;
//	@SerializedName("email")
//	@Expose
//	private String email;
//	@SerializedName("phone")
//	@Expose
//	private String phone;
//	@SerializedName("facebook_id")
//	@Expose
//	private String facebookId;
//	@SerializedName("balance")
//	@Expose
//	private String balance;
//	@SerializedName("token_count")
//	@Expose
//	private Integer tokenCount;
//	@SerializedName("ticket_count")
//	@Expose
//	private Integer ticketCount;
//	@SerializedName("subscription_expires_at")
//	@Expose
//	private Object subscriptionExpiresAt;
//	@SerializedName("allow_notification")
//	@Expose
//	private Integer allowNotification;
//	@SerializedName("locale")
//	@Expose
//	private Object locale;
//	@SerializedName("status")
//	@Expose
//	private Integer status;
//	@SerializedName("created_at")
//	@Expose
//	private String createdAt;
//	@SerializedName("updated_at")
//	@Expose
//	private String updatedAt;
//	@SerializedName("deleted_at")
//	@Expose
//	private Object deletedAt;
//	@SerializedName("display_name")
//	@Expose
//	private String displayName;
//	@SerializedName("avatar_url")
//	@Expose
//	private String avatarUrl = "";
//	@SerializedName("permissions")
//	@Expose
//	private List<String> permissions = new ArrayList<>();
//	@SerializedName("wallet")
//	@Expose
//	private Wallet wallet;
//	public final static Parcelable.Creator<User> CREATOR = new Creator<User>() {
//
//
//		@SuppressWarnings({
//				"unchecked"
//		})
//		public User createFromParcel(Parcel in) {
//			return new User(in);
//		}
//
//		public User[] newArray(int size) {
//			return (new User[size]);
//		}
//
//	}

//	protected User(Parcel in) {
//		this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
//		this.name = ((String) in.readValue((String.class.getClassLoader())));
//		this.email = ((String) in.readValue((String.class.getClassLoader())));
//		this.phone = ((String) in.readValue((String.class.getClassLoader())));
//		this.facebookId = ((String) in.readValue((String.class.getClassLoader())));
//		this.balance = ((String) in.readValue((String.class.getClassLoader())));
//		this.tokenCount = ((Integer) in.readValue((Integer.class.getClassLoader())));
//		this.ticketCount = ((Integer) in.readValue((Integer.class.getClassLoader())));
//		this.subscriptionExpiresAt = ((Object) in.readValue((Object.class.getClassLoader())));
//		this.allowNotification = ((Integer) in.readValue((Integer.class.getClassLoader())));
//		this.locale = ((Object) in.readValue((Object.class.getClassLoader())));
//		this.status = ((Integer) in.readValue((Integer.class.getClassLoader())));
//		this.createdAt = ((String) in.readValue((String.class.getClassLoader())));
//		this.updatedAt = ((String) in.readValue((String.class.getClassLoader())));
//		this.deletedAt = ((Object) in.readValue((Object.class.getClassLoader())));
//		this.displayName = ((String) in.readValue((String.class.getClassLoader())));
//		this.avatarUrl = ((String) in.readValue((String.class.getClassLoader())));
//		in.readList(this.permissions, (java.lang.String.class.getClassLoader()));
//		this.wallet = ((Wallet) in.readValue((Wallet.class.getClassLoader())));
//	}

//	public User() {
//	}
//
//	public Integer getId() {
//		return id;
//	}
//
//	public void setId(Integer id) {
//		this.id = id;
//	}
//
//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public String getEmail() {
//		return email;
//	}
//
//	public void setEmail(String email) {
//		this.email = email;
//	}
//
//	public String getPhone() {
//		return phone;
//	}
//
//	public void setPhone(String phone) {
//		this.phone = phone;
//	}
//
//	public String getFacebookId() {
//		return facebookId;
//	}
//
//	public void setFacebookId(String facebookId) {
//		this.facebookId = facebookId;
//	}
//
//	public String getBalance() {
//		return balance;
//	}
//
//	public void setBalance(String balance) {
//		this.balance = balance;
//	}
//
//	public Integer getTokenCount() {
//		return tokenCount;
//	}
//
//	public void setTokenCount(Integer tokenCount) {
//		this.tokenCount = tokenCount;
//	}
//
//	public Integer getTicketCount() {
//		return ticketCount;
//	}
//
//	public void setTicketCount(Integer ticketCount) {
//		this.ticketCount = ticketCount;
//	}
//
//	public Object getSubscriptionExpiresAt() {
//		return subscriptionExpiresAt;
//	}
//
//	public void setSubscriptionExpiresAt(Object subscriptionExpiresAt) {
//		this.subscriptionExpiresAt = subscriptionExpiresAt;
//	}
//
//	public Integer getAllowNotification() {
//		return allowNotification;
//	}
//
//	public void setAllowNotification(Integer allowNotification) {
//		this.allowNotification = allowNotification;
//	}
//
//	public Object getLocale() {
//		return locale;
//	}
//
//	public void setLocale(Object locale) {
//		this.locale = locale;
//	}
//
//	public Integer getStatus() {
//		return status;
//	}
//
//	public void setStatus(Integer status) {
//		this.status = status;
//	}
//
//	public String getCreatedAt() {
//		return createdAt;
//	}
//
//	public void setCreatedAt(String createdAt) {
//		this.createdAt = createdAt;
//	}
//
//	public String getUpdatedAt() {
//		return updatedAt;
//	}
//
//	public void setUpdatedAt(String updatedAt) {
//		this.updatedAt = updatedAt;
//	}
//
//	public Object getDeletedAt() {
//		return deletedAt;
//	}
//
//	public void setDeletedAt(Object deletedAt) {
//		this.deletedAt = deletedAt;
//	}
//
//	public String getDisplayName() {
//		return displayName;
//	}
//
//	public void setDisplayName(String displayName) {
//		this.displayName = displayName;
//	}
//
//	public String getAvatarUrl() {
//		return avatarUrl;
//	}
//
//	public void setAvatarUrl(String avatarUrl) {
//		this.avatarUrl = avatarUrl;
//	}
//
//	public List<String> getPermissions() {
//		return permissions;
//	}
//
//	public void setPermissions(List<String> permissions) {
//		this.permissions = permissions;
//	}
//
//	public Wallet getWallet() {
//		return wallet;
//	}
//
//	public void setWallet(Wallet wallet) {
//		this.wallet = wallet;
//	}
//
//	public void writeToParcel(Parcel dest, int flags) {
//		dest.writeValue(id);
//		dest.writeValue(name);
//		dest.writeValue(email);
//		dest.writeValue(phone);
//		dest.writeValue(facebookId);
//		dest.writeValue(balance);
//		dest.writeValue(tokenCount);
//		dest.writeValue(ticketCount);
//		dest.writeValue(subscriptionExpiresAt);
//		dest.writeValue(allowNotification);
//		dest.writeValue(locale);
//		dest.writeValue(status);
//		dest.writeValue(createdAt);
//		dest.writeValue(updatedAt);
//		dest.writeValue(deletedAt);
//		dest.writeValue(displayName);
//		dest.writeValue(avatarUrl);
//		dest.writeList(permissions);
//		dest.writeValue(wallet);
//	}
//
//	public int describeContents() {
//		return 0;
//	}

}