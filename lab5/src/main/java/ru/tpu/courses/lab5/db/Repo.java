package ru.tpu.courses.lab5.db;

import android.os.Parcel;
import android.os.Parcelable;

public class Repo implements Parcelable {
	public String fullName;
	public String url;

	public String getUrl() {
		return url;
	}

	public String getName() {
		return fullName;
	}
	@Override
	public String toString() {
		return "Repo{" +
				"fullName='" + fullName + '\'' +
				", url='" + url + '\'' +
				'}';
	}

	public Repo(){}
	protected Repo(Parcel in) {
		fullName = in.readString();
		url = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(fullName);
		dest.writeString(url);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<Repo> CREATOR = new Parcelable.Creator<Repo>() {
		@Override
		public Repo createFromParcel(Parcel in) {
			return new Repo(in);
		}

		@Override
		public Repo[] newArray(int size) {
			return new Repo[size];
		}
	};
}
