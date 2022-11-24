import numpy as np

def orthonormalization(*args):
    arr = []
    for i, arg in enumerate(args):
        if i == 0:
            u = arg / np.linalg.norm(arg)
        elif i == 1:
            arg_hat = ((arg@arr[0])/(arr[0]@arr[0])) * arr[0]
            ortho_arg = arg - arg_hat
            u = ortho_arg / np.linalg.norm(ortho_arg)
        else:
            for v in arr:
                arg_hat += ((arg@v)/(v@v)) * v
            ortho_arg = arg - arg_hat
            u = ortho_arg / np.linalg.norm(ortho_arg)
        arr.append(u)
        arg_hat = 0
    return arr
    
# 랜덤한 개수의 벡터들이 orthonormalization되는 함수
# 사용방법: 무작위한 개수의 벡터들을 인자로 넣어준다.
# 반환: orthonormalize된 벡터들을 반환한다.

# 증명1
# 결과 벡터들을 서로 내적하여 값이 0이 나오는지 확인한다.

# 증명2
# QR decompostion으로 벡터들을 stack해서 matrix값이 Q matrix와 일치하는지 확인한다.

# 증명3
'''
v를 stack하여 matrix V로 만들고 b를 u[i]라고 생각한다.
그리고 Ax=b 꼴로 하여 해가 있는지 없는지 확인한다.
반대로 matrix U를 만들어 b를 v[i]로 하여 확인한다.

해가 있으면 span이 되는 것이다.
'''
