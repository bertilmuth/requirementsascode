package org.requirementsascode;

class UserActor extends Actor{
	public UserActor() {
		super("User");
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserActor other = (UserActor) obj;
		if (getName() == null) {
			if (other.getName() != null)
				return false;
		} else if (!getName().equals(other.getName()))
			return false;
		return true;
	}
	
  @Override
  public int hashCode() {
    return getName().hashCode();
  }
}
