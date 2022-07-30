# Cấu trúc dự án

>## main\java\vn\tpsc\it4u

>### \config

  Chứa các config của ứng dụng

>### \controllers

  Chứa các controller (các class nhận và phản hồi request từ client)

>### \enums

  Định nghĩa các enum trong project

>### \exceptions

  Xử lý exception

>### \models

  Chứa các model (tương ứng với các table trong CSDL)

>### \payloads

  Chứa các class từ client **request** lên project hoặc các **response** về cho client.
  Khi đặt tên các class phải có hậu tố ```Request``` hoặc ```Response```

>### \repository

  Chứa các repository (các class làm việc với CSDL)

>### \security

  Xử lý Authentication, Authorize

>### \services

  Xử lý logic giữa controller và repository

>### \utils
