import mongoose from "mongoose";

const userSchema = new mongoose.Schema({
  name: { type: String, required: true },
  email: { type: String, required: true, unique: true },
  password: { type: String, required: true },
  verifyotp: { type: String, default: '' },
  verifyotpExpaireAt: { type: Number, default: 0 },
  isAccountVerified: { type: Boolean, default: false },
  resetotp: { type: String, default: '' },
  resetotpExpaireAt: { type: Number, default: 0 },
});

const UserModel =
  mongoose.models.User || mongoose.model("User", userSchema);

export default UserModel;
