package com.javaweb.service;

import com.javaweb.model.dto.AuthDTO.RegisterRequest;
import com.javaweb.model.dto.AuthDTO.RegisterWithOtpDTO;
import com.javaweb.model.entity.AccountEntity;
import com.javaweb.model.entity.RoleEntity;
import com.javaweb.model.entity.UserEntity;
import com.javaweb.repository.AccountRepository;
import com.javaweb.repository.RoleRepository;
import com.javaweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

class OtpData {
    private String otp;
    private long expiryTime;
    private final long sendTime;

    public OtpData(String otp, long expiryTime) {
        this.otp = otp;
        this.expiryTime = expiryTime;
        this.sendTime = System.currentTimeMillis();
    }

    public String getOtp() {
        return otp;
    }

    public long getExpiryTime() {
        return expiryTime;
    }

    public long getSendTime() {
        return sendTime;
    }
}

@Service
public class AuthService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender emailSender;

    private final ConcurrentHashMap<String, OtpData> otpStorage = new ConcurrentHashMap<>();

    private static final int OTP_MINUTES = 5;
    private static final long OTP_VALID_DURATION = OTP_MINUTES * 60 * 1000;
    private static final long RESEND_COOLDOWN = 60 * 1000;

    public void sendOtpEmail(String email) {
        if (accountRepository.findOneByEmail(email).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email này đã được đăng ký!");
        }

        OtpData existingOtp = otpStorage.get(email);

        if (existingOtp != null) {
            long timeSinceLastSend = System.currentTimeMillis() - existingOtp.getSendTime();

            if (timeSinceLastSend < RESEND_COOLDOWN) {
                long secondsLeft = (RESEND_COOLDOWN - timeSinceLastSend) / 1000;
                throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                        "Vui lòng chờ " + secondsLeft + " giây trước khi lấy mã mới.");
            }
        }

        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        long expiryTime = System.currentTimeMillis() + OTP_VALID_DURATION;
        otpStorage.put(email, new OtpData(otp, expiryTime));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("nguyenthanhhai03112003@gmail.com");
        message.setTo(email);
        message.setSubject("OTP Xác thực đăng ký");
        String content = "Xin chào,\n\n"
                + "Mã OTP xác thực của bạn là: " + otp + "\n\n"
                + "Mã này sẽ hết hạn trong vòng " + OTP_MINUTES + " phút.\n"
                + "Vui lòng không chia sẻ mã này cho bất kỳ ai.";

        message.setText(content);
        emailSender.send(message);
    }

    @Transactional
    public void registerWithOtp(RegisterWithOtpDTO request) {
        if (accountRepository.findOneByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email đã tồn tại!");
        }

        OtpData storedData = otpStorage.get(request.getEmail());

        if (storedData == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mã OTP không tồn tại hoặc đã hết hạn!");
        }

        if (System.currentTimeMillis() > storedData.getExpiryTime()) {
            otpStorage.remove(request.getEmail()); // Xóa OTP cũ đi cho sạch
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mã OTP đã hết hạn! Vui lòng lấy mã mới.");
        }

        if (!storedData.getOtp().equals(request.getOtpCode())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mã OTP không chính xác!");
        }

        AccountEntity newAccount = new AccountEntity();
        newAccount.setEmail(request.getEmail());
        newAccount.setPassword(passwordEncoder.encode(request.getPassword()));
        newAccount.setActive(true);
        RoleEntity roleCustomer = roleRepository.findByName("CUSTOMER")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Role not found"));
        newAccount.setRole(roleCustomer);
        newAccount = accountRepository.save(newAccount);
        Optional<UserEntity> existingUserOpt = userRepository.findOneByPhone(request.getPhone());
        UserEntity user;

        if (existingUserOpt.isPresent()) {
            user = existingUserOpt.get();
            if (user.getAccount() != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SĐT này đã được liên kết với một tài khoản khác!");
            }
            user.setAccount(newAccount);
            user.setName(request.getName());
            user.setIdentification(request.getIdentification());
            user.setGender(request.getGender());
            user.setAddress(request.getAddress());
            user.setDob(request.getDob());
        } else {
            user = new UserEntity();
            user.setName(request.getName());
            user.setPhone(request.getPhone());
            user.setIdentification(request.getIdentification());
            user.setGender(request.getGender());
            user.setAddress(request.getAddress());
            user.setDob(request.getDob());
            user.setAccount(newAccount);
        }
        userRepository.save(user);
        otpStorage.remove(request.getEmail());
    }

    public String register(RegisterRequest request) {

        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email không được để trống!");
        }
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mật khẩu không được để trống!");
        }
        if (request.getRoleName() == null || request.getRoleName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên role không được để trống!");
        }

        // 1 Kiểm tra email đã tồn tại chưa
        if (accountRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email đã tồn tại trong hệ thống!");
        }

        // 2️ Tìm role theo tên (CUSTOMER / STAFF / ADMIN)
        RoleEntity role = roleRepository.findByName(request.getRoleName().toUpperCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Role '" + request.getRoleName() + "' không tồn tại!"));

        // 3️ Tạo account
        AccountEntity account = new AccountEntity();
        account.setEmail(request.getEmail());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setActive(true);
        account.setRole(role);

        accountRepository.save(account);

        // 4️ Tạo user (thông tin cá nhân)
        UserEntity user = new UserEntity();
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setGender(request.getGender());
        user.setAccount(account);
        user.setDob(request.getDob());
        user.setIdentification(request.getIdentification());

        userRepository.save(user);

        return "Đăng ký thành công!";
    }
}
