package ericarfs.socialmedia.entity.enums;

public enum Role {
	ADMIN(1),
	BASIC(2);

	private int roleId;

	private Role(int roleId) {
		this.roleId = roleId;
	}

	public int getRoleId() {
		return roleId;
	}

	public static Role valueOf(int roleId) {
		for (Role value : Role.values()) {
			if (value.getRoleId() == roleId) {
				return value;
			}

		}
		throw new IllegalArgumentException("Invalid Role id");
	}
}
