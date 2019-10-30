
#import "RNUdesk.h"
#import "Udesk.h"
@implementation RNUdesk

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}
RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(initUdeskManager:(NSString *)domain appKey:(NSString *)appKey appId:(NSString *)appId customer:(NSDictionary *)usertInfo) {
    
    //初始化公司（appKey、appID、domain都是必传字段）
    UdeskOrganization *organization = [[UdeskOrganization alloc] initWithDomain:domain appKey:appKey appId:appId];
    
    //注意sdktoken 是客户的唯一标识，用来识别身份,是你们生成传入给我们的。
    //sdk_token: 传入的字符请使用 字母 / 数字 等常见字符集 。就如同身份证一样，不允许出现一个身份证号对应多个人，或者一个人有多个身份证号;其次如果给顾客设置了邮箱和手机号码，也要保证不同顾客对应的手机号和邮箱不一样，如出现相同的，则不会创建新顾客。
    UdeskCustomer *customer = [UdeskCustomer new];
    UdeskCustomerCustomField *textField = [UdeskCustomerCustomField new];
    textField.fieldKey = @"TextField_84621";
    textField.fieldValue = usertInfo[@"id"];
    customer.customField = @[textField];
    customer.sdkToken = usertInfo[@"id"];
    customer.nickName = usertInfo[@"nickname"];
    //初始化sdk
    [UdeskManager initWithOrganization:organization customer:customer];
}

RCT_EXPORT_METHOD(updateCustomer:(NSDictionary *)usertInfo) {
    UdeskCustomer *customer = [UdeskCustomer new];
    //客户自定义字段（非必填）
    UdeskCustomerCustomField *textField = [UdeskCustomerCustomField new];
    textField.fieldKey = @"TextField_84621";
    textField.fieldValue = usertInfo[@"id"];
    customer.customField = @[textField];
    customer.sdkToken = usertInfo[@"id"];
    customer.nickName = usertInfo[@"nickname"];
    [UdeskManager updateCustomer:customer completion:nil];
}

RCT_EXPORT_METHOD(entryChat) {
    //使用push
    UdeskSDKManager *chat = [[UdeskSDKManager alloc] initWithSDKStyle:[UdeskSDKStyle defaultStyle]];
    
    //使用present
    [chat presentUdeskInViewController: [UIApplication sharedApplication].keyWindow.rootViewController completion:nil];
}


@end

