import UserModel from "../Models/userModel.js";
import jwt from "jsonwebtoken";

import bcrypt from "bcryptjs";

export const register = async (req, res) => {    
        const { name, email, password } = req.body;
        // Validate input
        if (!name || !email || !password) {
            return res.status(400).json({ message: "messing data" });
        }
    try {
        const existingUser = await UserModel.findOne({ email });
        if (existingUser) {
            return res.status(400).json({success: false, message: "User already exists" });
        }
        const hashedPassword = await bcrypt.hash(password, 10);
const user=new UserModel({
            name,   email, password: hashedPassword });
        await user.save();
        const token= jwt.sign({ id: user._id }, process.env.JWT_SECRET, { expiresIn: '7d' });
        res.cookie("token", token, { 
            httpOnly: true, 
            secure: process.env.NODE_ENV === "production", 
            sameSite:process.env.NODE_ENV === "production" ? "None" : "strict",
            maxAge: 7 * 24 * 60 * 60 * 1000 // 7 days
        });
         return res.json({ success: true, message: "Registration successful" });
    } catch (error) {   res.json({ success: false, message: error.message });
        console.error(error);  
    } 
}
export const login = async (req, res) => {
    const { email, password } = req.body;
    // Validate input
    if (!email || !password) {
        return res.status(400).json({success: false, message: "amil and password are required" });
    }
    try {
        const user = await UserModel.findOne({ email });
        if (!user) {
            return res.status(400).json({ success: false, message: "invalid email" });
        }
        const isMatch = await bcrypt.compare(password, user.password);
        if (!isMatch) {
            return res.status(400).json({ success: false, message: "Invalid password" });
        }
        const token = jwt.sign({ id: user._id }, process.env.JWT_SECRET, { expiresIn: '7d' });
        res.cookie("token", token, { 
            httpOnly: true, 
            secure: process.env.NODE_ENV === "production", 
            sameSite: process.env.NODE_ENV === "production" ? "None" : "strict",
            maxAge: 7 * 24 * 60 * 60 * 1000 // 7 days
        });
       return res.json({ success: true, message: "Login successful" });
    } catch (error) {
        return res.status(500).json({ success: false, message: error.message });
    } 
}
export const logout = async (req, res) => {
    try {
        res.clearCookie("token", { 
            httpOnly: true, 
            secure: process.env.NODE_ENV === "production", 
            sameSite: process.env.NODE_ENV === "production" ? "None" : "strict"
        });
        return res.json({ success: true, message: "Logged out " });
    } catch (error) {
        return res.status(500).json({ success: false, message: error.message });
    }
}

